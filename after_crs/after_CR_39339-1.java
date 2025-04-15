/*Telephony: Fixed problem with short operator name

This patch solves the problem with erroneous service
provider name that is visible in status row. The name
of the service provider is one character short. This
happen when loading the service provider name from
EF_SPN_CPHS or EF_SPN_SHORT_CPHS.

Change-Id:I54df69b9d46bc90028581d16df72f1fd4757c913*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 1ec3ea7..a245620 100755

//Synthetic comment -- @@ -1525,8 +1525,7 @@
case READ_SPN_CPHS:
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
                    spn = IccUtils.adnStringFieldToString(data, 0, data.length);

if (DBG) log("Load EF_SPN_CPHS: " + spn);
SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);
//Synthetic comment -- @@ -1543,8 +1542,7 @@
case READ_SPN_SHORT_CPHS:
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
                    spn = IccUtils.adnStringFieldToString(data, 0, data.length);

if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);







