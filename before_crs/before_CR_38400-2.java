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
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.NetworkInterface;
//Synthetic comment -- @@ -65,6 +66,56 @@
int events;
}

private final Context mContext;

// access should be inside synchronized (mRecords) for these two fields
//Synthetic comment -- @@ -132,6 +183,7 @@
mContext = context;
mBatteryStats = BatteryStatsService.getService();
mConnectedApns = new ArrayList<String>();
}

public void listen(String pkgForDebug, IPhoneStateListener callback, int events,
//Synthetic comment -- @@ -393,7 +445,24 @@
+ isDataConnectivityPossible + " reason='" + reason
+ "' apn='" + apn + "' apnType=" + apnType + " networkType=" + networkType);
}
synchronized (mRecords) {
boolean modified = false;
if (state == TelephonyManager.DATA_CONNECTED) {
if (!mConnectedApns.contains(apnType)) {
//Synthetic comment -- @@ -441,8 +510,10 @@
handleRemoveListLocked();
}
}
        broadcastDataConnectionStateChanged(state, isDataConnectivityPossible, reason, apn,
                apnType, linkProperties, linkCapabilities, roaming);
}

public void notifyDataConnectionFailed(String reason, String apnType) {







