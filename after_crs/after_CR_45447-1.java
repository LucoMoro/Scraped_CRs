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
//Synthetic comment -- index 4817a7b..748c12f 100644

//Synthetic comment -- @@ -37,12 +37,12 @@

private DctConstants.State mState;

    private ArrayList<DataProfile> mWaitingApns = null;

/** A zero indicates that all waiting APNs had a permanent error */
private AtomicInteger mWaitingApnsPermanentFailureCountDown;

    private DataProfile mDataProfile;

DataConnection mDataConnection;

//Synthetic comment -- @@ -107,15 +107,15 @@
mDataConnectionAc = dcac;
}

    public synchronized DataProfile getApnSetting() {
        return mDataProfile;
}

    public synchronized void setApnSetting(DataProfile apnSetting) {
        mDataProfile = apnSetting;
}

    public synchronized void setWaitingApns(ArrayList<DataProfile> waitingApns) {
mWaitingApns = waitingApns;
mWaitingApnsPermanentFailureCountDown.set(mWaitingApns.size());
}
//Synthetic comment -- @@ -128,9 +128,9 @@
mWaitingApnsPermanentFailureCountDown.decrementAndGet();
}

    public synchronized DataProfile getNextWaitingApn() {
        ArrayList<DataProfile> list = mWaitingApns;
        DataProfile apn = null;

if (list != null) {
if (!list.isEmpty()) {
//Synthetic comment -- @@ -140,13 +140,13 @@
return apn;
}

    public synchronized void removeWaitingApn(DataProfile apn) {
if (mWaitingApns != null) {
mWaitingApns.remove(apn);
}
}

    public synchronized ArrayList<DataProfile> getWaitingApns() {
return mWaitingApns;
}

//Synthetic comment -- @@ -231,7 +231,7 @@
// We don't print mDataConnection because its recursive.
return "{mApnType=" + mApnType + " mState=" + getState() + " mWaitingApns=" + mWaitingApns +
" mWaitingApnsPermanentFailureCountDown=" + mWaitingApnsPermanentFailureCountDown +
                " mDataProfile =" + mDataProfile  + " mDataConnectionAc=" + mDataConnectionAc +
" mReason=" + mReason + " mRetryCount=" + mRetryCount +
" mDataEnabled=" + mDataEnabled + " mDependencyMet=" + mDependencyMet + "}";
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnSetting.java b/src/java/com/android/internal/telephony/ApnSetting.java
//Synthetic comment -- index b84c69c..d465789 100755

//Synthetic comment -- @@ -16,63 +16,42 @@

package com.android.internal.telephony;

import com.android.internal.telephony.DataProfile;

/**
* This class represents a apn setting for create PDP link
*/
public class ApnSetting extends DataProfile {

static final String V2_FORMAT_REGEX = "^\\[ApnSettingV2\\]\\s*";

public final String carrier;
public final String proxy;
public final String port;
public final String mmsc;
public final String mmsProxy;
public final String mmsPort;
/**
* Current status of APN
* true : enabled APN, false : disabled APN.
*/
public final boolean carrierEnabled;

public ApnSetting(int id, String numeric, String carrier, String apn,
String proxy, String port,
String mmsc, String mmsProxy, String mmsPort,
String user, String password, int authType, String[] types,
String protocol, String roamingProtocol, boolean carrierEnabled, int bearer) {
        super(id, numeric, apn, user, password, authType,
                types, protocol, roamingProtocol, bearer);

this.carrier = carrier;
this.proxy = proxy;
this.port = port;
this.mmsc = mmsc;
this.mmsProxy = mmsProxy;
this.mmsPort = mmsPort;
this.carrierEnabled = carrierEnabled;
}

/**
//Synthetic comment -- @@ -178,6 +157,17 @@
return sb.toString();
}

    @Override
    public DataProfileType getDataProfileType() {
        return DataProfileType.PROFILE_TYPE_APN;
    }

    @Override
    public int getProfileId() {
        return id;
    }

    @Override
public boolean canHandleType(String type) {
for (String t : types) {
// DEFAULT handles all, and HIPRI is handled by DEFAULT
//Synthetic comment -- @@ -197,4 +187,14 @@
if (o instanceof ApnSetting == false) return false;
return (this.toString().equals(o.toString()));
}

    @Override
    public String toShortString() {
        return "ApnSetting";
    }

    @Override
    public String toHash() {
        return this.toString();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..6c2177f 100644

//Synthetic comment -- @@ -18,6 +18,7 @@


import com.android.internal.telephony.DataCallState.SetupResult;
import com.android.internal.telephony.DataProfile;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;
import com.android.internal.util.State;
//Synthetic comment -- @@ -78,13 +79,13 @@
* Used internally for saving connecting parameters.
*/
protected static class ConnectionParams {
        public ConnectionParams(DataProfile apn, Message onCompletedMsg) {
this.apn = apn;
this.onCompletedMsg = onCompletedMsg;
}

public int tag;
        public DataProfile apn;
public Message onCompletedMsg;
}

//Synthetic comment -- @@ -235,7 +236,7 @@
protected static final int EVENT_LOG_BAD_DNS_ADDRESS = 50100;

//***** Member Variables
    protected DataProfile mApn;
protected int mTag;
protected PhoneBase phone;
protected int mRilVersion = -1;
//Synthetic comment -- @@ -1210,7 +1211,7 @@
*        AsyncResult.result = FailCause and AsyncResult.exception = Exception().
* @param apn is the Access Point Name to bring up a connection to
*/
    public void bringUp(Message onCompletedMsg, DataProfile apn) {
sendMessage(obtainMessage(EVENT_CONNECT, new ConnectionParams(apn, onCompletedMsg)));
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionAc.java b/src/java/com/android/internal/telephony/DataConnectionAc.java
//Synthetic comment -- index a24414f..c5ddea6 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.internal.telephony;

import com.android.internal.telephony.DataConnection.UpdateLinkPropertyResult;
import com.android.internal.telephony.DataProfile;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;

//Synthetic comment -- @@ -254,7 +255,7 @@
}

/**
     * Request the connections DataProfile.
* Response {@link #rspApnSetting}
*/
public void reqApnSetting() {
//Synthetic comment -- @@ -263,23 +264,23 @@
}

/**
     * Evaluate a RSP_APN_SETTING message and return the DataProfile.
*
* @param response Message
     * @return DataProfile, maybe null
*/
    public DataProfile rspApnSetting(Message response) {
        DataProfile retVal = (DataProfile) response.obj;
if (DBG) log("rspApnSetting=" + retVal);
return retVal;
}

/**
     * Get the connections DataProfile.
*
     * @return DataProfile or null if an error
*/
    public DataProfile getApnSettingSync() {
Message response = sendMessageSynchronously(REQ_GET_APNSETTING);
if ((response != null) && (response.what == RSP_GET_APNSETTING)) {
return rspApnSetting(response);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 43058b6..80208d3 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.AsyncChannel;
import com.android.internal.telephony.DataProfile;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -235,13 +236,13 @@
new ConcurrentHashMap<String, ApnContext>();

/* Currently active APN */
    protected DataProfile mActiveApn;

/** allApns holds all apns */
    protected ArrayList<DataProfile> mAllApns = null;

/** preferred apn */
    protected DataProfile mPreferredApn = null;

/** Is packet service restricted by network */
protected boolean mIsPsRestricted = false;
//Synthetic comment -- @@ -490,15 +491,15 @@
public boolean isApnTypeActive(String type) {
// TODO: support simultaneous with List instead
if (PhoneConstants.APN_TYPE_DUN.equals(type)) {
            DataProfile dunApn = fetchDunApn();
if (dunApn != null) {
                return ((mActiveApn != null) && (dunApn.toHash().equals(mActiveApn.toHash())));
}
}
return mActiveApn != null && mActiveApn.canHandleType(type);
}

    protected DataProfile fetchDunApn() {
if (SystemProperties.getBoolean("net.tethering.noprovisioning", false)) {
log("fetchDunApn: net.tethering.noprovisioning=true ret: null");
return null;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataProfile.java b/src/java/com/android/internal/telephony/DataProfile.java
new file mode 100644
//Synthetic comment -- index 0000000..ac96d04

//Synthetic comment -- @@ -0,0 +1,108 @@
/*
 * Copyright (c) 2011, Code Aurora Forum. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

public abstract class DataProfile {

    protected final static String LOG_TAG = "DataProfile";

    public final int id;
    // TODO: Keeping this public will mean 'apn' will be accessed in CdmaDCT etc.
    public final String apn;
    public final String user;
    public final String password;
    public final int    authType;
    public final String protocol;
    public final String roamingProtocol;
    public final String numeric;
    /**
     * Radio Access Technology info
     * To check what values can hold, refer to ServiceState.java.
     * This should be spread to other technologies,
     * but currently only used for LTE(14) and EHRPD(13).
     */
    public final int bearer;

    public final String[] types;

    public enum DataProfileType {
        PROFILE_TYPE_APN(0),
        PROFILE_TYPE_CDMA(1),
        PROFILE_TYPE_OMH(2);

        int id;

        private DataProfileType(int i) {
            this.id = i;
        }

        public int getid() {
            return id;
        }
    }

    private DataConnection mDc = null;

    public DataProfile (int id, String numeric, String apn, String user, String password,
            int authType, String[] types, String protocol, String roamingProtocol, int bearer) {
        this.id = id;
        this.numeric = numeric;
        this.apn = apn;
        this.types = types;
        this.user = user;
        this.password = password;
        this.authType = authType;
        this.protocol = protocol;
        this.roamingProtocol = roamingProtocol;
        this.bearer = bearer;
    }

    /* package */ boolean isActive() {
      return mDc != null;
    }

    /* package */void setAsActive(DataConnection dc) {
        mDc = dc;
    }

    /* package */void setAsInactive() {
        mDc = null;
    }

    public int getAuthType() {
        return authType;
    }

    public String[] getServiceTypes() {
        return types;
    }

    public String toString() {
        return "[dpt=" + getDataProfileType() + ", active=" + isActive() + ", ";
    }

    /* some way to identify this data profile uniquely */
    public abstract String toHash();

    public abstract String toShortString();

    public abstract boolean canHandleType(String type);

    public abstract DataProfileType getDataProfileType();

    public abstract int getProfileId();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8761828..e7d55a2 100644

//Synthetic comment -- @@ -21,6 +21,7 @@

import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
//Synthetic comment -- @@ -74,6 +75,8 @@
createTime = -1;
lastFailTime = -1;
lastFailCause = FailCause.NONE;

        // TODO: The data profile's profile ID must be set when it is created.
int dataProfile;
if ((cp.apn != null) && (cp.apn.types.length > 0) && (cp.apn.types[0] != null) &&
(cp.apn.types[0].equals(PhoneConstants.APN_TYPE_DUN))) {
//Synthetic comment -- @@ -83,14 +86,16 @@
dataProfile = RILConstants.DATA_PROFILE_DEFAULT;
}

        ((DataProfileCdma)mApn).setProfileId(dataProfile);

// msg.obj will be returned in AsyncResult.userObj;
Message msg = obtainMessage(EVENT_SETUP_DATA_CONNECTION_DONE, cp);
msg.obj = cp;
phone.mCM.setupDataCall(
Integer.toString(getRilRadioTechnology(RILConstants.SETUP_DATA_TECH_CDMA)),
                Integer.toString(((DataProfileCdma)mApn).getProfileId()),
                null, mApn.user, mApn.password,
                Integer.toString(mApn.getAuthType()),
RILConstants.SETUP_DATA_PROTOCOL_IP, msg);
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index c0dbf9f..2a5a130 100644

//Synthetic comment -- @@ -31,7 +31,8 @@
import android.util.EventLog;
import android.util.Log;

import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.cdma.DataProfileCdma;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
//Synthetic comment -- @@ -357,7 +358,7 @@
private CdmaDataConnection findFreeDataConnection() {
for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
if (dcac.isInactiveSync()) {
                log("found free CdmaDataConnection");
return (CdmaDataConnection) dcac.dataConnection;
}
}
//Synthetic comment -- @@ -384,8 +385,14 @@
types = mDefaultApnTypes;
apnId = mDefaultApnId;
}

        String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
        String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");

        // TODO: home operator numeric! 
        mActiveApn = (DataProfile)new DataProfileCdma(apnId, null, null, null, null,
                RILConstants.SETUP_DATA_AUTH_PAP_CHAP, types, ipProto, roamingIpProto,
                mPhone.getServiceState().getRadioTechnology());
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

    protected DataProfile fetchDunApn() {
        // TODO: TBD
        return null;
    }

@Override
public boolean isDisconnected() {
return ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.FAILED));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java b/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java
new file mode 100644
//Synthetic comment -- index 0000000..f0f4970

//Synthetic comment -- @@ -0,0 +1,63 @@
/*
 * Copyright (c) 2011, Code Aurora Forum. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony.cdma;

import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.DataProfile.DataProfileType;

import java.util.Arrays;

public class DataProfileCdma extends DataProfile {

    private static String PROFILE_TYPE = "CdmaNai";

    private int mProfileId = 0;

    public DataProfileCdma(int id, String numeric, String name, String user, String password,
            int authType, String[] types, String protocol, String roamingProtocol, int bearer) {
        super(id, numeric, name == null ? PROFILE_TYPE : name, user, password,
                authType, types, protocol, roamingProtocol, bearer);
    }

    @Override
    public boolean canHandleType(String type) {
        return Arrays.asList(types).contains(type);
    }

    @Override
    public DataProfileType getDataProfileType() {
        return DataProfileType.PROFILE_TYPE_CDMA;
    }

    public int getProfileId() {
        return mProfileId;
    }

    public void setProfileId(int profileId) {
        mProfileId = profileId;
    }

    @Override
    public String toShortString() {
        return "DataProfileCdma";
    }

    @Override
    public String toHash() {
        return this.toString();
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnection.java
//Synthetic comment -- index 156574d..46521b9 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.ApnSetting;

import java.io.FileDescriptor;
import java.io.PrintWriter;
//Synthetic comment -- @@ -75,9 +76,9 @@
void onConnect(ConnectionParams cp) {
mApn = cp.apn;

        if (DBG) log("Connecting to carrier: '" + ((ApnSetting)mApn).carrier
+ "' APN: '" + mApn.apn
                + "' proxy: '" + ((ApnSetting)mApn).proxy + "' port: '" + ((ApnSetting)mApn).port);

createTime = -1;
lastFailTime = -1;
//Synthetic comment -- @@ -134,11 +135,11 @@
// if Proxy is an IP-address.
// Otherwise, the default APN will not be restored anymore.
if (!mApn.types[0].equals(PhoneConstants.APN_TYPE_MMS)
                || !isIpAddress(((ApnSetting)mApn).mmsProxy)) {
log(String.format(
"isDnsOk: return false apn.types[0]=%s APN_TYPE_MMS=%s isIpAddress(%s)=%s",
                        mApn.types[0], PhoneConstants.APN_TYPE_MMS, ((ApnSetting)mApn).mmsProxy,
                        isIpAddress(((ApnSetting)mApn).mmsProxy)));
return false;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ebb41fc..4a6b39f 100644

//Synthetic comment -- @@ -50,8 +50,10 @@
import android.util.EventLog;
import android.util.Log;

import com.android.internal.R;
import com.android.internal.telephony.ApnContext;
import com.android.internal.telephony.ApnSetting;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnection.FailCause;
//Synthetic comment -- @@ -375,7 +377,7 @@
if (DBG) log( "get active apn string for type:" + apnType);
ApnContext apnContext = mApnContexts.get(apnType);
if (apnContext != null) {
            ApnSetting apnSetting = (ApnSetting)apnContext.getApnSetting();
if (apnSetting != null) {
return apnSetting.apn;
}
//Synthetic comment -- @@ -533,7 +535,7 @@
}

if (mAllApns != null) {
            for (DataProfile apn : mAllApns) {
if (apn.canHandleType(type)) {
return true;
}
//Synthetic comment -- @@ -732,7 +734,7 @@
isDataAllowed(apnContext) && getAnyDataEnabled() && !isEmergency()) {

if (apnContext.getState() == DctConstants.State.IDLE) {
                ArrayList<DataProfile> waitingApns = buildWaitingApns(apnContext.getApnType());
if (waitingApns.isEmpty()) {
if (DBG) log("trySetupData: No APN found");
notifyNoData(GsmDataConnection.FailCause.MISSING_UNKNOWN_APN, apnContext);
//Synthetic comment -- @@ -849,7 +851,7 @@
if (apnContext.getState() != DctConstants.State.DISCONNECTING) {
boolean disconnectAll = false;
if (PhoneConstants.APN_TYPE_DUN.equals(apnContext.getApnType())) {
                            DataProfile dunSetting = fetchDunApn();
if (dunSetting != null &&
dunSetting.equals(apnContext.getApnSetting())) {
if (DBG) log("tearing down dedicated DUN connection");
//Synthetic comment -- @@ -936,8 +938,8 @@
return result;
}

    private ArrayList<DataProfile> createApnList(Cursor cursor) {
        ArrayList<DataProfile> result = new ArrayList<DataProfile>();
if (cursor.moveToFirst()) {
do {
String[] types = parseTypes(
//Synthetic comment -- @@ -968,7 +970,8 @@
cursor.getInt(cursor.getColumnIndexOrThrow(
Telephony.Carriers.CARRIER_ENABLED)) == 1,
cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.BEARER)));
                 result.add((DataProfile)apn);

} while (cursor.moveToNext());
}
if (DBG) log("createApnList: X result=" + result);
//Synthetic comment -- @@ -1011,7 +1014,7 @@
return null;
}

    protected GsmDataConnection findReadyDataConnection(DataProfile apn) {
if (apn == null) {
return null;
}
//Synthetic comment -- @@ -1020,7 +1023,7 @@
" dcacs.size=" + mDataConnectionAsyncChannels.size());
}
for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
            ApnSetting apnSetting = (ApnSetting)dcac.getApnSettingSync();
if (DBG) {
log("findReadyDataConnection: dc apn string <" +
(apnSetting != null ? (apnSetting.toString()) : "null") + ">");
//Synthetic comment -- @@ -1040,11 +1043,11 @@

private boolean setupData(ApnContext apnContext) {
if (DBG) log("setupData: apnContext=" + apnContext);
        DataProfile apn;
GsmDataConnection dc;

int profileId = getApnProfileID(apnContext.getApnType());
        apn = (ApnSetting)apnContext.getNextWaitingApn();
if (apn == null) {
if (DBG) log("setupData: return for no apn found!");
return false;
//Synthetic comment -- @@ -1375,6 +1378,19 @@
mActiveApn = null;
}

   
    @Override
    protected DataProfile fetchDunApn() {
        Context c = mPhone.getContext();
        String apnData = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.TETHER_DUN_APN);
        ApnSetting dunSetting = ApnSetting.fromString(apnData);
        if (dunSetting != null) return dunSetting;

        apnData = c.getResources().getString(R.string.config_tether_apndata);
        return (DataProfile)ApnSetting.fromString(apnData);
    }

@Override
protected void restartRadio() {
if (DBG) log("restartRadio: ************TURN OFF RADIO**************");
//Synthetic comment -- @@ -1593,7 +1609,7 @@
private DataConnection checkForConnectionForApnContext(ApnContext apnContext) {
// Loop through all apnContexts looking for one with a conn that satisfies this apnType
String apnType = apnContext.getApnType();
        DataProfile dunSetting = null;

if (PhoneConstants.APN_TYPE_DUN.equals(apnType)) {
dunSetting = fetchDunApn();
//Synthetic comment -- @@ -1603,7 +1619,7 @@
for (ApnContext c : mApnContexts.values()) {
DataConnection conn = c.getDataConnection();
if (conn != null) {
                DataProfile apnSetting = c.getApnSetting();
if (dunSetting != null) {
if (dunSetting.equals(apnSetting)) {
switch (c.getState()) {
//Synthetic comment -- @@ -1778,7 +1794,7 @@
handleError = true;
} else {
DataConnection dc = apnContext.getDataConnection();
                ApnSetting apn = (ApnSetting)apnContext.getApnSetting();
if (DBG) {
log("onDataSetupComplete: success apn=" + (apn == null ? "unknown" : apn.apn));
}
//Synthetic comment -- @@ -1813,7 +1829,7 @@
} else {
cause = (DataConnection.FailCause) (ar.result);
if (DBG) {
                DataProfile apn = apnContext.getApnSetting();
log(String.format("onDataSetupComplete: error apn=%s cause=%s",
(apn == null ? "unknown" : apn.apn), cause));
}
//Synthetic comment -- @@ -2005,7 +2021,7 @@
* Data Connections and setup the preferredApn.
*/
private void createAllApnList() {
        mAllApns = new ArrayList<DataProfile>();
IccRecords r = mIccRecords.get();
String operator = (r != null) ? r.getOperatorNumeric() : "";
if (operator != null) {
//Synthetic comment -- @@ -2111,11 +2127,11 @@
* @return waitingApns list to be used to create PDP
*          error when waitingApns.isEmpty()
*/
    private ArrayList<DataProfile> buildWaitingApns(String requestedApnType) {
        ArrayList<DataProfile> apnList = new ArrayList<DataProfile>();

if (requestedApnType.equals(PhoneConstants.APN_TYPE_DUN)) {
            DataProfile dun = fetchDunApn();
if (dun != null) {
apnList.add(dun);
if (DBG) log("buildWaitingApns: X added APN_TYPE_DUN apnList=" + apnList);
//Synthetic comment -- @@ -2150,7 +2166,7 @@
}
}
if (mAllApns != null) {
            for (DataProfile apn : mAllApns) {
if (apn.canHandleType(requestedApnType)) {
if (apn.bearer == 0 || apn.bearer == radioTech) {
if (DBG) log("apn info : " +apn.toString());
//Synthetic comment -- @@ -2165,7 +2181,7 @@
return apnList;
}

    private String apnListToString (ArrayList<DataProfile> apns) {
StringBuilder result = new StringBuilder();
for (int i = 0, size = apns.size(); i < size; i++) {
result.append('[')
//Synthetic comment -- @@ -2199,7 +2215,7 @@
}
}

    private DataProfile getPreferredApn() {
if (mAllApns.isEmpty()) {
log("getPreferredApn: X not found mAllApns.isEmpty");
return null;
//Synthetic comment -- @@ -2219,7 +2235,7 @@
int pos;
cursor.moveToFirst();
pos = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers._ID));
            for(DataProfile p:mAllApns) {
if (p.id == pos && p.canHandleType(mRequestedApnType)) {
log("getPreferredApn: X found apnSetting" + p);
cursor.close();







