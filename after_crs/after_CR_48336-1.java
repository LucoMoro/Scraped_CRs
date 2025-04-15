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

    @Override
    public void setProfileId(int profileId) {
        // TODO Auto-generated method stub
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d9c3dc7..4077879 100644

//Synthetic comment -- @@ -1564,6 +1564,18 @@
public int getLteOnCdmaMode();

/**
     * Get the data call profile information from the modem
     *
     * @param appType
     *          Callback message containing the count and the list of {@link
     *          RIL_DataCallProfileInfo}
     *
     * @param result
     *          Callback message
     */
    public void getDataCallProfile(int appType, Message result);

    /**
* Request the ISIM application on the UICC to perform the AKA
* challenge/response algorithm for IMS authentication. The nonce string
* and challenge response are Base64 encoded Strings.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataProfile.java b/src/java/com/android/internal/telephony/DataProfile.java
//Synthetic comment -- index ac96d04..d666ee6 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public final int bearer;

    public String[] types;

public enum DataProfileType {
PROFILE_TYPE_APN(0),
//Synthetic comment -- @@ -105,4 +105,5 @@
public abstract DataProfileType getDataProfileType();

public abstract int getProfileId();
    public abstract void setProfileId(int profileId); 
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af81e04..cee938d 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.cdma.DataProfileOmh;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
//Synthetic comment -- @@ -689,6 +690,22 @@
}

@Override public void
    getDataCallProfile(int appType, Message result) {
        RILRequest rr = RILRequest.obtain(
                RILConstants.RIL_REQUEST_GET_DATA_CALL_PROFILE, result);

        // count of ints
        rr.mp.writeInt(1);
        rr.mp.writeInt(appType);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                + " : " + appType);

        send(rr);
    }

    @Override
    public void
supplyIccPin(String pin, Message result) {
supplyIccPinForApp(pin, null, result);
}
//Synthetic comment -- @@ -2331,6 +2348,7 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: ret =  responseInts(p); break;
            case RIL_REQUEST_GET_DATA_CALL_PROFILE: ret = responseGetDataCallProfile(p); break;
case RIL_REQUEST_ISIM_AUTHENTICATION: ret =  responseString(p); break;
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: ret = responseVoid(p); break;
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: ret = responseICC_IO(p); break;
//Synthetic comment -- @@ -3444,6 +3462,27 @@
return response;
}

    private ArrayList<DataProfile>
    responseGetDataCallProfile(Parcel p) {
        int nProfiles = p.readInt();
        if (RILJ_LOGD) riljLog("# data call profiles:" + nProfiles);

        ArrayList<DataProfile> response = new ArrayList<DataProfile>(nProfiles);

        int profileId = 0;
        int priority = 0;
        for (int i = 0; i < nProfiles; i++) {
            profileId = p.readInt();
            priority = p.readInt();
            DataProfileOmh profile = new DataProfileOmh(profileId, priority);
            if (RILJ_LOGD) riljLog("responseGetDataCallProfile()" +
                    profile.getProfileId() + ":" + profile.getPriority());
            response.add(profile);
        }

        return response;
    }

private void
notifyRegistrantsCdmaInfoRec(CdmaInformationRecords infoRec) {
int response = RIL_UNSOL_CDMA_INFO_REC;
//Synthetic comment -- @@ -3604,6 +3643,7 @@
case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
case RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE: return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
            case RIL_REQUEST_GET_DATA_CALL_PROFILE: return "RIL_REQUEST_GET_DATA_CALL_PROFILE";
case RIL_REQUEST_ISIM_AUTHENTICATION: return "RIL_REQUEST_ISIM_AUTHENTICATION";
case RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU: return "RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
case RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS: return "RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8ef1ac4..1d8afad 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.DataProfile.DataProfileType;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
//Synthetic comment -- @@ -78,7 +79,8 @@

// TODO: The data profile's profile ID must be set when it is created.
int dataProfile;
        if ((cp.apn != null) && (cp.apn.types != null) &&
                (cp.apn.types.length > 0) && (cp.apn.types[0] != null) &&
(cp.apn.types[0].equals(PhoneConstants.APN_TYPE_DUN))) {
if (DBG) log("CdmaDataConnection using DUN");
dataProfile = RILConstants.DATA_PROFILE_TETHERED;
//Synthetic comment -- @@ -86,14 +88,18 @@
dataProfile = RILConstants.DATA_PROFILE_DEFAULT;
}

        if (cp.apn.getDataProfileType() == DataProfileType.PROFILE_TYPE_OMH) {
            dataProfile = cp.apn.getProfileId() + RILConstants.DATA_PROFILE_OEM_BASE;
        } else {
            mApn.setProfileId(dataProfile);
        }

// msg.obj will be returned in AsyncResult.userObj;
Message msg = obtainMessage(EVENT_SETUP_DATA_CONNECTION_DONE, cp);
msg.obj = cp;
phone.mCM.setupDataCall(
Integer.toString(getRilRadioTechnology(RILConstants.SETUP_DATA_TECH_CDMA)),
                Integer.toString(dataProfile),
null, mApn.user, mApn.password,
Integer.toString(mApn.getAuthType()),
RILConstants.SETUP_DATA_PROTOCOL_IP, msg);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 8740d97..ee0b677 100644

//Synthetic comment -- @@ -31,19 +31,19 @@
import android.util.EventLog;
import android.telephony.Rlog;

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.DctConstants;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.telephony.cdma.CdmaDataProfileTracker;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
//Synthetic comment -- @@ -70,6 +70,8 @@
private static final int TIME_DELAYED_TO_RESTART_RADIO =
SystemProperties.getInt("ro.cdma.timetoradiorestart", 60000);

    private CdmaDataProfileTracker mDpt = null;

/**
* Pool size of CdmaDataConnection objects.
*/
//Synthetic comment -- @@ -81,22 +83,6 @@
private static final String INTENT_DATA_STALL_ALARM =
"com.android.internal.telephony.cdma-data-stall";

/* Constructor */

CdmaDataConnectionTracker(CDMAPhone p) {
//Synthetic comment -- @@ -118,23 +104,12 @@

mDataConnectionTracker = this;

        mDpt = new CdmaDataProfileTracker(p);
        mDpt.registerForModemProfileReady(this, DctConstants.EVENT_MODEM_DATA_PROFILE_READY, null);

createAllDataConnectionList();
broadcastMessenger();

}

@Override
//Synthetic comment -- @@ -157,6 +132,7 @@
mCdmaPhone.mSST.unregisterForRoamingOff(this);
mCdmaSSM.dispose(this);
mPhone.mCM.unregisterForCdmaOtaProvision(this);
        mDpt.unregisterForModemProfileReady(this);

destroyAllDataConnectionList();
}
//Synthetic comment -- @@ -189,6 +165,11 @@
}
}

    /* API provided for CdmaDataProfileTracker */
    public int apnTypeToId(String apnType) {
        return super.apnTypeToId(apnType);
    }

