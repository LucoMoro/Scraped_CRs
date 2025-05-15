
//<Beginning of snippet n. 0>


private Uri insertMessage(Context context, SmsMessage[] msgs) {
// Build the helper classes to parse the messages.
SmsMessage sms = msgs[0];
        final int TYPE_ZERO_MSG = 0x40;

        // As per 3GPP TS 23.040 section 9.2.3.9, Type Zero messages, indicated by TP_PID
        // field set to value 0x40, should not be Displayed/Stored/Notified. They should
        // only be acknowledged which is done in the frameworks. For CDMA,
        // getProtocolIdentifier() returns zero, hence this code will not be executed.
        if (sms.getProtocolIdentifier() == TYPE_ZERO_MSG) {
            Log.i(TAG, "Received Type Zero Message, Ignoring It");
            return null;
        }

if (sms.getMessageClass() == SmsMessage.MessageClass.CLASS_0) {
displayClassZeroMessage(context, sms);

//<End of snippet n. 0>








