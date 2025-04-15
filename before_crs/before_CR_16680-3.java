/*Telephony: Fixed problem with short operator name

This patch solves the problem with erroneous service
provider name that is visible in status row. The name
of the service provider is one character short. This
happen when loading the service provider name from
EF_SPN_CPHS or EF_SPN_SHORT_CPHS.

Change-Id:Iccd15de347ef39a0d7def7aa01de7c82518a6a66*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 495b5bc..098e977 100755

//Synthetic comment -- @@ -1500,7 +1500,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1 );

if (DBG) log("Load EF_SPN_CPHS: " + spn);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);
//Synthetic comment -- @@ -1518,7 +1518,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1);

if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);







