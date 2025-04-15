/*bluetooth/history: moved list clearing operation to an async task

If BluetoothOppTransferHistory view contains a lenghty list of items
(downloaded/sent/failed files), clearing this list can take some time.
The problem is that the clearing is done in foreground and blocks the
application from responding, usually leading to an ANR if there's a
user sollicitation at that moment.

The fix consists in moving the clearing process to an AsyncTask.
Clearing is then done in background while application can still
treat I/O events.

Change-Id:I9a6d9c8b62a8021307906c20c4d21443d9d2536bAuthor: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 10553 17106*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java b/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java
//Synthetic comment -- index 697e3cb..73b84fd 100644

//Synthetic comment -- @@ -37,10 +37,12 @@
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
//Synthetic comment -- @@ -68,6 +70,8 @@

private Cursor mTransferCursor;

    private Cursor mClearCursor;

private BluetoothOppTransferAdapter mTransferAdapter;

private int mIdColumnId;
//Synthetic comment -- @@ -122,6 +126,14 @@
BluetoothShare.VISIBILITY, BluetoothShare.DESTINATION, BluetoothShare.DIRECTION
}, selection, sortOrder);

        // Cursor used during the clearing operation which is done in an async task
        // It has to be a different object than mTransferCursor to prevent conflicts
        mClearCursor = managedQuery(BluetoothShare.CONTENT_URI, new String[] {
                "_id", BluetoothShare.FILENAME_HINT, BluetoothShare.STATUS,
                BluetoothShare.TOTAL_BYTES, BluetoothShare._DATA, BluetoothShare.TIMESTAMP,
                BluetoothShare.VISIBILITY, BluetoothShare.DESTINATION, BluetoothShare.DIRECTION
        }, selection, sortOrder);

// only attach everything to the listbox if we can access
// the transfer database. Otherwise, just show it empty
if (mTransferCursor != null) {
//Synthetic comment -- @@ -216,7 +228,7 @@
R.string.transfer_clear_dlg_msg).setPositiveButton(android.R.string.ok,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {
                        new ClearAllDownloadsTask().execute();
}
}).setNegativeButton(android.R.string.cancel, null).show();
}
//Synthetic comment -- @@ -242,16 +254,32 @@
/**
* Clear all finished transfers, error and success transfer items.
*/
    private class ClearAllDownloadsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (mClearCursor == null)
                cancel(true);

            if (mClearCursor.moveToFirst()) {

                Context context = getApplicationContext();
                try{
                    while (!mClearCursor.isAfterLast()) {
                        int sessionId = mClearCursor.getInt(mIdColumnId);
                        Uri contentUri = Uri.parse(BluetoothShare.CONTENT_URI + "/" + sessionId);
                        BluetoothOppUtility.updateVisibilityToHidden(context, contentUri);
                        mClearCursor.moveToNext();
                    }
                } catch (Exception e){
                    Log.e(TAG, "ClearAllDownloadsTask ended before finishing: " + e);
                }
                finally{
                    updateNotificationWhenBtDisabled();
                }
}
            return null;
}
}








