//<Beginning of snippet n. 0>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
    isRecordLoadResponse = true;

    ar = (AsyncResult)msg.obj;

    if (ar.exception != null || ar.result == null || !(ar.result instanceof String)) {
        voicemailNumber = null;
        voicemailTag = null;
        logInvalidMbdnData(); // Assuming a method exists for logging
    } else {
        // Existing logic for valid MBDN data
    }
//<End of snippet n. 0>