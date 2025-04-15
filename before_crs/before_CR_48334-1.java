/*Telephony: Introduce DataProfile base class

DataProfile is a base class from which we derive ApnSetting
for GSM and DataProfileCdma for CDMA. This can be extended
for other classes of DataProfiles.

Change-Id:I61bddbe6e57baf44325264a7f255cf19c1119f81Conflicts:

	src/java/com/android/internal/telephony/ApnContext.java
	src/java/com/android/internal/telephony/DataConnectionTracker.java
	src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
	src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
	src/java/com/android/internal/telephony/gsm/GsmDataConnection.java
	src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnContext.java b/src/java/com/android/internal/telephony/ApnContext.java
//Synthetic comment -- index b6083ad..5e68784 100644

//Synthetic comment -- @@ -37,12 +37,12 @@

private DctConstants.State mState;

    private ArrayList<ApnSetting> mWaitingApns = null;

/** A zero indicates that all waiting APNs had a permanent error */
private AtomicInteger mWaitingApnsPermanentFailureCountDown;

    private ApnSetting mApnSetting;

DataConnection mDataConnection;

//Synthetic comment -- @@ -107,15 +107,15 @@
mDataConnectionAc = dcac;
}

    public synchronized ApnSetting getApnSetting() {
        return mApnSetting;
}

    public synchronized void setApnSetting(ApnSetting apnSetting) {
        mApnSetting = apnSetting;
}

    public synchronized void setWaitingApns(ArrayList<ApnSetting> waitingApns) {
mWaitingApns = waitingApns;
mWaitingApnsPermanentFailureCountDown.set(mWaitingApns.size());
}
//Synthetic comment -- @@ -128,9 +128,9 @@
mWaitingApnsPermanentFailureCountDown.decrementAndGet();
}

    public synchronized ApnSetting getNextWaitingApn() {
        ArrayList<ApnSetting> list = mWaitingApns;
        ApnSetting apn = null;

if (list != null) {
if (!list.isEmpty()) {
//Synthetic comment -- @@ -140,13 +140,13 @@
return apn;
}

    public synchronized void removeWaitingApn(ApnSetting apn) {
if (mWaitingApns != null) {
mWaitingApns.remove(apn);
}
}

    public synchronized ArrayList<ApnSetting> getWaitingApns() {
return mWaitingApns;
}

//Synthetic comment -- @@ -231,7 +231,7 @@
// We don't print mDataConnection because its recursive.
return "{mApnType=" + mApnType + " mState=" + getState() + " mWaitingApns=" + mWaitingApns +
" mWaitingApnsPermanentFailureCountDown=" + mWaitingApnsPermanentFailureCountDown +
                " mApnSetting=" + mApnSetting + " mDataConnectionAc=" + mDataConnectionAc +
" mReason=" + mReason + " mRetryCount=" + mRetryCount +
" mDataEnabled=" + mDataEnabled + " mDependencyMet=" + mDependencyMet + "}";
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnSetting.java b/src/java/com/android/internal/telephony/ApnSetting.java
//Synthetic comment -- index b84c69c..d465789 100755

//Synthetic comment -- @@ -16,63 +16,42 @@

package com.android.internal.telephony;

/**
* This class represents a apn setting for create PDP link
*/
public class ApnSetting {

static final String V2_FORMAT_REGEX = "^\\[ApnSettingV2\\]\\s*";

public final String carrier;
    public final String apn;
public final String proxy;
public final String port;
public final String mmsc;
public final String mmsProxy;
public final String mmsPort;
    public final String user;
    public final String password;
    public final int authType;
    public final String[] types;
    public final int id;
    public final String numeric;
    public final String protocol;
    public final String roamingProtocol;
/**
* Current status of APN
* true : enabled APN, false : disabled APN.
*/
public final boolean carrierEnabled;
    /**
      * Radio Access Technology info
      * To check what values can hold, refer to ServiceState.java.
      * This should be spread to other technologies,
      * but currently only used for LTE(14) and EHRPD(13).
      */
    public final int bearer;

public ApnSetting(int id, String numeric, String carrier, String apn,
String proxy, String port,
String mmsc, String mmsProxy, String mmsPort,
String user, String password, int authType, String[] types,
String protocol, String roamingProtocol, boolean carrierEnabled, int bearer) {
        this.id = id;
        this.numeric = numeric;
this.carrier = carrier;
        this.apn = apn;
this.proxy = proxy;
this.port = port;
this.mmsc = mmsc;
this.mmsProxy = mmsProxy;
this.mmsPort = mmsPort;
        this.user = user;
        this.password = password;
        this.authType = authType;
        this.types = types;
        this.protocol = protocol;
        this.roamingProtocol = roamingProtocol;
this.carrierEnabled = carrierEnabled;
        this.bearer = bearer;
}

/**
//Synthetic comment -- @@ -178,6 +157,17 @@
return sb.toString();
}

public boolean canHandleType(String type) {
for (String t : types) {
// DEFAULT handles all, and HIPRI is handled by DEFAULT
//Synthetic comment -- @@ -197,4 +187,14 @@
if (o instanceof ApnSetting == false) return false;
return (this.toString().equals(o.toString()));
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..6c2177f 100644

//Synthetic comment -- @@ -18,6 +18,7 @@


import com.android.internal.telephony.DataCallState.SetupResult;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;
import com.android.internal.util.State;
//Synthetic comment -- @@ -78,13 +79,13 @@
* Used internally for saving connecting parameters.
*/
protected static class ConnectionParams {
        public ConnectionParams(ApnSetting apn, Message onCompletedMsg) {
this.apn = apn;
this.onCompletedMsg = onCompletedMsg;
}

public int tag;
        public ApnSetting apn;
public Message onCompletedMsg;
}

//Synthetic comment -- @@ -235,7 +236,7 @@
protected static final int EVENT_LOG_BAD_DNS_ADDRESS = 50100;

//***** Member Variables
    protected ApnSetting mApn;
protected int mTag;
protected PhoneBase phone;
protected int mRilVersion = -1;
//Synthetic comment -- @@ -1210,7 +1211,7 @@
*        AsyncResult.result = FailCause and AsyncResult.exception = Exception().
* @param apn is the Access Point Name to bring up a connection to
*/
    public void bringUp(Message onCompletedMsg, ApnSetting apn) {
sendMessage(obtainMessage(EVENT_CONNECT, new ConnectionParams(apn, onCompletedMsg)));
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionAc.java b/src/java/com/android/internal/telephony/DataConnectionAc.java
//Synthetic comment -- index 2cd64e1..15ae381 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.internal.telephony;

import com.android.internal.telephony.DataConnection.UpdateLinkPropertyResult;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;

//Synthetic comment -- @@ -254,7 +255,7 @@
}

/**
     * Request the connections ApnSetting.
* Response {@link #rspApnSetting}
*/
public void reqApnSetting() {
//Synthetic comment -- @@ -263,23 +264,23 @@
}

/**
     * Evaluate a RSP_APN_SETTING message and return the ApnSetting.
*
* @param response Message
     * @return ApnSetting, maybe null
*/
    public ApnSetting rspApnSetting(Message response) {
        ApnSetting retVal = (ApnSetting) response.obj;
if (DBG) log("rspApnSetting=" + retVal);
return retVal;
}

/**
     * Get the connections ApnSetting.
*
     * @return ApnSetting or null if an error
*/
    public ApnSetting getApnSettingSync() {
Message response = sendMessageSynchronously(REQ_GET_APNSETTING);
if ((response != null) && (response.what == RSP_GET_APNSETTING)) {
return rspApnSetting(response);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 347f00e..2e0a1a0 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.AsyncChannel;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -236,13 +237,13 @@
new ConcurrentHashMap<String, ApnContext>();

/* Currently active APN */
    protected ApnSetting mActiveApn;

/** allApns holds all apns */
    protected ArrayList<ApnSetting> mAllApns = null;

/** preferred apn */
    protected ApnSetting mPreferredApn = null;

/** Is packet service restricted by network */
protected boolean mIsPsRestricted = false;
//Synthetic comment -- @@ -491,15 +492,15 @@
public boolean isApnTypeActive(String type) {
// TODO: support simultaneous with List instead
if (PhoneConstants.APN_TYPE_DUN.equals(type)) {
            ApnSetting dunApn = fetchDunApn();
if (dunApn != null) {
                return ((mActiveApn != null) && (dunApn.toString().equals(mActiveApn.toString())));
}
}
return mActiveApn != null && mActiveApn.canHandleType(type);
}

    protected ApnSetting fetchDunApn() {
if (SystemProperties.getBoolean("net.tethering.noprovisioning", false)) {
log("fetchDunApn: net.tethering.noprovisioning=true ret: null");
return null;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataProfile.java b/src/java/com/android/internal/telephony/DataProfile.java
new file mode 100644
//Synthetic comment -- index 0000000..ac96d04

//Synthetic comment -- @@ -0,0 +1,108 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index a041974..8ef1ac4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@

import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
//Synthetic comment -- @@ -74,6 +75,8 @@
createTime = -1;
lastFailTime = -1;
lastFailCause = FailCause.NONE;
int dataProfile;
if ((cp.apn != null) && (cp.apn.types.length > 0) && (cp.apn.types[0] != null) &&
(cp.apn.types[0].equals(PhoneConstants.APN_TYPE_DUN))) {
//Synthetic comment -- @@ -83,14 +86,16 @@
dataProfile = RILConstants.DATA_PROFILE_DEFAULT;
}

// msg.obj will be returned in AsyncResult.userObj;
Message msg = obtainMessage(EVENT_SETUP_DATA_CONNECTION_DONE, cp);
msg.obj = cp;
phone.mCM.setupDataCall(
Integer.toString(getRilRadioTechnology(RILConstants.SETUP_DATA_TECH_CDMA)),
                Integer.toString(dataProfile),
                null, null, null,
                Integer.toString(RILConstants.SETUP_DATA_AUTH_PAP_CHAP),
RILConstants.SETUP_DATA_PROTOCOL_IP, msg);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 0e68125..4067210 100644

//Synthetic comment -- @@ -31,7 +31,8 @@
import android.util.EventLog;
import android.telephony.Rlog;

import com.android.internal.telephony.ApnSetting;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
//Synthetic comment -- @@ -357,7 +358,7 @@
private CdmaDataConnection findFreeDataConnection() {
for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
if (dcac.isInactiveSync()) {
                log("found free GsmDataConnection");
return (CdmaDataConnection) dcac.dataConnection;
}
}
//Synthetic comment -- @@ -384,8 +385,14 @@
types = mDefaultApnTypes;
apnId = mDefaultApnId;
}
        mActiveApn = new ApnSetting(apnId, "", "", "", "", "", "", "", "", "",
                                    "", 0, types, "IP", "IP", true, 0);
if (DBG) log("call conn.bringUp mActiveApn=" + mActiveApn);

Message msg = obtainMessage();
//Synthetic comment -- @@ -497,6 +504,7 @@
if (mState == DctConstants.State.FAILED) {
cleanUpAllConnections(null);
}
sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA, Phone.REASON_SIM_LOADED));
}

//Synthetic comment -- @@ -939,6 +947,11 @@
}
}

@Override
public boolean isDisconnected() {
return ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.FAILED));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java b/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java
new file mode 100644
//Synthetic comment -- index 0000000..f0f4970

