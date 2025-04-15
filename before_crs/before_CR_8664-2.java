/*Extend ANDROID with CDMA mobile technology support . Feature Complete

This contribution is final functional step of the first release of the CDMA extension of the
Android telephony layers.
It contains changes in the phone related applications, the application
framework telephony packages and in the RIL daemon library space.
The implementation of the CDMA support requires architectural changes in the
telephony package and extensions of the RIL interface.
The application interface (SDK interface) will be extended to provide
CDMA specific features/information to the phone related application and other
applications.
Where ever possible the actual used radio technology is transparent for the
application using mobile connections.

This increment is tested on the Android emulator with a RIL simulator tool and
also tested on a reference HW platform.
The CDMA extension of the telephony stack can be used for Android phones
supporting either CDMA mobile technology only
or world mode including GSM/WCDMA and CDMA.
The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
1x EVDO.

This contribution implements the following functionality:

- start up,
- access the CDMA subscription and other information from memory of from the
  card (either SIM, USIM or RUIM),
- register to the network,
- provides registration status to the application for displaying
- be able to handle incoming and outgoing voice calls,
- provide phone and call settings in the settings application
- provide supplementary services in the settings application
- provide supplementary services by in-call menues
- handles TTY and enhance voice privacy
- supports automatic radio technology change for a world mode phone from
  CDMA to GSM/UMTS or vice versa
- send and receive SMS
- configure and receive Broadcast SMS
- receive WAP Push SMS

Signed-off by : Saverio Labella,    saverio.labella@teleca.com
                Sigmar Lingner,     sigmar.lingner@teleca.com*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/SmsProvider.java b/src/com/android/providers/telephony/SmsProvider.java
//Synthetic comment -- index 5dd483f..6c42a27 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import com.android.internal.database.ArrayListCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//Synthetic comment -- @@ -31,37 +30,39 @@
import android.provider.Telephony.Sms;
import android.provider.Telephony.TextBasedSmsColumns;
import android.provider.Telephony.Threads;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SmsProvider extends ContentProvider {
private static final Uri NOTIFICATION_URI = Uri.parse("content://sms");
    private static final Uri SIM_URI = Uri.parse("content://sms/sim");
static final String TABLE_SMS = "sms";

private static final Integer ONE = Integer.valueOf(1);

/**
* These are the columns that are available when reading SMS
     * messages from the SIM.  Columns whose names begin with "is_"
* have either "true" or "false" as their values.
*/
    private final static String[] SIM_COLUMNS = new String[] {
// N.B.: These columns must appear in the same order as the
        // calls to add appear in convertSimToSms.
"service_center_address",       // getServiceCenterAddress
"address",                      // getDisplayOriginatingAddress
"message_class",                // getMessageClass
"body",                         // getDisplayMessageBody
"date",                         // getTimestampMillis
        "status",                       // getStatusOnSim
        "index_on_sim",                 // getIndexOnSim
"is_status_report",             // isStatusReportMessage
"transport_type",               // Always "sms".
"type"                          // Always MESSAGE_TYPE_ALL.
//Synthetic comment -- @@ -148,8 +149,10 @@
break;

case SMS_CONVERSATIONS:
                qb.setTables("sms, (SELECT thread_id AS group_thread_id, MAX(date) AS group_date, COUNT(*) AS msg_count FROM sms GROUP BY thread_id) AS groups");
                qb.appendWhere("sms.thread_id = groups.group_thread_id AND sms.date = groups.group_date");
qb.setProjectionMap(sConversationProjectionMap);
break;

//Synthetic comment -- @@ -183,13 +186,13 @@
qb.appendWhere("(_id = " + url.getPathSegments().get(1) + ")");
break;

            case SMS_ALL_SIM:
                return getAllMessagesFromSim();

            case SMS_SIM:
String messageIndexString = url.getPathSegments().get(1);

                return getSingleMessageFromSim(messageIndexString);

default:
Log.e(TAG, "Invalid request: " + url);
//Synthetic comment -- @@ -214,18 +217,18 @@
return ret;
}

    private ArrayList<String> convertSimToSms(SmsMessage message) {
ArrayList result = new ArrayList();

// N.B.: These calls must appear in the same order as the
        // columns appear in SIM_COLUMNS.
result.add(message.getServiceCenterAddress());
result.add(message.getDisplayOriginatingAddress());
        result.add(message.getMessageClass().toString());
result.add(message.getDisplayMessageBody());
result.add(message.getTimestampMillis());
result.add(Sms.STATUS_NONE);
        result.add(message.getIndexOnSim());
result.add(message.isStatusReportMessage());
result.add("sms");
result.add(TextBasedSmsColumns.MESSAGE_TYPE_ALL);
//Synthetic comment -- @@ -233,41 +236,40 @@
}

