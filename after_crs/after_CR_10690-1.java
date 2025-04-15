/*SetLocaleByCarrier, only if its not an unknown carrier.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 3571f02..71b593f 100644

//Synthetic comment -- @@ -323,7 +323,7 @@
private void setLocaleByCarrier() {
String carrier = SystemProperties.get("ro.carrier");

        if (null == carrier || 0 == carrier.length() || "unknown".equals(carrier)) {
return;
}








