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
            logInvalidData("Received invalid voicemail data: " + voicemailData.toString() + ", check data format and values.");
        }
    } else {
        voicemailNumber = null;
        voicemailTag = null;
        if (ar.exception != null) {
            logInvalidData("Error retrieving voicemail data: " + ar.exception.toString());
        } else {
            logInvalidData("Error retrieving voicemail data: unknown exception");
        }
    }
//<End of snippet n. 0>