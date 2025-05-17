//<Beginning of snippet n. 0>

int downloadStatus = mMessageItem.getMmsDownloadStatus();
switch (downloadStatus) {
    case DownloadManager.STATE_DOWNLOADING:
        showDownloadingAttachment();
        updateLoadingIndicator(true);
        break;
    case DownloadManager.STATE_TRANSIENT_FAILURE:
    case DownloadManager.STATE_PERMANENT_FAILURE:
        notifyUserOfDownloadFailure(downloadStatus);
        updateLoadingIndicator(false);
        break;
    default:
        updateLoadingIndicator(false);
        break;
}

intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
mContext.startService(intent);

// Delay UI update until download status is confirmed
mHandler.post(() -> {
    mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));
});

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;

// User feedback method
private void notifyUserOfDownloadFailure(int status) {
    String message;
    if (status == STATE_TRANSIENT_FAILURE) {
        message = "Download failed temporarily. Please try again.";
    } else {
        message = "Download failed permanently. Please check your connection.";
    }
    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
}

private void updateLoadingIndicator(boolean isLoading) {
    // Implementation for showing or hiding a loading indicator
    if (isLoading) {
        // Show loading indicator
    } else {
        // Hide loading indicator
    }
}

//<End of snippet n. 1>