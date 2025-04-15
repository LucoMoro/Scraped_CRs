/*issue 4102:Subject Field disappears while adding attachment to MMS

Change-Id:I9d0b2b2633a7ba68e0ed594eb3bda14529d9d958*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2dbdfab..a159fa4 100644

//Synthetic comment -- @@ -257,6 +257,8 @@
private ContactHeaderWidget mContactHeader;
private AttachmentTypeSelectorAdapter mAttachmentTypeSelectorAdapter;

@SuppressWarnings("unused")
private static void log(String logMsg) {
Thread current = Thread.currentThread();
//Synthetic comment -- @@ -1649,6 +1651,13 @@
return;
}
mSubjectTextEditor = (EditText)findViewById(R.id.subject);
}

mSubjectTextEditor.setOnKeyListener(show ? mSubjectKeyListener : null);
//Synthetic comment -- @@ -2176,6 +2185,8 @@
mSubjectTextEditor.requestFocus();
break;
case MENU_ADD_ATTACHMENT:
// Launch the add-attachment list dialog
showAddAttachmentDialog();
break;







