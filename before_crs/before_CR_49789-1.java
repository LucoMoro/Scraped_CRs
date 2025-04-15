/*Make CellInfo.TYPE_x public and add CELL_INFO request/unsol.

Change-Id:I9c63edd18d347bc924eac48600b092c24ecd42d4*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellInfo.java b/telephony/java/android/telephony/CellInfo.java
//Synthetic comment -- index f367f99..e808865 100644

//Synthetic comment -- @@ -26,11 +26,11 @@

// Type fields for parceling
/** @hide */
    protected static final int TYPE_GSM = 1;
/** @hide */
    protected static final int TYPE_CDMA = 2;
/** @hide */
    protected static final int TYPE_LTE = 3;

// Type to distinguish where time stamp gets recorded.









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..b7516d7 100644

//Synthetic comment -- @@ -260,6 +260,8 @@
int RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU = 106;
int RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS = 107;
int RIL_REQUEST_VOICE_RADIO_TECH = 108;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -297,4 +299,5 @@
int RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE = 1033;
int RIL_UNSOL_RIL_CONNECTED = 1034;
int RIL_UNSOL_VOICE_RADIO_TECH_CHANGED = 1035;
}







