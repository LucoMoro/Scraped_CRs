/*Correct VM number updation during MBDN refresh.

The voice mail number and voice mail tag are not getting updated properly
during MBDN refresh. When the data in MBDN EF is invalid, the voice mail
number and voice mail tag are unchanged. They retain their previous value
where as they should be null in this case.

Change-Id:Ic3b9cb4cdf3ee5d2a33fc7e47cac968e752a0940*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index d99a348..30f38bd 100644

//Synthetic comment -- @@ -560,6 +560,13 @@
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







