/*Enable OMH support in Android Telephony

Support the Open Mobile Handset initative in android telephony. As per
the OMH specification in 3GPP2, Data profiles defined for specific
services like LBS, TETHERED and UNSPECIFIED are defined in the
RUIM/CSIM cards. These profiles are read from the android telephony
and used for creating the data calls when the right radio technology
(CDMA/DO) is used and when the feature is enabled.

* Supports multiple profiles for a particular OMH service type

* Supports multiple OMH profiles with same priority

* In case of OMH, for all profiles supporting SUPL, arbitration
should choose the lowest priority profile when making data calls

* This feature supports the co-existance of other radio technologies in
an OMH enabled device. Even with the OMH feature enabled, the device
can operate with default profiles when OMH profiles are not (yet)
present.

Change-Id:Ic6314d584e10a1cd1712eb666d6fbc1747843f87*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DctConstants.java b/telephony/java/com/android/internal/telephony/DctConstants.java
//Synthetic comment -- index 10ac153..7611f7f 100644

//Synthetic comment -- @@ -91,7 +91,9 @@
public static final int CMD_SET_DEPENDENCY_MET = BASE + 31;
public static final int CMD_SET_POLICY_DATA_ENABLE = BASE + 32;
public static final int EVENT_ICC_CHANGED = BASE + 33;

/***** Constants *****/

public static final int APN_INVALID_ID = -1;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..b787589 100644

//Synthetic comment -- @@ -260,6 +260,7 @@
int RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU = 106;
int RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS = 107;
int RIL_REQUEST_VOICE_RADIO_TECH = 108;
int RIL_UNSOL_RESPONSE_BASE = 1000;
int RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED = 1000;
int RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED = 1001;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index f95e081..50b3c2b 100644

//Synthetic comment -- @@ -187,4 +187,19 @@
* Ignore RIL_UNSOL_NITZ_TIME_RECEIVED completely, used for debugging/testing.
*/
static final String PROPERTY_IGNORE_NITZ = "telephony.test.ignore.nitz";
}







