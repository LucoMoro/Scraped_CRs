/*Fix jank while Mms were downloading manually

The download button will be janked while several Mms
were downloading manually in a batch since message
list view will bind view before download state of Mms
were set in retrieve transaction.

Change-Id:Ibf9fbeee17150e2d7c3eba00ba34b56406ef96b5Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index 880b7b6..ffe06fb 100644

//Synthetic comment -- @@ -212,6 +212,7 @@
mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));

switch (mMessageItem.getMmsDownloadStatus()) {
case DownloadManager.STATE_DOWNLOADING:
showDownloadingAttachment();
break;
//Synthetic comment -- @@ -246,6 +247,9 @@
intent.putExtra(TransactionBundle.TRANSACTION_TYPE,
Transaction.RETRIEVE_TRANSACTION);
mContext.startService(intent);
}
});
break;








//Synthetic comment -- diff --git a/src/com/android/mms/util/DownloadManager.java b/src/com/android/mms/util/DownloadManager.java
//Synthetic comment -- index 5210597..3e061e3 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;







