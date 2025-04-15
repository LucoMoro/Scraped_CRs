/*Fix the Multi-Page SMS Sending error to several receipents

Change-Id:I52a5817c6a56fcb61cbc81313c2acea97c8a93b9*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 917e1d8..52032b4 100644

//Synthetic comment -- @@ -153,6 +153,8 @@
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;
//Synthetic comment -- @@ -463,7 +465,18 @@

if (sentIntent != null) {
try {
                    sentIntent.send(Activity.RESULT_OK);
} catch (CanceledException ex) {}
}
} else {
//Synthetic comment -- @@ -502,8 +515,13 @@
if (ar.result != null) {
fillIn.putExtra("errorCode", ((SmsResponse)ar.result).errorCode);
}
                    tracker.mSentIntent.send(mContext, error, fillIn);

} catch (CanceledException ex) {}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 5dacd64..066242b 100644

//Synthetic comment -- @@ -179,6 +179,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize
//Synthetic comment -- @@ -268,6 +270,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize







