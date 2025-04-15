/*Using SCREEN_BRIGHT_WAKE_LOCK instead of PARTIAL_WAKE_LOCK

As of now BT file transfer wakes device for too short time interval
this is because of holding the PARTIAL_WAKE_LOCK which turns off screen
and keyboard.

This patch uses SCREEN_BRIGHT_WAKE_LOCK instead of PARTIAL_WAKE_LOCK which
holds both CPU and Screen turned ON.

Change-Id:I3dd4fa0badcd040a1de7422ff83b5b8fa639fafeAcked-by: Pavan Savoy <pavan_savoy@ti.com>
Signed-off-by: halli manjunatha <hallimanju@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index dc69945..9f61b65 100644

//Synthetic comment -- @@ -93,7 +93,7 @@

private WakeLock mWakeLock;

    private WakeLock mPartialWakeLock;

boolean mTimeoutMsgSent = false;

//Synthetic comment -- @@ -103,7 +103,7 @@
PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
| PowerManager.ON_AFTER_RELEASE, TAG);
        mPartialWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
}

public void unblock() {
//Synthetic comment -- @@ -293,7 +293,7 @@

synchronized (this) {
if (mWakeLock.isHeld()) {
                mPartialWakeLock.acquire();
mWakeLock.release();
}
mServerBlocking = true;
//Synthetic comment -- @@ -550,8 +550,8 @@
if (mWakeLock.isHeld()) {
mWakeLock.release();
}
        if (mPartialWakeLock.isHeld()) {
            mPartialWakeLock.release();
}
}








