/*SDK Manager fix: message to restart after tools update.

The latest CL made this appear after a platform-tools update instead.

Change-Id:I5e27612b3fa6fd7e53cd10a369b2d5eba19a168b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 4661833..fe51f19 100755

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;
//Synthetic comment -- @@ -407,6 +408,7 @@
monitor.setDescription("Preparing to install archives");

boolean installedAddon = false;
                boolean installedTools = false;
boolean installedPlatformTools = false;

// Mark all current local archives as already installed.
//Synthetic comment -- @@ -466,6 +468,8 @@
// Check if we successfully installed a platform-tool or add-on package.
if (archive.getParentPackage() instanceof AddonPackage) {
installedAddon = true;
                            } else if (archive.getParentPackage() instanceof ToolPackage) {
                                installedTools = true;
} else if (archive.getParentPackage() instanceof PlatformToolPackage) {
installedPlatformTools = true;
}
//Synthetic comment -- @@ -521,7 +525,7 @@
askForAdbRestart(monitor);
}

                if (installedTools) {
notifyToolsNeedsToBeRestarted();
}








