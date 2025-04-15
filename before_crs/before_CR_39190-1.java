/*Delete obsolete threads when drafts updated

The delete obsolete threads action in CL will be executed
before the drafts saved in CMA, since saveDraft was called
in CMA' onStop(), but the delete action was in the CL's onStart().

It's useless to getSavingDraft as the condition to check if we should
delete the obsolete threads in mDeleteObsoleteThreadsRunnable,
since it always return false at that time. We should sync to
delete the obsolete threads in the CallBack instead.

Change-Id:Ia58dca6c8e0acb728bfa122a96bf6a4351f44a51Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 345bec3..d0cf0f6 100644

//Synthetic comment -- @@ -303,6 +303,11 @@
if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
log("onDraftChanged: threadId=" + threadId + ", hasDraft=" + hasDraft);
}
mListAdapter.notifyDataSetChanged();
}
});
//Synthetic comment -- @@ -694,18 +699,8 @@
private final Runnable mDeleteObsoleteThreadsRunnable = new Runnable() {
@Override
public void run() {
            if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
                LogTag.debug("mDeleteObsoleteThreadsRunnable getSavingDraft(): " +
                        DraftCache.getInstance().getSavingDraft());
            }
            if (DraftCache.getInstance().getSavingDraft()) {
                // We're still saving a draft. Try again in a second. We don't want to delete
                // any threads out from under the draft.
                mHandler.postDelayed(mDeleteObsoleteThreadsRunnable, 1000);
            } else {
                Conversation.asyncDeleteObsoleteThreads(mQueryHandler,
                        DELETE_OBSOLETE_THREADS_TOKEN);
            }
}
};








