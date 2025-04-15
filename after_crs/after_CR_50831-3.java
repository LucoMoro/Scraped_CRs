/*Send mms read report if it is required.

Change-Id:I35be42545f000c51ebe71444ab591c42b33160ccSigned-off-by: Bin Li <libin@marvell.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/data/Conversation.java b/src/com/android/mms/data/Conversation.java
//Synthetic comment -- index 91bea24..106cea2 100644

//Synthetic comment -- @@ -13,6 +13,7 @@
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
//Synthetic comment -- @@ -30,10 +31,14 @@
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;
import com.android.mms.transaction.MmsMessageSender;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.AddressUtils;
import com.android.mms.util.DraftCache;

import com.google.android.mms.pdu.PduHeaders;

/**
* An interface for finding information about conversations and/or creating new ones.
*/
//Synthetic comment -- @@ -297,6 +302,42 @@
}
}

    private void sendReadReport(final Context context,
            final long threadId,
            final int status) {
        String selection = Mms.MESSAGE_TYPE + " = " + PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF
            + " AND " + Mms.READ + " = 0"
            + " AND " + Mms.READ_REPORT + " = " + PduHeaders.VALUE_YES;

        if (threadId != -1) {
            selection = selection + " AND " + Mms.THREAD_ID + " = " + threadId;
        }

        final Cursor c = SqliteWrapper.query(context, context.getContentResolver(),
                        Mms.Inbox.CONTENT_URI, new String[] {Mms._ID, Mms.MESSAGE_ID},
                        selection, null, null);

        try {
            if (c == null || c.getCount() == 0) {
                return;
            }

            while (c.moveToNext()) {
                Uri uri = ContentUris.withAppendedId(Mms.CONTENT_URI, c.getLong(0));
                if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
                    LogTag.debug("sendReadReport: uri = " + uri);
                }
                MmsMessageSender.sendReadRec(context, AddressUtils.getFrom(context, uri),
                                             c.getString(1), status);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }


/**
* Marks all messages in this conversation as read and updates
* relevant notifications.  This method returns immediately;
//Synthetic comment -- @@ -347,6 +388,7 @@
}

if (needUpdate) {
                        sendReadReport(mContext, mThreadId, PduHeaders.READ_STATUS_READ);
LogTag.debug("markAsRead: update read/seen for thread uri: " +
threadUri);
mContext.getContentResolver().update(threadUri, sReadContentValues,








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/ReadRecTransaction.java b/src/com/android/mms/transaction/ReadRecTransaction.java
//Synthetic comment -- index d424860..bcbfa62 100644

//Synthetic comment -- @@ -42,11 +42,12 @@
* <li>Notifies the TransactionService about succesful completion.
* </ul>
*/
public class ReadRecTransaction extends Transaction implements Runnable{
private static final String TAG = "ReadRecTransaction";
private static final boolean DEBUG = false;
private static final boolean LOCAL_LOGV = false;

    private Thread mThread;
private final Uri mReadReportURI;

public ReadRecTransaction(Context context,
//Synthetic comment -- @@ -67,6 +68,11 @@
*/
@Override
public void process() {
        mThread = new Thread(this, "ReadRecTransaction");
        mThread.start();
    }

    public void run() {
PduPersister persister = PduPersister.getPduPersister(mContext);

try {







