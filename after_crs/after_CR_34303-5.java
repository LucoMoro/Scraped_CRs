/*GPS Provider main thread blocked by NTP and XTRA download

currently the NTP and XTRA download is running off of GPS provider
main thread. This could potentially block the next tasks for over
a minute of time. If the upcomings task happens to AGPS, AGPS will
time out.

Placed the NTP and XTRA downloads in other threads.

Change-Id:I57a6aaf5348212bc1246813f6d941da7d5b19136*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index b4e3c24..6b34829 100755

//Synthetic comment -- @@ -202,10 +202,15 @@
private boolean mNetworkAvailable;

// flags to trigger NTP or XTRA data download when network becomes available
    // initialized to DownloadStates.PendingNetwork for NTP, so we auto download
    // NTP when the network comes up after booting.  For XTRA however, we do not
    // want to auto download at power up upon first network connection.  So we
    // initialized mDownloadXtraDataPending to DownloadStates.Idle.
    private enum DownloadStates {
        PendingNetwork, Downloading, Idle;
    };
    private DownloadStates mInjectNtpTimePending = DownloadStates.PendingNetwork;
    private DownloadStates mDownloadXtraDataPending = DownloadStates.Idle;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;
//Synthetic comment -- @@ -549,10 +554,10 @@
}

if (mNetworkAvailable) {
            if (mInjectNtpTimePending == DownloadStates.PendingNetwork) {
sendMessage(INJECT_NTP_TIME, 0, null);
}
            if (mDownloadXtraDataPending == DownloadStates.PendingNetwork) {
sendMessage(DOWNLOAD_XTRA_DATA, 0, null);
}
}
//Synthetic comment -- @@ -561,10 +566,10 @@
private void handleInjectNtpTime() {
if (!mNetworkAvailable) {
// try again when network is up
            mInjectNtpTimePending = DownloadStates.PendingNetwork;
return;
}
        mInjectNtpTimePending = DownloadStates.Downloading;

long delay;

//Synthetic comment -- @@ -594,15 +599,17 @@
mHandler.removeMessages(INJECT_NTP_TIME);
mHandler.sendMessageDelayed(Message.obtain(mHandler, INJECT_NTP_TIME), delay);
}

        mInjectNtpTimePending = DownloadStates.Idle;
}

private void handleDownloadXtraData() {
if (!mNetworkAvailable) {
// try again when network is up
            mDownloadXtraDataPending = DownloadStates.PendingNetwork;
return;
}
        mDownloadXtraDataPending = DownloadStates.Downloading;


GpsXtraDownloader xtraDownloader = new GpsXtraDownloader(mContext, mProperties);
//Synthetic comment -- @@ -618,6 +625,7 @@
mHandler.removeMessages(DOWNLOAD_XTRA_DATA);
mHandler.sendMessageDelayed(Message.obtain(mHandler, DOWNLOAD_XTRA_DATA), RETRY_INTERVAL);
}
        mDownloadXtraDataPending = DownloadStates.Idle;
}

/**
//Synthetic comment -- @@ -1594,11 +1602,23 @@
handleUpdateNetworkState(msg.arg1, (NetworkInfo)msg.obj);
break;
case INJECT_NTP_TIME:
                    if (mInjectNtpTimePending != DownloadStates.Downloading) {
                        new Thread(new Runnable() {
                            public void run() {
                                handleInjectNtpTime();
                            }
                        }).start();
                    }
break;
case DOWNLOAD_XTRA_DATA:
                    if (mDownloadXtraDataPending != DownloadStates.Downloading) {
                        if (mSupportsXtra) {
                            new Thread(new Runnable() {
                                public void run() {
                                    handleDownloadXtraData();
                                }
                            }).start();
                        }
}
break;
case UPDATE_LOCATION:







