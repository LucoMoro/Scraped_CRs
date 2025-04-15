/*Telephony: Enhanced Operator Name String (EONS) algorithm.

The operator name for registered PLMN is displayed in the
following order of preference.

1) Name from EF_OPL/EF_PNN files.

2) Name from NITZ messge.

3) Name from ME database.

4) Name from Network.

This algorithm implements this operator name deriving logic.
3GPP specs referred
1) TS 22.101 A.3 - operator name priority.
2) TS 31.102 - for EF data description.
3) TS 24.008 - for PLMN and LAC coding details.
4) TS 23.122 - for PLMN matching.

Add support to derive EONS for LTE networks based on TAC.

Change-Id:I558af4972ded6c1bd891cad00090688df4c6aa47*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccRecords.java b/src/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index cfcc412..a6844e5 100644

//Synthetic comment -- @@ -86,6 +86,7 @@
public static final int EVENT_MWI = 0; // Message Waiting indication
public static final int EVENT_CFI = 1; // Call Forwarding indication
public static final int EVENT_SPN = 2; // Service Provider Name
    public static final int EVENT_EONS = 3;

public static final int EVENT_GET_ICC_RECORD_DONE = 100;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/OperatorInfo.java b/src/java/com/android/internal/telephony/OperatorInfo.java
//Synthetic comment -- index 1999cb3..79f6d49 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
return state;
}

    public OperatorInfo(String operatorAlphaLong,
String operatorAlphaShort,
String operatorNumeric,
State state) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1f69f7a..307b54b 100755

//Synthetic comment -- @@ -114,6 +114,7 @@
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;
protected static final int EVENT_ICC_CHANGED                    = 31;
    protected static final int EVENT_GET_NETWORKS_DONE              = 32;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 090e38d..a650b7b 100755

//Synthetic comment -- @@ -140,6 +140,7 @@
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
protected static final int EVENT_RADIO_ON                          = 41;
protected static final int EVENT_ICC_CHANGED                       = 42;
    protected static final int EVENT_ICC_RECORD_EVENTS                 = 43;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
//Synthetic comment -- index 5ef0333..ee79f20 100644

//Synthetic comment -- @@ -44,8 +44,8 @@
case EF_SPN:
case EF_AD:
case EF_MBDN:
case EF_OPL:
        case EF_PNN:
case EF_SPDI:
case EF_SST:
case EF_CFIS:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/Eons.java b/src/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..d7de1ab

//Synthetic comment -- @@ -0,0 +1,288 @@
/*
 * Copyright (C) 2010-2011 The Android Open Source Project
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

package com.android.internal.telephony.gsm;

import android.util.Log;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.OperatorInfo;

import java.util.ArrayList;


/**
 * {@hide}
 */
public final class Eons {
    static final String LOG_TAG = "GSM";

    private static final boolean DBG = true;

    /**
     * INITING: OPL or PNN records not read yet, in this state we do not
     *          know whether EONS is enabled or not, operator name display will be
     *          supressed in this state
     * DISABLED: Exception in reading PNN and or OPL records, EONS is
     *          disabled, use operator name from ril
     * PNN_PRESENT: Only PNN is present, Operator name from first record of PNN can be used
     *          if the registered operator is HPLMN otherwise use name from ril
     * PNN_AND_OPL_PRESENT: Both PNN and OPL files are available, EONS name can be
     *          derived using both these files
     */
    public enum EonsState {
        INITING,
        DISABLED,
        PNN_PRESENT,
        PNN_AND_OPL_PRESENT;

        public boolean isIniting() {
            return (this == INITING);
        }

        public boolean isDisabled() {
            return (this == DISABLED);
        }

        public boolean isPnnPresent() {
            return (this == PNN_PRESENT);
        }

        public boolean isPnnAndOplPresent() {
            return (this == PNN_AND_OPL_PRESENT);
        }
    }

    public enum EonsControlState {
        INITING,
        PRESENT,
        ABSENT;

        public boolean isIniting() {
            return (this == INITING);
        }

        public boolean isPresent() {
            return (this == PRESENT);
        }

        public boolean isAbsent() {
            return (this == ABSENT);
        }
    }

    // ***** Instance Variables
    EonsControlState mPnnDataState = EonsControlState.INITING;
    EonsControlState mOplDataState = EonsControlState.INITING;
    OplRecords mOplRecords;
    PnnRecords mPnnRecords;

