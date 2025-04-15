/*Fix the Multi-page SMS sending error to several receipents

Change-Id:Iefde94b638413e3c1761f17c3065b20a044e5958Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
old mode 100644
new mode 100755
//Synthetic comment -- index 917e1d8..4247113

//Synthetic comment -- @@ -65,6 +65,7 @@

public abstract class SMSDispatcher extends Handler {
private static final String TAG = "SMS";
    private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";

/** Default checking period for SMS sent without user permit */
private static final int DEFAULT_SMS_CHECK_PERIOD = 3600000;
//Synthetic comment -- @@ -153,6 +154,8 @@
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

    protected static int mRemainingMessages = -1;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;
//Synthetic comment -- @@ -463,7 +466,17 @@

if (sentIntent != null) {
try {
                    if (mRemainingMessages > -1) {
                        mRemainingMessages--;
                    }

                    if (mRemainingMessages == 0) {
                        Intent sendNext = new Intent();
                        sendNext.putExtra(SEND_NEXT_MSG_EXTRA, true);
                        sentIntent.send(mContext, Activity.RESULT_OK, sendNext);
                    } else {
                        sentIntent.send(Activity.RESULT_OK);
                    }
} catch (CanceledException ex) {}
}
} else {
//Synthetic comment -- @@ -502,8 +515,15 @@
if (ar.result != null) {
fillIn.putExtra("errorCode", ((SmsResponse)ar.result).errorCode);
}
                    if (mRemainingMessages > -1) {
                        mRemainingMessages--;
                    }

                    if (mRemainingMessages == 0) {
                        fillIn.putExtra(SEND_NEXT_MSG_EXTRA, true);
                    }

                    tracker.mSentIntent.send(mContext, RESULT_ERROR_GENERIC_FAILURE, fillIn);
} catch (CanceledException ex) {}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 3079a64..e0d137b 100644

//Synthetic comment -- @@ -175,6 +175,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemainingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize
//Synthetic comment -- @@ -264,6 +266,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemainingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize







