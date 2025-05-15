//<Beginning of snippet n. 0>

private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
// initialized to false to prevent automatic downloads during GPS provider startup
private boolean mInjectNtpTimePending = false;
private boolean mDownloadXtraDataPending = false; // Changed to false for on-demand downloads

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;

//<End of snippet n. 0>