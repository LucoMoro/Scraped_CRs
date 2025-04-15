/*Recreate the options menu after drew the top panel

The onPrepareOptionsMenu will be called back before the
draft loaded, since the loading proccess has moved to
the non-UI-thread. We always need to notify CMA to
recreate the options menu after drew the top panel.

Change-Id:I91a56b9012a2934d474ceee3e499c559aa8e934bSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 0bbceec..a289692 100644

//Synthetic comment -- @@ -1139,7 +1139,6 @@

mWorkingMessage = newWorkingMessage;
mWorkingMessage.setConversation(mConversation);
        invalidateOptionsMenu();

drawTopPanel(false);

//Synthetic comment -- @@ -1917,10 +1916,6 @@
} else {
hideRecipientEditor();
}
        invalidateOptionsMenu();    // do after show/hide of recipients editor because the options
                                    // menu depends on the recipients, which depending upon the
                                    // visibility of the recipients editor, returns a different
                                    // value (see getRecipients()).

updateSendButtonState();

//Synthetic comment -- @@ -2058,7 +2053,6 @@
loadDraft();
mWorkingMessage.setConversation(mConversation);
mAttachmentEditor.update(mWorkingMessage);
                invalidateOptionsMenu();
}
}
}
//Synthetic comment -- @@ -2808,7 +2802,6 @@
updateThreadIdIfRunning();
drawTopPanel(false);
updateSendButtonState();
                        invalidateOptionsMenu();
}
}
break;
//Synthetic comment -- @@ -3205,6 +3198,8 @@
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
}

//==========================================================







