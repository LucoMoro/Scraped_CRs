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
    private LoadStatus mSdkLoadedStatus = LoadStatus.LOADING;
/** Project to update once the SDK is loaded.
* Any access MUST be in a synchronized(mPostLoadProjectsToResolve) block */
private final ArrayList<IJavaProject> mPostLoadProjectsToResolve =
//Synthetic comment -- @@ -173,7 +173,12 @@
private ArrayList<ITargetChangeListener> mTargetChangeListeners =
new ArrayList<ITargetChangeListener>();

    /**
     * This variable indicates that the job inside parseSdkContent() is currently
     * trying to load the SDK, to avoid re-entrance.
     * To check whether this succeeds or not, please see {@link #getSdkLoadStatus()}.
     */
    private volatile boolean mParseSdkContentIsRunning;

/**
* An error handler for checkSdkLocationAndId() that will handle the generated error
//Synthetic comment -- @@ -189,12 +194,14 @@
OPEN_P2_UPDATE
}

        /**
         * Handle an error message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleError(Solution solution, String message);

        /**
         * Handle a warning message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleWarning(Solution solution, String message);
//Synthetic comment -- @@ -1079,7 +1086,7 @@
*/
public final LoadStatus getSdkLoadStatus() {
synchronized (Sdk.getLock()) {
            return mSdkLoadedStatus;
}
}

//Synthetic comment -- @@ -1106,24 +1113,36 @@
}

