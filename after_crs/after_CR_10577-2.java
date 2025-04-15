/*When replying in Email, do not set message id on new message.

When replying in Email application, the application deliberately
set the message id of the new message as message id of the original
message. The reason for this was to identify multiple draft messages.
However, I have found out this is not necessary. Moreover, when sending
a new message with the same message id, many mail stores do not accept the
new message at all, so this is a quite serious bug.

This has been reported as issue #3030.*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
//Synthetic comment -- index 02024dc..b88632f 100644

//Synthetic comment -- @@ -588,15 +588,6 @@
message.setRecipients(RecipientType.BCC, getAddresses(mBccView));
message.setSubject(mSubjectView.getText().toString());

/*
* Build the Body that will contain the text of the message. We'll decide where to
* include it later.







