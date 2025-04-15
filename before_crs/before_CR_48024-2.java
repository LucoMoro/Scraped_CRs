/*avd edit dialog: Warn if RAM > 512MB for Windows users.

There is usually a memory allocation failure on Windows with
larger amounts of emulated RAM due to the use of a 32 bit emulator.

Change-Id:Id0e59739e77c0aaf2776d7d883d6524de7255de2*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 89aa8ce..e9f2341 100644

//Synthetic comment -- @@ -922,6 +922,21 @@
mAvdName.getText());
}

if (mGpuEmulation.getSelection() && mSnapshot.getSelection()) {
valid = false;
error = "GPU Emulation and Snapshot cannot be used simultaneously";







