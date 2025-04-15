/*Fix crash issue when press 'Home' key

Fix a crash issue when press "Home" key in some other application.

W/dalvikvm( 8262): threadid=1: thread exiting with uncaught exception (group=0x40e1c300)
E/AndroidRuntime( 8262): FATAL EXCEPTION: main
E/AndroidRuntime( 8262): java.lang.NullPointerException
E/AndroidRuntime( 8262):        at com.android.launcher2.DragLayer.onInterceptHoverEvent(DragLayer.java:172)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.dispatchHoverEvent(ViewGroup.java:1416)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.exitHoverTargets(ViewGroup.java:1593)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:2517)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:2532)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:2532)
E/AndroidRuntime( 8262):        at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:2532)
E/AndroidRuntime( 8262):        at android.view.ViewRootImpl.dispatchDetachedFromWindow(ViewRootImpl.java:2613)
E/AndroidRuntime( 8262):        at android.view.ViewRootImpl.doDie(ViewRootImpl.java:3962)
E/AndroidRuntime( 8262):        at android.view.ViewRootImpl.die(ViewRootImpl.java:3945)
E/AndroidRuntime( 8262):        at android.view.WindowManagerImpl.removeViewImmediate(WindowManagerImpl.java:375)
E/AndroidRuntime( 8262):        at android.view.WindowManagerImpl$CompatModeWrapper.removeViewImmediate(WindowManagerImpl.java:170)
E/AndroidRuntime( 8262):        at android.app.ActivityThread.handleDestroyActivity(ActivityThread.java:3309)
E/AndroidRuntime( 8262):        at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:3489)
E/AndroidRuntime( 8262):        at android.app.ActivityThread.access$700(ActivityThread.java:130)
E/AndroidRuntime( 8262):        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1201)
E/AndroidRuntime( 8262):        at android.os.Handler.dispatchMessage(Handler.java:99)
E/AndroidRuntime( 8262):        at android.os.Looper.loop(Looper.java:137)
E/AndroidRuntime( 8262):        at android.app.ActivityThread.main(ActivityThread.java:4745)
E/AndroidRuntime( 8262):        at java.lang.reflect.Method.invokeNative(Native Method)
E/AndroidRuntime( 8262):        at java.lang.reflect.Method.invoke(Method.java:511)
E/AndroidRuntime( 8262):        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
E/AndroidRuntime( 8262):        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
E/AndroidRuntime( 8262):        at dalvik.system.NativeStart.main(Native Method)
W/ActivityManager( 8009):   Force finishing activity com.android.launcher/com.android.launcher2.Launcher

Signed-off-by: Zhang Jiejing <jiejing.zhang@freescale.com>*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/DragLayer.java b/src/com/android/launcher2/DragLayer.java
//Synthetic comment -- index 4be1914..7f2d276 100644

//Synthetic comment -- @@ -169,6 +169,8 @@

@Override
public boolean onInterceptHoverEvent(MotionEvent ev) {
        if (mLauncher == null)
            return false;
Folder currentFolder = mLauncher.getWorkspace().getOpenFolder();
if (currentFolder == null) {
return false;







