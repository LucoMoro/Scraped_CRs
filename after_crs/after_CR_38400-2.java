/*Eliminate unnecessary intent ANY_DATA_STATE broadcast

Log shows that over 300 intent android.intent.action.ANY_DATA_STATE
were broadcasted during power-up. Some of them were not necessary.
And, those additional intents will impact the device power-up
performance.
To avoid it, add checks to broadcast the intent only when
the data state does get changed.

Change-Id:I9feac82cdbd0a6737f4535194177dc83c763b998*/




//Synthetic comment -- diff --git a/services/java/com/android/server/TelephonyRegistry.java b/services/java/com/android/server/TelephonyRegistry.java
//Synthetic comment -- index 8c8e725..b7a0869 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.util.Slog;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.NetworkInterface;
//Synthetic comment -- @@ -65,6 +66,56 @@
int events;
}

    private static class DataStateNotification {
        private int state;
        private boolean isDataConnectivityPossible;
        private String reason;
        private String apn;
        private LinkProperties linkProperties;
        private LinkCapabilities linkCapabilities;
        private int networkType;
        private boolean roaming;

        public DataStateNotification(int state, boolean isDataConnectivityPossible,
                String reason, String apn, LinkProperties linkProperties,
                LinkCapabilities linkCapabilities, int networkType, boolean roaming) {
            if (DBG) Slog.i(TAG, "new DataStateNotification");
            setValues(state, isDataConnectivityPossible, reason, apn, linkProperties,
                    linkCapabilities, networkType, roaming);
        }

        // return true if notification needs be broadcasted
        public boolean isNotificationNeeded(int state, boolean isDataConnectivityPossible,
                String reason, String apn, LinkProperties linkProperties,
                LinkCapabilities linkCapabilities, int networkType, boolean roaming) {
            if ( state == TelephonyManager.DATA_DISCONNECTED && isDataConnectivityPossible ==
                    this.isDataConnectivityPossible && state == this.state ) {
                if (DBG) Slog.i(TAG, "toBeNotified = false");
                return false;
            } else {
                if (DBG) Slog.i(TAG, "update dataStateNotification");
                setValues(state, isDataConnectivityPossible, reason, apn, linkProperties,
                        linkCapabilities, networkType, roaming);
                return true;
            }
        }

        private void setValues(int state, boolean isDataConnectivityPossible,
                String reason, String apn, LinkProperties linkProperties,
                LinkCapabilities linkCapabilities, int networkType, boolean roaming) {
            this.state = state;
            this.isDataConnectivityPossible = isDataConnectivityPossible;
            this.reason = reason;
            this.apn = apn;
            this.linkProperties = linkProperties;
            this.linkCapabilities = linkCapabilities;
            this.networkType = networkType;
            this.roaming = roaming;
        }
    }

    private HashMap<String, DataStateNotification> mPreviousDataNotifications;

private final Context mContext;

// access should be inside synchronized (mRecords) for these two fields
//Synthetic comment -- @@ -132,6 +183,7 @@
mContext = context;
mBatteryStats = BatteryStatsService.getService();
mConnectedApns = new ArrayList<String>();
        mPreviousDataNotifications = new HashMap<String, DataStateNotification>();
}

public void listen(String pkgForDebug, IPhoneStateListener callback, int events,
//Synthetic comment -- @@ -393,7 +445,24 @@
+ isDataConnectivityPossible + " reason='" + reason
+ "' apn='" + apn + "' apnType=" + apnType + " networkType=" + networkType);
}

        boolean toBeNotified = true;
synchronized (mRecords) {
            DataStateNotification dataStateNotification = mPreviousDataNotifications.get(apnType);
            if (dataStateNotification == null) {
                if (DBG) Slog.i(TAG, "dataStateNotification is null");
                dataStateNotification = new DataStateNotification(state, isDataConnectivityPossible,
                        reason, apn, linkProperties, linkCapabilities, networkType, roaming);
                mPreviousDataNotifications.put(apnType, dataStateNotification);
                if (DBG) Slog.i(TAG, "new DataStateNotification for type:" + apnType);
            } else {
                if (DBG) Slog.i(TAG, "dataStateNotification not null");
                toBeNotified = dataStateNotification.isNotificationNeeded(state,
                        isDataConnectivityPossible, reason, apn, linkProperties,
                        linkCapabilities, networkType, roaming);
            }
            if (DBG) Slog.i(TAG, "toBeNotified=" + toBeNotified);

boolean modified = false;
if (state == TelephonyManager.DATA_CONNECTED) {
if (!mConnectedApns.contains(apnType)) {
//Synthetic comment -- @@ -441,8 +510,10 @@
handleRemoveListLocked();
}
}
        if (toBeNotified) {
            broadcastDataConnectionStateChanged(state, isDataConnectivityPossible, reason,
                    apn, apnType, linkProperties, linkCapabilities, roaming);
        }
}

public void notifyDataConnectionFailed(String reason, String apnType) {







