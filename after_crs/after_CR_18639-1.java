/*Fix for missing status reports for delayed messages

When an SMS is sent to a phone that is not turned on, the sending
phone receives a status report saying that the message is pending.
The pending intent that is to be matched with the status report
should then not be removed from the list but rather remain in the
list to await a success or fail status report.

Note that the status report notification will still be lost if the
sending phone is turned off before receiving the status report.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index d720516..a30763a 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Intents;
import android.telephony.ServiceState;
import android.util.Config;
//Synthetic comment -- @@ -66,8 +67,11 @@
for (int i = 0, count = deliveryPendingList.size(); i < count; i++) {
SmsTracker tracker = deliveryPendingList.get(i);
if (tracker.mMessageRef == messageRef) {
                    // Found it.  Remove from list if not pending, then broadcast.
                    int status = sms.getStatus();
                    if (status < Sms.STATUS_PENDING || status >= Sms.STATUS_FAILED) {
                        deliveryPendingList.remove(i);
                    }
PendingIntent intent = tracker.mDeliveryIntent;
Intent fillIn = new Intent();
fillIn.putExtra("pdu", IccUtils.hexStringToBytes(pduString));







