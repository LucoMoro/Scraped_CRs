/*Extended search for duplicate MMS messages during download

MMS messages sent from some test servers all have the same message ID,
which results in the message being deleted from the Inbox after a
first message has been downloaded. This is fixed by requiring that the
IDs and subjects of two messages are identical to regard them as
duplicates.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetrieveTransaction.java b/src/com/android/mms/transaction/RetrieveTransaction.java
//Synthetic comment -- index 78d746e..d8004ee 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.mms.util.Recycler;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.AcknowledgeInd;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
//Synthetic comment -- @@ -37,6 +38,7 @@
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.util.Config;
import android.util.Log;

//Synthetic comment -- @@ -194,15 +196,18 @@
+ Mms.MESSAGE_TYPE + " = ?)";
String[] selectionArgs = new String[] { messageId,
String.valueOf(PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF) };
Cursor cursor = SqliteWrapper.query(
context, context.getContentResolver(),
                    Mms.CONTENT_URI, new String[] { Mms._ID },
selection, selectionArgs, null);
if (cursor != null) {
try {
if (cursor.getCount() > 0) {
                        // We already received the same message before.
                        return true;
}
} finally {
cursor.close();
//Synthetic comment -- @@ -212,6 +217,46 @@
return false;
}

private void sendAcknowledgeInd(RetrieveConf rc) throws MmsException, IOException {
// Send M-Acknowledge.ind to MMSC if required.
// If the Transaction-ID isn't set in the M-Retrieve.conf, it means







