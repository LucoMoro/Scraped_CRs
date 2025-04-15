/*Refactor SDK Updater pages.

Use to plug in about/settings for SDK Manager 2.

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
//Synthetic comment -- index 4c72e1e..b576627 100755

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

//Synthetic comment -- @@ -52,6 +54,11 @@
postCreate();  //$hide$
}

    @Override
    public String getPageTitle() {
        return "About";
    }

private void createContents(Composite parent) {
parent.setLayout(new GridLayout(2, false));









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java
//Synthetic comment -- index eac7515..45b83bf 100755

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
//Synthetic comment -- @@ -70,6 +71,16 @@
super(parent, SWT.BORDER);

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
//Synthetic comment -- index d9d4111..3eee9dc 100755

//Synthetic comment -- @@ -87,9 +87,9 @@
shell.setMinimumSize(new Point(450, 300));
shell.setSize(450, 300);

        GridLayout glShell = new GridLayout();
        glShell.numColumns = 2;
        shell.setLayout(glShell);

mlabel = new Label(shell, SWT.NONE);
mlabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
new file mode 100755
//Synthetic comment -- index 0000000..669c506

//Synthetic comment -- @@ -0,0 +1,104 @@
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

import org.eclipse.swt.SWT;
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

    public UpdaterPage(Composite parent) {
        this(parent, SWT.NONE);
    }

    public UpdaterPage(Composite parent, int style) {
        super(parent, style);
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
            ISdkLog log) {

        Constructor<? extends UpdaterPage> cons;
        try {
            UpdaterPage instance = null;
            try {
                // First try to find a constructor that takes only a Composite parent
                // and no style. That's because we don't have any style to give and we
                // expect the page to be responsible for the style it will give to its
                // parent composite.
                cons = clazz.getConstructor(new Class<?>[] { Composite.class });
                instance = cons.newInstance(new Object[] { parent });
            } catch (Exception e) {
                // ignore
            }

            if (instance == null) {
                // The 1-arg constructor didn't work out. Look for one that takes a parent
                // Composite and an int swtStyle.
                cons = clazz.getConstructor(new Class<?>[] { Composite.class, int.class });
                instance = cons.newInstance(new Object[] { parent, SWT.NONE });
            }

            return instance;

        } catch (NoSuchMethodException e) {
            // There is no such constructor.
            log.error(e,
                    "Failed to instanciate page %1$s. Constructor args must be (Composite parent).",
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
//Synthetic comment -- index 7514dea..904e49f 100755

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
//Synthetic comment -- @@ -339,34 +347,19 @@
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
                    mUpdaterData.getSdkLog());
            if (instance != null) {
                addPage(instance, instance.getPageTitle());
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6b3cd66..9709241 100755

//Synthetic comment -- @@ -22,13 +22,15 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.ui.SwtBaseDialog;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -66,7 +68,7 @@
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
/** Sets whether the auto-update wizard will be shown when opening the window. */
private boolean mRequestAutoUpdate;

//Synthetic comment -- @@ -259,13 +261,11 @@

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
                showRegisteredPage(Purpose.SETTINGS);
}

public void onAboutMenuSelected() {
                showRegisteredPage(Purpose.ABOUT_BOX);
}

public void printError(String format, Object... args) {
//Synthetic comment -- @@ -304,14 +304,17 @@
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
//Synthetic comment -- @@ -461,6 +464,26 @@
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
            PageDialog d = new PageDialog(mShell, clazz);
            d.open();
        }
    }

// End of hiding from SWT Designer
//$hide<<$

//Synthetic comment -- @@ -583,4 +606,38 @@
return (isDisposed() || !isEnabled()) ? 1 : 0;
}
}

    // -----

    private class PageDialog extends SwtBaseDialog {

        private final Class<? extends UpdaterPage> mPageClass;

        protected PageDialog(Shell parentShell, Class<? extends UpdaterPage> pageClass) {
            super(parentShell, SWT.APPLICATION_MODAL, null /*title*/);
            mPageClass = pageClass;
        }

        @Override
        protected void createContents() {
            Shell shell = getShell();

            GridLayout glShell = new GridLayout();
            glShell.numColumns = 2;
            shell.setLayout(glShell);

            UpdaterPage content = UpdaterPage.newInstance(
                    mPageClass,
                    shell,
                    mUpdaterData.getSdkLog());
            getShell().setText(content.getPageTitle());


        }

        @Override
        protected void postCreate() {
            // pass
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