    // ***** Constructor
    public Eons() {
        reset();
    }

    // ***** Public Methods
    public void reset() {
        mPnnDataState = EonsControlState.INITING;
        mOplDataState = EonsControlState.INITING;
        mOplRecords = null;
        mPnnRecords = null;
    }

    public void setOplData(ArrayList <byte[]> records) {
        mOplDataState = EonsControlState.PRESENT;
        mOplRecords = new OplRecords(records);
    }

    public void resetOplData() {
        mOplDataState = EonsControlState.ABSENT;
        mOplRecords = null;
    }

    public void setPnnData(ArrayList <byte[]> records) {
        mPnnDataState = EonsControlState.PRESENT;
        mPnnRecords = new PnnRecords(records);
    }

    public void resetPnnData() {
        mPnnDataState = EonsControlState.ABSENT;
        mPnnRecords = null;
    }

    /**
     * Get the EONS derived from EF_OPL/EF_PNN or EF_CPHS_ONS/EF_CPHS_ONS_SHORT
     * files for registered operator.
     * @return Enhanced Operator Name String (EONS) if it can be derived and
     * null otherwise.
     */
    public String getEons() {
        String name = null;

        if (mPnnRecords != null) {
            name = mPnnRecords.getCurrentEons();
        }
        return name;
    }

    /**
     * When there is a change in LAC or Service State, update EONS
     * for registered plmn.
     * @param regOperator is the registered operator PLMN
     * @param lac is current lac
     * @param hplmn from SIM
     * @return returns true if operator name display needs updation, false
     * otherwise
     */
    public boolean updateEons(String regOperator, int lac, String hplmn) {
        boolean needsOperatorNameUpdate = true;

        if ((getEonsState()).isPnnAndOplPresent()) {
            // If both PNN and OPL data is available, a match should
            // be found in OPL file for registered operator and
            // corresponding record in the PNN file should be used
            // for fetching EONS name.
            updateEonsFromOplAndPnn(regOperator, lac);
        } else if ((getEonsState()).isPnnPresent()) {
            // If only PNN data is available, update EONS name from first
            // record of PNN if registered operator is HPLMN.
            updateEonsIfHplmn(regOperator, hplmn);
        } else if ((getEonsState()).isIniting()) {
            if (DBG) log("Reading data from EF_OPL or EF_PNN is not complete. " +
                "Suppress operator name display until all EF_OPL/EF_PNN data is read.");
            needsOperatorNameUpdate = false;
        }

        // For all other cases including both EF_PNN/EF_OPL absent use
        // operator name from ril.
        return needsOperatorNameUpdate;
    }

    /**
     * Fetch EONS for Available Networks from EF_PNN data.
     * @param avlNetworks, ArrayList of Available Networks
     * @return ArrayList Available Networks with EONS if
     * success,otherwise null
     */
    public ArrayList<OperatorInfo> getEonsForAvailableNetworks(ArrayList<OperatorInfo> avlNetworks) {
        ArrayList<OperatorInfo> eonsNetworkNames = null;

        if (!(getEonsState()).isPnnAndOplPresent()) {
            loge("OPL/PNN data is not available. Use the network names from Ril.");
            return null;
        }

        if ((avlNetworks != null) && (avlNetworks.size() > 0)) {
            int size = avlNetworks.size();
            eonsNetworkNames = new ArrayList<OperatorInfo>(size);
            if (DBG) log("Available Networks List Size = " + size);
            for (int i = 0; i < size; i++) {
                 int pnnRecord = 0;
                 String pnnName = null;
                 OperatorInfo ni;

                 ni = avlNetworks.get(i);
                 // Get EONS for this operator from OPL/PNN data.
                 pnnRecord = mOplRecords.getMatchingPnnRecord(ni.getOperatorNumeric(), -1, false);
                 pnnName = mPnnRecords.getNameFromPnnRecord(pnnRecord, false);
                 if (DBG) log("PLMN = " + ni.getOperatorNumeric() + ", ME Name = "
                   + ni.getOperatorAlphaLong() + ", PNN Name = " + pnnName);
                 // EONS could not be derived for this operator. Use the
                 // same name in the list.
                 if (pnnName == null) {
                     pnnName = ni.getOperatorAlphaLong();
                 }
                 eonsNetworkNames.add (new OperatorInfo(pnnName,ni.getOperatorAlphaShort(),
                        ni.getOperatorNumeric(),ni.getState()));
            }
        } else {
            loge("Available Networks List is empty");
        }

        return eonsNetworkNames;
    }

