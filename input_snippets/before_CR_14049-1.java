
//<Beginning of snippet n. 0>


private Uri insertMessage(Context context, SmsMessage[] msgs) {
// Build the helper classes to parse the messages.
SmsMessage sms = msgs[0];

if (sms.getMessageClass() == SmsMessage.MessageClass.CLASS_0) {
displayClassZeroMessage(context, sms);

//<End of snippet n. 0>








