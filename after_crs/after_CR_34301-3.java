/*GPS provider disable XTRA download on powerup

changed GpsLocationProvider and disable the auto XTRA data download
at powerup.

Change-Id:I3a2274a10064c57f94718b2906314b2e8a8e8eb2*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index b33db23..c3a9087 100755

//Synthetic comment -- @@ -213,7 +213,7 @@
// flags to trigger NTP or XTRA data download when network becomes available
// initialized to true so we do NTP and XTRA when the network comes up after booting
private boolean mInjectNtpTimePending = true;
    private boolean mDownloadXtraDataPending = false;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;







