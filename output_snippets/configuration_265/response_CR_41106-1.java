//<Beginning of snippet n. 0>


private final BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
            // Check if the intent is a Cell Broadcast message
            if (intent.getAction().equals("android.provider.Telephony.SMS_CB_RECEIVED")) {
                return; // Skip acknowledgment for CB messages
            }

            // Assume the intent is one of the SMS receive intents that
            // was sent as an ordered broadcast. Check result and ACK.
            int rc = getResultCode();
            boolean success = (rc == Activity.RESULT_OK)
                    || (rc == Intents.RESULT_SMS_HANDLED);

            // For a multi-part message, this only ACKs the last part.
            // Previous parts were ACK'd as they were received.
            acknowledgeLastIncomingSms(success, rc, null);
}
};


//<End of snippet n. 0>