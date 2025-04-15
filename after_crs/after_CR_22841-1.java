/*Refactor SDK Updater pages.

Use to plug in about/settings for SDK Manager 2.

Change-Id:I84357e34c66e8976843cbfb4a5413ac28b899279*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 5ac5f4c..6f621aa 100644

//Synthetic comment -- @@ -315,8 +315,8 @@
null /* parentShell */,
errorLogger,
mOsSdkFolder);
            window.registerPage(SettingsPage.class);
            window.registerPage(AboutPage.class);
if (autoUpdate) {
window.setInitialPage(PackagesPage.class);
window.setRequestAutoUpdate(true);








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 4c72e1e..6c6a04c 100755

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

//Synthetic comment -- @@ -52,6 +54,16 @@
postCreate();  //$hide$
}

    @Override
    public String getPageTitle() {
        return "About";
    }

    @Override
    public int getPagePurpose() {
        return PURPOSE_ABOUT_BOX;
    }

private void createContents(Composite parent) {
parent.setLayout(new GridLayout(2, false));









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java
//Synthetic comment -- index eac7515..94b33a0 100755

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
//Synthetic comment -- @@ -70,6 +71,21 @@
super(parent, SWT.BORDER);

createContents(this);
        postCreate();  //$hide$
    }

    @Override
    public String getPageTitle() {
        return "Settings";
    }

    @Override
    public int getPagePurpose() {
        return PURPOSE_SETTINGS;
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

mProxySettingsGroup = new Group(this, SWT.NONE);
mProxySettingsGroup.setText("Proxy Settings");
//Synthetic comment -- @@ -118,12 +134,6 @@
mAskAdbRestartCheck.setToolTipText("When checked, the user will be asked for permission " +
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
new file mode 100755
//Synthetic comment -- index 0000000..a51cc2b

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

    /** A generic page with is neither of the other specific purposes. */
    public final static int PURPOSE_GENERIC  = 0;
    /** A page that displays the about box for the SDK Manager. */
    public final static int PURPOSE_ABOUT_BOX = 1;
    /** A page that displays the settings for the SDK Manager. */
    public final static int PURPOSE_SETTINGS = 2;

    public int getPagePurpose() {
        return PURPOSE_GENERIC;
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
                return cons.newInstance(new Object[] { parent, SWT.NONE });
            }

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
//Synthetic comment -- index 7514dea..b51cd03 100755

//Synthetic comment -- @@ -39,7 +39,6 @@
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
//Synthetic comment -- @@ -61,7 +60,7 @@
private boolean mInternalPageChange;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Class<? extends UpdaterPage>> mExtraPages;
/** A factory to create progress task dialogs. */
private ProgressTaskFactory mTaskFactory;
/** The initial page to display. If null or not a know class, the first page will be displayed.
//Synthetic comment -- @@ -180,14 +179,13 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(Class<? extends UpdaterPage> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Class<? extends UpdaterPage>>();
}
        mExtraPages.add(pageClass);
}

/**
//Synthetic comment -- @@ -261,7 +259,7 @@
private void setWindowImage(Shell androidSdkUpdater) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png";
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -327,6 +325,10 @@
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
//Synthetic comment -- @@ -339,34 +341,15 @@
* using the constructor that takes a single {@link Composite} argument and then adds it
* to the page list.
*/
protected void addExtraPages() {
if (mExtraPages == null) {
return;
}

        for (Class<? extends UpdaterPage> clazz : mExtraPages) {
            UpdaterPage instance = UpdaterPage.newInstance(clazz, mPagesRootComposite, mUpdaterData.getSdkLog());
            if (instance != null) {
                addPage(instance, instance.getPageTitle());
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6b3cd66..5aee01a 100755

//Synthetic comment -- @@ -27,8 +27,8 @@
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -50,6 +50,7 @@
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -66,7 +67,7 @@
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Class<? extends UpdaterPage>> mExtraPages;
/** Sets whether the auto-update wizard will be shown when opening the window. */
private boolean mRequestAutoUpdate;

//Synthetic comment -- @@ -259,13 +260,11 @@

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
                showRegisteredPage(UpdaterPage.PURPOSE_SETTINGS);
}

public void onAboutMenuSelected() {
                showRegisteredPage(UpdaterPage.PURPOSE_ABOUT_BOX);
}

public void printError(String format, Object... args) {
//Synthetic comment -- @@ -307,11 +306,11 @@
* @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(Class<? extends UpdaterPage> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Class<? extends UpdaterPage>>();
}
        mExtraPages.add(pageClass);
}

/**
//Synthetic comment -- @@ -461,6 +460,10 @@
// TODO
}

    private void showRegisteredPage(int pagePurpose) {
        // TODO
    }

// End of hiding from SWT Designer
//$hide<<$

//Synthetic comment -- @@ -583,4 +586,43 @@
return (isDisposed() || !isEnabled()) ? 1 : 0;
}
}

    // -----

    private class PageDialog extends GridDialog {

        private final Class<? extends Composite> mPageClass;

        protected PageDialog(Shell parentShell, Class<? extends Composite> pageClass) {
            super(parentShell, 1, false/*makeColumnsEqual*/);
            mPageClass = pageClass;
        }

        @Override
        protected boolean isResizable() {
            return false;
        }

        @Override
        public void createDialogContent(Composite parent) {
            // We want the constructor that takes a single Composite as parameter
            Constructor<? extends Composite> cons;
            try {
                cons = mPageClass.getConstructor(new Class<?>[] { Composite.class });
                Composite instance = cons.newInstance(new Object[] { parent });

            } catch (NoSuchMethodException e) {
                // There is no such constructor.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s. Constructor args must be (Composite parent).",  //$NON-NLS-1$
                        mPageClass.getSimpleName());

            } catch (Exception e) {
                // Log this instead of crashing the whole app.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s.",  //$NON-NLS-1$
                        mPageClass.getSimpleName());
            }
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
//Synthetic comment -- index a771e53..69ed28b 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.repository;

import com.android.sdkuilib.internal.repository.UpdaterPage;

import org.eclipse.swt.widgets.Composite;

public interface IUpdaterWindow {
//Synthetic comment -- @@ -28,11 +30,9 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public abstract void registerPage(Class<? extends UpdaterPage> pageClass);

/**
* Indicate the initial page that should be selected when the window opens.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index d49b072..71daa9f 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl2;

//Synthetic comment -- @@ -57,11 +58,10 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(Class<? extends UpdaterPage> pageClass) {
        mWindow.registerPage(pageClass);
}

/**







