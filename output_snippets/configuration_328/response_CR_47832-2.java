//<Beginning of snippet n. 0>

mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));

switch (mMessageItem.getMmsDownloadStatus()) {
    case DownloadManager.STATE_DOWNLOADING:
        showDownloadingAttachment();
        disableDownloadButton();
        startMmsDownload();
        break;
    // Other cases for STATE_TRANSIENT_FAILURE and STATE_PERMANENT_FAILURE can go here.
}

// Async method for handling MMS download
private void startMmsDownload() {
    new Thread(() -> {
        try {
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
            mContext.startService(intent);
            // Notify UI updates in a synchronized manner for batch updates
            mHandler.post(() -> enableDownloadButton());
        } catch (Exception e) {
            mHandler.post(() -> handleError(e));
        }
    }).start();
}

// UI control methods
private void disableDownloadButton() {
    // Logic to disable the button and show loading indicator
}

private void enableDownloadButton() {
    // Logic to enable the button after download is complete
}

private void handleError(Exception e) {
    // Show user-friendly error message and re-enable the button
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;

//<End of snippet n. 1>