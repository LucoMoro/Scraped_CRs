/*Set local time as timestamp when MMS has been downloaded

Previously, the timestamp of notifications was the current local time,
but as soon as the MMS was downloaded it got the timestamp from the
PDU. This could cause problems if timezones weren't set correctly etc.
Now a downloaded MMS will always get the current local time as its
timestamp, regardless of when it was first received.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/NotificationTransaction.java b/src/com/android/mms/transaction/NotificationTransaction.java
//Synthetic comment -- index bcac9e7..a4041af 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.google.android.mms.pdu.PduPersister;
import android.database.sqlite.SqliteWrapper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
//Synthetic comment -- @@ -172,6 +173,13 @@
// Save the received PDU (must be a M-RETRIEVE.CONF).
PduPersister p = PduPersister.getPduPersister(mContext);
Uri uri = p.persist(pdu, Inbox.CONTENT_URI);
// We have successfully downloaded the new MM. Delete the
// M-NotifyResp.ind from Inbox.
SqliteWrapper.delete(mContext, mContext.getContentResolver(),








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetrieveTransaction.java b/src/com/android/mms/transaction/RetrieveTransaction.java
//Synthetic comment -- index 78d746e..935a5f0 100644

//Synthetic comment -- @@ -150,6 +150,12 @@
PduPersister persister = PduPersister.getPduPersister(mContext);
msgUri = persister.persist(retrieveConf, Inbox.CONTENT_URI);

// The M-Retrieve.conf has been successfully downloaded.
mTransactionState.setState(TransactionState.SUCCESS);
mTransactionState.setContentUri(msgUri);







