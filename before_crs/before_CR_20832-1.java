/*Fix delivery report error with PENDING status in SMS

1. According to TS 23.040, TP-Status values is changed properly.
2. When processing Status Report, it should be checked whether tpStatus is PENDING or FAILED.

Change-Id:I91c315cfb363f3e4b936c6b6b1a01083687a580f*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6d8bd9b..940bd24

//Synthetic comment -- @@ -114,8 +114,8 @@

public static final int STATUS_NONE = -1;
public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_PENDING = 64;
        public static final int STATUS_FAILED = 128;

/**
* The subject of the message, if present








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3079a64..49fa5da

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.provider.Telephony.Sms.Intents;
import android.telephony.ServiceState;
import android.util.Config;
//Synthetic comment -- @@ -60,13 +61,17 @@
String pduString = (String) ar.result;
SmsMessage sms = SmsMessage.newFromCDS(pduString);

if (sms != null) {
int messageRef = sms.messageRef;
for (int i = 0, count = deliveryPendingList.size(); i < count; i++) {
SmsTracker tracker = deliveryPendingList.get(i);
if (tracker.mMessageRef == messageRef) {
// Found it.  Remove from list and broadcast.
                    deliveryPendingList.remove(i);
PendingIntent intent = tracker.mDeliveryIntent;
Intent fillIn = new Intent();
fillIn.putExtra("pdu", IccUtils.hexStringToBytes(pduString));







