/*Telephony: Fix wakeup issues seen due to DataStall recovery

Telephony framework uses AlarmManager for recovery mechanism of data
stalled issue.
Framework configures the ELAPSED_REALTIME_WAKEUP alarm for every
6minutes irrespective of the data state. Due to this,quiet a few
wakeups are seen even when the device is not connected to Mobile
Data network.

Fix is to check the Data connection state before starting
this alarm and also to use ELAPSED_REALTIME which doesn't
wake the device up in sleep state.

Change-Id:I219bff9199e5ce70bbcd05c95d65731886215fe9Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28494*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 347f00e..065ed02 100644

//Synthetic comment -- @@ -1374,32 +1374,34 @@
int nextAction = getRecoveryAction();
int delayInMs;

        if (getOverallState() == DctConstants.State.CONNECTED) {
            // If screen is on or data stall is currently suspected, set the alarm
            // with an aggresive timeout.
            if (mIsScreenOn || suspectedStall || RecoveryAction.isAggressiveRecovery(nextAction)) {
                delayInMs = Settings.Global.getInt(mResolver,
                        Settings.Global.DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS,
                        DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS_DEFAULT);
            } else {
                delayInMs = Settings.Global.getInt(mResolver,
                        Settings.Global.DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS,
                        DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS_DEFAULT);
            }

            mDataStallAlarmTag += 1;
            if (VDBG) {
                log("startDataStallAlarm: tag=" + mDataStallAlarmTag +
                        " delay=" + (delayInMs / 1000) + "s");
            }
            AlarmManager am =
                    (AlarmManager) mPhone.getContext().getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getActionIntentDataStallAlarm());
            intent.putExtra(DATA_STALL_ALARM_TAG_EXTRA, mDataStallAlarmTag);
            mDataStallAlarmIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + delayInMs, mDataStallAlarmIntent);
        }
}

protected void stopDataStallAlarm() {







