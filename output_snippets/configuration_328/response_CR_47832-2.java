//<Beginning of snippet n. 0>


mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));

switch (mMessageItem.getMmsDownloadStatus()) {
case DownloadManager.STATE_DOWNLOADING:
    showDownloadingAttachment();
    Intent intent = new Intent(mContext, DownloadService.class);
    intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
    mContext.startService(intent);
    break;
case DownloadManager.STATE_TRANSIENT_FAILURE:
    handleTransientFailure();
    break;
case DownloadManager.STATE_PERMANENT_FAILURE:
    handlePermanentFailure();
    break;
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;

//<End of snippet n. 1>