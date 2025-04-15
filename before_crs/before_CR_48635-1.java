/*Telephony: Add support for SMS-SUBMIT.

Change-Id:I2b0887ae6b28decb38576cac60f1314ff8b73e1dSigned-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f23ec98..c1806b1 100644

//Synthetic comment -- @@ -924,6 +924,9 @@
//This should be processed in the same way as MTI == 0 (Deliver)
parseSmsDeliver(p, firstByte);
break;
case 2:
parseSmsStatusReport(p, firstByte);
break;
//Synthetic comment -- @@ -1019,6 +1022,66 @@
}

/**
* Parses the User Data of an SMS.
*
* @param p The current PduParser.







