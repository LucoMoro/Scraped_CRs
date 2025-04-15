/*Telephony: SMS over IMS

Add RIL_REQUEST_IMS_REGISTRATION_STATE, RIL_REQUEST_IMS_SEND_SMS, and
RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED.  Add FORMAT_UNKNOWN to
SmsConstants.

Change-Id:I5188fc178ed6772904722eb882df7b5370bd296e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..a32f73d 100644

//Synthetic comment -- @@ -260,6 +260,8 @@
int RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU = 106;
int RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS = 107;
int RIL_REQUEST_VOICE_RADIO_TECH = 108;
    int RIL_REQUEST_IMS_REGISTRATION_STATE = 109;
    int RIL_REQUEST_IMS_SEND_SMS = 110;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;
//Synthetic comment -- @@ -297,4 +299,5 @@
int RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE = 1033;
int RIL_UNSOL_RIL_CONNECTED = 1034;
int RIL_UNSOL_VOICE_RADIO_TECH_CHANGED = 1035;
    int RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED = 1036;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsConstants.java b/telephony/java/com/android/internal/telephony/SmsConstants.java
//Synthetic comment -- index 1ccdc3b..2449108 100644

//Synthetic comment -- @@ -62,6 +62,12 @@
}

/**
     * Indicates unknown format SMS message.
     * @hide pending API council approval
     */
    public static final String FORMAT_UNKNOWN = "unknown";

    /**
* Indicates a 3GPP format SMS message.
* @hide pending API council approval
*/