    // ***** Private Methods
    /**
     * Derive EONS from matching OPL and PNN record for registered
     * operator.
     * @param regOperator is the registered operator PLMN
     * @param lac is current lac
     */
    private void updateEonsFromOplAndPnn(String regOperator, int lac) {
        int pnnRecord;
        String pnnName;

        pnnRecord = mOplRecords.getMatchingPnnRecord(regOperator, lac, true);
        pnnName = mPnnRecords.getNameFromPnnRecord(pnnRecord, true);
        if (DBG) log("Fetched EONS name from EF_PNN record = " + pnnRecord +
              ", name = " + pnnName);
    }

    /**
     * Derive EONS from EF_PNN first record if registered PLMN is HPLMN.
     * @param regOperator is the registered operator PLMN
     * @param hplmn from SIM
     */
    private void updateEonsIfHplmn(String regOperator, String hplmn) {
        if (DBG) log ("Comparing hplmn, " + hplmn +
                       " with registered plmn " + regOperator);
        // If the registered PLMN is HPLMN, then derive EONS name
        // from first record of EF_PNN
        if ((hplmn != null) && hplmn.equals(regOperator)) {
            String pnnName = mPnnRecords.getNameFromPnnRecord(1, true);
            if (DBG) log("Fetched EONS name from EF_PNN's first record, name = " + pnnName);
        }
    }

    /**
     * Determines how to apply EONS algorithm based on OPL/PNN data
     * availability
     * If both OPL and PNN data is available, then EONS operator name
     * should be derived using both these files
     * If only PNN data is available, use name from first record of PNN as
     * EONS name if registered PLMN is HPLMN
     * If only OPL data is available, do not use EONS algorithm
     * Suppress operator name display until OPL and PNN data is read
     * This should not take much and should not cause any issue
     * If operator name display is allowed during this window, there will be a
     * blip in operator name, momentarily displaying name from ril and then
     * switching to EONS name, this is not acceptable.
     */
    private EonsState getEonsState() {
        // OPL or PNN data read is not complete.
        if ((mPnnDataState.isIniting()) || (mOplDataState.isIniting())) {
            return EonsState.INITING;
        } else if (mPnnDataState.isPresent()) {
            if(mOplDataState.isPresent()) {
               return EonsState.PNN_AND_OPL_PRESENT;
            } else {
               return EonsState.PNN_PRESENT;
            }
        } else {
            // If PNN is not present, disable EONS algorithm.
            return EonsState.DISABLED;
        }
    }

    private void log(String s) {
        Log.d(LOG_TAG, "[EONS] " + s);
    }

    private void loge(String s) {
        Log.e(LOG_TAG, "[EONS] " + s);
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..30de3d2 100644

//Synthetic comment -- @@ -113,6 +113,10 @@
SimSmsInterfaceManager mSimSmsIntManager;
PhoneSubInfo mSubInfo;

    /** EONS enabled flag. */
    private boolean mEonsEnabled =
            SystemProperties.getBoolean(TelephonyProperties.PROPERTY_EONS_ENABLED, true);


Registrant mPostDialHandler;

//Synthetic comment -- @@ -985,7 +989,13 @@

public void
getAvailableNetworks(Message response) {
        Message msg;
        if (mEonsEnabled) {
            msg = obtainMessage(EVENT_GET_NETWORKS_DONE,response);
        } else {
            msg = response;
        }
        mCM.getAvailableNetworks(msg);
}

/**
//Synthetic comment -- @@ -1332,6 +1342,34 @@
}
break;

            case EVENT_GET_NETWORKS_DONE:
                ArrayList<OperatorInfo> eonsNetworkNames = null;

                ar = (AsyncResult)msg.obj;
                if (ar.exception == null && mIccRecords != null) {
                    eonsNetworkNames =
                      ((SIMRecords)mIccRecords.get()).getEonsForAvailableNetworks((ArrayList<OperatorInfo>)ar.result);
                }

                if (mIccRecords == null && ar.exception == null) {
                    Log.w(LOG_TAG, "getEonsForAvailableNetworks() aborted. icc absent?");
                }

                if (eonsNetworkNames != null) {
                    Log.i(LOG_TAG, "[EONS] Populated EONS for available networks.");
                } else {
                    eonsNetworkNames = (ArrayList<OperatorInfo>)ar.result;
                }

                onComplete = (Message) ar.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, eonsNetworkNames, ar.exception);
                    onComplete.sendToTarget();
                } else {
                    Log.e(LOG_TAG, "[EONS] In EVENT_GET_NETWORKS_DONE, onComplete is null!");
                }
                break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1375,6 +1413,14 @@
case IccRecords.EVENT_MWI:
notifyMessageWaitingIndicator();
break;
            case SIMRecords.EVENT_SPN:
                mSST.updateSpnDisplay();
                break;
            case SIMRecords.EVENT_EONS:
                if (mEonsEnabled) {
                    mSST.updateEons();
                }
                break;
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..5146938 100755

//Synthetic comment -- @@ -152,6 +152,13 @@
/** waiting period before recheck gprs and voice registration. */
static final int DEFAULT_GPRS_CHECK_PERIOD_MILLIS = 60 * 1000;

    private int mDataRadioTechnology = 0;
    private int mTAC = -1;

    /** EONS enabled flag. */
    private boolean mEonsEnabled =
            SystemProperties.getBoolean(TelephonyProperties.PROPERTY_EONS_ENABLED, true);

/** Notification type. */
static final int PS_ENABLED = 1001;            // Access Control blocks data service
static final int PS_DISABLED = 1002;           // Access Control enables data service
//Synthetic comment -- @@ -235,6 +242,7 @@

// Gsm doesn't support OTASP so its not needed
phone.notifyOtaspChanged(OTASP_NOT_NEEDED);
        Log.i(LOG_TAG,"Is EONS enabled: " + mEonsEnabled);
}

public void dispose() {
//Synthetic comment -- @@ -244,7 +252,10 @@
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
if (mUiccApplcation != null) {mUiccApplcation.unregisterForReady(this);}
        if (mIccRecords != null) {
            mIccRecords.unregisterForRecordsLoaded(this);
            mIccRecords.unregisterForRecordsEvents(this);
        }
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -389,6 +400,11 @@
updateSpnDisplay();
break;

            case EVENT_ICC_RECORD_EVENTS:
                ar = (AsyncResult)msg.obj;
                processIccRecordEvents((Integer)ar.result);
                break;

case EVENT_LOCATION_UPDATES_ENABLED:
ar = (AsyncResult) msg.obj;

//Synthetic comment -- @@ -483,6 +499,47 @@
cm.setRadioPower(false, null);
}

    private void processIccRecordEvents(int eventCode) {
        switch (eventCode) {
            case SIMRecords.EVENT_SPN:
                updateSpnDisplay();
                break;
            case SIMRecords.EVENT_EONS:
                if (mEonsEnabled) {
                    int lactac = getLacOrTac();
                    boolean needsSpnUpdate =
                       ((SIMRecords)mIccRecords).updateEons(ss.getOperatorNumeric(), lactac);
                    if (needsSpnUpdate) {
                        updateSpnDisplay();
                    }
                }
                break;
        }
    }

