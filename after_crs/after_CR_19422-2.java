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
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
//Synthetic comment -- @@ -37,6 +38,7 @@
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

//Synthetic comment -- @@ -194,15 +196,18 @@
+ Mms.MESSAGE_TYPE + " = ?)";
String[] selectionArgs = new String[] { messageId,
String.valueOf(PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF) };

Cursor cursor = SqliteWrapper.query(
context, context.getContentResolver(),
                    Mms.CONTENT_URI, new String[] { Mms._ID, Mms.SUBJECT, Mms.SUBJECT_CHARSET },
selection, selectionArgs, null);

if (cursor != null) {
try {
if (cursor.getCount() > 0) {
                        // A message with identical message ID and type found.
                        // Do some additional checks to be sure it's a duplicate.
                        return isDuplicateMessageExtra(cursor, rc);
}
} finally {
cursor.close();
//Synthetic comment -- @@ -212,6 +217,46 @@
return false;
}

    private static boolean isDuplicateMessageExtra(Cursor cursor, RetrieveConf rc) {
        // Compare message subjects, taking encoding into account
        EncodedStringValue encodedSubjectReceived = null;
        EncodedStringValue encodedSubjectStored = null;
        String subjectReceived = null;
        String subjectStored = null;
        String subject = null;

        encodedSubjectReceived = rc.getSubject();
        if (encodedSubjectReceived != null) {
            subjectReceived = encodedSubjectReceived.getString();
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int subjectIdx = cursor.getColumnIndex(Mms.SUBJECT);
            int charsetIdx = cursor.getColumnIndex(Mms.SUBJECT_CHARSET);
            subject = cursor.getString(subjectIdx);
            int charset = cursor.getInt(charsetIdx);
            if (subject != null) {
                encodedSubjectStored = new EncodedStringValue(charset, PduPersister
                        .getBytes(subject));
            }
            if (encodedSubjectStored == null && encodedSubjectReceived == null) {
                // Both encoded subjects are null - return true
                return true;
            } else if (encodedSubjectStored != null && encodedSubjectReceived != null) {
                subjectStored = encodedSubjectStored.getString();
                if (!TextUtils.isEmpty(subjectStored) && !TextUtils.isEmpty(subjectReceived)) {
                    // Both decoded subjects are non-empty - compare them
                    return subjectStored.equals(subjectReceived);
                } else if (TextUtils.isEmpty(subjectStored) && TextUtils.isEmpty(subjectReceived)) {
                    // Both decoded subjects are "" - return true
                    return true;
                }
            }
        }

        return false;
    }

private void sendAcknowledgeInd(RetrieveConf rc) throws MmsException, IOException {
// Send M-Acknowledge.ind to MMSC if required.
// If the Transaction-ID isn't set in the M-Retrieve.conf, it means







