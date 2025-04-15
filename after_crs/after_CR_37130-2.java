/*Make the lifecycle of cursor under the Activity

The cursors of ComposeCardActivity and ConversationList
were never be closed. So the database will throw an
CursorWindowAllocationException if the cursors which not
be closed are too many to be allocated then the database
will return a NULL cursor back to onQueryComplete().
After that Mms will be crashed by the NullPointerException.

Change-Id:Ib8730b326edf9177bbcce48a42b85405acff51baSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 4fa1878..6aa4e8d 100644

//Synthetic comment -- @@ -286,6 +286,8 @@

private String mDebugRecipients;

    private Cursor mCursor = null;

@SuppressWarnings("unused")
public static void log(String logMsg) {
Thread current = Thread.currentThread();
//Synthetic comment -- @@ -2093,6 +2095,10 @@
mMsgListAdapter.changeCursor(null);
}

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

if (mRecipientsEditor != null) {
CursorAdapter recipientsAdapter = (CursorAdapter)mRecipientsEditor.getAdapter();
if (recipientsAdapter != null) {
//Synthetic comment -- @@ -3655,6 +3661,8 @@

@Override
protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            mCursor = cursor;

switch(token) {
case MESSAGE_LIST_QUERY_TOKEN:
// check consistency between the query result and 'mConversation'
//Synthetic comment -- @@ -3677,17 +3685,17 @@
int newSelectionPos = -1;
long targetMsgId = getIntent().getLongExtra("select_id", -1);
if (targetMsgId != -1) {
                        mCursor.moveToPosition(-1);
                        while (mCursor.moveToNext()) {
                            long msgId = mCursor.getLong(COLUMN_ID);
if (msgId == targetMsgId) {
                                newSelectionPos = mCursor.getPosition();
break;
}
}
}

                    mMsgListAdapter.changeCursor(mCursor);
if (newSelectionPos != -1) {
mMsgListView.setSelection(newSelectionPos);
}
//Synthetic comment -- @@ -3703,7 +3711,7 @@
// mSentMessage is true).
// Show the recipients editor to give the user a chance to add
// more people before the conversation begins.
                    if (mCursor.getCount() == 0 && !isRecipientsEditorVisible() && !mSentMessage) {
initRecipientsEditor();
}

//Synthetic comment -- @@ -3722,7 +3730,7 @@
new ConversationList.DeleteThreadListener(threadIds,
mBackgroundQueryHandler, ComposeMessageActivity.this),
threadIds,
                            mCursor != null && mCursor.getCount() > 0,
ComposeMessageActivity.this);
break;
}








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index abd0517..8de38db 100644

//Synthetic comment -- @@ -109,6 +109,8 @@
private MenuItem mSearchItem;
private SearchView mSearchView;

    private Cursor mCursor = null;

static private final String CHECKED_MESSAGE_LIMITS = "checked_message_limits";

@Override
//Synthetic comment -- @@ -282,6 +284,10 @@
getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

mListAdapter.changeCursor(null);

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
}

public void onDraftChanged(final long threadId, final boolean hasDraft) {
//Synthetic comment -- @@ -662,9 +668,11 @@

@Override
protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            mCursor = cursor;

switch (token) {
case THREAD_LIST_QUERY_TOKEN:
                mListAdapter.changeCursor(mCursor);
setTitle(mTitle);
setProgressBarIndeterminateVisibility(false);

//Synthetic comment -- @@ -679,7 +687,7 @@
break;

case UNREAD_THREADS_QUERY_TOKEN:
                int count = mCursor.getCount();
mUnreadConvCount.setText(count > 0 ? Integer.toString(count) : null);
break;

//Synthetic comment -- @@ -687,7 +695,7 @@
Collection<Long> threadIds = (Collection<Long>)cookie;
confirmDeleteThreadDialog(new DeleteThreadListener(threadIds, mQueryHandler,
ConversationList.this), threadIds,
                        mCursor != null && mCursor.getCount() > 0,
ConversationList.this);
break;








