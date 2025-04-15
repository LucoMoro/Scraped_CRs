/*Email widget displays valid emails then lots of empty entries with 01/01/1970

In the next situation:
- POP3 Account
- No remove messages from mail server Email account setting
- Single mailbox is selected

When a mail is deleted from local inbox, the mail is dropped to trash folder, but a null message
flagged with deleted status is created to allow sincronization with the pop3 server to not download
the message again. When the message is purged from server, the null message is also removed
from inbox

The mail activity make use of Message.FLAG_LOADED_SELECTION when query the mail database,
and is not showing this null messages, but the email widget, on "Simple mailbox selection" is
not using Message.FLAG_LOADED_SELECTION flag to discriminate this "null messages".

Change-Id:I35a147738167847834ab3898141bbd9817cd71cd*/
//Synthetic comment -- diff --git a/src/com/android/email/widget/EmailWidgetLoader.java b/src/com/android/email/widget/EmailWidgetLoader.java
//Synthetic comment -- index 71e3608..6d30fe2 100644

//Synthetic comment -- @@ -192,7 +192,7 @@
// Simple mailbox selection
setSelection(
MessageColumns.ACCOUNT_KEY + "=? AND " +
                    MessageColumns.MAILBOX_KEY + "=?");
setSelectionArgs(
new String[] { Long.toString(mAccountId), Long.toString(mMailboxId) });
} else {







