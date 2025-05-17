//<Beginning of snippet n. 0>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
    isRecordLoadResponse = true;

    ar = (AsyncResult) msg.obj;

    if (ar == null || ar.exception != null || ar.result == null || !isValidMbdnData((MbdnData) ar.result)) {
        Log.e(TAG, "Invalid MBDN EF data received. Skipping update.");
        voicemailNumber = null;
        voicemailTag = null;
        break;
    }

    MbdnData mbdnData = (MbdnData) ar.result;
    voicemailNumber = mbdnData.getMailboxNumber();
    voicemailTag = mbdnData.getTag();
    Log.i(TAG, "Updated voicemail fields successfully.");

//<End of snippet n. 0>