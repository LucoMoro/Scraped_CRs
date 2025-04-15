/*Replaced raw string arguments for Context.getSystemService() with final Context variables

Change-Id:I80ea9ce38aa3ca98f8f954f63336e865ea107a60*/
//Synthetic comment -- diff --git a/src/com/android/music/TouchInterceptor.java b/src/com/android/music/TouchInterceptor.java
//Synthetic comment -- index 6c22e72..b070053 100644

//Synthetic comment -- @@ -341,7 +341,7 @@
v.setImageBitmap(bm);
mDragBitmap = bm;

        mWindowManager = (WindowManager)mContext.getSystemService("window");
mWindowManager.addView(v, mWindowParams);
mDragView = v;
}
//Synthetic comment -- @@ -361,7 +361,7 @@

private void stopDragging() {
if (mDragView != null) {
            WindowManager wm = (WindowManager)mContext.getSystemService("window");
wm.removeView(mDragView);
mDragView.setImageDrawable(null);
mDragView = null;








//Synthetic comment -- diff --git a/tests/src/com/android/music/stress/AlbumsPlaybackStress.java b/tests/src/com/android/music/stress/AlbumsPlaybackStress.java
//Synthetic comment -- index f396317..46e1d9f 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
//Synthetic comment -- @@ -83,7 +84,7 @@

//Verification: check if it is in low memory
ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
      ((ActivityManager)getActivity().getSystemService("activity")).getMemoryInfo(mi);
assertFalse(TAG, mi.lowMemory); 










//Synthetic comment -- diff --git a/tests/src/com/android/music/stress/MusicPlaybackStress.java b/tests/src/com/android/music/stress/MusicPlaybackStress.java
//Synthetic comment -- index 80661f2..49e6e48 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
}
//Verification: check if it is in low memory
ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
      ((ActivityManager)getActivity().getSystemService("activity")).getMemoryInfo(mi);
assertFalse(TAG, mi.lowMemory);      
}
}







