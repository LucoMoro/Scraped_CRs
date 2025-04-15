/*Enable OMH support in Android Telephony

Support the Open Mobile Handset initative in android telephony. As per
the OMH specification in 3GPP2, Data profiles defined for specific
services like LBS, TETHERED and UNSPECIFIED are defined in the
RUIM/CSIM cards. These profiles are read from the android telephony
and used for creating the data calls when the right radio technology
(CDMA/DO) is used and when the feature is enabled.

* Supports multiple profiles for a particular OMH service type

* Supports multiple OMH profiles with same priority

* In case of OMH, for all profiles supporting SUPL, arbitration
should choose the lowest priority profile when making data calls

* This feature supports the co-existance of other radio technologies in
an OMH enabled device. Even with the OMH feature enabled, the device
can operate with default profiles when OMH profiles are not (yet)
present.

* Use PROFILE_TYPE_CDMA if no suitable PROFILE_TYPE_OMH available

* Fix phone crash:
Default code triggers setupDataCall even for cdma detachet event.
In a handoff scenario cdma detach event is sent before the objects
are initialized (profiles being read) and CdmaDCT attempts a call
with null DataProfile object.

* Add null checks in CdmaDataConnection

Change-Id:I7f58c9a4ba434b1fde01fa4a0abfaf0ce4cacbea*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ApnSetting.java b/src/java/com/android/internal/telephony/ApnSetting.java
//Synthetic comment -- index d465789..afb1c63 100755

//Synthetic comment -- @@ -197,4 +197,9 @@
public String toHash() {
return this.toString();
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d9c3dc7..4077879 100644

//Synthetic comment -- @@ -1564,6 +1564,18 @@
public int getLteOnCdmaMode();

/**
* Request the ISIM application on the UICC to perform the AKA
* challenge/response algorithm for IMS authentication. The nonce string
* and challenge response are Base64 encoded Strings.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataProfile.java b/src/java/com/android/internal/telephony/DataProfile.java
//Synthetic comment -- index ac96d04..d666ee6 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public final int bearer;

    public final String[] types;

public enum DataProfileType {
PROFILE_TYPE_APN(0),
//Synthetic comment -- @@ -105,4 +105,5 @@
public abstract DataProfileType getDataProfileType();

public abstract int getProfileId();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af81e04..cee938d 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
//Synthetic comment -- @@ -689,6 +690,22 @@
}

@Override public void
supplyIccPin(String pin, Message result) {
supplyIccPinForApp(pin, null, result);
}
//Synthetic comment -- @@ -2331,6 +2348,7 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret =  responseInts(p); break;
case RIL_REQUEST_ISIM_AUTHENTICATION: ret =  responseString(p); break;
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: ret = responseVoid(p); break;
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: ret = responseICC_IO(p); break;
//Synthetic comment -- @@ -3444,6 +3462,27 @@
return response;
}

private void
notifyRegistrantsCdmaInfoRec(CdmaInformationRecords infoRec) {
int response = RIL_UNSOL_CDMA_INFO_REC;
//Synthetic comment -- @@ -3604,6 +3643,7 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
case RIL_REQUEST_ISIM_AUTHENTICATION: return "RIL_REQUEST_ISIM_AUTHENTICATION";
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: return "RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: return "RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8ef1ac4..1d8afad 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
//Synthetic comment -- @@ -78,7 +79,8 @@

// TODO: The data profile's profile ID must be set when it is created.
int dataProfile;
        if ((cp.apn != null) && (cp.apn.types.length > 0) && (cp.apn.types[0] != null) &&
(cp.apn.types[0].equals(PhoneConstants.APN_TYPE_DUN))) {
if (DBG) log("CdmaDataConnection using DUN");
dataProfile = RILConstants.DATA_PROFILE_TETHERED;
//Synthetic comment -- @@ -86,14 +88,18 @@
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 8740d97..ee0b677 100644

//Synthetic comment -- @@ -31,19 +31,19 @@
import android.util.EventLog;
import android.telephony.Rlog;

import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.cdma.DataProfileCdma;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
//Synthetic comment -- @@ -70,6 +70,8 @@
private static final int TIME_DELAYED_TO_RESTART_RADIO =
SystemProperties.getInt("ro.cdma.timetoradiorestart", 60000);

/**
* Pool size of CdmaDataConnection objects.
*/
//Synthetic comment -- @@ -81,22 +83,6 @@
private static final String INTENT_DATA_STALL_ALARM =
"com.android.internal.telephony.cdma-data-stall";

    private static final String[] mSupportedApnTypes = {
            PhoneConstants.APN_TYPE_DEFAULT,
            PhoneConstants.APN_TYPE_MMS,
            PhoneConstants.APN_TYPE_DUN,
            PhoneConstants.APN_TYPE_HIPRI };

    private static final String[] mDefaultApnTypes = {
            PhoneConstants.APN_TYPE_DEFAULT,
            PhoneConstants.APN_TYPE_MMS,
            PhoneConstants.APN_TYPE_HIPRI };

    private String[] mDunApnTypes = {
            PhoneConstants.APN_TYPE_DUN };

    private static final int mDefaultApnId = DctConstants.APN_DEFAULT_ID;

/* Constructor */

