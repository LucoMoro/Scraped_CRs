/*Telephony: Implement synchronous sending of sms messages

The code reads persist.radio.sms_sync_sending system property
to determine if synchronous sending algorithm shoud be used.
When it is set to true SmsDispacher will wait for a message to be
sent before sending request for subsequent message while queueing all
messages that are requested to be sent. When this propery is not set
or is set to false the SmsDispatcher behaves as it used to by sending
all messages as soon as it receives the request to send them.

Change-Id:I1e80ff4eede936a95e1a96d54bfb66dd4008dcd1*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ca526a5..cdb43ab 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.provider.Telephony.Sms.Intents;
import android.provider.Settings;
//Synthetic comment -- @@ -161,6 +162,12 @@
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

    /** List of messages awaiting to be sent.
     * Message at index 0 is the one currently being sent
     */
    private ArrayList<SmsTracker> mPendingMessagesList;
    private boolean mSyncronousSending;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;
//Synthetic comment -- @@ -240,6 +247,11 @@
DEFAULT_SMS_MAX_COUNT);
mCounter = new SmsCounter(max_count, check_period);

        mPendingMessagesList = new ArrayList<SmsTracker>();
        mSyncronousSending = SystemProperties.getBoolean(
                TelephonyProperties.SMS_SYNCHRONOUS_SENDING,
                false);

mCm.setOnNewSMS(this, EVENT_NEW_SMS, null);
mCm.setOnSmsStatus(this, EVENT_NEW_SMS_STATUS_REPORT, null);
mCm.setOnIccSmsFull(this, EVENT_ICC_FULL, null);
//Synthetic comment -- @@ -476,6 +488,11 @@
sentIntent.send(Activity.RESULT_OK);
} catch (CanceledException ex) {}
}

            if (mSyncronousSending) {
                processNextPendingMessage();
            }

} else {
if (Config.LOGD) {
Log.d(TAG, "SMS send failed");
//Synthetic comment -- @@ -487,7 +504,7 @@
handleNotInService(ss, tracker);
} else if ((((CommandException)(ar.exception)).getCommandError()
== CommandException.Error.SMS_FAIL_RETRY) &&
                    tracker.mRetryCount < MAX_SEND_RETRIES) {
// Retry after a delay if needed.
// TODO: According to TS 23.040, 9.2.3.6, we should resend
//       with the same TP-MR as the failed message, and
//Synthetic comment -- @@ -515,6 +532,10 @@
tracker.mSentIntent.send(mContext, error, fillIn);

} catch (CanceledException ex) {}

                if (mSyncronousSending) {
                    processNextPendingMessage();
                }
}
}
}
//Synthetic comment -- @@ -798,7 +819,11 @@
} else {
String appName = getAppNameByIntent(sentIntent);
if (mCounter.check(appName, SINGLE_PART_SMS)) {
                if (mSyncronousSending) {
                    enqueueMessageForSending(tracker);
                } else {
                    sendSms(tracker);
                }
} else {
sendMessage(obtainMessage(EVENT_POST_ALERT, tracker));
}
//Synthetic comment -- @@ -972,27 +997,55 @@
}
};

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_DEVICE_STORAGE_LOW)) {
                mStorageAvailable = false;
                mCm.reportSmsMemoryStatus(false, obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
            } else if (intent.getAction().equals(Intent.ACTION_DEVICE_STORAGE_OK)) {
                mStorageAvailable = true;
                mCm.reportSmsMemoryStatus(true, obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
            } else {
                // Assume the intent is one of the SMS receive intents that
                // was sent as an ordered broadcast.  Check result and ACK.
                int rc = getResultCode();
                boolean success = (rc == Activity.RESULT_OK)
                                    || (rc == Intents.RESULT_SMS_HANDLED);

                // For a multi-part message, this only ACKs the last part.
                // Previous parts were ACK'd as they were received.
                acknowledgeLastIncomingSms(success, rc, null);
            }
        }
    };

    private void processNextPendingMessage() {
        synchronized (mPendingMessagesList) {
            // Remove sent message from the list
            if (mPendingMessagesList.size() > 0) {
                mPendingMessagesList.remove(0);
                Log.d(TAG, "Removed message from pending queue. " + mPendingMessagesList.size() + " left");
            } else {
                Log.e(TAG, "Pending messages list consistency failure detected!");
}

            // If there are more messages waiting to be sent - send next one
            if (mPendingMessagesList.size() > 0) {
                sendSms(mPendingMessagesList.get(0));
            }
        }
    }

    private void enqueueMessageForSending(SmsTracker tracker) {
        synchronized (mPendingMessagesList) {
            mPendingMessagesList.add(tracker);
            Log.d(TAG, "Added message to the pending queue. Queue size is " + mPendingMessagesList.size());
            //Trigger sending only if there are no other messages being sent right now
            //i.e. the queue was empty before we added this message to it
            if (mPendingMessagesList.size() == 1) {
                sendSms(tracker);
            }
        }
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index a113787..7b02698 100644

//Synthetic comment -- @@ -147,4 +147,10 @@
* when there is a radio technology change.
*/
static final String PROPERTY_RESET_ON_RADIO_TECH_CHANGE = "persist.radio.reset_on_switch";

    /**
     * Indicates whether sms should be sent in synchronous manner.
     * Type: boolean ( false = asynchronous, true = send next queued sms after
     * making sure previous sms was sent) */
    static final String SMS_SYNCHRONOUS_SENDING = "persist.radio.sms_sync_sending";
}







