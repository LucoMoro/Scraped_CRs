//<Beginning of snippet n. 0>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
    isRecordLoadResponse = true;

    ar = (AsyncResult)msg.obj;

    if (ar.exception == null && ar.result != null) {
        VoicemailData voicemailData = (VoicemailData) ar.result; // Example structure
        if (isValid(voicemailData)) {
            voicemailNumber = voicemailData.getNumber();
            voicemailTag = voicemailData.getTag(); 
        } else {
            voicemailNumber = null;
            voicemailTag = null;
            logInvalidData("Received invalid voicemail data: " + voicemailData.toString());
        }
    } else {
        voicemailNumber = null;
        voicemailTag = null;
        logInvalidData("Error retrieving voicemail data: " + (ar.exception != null ? ar.exception.toString() : "unknown exception"));
    }
//<End of snippet n. 0>