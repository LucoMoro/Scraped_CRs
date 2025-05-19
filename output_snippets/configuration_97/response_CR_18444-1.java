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

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyInstaller {
    private boolean installedAddon = false;
    private boolean installedTools = false;
    private final HashSet<Archive> installedArchives = new HashSet<Archive>();
    private static final Logger logger = Logger.getLogger(MyInstaller.class.getName());

    public void installArchives(ITaskMonitor monitor, Archive archive) {
        monitor.setDescription("Preparing to install archives");

        installedArchives.remove(archive.getReplaced());

        if (archive.getParentPackage() instanceof AddonPackage) {
            installedAddon = true;
        } else if (archive.getParentPackage() instanceof ToolPackage) {
            installedTools = true;
        }

        if (installedAddon || installedTools) {
            if (isAdbRunning()) {
                askForAdbRestart(monitor);
            } else {
                killAdb();
            }
        }

        if (installedTools) {
            notifyToolsNeedsToBeRestarted();
        }
    }

    private synchronized boolean isAdbRunning() {
        try {
            Process process = Runtime.getRuntime().exec("adb get-state");
            process.waitFor();
            String result = new String(process.getInputStream().readAllBytes()).trim();
            return result.equals("device") || result.equals("offline") || result.equals("unauthorized");
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error checking ADB status", e);
            return false;
        }
    }

    private synchronized void killAdb() {
        try {
            Process process = Runtime.getRuntime().exec("adb kill-server");
            process.waitFor();
            logger.log(Level.INFO, "ADB killed successfully with exit code: " + process.exitValue());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error killing ADB", e);
        }
    }

    public void askForAdbRestart(ITaskMonitor monitor) {
        boolean canRestart[] = new boolean[1];
        canRestart[0] = MessageDialog.openQuestion(getWindowShell(),
                "ADB Restart",
                "A package that depends on ADB has been updated. It is recommended " +
                "to restart ADB. Is it OK to do it now? If not, you can restart it " +
                "manually later.");

        logger.log(Level.INFO, "User response on ADB restart: " + canRestart[0] + " at " + System.currentTimeMillis());

        if (canRestart[0]) {
            restartAdb();
        }
    }

    private synchronized void restartAdb() {
        try {
            Process process = Runtime.getRuntime().exec("adb start-server");
            process.waitFor();
            logger.log(Level.INFO, "ADB restarted successfully with exit code: " + process.exitValue());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error restarting ADB", e);
        }
    }
}

//<End of snippet n. 0>