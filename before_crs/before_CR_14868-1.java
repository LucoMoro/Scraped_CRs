/*Telephony: Process MT SMS with MTI set to 3

Change-Id:Ie57a9465d41a3bd2c625ccf6e675af400be6d294*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 12c6b88..4fd62fb 100644

//Synthetic comment -- @@ -928,6 +928,8 @@
// TP-Message-Type-Indicator
// 9.2.3
case 0:
parseSmsDeliver(p, firstByte);
break;
case 2:







