/*SMS delivery reports fix.
Correction of SMS status codes. See TS 23.040 9.2.3.15.
Delivery intents will not be removed from list of pending intents for
temporary delivery failures.*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index 61ab228..7dabcd4 100644

//Synthetic comment -- @@ -104,8 +104,8 @@

public static final int STATUS_NONE = -1;
public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_PENDING = 64;
        public static final int STATUS_FAILED = 128;

/**
* The subject of the message, if present








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/SMSDispatcher.java
//Synthetic comment -- index 5585524..9fefd4d 100644

//Synthetic comment -- @@ -332,13 +332,18 @@
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
fillIn.putExtra("pdu", SimUtils.hexStringToBytes(pduString));







