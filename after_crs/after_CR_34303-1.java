/*GPS Provider main thread blocked by NTP and XTRA download

currently the NTP and XTRA download is running off of GPS provider main
thread. This could potentially block the next tasks for over a minute of
time. If the upcomings task happens to AGPS, AGPS will time out.

Placed the NTP and XTRA downloads in other threads.

Change-Id:I57a6aaf5348212bc1246813f6d941da7d5b19136*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index affc551..4fbaec2 100755

//Synthetic comment -- @@ -203,8 +203,11 @@

// flags to trigger NTP or XTRA data download when network becomes available
// initialized to true so we do NTP and XTRA when the network comes up after booting
    private enum DownloadStates {
        PendingNetwork, Downloading, Idle;
    };
    private DownloadStates mInjectNtpTimePending = DownloadStates.PendingNetwork;
    private DownloadStates mDownloadXtraDataPending = DownloadStates.Idle;

// set to true if the GPS engine does not do on-demand NTP time requests
private boolean mPeriodicTimeInjection;
//Synthetic comment -- @@ -548,10 +551,10 @@
}

if (mNetworkAvailable) {
            if (mInjectNtpTimePending == DownloadStates.PendingNetwork) {
sendMessage(INJECT_NTP_TIME, 0, null);
}
            if (mDownloadXtraDataPending == DownloadStates.PendingNetwork) {
sendMessage(DOWNLOAD_XTRA_DATA, 0, null);
}
}
//Synthetic comment -- @@ -560,10 +563,10 @@
private void handleInjectNtpTime() {
if (!mNetworkAvailable) {
// try again when network is up
            mInjectNtpTimePending = DownloadStates.PendingNetwork;
return;
}
        mInjectNtpTimePending = DownloadStates.Downloading;

long delay;

//Synthetic comment -- @@ -593,15 +596,17 @@
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
//Synthetic comment -- @@ -617,6 +622,7 @@
mHandler.removeMessages(DOWNLOAD_XTRA_DATA);
mHandler.sendMessageDelayed(Message.obtain(mHandler, DOWNLOAD_XTRA_DATA), RETRY_INTERVAL);
}
        mDownloadXtraDataPending = DownloadStates.Idle;
}

/**
//Synthetic comment -- @@ -1592,11 +1598,23 @@
handleUpdateNetworkState(msg.arg1, (NetworkInfo)msg.obj);
break;
case INJECT_NTP_TIME:
                    if (mInjectNtpTimePending != DownloadStates.Downloading) {
                        new Thread(new Runnable() {
                            public void run() {
                                handleInjectNtpTime();
                            }
                        }).run();
                    }
break;
case DOWNLOAD_XTRA_DATA:
                    if (mDownloadXtraDataPending != DownloadStates.Downloading) {
                        if (mSupportsXtra) {
                            new Thread(new Runnable() {
                                public void run() {
                                    handleDownloadXtraData();
                                }
                            }).run();
                        }
}
break;
case UPDATE_LOCATION:







