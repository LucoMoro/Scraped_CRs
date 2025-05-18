//<Beginning of snippet n. 0>
private Uri insertMessage(Context context, SmsMessage[] msgs) {
    SmsMessage sms = msgs[0];

    if (sms.getMessageClass() == SmsMessage.MessageClass.CLASS_0) {
        return null; // Acknowledge receipt and exit for Type Zero messages
    }

    // Continue with further processing for non-Type Zero messages
    // ...
//<End of snippet n. 0>