/*Mms: Fix ic_dialog_alert usage

The code wasn't updated for ICS everywhere so it points to
old upscaled Gingerbread drawables in a few places.

Change-Id:I8224c6cce285d42fa0ba3317367a0b025050a83c*/
//Synthetic comment -- diff --git a/src/com/android/mms/LogTag.java b/src/com/android/mms/LogTag.java
//Synthetic comment -- index bb1959a..c46da57 100644

//Synthetic comment -- @@ -114,7 +114,7 @@
activity.runOnUiThread(new Runnable() {
public void run() {
new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
.setTitle(R.string.error_state)
.setMessage(msg + "\n\n" + activity.getString(R.string.error_state_text))
.setPositiveButton(R.string.yes, new OnClickListener() {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index 68fd01d..beadb54 100644

//Synthetic comment -- @@ -317,7 +317,7 @@
private void confirmDeleteDialog(OnClickListener listener, int messageId) {
AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle(R.string.confirm_dialog_title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
builder.setCancelable(true);
builder.setPositiveButton(R.string.yes, listener);
builder.setNegativeButton(R.string.no, null);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessagingPreferenceActivity.java b/src/com/android/mms/ui/MessagingPreferenceActivity.java
//Synthetic comment -- index 383ff82..83297d1 100755

//Synthetic comment -- @@ -300,7 +300,7 @@
}
})
.setNegativeButton(android.R.string.cancel, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.create();
}
return super.onCreateDialog(id);







