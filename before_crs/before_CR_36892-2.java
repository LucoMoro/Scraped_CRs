/*Fix bug on copying/forwarding/viewing details of SMS message

When viewing an SMS conversation, sending a new message and
immediately long-pressing on that new message and immediately
select Copy, Forward, or View Details, on the context menu,
the app will act as if the message selected were another message.

Also reported by several people:http://code.google.com/p/android/issues/detail?id=28009http://code.google.com/p/android/issues/detail?id=26827http://code.google.com/p/android/issues/detail?id=25569This is caused by the app thinking that the
message selected is the same as the message pointed by
the position of the conversation's Cursor.
Actually, the position of the Cursor has been modified by
the ListView adapter calling getItem that sets the Cursor
position to the position of the last getItem, occuring
when the list refreshes itself due to the incoming new message
(which is the message that has just been sent).

This commit fixes the bug by:
- (for other than View Details): Passing the message item
itself to the handler of the context menu, so we're sure
that we're not copying/forwarding/deleting another message
- (for View Details): Creating another cursor whose
data is the same as conversation's cursor, and setting the
position to be the same as the selected message's position.

Change-Id:I2d9c412e6fa3683bc4f0c1710aba016daa68ccdcSigned-off-by: Yuku on yuku4 <yukuku@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 4fa1878..2d21e2b 100644

//Synthetic comment -- @@ -881,6 +881,8 @@
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);

addPositionBasedMenuItems(menu, v, menuInfo);

MessageItem msgItem = mMsgListAdapter.getCachedMessageItem(type, msgId, cursor);
//Synthetic comment -- @@ -892,7 +894,7 @@

menu.setHeaderTitle(R.string.message_options);

            MsgListMenuClickListener l = new MsgListMenuClickListener();

// It is unclear what would make most sense for copying an MMS message
// to the clipboard, so we currently do SMS only.
//Synthetic comment -- @@ -1091,14 +1093,18 @@
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

if (msgItem == null) {
return false;
//Synthetic comment -- @@ -1120,12 +1126,17 @@

case MENU_VIEW_SLIDESHOW:
MessageUtils.viewMmsMessageAttachment(ComposeMessageActivity.this,
                            ContentUris.withAppendedId(Mms.CONTENT_URI, msgId), null);
return true;

case MENU_VIEW_MESSAGE_DETAILS: {
String messageDetails = MessageUtils.getMessageDetails(
ComposeMessageActivity.this, cursor, msgItem.mMessageSize);
new AlertDialog.Builder(ComposeMessageActivity.this)
.setTitle(R.string.message_details_title)
.setMessage(messageDetails)
//Synthetic comment -- @@ -1140,18 +1151,18 @@
return true;
}
case MENU_DELIVERY_REPORT:
                    showDeliveryReport(msgId, type);
return true;

case MENU_COPY_TO_SDCARD: {
                    int resId = copyMedia(msgId) ? R.string.copy_to_sdcard_success :
R.string.copy_to_sdcard_fail;
Toast.makeText(ComposeMessageActivity.this, resId, Toast.LENGTH_SHORT).show();
return true;
}

case MENU_COPY_TO_DRM_PROVIDER: {
                    int resId = getDrmMimeSavedStringRsrc(msgId, copyToDrmProvider(msgId));
Toast.makeText(ComposeMessageActivity.this, resId, Toast.LENGTH_SHORT).show();
return true;
}







