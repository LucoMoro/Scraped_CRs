/*Camera: Resolve conflict between setPreviewFpsRange and setPreviewFrameRate

- Currently, Camera interface shares two different methods to set framerate -
  "PreviewFpsRange" and "PreviewFrameRate". Many existing Camera applications
  and CTS tests expects that Camera hardware has it's default framerate setting.
  Thus, CameraHardware should provide default values for both framerate
  methods at the same time. Camera application usually gets default parameters
  array from Camera hardware, changing just one of the framerate parameters, and then
  sends modified parameters array back to hardware. But camera hardware cannot recognize
  which parameter is the correct one to use for it's framerate setting - one of them is obsolete.
  The only possible way to solve this is every hardware implementation to implement
  it's own workaround of this conflict by tracking framerate setting changes.

- General solution: Simple sanity check is added in Android core. When application
  uses one method to set framerate, data owned by other method is deleted at the same time.*/




//Synthetic comment -- diff --git a/core/java/android/hardware/Camera.java b/core/java/android/hardware/Camera.java
//Synthetic comment -- index dee1d03..d9dc1684 100644

//Synthetic comment -- @@ -1505,6 +1505,7 @@
*/
@Deprecated
public void setPreviewFrameRate(int fps) {
            remove(KEY_PREVIEW_FPS_RANGE);
set(KEY_PREVIEW_FRAME_RATE, fps);
}

//Synthetic comment -- @@ -1547,6 +1548,7 @@
* @see #getSupportedPreviewFpsRange()
*/
public void setPreviewFpsRange(int min, int max) {
            remove(KEY_PREVIEW_FRAME_RATE);
set(KEY_PREVIEW_FPS_RANGE, "" + min + "," + max);
}








