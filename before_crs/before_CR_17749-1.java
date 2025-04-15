/*workaround for Issue 4193: Email read/unread marker work wrong

Fixes problem where pop3 account messages get marked unread after server sync if they have
attachments or images and have been marked read from the message list in lieu of entering
the email.

Change-Id:I562e1731fea00f980a2f1d5e63bd288b07c8ff95*/
//Synthetic comment -- diff --git a/src/com/android/email/MessagingController.java b/src/com/android/email/MessagingController.java
//Synthetic comment -- index 2a1a3b6..c2b9b81 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.email;

import com.android.email.mail.BodyPart;
import com.android.email.mail.FetchProfile;
import com.android.email.mail.Flag;
import com.android.email.mail.Folder;
//Synthetic comment -- @@ -573,9 +572,13 @@
if (localMessage == null) {
newMessageCount++;
}
                if (localMessage == null
                        || (localMessage.mFlagLoaded == EmailContent.Message.FLAG_LOADED_UNLOADED)
                        || (localMessage.mFlagLoaded == EmailContent.Message.FLAG_LOADED_PARTIAL)) {
unsyncedMessages.add(message);
}
}







