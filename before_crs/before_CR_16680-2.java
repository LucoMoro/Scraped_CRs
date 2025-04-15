/*Telephony: Fixed problem with short operator name

There was problem when reading the length
of the operator name it was one character short.

Change-Id:Iccd15de347ef39a0d7def7aa01de7c82518a6a66*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 438996f..add0b52 100644

//Synthetic comment -- @@ -1417,7 +1417,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1 );

if (DBG) log("Load EF_SPN_CPHS: " + spn);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);
//Synthetic comment -- @@ -1435,7 +1435,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1);

if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
phone.setSystemProperty(PROPERTY_ICC_OPERATOR_ALPHA, spn);







