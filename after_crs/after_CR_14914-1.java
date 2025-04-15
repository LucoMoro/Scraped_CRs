/*Replaced raw string arguments for Context.getSystemService() with final Context variables

Change-Id:If5139a1526101292e5da557bfad3f4db80fb64a8*/




//Synthetic comment -- diff --git a/core/java/android/app/Dialog.java b/core/java/android/app/Dialog.java
//Synthetic comment -- index 58e8b32..c82df0b 100644

//Synthetic comment -- @@ -136,7 +136,7 @@
public Dialog(Context context, int theme) {
mContext = new ContextThemeWrapper(
context, theme == 0 ? com.android.internal.R.style.Theme_Dialog : theme);
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
Window w = PolicyManager.makeNewWindow(mContext);
mWindow = w;
w.setCallback(this);








//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
//Synthetic comment -- index 446a992..2ae7b549 100644

//Synthetic comment -- @@ -123,7 +123,7 @@
}

private void initFloatingWindow() {
        mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
mWindow = PolicyManager.makeNewWindow(mContext);
mWindow.setWindowManager(mWindowManager, null, null);
mWindow.requestFeature(Window.FEATURE_NO_TITLE);








//Synthetic comment -- diff --git a/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java b/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java
//Synthetic comment -- index 4fd7e65..65c7dcbd 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ListView;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.app.Notification;
//Synthetic comment -- @@ -60,7 +61,7 @@
private Test[] mTests = new Test[] {
new Test("Off and sound") {
public void run() {
                PowerManager pm = (PowerManager)NotificationTestList.this.getSystemService(Context.POWER_SERVICE);
PowerManager.WakeLock wl = 
pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sound");
wl.acquire();
//Synthetic comment -- @@ -549,7 +550,7 @@
public void run()
{
PowerManager.WakeLock wl
                        = ((PowerManager)NotificationTestList.this.getSystemService(Context.POWER_SERVICE))
.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "crasher");
wl.acquire();
mHandler.postDelayed(new Runnable() {







