/*Fix the Multi-page SMS sending error to several receipents

Change-Id:Iefde94b638413e3c1761f17c3065b20a044e5958Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
old mode 100644
new mode 100755
//Synthetic comment -- index 917e1d8..c1fb27b9

//Synthetic comment -- @@ -153,6 +153,8 @@
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

    protected static int mRemaingMessages = -1;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;
//Synthetic comment -- @@ -463,7 +465,16 @@

if (sentIntent != null) {
try {
                    if(mRemaingMessages > -1)
                        mRemaingMessages--;

                    if(mRemaingMessages == 0) {
                        Intent sendNext = new Intent();
                        sendNext.putExtra("SendNextMsg", true);
                        sentIntent.send(mContext, Activity.RESULT_OK, sendNext);
                    } else {
                        sentIntent.send(Activity.RESULT_OK);
                    }
} catch (CanceledException ex) {}
}
} else {
//Synthetic comment -- @@ -502,8 +513,13 @@
if (ar.result != null) {
fillIn.putExtra("errorCode", ((SmsResponse)ar.result).errorCode);
}
                    if(mRemaingMessages > -1)
                        mRemaingMessages--;

                    if(mRemaingMessages == 0)
                        fillIn.putExtra("SendNextMsg", true);

                    tracker.mSentIntent.send(mContext, RESULT_ERROR_GENERIC_FAILURE, fillIn);
} catch (CanceledException ex) {}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 3079a64..99d7774 100644

//Synthetic comment -- @@ -175,6 +175,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemaingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize
//Synthetic comment -- @@ -264,6 +266,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemaingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize







