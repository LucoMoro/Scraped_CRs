/*Remove unnecessary locking for screen broadcasts.

The actual handling occurs in updateScreenOn() on the other side of a
handler, which acquires the lock correctly.

Change-Id:Ibd359446dba8e88f81d34f1e10a6b5e150348f89*/




//Synthetic comment -- diff --git a/services/java/com/android/server/net/NetworkPolicyManagerService.java b/services/java/com/android/server/net/NetworkPolicyManagerService.java
//Synthetic comment -- index 43ddf8d..18fefda 100644

//Synthetic comment -- @@ -423,11 +423,9 @@
private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
            // screen-related broadcasts are protected by system, no need
            // for permissions check.
            mHandler.obtainMessage(MSG_SCREEN_ON_CHANGED).sendToTarget();
}
};








