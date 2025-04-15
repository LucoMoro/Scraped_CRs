/*Fixing problem with retrieving a single SMS from an ICC slot

The parameter to content://sms/icc/# is supposed to be
the one-based ICC record index, but instead it was
treated as the index of the local list holding the messages.
If the SIM has any empty or non-supported records,
these two indices will not match.

For example if the SIM has 20 slots of which only slot 7 and 11 are
used, SmsManager.getAllMessagesFromIcc will return a list of 2 elements.
When retrieving message 7, we must not try to get element number 7
in that list but rather iterate through the list to find the message
that has the _SIM record index_ 7.

Change-Id:I086e81d564f0eb1f65bd8f59abfa9ccab8225da0*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/SmsProvider.java b/src/com/android/providers/telephony/SmsProvider.java
//Synthetic comment -- index 15e008d..7ff3433 100644

//Synthetic comment -- @@ -249,6 +249,16 @@
return row;
}

    private SmsMessage getMessageWithIccIndex(ArrayList<SmsMessage> messages, int messageIndex) {
        for (SmsMessage message : messages) {
            if (message != null && message.getIndexOnIcc() == messageIndex) {
                return message;
            }
        }

        return null;
    }

/**
* Return a Cursor containing just one message from the ICC.
*/
//Synthetic comment -- @@ -258,7 +268,7 @@
SmsManager smsManager = SmsManager.getDefault();
ArrayList<SmsMessage> messages = smsManager.getAllMessagesFromIcc();

            SmsMessage message = getMessageWithIccIndex(messages, messageIndex);
if (message == null) {
throw new IllegalArgumentException(
"Message not retrieved. ID: " + messageIndexString);







