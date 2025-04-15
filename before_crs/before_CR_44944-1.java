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

public static final int EVENT_GET_ICC_RECORD_DONE = 100;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/OperatorInfo.java b/src/java/com/android/internal/telephony/OperatorInfo.java
//Synthetic comment -- index 1999cb3..79f6d49 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
return state;
}

    OperatorInfo(String operatorAlphaLong,
String operatorAlphaShort,
String operatorNumeric,
State state) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1f69f7a..307b54b 100755

//Synthetic comment -- @@ -114,6 +114,7 @@
protected static final int EVENT_NEW_ICC_SMS                    = 29;
protected static final int EVENT_ICC_RECORD_EVENTS              = 30;
protected static final int EVENT_ICC_CHANGED                    = 31;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/ServiceStateTracker.java b/src/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index 090e38d..a650b7b 100755

//Synthetic comment -- @@ -140,6 +140,7 @@
protected static final int EVENT_CDMA_PRL_VERSION_CHANGED          = 40;
protected static final int EVENT_RADIO_ON                          = 41;
protected static final int EVENT_ICC_CHANGED                       = 42;

protected static final String TIMEZONE_PROPERTY = "persist.sys.timezone";









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UsimFileHandler.java b/src/java/com/android/internal/telephony/UsimFileHandler.java
//Synthetic comment -- index 5ef0333..ee79f20 100644

//Synthetic comment -- @@ -44,8 +44,8 @@
case EF_SPN:
case EF_AD:
case EF_MBDN:
        case EF_PNN:
case EF_OPL:
case EF_SPDI:
case EF_SST:
case EF_CFIS:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/Eons.java b/src/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..d7de1ab

//Synthetic comment -- @@ -0,0 +1,288 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c1cd019..30de3d2 100644

//Synthetic comment -- @@ -113,6 +113,10 @@
SimSmsInterfaceManager mSimSmsIntManager;
PhoneSubInfo mSubInfo;


Registrant mPostDialHandler;

//Synthetic comment -- @@ -985,7 +989,13 @@

public void
getAvailableNetworks(Message response) {
        mCM.getAvailableNetworks(response);
}

/**
//Synthetic comment -- @@ -1332,6 +1342,34 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1375,6 +1413,14 @@
case IccRecords.EVENT_MWI:
notifyMessageWaitingIndicator();
break;
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..5146938 100755

//Synthetic comment -- @@ -152,6 +152,13 @@
/** waiting period before recheck gprs and voice registration. */
static final int DEFAULT_GPRS_CHECK_PERIOD_MILLIS = 60 * 1000;

/** Notification type. */
static final int PS_ENABLED = 1001;            // Access Control blocks data service
static final int PS_DISABLED = 1002;           // Access Control enables data service
//Synthetic comment -- @@ -235,6 +242,7 @@

// Gsm doesn't support OTASP so its not needed
phone.notifyOtaspChanged(OTASP_NOT_NEEDED);
}

public void dispose() {
//Synthetic comment -- @@ -244,7 +252,10 @@
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);
if (mUiccApplcation != null) {mUiccApplcation.unregisterForReady(this);}
        if (mIccRecords != null) {mIccRecords.unregisterForRecordsLoaded(this);}
cm.unSetOnSignalStrengthUpdate(this);
cm.unSetOnRestrictedStateChanged(this);
cm.unSetOnNITZTime(this);
//Synthetic comment -- @@ -389,6 +400,11 @@
updateSpnDisplay();
break;

case EVENT_LOCATION_UPDATES_ENABLED:
ar = (AsyncResult) msg.obj;

//Synthetic comment -- @@ -483,6 +499,47 @@
cm.setRadioPower(false, null);
}

protected void updateSpnDisplay() {
if (mIccRecords == null) {
return;
//Synthetic comment -- @@ -502,7 +559,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
boolean showPlmn = !TextUtils.isEmpty(plmn) &&
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -525,6 +583,18 @@
curPlmn = plmn;
}

/**
* Handle the result of one of the pollState()-related requests
*/
//Synthetic comment -- @@ -623,6 +693,10 @@
if (states.length >= 6) {
mNewMaxDataCalls = Integer.parseInt(states[5]);
}
} catch (NumberFormatException ex) {
loge("error parsing GprsRegistrationState: " + ex);
}
//Synthetic comment -- @@ -631,6 +705,9 @@
mDataRoaming = regCodeIsRoaming(regState);
mNewRilRadioTechnology = type;
newSS.setRadioTechnology(type);
break;

case EVENT_POLL_STATE_OPERATOR:
//Synthetic comment -- @@ -833,6 +910,14 @@
mNitzUpdatedTime = false;
}

if (hasChanged) {
String operatorNumeric;

//Synthetic comment -- @@ -1632,6 +1717,7 @@
mUiccApplcation.unregisterForReady(this);
if (mIccRecords != null) {
mIccRecords.unregisterForRecordsLoaded(this);
}
mIccRecords = null;
mUiccApplcation = null;
//Synthetic comment -- @@ -1643,6 +1729,7 @@
mUiccApplcation.registerForReady(this, EVENT_SIM_READY, null);
if (mIccRecords != null) {
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
}
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/OplRecords.java b/src/java/com/android/internal/telephony/gsm/OplRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..179b625

//Synthetic comment -- @@ -0,0 +1,176 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/PnnRecords.java b/src/java/com/android/internal/telephony/gsm/PnnRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..5b53e92

//Synthetic comment -- @@ -0,0 +1,152 @@








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0387a70..97a43f9 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
case EF_SPN:
case EF_AD:
case EF_MBDN:
case EF_PNN:
case EF_SPDI:
case EF_SST:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..1115c59 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
//Synthetic comment -- @@ -38,11 +39,13 @@
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;
import com.android.internal.telephony.UiccCardApplication;

import java.util.ArrayList;

//Synthetic comment -- @@ -64,6 +67,12 @@

SpnOverride mSpnOverride;

// ***** Cached SIM State; cleared on channel close

private boolean callForwardingEnabled;
//Synthetic comment -- @@ -151,6 +160,9 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;

// Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

//Synthetic comment -- @@ -183,6 +195,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -226,6 +240,7 @@
pnnHomeName = null;

adnCache.reset();

log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, null);
//Synthetic comment -- @@ -1089,6 +1104,56 @@
handleEfCspData(data);
break;

default:
super.handleMessage(msg);   // IccRecords handles generic record load responses

//Synthetic comment -- @@ -1121,9 +1186,33 @@
mFh.loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
                // For now, fetch all records if this is not a
                // voicemail number.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1352,6 +1441,14 @@
mFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

mFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
recordsToLoad++;

//Synthetic comment -- @@ -1595,6 +1692,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