/**
     * Checks the location of the SDK in the prefs is valid.
     * If it is not, display a warning dialog to the user and try to display
     * some useful link to fix the situation (setup the preferences, perform an
     * update, etc.)
     *
     * @return True if the SDK location points to an SDK.
     *  If false, the user has already been presented with a modal dialog explaining that.
*/
    public boolean checkSdkLocationAndId() {
String sdkLocation = AdtPrefs.getPrefs().getOsSdkFolder();

return checkSdkLocationAndId(sdkLocation, new CheckSdkErrorHandler() {
            private String mTitle = "Android SDK";

            /**
             * Handle an error, which is the case where the check did not find any SDK.
             * This returns false to {@link AdtPlugin#checkSdkLocationAndId()}.
             */
@Override
public boolean handleError(Solution solution, String message) {
displayMessage(solution, message, MessageDialog.ERROR);
return false;
}

            /**
             * Handle an warning, which is the case where the check found an SDK
             * but it might need to be repaired or is missing an expected component.
             *
             * This returns true to {@link AdtPlugin#checkSdkLocationAndId()}.
             */
@Override
public boolean handleWarning(Solution solution, String message) {
displayMessage(solution, message, MessageDialog.WARNING);
//Synthetic comment -- @@ -1190,6 +1209,8 @@
private void openSdkManager() {
// Windows only: open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
                // Also when this is invoked becasue SdkManagerAction.run() fails, this
                // test will fail and we'll fallback on using the internal one.
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
if (SdkManagerAction.openExternalSdkManager()) {
return;
//Synthetic comment -- @@ -1249,13 +1270,24 @@

/**
* Internal helper to perform the actual sdk location and id check.
     * <p/>
     * This is useful for callers who want to override what happens when the check
     * fails. Otherwise consider calling {@link #checkSdkLocationAndId()} that will
     * present a modal dialog to the user in case of failure.
*
     * @param osSdkLocation The sdk directory, an OS path. Can be null.
* @param errorHandler An checkSdkErrorHandler that can display a warning or an error.
* @return False if there was an error or the result from the errorHandler invocation.
*/
    public boolean checkSdkLocationAndId(@Nullable String osSdkLocation,
                                         @NonNull CheckSdkErrorHandler errorHandler) {
        if (osSdkLocation == null || osSdkLocation.trim().length() == 0) {
            return errorHandler.handleError(
                    Solution.OPEN_ANDROID_PREFS,
                    "Location of the Android SDK has not been setup in the preferences.");
        }

        if (!osSdkLocation.endsWith(File.separator)) {
osSdkLocation = osSdkLocation + File.separator;
}

//Synthetic comment -- @@ -1355,12 +1387,12 @@
protected IStatus run(IProgressMonitor monitor) {
try {

                    if (mParseSdkContentIsRunning) {
return new Status(IStatus.WARNING, PLUGIN_ID,
"An Android SDK is already being loaded. Please try again later.");
}

                    mParseSdkContentIsRunning = true;

SubMonitor progress = SubMonitor.convert(monitor,
"Initialize SDK Manager", 100);
//Synthetic comment -- @@ -1368,10 +1400,9 @@
Sdk sdk = Sdk.loadSdk(AdtPrefs.getPrefs().getOsSdkFolder());

if (sdk != null) {
ArrayList<IJavaProject> list = new ArrayList<IJavaProject>();
synchronized (Sdk.getLock()) {
                            mSdkLoadedStatus = LoadStatus.LOADED;

progress.setTaskName("Check Projects");

//Synthetic comment -- @@ -1410,7 +1441,7 @@
// SDK failed to Load!
// Sdk#loadSdk() has already displayed an error.
synchronized (Sdk.getLock()) {
                            mSdkLoadedStatus = LoadStatus.FAILED;
}
}

//Synthetic comment -- @@ -1443,7 +1474,7 @@
"parseSdkContent failed", t);               //$NON-NLS-1$

} finally {
                    mParseSdkContentIsRunning = false;
if (monitor != null) {
monitor.done();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 9daa43c..c5d4713 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.annotations.Nullable;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -30,14 +31,22 @@
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* Delegate for the toolbar/menu action "Android SDK Manager".
//Synthetic comment -- @@ -57,63 +66,203 @@

@Override
public void run(IAction action) {
        if (!openExternalSdkManager()) {
            // If we failed to execute the sdk manager, check the SDK location.
            // If it's not properly setm the check will display a dialog to state
            // so to the user and a link to the prefs.
            // Here's it's ok to call checkSdkLocationAndId() since it will not try
            // to run the SdkManagerAction (it might run openExternalSdkManager though.)
            // If checkSdkLocationAndId tries to open the SDK Manager, it end up using
            // the internal one.
            if (AdtPlugin.getDefault().checkSdkLocationAndId()) {
                // The SDK check was successful, yet the sdk manager fail to launch anyway.
                AdtPlugin.displayError(
                        "Android SDK",
                        "Failed to run the Android SDK Manager. Check the Android Console View for details.");
            }
        }
    }

    /**
     * A custom implementation of {@link ProgressMonitorDialog} that allows us
     * to rename the "Cancel" button to "Close" from the internal task.
     */
    private static class CloseableProgressMonitorDialog extends ProgressMonitorDialog {

        public CloseableProgressMonitorDialog(Shell parent) {
            super(parent);
        }

        public void changeCancelToClose() {
            if (cancel != null && !cancel.isDisposed()) {
                Display display = getShell() == null ? null : getShell().getDisplay();
                if (display != null) {
                    display.syncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (cancel != null && !cancel.isDisposed()) {
                                cancel.setText(IDialogConstants.CLOSE_LABEL);
                            }
                        }
                    });
                }
            }
}
}

/**
* Opens the SDK Manager as an external application.
* This call is asynchronous, it doesn't wait for the manager to be closed.
     * <p/>
     * Important: this method must NOT invoke {@link AdtPlugin#checkSdkLocationAndId}
     * (in any of its variations) since the dialog uses this method to invoke the sdk
     * manager if needed.
*
* @return True if the application was found and executed. False if it could not
*   be located or could not be launched.
*/
public static boolean openExternalSdkManager() {

        // On windows this takes a couple seconds and it's not clear the launch action
        // has been invoked. To prevent the user from randomly clicking the "open sdk manager"
        // button multiple times, show a progress window that will automatically close
        // after a couple seconds.

        // By default openExternalSdkManager will return false.
        final AtomicBoolean returnValue = new AtomicBoolean(false);

        final CloseableProgressMonitorDialog p =
            new CloseableProgressMonitorDialog(AdtPlugin.getDisplay().getActiveShell());
        p.setOpenOnRun(true);
try {
            p.run(true /*fork*/, true /*cancelable*/, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException {

                    AdtPlugin ap = AdtPlugin.getDefault();

                    boolean sdkIsLoading = ap.getSdkLoadStatus() == LoadStatus.LOADING;

                    final int numIter1 = 50;  //50*100=5s to wait for sdk
                    final int numIter2 = 30;  //30*100=3s to wait for window
                    final int sleepMs = 100;
                    monitor.beginTask("Starting Android SDK Manager",
                                      sdkIsLoading ? numIter1 + numIter2 : numIter2);

                    // If the user tries to invoke the SDK Manager right when Eclipse open,
                    // the SDK is still in the process of loading. In this case give the action
                    // a bit more time to complete.
                    if (sdkIsLoading) {
                        monitor.subTask("Waiting for Android SDK to finish loading...");
                        int work = 0;
                        for (int i = 0; i < numIter1; i++) {
                            if (monitor.isCanceled()) {
                                // Canceled by user; return true as if it succeeded.
                                returnValue.set(true);
                                return;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                break;
                            }
                            monitor.worked(1);
                            work++;
                            if (ap.getSdkLoadStatus() != LoadStatus.LOADING) {
                                break;
}
}
                        if (work < numIter1) {
                            monitor.worked(numIter1 - work);
                        }
                        if (ap.getSdkLoadStatus() == LoadStatus.LOADED) {
                            monitor.subTask("Android SDK finished loading.");
                        }
                    }

                    Sdk sdk = Sdk.getCurrent();
                    if (sdk == null) {
                        AdtPlugin.printErrorToConsole("SDK Manager", "Android SDK is not loaded.");
                        return;
                    }

                    File androidBat = FileOp.append(
                            sdk.getSdkLocation(),
                            SdkConstants.FD_TOOLS,
                            SdkConstants.androidCmdName());

                    if (!androidBat.exists()) {
                        AdtPlugin.printErrorToConsole("SDK Manager",
                                "Missing %s file in Android SDK.", SdkConstants.androidCmdName());
                        return;
                    }

                    if (monitor.isCanceled()) {
                        // Canceled by user; return true as if it succeeded.
                        returnValue.set(true);
                        return;
                    }

                    p.changeCancelToClose();

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

                        // Set openExternalSdkManager to return true.
                        returnValue.set(true);
                    } catch (Exception ignore) {
                    }

                    // This small wait prevents the progress dialog from closing too fast.
                    for (int i = 0; i < numIter2; i++) {
                        if (monitor.isCanceled()) {
                            // Canceled by user; return true as if it succeeded.
                            returnValue.set(true);
                            return;
                        }
                        if (i == 10) {
                            monitor.subTask("Initializing... SDK Manager will show up shortly.");
                        }
                        try {
                            Thread.sleep(sleepMs);
                            monitor.worked(1);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }

                    monitor.done();
                }
            });
        } catch (Exception e) {
            AdtPlugin.log(e, "SDK Manager exec failed");    //$NON-NLS-1#
            return false;
}

        return returnValue.get();
}

/**







