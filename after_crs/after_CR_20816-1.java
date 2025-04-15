/*A email apps uses a context menu served by google.
But sometimes a parameter "MenuItem" in the "onContextItemSelected" was passed wrongly.
So switched to global variable and initiate it in the "onContextItemSelected" because
a parameter "MenuItem" in the "onCreateContextMenu" is always correct.
And global variable is used instead of a parameter while forwarding.

Change-Id:I7581123f21fbb4015153ca6f4a0c14c0f6a769fcSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageList.java b/src/com/android/email/activity/MessageList.java
//Synthetic comment -- index caedc3a..d855227 100644

//Synthetic comment -- @@ -119,6 +119,9 @@

private TextView mLeftTitle;
private ProgressBar mProgressIcon;
    private long mSelectedItemId;
    private boolean mSelectedItemState;
    private long mSelectedItemInfo;

// DB access
private ContentResolver mResolver;
//Synthetic comment -- @@ -486,6 +489,9 @@
return;
}
MessageListItem itemView = (MessageListItem) info.targetView;
        mSelectedItemId = itemView.mMessageId;
        mSelectedItemState = itemView.mRead;
        mSelectedItemInfo = info.id;

Cursor c = (Cursor) mListView.getItemAtPosition(info.position);
String messageName = c.getString(MessageListAdapter.COLUMN_SUBJECT);
//Synthetic comment -- @@ -527,22 +533,22 @@

switch (item.getItemId()) {
case R.id.open:
                onOpenMessage(info.id, mSelectedItemId);
break;
case R.id.delete:
                onDelete(info.id, mSelectedItemId);
break;
case R.id.reply:
                onReply(mSelectedItemId);
break;
case R.id.reply_all:
                onReplyAll(mSelectedItemId);
break;
case R.id.forward:
                onForward(mSelectedItemId);
break;
case R.id.mark_as_read:
                onSetMessageRead(mSelectedItemInfo, !mSelectedItemState);
break;
}
return super.onContextItemSelected(item);







