//<Beginning of snippet n. 0>
new Thread(() -> {
    List<Integer> statuses = new ArrayList<>();
    // Assume method to get multiple MMS item statuses will be implemented
    statuses.add(mMessageItem.getMmsDownloadStatus());
    // Add logic here to retrieve multiple MMS statuses

    boolean hasError = false;
    int transientErrors = 0;
    int permanentErrors = 0;

    for (int status : statuses) {
        switch (status) {
            case DownloadManager.STATE_DOWNLOADING:
                hasError = false;
                break;
            case DownloadManager.STATE_TRANSIENT_FAILURE:
                transientErrors++;
                hasError = true;
                break;
            case DownloadManager.STATE_PERMANENT_FAILURE:
                permanentErrors++;
                hasError = true;
                break;
        }
    }

    mHandler.post(() -> {
        updateLoadingIndicator(false);
        StringBuilder errorMessage = new StringBuilder();
        if (transientErrors > 0) {
            errorMessage.append(transientErrors).append(" transient failure(s) occurred. ");
        }
        if (permanentErrors > 0) {
            errorMessage.append(permanentErrors).append(" permanent failure(s) occurred.");
        }
        if (hasError) {
            showError(errorMessage.toString());
        } else {
            showDownloadingAttachment();
        }
    });

    if (!hasError) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(TransactionBundle.TRANSACTION_TYPE, Transaction.RETRIEVE_TRANSACTION);
        if (!isServiceRunning(DownloadService.class)) {
            mContext.startService(intent);
        }
    }
}).start();
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;

private void showError(String message) {
    // Implement error showing logic
}

private void updateLoadingIndicator(boolean isLoading) {
    // Implement loading indicator logic
}

private boolean isServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
            return true;
        }
    }
    return false;
}
//<End of snippet n. 1>