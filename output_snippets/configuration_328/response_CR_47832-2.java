//<Beginning of snippet n. 0>

mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));

if(!isAnyDownloadInProgress()) {
    switch (mMessageItem.getMmsDownloadStatus()) {
        case DownloadManager.STATE_DOWNLOADING:
            showDownloadingAttachment();
            break;
        case DownloadManager.STATE_TRANSIENT_FAILURE:
            handleDownloadError("Transient failure occurred. Please try again.");
            break;
        case DownloadManager.STATE_PERMANENT_FAILURE:
            handleDownloadError("Permanent failure. Please check your connection.");
            break;
    }
    intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
    mContext.startService(intent);
} else {
    showLoadingIndicator();
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;
private boolean isDownloadInProgress = false;

private boolean isAnyDownloadInProgress() {
    return isDownloadInProgress;
}

private void startDownload() {
    if (!isAnyDownloadInProgress()) {
        isDownloadInProgress = true;
        // Perform asynchronous download operation
        new Thread(() -> {
            // Simulated download logic
            try {
                // Simulate download process...
                Thread.sleep(5000); // Simulating delay
                // On success
            } catch (InterruptedException e) {
                // Handle interrupted exception
            } finally {
                isDownloadInProgress = false;
                // Notify UI to update once download is complete or if errors occurred
                mHandler.post(this::updateUIAfterDownload);
            }
        }).start();
    }
}

private void updateUIAfterDownload() {
    // Handle UI updates after the download process is completed
}

private void handleDownloadError(String errorMessage) {
    // Logic to notify user about the error
    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
}

//<End of snippet n. 1>