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
isRecordLoadResponse = true;

ar = (AsyncResult)msg.obj;







