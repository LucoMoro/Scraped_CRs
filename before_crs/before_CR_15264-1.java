/*Defect 1402: 'Subject' text field disappears after the attachment.

Change-Id:I0fa744cf1ee6774a6e2b20cb1f2acccd97c21284*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2dbdfab..6302552 100644

//Synthetic comment -- @@ -257,6 +257,8 @@
private ContactHeaderWidget mContactHeader;
private AttachmentTypeSelectorAdapter mAttachmentTypeSelectorAdapter;

@SuppressWarnings("unused")
private static void log(String logMsg) {
Thread current = Thread.currentThread();
//Synthetic comment -- @@ -1649,7 +1651,15 @@
return;
}
mSubjectTextEditor = (EditText)findViewById(R.id.subject);
        }

mSubjectTextEditor.setOnKeyListener(show ? mSubjectKeyListener : null);

//Synthetic comment -- @@ -2176,6 +2186,8 @@
mSubjectTextEditor.requestFocus();
break;
case MENU_ADD_ATTACHMENT:
// Launch the add-attachment list dialog
showAddAttachmentDialog();
break;







