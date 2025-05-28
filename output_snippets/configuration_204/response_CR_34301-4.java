//<Beginning of snippet n. 0>

private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
// initialized to false to prevent automatic downloads at power-up
private boolean mInjectNtpTimePending = false;
private boolean mDownloadXtraDataPending = false;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

public void initiateDownload() {
    if (!mNetworkAvailable) {
        return;
    }
    mDownloadXtraDataPending = true; // Only set to true in response to a user/request
}

// Ensure that the state remains false during power-up
private void resetFlags() {
    mDownloadXtraDataPending = false;
    mInjectNtpTimePending = false;
}

// Call resetFlags() during the power-up sequence to ensure proper initialization
//<End of snippet n. 0>