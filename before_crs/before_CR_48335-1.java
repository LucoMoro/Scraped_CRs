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

private ArrayList<DataProfile> mWaitingApns = null;

/** A zero indicates that all waiting APNs had a permanent error */
//Synthetic comment -- @@ -65,6 +67,7 @@
public ApnContext(String apnType, String logTag) {
mApnType = apnType;
mState = DctConstants.State.IDLE;
setReason(Phone.REASON_DATA_ENABLED);
setRetryCount(0);
mDataEnabled = new AtomicBoolean(false);
//Synthetic comment -- @@ -150,6 +153,22 @@
return mWaitingApns;
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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
//Synthetic comment -- @@ -236,6 +241,21 @@
protected ConcurrentHashMap<String, ApnContext> mApnContexts =
new ConcurrentHashMap<String, ApnContext>();

/* Currently active APN */
protected DataProfile mActiveApn;

//Synthetic comment -- @@ -597,6 +617,10 @@
protected abstract void onCleanUpAllConnections(String cause);
protected abstract boolean isDataPossible(String apnType);
protected abstract void onUpdateIcc();

@Override
public void handleMessage(Message msg) {
//Synthetic comment -- @@ -1117,6 +1141,25 @@
}
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

// Used for band mode selection methods
static final int BM_UNSPECIFIED = 0; // selected by baseband automatically








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 4067210..8740d97 100644

//Synthetic comment -- @@ -389,7 +389,6 @@
String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");

        // TODO: home operator numeric! 
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
public boolean isDisconnected() {
return ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.FAILED));
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4d5d53f..ae2819f 100644

//Synthetic comment -- @@ -127,6 +127,16 @@

private static final boolean DATA_STALL_SUSPECTED = true;
private static final boolean DATA_STALL_NOT_SUSPECTED = false;

@Override
protected void onActionIntentReconnectAlarm(Intent intent) {
//Synthetic comment -- @@ -207,6 +217,8 @@

initApnContextsAndDataConnection();
broadcastMessenger();
}

@Override
//Synthetic comment -- @@ -659,6 +671,8 @@
private void setupDataOnReadyApns(String reason) {
// Stop reconnect alarms on all data connections pending
// retry. Reset ApnContext state to IDLE.
for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
if (dcac.getReconnectIntentSync() != null) {
cancelReconnectAlarm(dcac);
//Synthetic comment -- @@ -685,7 +699,8 @@
resetAllRetryCounts();

// Only check for default APN state
        for (ApnContext apnContext : mApnContexts.values()) {
if (apnContext.getState() == DctConstants.State.FAILED) {
// By this time, alarms for all failed Apns
// should be stopped if any.
//Synthetic comment -- @@ -744,8 +759,27 @@

boolean desiredPowerState = mPhone.getServiceStateTracker().getDesiredPowerState();

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
        String apnData = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.TETHER_DUN_APN);
ApnSetting dunSetting = ApnSetting.fromString(apnData);
if (dunSetting != null) return dunSetting;

//Synthetic comment -- @@ -1406,6 +1440,63 @@
return (DataProfile)ApnSetting.fromString(apnData);
}

@Override
protected void restartRadio() {
if (DBG) log("restartRadio: ************TURN OFF RADIO**************");
//Synthetic comment -- @@ -1442,7 +1533,8 @@
private boolean retryAfterDisconnected(String reason) {
boolean retry = true;

        if ( Phone.REASON_RADIO_TURNED_OFF.equals(reason) ) {
retry = false;
}
return retry;
//Synthetic comment -- @@ -1947,6 +2039,9 @@
apnContext.setDataConnection(null);
apnContext.setDataConnectionAc(null);
}
}

protected void onPollPdp() {







