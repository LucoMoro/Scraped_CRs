
//<Beginning of snippet n. 0>


}

private synchronized TimelineEntry reloadCurrentEntry() {
        return mAllEntries.get(mCurrentElement);
}

private synchronized void actionPause() {
if (isReloadAction()) {
actionReload();
entry = reloadCurrentEntry();
if (isPausedState()) {
mAction = SmilPlayerAction.PAUSE;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

old mode 100644
new mode 100755

import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

// excess messages.
Long threadId = values.getAsLong(Sms.THREAD_ID);
String address = values.getAsString(Sms.ADDRESS);
        Contact cacheContact = Contact.get(address,true);
        if (cacheContact != null) {
            address = cacheContact.getNumber();
}

if (((threadId == null) || (threadId == 0)) && (address != null)) {

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


new OnCreateContextMenuListener() {
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
Cursor cursor = mMsgListAdapter.getCursor();
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);

private final class MsgListMenuClickListener implements MenuItem.OnMenuItemClickListener {
public boolean onMenuItemClick(MenuItem item) {
Cursor cursor = mMsgListAdapter.getCursor();
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);
MessageItem msgItem = getMessageItem(type, msgId);

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


updateState(SHOW_LIST);
}
startManagingCursor(mCursor);
                registerSimChangeObserver();
} else {
// Let user know the SIM is empty
updateState(SHOW_EMPTY);

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


" numberToDelete: " + numberToDelete);
}
if (numberToDelete <= 0) {
return;
}
try {
null, "date DESC");     // get in newest to oldest order

if (msgs.getCount() >= limit) {
return true;
}
}
} finally {
cursor.close();

//<End of snippet n. 4>