/**
     * Return a Cursor containing just one message from the SIM.
*/
    private Cursor getSingleMessageFromSim(String messageIndexString) {
try {
int messageIndex = Integer.parseInt(messageIndexString);
SmsManager smsManager = SmsManager.getDefault();
            ArrayList<SmsMessage> messages = smsManager.getAllMessagesFromSim();
ArrayList<ArrayList> singleRow = new ArrayList<ArrayList>();

            singleRow.add(convertSimToSms(messages.get(messageIndex)));
            return withSimNotificationUri(
                    new ArrayListCursor(SIM_COLUMNS, singleRow));
} catch (NumberFormatException exception) {
throw new IllegalArgumentException(
                    "Bad SMS SIM ID: " + messageIndexString);
}
}

/**
     * Return a Cursor listing all the messages stored on the SIM.
*/
    private Cursor getAllMessagesFromSim() {
SmsManager smsManager = SmsManager.getDefault();
        ArrayList<SmsMessage> messages = smsManager.getAllMessagesFromSim();
ArrayList<ArrayList> rows = new ArrayList<ArrayList>();

for (int count = messages.size(), i = 0; i < count; i++) {
            rows.add(convertSimToSms(messages.get(i)));
}
        return withSimNotificationUri(new ArrayListCursor(SIM_COLUMNS, rows));
}

    private Cursor withSimNotificationUri(Cursor cursor) {
        cursor.setNotificationUri(getContext().getContentResolver(),
                                  SIM_URI);
return cursor;
}

//Synthetic comment -- @@ -501,10 +503,10 @@
count = db.delete("sr_pending", where, whereArgs);
break;

            case SMS_SIM:
String messageIndexString = url.getPathSegments().get(1);

                return deleteMessageFromSim(messageIndexString);

default:
throw new IllegalArgumentException("Unknown URL");
//Synthetic comment -- @@ -517,23 +519,23 @@
}

/**
     * Delete the message at index from SIM.  Return true iff
* successful.
*/
    private int deleteMessageFromSim(String messageIndexString) {
SmsManager smsManager = SmsManager.getDefault();

try {
            return smsManager.deleteMessageFromSim(
Integer.parseInt(messageIndexString))
? 1 : 0;
} catch (NumberFormatException exception) {
throw new IllegalArgumentException(
                    "Bad SMS SIM ID: " + messageIndexString);
} finally {
ContentResolver cr = getContext().getContentResolver();

            cr.notifyChange(SIM_URI, null);
}
}

//Synthetic comment -- @@ -663,8 +665,8 @@
private static final int SMS_QUERY_THREAD_ID = 19;
private static final int SMS_STATUS_ID = 20;
private static final int SMS_STATUS_PENDING = 21;
    private static final int SMS_ALL_SIM = 22;
    private static final int SMS_SIM = 23;
private static final int SMS_FAILED = 24;
private static final int SMS_FAILED_ID = 25;
private static final int SMS_QUEUED = 26;
//Synthetic comment -- @@ -697,9 +699,12 @@
sURLMatcher.addURI("sms", "threadID/*", SMS_QUERY_THREAD_ID);
sURLMatcher.addURI("sms", "status/#", SMS_STATUS_ID);
sURLMatcher.addURI("sms", "sr_pending", SMS_STATUS_PENDING);
        sURLMatcher.addURI("sms", "sim", SMS_ALL_SIM);
        sURLMatcher.addURI("sms", "sim/#", SMS_SIM);

sConversationProjectionMap.put(Sms.Conversations.SNIPPET,
"body AS snippet");
sConversationProjectionMap.put("delta", null);







