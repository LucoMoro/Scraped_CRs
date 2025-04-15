/*Refactor SDK Updater pages.

Use to plug in about/settings for SDK Manager 2.

Change-Id:I84357e34c66e8976843cbfb4a5413ac28b899279*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 5ac5f4c..6f621aa 100644

//Synthetic comment -- @@ -315,8 +315,8 @@
null /* parentShell */,
errorLogger,
mOsSdkFolder);
            window.registerPage("Settings", SettingsPage.class);
            window.registerPage("About", AboutPage.class);
if (autoUpdate) {
window.setInitialPage(PackagesPage.class);
window.setRequestAutoUpdate(true);








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 4c72e1e..6c6a04c 100755

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -36,7 +38,7 @@
import java.io.InputStream;
import java.util.Properties;

public class AboutPage extends Composite {

private Label mLabel;

//Synthetic comment -- @@ -52,6 +54,16 @@
postCreate();  //$hide$
}

private void createContents(Composite parent) {
parent.setLayout(new GridLayout(2, false));









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/SettingsPage.java
//Synthetic comment -- index eac7515..94b33a0 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkmanager.internal.repository;

import com.android.sdkuilib.internal.repository.ISettingsPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
//Synthetic comment -- @@ -34,7 +35,7 @@
import java.util.Properties;


public class SettingsPage extends Composite implements ISettingsPage {

// data members
private SettingsChangedCallback mSettingsChangedCallback;
//Synthetic comment -- @@ -70,6 +71,21 @@
super(parent, SWT.BORDER);

createContents(this);

mProxySettingsGroup = new Group(this, SWT.NONE);
mProxySettingsGroup.setText("Proxy Settings");
//Synthetic comment -- @@ -118,12 +134,6 @@
mAskAdbRestartCheck.setToolTipText("When checked, the user will be asked for permission " +
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);

        postCreate();  //$hide$
    }

    private void createContents(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
new file mode 100755
//Synthetic comment -- index 0000000..a51cc2b

//Synthetic comment -- @@ -0,0 +1,104 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 7514dea..b51cd03 100755

//Synthetic comment -- @@ -39,7 +39,6 @@
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -61,7 +60,7 @@
private boolean mInternalPageChange;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Object[]> mExtraPages;
/** A factory to create progress task dialogs. */
private ProgressTaskFactory mTaskFactory;
/** The initial page to display. If null or not a know class, the first page will be displayed.
//Synthetic comment -- @@ -180,14 +179,13 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Object[]>();
}
        mExtraPages.add(new Object[]{ title, pageClass });
}

/**
//Synthetic comment -- @@ -261,7 +259,7 @@
private void setWindowImage(Shell androidSdkUpdater) {
String imageName = "android_icon_16.png"; //$NON-NLS-1$
if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
}

if (mUpdaterData != null) {
//Synthetic comment -- @@ -327,6 +325,10 @@
* {@link Composite#getData()} field.
*/
protected void addPage(Composite page, String title) {
page.setData(title);
mPages.add(page);
if (mPageList != null) {
//Synthetic comment -- @@ -339,34 +341,15 @@
* using the constructor that takes a single {@link Composite} argument and then adds it
* to the page list.
*/
    @SuppressWarnings("unchecked")
protected void addExtraPages() {
if (mExtraPages == null) {
return;
}

        for (Object[] extraPage : mExtraPages) {
            String title = (String) extraPage[0];
            Class<? extends Composite> clazz = (Class<? extends Composite>) extraPage[1];

            // We want the constructor that takes a single Composite as parameter
            Constructor<? extends Composite> cons;
            try {
                cons = clazz.getConstructor(new Class<?>[] { Composite.class });
                Composite instance = cons.newInstance(new Object[] { mPagesRootComposite });
                addPage(instance, title);

            } catch (NoSuchMethodException e) {
                // There is no such constructor.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s. Constructor args must be (Composite parent).",  //$NON-NLS-1$
                        clazz.getSimpleName());

            } catch (Exception e) {
                // Log this instead of crashing the whole app.
                mUpdaterData.getSdkLog().error(e,
                        "Failed to add extra page %1$s.",  //$NON-NLS-1$
                        clazz.getSimpleName());
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6b3cd66..5aee01a 100755

//Synthetic comment -- @@ -27,8 +27,8 @@
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -50,6 +50,7 @@
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
//Synthetic comment -- @@ -66,7 +67,7 @@
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
*  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Object[]> mExtraPages;
/** Sets whether the auto-update wizard will be shown when opening the window. */
private boolean mRequestAutoUpdate;

//Synthetic comment -- @@ -259,13 +260,11 @@

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
                // TODO: plug settings page here
                MessageDialog.openInformation(mShell, "test", "on prefs");
}

public void onAboutMenuSelected() {
                // TODO: plug about page here
                MessageDialog.openInformation(mShell, "test", "on about");
}

public void printError(String format, Object... args) {
//Synthetic comment -- @@ -307,11 +306,11 @@
* @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
            mExtraPages = new ArrayList<Object[]>();
}
        mExtraPages.add(new Object[]{ title, pageClass });
}

/**
//Synthetic comment -- @@ -461,6 +460,10 @@
// TODO
}

// End of hiding from SWT Designer
//$hide<<$

//Synthetic comment -- @@ -583,4 +586,43 @@
return (isDisposed() || !isEnabled()) ? 1 : 0;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
//Synthetic comment -- index a771e53..69ed28b 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.repository;

import org.eclipse.swt.widgets.Composite;

public interface IUpdaterWindow {
//Synthetic comment -- @@ -28,11 +30,9 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public abstract void registerPage(String title,
            Class<? extends Composite> pageClass);

/**
* Indicate the initial page that should be selected when the window opens.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index d49b072..71daa9f 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl2;

//Synthetic comment -- @@ -57,11 +58,10 @@
* <p/>
* All pages must be registered before the call to {@link #open()}.
*
     * @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
        mWindow.registerPage(title, pageClass);
}

/**







