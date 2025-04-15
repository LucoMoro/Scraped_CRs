/*issue 4102:subject filed disappers while adding attachment to mms

Change-Id:Ieeaca2e8ff7be8d3d02a5b69772a070aca74e91a*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2dbdfab..3f3ff82 100644

//Synthetic comment -- @@ -257,6 +257,8 @@
private ContactHeaderWidget mContactHeader;
private AttachmentTypeSelectorAdapter mAttachmentTypeSelectorAdapter;

@SuppressWarnings("unused")
private static void log(String logMsg) {
Thread current = Thread.currentThread();
//Synthetic comment -- @@ -1649,6 +1651,14 @@
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







