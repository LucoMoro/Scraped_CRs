
//<Beginning of snippet n. 0>


break;
case EVENT_GET_CPHS_MAILBOX_DONE:
case EVENT_GET_MBDN_DONE:
                //Resetting the voice mail number and voice mail tag to null
                //as these should be updated from the data read from EF_MBDN.
                //If they are not reset, incase of invalid data/exception these
                //variables are retaining their previous values and are
                //causing invalid voice mailbox info display to user.
                voiceMailNum = null;
                voiceMailTag = null;
isRecordLoadResponse = true;

ar = (AsyncResult)msg.obj;

//<End of snippet n. 0>








