/*Telephony: Handle SIM Refresh for MSISDN, CFIS, CFF_CPHS

Change-Id:Ia34af5a5ff3b311ffde41b097b932ee8e66cd0c1*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..7a9fe9d 100755

//Synthetic comment -- @@ -1121,6 +1121,24 @@
mFh.loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
default:
// For now, fetch all records if this is not a
// voicemail number.