    public void updateEons() {
        boolean needsUpdate = false;
        int lactac = -1;

        if (mIccRecords == null) return;

        lactac = getLacOrTac();

        needsUpdate = ((SIMRecords)mIccRecords).updateEons(ss.getOperatorNumeric(), lactac);
        Log.d(LOG_TAG, "[EONS] updateEons() lactac = " + lactac + " , needsUpdate = " +
                needsUpdate + " , OperatorNumeric = " + ss.getOperatorNumeric());
        if (needsUpdate) {
            String eonsLong = ((SIMRecords)mIccRecords).getEons();
            Log.d(LOG_TAG, "[EONS] updateEons() eonsLong = " + eonsLong);
            if (eonsLong != null) {
                // Update operator long name with EONS Long.
                ss.setOperatorName(eonsLong, ss.getOperatorAlphaShort(),
                      ss.getOperatorNumeric());
            }
            updateSpnDisplay();
        }
    }

protected void updateSpnDisplay() {
if (mIccRecords == null) {
return;
//Synthetic comment -- @@ -502,7 +559,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN
                && (ss.getState() == ServiceState.STATE_IN_SERVICE);
boolean showPlmn = !TextUtils.isEmpty(plmn) &&
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -525,6 +583,18 @@
curPlmn = plmn;
}

    private int getLacOrTac() {
        int lactac = -1;
        if ((mDataRadioTechnology == ServiceState.RIL_RADIO_TECHNOLOGY_LTE) &&
                (gprsState == ServiceState.STATE_IN_SERVICE) &&
                (mTAC >= 0)) {
            lactac = mTAC;
        } else {
            if (cellLoc != null) lactac = cellLoc.getLac();
        }
        return lactac;
    }

/**
* Handle the result of one of the pollState()-related requests
*/
//Synthetic comment -- @@ -623,6 +693,10 @@
if (states.length >= 6) {
mNewMaxDataCalls = Integer.parseInt(states[5]);
}
                            if (states[1] != null && states[1].length() > 0) {
                                mTAC = Integer.parseInt(states[1], 16);
                                Log.d(LOG_TAG, "[EONS] Received TAC = " + mTAC);
                            }
} catch (NumberFormatException ex) {
loge("error parsing GprsRegistrationState: " + ex);
}
//Synthetic comment -- @@ -631,6 +705,9 @@
mDataRoaming = regCodeIsRoaming(regState);
mNewRilRadioTechnology = type;
newSS.setRadioTechnology(type);
                    mDataRadioTechnology = mNewRilRadioTechnology;
                    Log.d(LOG_TAG, "[EONS] EVENT_POLL_STATE_GPRS newGPRSState ="
                        + newGPRSState + " , mDataRadioTechnology =" + mDataRadioTechnology + " , mTAC =" + mTAC);
break;

case EVENT_POLL_STATE_OPERATOR:
//Synthetic comment -- @@ -833,6 +910,14 @@
mNitzUpdatedTime = false;
}

         if (mEonsEnabled) {
            Log.i(LOG_TAG,"Network State Changed, get EONS and update operator name display");
            updateEons();
        } else {
            Log.i(LOG_TAG,"Network State Changed, update operator name display");
            updateSpnDisplay();
        }

