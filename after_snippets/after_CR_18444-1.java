
//<Beginning of snippet n. 0>


import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkRepoSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;
monitor.setDescription("Preparing to install archives");

boolean installedAddon = false;
                boolean installedPlatformTools = false;

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
// is no longer installed.
installedArchives.remove(ai.getReplaced());

                            // Check if we successfully installed a platform-tool or add-on package.
if (archive.getParentPackage() instanceof AddonPackage) {
installedAddon = true;
                            } else if (archive.getParentPackage() instanceof PlatformToolPackage) {
                                installedPlatformTools = true;
}
}

}
}

                if (installedAddon || installedPlatformTools) {
// We need to restart ADB. Actually since we don't know if it's even
// running, maybe we should just kill it and not start it.
// Note: it turns out even under Windows we don't need to kill adb
askForAdbRestart(monitor);
}

                if (installedPlatformTools) {
notifyToolsNeedsToBeRestarted();
}

public void run() {
canRestart[0] = MessageDialog.openQuestion(getWindowShell(),
"ADB Restart",
                            "A package that depends on ADB has been updated. \n" +
                            "Do you want to restart ADB now?");
}
});
}

//<End of snippet n. 0>








