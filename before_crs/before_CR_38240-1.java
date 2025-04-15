/*Mms: Consider only Inbox messages for reply path

SMSC info is not filled in MO SMS even if MT SMS is having
reply path (in GSM/WCDMA case). With current design Android telephony
is considering latest SMS message in thread to know the reply path.

Ex: If one MT message is available in Inbox and if User wants to
send MO SMS as reply app stores MO SMS in queue and send the queued
messages to lower layers. Now SMS app is having two messages - one is
MT and one is MO (queued message). In this case before sending SMS the app
is considering the latest queued message instead of MT message to get SMSC.

Consider only Inbox messages to get SMSC.

Change-Id:If37d5fdf9f11f9286f41839ea5b3028f02c1de3e*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsMessageSender.java b/src/com/android/mms/transaction/SmsMessageSender.java
//Synthetic comment -- index b2b7585..768f615 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Telephony.Sms;
import android.util.Log;

public class SmsMessageSender implements MessageSender {
//Synthetic comment -- @@ -130,7 +131,7 @@

try {
cursor = SqliteWrapper.query(mContext, mContext.getContentResolver(),
                            Sms.CONTENT_URI, SERVICE_CENTER_PROJECTION,
"thread_id = " + threadId, null, "date DESC");

if ((cursor == null) || !cursor.moveToFirst()) {







