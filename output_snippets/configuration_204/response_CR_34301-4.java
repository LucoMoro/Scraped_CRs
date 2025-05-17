//<Beginning of snippet n. 0>

private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
// initialized to false to prevent automatic XTRA download at powerup
private boolean mInjectNtpTimePending = false;
private boolean mDownloadXtraDataPending = false; // Changed to false

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

//<End of snippet n. 0>