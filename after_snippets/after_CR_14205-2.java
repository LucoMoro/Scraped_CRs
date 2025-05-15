
//<Beginning of snippet n. 0>


}

private synchronized TimelineEntry reloadCurrentEntry() {
        //Check if the position is less than size of all entries
        if (mCurrentElement < mAllEntries.size()) {
            return mAllEntries.get(mCurrentElement);
        } else {
            return null;
        }
}

private synchronized void actionPause() {
if (isReloadAction()) {
actionReload();
entry = reloadCurrentEntry();
                        if (entry == null)
                            return;
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
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

// excess messages.
Long threadId = values.getAsLong(Sms.THREAD_ID);
String address = values.getAsString(Sms.ADDRESS);
        if (!TextUtils.isEmpty(address)) {
            Contact cacheContact = Contact.get(address,true);
            if (cacheContact != null) {
                address = cacheContact.getNumber();
            }
        } else {
            address = "Unknown";
            values.put(Sms.ADDRESS, address);
}

if (((threadId == null) || (threadId == 0)) && (address != null)) {

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


new OnCreateContextMenuListener() {
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
Cursor cursor = mMsgListAdapter.getCursor();
            //Validate the cursor before creating the menu
            if (cursor.isClosed() || cursor.isBeforeFirst() || cursor.isAfterLast()) {
                Log.e(TAG, "Bad cursor.", new RuntimeException());
                return;
            }
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);

private final class MsgListMenuClickListener implements MenuItem.OnMenuItemClickListener {
public boolean onMenuItemClick(MenuItem item) {
Cursor cursor = mMsgListAdapter.getCursor();
            //Validate the cursor before performing the click operation
            if (cursor.isClosed() || cursor.isBeforeFirst() || cursor.isAfterLast()) {
                Log.e(TAG, "Bad cursor.", new RuntimeException());
                return false;
            }
String type = cursor.getString(COLUMN_MSG_TYPE);
long msgId = cursor.getLong(COLUMN_ID);
MessageItem msgItem = getMessageItem(type, msgId);

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


updateState(SHOW_LIST);
}
startManagingCursor(mCursor);
} else {
// Let user know the SIM is empty
updateState(SHOW_EMPTY);

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


" numberToDelete: " + numberToDelete);
}
if (numberToDelete <= 0) {
                cursor.close();
return;
}
try {
null, "date DESC");     // get in newest to oldest order

if (msgs.getCount() >= limit) {
                        msgs.close();
return true;
}
                    msgs.close();
}
} finally {
cursor.close();

//<End of snippet n. 4>








