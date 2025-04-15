/*Dont use the default locale when formatting SQL statements

It is not safe to use the default locale when using String.format
to produce SQL statements. Some locales will break the SQL
and as a consequence crash the app.

Change-Id:I2d4eac71a2a33a8fc669b532eef3d9e87f2800c5*/




//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsSmsProvider.java b/src/com/android/providers/telephony/MmsSmsProvider.java
//Synthetic comment -- index 93668c1..3abe156 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.SearchManager;
//Synthetic comment -- @@ -363,13 +364,13 @@
		"body,pdu.date,index_text,words._id";

// search on the words table but return the rows from the corresponding sms table
                String smsQuery = String.format(Locale.US,
"SELECT %s FROM sms,words WHERE (words MATCH ? " +
" AND sms._id=words.source_id AND words.table_to_use=1) ",
smsProjection);

// search on the words table but return the rows from the corresponding parts table
                String mmsQuery = String.format(Locale.US,
"SELECT %s FROM pdu,part,addr,words WHERE ((part.mid=pdu._id) AND " +
"(addr.msg_id=pdu._id) AND " +
"(addr.type=%d) AND " +
//Synthetic comment -- @@ -381,7 +382,7 @@
PduHeaders.TO);

// join the results from sms and part (mms)
                String rawQuery = String.format(Locale.US,
"%s UNION %s GROUP BY %s ORDER BY %s",
smsQuery,
mmsQuery,
//Synthetic comment -- @@ -461,7 +462,7 @@
if (isEmail) {
selectionArgs = new String[] { refinedAddress };
} else {
            selection += " OR " + String.format(Locale.US, "PHONE_NUMBERS_EQUAL(address, ?, %d)",
(mUseStrictPhoneNumberComparation ? 1 : 0));
selectionArgs = new String[] { refinedAddress, refinedAddress };
}







