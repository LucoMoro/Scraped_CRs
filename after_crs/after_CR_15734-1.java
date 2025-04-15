/*Add hours to ddms log timestamp.

Change-Id:I293b049e73e4776d969706b28ae7533ed581dfcd*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Log.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Log.java
//Synthetic comment -- index e24c353..55f7aab 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Log class that mirrors the API in main Android sources.
//Synthetic comment -- @@ -347,13 +349,10 @@
* @param message
*/
public static String getLogFormatString(LogLevel logLevel, String tag, String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return String.format("%s %c/%s: %s\n", formatter.format(new Date()),
logLevel.getPriorityLetter(), tag, message);
}
}