if (hasChanged) {
String operatorNumeric;

//Synthetic comment -- @@ -1632,6 +1717,7 @@
mUiccApplcation.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
                    mIccRecords.unregisterForRecordsEvents(this);
}
mIccRecords = null;
mUiccApplcation = null;
//Synthetic comment -- @@ -1643,6 +1729,7 @@
mUiccApplcation.registerForReady(this, EVENT_SIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
                    mIccRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/OplRecords.java b/src/java/com/android/internal/telephony/gsm/OplRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..179b625

//Synthetic comment -- @@ -0,0 +1,176 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.internal.telephony.gsm;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;


/**
 * {@hide}
 */
public final class OplRecords {
    static final String LOG_TAG = "GSM";
    static final int wildCardDigit = 0x0D;

    private static final boolean DBG = false;

    // ***** Instance Variables
    private ArrayList <OplRecord> mRecords;

    // ***** Constructor
    OplRecords(ArrayList <byte[]> records) {
        mRecords = new ArrayList<OplRecord>();

        for (byte[] record : records) {
             mRecords.add(new OplRecord(record));
             if (DBG) {
                log("Record " + mRecords.size() + ": " +
                mRecords.get(mRecords.size() - 1));
             }
        }
    }

    private void log(String s) {
        Log.d(LOG_TAG, "[OplRecords EONS] " + s);
    }

    private void loge(String s) {
        Log.e(LOG_TAG, "[OplRecords EONS] " + s);
    }

    // ***** Public Methods
    public int size() {
        return (mRecords != null) ? mRecords.size() : 0;
    }

    /**
     * Function to get PNN record number from matching OPL record for registered plmn.
     * @param operator, registered plmn (mcc+mnc)
     * @param lac, current lac
     * @param useLac, whether to match lac or not
     * @return returns PNN record number from matching OPL record.
     */
    public int getMatchingPnnRecord(String operator, int lac, boolean useLac) {
        int[] bcchPlmn = {0,0,0,0,0,0};

        if (TextUtils.isEmpty(operator)) {
            loge("No registered operator.");
            return 0;
        } else if (useLac && (lac == -1)) {
            loge("Invalid LAC");
            return 0;
        }

        int length = operator.length();
        if ((length != 5) && (length != 6)) {
            loge("Invalid registered operator length " + length);
            return 0;
        }

        // Convert operator sting into MCC/MNC digits.
        for (int i = 0; i < length; i++) {
             bcchPlmn[i] = operator.charAt(i) - '0';
        }

        for (OplRecord record : mRecords) {
             if (matchPlmn(record.mPlmn, bcchPlmn)) {
                 // While deriving EONS for Available Networks, we do
                 // not have Lac, hence just match the plmn.
                 if (!useLac || ((record.mLac1 <= lac) && (lac <= record.mLac2))) {
                      // Matching OPL record found, return PNN record number.
                      return record.getPnnRecordNumber();
                 }
             }
        }

        // No matching OPL record found, return 0 so that operator name from Ril
        // can be used.
        loge("No matching OPL record found.");
        return 0;
    }

    /**
     * Function to match plmn from EF_OPL record with the registered plmn.
     * @param simPlmn, plmn read from EF_OPL record, size will always be 6
     * @param bcchPlmn, registered plmn, size is 5 or 6
     * @return true if plmns match, otherwise false.
     */
    private boolean matchPlmn (int simPlmn[], int bcchPlmn[]) {
        boolean match = true;

        for (int i = 0; i < bcchPlmn.length; i++) {
             match = match & ((bcchPlmn[i] == simPlmn[i]) ||
                   (simPlmn[i] == wildCardDigit));
        }

        return match;
    }

    // EF_OPL record parsing as per 3GPP TS 31.102 section 4.2.59
    public static class OplRecord {
        private int[] mPlmn = {0,0,0,0,0,0};
        private int mLac1;
        private int mLac2;
        private int mPnnRecordNumber;

        OplRecord(byte[] record) {
            getPlmn(record);
            getLac(record);
            mPnnRecordNumber = 0xff & record[7];
        }

        // PLMN decoding as per 3GPP TS 24.008 section 10.5.1.13
        private void getPlmn(byte[] record) {
            mPlmn[0] = 0x0f & record[0];/*mcc1*/
            mPlmn[1] = 0x0f & (record[0] >> 4);/*mcc2*/
            mPlmn[2] = 0x0f & record[1];/*mcc3*/

            mPlmn[3] = 0x0f & record[2];/*mnc1*/
            mPlmn[4] = 0x0f & (record[2] >> 4);/*mnc2*/
            mPlmn[5] = 0x0f & (record[1] >> 4);/*mnc3*/

            // Certain operators support 2 digit MNCs. In such cases the last
            // digit is not programmed. Ideally only 2 digits of BCCH MNC
            // should be compared with corresponding digits of SIM MNC,
            // this should also match with BCCH MNC where MNC3 is zero.
            // Hence forcing SIM MNC3 to zero if it is not programmed.
            if (mPlmn[5] == 0x0f) mPlmn[5] = 0;
        }

        // LAC decoding as per 3GPP TS 24.008
        private void getLac(byte[] record) {
            // LAC bytes are in big endian. Bytes 3 and 4 are for LAC1 and
            // bytes 5 and 6 are for LAC2.
            mLac1 = ((record[3] & 0xff) << 8) | (record[4] & 0xff);
            mLac2 = ((record[5] & 0xff) << 8) | (record[6] & 0xff);
        }

        public int getPnnRecordNumber() {
            return mPnnRecordNumber;
        }

        public String toString() {
            return "PLMN=" + Integer.toHexString(mPlmn[0]) + Integer.toHexString(mPlmn[1]) +
                Integer.toHexString(mPlmn[2]) + Integer.toHexString(mPlmn[3]) +
                Integer.toHexString(mPlmn[4]) + Integer.toHexString(mPlmn[5]) +
                ", LAC1=" + mLac1 + ", LAC2=" + mLac2 + ", PNN Record=" + mPnnRecordNumber;
        }
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/PnnRecords.java b/src/java/com/android/internal/telephony/gsm/PnnRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..5b53e92

//Synthetic comment -- @@ -0,0 +1,152 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.internal.telephony.gsm;

import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.IccUtils;

import java.util.ArrayList;


/**
 * {@hide}
 */
public final class PnnRecords {
    static final String LOG_TAG = "GSM";

    private static final boolean DBG = false;

    // ***** Instance Variables
    private ArrayList <PnnRecord> mRecords;
    private String mCurrentEons;

    // ***** Constructor
    PnnRecords(ArrayList <byte[]> records) {
        mRecords = new ArrayList<PnnRecord>();
        mCurrentEons = null;

        for (byte[] record : records) {
             mRecords.add(new PnnRecord(record));
             if (DBG) {
                log("Record " + mRecords.size() + ": " +
                mRecords.get(mRecords.size() - 1));
             }
        }
    }

    public static void log(String s) {
        Log.d(LOG_TAG, "[PnnRecords EONS] " + s);
    }

    public static void loge(String s) {
        Log.e(LOG_TAG, "[PnnRecords EONS] " + s);
    }

    // ***** Public Methods
    public int size() {
        return (mRecords != null) ? mRecords.size() : 0;
    }

    public String getCurrentEons() {
        return mCurrentEons;
    }

    /**
     * Function to get Full Name from given PNN record number.
     * @param pnnRecord, PNN record number
     * @param update, specifies whether to update currentEons or not
     * @return returns Full Name from given PNN record.
     */
    public String getNameFromPnnRecord(int recordNumber, boolean update) {
        String fullName = null;

        if (recordNumber < 1 || recordNumber > mRecords.size()) {
            loge("Invalid PNN record number " + recordNumber);
        } else {
            fullName = mRecords.get(recordNumber - 1).getFullName();
        }

        // When deriving name for Available Networks, current EONS name should
        // not be updated.
        if (update) mCurrentEons = fullName;
        return fullName;
    }

    // EF_PNN record parsing as per 3GPP TS 31.102 section 4.2.58
    public static class PnnRecord {
        static final int TAG_FULL_NAME_IEI = 0x43;
        static final int TAG_SHORT_NAME_IEI = 0x45;
        static final int TAG_ADDL_INFO = 0x80;

        private String mFullName;
        private String mShortName;
        private String mAddlInfo;

        PnnRecord(byte[] record) {
            mFullName = null;
            mShortName = null;
            mAddlInfo = null;

            SimTlv tlv = new SimTlv(record, 0, record.length);

            if (tlv.isValidObject() && tlv.getTag() == TAG_FULL_NAME_IEI) {
                mFullName = IccUtils.networkNameToString(tlv.getData(), 0,
                      tlv.getData().length);
            } else {
                if(DBG) log("Invalid tlv Object for Full Name, tag= " +
                      tlv.getTag() + ", valid=" + tlv.isValidObject());
            }

            tlv.nextObject();
            if (tlv.isValidObject() && tlv.getTag() == TAG_SHORT_NAME_IEI) {
                mShortName = IccUtils.networkNameToString(tlv.getData(), 0,
                      tlv.getData().length);
            } else {
                if(DBG) log("Invalid tlv Object for Short Name, tag= " +
                      tlv.getTag() + ", valid=" + tlv.isValidObject());
            }

            tlv.nextObject();
            if (tlv.isValidObject() && tlv.getTag() == TAG_ADDL_INFO) {
                mAddlInfo = IccUtils.networkNameToString(tlv.getData(), 0,
                      tlv.getData().length);
            } else {
                if(DBG) log("Invalid tlv Object for Addl Info, tag= " +
                      tlv.getTag() + ", valid=" + tlv.isValidObject());
            }
        }

        public String getFullName() {
            return mFullName;
        }

        public String getShortName() {
            return mShortName;
        }

        public String getAddlInfo() {
            return mAddlInfo;
        }

        public String toString() {
            return "Full Name=" + mFullName + ", Short Name=" + mShortName +
               ", Additional Info=" + mAddlInfo;
        }
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0387a70..97a43f9 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
case EF_SPN:
case EF_AD:
case EF_MBDN:
        case EF_OPL:
case EF_PNN:
case EF_SPDI:
case EF_SST:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..1115c59 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_EONS_ENABLED;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -38,11 +39,13 @@
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.UiccCardApplication;
import com.android.internal.telephony.gsm.Eons;

import java.util.ArrayList;

//Synthetic comment -- @@ -64,6 +67,12 @@

SpnOverride mSpnOverride;

    Eons mEons;

    /** EONS enabled flag. */
    private boolean mEonsEnabled =
            SystemProperties.getBoolean(PROPERTY_EONS_ENABLED, true);

// ***** Cached SIM State; cleared on channel close

private boolean callForwardingEnabled;
//Synthetic comment -- @@ -151,6 +160,9 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;
    private static final int EVENT_GET_ALL_OPL_RECORDS_DONE = 34;
    private static final int EVENT_GET_ALL_PNN_RECORDS_DONE = 35;
    private static final int EVENT_GET_SPN = 36;

// Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

//Synthetic comment -- @@ -183,6 +195,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

        mEons = new Eons();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -226,6 +240,7 @@
pnnHomeName = null;

adnCache.reset();
        mEons.reset();

log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, null);
//Synthetic comment -- @@ -1089,6 +1104,56 @@
handleEfCspData(data);
break;

            case EVENT_GET_ALL_OPL_RECORDS_DONE:
                isRecordLoadResponse = true;
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG, "[EONS] Exception in fetching OPL Records: " + ar.exception);
                    mEons.resetOplData();
                    break;
                }

                mEons.setOplData((ArrayList<byte[]>)ar.result);
                mRecordsEventsRegistrants.notifyResult(EVENT_EONS);
                break;

            case EVENT_GET_ALL_PNN_RECORDS_DONE:
                isRecordLoadResponse = true;
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG, "[EONS] Exception in fetching PNN Records: " + ar.exception);
                    mEons.resetPnnData();
                    break;
                }

                mEons.setPnnData((ArrayList<byte[]>)ar.result);
                mRecordsEventsRegistrants.notifyResult(EVENT_EONS);
                break;

             case EVENT_GET_SPN:
                isRecordLoadResponse = true;
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG, "[EONS] Exception in reading EF_SPN: " + ar.exception);
                    spnDisplayCondition = -1;
                    break;
                }

                data = (byte[]) ar.result;
                spnDisplayCondition = 0xff & data[0];
                spn = IccUtils.adnStringFieldToString(data, 1, data.length - 1);

                SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                // When device enters or exits Home Zone, certain operators update
                // EF_SPN file. This helps to know if the device is in Home Zone or
                // not. Hence SPN display should be updated on EF_SPN refresh.
                mRecordsEventsRegistrants.notifyResult(EVENT_SPN);
                break;

