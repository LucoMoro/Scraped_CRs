/*Telephony: Control operator selection using EF_CSP data.

As per CPHS4_2.WW6, CPHS B.4.7.1, the most significant bit of
Value Added Services Group(0xC0) info, controls operator selection menu.
   -If this bit is set, display operator selection menu to user.
   -If this bit is not set do not display operator selection menu to user,
	 set Network Selection Mode to Automatic.

Change-Id:Icca4898abced0b0beb94c3434448a26eae72008b*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index acc9197..b12d2d4 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
static final int EF_SPN_CPHS = 0x6f14;
static final int EF_SPN_SHORT_CPHS = 0x6f18;
static final int EF_INFO_CPHS = 0x6f16;
    static final int EF_CSP_CPHS = 0x6f15;

// CDMA RUIM file ids from 3GPP2 C.S0023-0
static final int EF_CST = 0x6f32;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 23325f6..3c9a1f8 100644

//Synthetic comment -- @@ -1708,4 +1708,15 @@
void unsetOnEcbModeExitResponse(Handler h);


    /**
     * TODO: Adding a function for each property is not good.
     * A fucntion of type getPhoneProp(propType) where propType is an
     * enum of GSM+CDMA+LTE props would be a better approach.
     *
     * Get "Restriction of menu options for manual PLMN selection" bit
     * status from EF_CSP data, this belongs to "Value Added Services Group".
     * @return true if this bit is set or EF_CSP data is unavailable,
     * false otherwise
     */
    boolean isCspPlmnEnabled();
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 74601e6..d13c0f96 100644

//Synthetic comment -- @@ -1018,6 +1018,13 @@
}
}

    public boolean isCspPlmnEnabled() {
        // This function should be overridden by the class GSMPhone.
        // Not implemented in CDMAPhone.
        logUnexpectedGsmMethodCall("isCspPlmnEnabled");
        return false;
    }

/**
* Common error logger method for unexpected calls to CDMA-only methods.
*/
//Synthetic comment -- @@ -1026,4 +1033,13 @@
Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, CDMAPhone inactive.");
}

    /**
     * Common error logger method for unexpected calls to GSM/WCDMA-only methods.
     */
    private void logUnexpectedGsmMethodCall(String name)
    {
        Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
                "called, GSMPhone inactive.");
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index e1511e6..5e7dcb0 100644

//Synthetic comment -- @@ -839,4 +839,8 @@
public void unsetOnEcbModeExitResponse(Handler h){
mActivePhone.unsetOnEcbModeExitResponse(h);
}

    public boolean isCspPlmnEnabled() {
        return mActivePhone.isCspPlmnEnabled();
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 69a7a57..6665632 100644

//Synthetic comment -- @@ -1486,4 +1486,7 @@
Log.e(LOG_TAG, "Error! This functionality is not implemented for GSM.");
}

    public boolean isCspPlmnEnabled() {
        return mSIMRecords.isCspPlmnEnabled();
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 206e62f..e8d10f9 100644

//Synthetic comment -- @@ -81,6 +81,7 @@
case EF_SPN_CPHS:
case EF_SPN_SHORT_CPHS:
case EF_INFO_CPHS:
        case EF_CSP_CPHS:
return MF_SIM + DF_GSM;

case EF_PBR:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 30f38bd..c80c608 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
*  mCphsInfo[1] and mCphsInfo[2] is CPHS Service Table
*/
private byte[] mCphsInfo = null;
    boolean mCspPlmnEnabled = true;

byte[] efMWIS = null;
byte[] efCPHS_MWI =null;
//Synthetic comment -- @@ -141,6 +142,7 @@
private static final int EVENT_SET_MSISDN_DONE = 30;
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
    private static final int EVENT_GET_CSP_CPHS_DONE = 33;

// ***** Constructor

//Synthetic comment -- @@ -1002,6 +1004,22 @@
((GSMPhone) phone).notifyCallForwardingIndicator();
break;

            case EVENT_GET_CSP_CPHS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG,"Exception in fetching EF_CSP data " + ar.exception);
                    break;
                }

                data = (byte[])ar.result;

                Log.i(LOG_TAG,"EF_CSP: " + IccUtils.bytesToHexString(data));
                handleEfCspData(data);
                break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1025,6 +1043,12 @@
new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
            case EF_CSP_CPHS:
                recordsToLoad++;
                Log.i(LOG_TAG, "[CSP] SIM Refresh for EF_CSP_CPHS");
                phone.getIccFileHandler().loadEFTransparent(EF_CSP_CPHS,
                        obtainMessage(EVENT_GET_CSP_CPHS_DONE));
                break;
default:
// For now, fetch all records if this is not a
// voicemail number.
//Synthetic comment -- @@ -1255,6 +1279,9 @@
iccFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_CSP_CPHS,obtainMessage(EVENT_GET_CSP_CPHS_DONE));
        recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
iccFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
//Synthetic comment -- @@ -1476,4 +1503,53 @@
Log.d(LOG_TAG, "[SIMRecords] " + s);
}

    /**
     * Return true if "Restriction of menu options for manual PLMN selection"
     * bit is set or EF_CSP data is unavailable, return false otherwise.
     */
    public boolean isCspPlmnEnabled() {
        return mCspPlmnEnabled;
    }

    /**
     * Parse EF_CSP data and check if
     * "Restriction of menu options for manual PLMN selection" is
     * Enabled/Disabled
     *
     * @param data EF_CSP hex data.
     */
    private void handleEfCspData(byte[] data) {
        // As per spec CPHS4_2.WW6, CPHS B.4.7.1, EF_CSP contains CPHS defined
        // 18 bytes (i.e 9 service groups info) and additional data specific to
        // operator. The valueAddedServicesGroup is not part of standard
        // services. This is operator specific and can be programmed any where.
        // Normally this is programmed as 10th service after the standard
        // services.
        int usedCspGroups = data.length / 2;
        // This is the "Servive Group Number" of "Value Added Services Group".
        byte valueAddedServicesGroup = (byte)0xC0;

        mCspPlmnEnabled = true;
        for (int i = 0; i < usedCspGroups; i++) {
             if (data[2 * i] == valueAddedServicesGroup) {
                 Log.i(LOG_TAG, "[CSP] found ValueAddedServicesGroup, value "
                       + data[(2 * i) + 1]);
                 if ((data[(2 * i) + 1] & 0x80) == 0x80) {
                     // Bit 8 is for
                     // "Restriction of menu options for manual PLMN selection".
                     // Operator Selection menu should be enabled.
                     mCspPlmnEnabled = true;
                 } else {
                     mCspPlmnEnabled = false;
                     // Operator Selection menu should be disabled.
                     // Operator Selection Mode should be set to Automatic.
                     Log.i(LOG_TAG,"[CSP] Set Automatic Network Selection");
                     phone.setNetworkSelectionModeAutomatic(null);
                 }
                 return;
             }
        }

        Log.w(LOG_TAG, "[CSP] Value Added Service Group (0xC0), not found!");
    }
}







