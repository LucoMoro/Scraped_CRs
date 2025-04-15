/*Fix Some CalendarTest Locale Issues

Change-Id:If8890fc154866b6506a90815e5f1b5122fa9b52d*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/CalendarTest.java b/luni/src/test/java/tests/api/java/util/CalendarTest.java
//Synthetic comment -- index bca091f..bfe62e3 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import java.util.Map;
import java.util.TimeZone;

import org.apache.harmony.testframework.serialization.SerializationTest;

public class CalendarTest extends junit.framework.TestCase {
//Synthetic comment -- @@ -653,22 +655,28 @@
*/
public void test_getInstance() {
// test getInstance(Locale)
        Calendar us_calendar = Calendar.getInstance(Locale.US);
        Calendar ch_calendar = Calendar.getInstance(Locale.CHINESE);
        assertEquals(Calendar.SUNDAY, us_calendar
                .getFirstDayOfWeek());
        assertEquals(Calendar.MONDAY, ch_calendar
                .getFirstDayOfWeek());

// test getInstance(Locale, TimeZone)
        Calendar gmt_calendar = Calendar.getInstance(TimeZone
                .getTimeZone("GMT"), Locale.US);
        assertEquals(TimeZone.getTimeZone("GMT"),
                gmt_calendar.getTimeZone());
        Calendar est_calendar = Calendar.getInstance(TimeZone
                .getTimeZone("EST"), Locale.US);
        assertEquals(TimeZone.getTimeZone("EST")
                .getID(), est_calendar.getTimeZone().getID());
}

/**