default:
super.handleMessage(msg);   // IccRecords handles generic record load responses

//Synthetic comment -- @@ -1121,9 +1186,33 @@
mFh.loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
            case EF_OPL:
                if (DBG) log("[EONS] SIM Refresh for EF_OPL");
                if (mEonsEnabled) {
                    if (DBG) log("[EONS] Update EF_OPL Records");
                    recordsToLoad++;
                    mFh.loadEFLinearFixedAll(EF_OPL,
                          obtainMessage(EVENT_GET_ALL_OPL_RECORDS_DONE));
                }
                break;
            case EF_PNN:
                if (DBG) log("[EONS] SIM Refresh for EF_PNN");
                if (mEonsEnabled) {
                    if (DBG) log("[EONS] Update EF_PNN Records");
                    recordsToLoad++;
                    mFh.loadEFLinearFixedAll(EF_PNN,
                          obtainMessage(EVENT_GET_ALL_PNN_RECORDS_DONE));
                }
                break;
            case EF_SPN:
                if (DBG) log("[EONS] SIM Refresh for EF_SPN");
                recordsToLoad++;
                mFh.loadEFTransparent(EF_SPN,
                        obtainMessage(EVENT_GET_SPN));
                break;
default:
                // For now, fetch all records if this is not
                // one of the above handled files.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1352,6 +1441,14 @@
mFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

        if (mEonsEnabled) {
            mFh.loadEFLinearFixedAll(EF_OPL, obtainMessage(EVENT_GET_ALL_OPL_RECORDS_DONE));
            recordsToLoad++;

            mFh.loadEFLinearFixedAll(EF_PNN, obtainMessage(EVENT_GET_ALL_PNN_RECORDS_DONE));
            recordsToLoad++;
        }

mFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1595,6 +1692,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

    /**
     * Get the EONS name derived from EF_OPL/EF_PNN or EF_CPHS_ONS/EF_CPHS_ONS_SHORT
     * files for registered operator.
     * @return Enhanced Operator Name String (EONS) if it can be derived and
     * null otherwise.
     */
    public String getEons() {
        return mEons.getEons();
    }

    /**
     * When there is a change in LAC or Service State, update EONS
     * for registered plmn.
     * @param regOperator is the registered operator PLMN
     * @param lac is current lac
     * @return returns true if operator name display needs updation, false
     * otherwise
     */
    public boolean updateEons(String regOperator, int lac) {
        return mEons.updateEons(regOperator, lac, getOperatorNumeric());
    }

    /**
     * Fetch EONS for Available Networks from EF_PNN data.
     * @param avlNetworks, ArrayList of Available Networks
     * @return ArrayList Available Networks with EONS if
     * success, otherwise null
     */
    public ArrayList<OperatorInfo> getEonsForAvailableNetworks(ArrayList<OperatorInfo> avlNetworks) {
        return mEons.getEonsForAvailableNetworks(avlNetworks);
    }

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







