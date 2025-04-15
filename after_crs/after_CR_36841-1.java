/*Set the recipient for the reused conversation

The recipients of WorkingMessage will be set to NULL if
user deleted all messages in the conversation one by one.
So the new draft in the NULL recipient conversation won't
be saved after user composed and backed to the previous
activity.

Change-Id:Icd179e82addc750c95ad9eca4683f17b66b12202Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 085822f..9c1a880 100644

//Synthetic comment -- @@ -3386,6 +3386,11 @@
return;
}

        // Reset the recipients of WorkingMessage if user deleted all messages one by one
        if (mWorkingMessage.getWorkingRecipients() == null && mConversation.getMessageCount() == 0) {
            mWorkingMessage.setWorkingRecipients(mRecipientsEditor.getNumbers());
        }

mWorkingMessage.saveDraft(isStopping);

if (mToastForDraftSave) {







