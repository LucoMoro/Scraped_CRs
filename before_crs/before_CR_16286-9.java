/*Telephony: Enhanced Operator Name String (EONS) algorithm.

The operator name for registered PLMN should be displayed in the
following order of preference.

1) Name should be derived from EF_OPL/EF_PNN files.

2) NITZ name.

3) Name from ME Database.

4) Name from Network.

This algorithm implements this operator name deriving logic.
3GPP specs referred
1) TS 22.101 A.3 - operator name priority.
2) TS 31.102 - for EF data description.
3) TS 24.008 - for PLMN and LAC coding details.
4) TS 23.122 - for PLMN matching.

Change-Id:I4f0a29bdaa46376f9cbb8adc16170de56b10989a*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 1ba6dfe..7e2b3de 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index fc011c0..0ef7bc6 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -132,6 +133,15 @@
return null;
}

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/OperatorInfo.java b/telephony/java/com/android/internal/telephony/OperatorInfo.java
//Synthetic comment -- index 1999cb3..79f6d49 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
return state;
}

    OperatorInfo(String operatorAlphaLong,
String operatorAlphaShort,
String operatorNumeric,
State state) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 94f7a13..ee0e95a 100644

//Synthetic comment -- @@ -103,6 +103,9 @@
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/Eons.java b/telephony/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..428aba7

//Synthetic comment -- @@ -0,0 +1,295 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 5c95e7d..a456f9b 100644

//Synthetic comment -- @@ -151,6 +151,7 @@

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -204,6 +205,7 @@
//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mIccRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
//Synthetic comment -- @@ -979,7 +981,9 @@

public void
getAvailableNetworks(Message response) {
        mCM.getAvailableNetworks(response);
}

/**
//Synthetic comment -- @@ -1189,6 +1193,18 @@

break;

case EVENT_GET_BASEBAND_VERSION_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1309,6 +1325,30 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1334,6 +1374,17 @@
return false;
}

/**
* Used to track the settings upon completion of the network change.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6e2b262d..9ff5f64 100644

//Synthetic comment -- @@ -482,6 +482,37 @@
cm.setRadioPower(false, null);
}

protected void updateSpnDisplay() {
int rule = phone.mIccRecords.getDisplayRule(ss.getOperatorNumeric());
String spn = phone.mIccRecords.getServiceProviderName();
//Synthetic comment -- @@ -498,7 +529,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly && !TextUtils.isEmpty(spn)
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
boolean showPlmn = !TextUtils.isEmpty(plmn) &&
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -838,10 +870,13 @@
mNetworkAttachedRegistrants.notifyRegistrants();
}

if (hasChanged) {
String operatorNumeric;

            updateSpnDisplay();

phone.setSystemProperty(TelephonyProperties.PROPERTY_OPERATOR_ALPHA,
ss.getOperatorAlphaLong());








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/OplRecords.java b/telephony/java/com/android/internal/telephony/gsm/OplRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..dac2481

//Synthetic comment -- @@ -0,0 +1,175 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java b/telephony/java/com/android/internal/telephony/gsm/PnnRecords.java
new file mode 100644
//Synthetic comment -- index 0000000..93d68cd

//Synthetic comment -- @@ -0,0 +1,151 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index e8d10f9..adb8dca 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
case EF_AD:
case EF_MBDN:
case EF_PNN:
case EF_SPDI:
case EF_SST:
case EF_CFIS:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 495b5bc..c34541c 100755

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
//Synthetic comment -- @@ -60,6 +61,8 @@

SpnOverride mSpnOverride;

// ***** Cached SIM State; cleared on channel close

private String imsi;
//Synthetic comment -- @@ -95,6 +98,10 @@

// ***** Constants

// Bitmasks for SPN display rules.
static final int SPN_RULE_SHOW_SPN  = 0x01;
static final int SPN_RULE_SHOW_PLMN = 0x02;
//Synthetic comment -- @@ -135,7 +142,6 @@
private static final int EVENT_GET_SPN_DONE = 12;
private static final int EVENT_GET_SPDI_DONE = 13;
private static final int EVENT_UPDATE_DONE = 14;
    private static final int EVENT_GET_PNN_DONE = 15;
protected static final int EVENT_GET_SST_DONE = 17;
private static final int EVENT_GET_ALL_SMS_DONE = 18;
private static final int EVENT_MARK_SMS_READ_DONE = 19;
//Synthetic comment -- @@ -149,6 +155,9 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;

// Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

//Synthetic comment -- @@ -179,6 +188,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -222,6 +233,7 @@
pnnHomeName = null;

adnCache.reset();

phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, null);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, null);
//Synthetic comment -- @@ -894,28 +906,6 @@
}
break;

            case EVENT_GET_PNN_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

                SimTlv tlv = new SimTlv(data, 0, data.length);

                for ( ; tlv.isValidObject() ; tlv.nextObject()) {
                    if (tlv.getTag() == TAG_FULL_NETWORK_NAME) {
                        pnnHomeName
                            = IccUtils.networkNameToString(
                                tlv.getData(), 0, tlv.getData().length);
                        break;
                    }
                }
            break;

case EVENT_GET_ALL_SMS_DONE:
isRecordLoadResponse = true;

//Synthetic comment -- @@ -1093,6 +1083,56 @@
default:
super.handleMessage(msg);   // IccRecords handles generic record load responses

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1122,9 +1162,27 @@
phone.getIccFileHandler().loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
                // For now, fetch all records if this is not a
                // voicemail number.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1352,7 +1410,12 @@
iccFh.loadEFTransparent(EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
recordsToLoad++;

        iccFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

iccFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
//Synthetic comment -- @@ -1582,6 +1645,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







