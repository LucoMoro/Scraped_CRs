/*Recreate the option menu after loaded draft

The onPrepareOptionMenu will be called back before the
draft loaded, since the loading proccess has moved to
the non-UI-thread. We only need to notify CMA to recreate
the option menu once, then Acitivy can handle itself.

Change-Id:I91a56b9012a2934d474ceee3e499c559aa8e934bSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 0bbceec..fb9fbd7 100644

//Synthetic comment -- @@ -304,7 +304,7 @@
// so we can remember it after re-entering the activity.
// If the value >= 0, then we jump to that line. If the
// value is maxint, then we jump to the end.

/**
* Whether this activity is currently running (i.e. not paused)
*/
//Synthetic comment -- @@ -2067,6 +2067,8 @@
protected void onStart() {
super.onStart();

initFocus();

// Register a BroadcastReceiver to listen on HTTP I/O process.
//Synthetic comment -- @@ -3205,6 +3207,12 @@
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
}

//==========================================================







