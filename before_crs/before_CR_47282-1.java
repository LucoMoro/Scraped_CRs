/*Telephony: Add new apn.sim.operator_numeric property

Use new property to filter apns. We need this new property
because icc.operator_numeric property is also used in cdma
and ApnSetting UI might display wrong set of apns if the
peroperty value used was set by RuimRecords

Change-Id:If515dea89bf5a1ae342a8f97b51bfce88d818477CRs-Fixed: 362846*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index f95e081..f807c50 100644

//Synthetic comment -- @@ -103,6 +103,12 @@
*/
static String PROPERTY_ICC_OPERATOR_NUMERIC = "gsm.sim.operator.numeric";

/** PROPERTY_ICC_OPERATOR_ALPHA is also known as the SPN, or Service Provider Name.
*  Availability: SIM state must be "READY"
*/







