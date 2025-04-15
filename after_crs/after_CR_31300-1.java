/*Telephony: Enhanced Operator Name String (EONS) algorithm.

The operator name for registered PLMN should be displayed in the
following order of preference.

1) Name should be derived from EF_OPL/EF_PNN files.

2) NITZ name.

3) Name from ME database.

4) Name from Network.

This algorithm implements this operator name deriving logic.
3GPP specs referred
1) TS 22.101 A.3 - operator name priority.
2) TS 31.102 - for EF data description.
3) TS 24.008 - for PLMN and LAC coding details.
4) TS 23.122 - for PLMN matching.

Change-Id:I14f1cf105773f7fdd0cda7f7e20dd66a14334384*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 1ba6dfe..7e2b3de 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
    static final int EF_OPL = 0x6fc6;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index fc011c0..0ef7bc6 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
    protected RegistrantList mRecordsEventsRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -132,6 +133,15 @@
return null;
}

    public synchronized void registerForSIMEFUpdates(Handler h, int what, Object obj) {
        Registrant r = new Registrant (h, what, obj);
        mRecordsEventsRegistrants.add(r);
    }

    public synchronized void unregisterForSIMEFUpdates(Handler h) {
        mRecordsEventsRegistrants.remove(h);
    }

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/OperatorInfo.java b/telephony/java/com/android/internal/telephony/OperatorInfo.java
//Synthetic comment -- index 1999cb3..79f6d49 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
return state;
}

    public OperatorInfo(String operatorAlphaLong,
String operatorAlphaShort,
String operatorNumeric,
State state) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 94f7a13..b34391de 100644

//Synthetic comment -- @@ -96,6 +96,7 @@
protected static final int EVENT_SET_CLIR_COMPLETE              = 18;
protected static final int EVENT_REGISTERED_TO_NETWORK          = 19;
protected static final int EVENT_SET_VM_NUMBER_DONE             = 20;
    protected static final int EVENT_GET_NETWORKS_DONE              = 28;
// Events for CDMA support
protected static final int EVENT_GET_DEVICE_IDENTITY_DONE       = 21;
protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
//Synthetic comment -- @@ -103,6 +104,8 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
    //other
    protected static final int EVENT_ICC_RECORDS_EONS_UPDATED = 27;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/Eons.java b/telephony/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..428aba7

//Synthetic comment -- @@ -0,0 +1,295 @@
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

import android.util.Log;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.OperatorInfo;

import java.util.ArrayList;


/**
 * {@hide}
 */
class Eons {
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
    enum EonsState {
        INITING,
        DISABLED,
        PNN_PRESENT,
        PNN_AND_OPL_PRESENT;

        boolean isIniting() {
            return (this == INITING);
        }

        boolean isDisabled() {
            return (this == DISABLED);
        }

        boolean isPnnPresent() {
            return (this == PNN_PRESENT);
        }

        boolean isPnnAndOplPresent() {
            return (this == PNN_AND_OPL_PRESENT);
        }
    }

    enum EonsControlState {
        INITING,
        PRESENT,
        ABSENT;

        boolean isIniting() {
            return (this == INITING);
        }

        boolean isPresent() {
            return (this == PRESENT);
        }

        boolean isAbsent() {
            return (this == ABSENT);
        }
    }

    // ***** Instance Variables
    EonsControlState mPnnDataState = EonsControlState.INITING;
    EonsControlState mOplDataState = EonsControlState.INITING;
    OplRecords mOplRecords;
    PnnRecords mPnnRecords;

    // ***** Constructor
    Eons() {
        reset();
    }

    void reset() {
        mPnnDataState = EonsControlState.INITING;
        mOplDataState = EonsControlState.INITING;
        mOplRecords = null;
        mPnnRecords = null;
    }

    void setOplData(ArrayList <byte[]> records) {
        mOplDataState = EonsControlState.PRESENT;
        mOplRecords = new OplRecords(records);
    }

    void resetOplData() {
        mOplDataState = EonsControlState.ABSENT;
        mOplRecords = null;
    }

