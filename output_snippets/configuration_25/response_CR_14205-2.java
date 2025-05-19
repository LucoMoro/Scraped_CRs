//<Beginning of snippet n. 0>
private synchronized TimelineEntry reloadCurrentEntry() {
    if (mAllEntries != null && mCurrentElement >= 0 && mCurrentElement < mAllEntries.size()) {
        return mAllEntries.get(mCurrentElement);
    }
    return null; 
}

private synchronized void actionPause() {
    if (isReloadAction()) {
        actionReload();
        TimelineEntry entry = reloadCurrentEntry();
        if (isPausedState()) {
            mAction = SmilPlayerAction.PAUSE;
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

Long threadId = values.getAsLong(Sms.THREAD_ID);
String address = values.getAsString(Sms.ADDRESS);
if (address != null && !address.isEmpty()) {
    Contact cacheContact = Contact.get(address, true);
    if (cacheContact != null) {
        address = cacheContact.getNumber();
    }
}

if ((threadId == null || threadId == 0) && address != null && !address.isEmpty()) {
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
new OnCreateContextMenuListener() {
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Cursor cursor = mMsgListAdapter.getCursor();
        try {
            if (cursor != null) {
                String type = cursor.getString(COLUMN_MSG_TYPE);
                long msgId = cursor.getLong(COLUMN_ID);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
};
private final class MsgListMenuClickListener implements MenuItem.OnMenuItemClickListener {
    public boolean onMenuItemClick(MenuItem item) {
        Cursor cursor = mMsgListAdapter.getCursor();
        try {
            if (cursor != null) {
                String type = cursor.getString(COLUMN_MSG_TYPE);
                long msgId = cursor.getLong(COLUMN_ID);
                MessageItem msgItem = getMessageItem(type, msgId);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
updateState(SHOW_LIST);
// Removed redundant registerSimChangeObserver() as it's handled in onResume()
} else {
    updateState(SHOW_EMPTY);
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
if (numberToDelete <= 0) {
    return;
}
try {
    if (msgs != null && msgs.getCount() >= limit) {
        return true;
    }
} finally {
    if (cursor != null) {
        cursor.close();  
    }
}
//<End of snippet n. 4>