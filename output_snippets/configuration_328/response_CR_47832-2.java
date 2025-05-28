//<Beginning of snippet n. 0>
int downloadStatus = mMessageItem.getMmsDownloadStatus();

if (downloadStatus == DownloadManager.STATE_DOWNLOADING) {
    showDownloadingAttachment();
    intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
    mContext.startService(intent);
} else {
    List<Integer> statuses = checkDownloadStates(); // Batch check for download states
    boolean allDone = statuses.stream().allMatch(status -> status != DownloadManager.STATE_DOWNLOADING);

    if (allDone) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));
            }
        });
    }
}

private List<Integer> checkDownloadStates() {
    // Implement logic to batch handle and return download states for all MMS
}
//<End of snippet n. 0>