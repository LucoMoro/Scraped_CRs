/*Telephony: Handle SIM Refresh for MSISDN, CFIS, CFF_CPHS

Change-Id:Ia34af5a5ff3b311ffde41b097b932ee8e66cd0c1*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..7a9fe9d 100755

//Synthetic comment -- @@ -1121,6 +1121,24 @@
mFh.loadEFTransparent(EF_CSP_CPHS,
obtainMessage(EVENT_GET_CSP_CPHS_DONE));
break;
            case EF_MSISDN:
                recordsToLoad++;
                Log.i(LOG_TAG,"SIM Refresh called for EF_MSISDN");
                new AdnRecordLoader(mFh).loadFromEF(EF_MSISDN, EF_EXT1, 1,
                        obtainMessage(EVENT_GET_MSISDN_DONE));
                break;
            case EF_CFIS:
                recordsToLoad++;
                Log.i(LOG_TAG,"SIM Refresh called for EF_CFIS");
                mFh.loadEFLinearFixed(EF_CFIS,
                        1, obtainMessage(EVENT_GET_CFIS_DONE));
                break;
            case EF_CFF_CPHS:
                recordsToLoad++;
                Log.i(LOG_TAG,"SIM Refresh called for EF_CFF_CPHS");
                mFh.loadEFTransparent(EF_CFF_CPHS,
                obtainMessage(EVENT_GET_CFF_DONE));
                break;
default:
// For now, fetch all records if this is not a
// voicemail number.







