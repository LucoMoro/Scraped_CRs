/*Don't call overrideable methods from a constructor.

Conflicts:

	luni/src/test/java/libcore/java/util/TimeZoneTest.java

(cherry-pick of 7dbf334facc7f43c18244150d2052ae4ec8c5e16.)

Change-Id:I0aa0f3c8776538076bdfed070da08e65aa959486*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/SimpleTimeZone.java b/luni/src/main/java/java/util/SimpleTimeZone.java
//Synthetic comment -- index 6c9b443..93dc88e 100644

//Synthetic comment -- @@ -217,10 +217,17 @@
throw new IllegalArgumentException("Invalid daylightSavings: " + daylightSavings);
}
dstSavings = daylightSavings;
        // TODO: do we need to set useDaylight is dstSavings != 0?

        setStartRule(startMonth, startDay, startDayOfWeek, startTime);
        setEndRule(endMonth, endDay, endDayOfWeek, endTime);
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/TimeZoneTest.java b/luni/src/test/java/libcore/java/util/TimeZoneTest.java
//Synthetic comment -- index d5fa558..6e8b8d8 100644

//Synthetic comment -- @@ -17,12 +17,14 @@
package libcore.java.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.SimpleTimeZone;

public class TimeZoneTest extends junit.framework.TestCase {
// http://code.google.com/p/android/issues/detail?id=877
public void test_useDaylightTime_Taiwan() {
TimeZone asiaTaipei = TimeZone.getTimeZone("Asia/Taipei");
//Synthetic comment -- @@ -192,4 +194,21 @@
assertEquals(3600000, TimeZone.getTimeZone("America/Los_Angeles").getDSTSavings());
assertEquals(1800000, TimeZone.getTimeZone("Australia/Lord_Howe").getDSTSavings());
}
}







