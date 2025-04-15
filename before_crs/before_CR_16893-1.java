/*GPS: Initialize XTRA support when GPS is enabled

BUG: 2923148

Change-Id:If9a643e385c6c567b30f47d4caa35fa812a2ba43Signed-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index c3dddad..ab79147 100755

//Synthetic comment -- @@ -216,6 +216,9 @@
// true if we started navigation
private boolean mStarted;

// for calculating time to first fix
private long mFixRequestTime = 0;
// time to first fix for most recent session
//Synthetic comment -- @@ -659,6 +662,7 @@
mEnabled = native_init();

if (mEnabled) {
if (mSuplServerHost != null) {
native_set_agps_server(AGPS_TYPE_SUPL, mSuplServerHost, mSuplServerPort);
}
//Synthetic comment -- @@ -860,7 +864,7 @@
return true;
}
if ("force_xtra_injection".equals(command)) {
            if (native_supports_xtra()) {
xtraDownloadRequest();
return true;
}
//Synthetic comment -- @@ -1383,7 +1387,7 @@
handleInjectNtpTime();
break;
case DOWNLOAD_XTRA_DATA:
                    if (native_supports_xtra()) {
handleDownloadXtraData();
}
break;







