/*Plug in about/settings for SDK Manager 2.

Refactor SDK Updater pages.

Change-Id:I84357e34c66e8976843cbfb4a5413ac28b899279*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 5ac5f4c..d3e3235 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;
//Synthetic comment -- @@ -315,8 +316,8 @@
null /* parentShell */,
errorLogger,
mOsSdkFolder);
            window.registerPage(SettingsPage.class, UpdaterPage.Purpose.SETTINGS);
            window.registerPage(AboutPage.class,    UpdaterPage.Purpose.ABOUT_BOX);
if (autoUpdate) {
window.setInitialPage(PackagesPage.class);
window.setRequestAutoUpdate(true);








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 4c72e1e..ac4a1ea 100755

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkmanager.*;
import com.android.sdkuilib.internal.repository.UpdaterPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -36,7 +38,7 @@
import java.io.InputStream;
import java.util.Properties;

public class AboutPage extends UpdaterPage {

private Label mLabel;

//Synthetic comment -- @@ -44,14 +46,19 @@
* Create the composite.
* @param parent The parent of the composite.
*/
    public AboutPage(Composite parent, int swtStyle) {
        super(parent, swtStyle);

createContents(this);

postCreate();  //$hide$
}

    @Override
    public String getPageTitle() {
        return "About";
    }

private void createContents(Composite parent) {
parent.setLayout(new GridLayout(2, false));









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java
//Synthetic comment -- index eac7515..3a92870 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkmanager.internal.repository;

import com.android.sdkuilib.internal.repository.ISettingsPage;
import com.android.sdkuilib.internal.repository.UpdaterPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
//Synthetic comment -- @@ -34,7 +35,7 @@
import java.util.Properties;


public class SettingsPage extends UpdaterPage implements ISettingsPage {

// data members
private SettingsChangedCallback mSettingsChangedCallback;
//Synthetic comment -- @@ -66,10 +67,20 @@
* Create the composite.
* @param parent The parent of the composite.
*/
    public SettingsPage(Composite parent, int swtStyle) {
        super(parent, swtStyle);

createContents(this);
        postCreate();  //$hide$
    }

    @Override
    public String getPageTitle() {
        return "Settings";
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

mProxySettingsGroup = new Group(this, SWT.NONE);
mProxySettingsGroup.setText("Proxy Settings");
//Synthetic comment -- @@ -118,12 +129,6 @@
mAskAdbRestartCheck.setToolTipText("When checked, the user will be asked for permission " +
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);
}

@Override
//Synthetic comment -- @@ -172,7 +177,8 @@
}

/**
     * Callback invoked when user touches one of the settings.
     * There is no "Apply" button, settings are applied immediately as they are changed.
* Notify the application that settings have changed.
*/
private void applyNewSettings() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
//Synthetic comment -- index d9d4111..7927ec6 100755

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.jface.dialogs.IInputValidator;
//Synthetic comment -- @@ -86,10 +88,11 @@
Shell shell = getShell();
shell.setMinimumSize(new Point(450, 300));
shell.setSize(450, 300);
        setWindowImage(shell);

        GridLayout glShell = new GridLayout();
        glShell.numColumns = 2;
        shell.setLayout(glShell);

mlabel = new Label(shell, SWT.NONE);
mlabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//Synthetic comment -- @@ -167,6 +170,25 @@
}

/**
     * Creates the icon of the window shell.
     *
     * @param shell The shell on which to put the icon
     */
    private void setWindowImage(Shell shell) {
        String imageName = "android_icon_16.png"; //$NON-NLS-1$
        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
        }

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                shell.setImage(imgFactory.getImageByName(imageName));
            }
        }
    }

    /**
* Adds a listener to adjust the column width when the parent is resized.
*/
private void adjustColumnsWidth(final Table table, final TableColumn column0) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java
//Synthetic comment -- index 7c78983..b2062f0 100755

//Synthetic comment -- @@ -27,7 +27,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -38,8 +38,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public AvdManagerPage(Composite parent, int swtStyle, UpdaterData updaterData) {
        super(parent, swtStyle);

