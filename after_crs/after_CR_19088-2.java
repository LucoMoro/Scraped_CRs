/*Dont use the default locale when formatting SQL statements

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app.

Change-Id:I2d4eac71a2a33a8fc669b532eef3d9e87f2800c5*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsProvider.java b/src/com/android/providers/telephony/MmsSmsProvider.java
//Synthetic comment -- index 93668c1..0b7015b 100644

//Synthetic comment -- @@ -175,6 +175,33 @@
Mms.MESSAGE_TYPE + " = " + PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF + " OR " +
Mms.MESSAGE_TYPE + " = " + PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND + "))";

    // Search on the words table but return the rows from the corresponding sms table
    private static final String SMS_QUERY =
            "SELECT sms._id AS _id,thread_id,address,body,date,index_text,words._id " +
            "FROM sms,words WHERE (words MATCH ? " +
            "AND sms._id=words.source_id AND words.table_to_use=1)";

    // Search on the words table but return the rows from the corresponding parts table
    private static final String MMS_QUERY =
            "SELECT pdu._id,thread_id,addr.address,part.text " +
            "AS body,pdu.date,index_text,words._id " +
            "FROM pdu,part,addr,words WHERE ((part.mid=pdu._id) AND " +
            "(addr.msg_id=pdu._id) AND " +
            "(addr.type=" + PduHeaders.TO + ") AND " +
            "(part.ct='text/plain') AND " +
            "(words MATCH ?) AND " +
            "(part._id = words.source_id) AND " +
            "(words.table_to_use=2))";

    // This code queries the sms and mms tables and returns a unified result set
    // of text matches.  We query the sms table which is pretty simple.  We also
    // query the pdu, part and addr table to get the mms result.  Note that we're
    // using a UNION so we have to have the same number of result columns from
    // both queries.
    private static final String SMS_MMS_QUERY =
            SMS_QUERY + " UNION " + MMS_QUERY +
            " GROUP BY thread_id ORDER BY thread_id ASC, date DESC";

private static final String AUTHORITY = "mms-sms";

static {
//Synthetic comment -- @@ -349,46 +376,10 @@
"with this query");
}

String searchString = uri.getQueryParameter("pattern") + "*";

try {
                    cursor = db.rawQuery(SMS_MMS_QUERY, new String[] { searchString, searchString });
} catch (Exception ex) {
Log.e(LOG_TAG, "got exception: " + ex.toString());
}
//Synthetic comment -- @@ -461,8 +452,8 @@
if (isEmail) {
selectionArgs = new String[] { refinedAddress };
} else {
            selection += " OR PHONE_NUMBERS_EQUAL(address, ?, " +
                        (mUseStrictPhoneNumberComparation ? 1 : 0) + ")";
selectionArgs = new String[] { refinedAddress, refinedAddress };
}








