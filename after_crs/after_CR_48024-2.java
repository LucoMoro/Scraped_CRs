/*avd edit dialog: Warn if RAM > 512MB for Windows users.

There is usually a memory allocation failure on Windows with
larger amounts of emulated RAM due to the use of a 32 bit emulator.

Change-Id:Id0e59739e77c0aaf2776d7d883d6524de7255de2*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 89aa8ce..e9f2341 100644

//Synthetic comment -- @@ -922,6 +922,21 @@
mAvdName.getText());
}

        // On Windows, display a warning if attempting to create AVD's with RAM > 512 MB.
        // This restriction should go away when we switch to using a 64 bit emulator.
        if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
            long ramSize = 0;
            try {
                ramSize = Long.parseLong(mRam.getText());
            } catch (NumberFormatException e) {
                // ignore
            }

            if (ramSize > 512) {
                warning = "On Windows, set emulated RAM to be less than or equal to 512 MB.";
            }
        }

if (mGpuEmulation.getSelection() && mSnapshot.getSelection()) {
valid = false;
error = "GPU Emulation and Snapshot cannot be used simultaneously";







