/*We sometime need a new thread id to update drafts

We are saving the draft in a conversation which had been
deleted all messages one by one(Now the thread id is orphaned).
The draft will be saved in a obseleted thread, so we lose them
and cann't find them in ConversationList. We need to get a new
thread id in this case.

Refer to ef3eb49cde359d36f9536dbaffe5c16f3639c1f2, it fixed this
issue while we are saving the Mms draft, but Sms.

Change-Id:I29b3c1abf373877924b1afb45027417a12a1ec68Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index b63f366..c74335e 100755

//Synthetic comment -- @@ -885,7 +885,7 @@
// and takes that thread id (because it's the next thread id to be assigned), the
// new message will be merged with the draft message thread, causing confusion!
if (!TextUtils.isEmpty(content)) {
                asyncUpdateDraftSmsMessage(mConversation, content);
mHasSmsDraft = true;
} else {
// When there's no associated text message, we have to handle the case where there
//Synthetic comment -- @@ -1557,20 +1557,7 @@
} else {
updateDraftMmsMessage(mMessageUri, persister, mSlideshow, sendReq);
}
                    if (isStopping && conv.getMessageCount() == 0) {
                        // createDraftMmsMessage can create the new thread in the threads table (the
                        // call to createDraftMmsDraftMessage calls PduPersister.persist() which
                        // can call Threads.getOrCreateThreadId()). Meanwhile, when the user goes
                        // back to ConversationList while we're saving a draft from CMA's.onStop,
                        // ConversationList will delete all threads from the thread table that
                        // don't have associated sms or pdu entries. In case our thread got deleted,
                        // well call clearThreadId() so ensureThreadId will query the db for the new
                        // thread.
                        conv.clearThreadId();   // force us to get the updated thread id
                    }
                    if (!conv.getRecipients().isEmpty()) {
                        conv.ensureThreadId();
                    }
conv.setDraftState(true);
if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
LogTag.debug("asyncUpdateDraftMmsMessage conv: " + conv +
//Synthetic comment -- @@ -1673,7 +1660,7 @@
conv.setDraftState(false);
}

    private void asyncUpdateDraftSmsMessage(final Conversation conv, final String contents) {
new Thread(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1685,7 +1672,7 @@
}
return;
}
                    long threadId = conv.ensureThreadId();
conv.setDraftState(true);
updateDraftSmsMessage(conv, contents);
} finally {
//Synthetic comment -- @@ -1752,4 +1739,25 @@
final String where = Mms.THREAD_ID +  (threadId > 0 ? " = " + threadId : " IS NULL");
asyncDelete(Mms.Draft.CONTENT_URI, where, null);
}
}







