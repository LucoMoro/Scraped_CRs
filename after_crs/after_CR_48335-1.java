/*Telephony: DataProfile priorities/arbitration

* Add flag to disable MPDN support
* Introduce priorities for different APN Types
* Create data calls in a prioritized order, tearing
  down lower priority calls if required.

Change-Id:I12d4c8bddce27477fcb244e0f873dbecf17ae116*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnContext.java b/src/java/com/android/internal/telephony/ApnContext.java
//Synthetic comment -- index 5e68784..5182408 100644

//Synthetic comment -- @@ -37,6 +37,8 @@

private DctConstants.State mState;

    private final int mPriority;

private ArrayList<DataProfile> mWaitingApns = null;

/** A zero indicates that all waiting APNs had a permanent error */
//Synthetic comment -- @@ -65,6 +67,7 @@
public ApnContext(String apnType, String logTag) {
mApnType = apnType;
mState = DctConstants.State.IDLE;
        mPriority = DataConnectionTracker.mApnPriorities.get(apnType);
setReason(Phone.REASON_DATA_ENABLED);
setRetryCount(0);
mDataEnabled = new AtomicBoolean(false);
//Synthetic comment -- @@ -150,6 +153,22 @@
return mWaitingApns;
}

    public synchronized int getPriority() {
        return mPriority;
    }

    public synchronized boolean isHigherPriority(ApnContext context) {
        return this.mPriority > context.getPriority();
    }

    public synchronized boolean isLowerPriority(ApnContext context) {
        return this.mPriority < context.getPriority();
    }

    public synchronized boolean isEqualPriority(ApnContext context) {
        return this.mPriority == context.getPriority();
    }

public synchronized void setState(DctConstants.State s) {
if (DBG) {
log("setState: " + s + ", previous state:" + mState);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 2e0a1a0..ffcef27 100644

//Synthetic comment -- @@ -57,8 +57,13 @@
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
//Synthetic comment -- @@ -236,6 +241,21 @@
protected ConcurrentHashMap<String, ApnContext> mApnContexts =
new ConcurrentHashMap<String, ApnContext>();

    /** Priorities for APN_TYPEs. package level access, used by ApnContext */
    static LinkedHashMap<String, Integer> mApnPriorities =
        new LinkedHashMap<String, Integer>() {
            {
                put(PhoneConstants.APN_TYPE_CBS,     7);
                put(PhoneConstants.APN_TYPE_IMS,     6);
                put(PhoneConstants.APN_TYPE_FOTA,    5);
                put(PhoneConstants.APN_TYPE_HIPRI,   4);
                put(PhoneConstants.APN_TYPE_DUN,     3);
                put(PhoneConstants.APN_TYPE_SUPL,    2);
                put(PhoneConstants.APN_TYPE_MMS,     1);
                put(PhoneConstants.APN_TYPE_DEFAULT, 0);
            }
        };

/* Currently active APN */
protected DataProfile mActiveApn;

//Synthetic comment -- @@ -597,6 +617,10 @@
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);
protected abstract void onUpdateIcc();
    /* If multiple calls (mms, supl etc) cannot be supported at the same time
     * (e.g: MPDN not supported), disconnect a lower priority call
     */
    protected abstract boolean disconnectOneLowerPriorityCall(String apnType);

@Override
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -1117,6 +1141,25 @@
}
}

    /* Return the list of ApnContexts based on their priorities */
    protected List<ApnContext> getPrioritySortedApnContextList() {

        ArrayList<ApnContext> sortedList = new ArrayList<ApnContext>();

        /*
         *  Get the prioritized enumerated APN Types and retrieve the APN
         *  context associated with it from the list of APN contexts
         */
        Iterator apnTypes = mApnPriorities.keySet().iterator();
        while(apnTypes.hasNext()) {
            ApnContext apnContext = mApnContexts.get(apnTypes.next());
            if (apnContext != null)
                sortedList.add(apnContext);
        }

        return sortedList;
    }

