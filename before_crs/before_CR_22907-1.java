/*Mechanism to disable SwtMenuBar in SdkMan2

The SDK Manager 2 should not try to change the
menu bar when invoked from Eclipse. Actually it
can't because the SwtMenuBar lib isn't even present
to it crashes with a NoClassDef exception.

This CL adds the notion of "invocation context" so
that we know what is invoking the manager and we can
change the UI accordingly.

Change-Id:I606850a20fbc5f9d2d1d4fd0e16aa0bd71ef41c7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index 42739e2..ded5919 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -45,7 +46,7 @@
}

public void run(IAction action) {
        Sdk sdk = Sdk.getCurrent();
if (sdk != null) {

// Runs the updater window, directing all logs to the ADT console.
//Synthetic comment -- @@ -53,7 +54,8 @@
UpdaterWindow window = new UpdaterWindow(
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
                    sdk.getSdkLocation());

ISdkChangeListener listener = new ISdkChangeListener() {
public void onSdkLoaded() {
//Synthetic comment -- @@ -88,7 +90,6 @@
// then we want to make sure we don't get any attempt to use the SDK
// before the postInstallHook is called.

                    Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
DexWrapper dx = sdk.getDexWrapper();
dx.unload();








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 8dce9b1..ccac9b7 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.util.Pair;

import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -314,7 +315,8 @@
UpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
                    mOsSdkFolder);
window.registerPage(SettingsPage.class, UpdaterPage.Purpose.SETTINGS);
window.registerPage(AboutPage.class,    UpdaterPage.Purpose.ABOUT_BOX);
if (autoUpdate) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java
new file mode 100755
//Synthetic comment -- index 0000000..eabe603

//Synthetic comment -- @@ -0,0 +1,56 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 90534e0..7a7cc6d 100755

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -86,8 +87,14 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterWindowImpl(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
mParentShell = parentShell;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index abbb3ef..ee81897 100755

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.sdkuilib.internal.repository;


import com.android.menubar.IMenuBarCallback;
import com.android.menubar.MenuBarEnhancer;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
//Synthetic comment -- @@ -27,6 +25,7 @@
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
//Synthetic comment -- @@ -68,6 +67,7 @@

private static final String APP_NAME = "Android SDK Manager";
private final Shell mParentShell;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
//Synthetic comment -- @@ -92,9 +92,16 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterWindowImpl2(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
mParentShell = parentShell;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

//Synthetic comment -- @@ -254,8 +261,10 @@
Menu menuTools = new Menu(menuBarTools);
menuBarTools.setMenu(menuTools);

        MenuItem manageAvds = new MenuItem(menuTools, SWT.NONE);
        manageAvds.setText("Manage AVDs...");

MenuItem manageSources = new MenuItem(menuTools,
MenuAction.SHOW_ADDON_SITES.getMenuStyle());
//Synthetic comment -- @@ -264,24 +273,32 @@
mPkgPage.registerMenuAction(
MenuAction.SHOW_ADDON_SITES, manageSources);

        // TODO: When invoked from Eclipse, we actually don't want to change the menu bar.
        MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
            public void onPreferencesMenuSelected() {
                showRegisteredPage(Purpose.SETTINGS);
            }

            public void onAboutMenuSelected() {
                showRegisteredPage(Purpose.ABOUT_BOX);
            }

            public void printError(String format, Object... args) {
                if (mUpdaterData != null) {
                    // TODO: right now dump to stderr. Use sdklog later.
                    //mUpdaterData.getSdkLog().error(null, format, args);
                    System.err.printf(format, args);
}
            }
        });
}

private Image getImage(String filename) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 1f68471..f2cd67d 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -1011,7 +1012,8 @@
UpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
                mAvdManager.getSdkManager().getLocation());
window.open();
refresh(true /*reload*/); // UpdaterWindow uses its own AVD manager so this one must reload.









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 8121b5a..a8c1570 100755

//Synthetic comment -- @@ -32,22 +32,57 @@
*/
public class UpdaterWindow {

private IUpdaterWindow mWindow;

/**
* Creates a new window. Caller must call open(), which will block.
*
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterWindow(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {

// TODO right now the new PackagesPage is experimental and not enabled by default
if (System.getenv("ANDROID_SDKMAN_EXP") != null) {  //$NON-NLS-1$
            mWindow = new UpdaterWindowImpl2(parentShell, sdkLog, osSdkRoot);
} else {
            mWindow = new UpdaterWindowImpl(parentShell, sdkLog, osSdkRoot);
}
}








