/*Group contacts are displayed as anonymous

Root Cause:
The recipient ids of a group threads were saved as a string and
using SPACE to separate the ids in the string. When updating
canonical address if a thread is deleted, the address of a group
sms in the canonical_address will be deleted since it can not
match any recipients in the threads table.

Solution:
Compose a string that contains all recipient ids in the thread table,
and use ',' to separate the ids. Then, use this string in sql
statement to clear the canonical_address table.

Change-Id:I947a8b5a07eb98fce11e57eee4ea5c3772bc9f29*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java b/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java
//Synthetic comment -- index 62e435a..1c8869a 100644

//Synthetic comment -- @@ -251,8 +251,7 @@
new String[] { String.valueOf(thread_id) });
if (rows > 0) {
// If this deleted a row, let's remove orphaned canonical_addresses and get outta here
            db.delete("canonical_addresses",
                    "_id NOT IN (SELECT DISTINCT recipient_ids FROM threads)", null);
return;
}
// Update the message count in the threads table as the sum
//Synthetic comment -- @@ -354,8 +353,31 @@
"UNION SELECT DISTINCT thread_id FROM pdu)", null);

// remove orphaned canonical_addresses
db.delete("canonical_addresses",
                "_id NOT IN (SELECT DISTINCT recipient_ids FROM threads)", null);
}

public static int deleteOneSms(SQLiteDatabase db, int message_id) {