CdmaDataConnectionTracker(CDMAPhone p) {
//Synthetic comment -- @@ -118,23 +104,12 @@

mDataConnectionTracker = this;

createAllDataConnectionList();
broadcastMessenger();

        Context c = mCdmaPhone.getContext();
        String[] t = c.getResources().getStringArray(
                com.android.internal.R.array.config_cdma_dun_supported_types);
        if (t != null && t.length > 0) {
            ArrayList<String> temp = new ArrayList<String>();
            for(int i=0; i< t.length; i++) {
                if (!PhoneConstants.APN_TYPE_DUN.equalsIgnoreCase(t[i])) {
                    temp.add(t[i]);
                }
            }
            temp.add(0,PhoneConstants.APN_TYPE_DUN);
            mDunApnTypes = temp.toArray(t);
        }

}

@Override
//Synthetic comment -- @@ -157,6 +132,7 @@
mCdmaPhone.mSST.unregisterForRoamingOff(this);
mCdmaSSM.dispose(this);
mPhone.mCM.unregisterForCdmaOtaProvision(this);

destroyAllDataConnectionList();
}
//Synthetic comment -- @@ -189,6 +165,11 @@
}
}

@Override
public synchronized DctConstants.State getState(String apnType) {
return mState;
//Synthetic comment -- @@ -201,12 +182,7 @@

@Override
protected boolean isApnTypeAvailable(String type) {
        for (String s : mSupportedApnTypes) {
            if (TextUtils.equals(type, s)) {
                return true;
            }
        }
        return false;
}

@Override
//Synthetic comment -- @@ -376,22 +352,17 @@

/** TODO: We probably want the connection being setup to a parameter passed around */
mPendingDataConnection = conn;
        String[] types;
        int apnId;
        if (mRequestedApnType.equals(PhoneConstants.APN_TYPE_DUN)) {
            types = mDunApnTypes;
            apnId = DctConstants.APN_DUN_ID;
        } else {
            types = mDefaultApnTypes;
            apnId = mDefaultApnId;
        }

String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");

        mActiveApn = (DataProfile)new DataProfileCdma(apnId, null, null, null, null,
                RILConstants.SETUP_DATA_AUTH_PAP_CHAP, types, ipProto, roamingIpProto,
                mPhone.getServiceState().getRadioTechnology());
if (DBG) log("call conn.bringUp mActiveApn=" + mActiveApn);

Message msg = obtainMessage();
//Synthetic comment -- @@ -497,14 +468,22 @@
setState(DctConstants.State.IDLE);
notifyDataConnection(reason);
mActiveApn = null;
}

protected void onRecordsLoaded() {
if (mState == DctConstants.State.FAILED) {
cleanUpAllConnections(null);
}

        sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA, Phone.REASON_SIM_LOADED));
}

protected void onNVReady() {
//Synthetic comment -- @@ -664,6 +643,7 @@

notifyDataConnection(reason);
mActiveApn = null;
if (retryAfterDisconnected(reason)) {
// Wait a bit before trying, so we're not tying up RIL command channel.
startAlarmForReconnect(APN_DELAY_MILLIS, reason);
//Synthetic comment -- @@ -805,6 +785,15 @@
}
}

private void writeEventLogCdmaDataDrop() {
CdmaCellLocation loc = (CdmaCellLocation)(mPhone.getCellLocation());
EventLog.writeEvent(EventLogTags.CDMA_DATA_DROP,
//Synthetic comment -- @@ -915,6 +904,10 @@
onRestartRadio();
break;

default:
// handle the message in the super class DataConnectionTracker
super.handleMessage(msg);
//Synthetic comment -- @@ -984,9 +977,5 @@
pw.println(" mCdmaSSM=" + mCdmaSSM);
pw.println(" mPendingDataConnection=" + mPendingDataConnection);
pw.println(" mPendingRestartRadio=" + mPendingRestartRadio);
        pw.println(" mSupportedApnTypes=" + mSupportedApnTypes);
        pw.println(" mDefaultApnTypes=" + mDefaultApnTypes);
        pw.println(" mDunApnTypes=" + mDunApnTypes);
        pw.println(" mDefaultApnId=" + mDefaultApnId);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataProfileTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataProfileTracker.java
new file mode 100644
//Synthetic comment -- index 0000000..799e910

//Synthetic comment -- @@ -0,0 +1,453 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java b/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java
//Synthetic comment -- index f0f4970..a62e270 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

public class DataProfileCdma extends DataProfile {

private static String PROFILE_TYPE = "CdmaNai";

private int mProfileId = 0;
//Synthetic comment -- @@ -43,6 +44,7 @@
return DataProfileType.PROFILE_TYPE_CDMA;
}

public int getProfileId() {
return mProfileId;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileOmh.java b/src/java/com/android/internal/telephony/cdma/DataProfileOmh.java
new file mode 100644
//Synthetic comment -- index 0000000..61ea894

//Synthetic comment -- @@ -0,0 +1,238 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipCommandInterface.java b/src/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index 99f4e0f..70962ab 100644

//Synthetic comment -- @@ -421,4 +421,7 @@

public void getVoiceRadioTechnology(Message result) {
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 1672044..f5d66d0 100644

//Synthetic comment -- @@ -1522,4 +1522,22 @@
public void getVoiceRadioTechnology(Message response) {
unimplemented(response);
}
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java
//Synthetic comment -- index f001977..182f5f5 100644

//Synthetic comment -- @@ -621,4 +621,8 @@
public void iccIOForApp(int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, String aid, Message response) {
}
}







