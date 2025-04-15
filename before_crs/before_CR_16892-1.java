/*GPS: Don't set XTRA download pending flag at boot time.

XTRA data downloads are now strictly on demand from the GPS engine.
Also fix typo in handleDownloadXtraData()

Change-Id:Ied1a6e2e62134add4d965326aae909c86f834682Signed-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index fa53ccf..c3dddad 100755

//Synthetic comment -- @@ -199,7 +199,7 @@
// flags to trigger NTP or XTRA data download when network becomes available
// initialized to true so we do NTP and XTRA when the network comes up after booting
private boolean mInjectNtpTimePending = true;
    private boolean mDownloadXtraDataPending = true;

// true if GPS is navigating
private boolean mNavigating;
//Synthetic comment -- @@ -522,7 +522,7 @@
}

private void handleDownloadXtraData() {
        if (!mDownloadXtraDataPending) {
// try again when network is up
mDownloadXtraDataPending = true;
return;







