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
//Synthetic comment -- index a2afc89..0c8e35d 100644

//Synthetic comment -- @@ -189,12 +189,14 @@
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
//Synthetic comment -- @@ -1106,24 +1108,36 @@
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
//Synthetic comment -- @@ -1190,6 +1204,8 @@
private void openSdkManager() {
// Windows only: open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
                // Also when this is invoked becasue SdkManagerAction.run() fails, this
                // test will fail and we'll fallback on using the internal one.
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
if (SdkManagerAction.openExternalSdkManager()) {
return;
//Synthetic comment -- @@ -1249,13 +1265,24 @@

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









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 9daa43c..41ddd38 100755

//Synthetic comment -- @@ -22,15 +22,21 @@
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
//Synthetic comment -- @@ -38,6 +44,7 @@
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
* Delegate for the toolbar/menu action "Android SDK Manager".
//Synthetic comment -- @@ -57,16 +64,30 @@

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
* Opens the SDK Manager as an external application.
* This call is asynchronous, it doesn't wait for the manager to be closed.
     * <p/>
     * Important: this method must NOT invoke {@link AdtPlugin#checkSdkLocationAndId}
     * (in any of its variations) since the dialog uses this method to invoke the sdk
     * manager if needed.
*
* @return True if the application was found and executed. False if it could not
*   be located or could not be launched.
//Synthetic comment -- @@ -77,7 +98,7 @@
return false;
}

        final File androidBat = FileOp.append(
sdk.getSdkLocation(),
SdkConstants.FD_TOOLS,
SdkConstants.androidCmdName());
//Synthetic comment -- @@ -86,33 +107,122 @@
return false;
}

        // On windows this takes a couple seconds and it's not clear the launch action
        // has been invoked. To prevent the user from randomly clicking the "open sdk manager"
        // button multiple times, show a progress window that will automatically close
        // after a couple seconds.

        ProgressMonitorDialog p = new ProgressMonitorDialog(AdtPlugin.getDisplay().getActiveShell());
try {
            p.setOpenOnRun(true);
            p.run(true /*fork*/, false /*cancelable*/, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException {

                    final int numIter = 30;
                    final int sleepMs = 100;
                    monitor.beginTask("Starting Android SDK Manager", numIter);

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

                    // This small wait prevents the progress dialog from closing too fast.
                    for (int i = 0; i < numIter; i++) {
                        if (monitor.isCanceled()) {
                            break;
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

        /*
        final ProgressTask progress = new ProgressTask(AdtPlugin.getDisplay().getActiveShell(),
                "Starting Android SDK Manager");
        progress.setAutoClose(true);

        progress.start(new ITask() {
            @Override
            public void run(ITaskMonitor monitor) {
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

                // This small wait prevents the dialog from closing too fast.
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(100);
                        monitor.incProgress(1);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        });
        */

return true;
}








