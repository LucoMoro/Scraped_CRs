/*Dont use the default locale when formatting SQL statements

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app.

Change-Id:I2d4eac71a2a33a8fc669b532eef3d9e87f2800c5*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsProvider.java b/src/com/android/providers/telephony/MmsSmsProvider.java
//Synthetic comment -- index 31f5062..f715cdb 100644

//Synthetic comment -- @@ -175,6 +175,33 @@
Mms.MESSAGE_TYPE + " = " + PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF + " OR " +
Mms.MESSAGE_TYPE + " = " + PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND + "))";

private static final String AUTHORITY = "mms-sms";

static {
//Synthetic comment -- @@ -349,46 +376,10 @@
"with this query");
}

                // This code queries the sms and mms tables and returns a unified result set
                // of text matches.  We query the sms table which is pretty simple.  We also
                // query the pdu, part and addr table to get the mms result.  Note that we're
                // using a UNION so we have to have the same number of result columns from
                // both queries.

String searchString = uri.getQueryParameter("pattern") + "*";

                String smsProjection = "sms._id as _id,thread_id,address,body,date," +
                "index_text,words._id";
                String mmsProjection = "pdu._id,thread_id,addr.address,part.text as " + "" +
                		"body,pdu.date,index_text,words._id";

                // search on the words table but return the rows from the corresponding sms table
                String smsQuery = String.format(
                        "SELECT %s FROM sms,words WHERE (words MATCH ? " +
                        " AND sms._id=words.source_id AND words.table_to_use=1) ",
                        smsProjection);

                // search on the words table but return the rows from the corresponding parts table
                String mmsQuery = String.format(
                        "SELECT %s FROM pdu,part,addr,words WHERE ((part.mid=pdu._id) AND " +
                        "(addr.msg_id=pdu._id) AND " +
                        "(addr.type=%d) AND " +
                        "(part.ct='text/plain') AND " +
                        "(words MATCH ?) AND " +
                        "(part._id = words.source_id) AND " +
                        "(words.table_to_use=2))",
                        mmsProjection,
                        PduHeaders.TO);

                // join the results from sms and part (mms)
                String rawQuery = String.format(
                        "%s UNION %s GROUP BY %s ORDER BY %s",
                        smsQuery,
                        mmsQuery,
                        "thread_id",
                        "thread_id ASC, date DESC");
try {
                    cursor = db.rawQuery(rawQuery, new String[] { searchString, searchString });
} catch (Exception ex) {
Log.e(LOG_TAG, "got exception: " + ex.toString());
}
//Synthetic comment -- @@ -461,8 +452,8 @@
if (isEmail) {
selectionArgs = new String[] { refinedAddress };
} else {
            selection += " OR " + String.format("PHONE_NUMBERS_EQUAL(address, ?, %d)",
                        (mUseStrictPhoneNumberComparation ? 1 : 0));
selectionArgs = new String[] { refinedAddress, refinedAddress };
}








