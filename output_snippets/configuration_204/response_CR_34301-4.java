//<Beginning of snippet n. 0>

private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
private boolean mInjectNtpTimePending = false;
private boolean mDownloadXtraDataPending = false;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

public void powerUp() {
    mDownloadXtraDataPending = false;
    mInjectNtpTimePending = false;
    // Additional logic to set mNetworkAvailable if necessary can be added here
}

public void requestXtraDownload() {
    if (mNetworkAvailable && shouldDownloadXtra()) {
        mDownloadXtraDataPending = true;
        logDownloadInitiation();
        initiateXtraDownload();
    }
}

private boolean shouldDownloadXtra() {
    // Logic to allow on-demand download only based on user request
    return mDownloadXtraDataPending; // Validate based on explicit user-triggered actions
}

private void initiateXtraDownload() {
    // Logic to initiate XTRA download
}

private void logDownloadInitiation() {
    // Enhanced logging logic to track download events and suppressions
    // More details can be added here for traceability
}

//<End of snippet n. 0>