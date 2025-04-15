/*TelephonyProvider : Do not ignore seletion when quering "current"

- MMS uses "current" and apnName when quering MMSC, but TelephonyProvider ignores apnsName.
In result, MMS  would get wrong MMSC if there are two more apns for certain mcc/mnc in apns-conf.xml.

Change-Id:I58a9f8c7655b500687c77f5f96808557934b5e21*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/TelephonyProvider.java b/src/com/android/providers/telephony/TelephonyProvider.java
//Synthetic comment -- index 85bd0f6..404c015 100644

//Synthetic comment -- @@ -333,8 +333,8 @@

case URL_CURRENT: {
qb.appendWhere("current IS NOT NULL");
                // ignore the selection
                selection = null;
break;
}








