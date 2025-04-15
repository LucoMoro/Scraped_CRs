/*Mms: Fix wrong message selection in context menu

Sometimes when the user open a message's context menu for the
first time, the action will be performed on the incorrect message
(i.e. deleting the wrong message). This is because the menu click
listener relies on the fact that that the cursor points to the
current message, which is not always true because bindView can
be called on other messages causing the cursor to move.

Fix: Cache the position of the message and move the cursor back.

Change-Id:I4e8fdd2956987fc3f12c80def46c107a4089d0ae*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 085822f..2469d42 100644

//Synthetic comment -- @@ -892,7 +892,10 @@

menu.setHeaderTitle(R.string.message_options);

            MsgListMenuClickListener l = new MsgListMenuClickListener();

// It is unclear what would make most sense for copying an MMS message
// to the clipboard, so we currently do SMS only.
//Synthetic comment -- @@ -1091,11 +1094,19 @@
* Context menu handlers for the message list view.
*/
private final class MsgListMenuClickListener implements MenuItem.OnMenuItemClickListener {
public boolean onMenuItemClick(MenuItem item) {
if (!isCursorValid()) {
return false;
}
Cursor cursor = mMsgListAdapter.getCursor();
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);
MessageItem msgItem = getMessageItem(type, msgId, true);







