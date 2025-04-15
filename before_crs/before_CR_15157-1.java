/*issue 4102: Subject Field disappears while adding attachment to MMS

Change-Id:Id0a35cfe5c2b735bc29254492a41e508a563840c*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2dbdfab..20cc435 100644

//Synthetic comment -- @@ -256,6 +256,8 @@
private int mLastRecipientCount;            // Used for warning the user on too many recipients.
private ContactHeaderWidget mContactHeader;
private AttachmentTypeSelectorAdapter mAttachmentTypeSelectorAdapter;

@SuppressWarnings("unused")
private static void log(String logMsg) {
//Synthetic comment -- @@ -1641,16 +1643,24 @@
if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
log("showSubjectEditor: " + show);
}

if (mSubjectTextEditor == null) {
// Don't bother to initialize the subject editor if
// we're just going to hide it.
if (show == false) {
return;
}
mSubjectTextEditor = (EditText)findViewById(R.id.subject);
}

mSubjectTextEditor.setOnKeyListener(show ? mSubjectKeyListener : null);

if (show) {
//Synthetic comment -- @@ -2176,6 +2186,8 @@
mSubjectTextEditor.requestFocus();
break;
case MENU_ADD_ATTACHMENT:
// Launch the add-attachment list dialog
showAddAttachmentDialog();
break;
//Synthetic comment -- @@ -2311,6 +2323,7 @@

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if (DEBUG) {
log("onActivityResult: requestCode=" + requestCode
+ ", resultCode=" + resultCode + ", data=" + data);







