/*Mms: Fix ic_dialog_alert usage

The code wasn't updated for ICS everywhere so it points to
old upscaled Gingerbread drawables in a few places.

Change-Id:I8224c6cce285d42fa0ba3317367a0b025050a83c*/




//Synthetic comment -- diff --git a/src/com/android/mms/LogTag.java b/src/com/android/mms/LogTag.java
//Synthetic comment -- index 2f3db57..79db5b1 100644

//Synthetic comment -- @@ -104,7 +104,7 @@
activity.runOnUiThread(new Runnable() {
public void run() {
new AlertDialog.Builder(activity)
                        .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(R.string.error_state)
.setMessage(msg + "\n\n" + activity.getString(R.string.error_state_text))
.setPositiveButton(R.string.yes, new OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index cb5b634..cf00a60 100644

//Synthetic comment -- @@ -560,7 +560,7 @@
String title = getResourcesString(R.string.has_invalid_recipient,
mRecipientsEditor.formatInvalidNumbers(isMms));
new AlertDialog.Builder(this)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(title)
.setMessage(R.string.invalid_recipient_message)
.setPositiveButton(R.string.try_to_send,
//Synthetic comment -- @@ -569,7 +569,7 @@
.show();
} else {
new AlertDialog.Builder(this)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(R.string.cannot_send_message)
.setMessage(R.string.cannot_send_message_reason)
.setPositiveButton(R.string.yes, new CancelSendingListener())
//Synthetic comment -- @@ -2757,7 +2757,7 @@
if (recipientLimit != Integer.MAX_VALUE && recipientCount > recipientLimit) {
new AlertDialog.Builder(this)
.setTitle(R.string.pick_too_many_recipients)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setMessage(getString(R.string.too_many_recipients, recipientCount, recipientLimit))
.setPositiveButton(android.R.string.ok, null)
.create().show();








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index 2f75211..9a33489 100644

//Synthetic comment -- @@ -308,7 +308,7 @@
private void confirmDeleteDialog(OnClickListener listener, int messageId) {
AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle(R.string.confirm_dialog_title);
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
builder.setCancelable(true);
builder.setPositiveButton(R.string.yes, listener);
builder.setNegativeButton(R.string.no, null);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index a14550a..caef367 100644

//Synthetic comment -- @@ -617,7 +617,7 @@
public static void showDiscardDraftConfirmDialog(Context context,
OnClickListener listener) {
new AlertDialog.Builder(context)
                .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(R.string.discard_message)
.setMessage(R.string.discard_message_reason)
.setPositiveButton(R.string.yes, listener)








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessagingPreferenceActivity.java b/src/com/android/mms/ui/MessagingPreferenceActivity.java
//Synthetic comment -- index 28c8ae9..bbe5d2f 100755

//Synthetic comment -- @@ -289,7 +289,7 @@
}
})
.setNegativeButton(android.R.string.cancel, null)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.create();
}
return super.onCreateDialog(id);







