/*frameworks/base: Support for LTE network modes

Change-Id:I93c102f92ff2d10856d91108bb50fe5c0f06ad66*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/TelephonyManager.java b/telephony/java/android/telephony/TelephonyManager.java
//Synthetic comment -- index fa4b7cd..229b4a3 100755

//Synthetic comment -- @@ -376,6 +376,8 @@
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
return PhoneConstants.PHONE_TYPE_GSM;

// Use CDMA Phone for the global mode including CDMA








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..077ad68 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
int NETWORK_MODE_LTE_GSM_WCDMA  = 9; /* LTE, GSM/WCDMA */
int NETWORK_MODE_LTE_CMDA_EVDO_GSM_WCDMA = 10; /* LTE, CDMA, EvDo, GSM/WCDMA */
int NETWORK_MODE_LTE_ONLY       = 11; /* LTE Only mode. */

int PREFERRED_NETWORK_MODE      = NETWORK_MODE_WCDMA_PREF;

int CDMA_CELL_BROADCAST_SMS_DISABLED = 1;







