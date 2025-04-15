/*Make Timestamp.valueOf locale safe

Timestamp.valueOf was producing results that varied
depending on the current default locale.

Change-Id:I464be9c1553dfc4886be7bfbdbf0ac4eb50929b3*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/sql/Timestamp.java b/luni/src/main/java/java/sql/Timestamp.java
//Synthetic comment -- index 463ea8b..070e86f 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -413,7 +414,7 @@
throw badTimestampString(s);
}

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
ParsePosition pp = new ParsePosition(0);

/*