@Override
public synchronized DctConstants.State getState(String apnType) {
return mState;
//Synthetic comment -- @@ -201,12 +182,7 @@

@Override
protected boolean isApnTypeAvailable(String type) {
        return mDpt.isApnTypeAvailable(type);
}

@Override
//Synthetic comment -- @@ -376,22 +352,17 @@

/** TODO: We probably want the connection being setup to a parameter passed around */
mPendingDataConnection = conn;

String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");

        mActiveApn = mDpt.getDataProfile(mRequestedApnType);

        if (mActiveApn == null) {
            if (DBG) log("mActiveApn is null, unable to initiate data call");
            return false;
        }

if (DBG) log("call conn.bringUp mActiveApn=" + mActiveApn);

Message msg = obtainMessage();
//Synthetic comment -- @@ -497,14 +468,22 @@
setState(DctConstants.State.IDLE);
notifyDataConnection(reason);
mActiveApn = null;
        mDpt.clearActiveDataProfile();
}

protected void onRecordsLoaded() {
        log("OMH: onRecordsLoaded(): calling readDataProfilesFromModem()");
        /* query for data profiles stored in the modem */
        boolean needModemProfiles = mDpt.readDataProfilesFromModem();

if (mState == DctConstants.State.FAILED) {
cleanUpAllConnections(null);
}

        if (!needModemProfiles) {
            log("OMH: " + Phone.REASON_SIM_LOADED);
            sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA, Phone.REASON_SIM_LOADED));
        }
}

