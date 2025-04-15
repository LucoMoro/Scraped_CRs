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

        /** Handle an error message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleError(Solution solution, String message);

        /** Handle a warning message during sdk location check. Returns whatever
* checkSdkLocationAndId() should returns.
*/
public abstract boolean handleWarning(Solution solution, String message);
//Synthetic comment -- @@ -1106,24 +1108,36 @@
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
//Synthetic comment -- @@ -1190,6 +1204,8 @@
private void openSdkManager() {
// Windows only: open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
if (SdkManagerAction.openExternalSdkManager()) {
return;
//Synthetic comment -- @@ -1249,13 +1265,24 @@

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









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 9daa43c..41ddd38 100755

//Synthetic comment -- @@ -22,15 +22,21 @@
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
//Synthetic comment -- @@ -38,6 +44,7 @@
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;

/**
* Delegate for the toolbar/menu action "Android SDK Manager".
//Synthetic comment -- @@ -57,16 +64,30 @@

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
//Synthetic comment -- @@ -77,7 +98,7 @@
return false;
}

        File androidBat = FileOp.append(
sdk.getSdkLocation(),
SdkConstants.FD_TOOLS,
SdkConstants.androidCmdName());
//Synthetic comment -- @@ -86,33 +107,122 @@
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








