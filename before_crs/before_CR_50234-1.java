/*Send correct screen state when radio available.

Change-Id:I098ffd06916d9d37272ee3c8c57a4d7e21e564c6Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index dbfe5d9..453ca82 100644

//Synthetic comment -- @@ -222,6 +222,7 @@
RILSender mSender;
Thread mReceiverThread;
RILReceiver mReceiver;
WakeLock mWakeLock;
int mWakeLockTimeout;
// The number of requests pending to be sent out, it increases before calling
//Synthetic comment -- @@ -622,8 +623,8 @@
mPreferredNetworkType = preferredNetworkType;
mPhoneType = RILConstants.NO_PHONE;

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
mWakeLock.setReferenceCounted(false);
mWakeLockTimeout = SystemProperties.getInt(TelephonyProperties.PROPERTY_WAKE_LOCK_TIMEOUT,
DEFAULT_WAKE_LOCK_TIMEOUT);
//Synthetic comment -- @@ -2054,9 +2055,8 @@
// In case screen state was lost (due to process crash),
// this ensures that the RIL knows the correct screen state.

        // TODO: Should query Power Manager and send the actual
        // screen state.  Just send true for now.
        sendScreenState(true);
}

private RadioState getRadioStateFromInt(int stateInt) {







