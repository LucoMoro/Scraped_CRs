/*Type Zero Sms should not be displayed/stored/notified.

Type Zero messages indicated by TP_PID field set to value 0x40,
should not be displayed/stored/notified. They should only be
acknowledged.*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsMessage.java b/telephony/java/android/telephony/SmsMessage.java
//Synthetic comment -- index b8ea4c0..5b00b43 100644

//Synthetic comment -- @@ -75,6 +75,12 @@
*/
public static final int MAX_USER_DATA_SEPTETS_WITH_HEADER = 153;

/** Contains actual SmsMessage. Only public for debugging and for framework layer.
*
* @hide








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 6ae316d..9953d34 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import java.util.HashMap;

import static android.telephony.SmsMessage.MessageClass;

final class GsmSMSDispatcher extends SMSDispatcher {
private static final String TAG = "GSM";
//Synthetic comment -- @@ -94,6 +95,14 @@
SmsMessage sms = (SmsMessage) smsb;
boolean handled = false;

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
mGsmPhone.updateMessageWaitingIndicator(true);







