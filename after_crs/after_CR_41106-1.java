/*Telephony: Don't ack CB messages

SmsDispatcher receives its own broadcast to ack messages.
CB message needs to be excluded from the acking code since
it should not be acked.

Change-Id:I3f1496bb0c81a8edcc4173661e2ca75b03a9c6fb*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..8694a09 100644

//Synthetic comment -- @@ -1224,15 +1224,20 @@
private final BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intents.SMS_CB_RECEIVED_ACTION) ||
                    intent.getAction().equals(Intents.SMS_EMERGENCY_CB_RECEIVED_ACTION)) {
                // Ignore this intent. Apps will process it.
            } else {
                // Assume the intent is one of the SMS receive intents that
                // was sent as an ordered broadcast. Check result and ACK.
                int rc = getResultCode();
                boolean success = (rc == Activity.RESULT_OK)
                        || (rc == Intents.RESULT_SMS_HANDLED);

                // For a multi-part message, this only ACKs the last part.
                // Previous parts were ACK'd as they were received.
                acknowledgeLastIncomingSms(success, rc, null);
            }
}
};








