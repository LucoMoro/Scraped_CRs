/*SDK Manager: fix link to AVD Manager.

The AVD Manager still had remnants of the obsolete
"pages" mechanism removed in changeI7f4f3941.
That was making it crash with an NPE when started
from the SDK Manager.

SDK Bug: 6272923

Change-Id:I1f010c016e1db0e884aea7beda23ead6f47df70e*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index ba87300..35e3420 100755

//Synthetic comment -- @@ -70,7 +70,6 @@
SdkAddonConstants.NS_LATEST_VERSION,
SdkRepoConstants.NS_LATEST_VERSION));


Label filler = new Label(shell, SWT.NONE);
GridDataBuilder.create(filler).fill().grab().hSpan(2);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java
//Synthetic comment -- index 6bf8684..e5f2521 100755

//Synthetic comment -- @@ -18,43 +18,12 @@

import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.widgets.Composite;

/**
* Interface for the actual implementation of the Update Window.
*/
public interface ISdkUpdaterWindow {

/**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     * @param purpose The purpose of this page, e.g. an about box, settings page or generic.
     */
    public abstract void registerPage(Class<? extends UpdaterPage> pageClass,
            UpdaterPage.Purpose purpose);

    /**
     * Indicate the initial page that should be selected when the window opens.
     * <p/>
     * This must be called before the call to {@link #open()}.
     * If null or if the page class is not found, the first page will be selected.
     */
    public abstract void setInitialPage(Class<? extends Composite> pageClass);

    /**
     * Sets whether the auto-update wizard will be shown when opening the window.
     * <p/>
     * This must be called before the call to {@link #open()}.
     */
    public abstract void setRequestAutoUpdate(boolean requestAutoUpdate);

    /**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
public abstract void addListener(ISdkChangeListener listener);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index f145453..a90002c 100755

//Synthetic comment -- @@ -122,6 +122,9 @@
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);

createCloseButton();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 23fde3f..8277ce2 100755

//Synthetic comment -- @@ -20,21 +20,16 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.ISettingsPage;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman1.AvdManagerPage;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -44,16 +39,11 @@
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
* This is an intermediate version of the {@link AvdManagerPage}
* wrapped in its own standalone window for use from the SDK Manager 2.
//Synthetic comment -- @@ -68,11 +58,6 @@
private final AvdInvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;
    /** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
     *  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
    /** Sets whether the auto-update wizard will be shown when opening the window. */
    private boolean mRequestAutoUpdate;

// --- UI members ---

//Synthetic comment -- @@ -227,12 +212,14 @@
new MenuBarWrapper(APP_NAME_MAC_MENU, menuTools) {
@Override
public void onPreferencesMenuSelected() {
                        showRegisteredPage(Purpose.SETTINGS);
}

@Override
public void onAboutMenuSelected() {
                        showRegisteredPage(Purpose.ABOUT_BOX);
}

@Override
//Synthetic comment -- @@ -256,46 +243,6 @@

// --- Public API -----------


    /**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     * @param purpose The purpose of this page, e.g. an about box, settings page or generic.
     */
    @SuppressWarnings("unchecked")
    public void registerPage(Class<? extends UpdaterPage> pageClass,
            Purpose purpose) {
        if (mExtraPages == null) {
            mExtraPages = new ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>>();
        }
        Pair<?, Purpose> value = Pair.of(pageClass, purpose);
        mExtraPages.add((Pair<Class<? extends UpdaterPage>, Purpose>) value);
    }

    /**
     * Indicate the initial page that should be selected when the window opens.
     * This must be called before the call to {@link #open()}.
     * If null or if the page class is not found, the first page will be selected.
     */
    public void setInitialPage(Class<? extends Composite> pageClass) {
        // Unused in this case. This window display only one page.
    }

    /**
     * Sets whether the auto-update wizard will be shown when opening the window.
     * <p/>
     * This must be called before the call to {@link #open()}.
     */
    public void setRequestAutoUpdate(boolean requestAutoUpdate) {
        mRequestAutoUpdate = requestAutoUpdate;
    }

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
//Synthetic comment -- @@ -342,15 +289,6 @@

mUpdaterData.broadcastOnSdkLoaded();

        if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    null /*selectedArchives*/,
                    false /* includeObsoletes */,
                    mContext == AvdInvocationContext.IDE ?
                            UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT :
                                UpdaterData.TOOLS_MSG_UPDATED_FROM_SDKMAN);
        }

return true;
}

//Synthetic comment -- @@ -411,26 +349,6 @@
mSettingsController.applySettings();
}

    private void showRegisteredPage(Purpose purpose) {
        if (mExtraPages == null) {
            return;
        }

        Class<? extends UpdaterPage> clazz = null;

        for (Pair<Class<? extends UpdaterPage>, Purpose> extraPage : mExtraPages) {
            if (extraPage.getSecond() == purpose) {
                clazz = extraPage.getFirst();
                break;
            }
        }

        if (clazz != null) {
            PageDialog d = new PageDialog(mShell, clazz, purpose == Purpose.SETTINGS);
            d.open();
        }
    }

private void onSdkManager() {
ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

//Synthetic comment -- @@ -440,10 +358,6 @@
mUpdaterData,
SdkUpdaterWindow.SdkInvocationContext.AVD_MANAGER);

            for (Pair<Class<? extends UpdaterPage>, Purpose> page : mExtraPages) {
                win.registerPage(page.getFirst(), page.getSecond());
            }

