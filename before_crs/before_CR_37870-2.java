/*Holding FULL_WAKE_LOCK for 60 seconds to hold display ON

As of now BT file transfer wakes device for too short time interval
this is because of releasing FULL_WAKE_LOCK (after 1 second) and
holding the PARTIAL_WAKE_LOCK which turns off screen and keyboard.

This patch holds the FULL_WAKE_LOCK for 60 seconds so that user will
have a buffer time to look in to the display and decide weather to
accept or deny the operation.

Meanwhile since we have held the PARTIAL_WAKE_LOCK, so if user presses
power button CPU is still alive to handle the file operation.

Change-Id:I3dd4fa0badcd040a1de7422ff83b5b8fa639fafeAcked-by: Pavan Savoy <pavan_savoy@ti.com>
Signed-off-by: halli manjunatha <hallimanju@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index dc69945..5a416b5 100644

//Synthetic comment -- @@ -93,6 +93,8 @@

private WakeLock mWakeLock;

private WakeLock mPartialWakeLock;

boolean mTimeoutMsgSent = false;
//Synthetic comment -- @@ -116,7 +118,8 @@
*/
public void preStart() {
if (D) Log.d(TAG, "acquire full WakeLock");
        mWakeLock.acquire();
try {
if (D) Log.d(TAG, "Create ServerSession with transport " + mTransport.toString());
mSession = new ServerSession(mTransport, this, null);
//Synthetic comment -- @@ -292,10 +295,7 @@


synchronized (this) {
            if (mWakeLock.isHeld()) {
                mPartialWakeLock.acquire();
                mWakeLock.release();
            }
mServerBlocking = true;
try {








