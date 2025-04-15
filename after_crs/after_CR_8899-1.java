/*Fixing a bug that Email app fails to read emails with ParseException in
non-English locales such as Japanese.

SimpleDateFormat.parse() fails to parse "14-Feb-2009 20:00:37 +0000"
because month names like "Feb" cannot be parsed with MMM
format in such locales.*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/store/ImapResponseParser.java b/src/com/android/email/mail/store/ImapResponseParser.java
//Synthetic comment -- index 96b4181..76a4d30 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.util.Config;
import android.util.Log;
//Synthetic comment -- @@ -35,7 +36,7 @@
// DEBUG ONLY - Always check in as "false"
private static boolean DEBUG_LOG_RAW_STREAM = false;

    SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss Z", Locale.US);
PeekableInputStream mIn;
InputStream mActiveLiteral;








