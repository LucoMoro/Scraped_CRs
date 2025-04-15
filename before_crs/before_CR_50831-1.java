/*Send mms read report if it is required.

Change-Id:I35be42545f000c51ebe71444ab591c42b33160ccSigned-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/data/Conversation.java b/src/com/android/mms/data/Conversation.java
//Synthetic comment -- index 91bea24..75a3a5b 100644

//Synthetic comment -- @@ -13,6 +13,7 @@
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
//Synthetic comment -- @@ -30,10 +31,14 @@
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.DraftCache;

/**
* An interface for finding information about conversations and/or creating new ones.
*/
//Synthetic comment -- @@ -297,6 +302,38 @@
}
}

/**
* Marks all messages in this conversation as read and updates
* relevant notifications.  This method returns immediately;
//Synthetic comment -- @@ -347,6 +384,7 @@
}

if (needUpdate) {
LogTag.debug("markAsRead: update read/seen for thread uri: " +
threadUri);
mContext.getContentResolver().update(threadUri, sReadContentValues,








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/ReadRecTransaction.java b/src/com/android/mms/transaction/ReadRecTransaction.java
//Synthetic comment -- index d424860..bcbfa62 100644

//Synthetic comment -- @@ -42,11 +42,12 @@
* <li>Notifies the TransactionService about succesful completion.
* </ul>
*/
public class ReadRecTransaction extends Transaction {
private static final String TAG = "ReadRecTransaction";
private static final boolean DEBUG = false;
private static final boolean LOCAL_LOGV = false;

private final Uri mReadReportURI;

public ReadRecTransaction(Context context,
//Synthetic comment -- @@ -67,6 +68,11 @@
*/
@Override
public void process() {
PduPersister persister = PduPersister.getPduPersister(mContext);

try {







