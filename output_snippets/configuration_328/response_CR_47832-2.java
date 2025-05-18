//<Beginning of snippet n. 0>
int downloadStatus = mMessageItem.getMmsDownloadStatus();

if (downloadStatus == DownloadManager.STATE_DOWNLOADING) {
    showDownloadingAttachment();
    intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
    mContext.startService(intent);
} else {
    showLoadingIndicator();
    checkDownloadStates(new DownloadStateCallback() {
        @Override
        public void onDownloadStatesChecked(List<Integer> statuses) {
            boolean allDone = statuses.stream().allMatch(status -> status != DownloadManager.STATE_DOWNLOADING);
            hideLoadingIndicator();
            
            if (allDone) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));
                    }
                });
            }
        }

        @Override
        public void onError(String errorMessage) {
            hideLoadingIndicator();
            showError(errorMessage);
        }
    });
}

private void checkDownloadStates(DownloadStateCallback callback) {
    // Implement logic to batch handle and return download states for all MMS
}

private void showLoadingIndicator() {
    // Logic to show loading indicator
}

private void hideLoadingIndicator() {
    // Logic to hide loading indicator
}

private void showError(String message) {
    // Logic to show error message to the user
}

interface DownloadStateCallback {
    void onDownloadStatesChecked(List<Integer> statuses);
    void onError(String errorMessage);
}
//<End of snippet n. 0>