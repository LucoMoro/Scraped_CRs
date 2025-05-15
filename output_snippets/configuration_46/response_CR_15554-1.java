//<Beginning of snippet n. 0>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
isRecordLoadResponse = true;

ar = (AsyncResult)msg.obj;

// Validation Check
if (ar == null || ar.result == null) {
    voicemailNumber = null;
    voicemailTag = null;
} else {
    // Additional logic for valid data, if needed
}

//<End of snippet n. 0>