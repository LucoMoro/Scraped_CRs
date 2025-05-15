
//<Beginning of snippet n. 0>


static final int EF_SPN_CPHS = 0x6f14;
static final int EF_SPN_SHORT_CPHS = 0x6f18;
static final int EF_INFO_CPHS = 0x6f16;
    static final int EF_CSP_CPHS = 0x6f15;

// CDMA RUIM file ids from 3GPP2 C.S0023-0
static final int EF_CST = 0x6f32;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


public void unsetOnEcbModeExitResponse(Handler h){
mActivePhone.unsetOnEcbModeExitResponse(h);
}

    public boolean isCspPlmnEnabled() {
        return mActivePhone.isCspPlmnEnabled();
    }
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


Log.e(LOG_TAG, "Error! This functionality is not implemented for GSM.");
}

    public boolean isCspPlmnEnabled() {
        return mSIMRecords.isCspPlmnEnabled();
    }
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


case EF_SPN_CPHS:
case EF_SPN_SHORT_CPHS:
case EF_INFO_CPHS:
        case EF_CSP_CPHS:
return MF_SIM + DF_GSM;

case EF_PBR:

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


*  mCphsInfo[1] and mCphsInfo[2] is CPHS Service Table
*/
private byte[] mCphsInfo = null;
    boolean mCspPlmnEnabled = true;

byte[] efMWIS = null;
byte[] efCPHS_MWI =null;
private static final int EVENT_SET_MSISDN_DONE = 30;
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
    private static final int EVENT_GET_CSP_CPHS_DONE = 33;

// ***** Constructor

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
iccFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

        iccFh.loadEFTransparent(EF_CSP_CPHS,obtainMessage(EVENT_GET_CSP_CPHS_DONE));
        recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
iccFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
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

//<End of snippet n. 6>








