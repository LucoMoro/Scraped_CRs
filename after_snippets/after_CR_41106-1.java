
//<Beginning of snippet n. 0>


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


//<End of snippet n. 0>








