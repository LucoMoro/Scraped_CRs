//<Beginning of snippet n. 0>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
    isRecordLoadResponse = true;

    ar = (AsyncResult)msg.obj;

    if (ar.exception != null) {
        voicemailNumber = null;
        voicemailTag = null;
        // Handle error: log or notify user
    } else if (ar.result instanceof YourExpectedDataType) {
        YourExpectedDataType data = (YourExpectedDataType) ar.result;
        
        if (isValidData(data)) {
            voicemailNumber = data.getVoicemailNumber();
            voicemailTag = data.getVoicemailTag();
        } else {
            voicemailNumber = null;
            voicemailTag = null;
        }
    } else {
        voicemailNumber = null;
        voicemailTag = null;
    }

//<End of snippet n. 0>