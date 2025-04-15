/*GPS provider disable XTRA download on powerup

changed GpsLocationProvider and disable the auto XTRA data download
at powerup.  As default, we do not want XTRA download to happen at
power up. We only want XTRA download on demand.

Change-Id:I3a2274a10064c57f94718b2906314b2e8a8e8eb2*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index d1ded10b..7ec29ef 100755

//Synthetic comment -- @@ -211,9 +211,10 @@
private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
    // initialized to true for NTP, but false for XTRA, so we only auto download
    // NTP when the network comes up after booting
private boolean mInjectNtpTimePending = true;
    private boolean mDownloadXtraDataPending = false;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;







