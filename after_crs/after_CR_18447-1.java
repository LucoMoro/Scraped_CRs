/*Integrate 11357696 into tools_r8. DO NOT MERGE

SDK Manager: Restart ADB after platform-tools update.

Change-Id:Ie59201a3f303e1a9a837e82cbf4a83f620b4e7f2*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index a2092e0..4661833 100755

//Synthetic comment -- @@ -29,12 +29,12 @@
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
//Synthetic comment -- @@ -407,7 +407,7 @@
monitor.setDescription("Preparing to install archives");

boolean installedAddon = false;
                boolean installedPlatformTools = false;

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
//Synthetic comment -- @@ -463,11 +463,11 @@
// is no longer installed.
installedArchives.remove(ai.getReplaced());

                            // Check if we successfully installed a platform-tool or add-on package.
if (archive.getParentPackage() instanceof AddonPackage) {
installedAddon = true;
                            } else if (archive.getParentPackage() instanceof PlatformToolPackage) {
                                installedPlatformTools = true;
}
}

//Synthetic comment -- @@ -511,7 +511,7 @@
}
}

                if (installedAddon || installedPlatformTools) {
// We need to restart ADB. Actually since we don't know if it's even
// running, maybe we should just kill it and not start it.
// Note: it turns out even under Windows we don't need to kill adb
//Synthetic comment -- @@ -521,7 +521,7 @@
askForAdbRestart(monitor);
}

                if (installedPlatformTools) {
notifyToolsNeedsToBeRestarted();
}

//Synthetic comment -- @@ -558,9 +558,8 @@
public void run() {
canRestart[0] = MessageDialog.openQuestion(getWindowShell(),
"ADB Restart",
                            "A package that depends on ADB has been updated. \n" +
                            "Do you want to restart ADB now?");
}
});
}







