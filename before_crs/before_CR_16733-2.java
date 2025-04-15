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

// CDMA RUIM file ids from 3GPP2 C.S0023-0
static final int EF_CST = 0x6f32;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 23325f6..3c9a1f8 100644

//Synthetic comment -- @@ -1708,4 +1708,15 @@
void unsetOnEcbModeExitResponse(Handler h);


}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 74601e6..d13c0f96 100644

//Synthetic comment -- @@ -1018,6 +1018,13 @@
}
}

/**
* Common error logger method for unexpected calls to CDMA-only methods.
*/
//Synthetic comment -- @@ -1026,4 +1033,13 @@
Log.e(LOG_TAG, "Error! " + name + "() in PhoneBase should not be " +
"called, CDMAPhone inactive.");
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index e1511e6..5e7dcb0 100644

//Synthetic comment -- @@ -839,4 +839,8 @@
public void unsetOnEcbModeExitResponse(Handler h){
mActivePhone.unsetOnEcbModeExitResponse(h);
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 69a7a57..6665632 100644

//Synthetic comment -- @@ -1486,4 +1486,7 @@
Log.e(LOG_TAG, "Error! This functionality is not implemented for GSM.");
}

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/telephony/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 206e62f..e8d10f9 100644

//Synthetic comment -- @@ -81,6 +81,7 @@
case EF_SPN_CPHS:
case EF_SPN_SHORT_CPHS:
case EF_INFO_CPHS:
return MF_SIM + DF_GSM;

case EF_PBR:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 30f38bd..c80c608 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
*  mCphsInfo[1] and mCphsInfo[2] is CPHS Service Table
*/
private byte[] mCphsInfo = null;

byte[] efMWIS = null;
byte[] efCPHS_MWI =null;
//Synthetic comment -- @@ -141,6 +142,7 @@
private static final int EVENT_SET_MSISDN_DONE = 30;
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;

// ***** Constructor

//Synthetic comment -- @@ -1002,6 +1004,22 @@
((GSMPhone) phone).notifyCallForwardingIndicator();
break;

}}catch (RuntimeException exc) {
// I don't want these exceptions to be fatal
Log.w(LOG_TAG, "Exception parsing SIM record", exc);
//Synthetic comment -- @@ -1025,6 +1043,12 @@
new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
break;
default:
// For now, fetch all records if this is not a
// voicemail number.
//Synthetic comment -- @@ -1255,6 +1279,9 @@
iccFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

// XXX should seek instead of examining them all
if (false) { // XXX
iccFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
//Synthetic comment -- @@ -1476,4 +1503,53 @@
Log.d(LOG_TAG, "[SIMRecords] " + s);
}

}







