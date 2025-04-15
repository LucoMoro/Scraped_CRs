/*Avoid sending two PDP deactivate at the same time

When an application is aggressive and calls startUsingNetworkFeature
two times in a row, the state machine in the framework gets confused.
This patch will not allow a disconnect when state is disconnecting.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 265bf7e..32fb08d 100644

//Synthetic comment -- @@ -16,16 +16,19 @@

package com.android.internal.telephony;

import android.app.PendingIntent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
//Synthetic comment -- @@ -121,6 +124,9 @@
protected boolean[] dataEnabled = new boolean[APN_NUM_TYPES];
protected int enabledCount = 0;

/* Currently requested APN type */
protected String mRequestedApnType = Phone.APN_TYPE_DEFAULT;

//Synthetic comment -- @@ -170,6 +176,11 @@
protected State state = State.IDLE;
protected Handler mDataConnectionTracker = null;


protected long txPkts, rxPkts, sentSinceLastRecv;
protected int netStatPollPeriod;
//Synthetic comment -- @@ -267,7 +278,6 @@
protected abstract void onResetDone(AsyncResult ar);
protected abstract void onVoiceCallStarted();
protected abstract void onVoiceCallEnded();
    protected abstract void onCleanUpConnection(boolean tearDown, String reason);

@Override
public void handleMessage (Message msg) {
//Synthetic comment -- @@ -578,5 +588,63 @@
}
}


}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index fa80063..8f0067a 100644

//Synthetic comment -- @@ -70,11 +70,6 @@
//useful for debugging
boolean failNextConnect = false;

    /**
     * dataConnectionList holds all the Data connection
     */
    private ArrayList<DataConnection> dataConnectionList;

/** Currently active CdmaDataConnection */
private CdmaDataConnection mActiveDataConnection;

//Synthetic comment -- @@ -111,9 +106,6 @@
Phone.APN_TYPE_MMS,
Phone.APN_TYPE_HIPRI };

    // if we have no active Apn this is null
    protected ApnSetting mActiveApn;

// Possibly promote to base class, the only difference is
// the INTENT_RECONNECT_ALARM action is a different string.
// Do consider technology changes if it is promoted.
//Synthetic comment -- @@ -361,51 +353,6 @@
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
    private void cleanUpConnection(boolean tearDown, String reason) {
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

private CdmaDataConnection findFreeDataConnection() {
for (DataConnection connBase : dataConnectionList) {
CdmaDataConnection conn = (CdmaDataConnection) connBase;
//Synthetic comment -- @@ -625,13 +572,6 @@
setState(State.FAILED);
}

    private void gotoIdleAndNotifyDataConnection(String reason) {
        if (DBG) log("gotoIdleAndNotifyDataConnection: reason=" + reason);
        setState(State.IDLE);
        phone.notifyDataConnection(reason);
        mActiveApn = null;
    }

protected void onRecordsLoaded() {
if (state == State.FAILED) {
cleanUpConnection(false, null);
//Synthetic comment -- @@ -813,13 +753,6 @@
}
}

    /**
     * @override com.android.internal.telephony.DataConnectionTracker
     */
    protected void onCleanUpConnection(boolean tearDown, String reason) {
        cleanUpConnection(tearDown, reason);
    }

private void createAllDataConnectionList() {
dataConnectionList = new ArrayList<DataConnection>();
CdmaDataConnection dataConn;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 96005f0..6458cb5 100644

//Synthetic comment -- @@ -118,14 +118,6 @@
private int waitingApnsPermanentFailureCountDown = 0;
private ApnSetting preferredApn = null;

    /* Currently active APN */
    protected ApnSetting mActiveApn;

    /**
     * pdpList holds all the PDP connection, i.e. IP Link in GPRS
     */
    private ArrayList<DataConnection> pdpList;

/** Currently active DataConnection */
private GsmDataConnection mActivePdp;

//Synthetic comment -- @@ -390,7 +382,7 @@
* Formerly this method was ArrayList<GsmDataConnection> getAllPdps()
*/
public ArrayList<DataConnection> getAllDataConnections() {
        ArrayList<DataConnection> pdps = (ArrayList<DataConnection>)pdpList.clone();
return pdps;
}

//Synthetic comment -- @@ -489,48 +481,6 @@
}

/**
     * If tearDown is true, this only tears down a CONNECTED session. Presently,
     * there is no mechanism for abandoning an INITING/CONNECTING session,
     * but would likely involve cancelling pending async requests or
     * setting a flag or new state to ignore them when they came in
     * @param tearDown true if the underlying GsmDataConnection should be
     * disconnected.
     * @param reason reason for the clean up.
     */
    private void cleanUpConnection(boolean tearDown, String reason) {
        if (DBG) log("Clean up connection due to " + reason);

        // Clear the reconnect alarm, if set.
        if (mReconnectIntent != null) {
            AlarmManager am =
                (AlarmManager) phone.getContext().getSystemService(Context.ALARM_SERVICE);
            am.cancel(mReconnectIntent);
            mReconnectIntent = null;
        }

        setState(State.DISCONNECTING);

        boolean notificationDeferred = false;
        for (DataConnection conn : pdpList) {
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
        stopNetStatPoll();

        if (!notificationDeferred) {
            if (DBG) log("cleanupConnection: !notificationDeferred");
            gotoIdleAndNotifyDataConnection(reason);
        }
    }

    /**
* @param types comma delimited list of APN types
* @return array of APN types
*/
//Synthetic comment -- @@ -576,7 +526,7 @@
}

private GsmDataConnection findFreePdp() {
        for (DataConnection conn : pdpList) {
GsmDataConnection pdp = (GsmDataConnection) conn;
if (pdp.isInactive()) {
return pdp;
//Synthetic comment -- @@ -763,13 +713,6 @@
mReregisterOnReconnectFailure = false;
}

    private void gotoIdleAndNotifyDataConnection(String reason) {
        if (DBG) log("gotoIdleAndNotifyDataConnection: reason=" + reason);
        setState(State.IDLE);
        phone.notifyDataConnection(reason);
        mActiveApn = null;
    }

/**
* This is a kludge to deal with the fact that
* the PDP state change notification doesn't always work
//Synthetic comment -- @@ -1254,10 +1197,6 @@
}
}

    protected void onCleanUpConnection(boolean tearDown, String reason) {
        cleanUpConnection(tearDown, reason);
    }

/**
* Based on the sim operator numeric, create a list for all possible pdps
* with all apns associated with that pdp
//Synthetic comment -- @@ -1304,19 +1243,19 @@
}

private void createAllPdpList() {
        pdpList = new ArrayList<DataConnection>();
DataConnection pdp;

for (int i = 0; i < PDP_CONNECTION_POOL_SIZE; i++) {
pdp = GsmDataConnection.makeDataConnection(mGsmPhone);
            pdpList.add(pdp);
}
}

private void destroyAllPdpList() {
        if(pdpList != null) {
GsmDataConnection pdp;
            pdpList.removeAll(pdpList);
}
}








