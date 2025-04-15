/*Retry pending memory status update to modem when radio is ON.

When radio is powered off / airplane mode, memory status updates are
ignored by RIL. With this fix, pending memory status updates are sent
again when radio is powered back on.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 53c0bef..fc0aa06 100644

//Synthetic comment -- @@ -109,6 +109,12 @@
/** Stop the sending */
static final protected int EVENT_STOP_SENDING = 10;

    /** Memory status reporting is acknowledged by RIL */
    static final protected int EVENT_REPORT_MEMORY_STATUS_DONE = 11;

    /** Radio is ON */
    static final protected int EVENT_RADIO_ON = 12;

protected Phone mPhone;
protected Context mContext;
protected ContentResolver mResolver;
//Synthetic comment -- @@ -152,6 +158,7 @@
private SmsMessageBase.SubmitPduBase mSubmitPduBase;

protected boolean mStorageAvailable = true;
    protected boolean mReportMemoryStatusPending = false;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
//Synthetic comment -- @@ -235,6 +242,7 @@
mCm.setOnNewSMS(this, EVENT_NEW_SMS, null);
mCm.setOnSmsStatus(this, EVENT_NEW_SMS_STATUS_REPORT, null);
mCm.setOnIccSmsFull(this, EVENT_ICC_FULL, null);
        mCm.registerForOn(this, EVENT_RADIO_ON, null);

// Don't always start message ref at 0.
sConcatenatedRef = new Random().nextInt(256);
//Synthetic comment -- @@ -253,6 +261,7 @@
mCm.unSetOnNewSMS(this);
mCm.unSetOnSmsStatus(this);
mCm.unSetOnIccSmsFull(this);
        mCm.unregisterForOn(this);
}

protected void finalize() {
//Synthetic comment -- @@ -370,6 +379,26 @@
removeMessages(EVENT_ALERT_TIMEOUT, msg.obj);
}
break;

        case EVENT_REPORT_MEMORY_STATUS_DONE:
            ar = (AsyncResult)msg.obj;
            if (ar.exception != null) {
                mReportMemoryStatusPending = true;
                Log.v(TAG, "Memory status report to modem pending : mStorageAvailable = "
                        + mStorageAvailable);
            } else {
                mReportMemoryStatusPending = false;
            }
            break;

        case EVENT_RADIO_ON:
            if (mReportMemoryStatusPending) {
                Log.v(TAG, "Sending pending memory status report : mStorageAvailable = "
                        + mStorageAvailable);
                mCm.reportSmsMemoryStatus(mStorageAvailable,
                        obtainMessage(EVENT_REPORT_MEMORY_STATUS_DONE));
            }
            break;
}
}

//Synthetic comment -- @@ -940,10 +969,10 @@
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