win.open();
} catch (Exception e) {
mUpdaterData.getSdkLog().error(e, "SDK Manager window error");
//Synthetic comment -- @@ -451,80 +365,4 @@
mUpdaterData.setTaskFactory(oldFactory);
}
}

    // End of hiding from SWT Designer
    //$hide<<$

    // -----

    /**
     * Dialog used to display either the About page or the Settings (aka Options) page
     * with a "close" button.
     */
    private class PageDialog extends SwtBaseDialog {

        private final Class<? extends UpdaterPage> mPageClass;
        private final boolean mIsSettingsPage;

        protected PageDialog(
                Shell parentShell,
                Class<? extends UpdaterPage> pageClass,
                boolean isSettingsPage) {
            super(parentShell, SWT.APPLICATION_MODAL, null /*title*/);
            mPageClass = pageClass;
            mIsSettingsPage = isSettingsPage;
        }

        @Override
        protected void createContents() {
            Shell shell = getShell();
            setWindowImage(shell);

            GridLayoutBuilder.create(shell).columns(2);

            UpdaterPage content = UpdaterPage.newInstance(
                    mPageClass,
                    shell,
                    SWT.NONE,
                    mUpdaterData.getSdkLog());
            GridDataBuilder.create(content).fill().grab().hSpan(2);
            if (content.getLayout() instanceof GridLayout) {
                GridLayout gl = (GridLayout) content.getLayout();
                gl.marginHeight = gl.marginWidth = 0;
            }

            if (mIsSettingsPage && content instanceof ISettingsPage) {
                mSettingsController.setSettingsPage((ISettingsPage) content);
            }

            getShell().setText(
                    String.format("%1$s - %2$s", APP_NAME, content.getPageTitle()));

            Label filler = new Label(shell, SWT.NONE);
            GridDataBuilder.create(filler).hFill().hGrab();

            Button close = new Button(shell, SWT.PUSH);
            close.setText("Close");
            GridDataBuilder.create(close);
            close.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    close();
                }
            });
        }

        @Override
        protected void postCreate() {
            // pass
        }

        @Override
        protected void close() {
            if (mIsSettingsPage) {
                mSettingsController.setSettingsPage(null);
            }
            super.close();
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 135c6bd..e5f26cd 100755

//Synthetic comment -- @@ -26,8 +26,6 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;
//Synthetic comment -- @@ -38,7 +36,6 @@
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -59,8 +56,6 @@
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
* This is the private implementation of the UpdateWindow
* for the second version of the SDK Manager.
//Synthetic comment -- @@ -76,11 +71,6 @@
private final SdkInvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;
    /** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
     *  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
    /** Sets whether the auto-update wizard will be shown when opening the window. */
    private boolean mRequestAutoUpdate;

// --- UI members ---

//Synthetic comment -- @@ -391,49 +381,6 @@

// --- Public API -----------


    /**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     * @param purpose The purpose of this page, e.g. an about box, settings page or generic.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void registerPage(Class<? extends UpdaterPage> pageClass,
            Purpose purpose) {
        if (mExtraPages == null) {
            mExtraPages = new ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>>();
        }
        Pair<?, Purpose> value = Pair.of(pageClass, purpose);
        mExtraPages.add((Pair<Class<? extends UpdaterPage>, Purpose>) value);
    }

    /**
     * Indicate the initial page that should be selected when the window opens.
     * This must be called before the call to {@link #open()}.
     * If null or if the page class is not found, the first page will be selected.
     */
    @Override
    public void setInitialPage(Class<? extends Composite> pageClass) {
        // Unused in this case. This window display only one page.
    }

    /**
     * Sets whether the auto-update wizard will be shown when opening the window.
     * <p/>
     * This must be called before the call to {@link #open()}.
     */
    @Override
    public void setRequestAutoUpdate(boolean requestAutoUpdate) {
        mRequestAutoUpdate = requestAutoUpdate;
    }

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
//Synthetic comment -- @@ -532,15 +479,6 @@

mUpdaterData.broadcastOnSdkLoaded();

        if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    null /*selectedArchives*/,
                    false /* includeObsoletes */,
                    mContext == SdkInvocationContext.IDE ?
                            UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT :
                                UpdaterData.TOOLS_MSG_UPDATED_FROM_SDKMAN);
        }

// Tell the one page its the selected one
mPkgPage.onPageSelected();

//Synthetic comment -- @@ -626,10 +564,6 @@
mUpdaterData,
AvdInvocationContext.SDK_MANAGER);

            for (Pair<Class<? extends UpdaterPage>, Purpose> page : mExtraPages) {
                win.registerPage(page.getFirst(), page.getSecond());
            }

win.open();
} catch (Exception e) {
mUpdaterData.getSdkLog().error(e, "AVD Manager window error");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java
//Synthetic comment -- index 73be6a2..5c601e6 100755

//Synthetic comment -- @@ -17,10 +17,8 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.sdkman2.AvdManagerWindowImpl1;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
//Synthetic comment -- @@ -88,22 +86,6 @@
}

/**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     * @param purpose The purpose of this page, e.g. an about box, settings page or generic.
     */
    public void registerPage(Class<? extends UpdaterPage> pageClass,
            UpdaterPage.Purpose purpose) {
        mWindow.registerPage(pageClass, purpose);
    }

    /**
* Opens the window.
*/
public void open() {







