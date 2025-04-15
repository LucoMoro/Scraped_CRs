/*Telephony: Enhanced Operator Name String (EONS) algorithm.

The operator name for registered PLMN should be displayed in the
following order of preference.

1) Name should be derived from EF_OPL/EF_PNN files.

2) Name should be derived from EF_CPHS_ONS/EF_CPHS_ONS_SHORT
or from NITZ messge. The order is not specified. In this
implementation, CPHS name is given priority over NITZ name.

3) Name from ME database.

4) Name from Network.

This algorithm implements this operator name deriving logic.
3GPP specs referred
1) TS 22.101 A.3 - operator name priority.
2) TS 31.102 - for EF data description.
3) TS 24.008 - for PLMN and LAC coding details.
4) TS 23.122 - for PLMN matching.

Change-Id:I922798b39a91b679da434cd2434d41c375c91a37*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index acc9197..3fe2212 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..4f5d8de 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -61,6 +62,8 @@
protected int spnDisplayCondition;

// ***** Constants

// Markers for mncLength
protected static final int UNINITIALIZED = -1;
//Synthetic comment -- @@ -99,6 +102,15 @@
recordsLoadedRegistrants.remove(h);
}

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 23325f6..ac7ba8c 100644

//Synthetic comment -- @@ -1708,4 +1708,11 @@
void unsetOnEcbModeExitResponse(Handler h);


}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 74601e6..6075868 100644

//Synthetic comment -- @@ -92,6 +92,7 @@
protected static final int EVENT_SET_CLIR_COMPLETE              = 18;
protected static final int EVENT_REGISTERED_TO_NETWORK          = 19;
protected static final int EVENT_SET_VM_NUMBER_DONE             = 20;
// Events for CDMA support
protected static final int EVENT_GET_DEVICE_IDENTITY_DONE       = 21;
protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
//Synthetic comment -- @@ -99,6 +100,8 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";
//Synthetic comment -- @@ -1018,6 +1021,13 @@
}
}

/**
* Common error logger method for unexpected calls to CDMA-only methods.
*/
//Synthetic comment -- @@ -1026,4 +1036,13 @@
Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, CDMAPhone inactive.");
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index e1511e6..0df0249 100644

//Synthetic comment -- @@ -839,4 +839,8 @@
public void unsetOnEcbModeExitResponse(Handler h){
mActivePhone.unsetOnEcbModeExitResponse(h);
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/Eons.java b/telephony/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..2c21246

//Synthetic comment -- @@ -0,0 +1,332 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 69a7a57..c995f99 100644

//Synthetic comment -- @@ -159,6 +159,7 @@

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mSIMRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -992,7 +993,9 @@

public void
getAvailableNetworks(Message response) {
        mCM.getAvailableNetworks(response);
}

/**
//Synthetic comment -- @@ -1234,6 +1237,18 @@

break;

case EVENT_GET_BASEBAND_VERSION_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1354,6 +1369,30 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1379,6 +1418,17 @@
return false;
}

/**
* Used to track the settings upon completion of the network change.
*/
//Synthetic comment -- @@ -1486,4 +1536,7 @@
Log.e(LOG_TAG, "Error! This functionality is not implemented for GSM.");
}

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..e4405cf 100644

//Synthetic comment -- @@ -575,10 +575,25 @@
cm.setRadioPower(false, null);
}

protected void updateSpnDisplay() {
int rule = phone.mSIMRecords.getDisplayRule(ss.getOperatorNumeric());
String spn = phone.mSIMRecords.getServiceProviderName();
        String plmn = ss.getOperatorAlphaLong();

// For emergency calls only, pass the EmergencyCallsOnly string via EXTRA_PLMN
if (mEmergencyOnly && cm.getRadioState().isOn()) {
//Synthetic comment -- @@ -590,7 +605,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
boolean showPlmn =
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -885,6 +901,8 @@

boolean hasLocationChanged = !newCellLoc.equals(cellLoc);

// Add an event log when connection state changes
if (ss.getState() != newSS.getState() || gprsState != newGPRSState) {
EventLog.writeEvent(EventLogTags.GSM_SERVICE_STATE_CHANGE,
//Synthetic comment -- @@ -932,7 +950,8 @@
if (hasChanged) {
String operatorNumeric;

            updateSpnDisplay();

phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ALPHA,
ss.getOperatorAlphaLong());
//Synthetic comment -- @@ -1026,6 +1045,10 @@
phone.notifyLocationChanged();
}

if (! isGprsConsistent(gprsState, ss.getState())) {
if (!mStartedGprsRegCheck && !mReportedGprsNoReg) {
mStartedGprsRegCheck = true;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/OplRecords.java b/telephony/java/com/android/internal/telephony/gsm/OplRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..db52c77

//Synthetic comment -- @@ -0,0 +1,176 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java b/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..d3cca3d

//Synthetic comment -- @@ -0,0 +1,152 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 206e62f..bfca0fd 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
case EF_AD:
case EF_MBDN:
case EF_PNN:
case EF_SPDI:
case EF_SST:
case EF_CFIS:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 30f38bd..7f173b6 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;

import java.util.ArrayList;

//Synthetic comment -- @@ -56,6 +57,8 @@

SpnOverride mSpnOverride;

// ***** Cached SIM State; cleared on channel close

String imsi;
//Synthetic comment -- @@ -141,6 +144,11 @@
private static final int EVENT_SET_MSISDN_DONE = 30;
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;

// ***** Constructor

//Synthetic comment -- @@ -152,6 +160,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -195,6 +205,7 @@
pnnHomeName = null;

adnCache.reset();

phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, null);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, null);
//Synthetic comment -- @@ -1002,6 +1013,83 @@
((GSMPhone) phone).notifyCallForwardingIndicator();
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1025,9 +1113,39 @@
new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
default:
                // For now, fetch all records if this is not a
                // voicemail number.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1249,6 +1367,22 @@
iccFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

iccFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1472,6 +1606,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







