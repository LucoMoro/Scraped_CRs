/*Recreate the options menu after drew the top panel

The onPrepareOptionsMenu will be called back before the
draft loaded, since the loading proccess has moved to
the non-UI-thread. We always need to notify CMA to
recreate the options menu after drew the top panel.

Change-Id:I91a56b9012a2934d474ceee3e499c559aa8e934bSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 0bbceec..a9f9cbd 100644

//Synthetic comment -- @@ -3205,6 +3205,8 @@
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());

        invalidateOptionsMenu();
}

//==========================================================







