/*SetLocaleByCarrier, only if its not an unknown carrier.

Change-Id:Ia218b3622a23cc0fc1bb2c1606c832ecc561d5bf*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index eb406b7..a2554ba 100644

//Synthetic comment -- @@ -540,7 +540,7 @@
private void setPropertiesByCarrier() {
String carrier = SystemProperties.get("ro.carrier");

        if (null == carrier || 0 == carrier.length() || "unknown".equals(carrier)) {
return;
}








