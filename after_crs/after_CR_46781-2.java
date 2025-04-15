/*Fixes Binder Buffer leak after crashing com.android.smspush process

After many crashes of com.android.smspush process under low memory
condition, binder buffer used to get overflow as there is a leak
in buffer.
This fix restricts the leak in binder buffer by unbinding it upon
com.android.smspush process crash.

Change-Id:I0b5cd4ef282ec9f12693337001c084ce60db4555*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WapPushOverSms.java b/src/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 1b174ab..ef74e1e 100755

//Synthetic comment -- @@ -70,6 +70,8 @@
public void onServiceDisconnected(ComponentName name) {
mWapPushMan = null;
if (false) Rlog.v(LOG_TAG, "wappush manager disconnected.");
            // Detach the previous binder
            mOwner.unbindService(mWapConn);
// WapPushManager must be always attached.
rebindWapPushManager();
}