    void setPnnData(ArrayList <byte[]> records) {
        mPnnDataState = EonsControlState.PRESENT;
        mPnnRecords = new PnnRecords(records);
    }

    void resetPnnData() {
        mPnnDataState = EonsControlState.ABSENT;
        mPnnRecords = null;
    }

    /**
     * Get the EONS derived from EF_OPL/EF_PNN files for registered operator.
     * @return Enhanced Operator Name String (EONS) if it can be derived and
     * null otherwise.
     */
    String getEons() {
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
     * @return true if operator name display needs updating, false
     * otherwise
     */
    boolean updateEons(String regOperator, int lac, String hplmn) {
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
     * Get name from EF_PNN first record.
     * @return Long Name from PNNs first record.
     */
    String getNameFromFirstPnnRecord() {
        return mPnnRecords.getNameFromPnnRecord(1, false);
    }

    /**
     * Fetch EONS for Available Networks from EF_PNN data.
     * @param avlNetworks, ArrayList of Available Networks
     * @return ArrayList Available Networks with EONS if
     * success,otherwise null
     */
    ArrayList<OperatorInfo> getEonsForAvailableNetworks(ArrayList<OperatorInfo> avlNetworks) {
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
                 eonsNetworkNames.add (new OperatorInfo(pnnName, ni.getOperatorAlphaShort(),
                        ni.getOperatorNumeric(), ni.getState()));
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index e1f4c4b..8c3685a 100644

//Synthetic comment -- @@ -151,6 +151,7 @@

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
        mIccRecords.registerForSIMEFUpdates(this, EVENT_ICC_RECORDS_EONS_UPDATED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -204,6 +205,7 @@
//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mIccRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
            mIccRecords.unregisterForSIMEFUpdates(this); // EVENT_ICC_RECORDS_EONS_UPDATED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
//Synthetic comment -- @@ -979,7 +981,9 @@

public void
getAvailableNetworks(Message response) {
        Message msg;
        msg = obtainMessage(EVENT_GET_NETWORKS_DONE,response);
        mCM.getAvailableNetworks(msg);
}

/**
//Synthetic comment -- @@ -1189,6 +1193,18 @@

break;

            case EVENT_ICC_RECORDS_EONS_UPDATED:
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG, "EVENT_ICC_RECORDS_EONS_UPDATED exception "
                          + ar.exception);
                    break;
                }

                processIccEonsRecordsUpdated((Integer)ar.result);
                break;

case EVENT_GET_BASEBAND_VERSION_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1309,6 +1325,30 @@
}
break;

            case EVENT_GET_NETWORKS_DONE:
                ArrayList<OperatorInfo> eonsNetworkNames = null;

                ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    eonsNetworkNames = ((SIMRecords) mIccRecords).
                         getEonsForAvailableNetworks((ArrayList<OperatorInfo>)ar.result);
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
//Synthetic comment -- @@ -1334,6 +1374,17 @@
return false;
}

    private void processIccEonsRecordsUpdated(int eventCode) {
        switch (eventCode) {
            case SIMRecords.EVENT_SPN:
                mSST.updateSpnDisplay();
                break;
            case SIMRecords.EVENT_EONS:
                mSST.updateEons(1);
                break;
        }
    }

/**
* Used to track the settings upon completion of the network change.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 84127cf..9b5caf9 100644

//Synthetic comment -- @@ -482,6 +482,37 @@
cm.setRadioPower(false, null);
}

    boolean updateEons(int reason) {
        boolean needsUpdate = false;
        boolean ssUpdated = false;
        int lac = -1;

        // If updateEons is called as a consequence of EONS realted EFs refresh
        // (with reason 1), notify ServiceStateChanged registrants.
        // If updateEons is called as a consequence of Network State Change,
        // ServiceStateChanged registrants need not be notified here since it
        // will any way be done in pollStateDone.

        if (cellLoc != null) lac = cellLoc.getLac();
        needsUpdate =
            ((SIMRecords) (phone.mIccRecords)).updateEons(ss.getOperatorNumeric(), lac);
        if (needsUpdate) {
            String eonsLong = ((SIMRecords) (phone.mIccRecords)).getEons();
            if (eonsLong != null) {
                // Update operator long name with EONS Long.
                ss.setOperatorName(eonsLong, ss.getOperatorAlphaShort(),
                      ss.getOperatorNumeric());
                ssUpdated = true;
            }

            if (reason == 1) {
                phone.notifyServiceStateChanged(ss);
            }
            updateSpnDisplay();
        }
        return ssUpdated;
    }

protected void updateSpnDisplay() {
int rule = phone.mIccRecords.getDisplayRule(ss.getOperatorNumeric());
String spn = phone.mIccRecords.getServiceProviderName();
//Synthetic comment -- @@ -498,7 +529,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN
                && (ss.getState() == ServiceState.STATE_IN_SERVICE);
boolean showPlmn = !TextUtils.isEmpty(plmn) &&
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -836,10 +868,13 @@
mNetworkAttachedRegistrants.notifyRegistrants();
}

        Log.i(LOG_TAG,"Network State Changed, get EONS and update operator name display");
        boolean ret = updateEons(2);
        hasChanged = hasChanged || ret;

if (hasChanged) {
String operatorNumeric;


phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ALPHA,
ss.getOperatorAlphaLong());








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/OplRecords.java b/telephony/java/com/android/internal/telephony/gsm/OplRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..dac2481

//Synthetic comment -- @@ -0,0 +1,175 @@
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
class OplRecords {
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

    private int size() {
        return (mRecords != null) ? mRecords.size() : 0;
    }

    /**
     * Function to get PNN record number from matching OPL record for registered plmn.
     * @param operator, registered plmn (mcc+mnc)
     * @param lac, current lac
     * @param useLac, whether to match lac or not
     * @return returns PNN record number from matching OPL record.
     */
    int getMatchingPnnRecord(String operator, int lac, boolean useLac) {
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
    private static class OplRecord {
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

        private int getPnnRecordNumber() {
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java b/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..93d68cd

//Synthetic comment -- @@ -0,0 +1,151 @@
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
class PnnRecords {
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

    private static void log(String s) {
        Log.d(LOG_TAG, "[PnnRecords EONS] " + s);
    }

    private static void loge(String s) {
        Log.e(LOG_TAG, "[PnnRecords EONS] " + s);
    }

    private int size() {
        return (mRecords != null) ? mRecords.size() : 0;
    }

    String getCurrentEons() {
        return mCurrentEons;
    }

    /**
     * Function to get Full Name from given PNN record number.
     * @param pnnRecord, PNN record number
     * @param update, specifies whether to update currentEons or not
     * @return returns Full Name from given PNN record.
     */
    String getNameFromPnnRecord(int recordNumber, boolean update) {
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
    private static class PnnRecord {
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

        private String getFullName() {
            return mFullName;
        }

        private String getShortName() {
            return mShortName;
        }

        private String getAddlInfo() {
            return mAddlInfo;
        }

        public String toString() {
            return "Full Name=" + mFullName + ", Short Name=" + mShortName +
               ", Additional Info=" + mAddlInfo;
        }
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index e8d10f9..adb8dca 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
case EF_AD:
case EF_MBDN:
case EF_PNN:
        case EF_OPL:
case EF_SPDI:
case EF_SST:
case EF_CFIS:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 495b5bc..c34541c 100755

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
//Synthetic comment -- @@ -60,6 +61,8 @@

SpnOverride mSpnOverride;

    Eons mEons;

// ***** Cached SIM State; cleared on channel close

private String imsi;
//Synthetic comment -- @@ -95,6 +98,10 @@

// ***** Constants

    // Events to notify OTA refresh for EF_OPL/EF_PNN/EF_SPN
    static final int EVENT_SPN = 0;
    static final int EVENT_EONS = 1;

// Bitmasks for SPN display rules.
static final int SPN_RULE_SHOW_SPN  = 0x01;
static final int SPN_RULE_SHOW_PLMN = 0x02;
//Synthetic comment -- @@ -135,7 +142,6 @@
private static final int EVENT_GET_SPN_DONE = 12;
private static final int EVENT_GET_SPDI_DONE = 13;
private static final int EVENT_UPDATE_DONE = 14;
protected static final int EVENT_GET_SST_DONE = 17;
private static final int EVENT_GET_ALL_SMS_DONE = 18;
private static final int EVENT_MARK_SMS_READ_DONE = 19;
//Synthetic comment -- @@ -149,6 +155,9 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;
    private static final int EVENT_GET_ALL_OPL_RECORDS_DONE = 34;
    private static final int EVENT_GET_ALL_PNN_RECORDS_DONE = 35;
    private static final int EVENT_GET_SPN = 36;

// Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

//Synthetic comment -- @@ -179,6 +188,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

        mEons = new Eons();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -222,6 +233,7 @@
pnnHomeName = null;

adnCache.reset();
        mEons.reset();

phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, null);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, null);
//Synthetic comment -- @@ -894,28 +906,6 @@
}
break;

case EVENT_GET_ALL_SMS_DONE:
isRecordLoadResponse = true;

//Synthetic comment -- @@ -1093,6 +1083,56 @@
default:
super.handleMessage(msg);   // IccRecords handles generic record load responses

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
                pnnHomeName = mEons.getNameFromFirstPnnRecord();
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

                phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                // When device enters or exits Home Zone, certain operators update
                // EF_SPN file. This helps to know if the device is in Home Zone or
                // not. Hence SPN display should be updated on EF_SPN refresh.
                mRecordsEventsRegistrants.notifyResult(EVENT_SPN);
                break;
}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1122,9 +1162,27 @@
phone.getIccFileHandler().loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
            case EF_OPL:
                if (DBG) log("[EONS] SIM Refresh for EF_OPL");
                recordsToLoad++;
                phone.getIccFileHandler().loadEFLinearFixedAll(EF_OPL,
                      obtainMessage(EVENT_GET_ALL_OPL_RECORDS_DONE));
                break;
            case EF_PNN:
                if (DBG) log("[EONS] SIM Refresh for EF_PNN");
                recordsToLoad++;
                phone.getIccFileHandler().loadEFLinearFixedAll(EF_PNN,
                      obtainMessage(EVENT_GET_ALL_PNN_RECORDS_DONE));
                break;
            case EF_SPN:
                if (DBG) log("[EONS] SIM Refresh for EF_SPN");
                recordsToLoad++;
                phone.getIccFileHandler().loadEFTransparent(EF_SPN,
                        obtainMessage(EVENT_GET_SPN));
                break;
default:
                // For now, fetch all records if this is not
                // one of above handled files.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1352,7 +1410,12 @@
iccFh.loadEFTransparent(EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
recordsToLoad++;

        phone.getIccFileHandler().loadEFLinearFixedAll(EF_OPL,
              obtainMessage(EVENT_GET_ALL_OPL_RECORDS_DONE));
        recordsToLoad++;

        phone.getIccFileHandler().loadEFLinearFixedAll(EF_PNN,
              obtainMessage(EVENT_GET_ALL_PNN_RECORDS_DONE));
recordsToLoad++;

iccFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
//Synthetic comment -- @@ -1582,6 +1645,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

    /**
     * Get the EONS name derived from EF_OPL/EF_PNN or EF_CPHS_ONS/EF_CPHS_ONS_SHORT
     * files for registered operator.
     * @return Enhanced Operator Name String (EONS) if it can be derived and
     * null otherwise.
     */
    String getEons() {
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
    boolean updateEons(String regOperator, int lac) {
        return mEons.updateEons(regOperator, lac, getOperatorNumeric());
    }

    /**
     * Fetch EONS for Available Networks from EF_PNN data.
     * @param avlNetworks, ArrayList of Available Networks
     * @return ArrayList Available Networks with EONS if
     * success, otherwise null
     */
    ArrayList<OperatorInfo> getEonsForAvailableNetworks(ArrayList<OperatorInfo> avlNetworks) {
        return mEons.getEonsForAvailableNetworks(avlNetworks);
    }

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







