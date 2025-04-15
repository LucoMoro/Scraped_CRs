/*Pull up Stats polling and Data Stall Alarm handling to DCT

Code in GsmDataConnectionTracker and CdmaDataConnectionTracker is
very similar for maintaining data activity statistics and detecting
data stalls. So it can be moved up to the parent class.

Change-Id:I3ec63f6bbfe369e0006d3aa6d6b92abf451657f4*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index fdaf0a3..a528640 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
//Synthetic comment -- @@ -42,6 +43,7 @@
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.R;
//Synthetic comment -- @@ -66,6 +68,7 @@
public abstract class DataConnectionTracker extends Handler {
protected static final boolean DBG = true;
protected static final boolean VDBG = false;

/**
* Constants for the data connection activity:
//Synthetic comment -- @@ -144,6 +147,12 @@
// Tag for tracking stale alarms
protected static final String DATA_STALL_ALARM_TAG_EXTRA = "data.stall.alram.tag";

// TODO: See if we can remove INTENT_RECONNECT_ALARM
//       having to have different values for GSM and
//       CDMA. If so we can then remove the need for
//Synthetic comment -- @@ -240,6 +249,8 @@
/* Once disposed dont handle any messages */
protected boolean mIsDisposed = false;

protected BroadcastReceiver mIntentReceiver = new BroadcastReceiver ()
{
@Override
//Synthetic comment -- @@ -287,6 +298,26 @@
};

private final DataRoamingSettingObserver mDataRoamingSettingObserver;

private class DataRoamingSettingObserver extends ContentObserver {
public DataRoamingSettingObserver(Handler handler) {
//Synthetic comment -- @@ -345,34 +376,8 @@
}

public void updateTxRxSum() {
            boolean txUpdated = false, rxUpdated = false;
            long txSum = 0, rxSum = 0;
            for (ApnContext apnContext : mApnContexts.values()) {
                if (apnContext.getState() == DctConstants.State.CONNECTED) {
                    DataConnectionAc dcac = apnContext.getDataConnectionAc();
                    if (dcac == null) continue;

                    LinkProperties linkProp = dcac.getLinkPropertiesSync();
                    if (linkProp == null) continue;

                    String iface = linkProp.getInterfaceName();

                    if (iface != null) {
                        long stats = TrafficStats.getTxPackets(iface);
                        if (stats > 0) {
                            txUpdated = true;
                            txSum += stats;
                        }
                        stats = TrafficStats.getRxPackets(iface);
                        if (stats > 0) {
                            rxUpdated = true;
                            rxSum += stats;
                        }
                    }
                }
            }
            if (txUpdated) this.txPkts = txSum;
            if (rxUpdated) this.rxPkts = rxSum;
}
}

//Synthetic comment -- @@ -455,6 +460,8 @@
// watch for changes to Settings.Secure.DATA_ROAMING
mDataRoamingSettingObserver = new DataRoamingSettingObserver(mPhone);
mDataRoamingSettingObserver.register(mPhone.getContext());
}