protected String getReryConfig(boolean forDefault) {
int nt = mPhone.getServiceState().getNetworkType();









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 7502dc5..71f40da 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.UsimServiceTable;

import com.android.internal.telephony.PhoneConstants.*; // ????

import java.util.List;

//Synthetic comment -- @@ -103,6 +103,9 @@
static final String REASON_NW_TYPE_CHANGED = "nwTypeChanged";
static final String REASON_DATA_DEPENDENCY_MET = "dependencyMet";
static final String REASON_DATA_DEPENDENCY_UNMET = "dependencyUnmet";
    static final String REASON_LINK_PROPERTIES_CHANGED = "linkPropertiesChanged";
    static final String REASON_TETHERED_MODE_STATE_CHANGED = "tetheredModeStateChanged";
    static final String REASON_SINGLE_PDN_ARBITRATION = "SinglePdnArbitration";

// Used for band mode selection methods
static final int BM_UNSPECIFIED = 0; // selected by baseband automatically








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 4067210..8740d97 100644

//Synthetic comment -- @@ -389,7 +389,6 @@
String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");

mActiveApn = (DataProfile)new DataProfileCdma(apnId, null, null, null, null,
RILConstants.SETUP_DATA_AUTH_PAP_CHAP, types, ipProto, roamingIpProto,
mPhone.getServiceState().getRadioTechnology());
//Synthetic comment -- @@ -504,7 +503,7 @@
if (mState == DctConstants.State.FAILED) {
cleanUpAllConnections(null);
}

sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA, Phone.REASON_SIM_LOADED));
}

//Synthetic comment -- @@ -953,6 +952,11 @@
}

@Override
    protected boolean disconnectOneLowerPriorityCall(String apnType) {
        return false;
    }

    @Override
public boolean isDisconnected() {
return ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.FAILED));
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4d5d53f..ae2819f 100644

//Synthetic comment -- @@ -127,6 +127,16 @@

private static final boolean DATA_STALL_SUSPECTED = true;
private static final boolean DATA_STALL_NOT_SUSPECTED = false;
    /*
     * If this property is set to true then android assumes that multiple PDN is
     * going to be supported in modem/nw. However if second PDN requests fails,
     * then behavior is going to be determined by the
     * SUPPORT_SERVICE_ARBITRATION property below. If MPDN is set to false, then
     * android will ensure that the higher priority service is active. Low
     * priority data calls may be pro-actively torn down to ensure this.
     */
    private static final boolean SUPPORT_MPDN = SystemProperties.getBoolean(
            "persist.telephony.mpdn", true);

@Override
protected void onActionIntentReconnectAlarm(Intent intent) {
//Synthetic comment -- @@ -207,6 +217,8 @@

initApnContextsAndDataConnection();
broadcastMessenger();

        log("SUPPORT_MPDN = " + SUPPORT_MPDN);
}

