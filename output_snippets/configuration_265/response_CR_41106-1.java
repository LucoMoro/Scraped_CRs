//<Beginning of snippet n. 0>
private final BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && SmsMessage.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            int rc = getResultCode();
            boolean success = (rc == Activity.RESULT_OK) || (rc == Intents.RESULT_SMS_HANDLED);

            // Exclude Cell Broadcast (CB) messages
            if (!isCellBroadcast(intent)) {
                acknowledgeLastIncomingSms(success, rc, null);
            }
        }
    }

    private boolean isCellBroadcast(Intent intent) {
        // Check if the incoming message is a Cell Broadcast message.
        return intent.getStringExtra("msg_id") != null && intent.getStringExtra("msg_id").equals("CB_MESSAGE_ID");
    }
};
//<End of snippet n. 0>