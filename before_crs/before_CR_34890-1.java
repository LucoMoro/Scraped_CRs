/*Addresses that are still in use no longer pruned

A previous bugfix that was added to prune the canonical address table when threads are deleted would delete any address that only occurred in multi-recipient threads. This fix will make sure an address isn't in a multi-recipient thread before deleting it. This fixes issue 27871.

Change-Id:If67606e7e29d5c996d9e2bc569d90bd0edbbb790Signed-off-by: Erik Browne <erik@browne.name>*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java b/src/com/android/providers/telephony/MmsSmsDatabaseHelper.java
//Synthetic comment -- index 62e435a..ab32a88 100644

//Synthetic comment -- @@ -251,8 +251,8 @@
new String[] { String.valueOf(thread_id) });
if (rows > 0) {
// If this deleted a row, let's remove orphaned canonical_addresses and get outta here
            db.delete("canonical_addresses",
                    "_id NOT IN (SELECT DISTINCT recipient_ids FROM threads)", null);
return;
}
// Update the message count in the threads table as the sum
//Synthetic comment -- @@ -354,8 +354,8 @@
"UNION SELECT DISTINCT thread_id FROM pdu)", null);

// remove orphaned canonical_addresses
        db.delete("canonical_addresses",
                "_id NOT IN (SELECT DISTINCT recipient_ids FROM threads)", null);
}

public static int deleteOneSms(SQLiteDatabase db, int message_id) {







