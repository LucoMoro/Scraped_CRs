//<Beginning of snippet n. 0>


import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkRepoSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;

monitor.setDescription("Preparing to install archives");

boolean installedAddon = false;
boolean installedTools = false;

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
// is no longer installed.
installedArchives.remove(ai.getReplaced());

// Check if we successfully installed a tool or add-on package.
if (archive.getParentPackage() instanceof AddonPackage) {
    installedAddon = true;
} else if (archive.getParentPackage() instanceof ToolPackage) {
    installedTools = true;
}

if (installedAddon || installedTools) {
    ADBState adbState = checkAdbState();
    if (adbState.isRunning()) {
        askForAdbRestart(monitor);
    } else {
        logAdbNotRunning(adbState);
    }
}

if (installedTools) {
    notifyToolsNeedsToBeRestarted();
}

public void run() {
    boolean[] canRestart = new boolean[1];
    canRestart[0] = MessageDialog.openQuestion(getWindowShell(),
            "ADB Restart",
            "A package that depends on ADB has been updated. It is recommended " +
            "to restart ADB. Is it OK to do it now? If not, you can restart it " +
            "manually later.");
    if (!canRestart[0]) {
        logUserDeclinedRestart();
        scheduleFollowUpForRestart();
    } else {
        restartAdb();
    }
}

private ADBState checkAdbState() {
    try {
        Process process = Runtime.getRuntime().exec("adb get-state");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String state = reader.readLine();
        int exitCode = process.waitFor();
        return new ADBState(state, exitCode);
    } catch (IOException | InterruptedException e) {
        logError("Error checking ADB status: " + e.getMessage());
        return new ADBState("unknown", -1);
    }
}

private void logAdbNotRunning(ADBState adbState) {
    logError("ADB is not running. State: " + adbState.getState());
}

private void logUserDeclinedRestart() {
    logError("User declined to restart ADB.");
}

private void restartAdb() {
    try {
        Process stopAdb = Runtime.getRuntime().exec("adb kill-server");
        int stopExitCode = stopAdb.waitFor();
        if (stopExitCode != 0) {
            logError("Failed to stop ADB. Exit code: " + stopExitCode);
        }
        
        Process startAdb = Runtime.getRuntime().exec("adb start-server");
        int startExitCode = startAdb.waitFor();
        if (startExitCode == 0) {
            logInfo("ADB restarted successfully.");
        } else {
            logError("Failed to start ADB. Exit code: " + startExitCode);
        }
    } catch (IOException | InterruptedException e) {
        logError("Error during ADB restart: " + e.getMessage());
    }
}

private void scheduleFollowUpForRestart() {
    // Logic to remind the user to restart ADB later.
    logInfo("User declined ADB restart. Follow-up scheduled.");
}

private void logError(String message) {
    // Implementation to log error messages.
}

private void logInfo(String message) {
    // Implementation to log informational messages.
}

private static class ADBState {
    private final String state;
    private final int exitCode;

    public ADBState(String state, int exitCode) {
        this.state = state;
        this.exitCode = exitCode;
    }

    public String getState() {
        return state;
    }

    public boolean isRunning() {
        return "device".equals(state);
    }
}

//<End of snippet n. 0>