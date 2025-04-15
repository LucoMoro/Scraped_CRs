/*Workaround for Issue 4193: Email read/unread marker work wrong

Fixes problem where pop3 account messages get marked unread after server sync if they have
attachments or images and have been marked read from the message list in lieu of entering
the email.

Change-Id:I5363316f44cbd22eb047ba88d99c8a80ceab0dd7*/
//Synthetic comment -- diff --git a/src/com/android/email/MessagingController.java b/src/com/android/email/MessagingController.java
old mode 100644
new mode 100755
//Synthetic comment -- index c04280f..b3ea4db

//Synthetic comment -- @@ -566,16 +566,23 @@
* Note, we also skip syncing messages which are flagged as "deleted message" sentinels,
* because they are locally deleted and we don't need or want the old message from
* the server.
*/
for (Message message : remoteMessages) {
LocalMessageInfo localMessage = localMessageMap.get(message.getUid());
if (localMessage == null) {
newMessageCount++;
}
                if (localMessage == null
                        || (localMessage.mFlagLoaded == EmailContent.Message.FLAG_LOADED_UNLOADED)
                        || (localMessage.mFlagLoaded == EmailContent.Message.FLAG_LOADED_PARTIAL)) {
                    unsyncedMessages.add(message);
}
}
}







