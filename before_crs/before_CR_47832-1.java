/*Fix jank while Mms were downloading manually

The download button will be janked while several Mms
were downloading manually in a batch since message
list view will bind view before download state of Mms
were set in retrieve transaction.

Change-Id:Ibf9fbeee17150e2d7c3eba00ba34b56406ef96b5Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetrieveTransaction.java b/src/com/android/mms/transaction/RetrieveTransaction.java
//Synthetic comment -- index 236b2e9..6e37db9 100644

//Synthetic comment -- @@ -26,9 +26,11 @@
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.MmsConfig;
import com.android.mms.ui.MessageUtils;
import com.android.mms.ui.MessagingPreferenceActivity;
//Synthetic comment -- @@ -128,9 +130,17 @@

public void run() {
try {
            // Change the downloading state of the M-Notification.ind.
            DownloadManager.getInstance().markState(
                    mUri, DownloadManager.STATE_DOWNLOADING);

// Send GET request to MMSC and retrieve the response data.
byte[] resp = getPdu(mContentLocation);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index 880b7b6..029ee25 100644

//Synthetic comment -- @@ -246,6 +246,18 @@
intent.putExtra(TransactionBundle.TRANSACTION_TYPE,
Transaction.RETRIEVE_TRANSACTION);
mContext.startService(intent);
}
});
break;







