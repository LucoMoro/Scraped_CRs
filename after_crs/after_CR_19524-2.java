/*Avoid sending two PDP deactivate at the same time

When an application is aggressive and calls startUsingNetworkFeature
two times in a row, the state machine in the framework gets confused.
This patch will not allow a disconnect when state is disconnecting.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 265bf7e..32fb08d 100644

//Synthetic comment -- @@ -16,16 +16,19 @@

package com.android.internal.telephony;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.gsm.ApnSetting;

import java.util.ArrayList;

/**
//Synthetic comment -- @@ -121,6 +124,9 @@
protected boolean[] dataEnabled = new boolean[APN_NUM_TYPES];
protected int enabledCount = 0;

    /* Currently active APN. If we have no active Apn this is null. */
    protected ApnSetting mActiveApn;

/* Currently requested APN type */
protected String mRequestedApnType = Phone.APN_TYPE_DEFAULT;

//Synthetic comment -- @@ -170,6 +176,11 @@
protected State state = State.IDLE;
protected Handler mDataConnectionTracker = null;

    /**
     * dataConnectionList
     * holds all the Data connection (PDP connection, i.e. IP Link in GPRS)
     */
    protected ArrayList<DataConnection> dataConnectionList;

protected long txPkts, rxPkts, sentSinceLastRecv;
protected int netStatPollPeriod;
//Synthetic comment -- @@ -267,7 +278,6 @@
protected abstract void onResetDone(AsyncResult ar);
protected abstract void onVoiceCallStarted();
protected abstract void onVoiceCallEnded();

@Override
public void handleMessage (Message msg) {
//Synthetic comment -- @@ -578,5 +588,63 @@
}
}

    /**
     * If tearDown is true, this only tears down a CONNECTED session. Presently,
     * there is no mechanism for abandoning an INITING/CONNECTING session,
     * but would likely involve cancelling pending async requests or
     * setting a flag or new state to ignore them when they came in
     * @param tearDown true if the underlying DataConnection should be
     * disconnected.
     * @param reason reason for the clean up.
     */
    protected void cleanUpConnection(boolean tearDown, String reason) {
        if (state == State.DISCONNECTING) {
            if (DBG) log("Clean up connection already in progress. Ignoring " + reason);
            return;
        }
        if (DBG) log("cleanUpConnection: reason: " + reason);

        // Clear the reconnect alarm, if set.
        if (mReconnectIntent != null) {
            AlarmManager am =
                (AlarmManager) phone.getContext().getSystemService(Context.ALARM_SERVICE);
            am.cancel(mReconnectIntent);
            mReconnectIntent = null;
        }

        setState(State.DISCONNECTING);

        boolean notificationDeferred = false;
        for (DataConnection conn : dataConnectionList) {
            if(conn != null) {
                if (tearDown) {
                    if (DBG) log("cleanUpConnection: teardown, call conn.disconnect");
                    conn.disconnect(obtainMessage(EVENT_DISCONNECT_DONE, reason));
                    notificationDeferred = true;
                } else {
                    if (DBG) log("cleanUpConnection: !tearDown, call conn.resetSynchronously");
                    conn.resetSynchronously();
                    notificationDeferred = false;
                }
            }
        }

        stopNetStatPoll();

        if (!notificationDeferred) {
            if (DBG) log("cleanupConnection: !notificationDeferred");
            gotoIdleAndNotifyDataConnection(reason);
        }
    }

    protected void onCleanUpConnection(boolean tearDown, String reason) {
        cleanUpConnection(tearDown, reason);
    }

    protected void gotoIdleAndNotifyDataConnection(String reason) {
        if (DBG) log("gotoIdleAndNotifyDataConnection: reason=" + reason);
        setState(State.IDLE);
        phone.notifyDataConnection(reason);
        mActiveApn = null;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..25a4ee3 100644

//Synthetic comment -- @@ -70,11 +70,6 @@
//useful for debugging
boolean failNextConnect = false;

/** Currently active CdmaDataConnection */
private CdmaDataConnection mActiveDataConnection;

//Synthetic comment -- @@ -111,9 +106,6 @@
Phone.APN_TYPE_MMS,
Phone.APN_TYPE_HIPRI };

// Possibly promote to base class, the only difference is
// the INTENT_RECONNECT_ALARM action is a different string.
// Do consider technology changes if it is promoted.
//Synthetic comment -- @@ -361,51 +353,6 @@
}
}

private CdmaDataConnection findFreeDataConnection() {
for (DataConnection connBase : dataConnectionList) {
CdmaDataConnection conn = (CdmaDataConnection) connBase;
//Synthetic comment -- @@ -624,13 +571,6 @@
setState(State.FAILED);
}

protected void onRecordsLoaded() {
if (state == State.FAILED) {
cleanUpConnection(false, null);
//Synthetic comment -- @@ -812,13 +752,6 @@
}
}

private void createAllDataConnectionList() {
dataConnectionList = new ArrayList<DataConnection>();
CdmaDataConnection dataConn;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ab9cf2a..d698da6 100644

//Synthetic comment -- @@ -118,14 +118,6 @@
private int waitingApnsPermanentFailureCountDown = 0;
private ApnSetting preferredApn = null;

/** Currently active DataConnection */
private GsmDataConnection mActivePdp;

//Synthetic comment -- @@ -384,7 +376,7 @@
* Formerly this method was ArrayList<GsmDataConnection> getAllPdps()
*/
public ArrayList<DataConnection> getAllDataConnections() {
        ArrayList<DataConnection> pdps = (ArrayList<DataConnection>)dataConnectionList.clone();
return pdps;
}

//Synthetic comment -- @@ -483,48 +475,6 @@
}

/**
* @param types comma delimited list of APN types
* @return array of APN types
*/
//Synthetic comment -- @@ -567,7 +517,7 @@
}

private GsmDataConnection findFreePdp() {
        for (DataConnection conn : dataConnectionList) {
GsmDataConnection pdp = (GsmDataConnection) conn;
if (pdp.isInactive()) {
return pdp;
//Synthetic comment -- @@ -754,13 +704,6 @@
mReregisterOnReconnectFailure = false;
}

/**
* This is a kludge to deal with the fact that
* the PDP state change notification doesn't always work
//Synthetic comment -- @@ -1245,10 +1188,6 @@
}
}

/**
* Based on the sim operator numeric, create a list for all possible pdps
* with all apns associated with that pdp
//Synthetic comment -- @@ -1295,19 +1234,19 @@
}

private void createAllPdpList() {
        dataConnectionList = new ArrayList<DataConnection>();
DataConnection pdp;

for (int i = 0; i < PDP_CONNECTION_POOL_SIZE; i++) {
pdp = GsmDataConnection.makeDataConnection(mGsmPhone);
            dataConnectionList.add(pdp);
}
}

private void destroyAllPdpList() {
        if(dataConnectionList != null) {
GsmDataConnection pdp;
            dataConnectionList.removeAll(dataConnectionList);
}
}








