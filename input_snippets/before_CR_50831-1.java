
//<Beginning of snippet n. 0>


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.DraftCache;

/**
* An interface for finding information about conversations and/or creating new ones.
*/
}
}

/**
* Marks all messages in this conversation as read and updates
* relevant notifications.  This method returns immediately;
}

if (needUpdate) {
LogTag.debug("markAsRead: update read/seen for thread uri: " +
threadUri);
mContext.getContentResolver().update(threadUri, sReadContentValues,

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


* <li>Notifies the TransactionService about succesful completion.
* </ul>
*/
public class ReadRecTransaction extends Transaction {
private static final String TAG = "ReadRecTransaction";
private static final boolean DEBUG = false;
private static final boolean LOCAL_LOGV = false;

private final Uri mReadReportURI;

public ReadRecTransaction(Context context,
*/
@Override
public void process() {
PduPersister persister = PduPersister.getPduPersister(mContext);

try {

//<End of snippet n. 1>








