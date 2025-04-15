/*ADT: run external SDK Manager.

When the user invokes the SdkManagerAction (via the icon toolbar)
this now runs the external SDK Manager (instead of the direct
Java version).

If the SDK isn't set properly, this triggers the SDK check
in AdtPlugin. This check tries to display some help to setup
the SDK -- typically openeing preferences. If for some
reason this check where to suggest to run the sdk manager
there's a fallback to using the internal version.

Also when starting the sdk manager as an external app on
windows there's a 2-3 seconds delay before the SDK Manager
window happen. To give some feedback to the user that the
action is on-going a jface progress dialog is shown that
will automatically close 3 seconds later.

Change-Id:I1aae5dbc5ede6299fc95f81d8c3a94288861d55c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index a2afc89..faa5981 100644

//Synthetic comment -- @@ -160,7 +160,7 @@
private Color mRed;

/** Load status of the SDK. Any access MUST be in a synchronized(mPostLoadProjects) block */
    private LoadStatus mSdkIsLoaded = LoadStatus.LOADING;
/** Project to update once the SDK is loaded.
* Any access MUST be in a synchronized(mPostLoadProjectsToResolve) block */
private final ArrayList<IJavaProject> mPostLoadProjectsToResolve =
//Synthetic comment -- @@ -173,7 +173,12 @@
private ArrayList<ITargetChangeListener> mTargetChangeListeners =
new ArrayList<ITargetChangeListener>();

    protected boolean mSdkIsLoading;

/**
* An error handler for checkSdkLocationAndId() that will handle the generated error
//Synthetic comment -- @@ -189,12 +194,14 @@
OPEN_P2_UPDATE
}

        /** Handle an error message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleError(Solution solution, String message);

        /** Handle a warning message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleWarning(Solution solution, String message);
//Synthetic comment -- @@ -1079,7 +1086,7 @@
*/
public final LoadStatus getSdkLoadStatus() {
synchronized (Sdk.getLock()) {
            return mSdkIsLoaded;
}
}

//Synthetic comment -- @@ -1106,24 +1113,36 @@
}

/**
     * Checks the location of the SDK is valid and if it is, grab the SDK API version
     * from the SDK.
     * @return false if the location is not correct.
*/
    private boolean checkSdkLocationAndId() {
String sdkLocation = AdtPrefs.getPrefs().getOsSdkFolder();
        if (sdkLocation == null || sdkLocation.length() == 0) {
            return false;
        }

return checkSdkLocationAndId(sdkLocation, new CheckSdkErrorHandler() {
            private String mTitle = "Android SDK Verification";
@Override
public boolean handleError(Solution solution, String message) {
displayMessage(solution, message, MessageDialog.ERROR);
return false;
}

@Override
public boolean handleWarning(Solution solution, String message) {
displayMessage(solution, message, MessageDialog.WARNING);
//Synthetic comment -- @@ -1190,6 +1209,8 @@
private void openSdkManager() {
// Windows only: open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
if (SdkManagerAction.openExternalSdkManager()) {
return;
//Synthetic comment -- @@ -1249,13 +1270,24 @@

/**
* Internal helper to perform the actual sdk location and id check.
*
     * @param osSdkLocation The sdk directory, an OS path.
* @param errorHandler An checkSdkErrorHandler that can display a warning or an error.
* @return False if there was an error or the result from the errorHandler invocation.
*/
    public boolean checkSdkLocationAndId(String osSdkLocation, CheckSdkErrorHandler errorHandler) {
        if (osSdkLocation.endsWith(File.separator) == false) {
osSdkLocation = osSdkLocation + File.separator;
}

//Synthetic comment -- @@ -1355,12 +1387,12 @@
protected IStatus run(IProgressMonitor monitor) {
try {

                    if (mSdkIsLoading) {
return new Status(IStatus.WARNING, PLUGIN_ID,
"An Android SDK is already being loaded. Please try again later.");
}

                    mSdkIsLoading = true;

SubMonitor progress = SubMonitor.convert(monitor,
"Initialize SDK Manager", 100);
//Synthetic comment -- @@ -1368,10 +1400,9 @@
Sdk sdk = Sdk.loadSdk(AdtPrefs.getPrefs().getOsSdkFolder());

if (sdk != null) {

ArrayList<IJavaProject> list = new ArrayList<IJavaProject>();
synchronized (Sdk.getLock()) {
                            mSdkIsLoaded = LoadStatus.LOADED;

progress.setTaskName("Check Projects");

//Synthetic comment -- @@ -1410,7 +1441,7 @@
// SDK failed to Load!
// Sdk#loadSdk() has already displayed an error.
synchronized (Sdk.getLock()) {
                            mSdkIsLoaded = LoadStatus.FAILED;
}
}

//Synthetic comment -- @@ -1443,7 +1474,7 @@
"parseSdkContent failed", t);               //$NON-NLS-1$

} finally {
                    mSdkIsLoading = false;
if (monitor != null) {
monitor.done();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 9daa43c..c5d4713 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -30,14 +31,22 @@
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;

/**
* Delegate for the toolbar/menu action "Android SDK Manager".
//Synthetic comment -- @@ -57,63 +66,203 @@

@Override
public void run(IAction action) {
        if (!openAdtSdkManager()) {
            AdtPlugin.displayError(
                    "Android SDK",
                    "Location of the Android SDK has not been setup in the preferences.");
}
}

/**
* Opens the SDK Manager as an external application.
* This call is asynchronous, it doesn't wait for the manager to be closed.
*
* @return True if the application was found and executed. False if it could not
*   be located or could not be launched.
*/
public static boolean openExternalSdkManager() {
        final Sdk sdk = Sdk.getCurrent();
        if (sdk == null) {
            return false;
        }

        File androidBat = FileOp.append(
                sdk.getSdkLocation(),
                SdkConstants.FD_TOOLS,
                SdkConstants.androidCmdName());

        if (!androidBat.exists()) {
            return false;
        }

try {
            final AdtConsoleSdkLog logger = new AdtConsoleSdkLog();

            String command[] = new String[] {
                    androidBat.getAbsolutePath(),
                    "sdk"   //$NON-NLS-1$
            };
            Process process = Runtime.getRuntime().exec(command);
            GrabProcessOutput.grabProcessOutput(
                    process,
                    Wait.ASYNC,
                    new IProcessOutput() {
                        @Override
                        public void out(@Nullable String line) {
                            // Ignore stdout
                        }

                        @Override
                        public void err(@Nullable String line) {
                            if (line != null) {
                                logger.printf("[SDK Manager] %s", line);
}
}
                    });
        } catch (Exception ignore) {
}

        return true;
}

/**







