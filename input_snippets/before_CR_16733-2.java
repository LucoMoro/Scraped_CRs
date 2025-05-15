
//<Beginning of snippet n. 0>


static final int EF_SPN_CPHS = 0x6f14;
static final int EF_SPN_SHORT_CPHS = 0x6f18;
static final int EF_INFO_CPHS = 0x6f16;

// CDMA RUIM file ids from 3GPP2 C.S0023-0
static final int EF_CST = 0x6f32;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


void unsetOnEcbModeExitResponse(Handler h);


}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


}
}

/**
* Common error logger method for unexpected calls to CDMA-only methods.
*/
Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, CDMAPhone inactive.");
}
}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


public void unsetOnEcbModeExitResponse(Handler h){
mActivePhone.unsetOnEcbModeExitResponse(h);
}
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


Log.e(LOG_TAG, "Error! This functionality is not implemented for GSM.");
}

}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


case EF_SPN_CPHS:
case EF_SPN_SHORT_CPHS:
case EF_INFO_CPHS:
return MF_SIM + DF_GSM;

case EF_PBR:

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


*  mCphsInfo[1] and mCphsInfo[2] is CPHS Service Table
*/
private byte[] mCphsInfo = null;

byte[] efMWIS = null;
byte[] efCPHS_MWI =null;
private static final int EVENT_SET_MSISDN_DONE = 30;
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;

// ***** Constructor

((GSMPhone) phone).notifyCallForwardingIndicator();
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
default:
// For now, fetch all records if this is not a
// voicemail number.
iccFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
iccFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
Log.d(LOG_TAG, "[SIMRecords] " + s);
}

}

//<End of snippet n. 6>