mUpdaterData = updaterData;
mUpdaterData.addListeners(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index d2824ea..d7ac4aa 100755

//Synthetic comment -- @@ -45,7 +45,7 @@
/**
* Page that displays all locally installed packages from the current SDK.
*/
public class LocalPackagesPage extends UpdaterPage implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -68,8 +68,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public LocalPackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
        super(parent, swtStyle);

mUpdaterData = updaterData;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 708c2d6..b761510 100755

//Synthetic comment -- @@ -82,7 +82,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public class PackagesPage extends UpdaterPage
implements ISdkChangeListener, IPageListener {

private static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
//Synthetic comment -- @@ -150,8 +150,8 @@
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

    public PackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
        super(parent, swtStyle);

mUpdaterData = updaterData;
createContents(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 0a70162..1dc31bc 100755

//Synthetic comment -- @@ -56,7 +56,7 @@
* Page that displays remote repository & add-ons sources and let the user
* select packages for installation.
*/
public class RemotePackagesPage extends UpdaterPage implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -79,8 +79,8 @@
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    RemotePackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
        super(parent, swtStyle);

mUpdaterData = updaterData;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 009e1e5..fde9b28 100755

//Synthetic comment -- @@ -42,6 +42,7 @@

private final Properties mProperties = new Properties();

    /** The currently associated {@link ISettingsPage}. Can be null. */
private ISettingsPage mSettingsPage;

private final UpdaterData mUpdaterData;
//Synthetic comment -- @@ -135,20 +136,26 @@

/**
* Associate the given {@link ISettingsPage} with this {@link SettingsController}.
     * <p/>
* This loads the current properties into the setting page UI.
* It then associates the SettingsChanged callback with this controller.
     * <p/>
     * If the setting page given is null, it will be unlinked from controller.
     *
     * @param settingsPage An {@link ISettingsPage} to associate with the controller.
*/
public void setSettingsPage(ISettingsPage settingsPage) {
mSettingsPage = settingsPage;

        if (settingsPage != null) {
            settingsPage.loadSettings(mProperties);

            settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
                public void onSettingsChanged(ISettingsPage page) {
                    SettingsController.this.onSettingsChanged();
                }
            });
        }
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
new file mode 100755
//Synthetic comment -- index 0000000..9064f91

//Synthetic comment -- @@ -0,0 +1,83 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.ISdkLog;

import org.eclipse.swt.widgets.Composite;

import java.lang.reflect.Constructor;



/**
 * Base class for pages shown in the updater.
 */
public abstract class UpdaterPage extends Composite {

    public enum Purpose {
        /** A generic page with is neither of the other specific purposes. */
        GENERIC,
        /** A page that displays the about box for the SDK Manager. */
        ABOUT_BOX,
        /** A page that displays the settings for the SDK Manager. */
        SETTINGS
    }

    public UpdaterPage(Composite parent, int swtStyle) {
        super(parent, swtStyle);
    }

    /**
     * The title of the page. Default is null.
     * <p/>
     * Useful for SdkManager1 when it displays a list of pages using
     * a vertical page selector.
     * Default implement for SdkManager2 is to return null.
     */
    public String getPageTitle() {
        return null;
    }

    public static UpdaterPage newInstance(
            Class<? extends UpdaterPage> clazz,
            Composite parent,
            int swtStyle,
            ISdkLog log) {

        try {
            Constructor<? extends UpdaterPage> cons =
                clazz.getConstructor(new Class<?>[] { Composite.class, int.class });

            return cons.newInstance(new Object[] { parent, swtStyle });

        } catch (NoSuchMethodException e) {
            // There is no such constructor.
            log.error(e,
                    "Failed to instanciate page %1$s. Constructor args must be (Composite,int).",
                    clazz.getSimpleName());

        } catch (Exception e) {
            // Log this instead of crashing the whole app.
            log.error(e,
                    "Failed to instanciate page %1$s.",
                    clazz.getSimpleName());
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 7514dea..b148b4e 100755

//Synthetic comment -- @@ -19,10 +19,12 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -39,7 +41,6 @@
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
//Synthetic comment -- @@ -61,7 +62,7 @@
private boolean mInternalPageChange;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
/** A factory to create progress task dialogs. */
private ProgressTaskFactory mTaskFactory;
/** The initial page to display. If null or not a know class, the first page will be displayed.
//Synthetic comment -- @@ -180,14 +181,17 @@
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
//Synthetic comment -- @@ -230,10 +234,10 @@
* displayed in the window.
*/
protected void createPages() {
        mAvdManagerPage = new AvdManagerPage(mPagesRootComposite, SWT.BORDER, mUpdaterData);

        mLocalPackagePage = new LocalPackagesPage(mPagesRootComposite, SWT.BORDER, mUpdaterData);
        mRemotePackagesPage = new RemotePackagesPage(mPagesRootComposite, SWT.BORDER, mUpdaterData);

addPage(mAvdManagerPage, "Virtual devices");

//Synthetic comment -- @@ -261,7 +265,7 @@
private void setWindowImage(Shell androidSdkUpdater) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png";
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -327,6 +331,10 @@
* {@link Composite#getData()} field.
*/
protected void addPage(Composite page, String title) {
        assert title != null;
        if (title == null) {
            title = "Unknown";
        }
page.setData(title);
mPages.add(page);
if (mPageList != null) {
//Synthetic comment -- @@ -339,34 +347,20 @@
* using the constructor that takes a single {@link Composite} argument and then adds it
* to the page list.
*/
protected void addExtraPages() {
if (mExtraPages == null) {
return;
}

        for (Pair<Class<? extends UpdaterPage>, Purpose> extraPage : mExtraPages) {
            Class<? extends UpdaterPage> clazz = extraPage.getFirst();
            UpdaterPage instance = UpdaterPage.newInstance(
                    clazz,
                    mPagesRootComposite,
                    SWT.BORDER,
                    mUpdaterData.getSdkLog());
            if (instance != null) {
                addPage(instance, instance.getPageTitle());
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6b3cd66..43ec7cc 100755

//Synthetic comment -- @@ -22,13 +22,17 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -36,10 +40,13 @@
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
//Synthetic comment -- @@ -66,7 +73,7 @@
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
/** Sets whether the auto-update wizard will be shown when opening the window. */
private boolean mRequestAutoUpdate;

//Synthetic comment -- @@ -78,6 +85,7 @@
private Label mStatusText;
private ImgDisabledButton mButtonStop;
private ToggleButton mButtonDetails;
    private SettingsController mSettingsController;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -141,7 +149,7 @@

private void createContents() {

        mPkgPage = new PackagesPage(mShell, SWT.NONE, mUpdaterData);
mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

Composite composite1 = new Composite(mShell, SWT.NONE);
//Synthetic comment -- @@ -259,13 +267,11 @@

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
                showRegisteredPage(Purpose.SETTINGS);
}

public void onAboutMenuSelected() {
                showRegisteredPage(Purpose.ABOUT_BOX);
}

public void printError(String format, Object... args) {
//Synthetic comment -- @@ -304,14 +310,17 @@
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
//Synthetic comment -- @@ -397,8 +406,10 @@

/**
* Creates the icon of the window shell.
     *
     * @param shell The shell on which to put the icon
*/
    private void setWindowImage(Shell shell) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
imageName = "android_icon_128.png"; //$NON-NLS-1$
//Synthetic comment -- @@ -407,7 +418,7 @@
if (mUpdaterData != null) {
ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
                shell.setImage(imgFactory.getImageByName(imageName));
}
}
}
//Synthetic comment -- @@ -445,12 +456,9 @@
* and use it to load and apply these settings.
*/
private void initializeSettings() {
        mSettingsController = mUpdaterData.getSettingsController();
        mSettingsController.loadSettings();
        mSettingsController.applySettings();
}

private void onToggleDetails() {
//Synthetic comment -- @@ -461,6 +469,26 @@
// TODO
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

// End of hiding from SWT Designer
//$hide<<$

//Synthetic comment -- @@ -583,4 +611,77 @@
return (isDisposed() || !isEnabled()) ? 1 : 0;
}
}

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
                    String.format("%1$s - %2$s", APP_NAME, content.getPageTitle()));  //$NON-NLS-1$

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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
//Synthetic comment -- index a771e53..def5de0 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.repository;

import com.android.sdkuilib.internal.repository.UpdaterPage;

import org.eclipse.swt.widgets.Composite;

public interface IUpdaterWindow {
//Synthetic comment -- @@ -28,11 +30,11 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index d49b072..0fd0db2 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl2;

//Synthetic comment -- @@ -57,11 +58,12 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java
//Synthetic comment -- index 524c7b5..f731baf 100755

//Synthetic comment -- @@ -70,11 +70,13 @@
* Create the dialog.
*
* @param parent The parent's shell
     * @param title The dialog title. Can be null.
*/
public SwtBaseDialog(Shell parent, int swtStyle, String title) {
super(parent, swtStyle);
        if (title != null) {
            setText(title);
        }
}

/**
//Synthetic comment -- @@ -110,7 +112,9 @@
mShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
mShell.setMinimumSize(new Point(450, 300));
mShell.setSize(450, 300);
        if (getText() != null) {
            mShell.setText(getText());
        }
mShell.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
saveSize();







