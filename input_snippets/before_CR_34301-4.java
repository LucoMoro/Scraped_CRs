
//<Beginning of snippet n. 0>


private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
    // initialized to true so we do NTP and XTRA when the network comes up after booting
private boolean mInjectNtpTimePending = true;
    private boolean mDownloadXtraDataPending = true;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

//<End of snippet n. 0>








