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
    if (isAdbRunning()) {
        askForAdbRestart(monitor);
    } else {
        logAdbNotRunning();
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

private boolean isAdbRunning() {
    try {
        Process process = Runtime.getRuntime().exec("adb get-state");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String state = reader.readLine();
        return "device".equals(state);
    } catch (IOException e) {
        logError("Error checking ADB status: " + e.getMessage());
        return false;
    }
}

private void logAdbNotRunning() {
    logError("ADB is not running.");
}

private void logUserDeclinedRestart() {
    logError("User declined to restart ADB.");
}

private void restartAdb() {
    try {
        Process stopAdb = Runtime.getRuntime().exec("adb kill-server");
        stopAdb.waitFor();
        Process startAdb = Runtime.getRuntime().exec("adb start-server");
        startAdb.waitFor();
        logInfo("ADB restarted successfully.");
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

//<End of snippet n. 0>