/*TelephonyProvider: SMS support for Multi SIM functionality.

Added support to send and receive message on multiple subscriptions.

Change-Id:I1504c06efe0bed1b4d9c43817ca689747a89f231*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java b/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java
//Synthetic comment -- index c26e855..0995956 100644

//Synthetic comment -- @@ -518,7 +518,8 @@
Mms.DELIVERY_TIME + " INTEGER," +
Mms.DELIVERY_REPORT + " INTEGER," +
Mms.LOCKED + " INTEGER DEFAULT 0," +
                   Mms.SEEN + " INTEGER DEFAULT 0," +
                   Mms.SUB_ID + " INTEGER DEFAULT 0" +
");");

db.execSQL("CREATE TABLE " + MmsProvider.TABLE_ADDR + " (" +
//Synthetic comment -- @@ -610,6 +611,8 @@
"body TEXT," +
"service_center TEXT," +
"locked INTEGER DEFAULT 0," +
                   "sub_id INTEGER DEFAULT 0," +   // sub_id : 0 for subscription 1
                                                   // sub_id : 1 for subscription 2
"error_code INTEGER DEFAULT 0," +
"seen INTEGER DEFAULT 0" +
");");








//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsProvider.java b/src/com/android/providers/telephony/MmsSmsProvider.java
//Synthetic comment -- index 31f5062..715c049 100644

//Synthetic comment -- @@ -112,7 +112,7 @@
// These are the columns that appear in both the MMS ("pdu") and
// SMS ("sms") message tables.
private static final String[] MMS_SMS_COLUMNS =
            { BaseColumns._ID, Mms.DATE, Mms.READ, Mms.THREAD_ID, Mms.LOCKED, Mms.SUB_ID };

// These are the columns that appear only in the MMS message
// table.








//Synthetic comment -- diff --git a/src/com/android/providers/telephony/SmsProvider.java b/src/com/android/providers/telephony/SmsProvider.java
//Synthetic comment -- index 57ac256..b66570f 100644

//Synthetic comment -- @@ -127,6 +127,14 @@
constructQueryForBox(qb, Sms.MESSAGE_TYPE_OUTBOX);
break;

            case SMS_INBOX_SIM1:
                constructQueryForBox(qb, Sms.MESSAGE_TYPE_INBOX_SUB1);
                break;

            case SMS_INBOX_SIM2:
                constructQueryForBox(qb, Sms.MESSAGE_TYPE_INBOX_SUB2);
                break;

case SMS_ALL_ID:
qb.setTables(TABLE_SMS);
qb.appendWhere("(_id = " + url.getPathSegments().get(0) + ")");
//Synthetic comment -- @@ -359,6 +367,14 @@
type = Sms.MESSAGE_TYPE_INBOX;
break;

            case SMS_INBOX_SIM1:
                type = Sms.MESSAGE_TYPE_INBOX_SUB1;
                break;

            case SMS_INBOX_SIM2:
                type = Sms.MESSAGE_TYPE_INBOX_SUB2;
                break;

case SMS_FAILED:
type = Sms.MESSAGE_TYPE_FAILED;
break;
//Synthetic comment -- @@ -721,6 +737,8 @@
private static final int SMS_FAILED_ID = 25;
private static final int SMS_QUEUED = 26;
private static final int SMS_UNDELIVERED = 27;
    private static final int SMS_INBOX_SIM1 = 28;
    private static final int SMS_INBOX_SIM2 = 29;

private static final UriMatcher sURLMatcher =
new UriMatcher(UriMatcher.NO_MATCH);
//Synthetic comment -- @@ -754,6 +772,9 @@
//we keep these for not breaking old applications
sURLMatcher.addURI("sms", "sim", SMS_ALL_ICC);
sURLMatcher.addURI("sms", "sim/#", SMS_ICC);
        //URLs for the sms belongs to sim1 and sim2
        sURLMatcher.addURI("sms", "inbox/sim1", SMS_INBOX_SIM1);
        sURLMatcher.addURI("sms", "inbox/sim2", SMS_INBOX_SIM2);

sConversationProjectionMap.put(Sms.Conversations.SNIPPET,
"sms.body AS snippet");







