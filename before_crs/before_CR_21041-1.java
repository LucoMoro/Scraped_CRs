/*Support for LTE network modes

Change-Id:I29daeb951a5a201a75908fef2a74fedfed73c1cb*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index e426e94..55b62cc 100644

//Synthetic comment -- @@ -193,6 +193,11 @@
int NT_MODE_EVDO_NO_CDMA = RILConstants.NETWORK_MODE_EVDO_NO_CDMA;
int NT_MODE_GLOBAL       = RILConstants.NETWORK_MODE_GLOBAL;

int PREFERRED_NT_MODE    = RILConstants.PREFERRED_NETWORK_MODE;










//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 2e391cb..d75e589 100644

//Synthetic comment -- @@ -137,15 +137,19 @@
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
return Phone.PHONE_TYPE_CDMA;

case RILConstants.NETWORK_MODE_WCDMA_PREF:
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
return Phone.PHONE_TYPE_GSM;

case RILConstants.NETWORK_MODE_GLOBAL:
return Phone.PHONE_TYPE_CDMA;
default:
return Phone.PHONE_TYPE_GSM;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af483db..4dd761f 100644

//Synthetic comment -- @@ -598,14 +598,18 @@
case RILConstants.NETWORK_MODE_GSM_ONLY:
case RILConstants.NETWORK_MODE_WCDMA_ONLY:
case RILConstants.NETWORK_MODE_GSM_UMTS:
mPhoneType = RILConstants.GSM_PHONE;
break;
case RILConstants.NETWORK_MODE_CDMA:
case RILConstants.NETWORK_MODE_CDMA_NO_EVDO:
case RILConstants.NETWORK_MODE_EVDO_NO_CDMA:
mPhoneType = RILConstants.CDMA_PHONE;
break;
case RILConstants.NETWORK_MODE_GLOBAL:
mPhoneType = RILConstants.CDMA_PHONE;
break;
default:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 888f721..1545805 100644

//Synthetic comment -- @@ -66,6 +66,10 @@
int NETWORK_MODE_EVDO_NO_CDMA   = 6; /* EvDo only */
int NETWORK_MODE_GLOBAL         = 7; /* GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL)
AVAILABLE Application Settings menu*/
int PREFERRED_NETWORK_MODE      = NETWORK_MODE_WCDMA_PREF;

/* CDMA subscription source. See ril.h RIL_REQUEST_CDMA_SET_SUBSCRIPTION */







