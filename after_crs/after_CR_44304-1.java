/*Telephony: IP Version SystemProperty for 3gpp2

Include a means to configure the IP version of the 3gpp2
radio techs

Change-Id:I7c509a1aca12260ecc27b216394e76c8fc65e063*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnection.java
//Synthetic comment -- index 8761828..9ff51cb 100644

//Synthetic comment -- @@ -91,7 +91,7 @@
Integer.toString(dataProfile),
null, null, null,
Integer.toString(RILConstants.SETUP_DATA_AUTH_PAP_CHAP),
                mApn.protocol, msg);
}

@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index c0dbf9f..af0ead1 100644

//Synthetic comment -- @@ -384,8 +384,11 @@
types = mDefaultApnTypes;
apnId = mDefaultApnId;
}

        String ipProto = SystemProperties.get("persist.telephony.cdma.protocol", "IP");
        String roamingIpProto = SystemProperties.get("persist.telephony.cdma.rproto", "IP");
mActiveApn = new ApnSetting(apnId, "", "", "", "", "", "", "", "", "",
                                    "", 0, types, ipProto, roamingIpProto, true, 0);
if (DBG) log("call conn.bringUp mActiveApn=" + mActiveApn);

Message msg = obtainMessage();







