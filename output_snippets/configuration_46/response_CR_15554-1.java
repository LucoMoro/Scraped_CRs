<<Beginning of snippet n. 0>>
break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
isRecordLoadResponse = true;

ar = (AsyncResult)msg.obj;

if (ar.exception != null) {
    voicemailNumber = null;
    voicemailTag = null;
    log("MBDN data fetch failed: " + ar.exception);
} else {
    // Validate MBDN data immediately after retrieval
    if (!isMbdnDataValid(ar)) {
        voicemailNumber = null;
        voicemailTag = null;
        log("MBDN data is invalid, resetting voicemail number and tag.");
    } else {
        try {
            updateVoicemailDetails(ar);
        } catch (Exception e) {
            voicemailNumber = null;
            voicemailTag = null;
            log("Error updating voicemail details: " + e.getMessage());
        }
    }
}
//<End of snippet n. 0>>