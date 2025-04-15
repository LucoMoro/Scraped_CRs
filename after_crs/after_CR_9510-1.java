/*Video Camera Options*/




//Synthetic comment -- diff --git a/src/com/android/camera/CameraSettings.java b/src/com/android/camera/CameraSettings.java
//Synthetic comment -- index 078b84f..27ecd9a 100644

//Synthetic comment -- @@ -64,6 +64,9 @@
protected void onResume() {
super.onResume();
updateVideoQuality();
        updateAudioEncoder();
        updateVideoEncoder();
        updateOutputFormat();
}

private void initUI() {