public void dispose() {
//Synthetic comment -- @@ -565,9 +572,6 @@
// abstract methods
protected abstract String getActionIntentReconnectAlarm();
protected abstract String getActionIntentDataStallAlarm();
    protected abstract void startNetStatPoll();
    protected abstract void stopNetStatPoll();
    protected abstract void restartDataStallAlarm();
protected abstract void restartRadio();
protected abstract void log(String s);
protected abstract void loge(String s);
//Synthetic comment -- @@ -591,10 +595,6 @@
protected abstract boolean isDataPossible(String apnType);
protected abstract void onUpdateIcc();

    protected void onDataStallAlarm(int tag) {
        loge("onDataStallAlarm: not impleted tag=" + tag);
    }

@Override
public void handleMessage(Message msg) {
switch (msg.what) {
//Synthetic comment -- @@ -1144,8 +1144,294 @@
}
}

public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
pw.println("DataConnectionTracker:");
pw.println(" mInternalDataEnabled=" + mInternalDataEnabled);
pw.println(" mUserDataEnabled=" + mUserDataEnabled);
pw.println(" sPolicyDataEnabed=" + sPolicyDataEnabled);
//Synthetic comment -- @@ -1167,6 +1453,7 @@
pw.println(" mDataStallAlarmTag=" + mDataStallAlarmTag);
pw.println(" mSentSinceLastRecv=" + mSentSinceLastRecv);
pw.println(" mNoRecvPollCount=" + mNoRecvPollCount);
pw.println(" mIsWifiConnected=" + mIsWifiConnected);
pw.println(" mReconnectIntent=" + mReconnectIntent);
pw.println(" mCidActive=" + mCidActive);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 51b4a4c..8e81302 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
//Synthetic comment -- @@ -81,6 +80,8 @@
private static final String INTENT_DATA_STALL_ALARM =
"com.android.internal.telephony.cdma-data-stall";


private static final String[] mSupportedApnTypes = {
PhoneConstants.APN_TYPE_DEFAULT,
//Synthetic comment -- @@ -196,6 +197,11 @@
}

@Override
protected boolean isApnTypeAvailable(String type) {
for (String s : mSupportedApnTypes) {
if (TextUtils.equals(type, s)) {
//Synthetic comment -- @@ -299,6 +305,8 @@
*
* @param tearDown true if the underlying DataConnection should be disconnected.
* @param reason for the clean up.
*/
private void cleanUpConnection(boolean tearDown, String reason, boolean doAll) {
if (DBG) log("cleanUpConnection: reason: " + reason);
//Synthetic comment -- @@ -341,6 +349,7 @@
}

stopNetStatPoll();

if (!notificationDeferred) {
if (DBG) log("cleanupConnection: !notificationDeferred");
//Synthetic comment -- @@ -396,34 +405,10 @@
setState(DctConstants.State.CONNECTED);
notifyDataConnection(reason);
startNetStatPoll();
mDataConnections.get(0).resetRetryCount();
}

    private void resetPollStats() {
        mTxPkts = -1;
        mRxPkts = -1;
        mSentSinceLastRecv = 0;
        mNetStatPollPeriod = POLL_NETSTAT_MILLIS;
        mNoRecvPollCount = 0;
    }

    @Override
    protected void startNetStatPoll() {
        if (mState == DctConstants.State.CONNECTED && mNetStatPollEnabled == false) {
            log("[DataConnection] Start poll NetStat");
            resetPollStats();
            mNetStatPollEnabled = true;
            mPollNetStat.run();
        }
    }

    @Override
    protected void stopNetStatPoll() {
        mNetStatPollEnabled = false;
        removeCallbacks(mPollNetStat);
        log("[DataConnection] Stop poll NetStat");
    }

@Override
protected void restartRadio() {
if (DBG) log("Cleanup connection and wait " +
//Synthetic comment -- @@ -433,87 +418,6 @@
mPendingRestartRadio = true;
}

    private Runnable mPollNetStat = new Runnable() {

        public void run() {
            long sent, received;
            long preTxPkts = -1, preRxPkts = -1;

            DctConstants.Activity newActivity;

            preTxPkts = mTxPkts;
            preRxPkts = mRxPkts;

            mTxPkts = TrafficStats.getMobileTxPackets();
            mRxPkts = TrafficStats.getMobileRxPackets();

            //log("rx " + String.valueOf(rxPkts) + " tx " + String.valueOf(txPkts));

            if (mNetStatPollEnabled && (preTxPkts > 0 || preRxPkts > 0)) {
                sent = mTxPkts - preTxPkts;
                received = mRxPkts - preRxPkts;

                if ( sent > 0 && received > 0 ) {
                    mSentSinceLastRecv = 0;
                    newActivity = DctConstants.Activity.DATAINANDOUT;
                } else if (sent > 0 && received == 0) {
                    if (mPhone.getState()  ==PhoneConstants.State.IDLE) {
                        mSentSinceLastRecv += sent;
                    } else {
                        mSentSinceLastRecv = 0;
                    }
                    newActivity = DctConstants.Activity.DATAOUT;
                } else if (sent == 0 && received > 0) {
                    mSentSinceLastRecv = 0;
                    newActivity = DctConstants.Activity.DATAIN;
                } else if (sent == 0 && received == 0) {
                    newActivity = (mActivity == DctConstants.Activity.DORMANT) ?
                            mActivity : DctConstants.Activity.NONE;
                } else {
                    mSentSinceLastRecv = 0;
                    newActivity = (mActivity == DctConstants.Activity.DORMANT) ?
                            mActivity : DctConstants.Activity.NONE;
                }

                if (mActivity != newActivity && mIsScreenOn) {
                    mActivity = newActivity;
                    mPhone.notifyDataActivity();
                }
            }

            if (mSentSinceLastRecv >= NUMBER_SENT_PACKETS_OF_HANG) {
                // Packets sent without ack exceeded threshold.

                if (mNoRecvPollCount == 0) {
                    EventLog.writeEvent(
                            EventLogTags.PDP_RADIO_RESET_COUNTDOWN_TRIGGERED,
                            mSentSinceLastRecv);
                }

                if (mNoRecvPollCount < NO_RECV_POLL_LIMIT) {
                    mNoRecvPollCount++;
                    // Slow down the poll interval to let things happen
                    mNetStatPollPeriod = POLL_NETSTAT_SLOW_MILLIS;
                } else {
                    if (DBG) log("Sent " + String.valueOf(mSentSinceLastRecv) +
                                        " pkts since last received");
                    // We've exceeded the threshold.  Restart the radio.
                    mNetStatPollEnabled = false;
                    stopNetStatPoll();
                    restartRadio();
                    EventLog.writeEvent(EventLogTags.PDP_RADIO_RESET, NO_RECV_POLL_LIMIT);
                }
            } else {
                mNoRecvPollCount = 0;
                mNetStatPollPeriod = POLL_NETSTAT_MILLIS;
            }

            if (mNetStatPollEnabled) {
                mDataConnectionTracker.postDelayed(this, mNetStatPollPeriod);
            }
        }
    };

/**
* Returns true if the last fail cause is something that
* seems like it deserves an error notification.
//Synthetic comment -- @@ -770,6 +674,7 @@
if (mState == DctConstants.State.CONNECTED &&
!mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed()) {
stopNetStatPoll();
notifyDataConnection(Phone.REASON_VOICE_CALL_STARTED);
notifyOffApnsOfAvailability(Phone.REASON_VOICE_CALL_STARTED);
}
//Synthetic comment -- @@ -783,6 +688,7 @@
if (mState == DctConstants.State.CONNECTED) {
if (!mCdmaPhone.mSST.isConcurrentVoiceAndDataAllowed()) {
startNetStatPoll();
notifyDataConnection(Phone.REASON_VOICE_CALL_ENDED);
} else {
// clean slate after call end.
//Synthetic comment -- @@ -848,6 +754,7 @@
private void onCdmaDataDetached() {
if (mState == DctConstants.State.CONNECTED) {
startNetStatPoll();
notifyDataConnection(Phone.REASON_CDMA_DATA_DETACHED);
} else {
if (mState == DctConstants.State.FAILED) {
//Synthetic comment -- @@ -940,6 +847,7 @@
mActivity = DctConstants.Activity.NONE;
mPhone.notifyDataActivity();
startNetStatPoll();
break;

case DATA_CONNECTION_ACTIVE_PH_LINK_DOWN:
//Synthetic comment -- @@ -947,6 +855,7 @@
mActivity = DctConstants.Activity.DORMANT;
mPhone.notifyDataActivity();
stopNetStatPoll();
break;

default:
//Synthetic comment -- @@ -1043,6 +952,11 @@
}

@Override
protected void log(String s) {
Log.d(LOG_TAG, "[CdmaDCT] " + s);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index a1812f1..9782728 100644

//Synthetic comment -- @@ -82,7 +82,6 @@
*/
public final class GsmDataConnectionTracker extends DataConnectionTracker {
protected final String LOG_TAG = "GSM";
    private static final boolean RADIO_TESTS = false;

/**
* Handles changes to the APN db.
//Synthetic comment -- @@ -101,35 +100,7 @@
//***** Instance Variables

private boolean mReregisterOnReconnectFailure = false;
    private ContentResolver mResolver;

    // Recovery action taken in case of data stall
    private static class RecoveryAction {
        public static final int GET_DATA_CALL_LIST      = 0;
        public static final int CLEANUP                 = 1;
        public static final int REREGISTER              = 2;
        public static final int RADIO_RESTART           = 3;
        public static final int RADIO_RESTART_WITH_PROP = 4;

        private static boolean isAggressiveRecovery(int value) {
            return ((value == RecoveryAction.CLEANUP) ||
                    (value == RecoveryAction.REREGISTER) ||
                    (value == RecoveryAction.RADIO_RESTART) ||
                    (value == RecoveryAction.RADIO_RESTART_WITH_PROP));
        }
    }

    public int getRecoveryAction() {
        int action = Settings.System.getInt(mPhone.getContext().getContentResolver(),
                "radio.data.stall.recovery.action", RecoveryAction.GET_DATA_CALL_LIST);
        if (VDBG) log("getRecoveryAction: " + action);
        return action;
    }
    public void putRecoveryAction(int action) {
        Settings.System.putInt(mPhone.getContext().getContentResolver(),
                "radio.data.stall.recovery.action", action);
        if (VDBG) log("putRecoveryAction: " + action);
    }

//***** Constants

//Synthetic comment -- @@ -144,6 +115,8 @@
private static final String INTENT_DATA_STALL_ALARM =
"com.android.internal.telephony.gprs-data-stall";

static final Uri PREFERAPN_NO_UPDATE_URI =
Uri.parse("content://telephony/carriers/preferapn_no_update");
static final String APN_ID = "apn_id";
//Synthetic comment -- @@ -216,7 +189,6 @@
p.getContext().registerReceiver(mIntentReceiver, filter, null, p);

mDataConnectionTracker = this;
        mResolver = mPhone.getContext().getContentResolver();

mApnObserver = new ApnChangeObserver();
p.getContext().getContentResolver().registerContentObserver(
//Synthetic comment -- @@ -1394,85 +1366,6 @@
mActiveApn = null;
}

    private void resetPollStats() {
        mTxPkts = -1;
        mRxPkts = -1;
        mNetStatPollPeriod = POLL_NETSTAT_MILLIS;
    }

    private void doRecovery() {
        if (getOverallState() == DctConstants.State.CONNECTED) {
            // Go through a series of recovery steps, each action transitions to the next action
            int recoveryAction = getRecoveryAction();
            switch (recoveryAction) {
            case RecoveryAction.GET_DATA_CALL_LIST:
                EventLog.writeEvent(EventLogTags.DATA_STALL_RECOVERY_GET_DATA_CALL_LIST,
                        mSentSinceLastRecv);
                if (DBG) log("doRecovery() get data call list");
                mPhone.mCM.getDataCallList(obtainMessage(DctConstants.EVENT_DATA_STATE_CHANGED));
                putRecoveryAction(RecoveryAction.CLEANUP);
                break;
            case RecoveryAction.CLEANUP:
                EventLog.writeEvent(EventLogTags.DATA_STALL_RECOVERY_CLEANUP, mSentSinceLastRecv);
                if (DBG) log("doRecovery() cleanup all connections");
                cleanUpAllConnections(true, Phone.REASON_PDP_RESET);
                putRecoveryAction(RecoveryAction.REREGISTER);
                break;
            case RecoveryAction.REREGISTER:
                EventLog.writeEvent(EventLogTags.DATA_STALL_RECOVERY_REREGISTER,
                        mSentSinceLastRecv);
                if (DBG) log("doRecovery() re-register");
                mPhone.getServiceStateTracker().reRegisterNetwork(null);
                putRecoveryAction(RecoveryAction.RADIO_RESTART);
                break;
            case RecoveryAction.RADIO_RESTART:
                EventLog.writeEvent(EventLogTags.DATA_STALL_RECOVERY_RADIO_RESTART,
                        mSentSinceLastRecv);
                if (DBG) log("restarting radio");
                putRecoveryAction(RecoveryAction.RADIO_RESTART_WITH_PROP);
                restartRadio();
                break;
            case RecoveryAction.RADIO_RESTART_WITH_PROP:
                // This is in case radio restart has not recovered the data.
                // It will set an additional "gsm.radioreset" property to tell
                // RIL or system to take further action.
                // The implementation of hard reset recovery action is up to OEM product.
                // Once gsm.radioreset property is consumed, it is expected to set back
                // to false by RIL.
                EventLog.writeEvent(EventLogTags.DATA_STALL_RECOVERY_RADIO_RESTART_WITH_PROP, -1);
                if (DBG) log("restarting radio with gsm.radioreset to true");
                SystemProperties.set("gsm.radioreset", "true");
                // give 1 sec so property change can be notified.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                restartRadio();
                putRecoveryAction(RecoveryAction.GET_DATA_CALL_LIST);
                break;
            default:
                throw new RuntimeException("doRecovery: Invalid recoveryAction=" +
                    recoveryAction);
            }
        }
    }

    @Override
    protected void startNetStatPoll() {
        if (getOverallState() == DctConstants.State.CONNECTED && mNetStatPollEnabled == false) {
            if (DBG) log("startNetStatPoll");
            resetPollStats();
            mNetStatPollEnabled = true;
            mPollNetStat.run();
        }
    }

    @Override
    protected void stopNetStatPoll() {
        mNetStatPollEnabled = false;
        removeCallbacks(mPollNetStat);
        if (DBG) log("stopNetStatPoll");
    }

@Override
protected void restartRadio() {
if (DBG) log("restartRadio: ************TURN OFF RADIO**************");
//Synthetic comment -- @@ -1490,141 +1383,6 @@
SystemProperties.set("net.ppp.reset-by-timeout", String.valueOf(reset+1));
}


    private void updateDataStallInfo() {
        long sent, received;

        TxRxSum preTxRxSum = new TxRxSum(mDataStallTxRxSum);
        mDataStallTxRxSum.updateTxRxSum();

        if (VDBG) {
            log("updateDataStallInfo: mDataStallTxRxSum=" + mDataStallTxRxSum +
                    " preTxRxSum=" + preTxRxSum);
        }

        sent = mDataStallTxRxSum.txPkts - preTxRxSum.txPkts;
        received = mDataStallTxRxSum.rxPkts - preTxRxSum.rxPkts;

        if (RADIO_TESTS) {
            if (SystemProperties.getBoolean("radio.test.data.stall", false)) {
                log("updateDataStallInfo: radio.test.data.stall true received = 0;");
                received = 0;
            }
        }
        if ( sent > 0 && received > 0 ) {
            if (VDBG) log("updateDataStallInfo: IN/OUT");
            mSentSinceLastRecv = 0;
            putRecoveryAction(RecoveryAction.GET_DATA_CALL_LIST);
        } else if (sent > 0 && received == 0) {
            if (mPhone.getState() == PhoneConstants.State.IDLE) {
                mSentSinceLastRecv += sent;
            } else {
                mSentSinceLastRecv = 0;
            }
            if (DBG) {
                log("updateDataStallInfo: OUT sent=" + sent +
                        " mSentSinceLastRecv=" + mSentSinceLastRecv);
            }
        } else if (sent == 0 && received > 0) {
            if (VDBG) log("updateDataStallInfo: IN");
            mSentSinceLastRecv = 0;
            putRecoveryAction(RecoveryAction.GET_DATA_CALL_LIST);
        } else {
            if (VDBG) log("updateDataStallInfo: NONE");
        }
    }

    @Override
    protected void onDataStallAlarm(int tag) {
        if (mDataStallAlarmTag != tag) {
            if (DBG) {
                log("onDataStallAlarm: ignore, tag=" + tag + " expecting " + mDataStallAlarmTag);
            }
            return;
        }
        updateDataStallInfo();

        int hangWatchdogTrigger = Settings.Secure.getInt(mResolver,
                Settings.Secure.PDP_WATCHDOG_TRIGGER_PACKET_COUNT,
                NUMBER_SENT_PACKETS_OF_HANG);

        boolean suspectedStall = DATA_STALL_NOT_SUSPECTED;
        if (mSentSinceLastRecv >= hangWatchdogTrigger) {
            if (DBG) {
                log("onDataStallAlarm: tag=" + tag + " do recovery action=" + getRecoveryAction());
            }
            suspectedStall = DATA_STALL_SUSPECTED;
            sendMessage(obtainMessage(DctConstants.EVENT_DO_RECOVERY));
        } else {
            if (VDBG) {
                log("onDataStallAlarm: tag=" + tag + " Sent " + String.valueOf(mSentSinceLastRecv) +
                    " pkts since last received, < watchdogTrigger=" + hangWatchdogTrigger);
            }
        }
        startDataStallAlarm(suspectedStall);
    }


    private void updateDataActivity() {
        long sent, received;

        DctConstants.Activity newActivity;

        TxRxSum preTxRxSum = new TxRxSum(mTxPkts, mRxPkts);
        TxRxSum curTxRxSum = new TxRxSum();
        curTxRxSum.updateTxRxSum();
        mTxPkts = curTxRxSum.txPkts;
        mRxPkts = curTxRxSum.rxPkts;

        if (VDBG) {
            log("updateDataActivity: curTxRxSum=" + curTxRxSum + " preTxRxSum=" + preTxRxSum);
        }

        if (mNetStatPollEnabled && (preTxRxSum.txPkts > 0 || preTxRxSum.rxPkts > 0)) {
            sent = mTxPkts - preTxRxSum.txPkts;
            received = mRxPkts - preTxRxSum.rxPkts;

            if (VDBG) log("updateDataActivity: sent=" + sent + " received=" + received);
            if ( sent > 0 && received > 0 ) {
                newActivity = DctConstants.Activity.DATAINANDOUT;
            } else if (sent > 0 && received == 0) {
                newActivity = DctConstants.Activity.DATAOUT;
            } else if (sent == 0 && received > 0) {
                newActivity = DctConstants.Activity.DATAIN;
            } else {
                newActivity = (mActivity == DctConstants.Activity.DORMANT) ?
                                            mActivity : DctConstants.Activity.NONE;
            }

            if (mActivity != newActivity && mIsScreenOn) {
                if (VDBG) log("updateDataActivity: newActivity=" + newActivity);
                mActivity = newActivity;
                mPhone.notifyDataActivity();
            }
        }
    }

    private Runnable mPollNetStat = new Runnable()
    {
        @Override
        public void run() {
            updateDataActivity();

            if (mIsScreenOn) {
                mNetStatPollPeriod = Settings.Secure.getInt(mResolver,
                        Settings.Secure.PDP_WATCHDOG_POLL_INTERVAL_MS, POLL_NETSTAT_MILLIS);
            } else {
                mNetStatPollPeriod = Settings.Secure.getInt(mResolver,
                        Settings.Secure.PDP_WATCHDOG_LONG_POLL_INTERVAL_MS,
                        POLL_NETSTAT_SCREEN_OFF_MILLIS);
            }

            if (mNetStatPollEnabled) {
                mDataConnectionTracker.postDelayed(this, mNetStatPollPeriod);
            }
        }
    };

/**
* Returns true if the last fail cause is something that
* seems like it deserves an error notification.
//Synthetic comment -- @@ -1749,68 +1507,6 @@

}

    private void startDataStallAlarm(boolean suspectedStall) {
        int nextAction = getRecoveryAction();
        int delayInMs;

        // If screen is on or data stall is currently suspected, set the alarm
        // with an aggresive timeout.
        if (mIsScreenOn || suspectedStall || RecoveryAction.isAggressiveRecovery(nextAction)) {
            delayInMs = Settings.Secure.getInt(mResolver,
                                       Settings.Secure.DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS,
                                       DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS_DEFAULT);
        } else {
            delayInMs = Settings.Secure.getInt(mResolver,
                                       Settings.Secure.DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS,
                                       DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS_DEFAULT);
        }

        mDataStallAlarmTag += 1;
        if (VDBG) {
            log("startDataStallAlarm: tag=" + mDataStallAlarmTag +
                    " delay=" + (delayInMs / 1000) + "s");
        }
        AlarmManager am =
            (AlarmManager) mPhone.getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(INTENT_DATA_STALL_ALARM);
        intent.putExtra(DATA_STALL_ALARM_TAG_EXTRA, mDataStallAlarmTag);
        mDataStallAlarmIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + delayInMs, mDataStallAlarmIntent);
    }

    private void stopDataStallAlarm() {
        AlarmManager am =
            (AlarmManager) mPhone.getContext().getSystemService(Context.ALARM_SERVICE);

        if (VDBG) {
            log("stopDataStallAlarm: current tag=" + mDataStallAlarmTag +
                    " mDataStallAlarmIntent=" + mDataStallAlarmIntent);
        }
        mDataStallAlarmTag += 1;
        if (mDataStallAlarmIntent != null) {
            am.cancel(mDataStallAlarmIntent);
            mDataStallAlarmIntent = null;
        }
    }

    @Override
    protected void restartDataStallAlarm() {
        if (isConnected() == false) return;
        // To be called on screen status change.
        // Do not cancel the alarm if it is set with aggressive timeout.
        int nextAction = getRecoveryAction();

        if (RecoveryAction.isAggressiveRecovery(nextAction)) {
            if (DBG) log("data stall recovery action is pending. not resetting the alarm.");
            return;
        }
        stopDataStallAlarm();
        startDataStallAlarm(DATA_STALL_NOT_SUSPECTED);
    }

private void notifyNoData(GsmDataConnection.FailCause lastFailCauseCode,
ApnContext apnContext) {
if (DBG) log( "notifyNoData: type=" + apnContext.getApnType());
//Synthetic comment -- @@ -2696,9 +2392,7 @@
public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
pw.println("GsmDataConnectionTracker extends:");
super.dump(fd, pw, args);
        pw.println(" RADIO_TESTS=" + RADIO_TESTS);
pw.println(" mReregisterOnReconnectFailure=" + mReregisterOnReconnectFailure);
        pw.println(" mResolver=" + mResolver);
pw.println(" canSetPreferApn=" + canSetPreferApn);
pw.println(" mApnObserver=" + mApnObserver);
pw.println(" getOverallState=" + getOverallState());







