//<Beginning of snippet n. 0>


private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
// initialized to true so we do NTP and XTRA when the network comes up after booting
private boolean mInjectNtpTimePending = true;
private boolean mDownloadXtraDataPending = false; // Changed to false to prevent automatic XTRA downloads

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

// Method to request XTRA data download
public void requestXtraDataDownload() {
    if (mNetworkAvailable) {
        mDownloadXtraDataPending = true; // Set to true on explicit request
        // Code to initiate XTRA download goes here
    }
}

// Other relevant logic that includes checking mDownloadXtraDataPending 
// should ensure that it only processes true if a request has been made.

/* Additional logic for handling download goes here... */ 

//<End of snippet n. 0>