@Override
//Synthetic comment -- @@ -659,6 +671,8 @@
private void setupDataOnReadyApns(String reason) {
// Stop reconnect alarms on all data connections pending
// retry. Reset ApnContext state to IDLE.
        log("setupDataOnReadyApns: " + reason);

for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
if (dcac.getReconnectIntentSync() != null) {
cancelReconnectAlarm(dcac);
//Synthetic comment -- @@ -685,7 +699,8 @@
resetAllRetryCounts();

// Only check for default APN state
        for (ApnContext apnContext :
                    getPrioritySortedApnContextList().toArray(new ApnContext[0])) {
if (apnContext.getState() == DctConstants.State.FAILED) {
// By this time, alarms for all failed Apns
// should be stopped if any.
//Synthetic comment -- @@ -744,8 +759,27 @@

boolean desiredPowerState = mPhone.getServiceStateTracker().getDesiredPowerState();

        // If MPDN is disabled and if the current active ApnContext cannot handle the
        // requested apnType, then
        //  - Disconnect one active low priority data call if there is any, and after
        //    disconnect setup up the new requested connection.
        //  - Do not bring up the requested connection, if there is any high priority
        //    data connection is active.
        if (SUPPORT_MPDN == false
                && !isAnyActiveApnContextHandlesType(apnContext.getApnType())) {
            if (disconnectOneLowerPriorityCall(apnContext.getApnType())) {
                log("Lower/Equal priority call disconnected.");
                return false;
            }

            if (isHigherPriorityDataCallActive(apnContext.getApnType())) {
                log("Higher priority call active. Ignoring setup data call request.");
                return false;
            }
        }

if ((apnContext.getState() == DctConstants.State.IDLE ||
                apnContext.getState() == DctConstants.State.SCANNING) &&
isDataAllowed(apnContext) && getAnyDataEnabled() && !isEmergency()) {

if (apnContext.getState() == DctConstants.State.IDLE) {
//Synthetic comment -- @@ -1393,12 +1427,12 @@
mActiveApn = null;
}


@Override
protected DataProfile fetchDunApn() {
Context c = mPhone.getContext();
        String apnData = Settings.Global.getString(c.getContentResolver(),
                Settings.Global.TETHER_DUN_APN);
ApnSetting dunSetting = ApnSetting.fromString(apnData);
if (dunSetting != null) return dunSetting;

//Synthetic comment -- @@ -1406,6 +1440,63 @@
return (DataProfile)ApnSetting.fromString(apnData);
}

    private boolean isAnyActiveApnContextHandlesType(String apnType) {
        for (ApnContext apnContext : mApnContexts.values()) {
            if (!apnContext.isDisconnected()) {
                // If the ApnContext can handle the request apnType, do not disconnect
                DataProfile apnSetting = apnContext.getApnSetting();
                if (apnSetting != null && apnSetting.canHandleType(apnType)) {
                    // Found a ApnContext, which can handle the required apn type
                    log("isAnyActiveApnContextHandlesType:  - apnContext = [" + apnContext + "]"
                            + " can handle apnType=" + apnType);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHigherPriorityDataCallActive(String apnType) {
        boolean result = false;
        ApnContext apnContext = mApnContexts.get(apnType);

        for (ApnContext apnContextEntry :
                getPrioritySortedApnContextList().toArray(new ApnContext[0])) {
            if (apnContextEntry.isHigherPriority(apnContext)
                    && (apnContextEntry.getState() == DctConstants.State.CONNECTED
                        || apnContextEntry.getState() == DctConstants.State.CONNECTING
                        || apnContextEntry.getState() == DctConstants.State.INITING)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    protected boolean disconnectOneLowerPriorityCall(String apnType) {
        boolean disconnect = false;

        ApnContext apnContext = mApnContexts.get(apnType);

        for (ApnContext apnContextEntry :
                getPrioritySortedApnContextList().toArray(new ApnContext[0])) {
            if (!apnContextEntry.isDisconnected() &&
                    apnContextEntry.isLowerPriority(apnContext)) {
                disconnect = true;

                // Found a lower priority call, disconnect it.
                apnContextEntry.setReason(Phone.REASON_SINGLE_PDN_ARBITRATION);
                cleanUpConnection(true, apnContextEntry);
                break;
            }
        }

        log("disconnectOneLowerPriorityCall:" + apnContext.getApnType() + " " + disconnect);

        return disconnect;
    }

@Override
protected void restartRadio() {
if (DBG) log("restartRadio: ************TURN OFF RADIO**************");
//Synthetic comment -- @@ -1442,7 +1533,8 @@
private boolean retryAfterDisconnected(String reason) {
boolean retry = true;

        if (( Phone.REASON_RADIO_TURNED_OFF.equals(reason) )
                || (!SUPPORT_MPDN && Phone.REASON_SINGLE_PDN_ARBITRATION.equals(reason)) ) {
retry = false;
}
return retry;
//Synthetic comment -- @@ -1947,6 +2039,9 @@
apnContext.setDataConnection(null);
apnContext.setDataConnectionAc(null);
}

        if (SUPPORT_MPDN == false)
            setupDataOnReadyApns(Phone.REASON_SINGLE_PDN_ARBITRATION);
}

protected void onPollPdp() {







