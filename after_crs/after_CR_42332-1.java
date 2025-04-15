/*Make Pop3Store locale safe

Using regular string concatenation instead of default String.format
since it is locale safe, and String.format was not used for anything
fancy.

Change-Id:I4995691518cc41b4751840f42808d414f0d6be3d*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/store/Pop3Store.java b/src/com/android/email/mail/store/Pop3Store.java
//Synthetic comment -- index b3ee420..ea3685e 100644

//Synthetic comment -- @@ -722,8 +722,8 @@
"Pop3Store.fetch called with non-Pop3 Message");
}
Pop3Message pop3Message = (Pop3Message)message;
                    String response = executeSimpleCommand("LIST " +
                            mUidToMsgNumMap.get(pop3Message.getUid()));
try {
String[] listParts = response.split(" ");
int msgNum = Integer.parseInt(listParts[1]);
//Synthetic comment -- @@ -786,13 +786,13 @@
int messageId = mUidToMsgNumMap.get(message.getUid());
if (lines == -1) {
// Fetch entire message
                response = executeSimpleCommand("RETR " + messageId);
} else {
// Fetch partial message.  Try "TOP", and fall back to slower "RETR" if necessary
try {
                    response = executeSimpleCommand("TOP " + messageId + ' ' + lines);
} catch (MessagingException me) {
                    response = executeSimpleCommand("RETR " + messageId);
}
}
if (response != null)  {
//Synthetic comment -- @@ -846,8 +846,7 @@
}
try {
for (Message message : messages) {
                    executeSimpleCommand("DELE " + mUidToMsgNumMap.get(message.getUid()));
}
}
catch (IOException ioe) {







