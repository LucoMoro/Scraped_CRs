/*Fix the error for delivery report with PENDING status

1. According to TS 23.040, TP-Status values is changed properly.
2. When processing Status Report, it should be checked whether tpStatus is PENDING or FAILED.

Change-Id:I9bab708219f4ea6f5dcca753ccf202997e8c270f*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index 6d8bd9b..940bd24 100644

//Synthetic comment -- @@ -114,8 +114,8 @@

public static final int STATUS_NONE = -1;
public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_PENDING = 64;
        public static final int STATUS_FAILED = 128;

/**
* The subject of the message, if present








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 3079a64..ecc8f2d 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.provider.Telephony.Sms.Intents;
import android.telephony.ServiceState;
import android.util.Config;
//Synthetic comment -- @@ -59,6 +60,7 @@
protected void handleStatusReport(AsyncResult ar) {
String pduString = (String) ar.result;
SmsMessage sms = SmsMessage.newFromCDS(pduString);

if (sms != null) {
int messageRef = sms.messageRef;
//Synthetic comment -- @@ -66,7 +68,9 @@
SmsTracker tracker = deliveryPendingList.get(i);
if (tracker.mMessageRef == messageRef) {
// Found it.  Remove from list and broadcast.
                    deliveryPendingList.remove(i);
PendingIntent intent = tracker.mDeliveryIntent;
Intent fillIn = new Intent();
fillIn.putExtra("pdu", IccUtils.hexStringToBytes(pduString));







