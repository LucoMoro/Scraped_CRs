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
//Synthetic comment -- index b12d2d4..095b58c 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
static final int EF_MWIS = 0x6FCA;
static final int EF_MBDN = 0x6fc7;
static final int EF_PNN = 0x6fc5;
static final int EF_SPN = 0x6F46;
static final int EF_SMS = 0x6F3C;
static final int EF_ICCID = 0x2fe2;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..81f5ebe 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -99,6 +100,15 @@
recordsLoadedRegistrants.remove(h);
}

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..7792fd6 100644

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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/Eons.java b/telephony/java/com/android/internal/telephony/gsm/Eons.java
new file mode 100644
//Synthetic comment -- index 0000000..5bc7195

//Synthetic comment -- @@ -0,0 +1,294 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..bea3b03 100644

//Synthetic comment -- @@ -159,6 +159,7 @@

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mSIMRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
mCM.registerForOn(this, EVENT_RADIO_ON, null);
mCM.setOnUSSD(this, EVENT_USSD, null);
//Synthetic comment -- @@ -211,6 +212,7 @@
//Unregister from all former registered events
mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
mSIMRecords.unregisterForRecordsLoaded(this); //EVENT_SIM_RECORDS_LOADED
mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
mCM.unregisterForOn(this); //EVENT_RADIO_ON
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
//Synthetic comment -- @@ -992,7 +994,9 @@

public void
getAvailableNetworks(Message response) {
        mCM.getAvailableNetworks(response);
}

/**
//Synthetic comment -- @@ -1235,6 +1239,18 @@

break;

case EVENT_GET_BASEBAND_VERSION_DONE:
ar = (AsyncResult)msg.obj;

//Synthetic comment -- @@ -1355,6 +1371,30 @@
}
break;

default:
super.handleMessage(msg);
}
//Synthetic comment -- @@ -1380,6 +1420,17 @@
return false;
}

/**
* Used to track the settings upon completion of the network change.
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..b8e66de 100644

//Synthetic comment -- @@ -575,6 +575,36 @@
cm.setRadioPower(false, null);
}

protected void updateSpnDisplay() {
int rule = phone.mSIMRecords.getDisplayRule(ss.getOperatorNumeric());
String spn = phone.mSIMRecords.getServiceProviderName();
//Synthetic comment -- @@ -590,7 +620,8 @@
|| !TextUtils.equals(spn, curSpn)
|| !TextUtils.equals(plmn, curPlmn)) {
boolean showSpn = !mEmergencyOnly
                && (rule & SIMRecords.SPN_RULE_SHOW_SPN) == SIMRecords.SPN_RULE_SHOW_SPN;
boolean showPlmn =
(rule & SIMRecords.SPN_RULE_SHOW_PLMN) == SIMRecords.SPN_RULE_SHOW_PLMN;

//Synthetic comment -- @@ -929,10 +960,13 @@
networkAttachedRegistrants.notifyRegistrants();
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
//Synthetic comment -- index c80c608..5e4e0c9 100644

//Synthetic comment -- @@ -56,6 +56,8 @@

SpnOverride mSpnOverride;

// ***** Cached SIM State; cleared on channel close

String imsi;
//Synthetic comment -- @@ -89,6 +91,10 @@

// ***** Constants

// Bitmasks for SPN display rules.
static final int SPN_RULE_SHOW_SPN  = 0x01;
static final int SPN_RULE_SHOW_PLMN = 0x02;
//Synthetic comment -- @@ -129,7 +135,6 @@
private static final int EVENT_GET_SPN_DONE = 12;
private static final int EVENT_GET_SPDI_DONE = 13;
private static final int EVENT_UPDATE_DONE = 14;
    private static final int EVENT_GET_PNN_DONE = 15;
private static final int EVENT_GET_SST_DONE = 17;
private static final int EVENT_GET_ALL_SMS_DONE = 18;
private static final int EVENT_MARK_SMS_READ_DONE = 19;
//Synthetic comment -- @@ -143,6 +148,9 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;

// ***** Constructor

//Synthetic comment -- @@ -154,6 +162,8 @@
mVmConfig = new VoiceMailConstants();
mSpnOverride = new SpnOverride();

recordsRequested = false;  // No load request is made till SIM ready

// recordsToLoad is set to 0 because no requests are made yet
//Synthetic comment -- @@ -197,6 +207,7 @@
pnnHomeName = null;

adnCache.reset();

phone.setSystemProperty(PROPERTY_ICC_OPERATOR_NUMERIC, null);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, null);
//Synthetic comment -- @@ -825,28 +836,6 @@
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

//Synthetic comment -- @@ -1020,6 +1009,56 @@
handleEfCspData(data);
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1049,9 +1088,27 @@
phone.getIccFileHandler().loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
                // For now, fetch all records if this is not a
                // voicemail number.
// TODO: Handle other cases, instead of fetching all.
adnCache.reset();
fetchSimRecords();
//Synthetic comment -- @@ -1270,7 +1327,12 @@
iccFh.loadEFTransparent(EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
recordsToLoad++;

        iccFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
recordsToLoad++;

iccFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
//Synthetic comment -- @@ -1499,6 +1561,38 @@
return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
}

protected void log(String s) {
Log.d(LOG_TAG, "[SIMRecords] " + s);
}







