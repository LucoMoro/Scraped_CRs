/*Remove message notification while in conversation ui

While composing message, on data set changed, such as new
incoming message, remove from notification bar any
notification regarding the current conversation.

While display list of conversations, on query complete, such
as new incoming message, remove from notification bar any
notification regarding the whole list of conversations.

Change-Id:I806d4a921ed13af3db07f1e55b0fc5ba021ea0adSigned-off-by: gilles le brun <gil.lebrun@orange.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 085822f..f8e2252 100644

//Synthetic comment -- @@ -3627,6 +3627,7 @@
mDataSetChangedListener = new MessageListAdapter.OnDataSetChangedListener() {
public void onDataSetChanged(MessageListAdapter adapter) {
mPossiblePendingNotification = true;
}

public void onContentChanged(MessageListAdapter adapter) {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index a557940..abd0517 100644

//Synthetic comment -- @@ -103,7 +103,7 @@
private CharSequence mTitle;
private SharedPreferences mPrefs;
private Handler mHandler;
    private boolean mNeedToMarkAsSeen;
private TextView mUnreadConvCount;

private MenuItem mSearchItem;
//Synthetic comment -- @@ -249,7 +249,7 @@

DraftCache.getInstance().addOnDraftChangedListener(this);

        mNeedToMarkAsSeen = true;

startAsyncQuery();

//Synthetic comment -- @@ -668,13 +668,12 @@
setTitle(mTitle);
setProgressBarIndeterminateVisibility(false);

                if (mNeedToMarkAsSeen) {
                    mNeedToMarkAsSeen = false;
                    Conversation.markAllConversationsAsSeen(getApplicationContext());

// Delete any obsolete threads. Obsolete threads are threads that aren't
// referenced by at least one message in the pdu or sms tables. We only call
                    // this on the first query (because of mNeedToMarkAsSeen).
mHandler.post(mDeleteObsoleteThreadsRunnable);
}
break;







