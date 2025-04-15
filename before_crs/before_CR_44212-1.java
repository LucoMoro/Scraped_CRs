/*Frameworks: Add ADN capabilities for CDMA phone

CDMA can support Abbreviated Dialing Numbers so add
CDMA in TelephonyCapabilities.

Change-Id:I890d45962ff2a5248b4bf333c9c71643bea8e5dd*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/TelephonyCapabilities.java b/src/java/com/android/internal/telephony/TelephonyCapabilities.java
//Synthetic comment -- index a9e9376..c7d0cea 100644

//Synthetic comment -- @@ -175,7 +175,8 @@
* something more appropriate.
*/
public static boolean supportsAdn(int phoneType) {
        return phoneType == PhoneConstants.PHONE_TYPE_GSM;
}

/**







