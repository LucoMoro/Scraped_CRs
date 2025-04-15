/*Telephony: Fixed problem with short operator name

There was problem when reading the length
of the operator name it was one character short.

Change-Id:Iccd15de347ef39a0d7def7aa01de7c82518a6a66*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index 9d2fd89..55cc406 100644

//Synthetic comment -- @@ -1399,7 +1399,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
String spnCphsTmp = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1 );
if (spnCphsTmp == null || spnCphsTmp.length() == 0) {
phone.getIccFileHandler().loadEFTransparent(
EF_SPN_SHORT_CPHS, obtainMessage(EVENT_GET_SPN_DONE));
//Synthetic comment -- @@ -1427,7 +1427,7 @@
if (ar != null && ar.exception == null) {
data = (byte[]) ar.result;
String spnShortCphsTmp = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1);

if (spnShortCphsTmp == null || spnShortCphsTmp.length() == 0) {
if (DBG) log("No SPN loaded in either CHPS or 3GPP");







