/*Telephony: Set TP-RD to 1 in case of retry

per TS 23.040 Section 9.2.3.6: In the case where no response or an
RP-ERROR with an appropriate cause value (see 3GPP TS 24.011 [13]) is
received in response to an SMS-SUBMIT, then the MS shall automatically
repeat the SMS-SUBMIT but must use the same TP-MR value
and set the TP-RD bit to 1 (see 9.2.3.25)

Change-Id:I1c7ebc67f80a6eeba456ab0dc5f6331a038d7ab2*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..3bfc116 100644

//Synthetic comment -- @@ -366,6 +366,12 @@
SmsTracker tracker = (SmsTracker) ar.userObj;
PendingIntent sentIntent = tracker.mSentIntent;

if (ar.exception == null) {
if (false) {
Log.d(TAG, "SMS send complete. Broadcasting "
//Synthetic comment -- @@ -374,8 +380,6 @@

if (tracker.mDeliveryIntent != null) {
// Expecting a status report.  Add it to the list.
                int messageRef = ((SmsResponse)ar.result).messageRef;
                tracker.mMessageRef = messageRef;
deliveryPendingList.add(tracker);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/src/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index d6c2a20..cc7ea4e 100644

//Synthetic comment -- @@ -293,6 +293,21 @@
byte pdu[] = (byte[]) map.get("pdu");

Message reply = obtainMessage(EVENT_SEND_SMS_COMPLETE, tracker);
mCm.sendSMS(IccUtils.bytesToHexString(smsc), IccUtils.bytesToHexString(pdu), reply);
}








