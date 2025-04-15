/*TelephonyProvider(DSDS): Add support for DSDS

Change-Id:I7fb7b30a02b2aea3bfd60d0a9b59c7938ea943ae*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java b/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java
//Synthetic comment -- index d0d410e..52360c2 100644

//Synthetic comment -- @@ -582,7 +582,8 @@
Mms.DELIVERY_TIME + " INTEGER," +
Mms.DELIVERY_REPORT + " INTEGER," +
Mms.LOCKED + " INTEGER DEFAULT 0," +
                   Mms.SEEN + " INTEGER DEFAULT 0" +
");");

db.execSQL("CREATE TABLE " + MmsProvider.TABLE_ADDR + " (" +
//Synthetic comment -- @@ -675,6 +676,8 @@
"body TEXT," +
"service_center TEXT," +
"locked INTEGER DEFAULT 0," +
"error_code INTEGER DEFAULT 0," +
"seen INTEGER DEFAULT 0" +
");");








//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsProvider.java b/src/com/android/providers/telephony/MmsSmsProvider.java
//Synthetic comment -- index fb082b9..a2897b3 100644

//Synthetic comment -- @@ -111,7 +111,8 @@
// These are the columns that appear in both the MMS ("pdu") and
// SMS ("sms") message tables.
private static final String[] MMS_SMS_COLUMNS =
            { BaseColumns._ID, Mms.DATE, Mms.DATE_SENT, Mms.READ, Mms.THREAD_ID, Mms.LOCKED };

// These are the columns that appear only in the MMS message
// table.








//Synthetic comment -- diff --git a/src/com/android/providers/telephony/SmsProvider.java b/src/com/android/providers/telephony/SmsProvider.java
//Synthetic comment -- index 15e008d..0ee8263 100644

//Synthetic comment -- @@ -1,5 +1,9 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -126,6 +130,14 @@
constructQueryForBox(qb, Sms.MESSAGE_TYPE_OUTBOX);
break;

case SMS_ALL_ID:
qb.setTables(TABLE_SMS);
qb.appendWhere("(_id = " + url.getPathSegments().get(0) + ")");
//Synthetic comment -- @@ -358,6 +370,14 @@
type = Sms.MESSAGE_TYPE_INBOX;
break;

case SMS_FAILED:
type = Sms.MESSAGE_TYPE_FAILED;
break;
//Synthetic comment -- @@ -720,6 +740,8 @@
private static final int SMS_FAILED_ID = 25;
private static final int SMS_QUEUED = 26;
private static final int SMS_UNDELIVERED = 27;

private static final UriMatcher sURLMatcher =
new UriMatcher(UriMatcher.NO_MATCH);
//Synthetic comment -- @@ -753,6 +775,9 @@
//we keep these for not breaking old applications
sURLMatcher.addURI("sms", "sim", SMS_ALL_ICC);
sURLMatcher.addURI("sms", "sim/#", SMS_ICC);

sConversationProjectionMap.put(Sms.Conversations.SNIPPET,
"sms.body AS snippet");