protected void onNVReady() {
//Synthetic comment -- @@ -664,6 +643,7 @@

notifyDataConnection(reason);
mActiveApn = null;
        mDpt.clearActiveDataProfile();
if (retryAfterDisconnected(reason)) {
// Wait a bit before trying, so we're not tying up RIL command channel.
startAlarmForReconnect(APN_DELAY_MILLIS, reason);
//Synthetic comment -- @@ -805,6 +785,15 @@
}
}

    private void onModemDataProfileReady() {
        if (mState == DctConstants.State.FAILED) {
            cleanUpConnection(false, null, false);
        }

        log("OMH: onModemDataProfileReady(): Setting up data call");
        sendMessage(obtainMessage(DctConstants.EVENT_TRY_SETUP_DATA));
    }

private void writeEventLogCdmaDataDrop() {
CdmaCellLocation loc = (CdmaCellLocation)(mPhone.getCellLocation());
EventLog.writeEvent(EventLogTags.CDMA_DATA_DROP,
//Synthetic comment -- @@ -915,6 +904,10 @@
onRestartRadio();
break;

            case DctConstants.EVENT_MODEM_DATA_PROFILE_READY:
                onModemDataProfileReady();
                break;

default:
// handle the message in the super class DataConnectionTracker
super.handleMessage(msg);
//Synthetic comment -- @@ -984,9 +977,5 @@
pw.println(" mCdmaSSM=" + mCdmaSSM);
pw.println(" mPendingDataConnection=" + mPendingDataConnection);
pw.println(" mPendingRestartRadio=" + mPendingRestartRadio);
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataProfileTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataProfileTracker.java
new file mode 100644
//Synthetic comment -- index 0000000..799e910

//Synthetic comment -- @@ -0,0 +1,453 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of Code Aurora Forum, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.android.internal.telephony.cdma;

import android.os.Registrant;
import android.os.RegistrantList;
import android.os.Handler;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.DataConnectionTracker;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.cdma.DataProfileCdma;
import com.android.internal.telephony.cdma.DataProfileOmh.DataProfileTypeModem;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.TelephonyProperties;

/**
 * {@hide}
 */
public final class CdmaDataProfileTracker extends Handler {
    protected final String LOG_TAG = "CDMA";

    private CDMAPhone mPhone;

    /**
     * mDataProfilesList holds all the Data profiles for cdma
     */
    private ArrayList<DataProfile> mDataProfilesList = new ArrayList<DataProfile>();

    private static final String[] mSupportedApnTypes = {
            PhoneConstants.APN_TYPE_DEFAULT,
            PhoneConstants.APN_TYPE_MMS,
            PhoneConstants.APN_TYPE_SUPL,
            PhoneConstants.APN_TYPE_DUN,
            PhoneConstants.APN_TYPE_HIPRI,
            PhoneConstants.APN_TYPE_FOTA,
            PhoneConstants.APN_TYPE_IMS,
            PhoneConstants.APN_TYPE_CBS };

    private static final String[] mDefaultApnTypes = {
            PhoneConstants.APN_TYPE_DEFAULT,
            PhoneConstants.APN_TYPE_MMS,
            PhoneConstants.APN_TYPE_SUPL,
            PhoneConstants.APN_TYPE_HIPRI,
            PhoneConstants.APN_TYPE_FOTA,
            PhoneConstants.APN_TYPE_IMS,
            PhoneConstants.APN_TYPE_CBS };

    // if we have no active DataProfile this is null
    protected DataProfile mActiveDp;

    /*
     * Context for read profiles for OMH.
     */
    private int mOmhReadProfileContext = 0;

    /*
     * Count to track if all read profiles for OMH are completed or not.
     */
    private int mOmhReadProfileCount = 0;

    private boolean mIsOmhEnabled =
                SystemProperties.getBoolean(TelephonyProperties.PROPERTY_OMH_ENABLED, false);

    // Enumerated list of DataProfile from the modem.
    ArrayList<DataProfile> mOmhDataProfilesList = new ArrayList<DataProfile>();

    // Temp. DataProfile list from the modem.
    ArrayList<DataProfile> mTempOmhDataProfilesList = new ArrayList<DataProfile>();

    // Map of the service type to its priority
    HashMap<String, Integer> mOmhServicePriorityMap;

    /* Registrant list for objects interested in modem profile related events */
    private RegistrantList mModemDataProfileRegistrants = new RegistrantList();

    private static final int EVENT_READ_MODEM_PROFILES = 0;
    private static final int EVENT_GET_DATA_CALL_PROFILE_DONE = 1;
    private static final int EVENT_LOAD_PROFILES = 2;

    /* Constructor */

    CdmaDataProfileTracker(CDMAPhone phone) {
        mPhone = phone;

        mOmhServicePriorityMap = new HashMap<String, Integer>();

        sendMessage(obtainMessage(EVENT_LOAD_PROFILES));

        log("SUPPORT_OMH: " + mIsOmhEnabled);
    }

    /**
     * Load the CDMA profiles
     */
    private void loadProfiles() {
        log("Creating default profiles...");

        String ipProto = SystemProperties.get(
                TelephonyProperties.PROPERTY_CDMA_IPPROTOCOL, "IP");
        String roamingIpProto = SystemProperties.get(
                TelephonyProperties.PROPERTY_CDMA_ROAMING_IPPROTOCOL, "IP");

        /**
         * - Create the default profiles.
         * - One for DUN and another for all the default profiles supported
         * - Set the active profile as the default one
         */
        CdmaDataConnectionTracker cdmaDct =
            (CdmaDataConnectionTracker)(mPhone.mDataConnectionTracker);

        DataProfileCdma dp;
        dp = new DataProfileCdma(
                cdmaDct.apnTypeToId(PhoneConstants.APN_TYPE_DEFAULT),
                null, null, null, null,
                RILConstants.SETUP_DATA_AUTH_PAP_CHAP, mDefaultApnTypes, ipProto, roamingIpProto,
                mPhone.getServiceState().getRadioTechnology());
        dp.setProfileId(RILConstants.DATA_PROFILE_DEFAULT);
        mDataProfilesList.add((DataProfile)dp);

        mActiveDp = dp;

        String[] types = {PhoneConstants.APN_TYPE_DUN};

        dp = new DataProfileCdma(
                cdmaDct.apnTypeToId(PhoneConstants.APN_TYPE_DUN),
                null, null, null, null,
                RILConstants.SETUP_DATA_AUTH_PAP_CHAP, types, ipProto, roamingIpProto,
                mPhone.getServiceState().getRadioTechnology());
        dp.setProfileId(RILConstants.DATA_PROFILE_TETHERED);
        mDataProfilesList.add((DataProfile)dp);
    }

    public void dispose() {
    }

    protected void finalize() {
        Log.d(LOG_TAG, "CdmaDataProfileTracker finalized");
    }

    void registerForModemProfileReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        mModemDataProfileRegistrants.add(r);
    }

    void unregisterForModemProfileReady(Handler h) {
        mModemDataProfileRegistrants.remove(h);
    }

    public void handleMessage (Message msg) {

        if (!mPhone.mIsTheCurrentActivePhone) {
            Log.d(LOG_TAG, "Ignore CDMA msgs since CDMA phone is inactive");
            return;
        }

        switch (msg.what) {
            case EVENT_LOAD_PROFILES:
                loadProfiles();
                break;
            case EVENT_READ_MODEM_PROFILES:
                onReadDataProfilesFromModem();
                break;

            case EVENT_GET_DATA_CALL_PROFILE_DONE:
                onGetDataCallProfileDone((AsyncResult) msg.obj, (int)msg.arg1);
                break;

            default:
                // handle the message in the super class DataConnectionTracker
                super.handleMessage(msg);
                break;
        }
    }

    /*
     * Trigger modem read for data profiles
     */
    public boolean readDataProfilesFromModem() {
        boolean retVal = false;
        if (mIsOmhEnabled) {
            sendMessage(obtainMessage(EVENT_READ_MODEM_PROFILES));
            retVal = true;
        } else {
            log("OMH is disabled, ignoring request!");
        }
        return retVal;
    }

    /*
     * Reads all the data profiles from the modem
     */
    private void onReadDataProfilesFromModem() {
        log("OMH: onReadDataProfilesFromModem()");
        mOmhReadProfileContext++;

        mOmhReadProfileCount = 0; // Reset the count and list(s)
        /* Clear out the modem profiles lists (main and temp) which were read/saved */
        mOmhDataProfilesList.clear();
        mTempOmhDataProfilesList.clear();
        mOmhServicePriorityMap.clear();

        // For all the service types known in modem, read the data profies
        for (DataProfileTypeModem p : DataProfileTypeModem.values()) {
            log("OMH: Reading profiles for:" + p.getid());
            mOmhReadProfileCount++;
            mPhone.mCM.getDataCallProfile(p.getid(),
                            obtainMessage(EVENT_GET_DATA_CALL_PROFILE_DONE, //what
                            mOmhReadProfileContext, //arg1
                            0 , //arg2  -- ignore
                            p));//userObj
        }

    }

    /*
     * Process the response for the RIL request GET_DATA_CALL_PROFILE.
     * Save the profile details received.
     */
    private void onGetDataCallProfileDone(AsyncResult ar, int context) {
        if (ar.exception != null) {
            log("OMH: Exception in onGetDataCallProfileDone:" + ar.exception);
            return;
        }

        if (context != mOmhReadProfileContext) {
            //we have other onReadOmhDataprofiles() on the way.
            return;
        }

        // DataProfile list from the modem for a given SERVICE_TYPE. These may
        // be from RUIM in case of OMH
        ArrayList<DataProfile> dataProfileListModem = new ArrayList<DataProfile>();
        dataProfileListModem = (ArrayList<DataProfile>)ar.result;

        DataProfileTypeModem modemProfile = (DataProfileTypeModem)ar.userObj;

        mOmhReadProfileCount--;

        if (dataProfileListModem != null && dataProfileListModem.size() > 0) {
            String serviceType;

            /* For the modem service type, get the android DataServiceType */
            serviceType = modemProfile.getDataServiceType();

            log("OMH: # profiles returned from modem:" + dataProfileListModem.size()
                    + " for " + serviceType);

            mOmhServicePriorityMap.put(serviceType,
                    omhListGetArbitratedPriority(dataProfileListModem, serviceType));

            for (DataProfile dp : dataProfileListModem) {

                /* Store the modem profile type in the data profile */
                ((DataProfileOmh)dp).setDataProfileTypeModem(modemProfile);

                /* Look through mTempOmhDataProfilesList for existing profile id's
                 * before adding it. This implies that the (similar) profile with same
                 * priority already exists.
                 */
                DataProfileOmh omhDuplicatedp = getDuplicateProfile(dp);
                if(null == omhDuplicatedp) {
                    mTempOmhDataProfilesList.add(dp);
                    ((DataProfileOmh)dp).addServiceType(DataProfileTypeModem.
                            getDataProfileTypeModem(serviceType));
                } else {
                    /*  To share the already established data connection
                     * (say between SUPL and DUN) in cases such as below:
                     *  Ex:- SUPL+DUN [profile id 201, priority 1]
                     *  'dp' instance is found at this point. Add the non-provisioned
                     *   service type to this 'dp' instance
                     */
                    log("OMH: Duplicate Profile " + omhDuplicatedp);
                    ((DataProfileOmh)omhDuplicatedp).addServiceType(DataProfileTypeModem.
                            getDataProfileTypeModem(serviceType));
                }
            }
        }

        //(Re)Load APN List
        if(mOmhReadProfileCount == 0) {
            log("OMH: Modem omh profile read complete.");
            addServiceTypeToUnSpecified();
            mDataProfilesList.addAll(mTempOmhDataProfilesList);
            mModemDataProfileRegistrants.notifyRegistrants();
        }

        return;
    }

    /*
     * returns the object 'OMH dataProfile' if a match with the same profile id
     * exists in the enumerated list of OMH profile list
     */
    private DataProfileOmh getDuplicateProfile(DataProfile dp) {
        for (DataProfile dataProfile : mTempOmhDataProfilesList) {
            if (((DataProfileOmh)dp).getProfileId() ==
                ((DataProfileOmh)dataProfile).getProfileId()){
                return (DataProfileOmh)dataProfile;
            }
        }
        return null;
    }

    public DataProfile getDataProfile(String serviceType) {
        DataProfile profile = null;

        // Go through all the profiles to find one
        for (DataProfile dp: mDataProfilesList) {
            if (dp.canHandleType(serviceType)) {
                profile = dp;
                if (mIsOmhEnabled &&
                    dp.getDataProfileType() != DataProfile.DataProfileType.PROFILE_TYPE_OMH) {
                    // OMH enabled - Keep looking for OMH profile
                    continue;
                }
                break;
            }
        }
        return profile;
    }

    /* For all the OMH service types not present in the card, add them to the
     * UNSPECIFIED/DEFAULT data profile.
     */
    private void addServiceTypeToUnSpecified() {
        for (String apntype : mSupportedApnTypes) {
            if(!mOmhServicePriorityMap.containsKey(apntype)) {

                // ServiceType :apntype is not provisioned in the card,
                // Look through the profiles read from the card to locate
                // the UNSPECIFIED profile and add the service type to it.
                for (DataProfile dp : mTempOmhDataProfilesList) {
                    if (((DataProfileOmh)dp).getDataProfileTypeModem() ==
                                DataProfileTypeModem.PROFILE_TYPE_UNSPECIFIED) {
                        ((DataProfileOmh)dp).addServiceType(DataProfileTypeModem.
                                getDataProfileTypeModem(apntype));
                        log("OMH: Service Type added to UNSPECIFIED is : " +
                                DataProfileTypeModem.getDataProfileTypeModem(apntype));
                        break;
                    }
                }
            }
        }
    }

    /*
     * Retrieves the highest priority for all APP types except SUPL. Note that
     * for SUPL, retrieve the least priority among its profiles.
     */
    private int omhListGetArbitratedPriority(
            ArrayList<DataProfile> dataProfileListModem,
            String serviceType) {
        DataProfile profile = null;

        for (DataProfile dp : dataProfileListModem) {
            if (!((DataProfileOmh) dp).isValidPriority()) {
                log("[OMH] Invalid priority... skipping");
                continue;
            }

            if (profile == null) {
                profile = dp; // first hit
            } else {
                if (serviceType == PhoneConstants.APN_TYPE_SUPL) {
                    // Choose the profile with lower priority
                    profile = ((DataProfileOmh) dp).isPriorityLower(((DataProfileOmh) profile)
                            .getPriority()) ? dp : profile;
                } else {
                    // Choose the profile with higher priority
                    profile = ((DataProfileOmh) dp).isPriorityHigher(((DataProfileOmh) profile)
                            .getPriority()) ? dp : profile;
                }
            }
        }
        return ((DataProfileOmh) profile).getPriority();
    }

    public void clearActiveDataProfile() {
        mActiveDp = null;
    }

    public boolean isApnTypeActive(String type) {
        return mActiveDp != null && mActiveDp.canHandleType(type);
    }

    public boolean isOmhEnabled() {
        return mIsOmhEnabled;
    }

    protected boolean isApnTypeAvailable(String type) {
        for (String s : mSupportedApnTypes) {
            if (TextUtils.equals(type, s)) {
                return true;
            }
        }
        return false;
    }

    protected String[] getActiveApnTypes() {
        String[] result;
        if (mActiveDp != null) {
            result = mActiveDp.getServiceTypes();
        } else {
            result = new String[1];
            result[0] = PhoneConstants.APN_TYPE_DEFAULT;
        }
        return result;
    }

    protected void log(String s) {
        Log.d(LOG_TAG, "[CdmaDataProfileTracker] " + s);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java b/src/java/com/android/internal/telephony/cdma/DataProfileCdma.java
//Synthetic comment -- index f0f4970..a62e270 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

public class DataProfileCdma extends DataProfile {

    /* ID of the profile in the modem */
private static String PROFILE_TYPE = "CdmaNai";

private int mProfileId = 0;
//Synthetic comment -- @@ -43,6 +44,7 @@
return DataProfileType.PROFILE_TYPE_CDMA;
}

    @Override
public int getProfileId() {
return mProfileId;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/DataProfileOmh.java b/src/java/com/android/internal/telephony/cdma/DataProfileOmh.java
new file mode 100644
//Synthetic comment -- index 0000000..61ea894

//Synthetic comment -- @@ -0,0 +1,238 @@
/*
 * Copyright (c) 2010, Code Aurora Forum. All rights reserved.
 * Copyright (c) 2012, The Linux Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of Code Aurora Forum, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.android.internal.telephony.cdma;

import java.util.ArrayList;

import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.DataProfile;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.TelephonyProperties;

public class DataProfileOmh extends DataProfile {

    /**
     *  OMH spec 3GPP2 C.S0023-D defines the application types in terms of a
     *  32-bit mask where each bit represents one application
     *
     *  Application bit and the correspondign app type is listed below:
     *  1 Unspecified (all applications use the same profile)
     *  2 MMS
     *  3 Browser
     *  4 BREW
     *  5 Java
     *  6 LBS
     *  7 Terminal (tethered mode for terminal access)
     *  8-32 Reserved for future use
     *
     *  From this list all the implemented app types are listed in the enum
     */
    enum DataProfileTypeModem {
        /* Static mapping of OMH profiles to Android Service Types */
        PROFILE_TYPE_UNSPECIFIED(0x00000001, PhoneConstants.APN_TYPE_DEFAULT),
        PROFILE_TYPE_MMS(0x00000002, PhoneConstants.APN_TYPE_MMS),
        PROFILE_TYPE_LBS(0x00000020, PhoneConstants.APN_TYPE_SUPL),
        PROFILE_TYPE_TETHERED(0x00000040, PhoneConstants.APN_TYPE_DUN);

        int id;
        String serviceType;

        private DataProfileTypeModem(int i, String serviceType) {
            this.id = i;
            this.serviceType = serviceType;
        }

        public int getid() {
            return id;
        }

        public String getDataServiceType() {
            return serviceType;
        }

        public static DataProfileTypeModem getDataProfileTypeModem(String serviceType) {

            if (TextUtils.equals(serviceType, PhoneConstants.APN_TYPE_DEFAULT)) {
                return PROFILE_TYPE_UNSPECIFIED;
            } else if (TextUtils.equals(serviceType, PhoneConstants.APN_TYPE_MMS)) {
                return PROFILE_TYPE_MMS;
            } else if (TextUtils.equals(serviceType, PhoneConstants.APN_TYPE_SUPL)) {
                return PROFILE_TYPE_LBS;
            } else if (TextUtils.equals(serviceType, PhoneConstants.APN_TYPE_DUN)) {
                return PROFILE_TYPE_TETHERED;
            } else {
                /* For all other service types, return unspecified */
                return PROFILE_TYPE_UNSPECIFIED;
            }
        }
    }

    private int DATA_PROFILE_OMH_PRIORITY_LOWEST = 255;

    private int DATA_PROFILE_OMH_PRIORITY_HIGHEST = 0;

    private DataProfileTypeModem mDataProfileModem;

    private int serviceTypeMasks = 0;

    /* ID of the profile in the modem */
    private int mProfileId = 0;

    /* Priority of this profile in the modem */
    private int mPriority = 0;

    private static String PROFILE_TYPE = "DataProfileOmh";

    public DataProfileOmh(int id, String numeric, String name, String user, String password,
            int authType, String[] types, String protocol, String roamingProtocol, int bearer) {
        super(id, numeric, "", user, password,
                authType, types, protocol, roamingProtocol, bearer);
    }

    public DataProfileOmh() {
        /**
         * Default values if the profile is being used for only selective
         * fields e.g: just profileId and Priority. use case is when rest of the
         * fields can be read and processed only by the modem
         */
        this(0, null, PROFILE_TYPE, null, null,
               RILConstants.SETUP_DATA_AUTH_PAP_CHAP, null,
               SystemProperties.get(TelephonyProperties.PROPERTY_CDMA_IPPROTOCOL, "IP"),
               SystemProperties.get(TelephonyProperties.PROPERTY_CDMA_ROAMING_IPPROTOCOL, "IP"),
               0);

        this.mProfileId = 0;
        this.mPriority = 0;
    }

    public DataProfileOmh(int profileId, int priority) {
        this();
        this.mProfileId = profileId;
        this.mPriority = priority;
        this.types = new String[0];
    }

    @Override
    public boolean canHandleType(String serviceType) {
        return ( 0 != (serviceTypeMasks & DataProfileTypeModem.
                getDataProfileTypeModem(serviceType).getid()));
    }

    @Override
    public DataProfileType getDataProfileType() {
        return DataProfileType.PROFILE_TYPE_OMH;
    }

    @Override
    public String toShortString() {
        return "DataProfile OMH";
    }

    @Override
    public String toHash() {
        return this.toString() + mProfileId + mPriority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString())
        .append(mProfileId)
        .append(", ").append(mPriority);
        sb.append("]");
        return sb.toString();
    }

    public void setDataProfileTypeModem(DataProfileTypeModem modemProfile) {
        mDataProfileModem = modemProfile;
    }

    public DataProfileTypeModem getDataProfileTypeModem() {
        return mDataProfileModem;
    }

    public void setProfileId(int profileId) {
        mProfileId = profileId;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    /* priority defined from 0..255; 0 is highest */
    public boolean isPriorityHigher(int priority) {
        return isValidPriority(priority) && (mPriority < priority);
    }

    /* priority defined from 0..255; 0 is highest */
    public boolean isPriorityLower(int priority) {
        return isValidPriority(priority) && mPriority > priority;
    }

    public boolean isValidPriority() {
        return isValidPriority(mPriority);
    }

    /* NOTE: priority values are reverse, lower number = higher priority */
    private boolean isValidPriority(int priority) {
        return priority >= DATA_PROFILE_OMH_PRIORITY_HIGHEST && priority <= DATA_PROFILE_OMH_PRIORITY_LOWEST;
    }

    public int getProfileId() {
        return mProfileId;
    }

    public int getPriority() {
        return mPriority;
    }

    public String[] getServiceTypes() {
        String[] dummy = null;
        return dummy;
    }

    public void addServiceType(DataProfileTypeModem modemProfile) {
        serviceTypeMasks |= modemProfile.getid();

        // Update the types
        ArrayList<String> serviceTypes = new ArrayList<String>();
        for (DataProfileTypeModem dpt : DataProfileTypeModem.values()) {
            if (0 != (serviceTypeMasks & dpt.getid())) {
                serviceTypes.add(dpt.getDataServiceType());
            }
        }
        types = serviceTypes.toArray(new String[0]);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipCommandInterface.java b/src/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index 99f4e0f..70962ab 100644

//Synthetic comment -- @@ -421,4 +421,7 @@

public void getVoiceRadioTechnology(Message result) {
}

    public void getDataCallProfile(int appType, Message result) {
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/test/SimulatedCommands.java b/src/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 1672044..f5d66d0 100644

//Synthetic comment -- @@ -1522,4 +1522,22 @@
public void getVoiceRadioTechnology(Message response) {
unimplemented(response);
}

    public void getImsRegistrationState(Message response) {
        unimplemented(response);
    }

    public void sendImsCdmaSms(byte[] pdu, int retry, int messageRef,
            Message response){
        unimplemented(response);
    }

    public void sendImsGsmSms(String smscPDU, String pdu,
            int retry, int messageRef, Message response){
        unimplemented(response);
    }

    public void getDataCallProfile(int appType, Message response){
        unimplemented(response);
    }
}








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java b/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java
//Synthetic comment -- index f001977..182f5f5 100644

//Synthetic comment -- @@ -621,4 +621,8 @@
public void iccIOForApp(int command, int fileid, String path, int p1, int p2, int p3,
String data, String pin2, String aid, Message response) {
}

    @Override
    public void getDataCallProfile(int appType, Message result) {
    }
}







