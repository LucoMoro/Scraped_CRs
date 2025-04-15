/*Telephony: Add null checks in CdmaDataConnection

Change-Id:Ie8001543e7d2967c58b7701f324a011e2a5b8aa9*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8761828..79114b1 100644

//Synthetic comment -- @@ -75,7 +75,9 @@
lastFailTime = -1;
lastFailCause = FailCause.NONE;
int dataProfile;
        if ((cp.apn != null) && (cp.apn.types.length > 0) && (cp.apn.types[0] != null) &&
(cp.apn.types[0].equals(PhoneConstants.APN_TYPE_DUN))) {
if (DBG) log("CdmaDataConnection using DUN");
dataProfile = RILConstants.DATA_PROFILE_TETHERED;