//Synthetic comment -- @@ -0,0 +1,63 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java
//Synthetic comment -- index af36f8e..9202d6d 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -75,9 +76,9 @@
void onConnect(ConnectionParams cp) {
mApn = cp.apn;

        if (DBG) log("Connecting to carrier: '" + mApn.carrier
+ "' APN: '" + mApn.apn
                + "' proxy: '" + mApn.proxy + "' port: '" + mApn.port);

createTime = -1;
lastFailTime = -1;
//Synthetic comment -- @@ -134,11 +135,11 @@
// if Proxy is an IP-address.
// Otherwise, the default APN will not be restored anymore.
if (!mApn.types[0].equals(PhoneConstants.APN_TYPE_MMS)
                || !isIpAddress(mApn.mmsProxy)) {
log(String.format(
"isDnsOk: return false apn.types[0]=%s APN_TYPE_MMS=%s isIpAddress(%s)=%s",
                        mApn.types[0], PhoneConstants.APN_TYPE_MMS, mApn.mmsProxy,
                        isIpAddress(mApn.mmsProxy)));
return false;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index e8b662c..4d5d53f 100644

//Synthetic comment -- @@ -51,8 +51,10 @@
import android.util.EventLog;
import android.telephony.Rlog;

import com.android.internal.telephony.ApnContext;
import com.android.internal.telephony.ApnSetting;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnection.FailCause;
//Synthetic comment -- @@ -389,7 +391,7 @@
if (DBG) log( "get active apn string for type:" + apnType);
ApnContext apnContext = mApnContexts.get(apnType);
if (apnContext != null) {
            ApnSetting apnSetting = apnContext.getApnSetting();
if (apnSetting != null) {
return apnSetting.apn;
}
//Synthetic comment -- @@ -547,7 +549,7 @@
}

if (mAllApns != null) {
            for (ApnSetting apn : mAllApns) {
if (apn.canHandleType(type)) {
return true;
}
//Synthetic comment -- @@ -747,7 +749,7 @@
isDataAllowed(apnContext) && getAnyDataEnabled() && !isEmergency()) {

if (apnContext.getState() == DctConstants.State.IDLE) {
                ArrayList<ApnSetting> waitingApns = buildWaitingApns(apnContext.getApnType());
if (waitingApns.isEmpty()) {
if (DBG) log("trySetupData: No APN found");
notifyNoData(GsmDataConnection.FailCause.MISSING_UNKNOWN_APN, apnContext);
//Synthetic comment -- @@ -864,7 +866,7 @@
if (apnContext.getState() != DctConstants.State.DISCONNECTING) {
boolean disconnectAll = false;
if (PhoneConstants.APN_TYPE_DUN.equals(apnContext.getApnType())) {
                            ApnSetting dunSetting = fetchDunApn();
if (dunSetting != null &&
dunSetting.equals(apnContext.getApnSetting())) {
if (DBG) log("tearing down dedicated DUN connection");
//Synthetic comment -- @@ -951,8 +953,8 @@
return result;
}

    private ArrayList<ApnSetting> createApnList(Cursor cursor) {
        ArrayList<ApnSetting> result = new ArrayList<ApnSetting>();
if (cursor.moveToFirst()) {
do {
String[] types = parseTypes(
//Synthetic comment -- @@ -983,7 +985,8 @@
cursor.getInt(cursor.getColumnIndexOrThrow(
Telephony.Carriers.CARRIER_ENABLED)) == 1,
cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.BEARER)));
                result.add(apn);
} while (cursor.moveToNext());
}
if (DBG) log("createApnList: X result=" + result);
//Synthetic comment -- @@ -1026,7 +1029,7 @@
return null;
}

    protected GsmDataConnection findReadyDataConnection(ApnSetting apn) {
if (apn == null) {
return null;
}
//Synthetic comment -- @@ -1035,7 +1038,7 @@
" dcacs.size=" + mDataConnectionAsyncChannels.size());
}
for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
            ApnSetting apnSetting = dcac.getApnSettingSync();
if (DBG) {
log("findReadyDataConnection: dc apn string <" +
(apnSetting != null ? (apnSetting.toString()) : "null") + ">");
//Synthetic comment -- @@ -1055,11 +1058,11 @@

private boolean setupData(ApnContext apnContext) {
if (DBG) log("setupData: apnContext=" + apnContext);
        ApnSetting apn;
GsmDataConnection dc;

int profileId = getApnProfileID(apnContext.getApnType());
        apn = apnContext.getNextWaitingApn();
if (apn == null) {
if (DBG) log("setupData: return for no apn found!");
return false;
//Synthetic comment -- @@ -1390,6 +1393,19 @@
mActiveApn = null;
}

@Override
protected void restartRadio() {
if (DBG) log("restartRadio: ************TURN OFF RADIO**************");
//Synthetic comment -- @@ -1608,7 +1624,7 @@
private DataConnection checkForConnectionForApnContext(ApnContext apnContext) {
// Loop through all apnContexts looking for one with a conn that satisfies this apnType
String apnType = apnContext.getApnType();
        ApnSetting dunSetting = null;

if (PhoneConstants.APN_TYPE_DUN.equals(apnType)) {
dunSetting = fetchDunApn();
//Synthetic comment -- @@ -1618,7 +1634,7 @@
for (ApnContext c : mApnContexts.values()) {
DataConnection conn = c.getDataConnection();
if (conn != null) {
                ApnSetting apnSetting = c.getApnSetting();
if (dunSetting != null) {
if (dunSetting.equals(apnSetting)) {
switch (c.getState()) {
//Synthetic comment -- @@ -1793,7 +1809,7 @@
handleError = true;
} else {
DataConnection dc = apnContext.getDataConnection();
                ApnSetting apn = apnContext.getApnSetting();
if (DBG) {
log("onDataSetupComplete: success apn=" + (apn == null ? "unknown" : apn.apn));
}
//Synthetic comment -- @@ -1828,7 +1844,7 @@
} else {
cause = (DataConnection.FailCause) (ar.result);
if (DBG) {
                ApnSetting apn = apnContext.getApnSetting();
log(String.format("onDataSetupComplete: error apn=%s cause=%s",
(apn == null ? "unknown" : apn.apn), cause));
}
//Synthetic comment -- @@ -2020,7 +2036,7 @@
* Data Connections and setup the preferredApn.
*/
private void createAllApnList() {
        mAllApns = new ArrayList<ApnSetting>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
//Synthetic comment -- @@ -2126,11 +2142,11 @@
* @return waitingApns list to be used to create PDP
*          error when waitingApns.isEmpty()
*/
    private ArrayList<ApnSetting> buildWaitingApns(String requestedApnType) {
        ArrayList<ApnSetting> apnList = new ArrayList<ApnSetting>();

if (requestedApnType.equals(PhoneConstants.APN_TYPE_DUN)) {
            ApnSetting dun = fetchDunApn();
if (dun != null) {
apnList.add(dun);
if (DBG) log("buildWaitingApns: X added APN_TYPE_DUN apnList=" + apnList);
//Synthetic comment -- @@ -2178,7 +2194,7 @@
}
}
if (mAllApns != null) {
            for (ApnSetting apn : mAllApns) {
if (apn.canHandleType(requestedApnType)) {
if (apn.bearer == 0 || apn.bearer == radioTech) {
if (DBG) log("apn info : " +apn.toString());
//Synthetic comment -- @@ -2193,7 +2209,7 @@
return apnList;
}

    private String apnListToString (ArrayList<ApnSetting> apns) {
StringBuilder result = new StringBuilder();
for (int i = 0, size = apns.size(); i < size; i++) {
result.append('[')
//Synthetic comment -- @@ -2227,7 +2243,7 @@
}
}

    private ApnSetting getPreferredApn() {
if (mAllApns.isEmpty()) {
log("getPreferredApn: X not found mAllApns.isEmpty");
return null;
//Synthetic comment -- @@ -2247,7 +2263,7 @@
int pos;
cursor.moveToFirst();
pos = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers._ID));
            for(ApnSetting p:mAllApns) {
if (p.id == pos && p.canHandleType(mRequestedApnType)) {
log("getPreferredApn: X found apnSetting" + p);
cursor.close();







