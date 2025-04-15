/*Fix the unslightly AOSP crash on boot.

AOSP isn't usually built with binary blobs for camera and so on, so
this code was crashing on every runtime restart, which was annoying:

  threadid=1: thread exiting with uncaught exception (group=0x40bab950)
  FATAL EXCEPTION: main
  java.lang.RuntimeException: Unable to start receiver com.android.camera.DisableCameraReceiver: java.lang.RuntimeException: Fail to get camera info
  	at android.app.ActivityThread.handleReceiver(ActivityThread.java:2236)
  	at android.app.ActivityThread.access$1500(ActivityThread.java:130)
  	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1271)
  	at android.os.Handler.dispatchMessage(Handler.java:99)
  	at android.os.Looper.loop(Looper.java:137)
  	at android.app.ActivityThread.main(ActivityThread.java:4745)
  	at java.lang.reflect.Method.invokeNative(Native Method)
  	at java.lang.reflect.Method.invoke(Method.java:511)
  	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
  	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
  	at dalvik.system.NativeStart.main(Native Method)
  Caused by: java.lang.RuntimeException: Fail to get camera info
  	at android.hardware.Camera.getCameraInfo(Native Method)
  	at com.android.camera.DisableCameraReceiver.hasBackCamera(DisableCameraReceiver.java:65)
  	at com.android.camera.DisableCameraReceiver.onReceive(DisableCameraReceiver.java:40)
  	at android.app.ActivityThread.handleReceiver(ActivityThread.java:2229)
  	... 10 more

Change-Id:I24c62603e77cad58259b26da62d8bea820ced21d*/
//Synthetic comment -- diff --git a/src/com/android/camera/DisableCameraReceiver.java b/src/com/android/camera/DisableCameraReceiver.java
//Synthetic comment -- index 3517405..a31599d 100644

//Synthetic comment -- @@ -62,10 +62,16 @@
int n = android.hardware.Camera.getNumberOfCameras();
CameraInfo info = new CameraInfo();
for (int i = 0; i < n; i++) {
            android.hardware.Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                Log.i(TAG, "back camera found: " + i);
                return true;
}
}
Log.i(TAG, "no back camera");







