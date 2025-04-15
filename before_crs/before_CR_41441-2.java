/*Fix test flakiness by not assuming we're in en_US.

Bug: 6410256
Bug: 6527877
Bug: 6971390
Change-Id:I585bb21a300876a83a6e61139e8a1cd04398788d*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDateFormatTest.java b/luni/src/test/java/libcore/java/text/OldDateFormatTest.java
//Synthetic comment -- index 6b3885c..df388d3 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
DateFormat.SHORT, DateFormat.SHORT, Locale.US);
Date current = new Date();
String dtf = format.format(current);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a");
assertTrue("Incorrect date format", sdf.format(current).equals(dtf));
} catch (Exception e) {
fail("Unexpected exception " + e.toString());
//Synthetic comment -- @@ -110,7 +110,7 @@
StringBuffer toAppend = new StringBuffer();
FieldPosition fp = new FieldPosition(DateFormat.YEAR_FIELD);
StringBuffer sb = format.format(current, toAppend, fp);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a");
assertTrue("Incorrect date format", sdf.format(current).equals(
sb.toString()));
assertTrue("Incorrect beginIndex of filed position", fp








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/SimpleDateFormatTest.java b/luni/src/test/java/libcore/java/text/SimpleDateFormatTest.java
//Synthetic comment -- index c6296ff..4aabf2b 100644

//Synthetic comment -- @@ -77,7 +77,7 @@
}

public void test2038() {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
format.setTimeZone(TimeZone.getTimeZone("UTC"));

assertEquals("Sun Nov 24 17:31:44 1833",
//Synthetic comment -- @@ -175,14 +175,14 @@
* longer use their DST zone but we should continue to parse it properly.
*/
public void testObsoleteDstZoneName() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm zzzz");
Date normal = format.parse("1970-01-01T00:00 EET");
Date dst = format.parse("1970-01-01T00:00 EEST");
assertEquals(60 * 60 * 1000, normal.getTime() - dst.getTime());
}

public void testDstZoneNameWithNonDstTimestamp() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm zzzz");
Calendar calendar = new GregorianCalendar(AMERICA_LOS_ANGELES);
calendar.setTime(format.parse("2011-06-21T10:00 Pacific Standard Time")); // 18:00 GMT-8
assertEquals(11, calendar.get(Calendar.HOUR_OF_DAY)); // 18:00 GMT-7
//Synthetic comment -- @@ -190,7 +190,7 @@
}

public void testNonDstZoneNameWithDstTimestamp() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm zzzz");
Calendar calendar = new GregorianCalendar(AMERICA_LOS_ANGELES);
calendar.setTime(format.parse("2010-12-21T10:00 Pacific Daylight Time")); // 17:00 GMT-7
assertEquals(9, calendar.get(Calendar.HOUR_OF_DAY)); // 17:00 GMT-8
//Synthetic comment -- @@ -199,7 +199,7 @@

// http://b/4723412
public void testDstZoneWithNonDstTimestampForNonHourDstZone() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm zzzz");
Calendar calendar = new GregorianCalendar(AUSTRALIA_LORD_HOWE);
calendar.setTime(format.parse("2011-06-21T20:00 Lord Howe Daylight Time")); // 9:00 GMT+11
assertEquals(19, calendar.get(Calendar.HOUR_OF_DAY)); // 9:00 GMT+10:30
//Synthetic comment -- @@ -207,7 +207,7 @@
}

public void testNonDstZoneWithDstTimestampForNonHourDstZone() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm zzzz");
Calendar calendar = new GregorianCalendar(AUSTRALIA_LORD_HOWE);
calendar.setTime(format.parse("2010-12-21T19:30 Lord Howe Standard Time")); //9:00 GMT+10:30
assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY)); // 9:00 GMT+11:00








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/OldAndroidLocaleTest.java b/luni/src/test/java/libcore/java/util/OldAndroidLocaleTest.java
//Synthetic comment -- index 547b70a..8fbe5ff 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
* resource bundles.
*/
public class OldAndroidLocaleTest extends TestCase {

// Test basic Locale infrastructure.
public void testLocale() throws Exception {
Locale locale = new Locale("en");
//Synthetic comment -- @@ -47,49 +46,6 @@
assertEquals("en_US_POSIX", locale.toString());
}

    /*
     * Tests some must-have locales. TODO: Add back "de". See discussion
     * immediately below this method.
     */
    public void testResourceBundles() throws Exception {
        Locale eng = new Locale("en", "US");
        DateFormatSymbols engSymbols = new DateFormatSymbols(eng);

        //Locale deu = new Locale("de", "DE");
        //DateFormatSymbols deuSymbols = new DateFormatSymbols(deu);

        TimeZone berlin = TimeZone.getTimeZone("Europe/Berlin");

        assertEquals("January", engSymbols.getMonths()[0]);
        //assertEquals("Januar", deuSymbols.getMonths()[0]);

        assertEquals("Sunday", engSymbols.getWeekdays()[Calendar.SUNDAY]);
        //assertEquals("Sonntag", deuSymbols.getWeekdays()[Calendar.SUNDAY]);

        assertEquals("Central European Time",
                berlin.getDisplayName(false, TimeZone.LONG, eng));
        assertEquals("Central European Summer Time",
                berlin.getDisplayName(true, TimeZone.LONG, eng));

        //assertEquals("Mitteleurop\u00E4ische Zeit",
        //        berlin.getDisplayName(false, TimeZone.LONG, deu));
        //assertEquals("Mitteleurop\u00E4ische Sommerzeit",
        //        berlin.getDisplayName(true, TimeZone.LONG, deu));

        assertTrue(engSymbols.getZoneStrings().length > 100);
    }

    /*
     * Disabled version of the above test. The version above omits
     * checks for stuff in the "de" locale, because we stripped that
     * out as part of the flash reduction effort (so that we could
     * still ship on Dream). We expect to have a baseline target that
     * includes a large enough system partition to include "de"
     * immediately after the last official release for Dream (whenever
     * that may be).
     *
    // Test some must-have locales.
    @LargeTest
public void testResourceBundles() throws Exception {
Locale eng = new Locale("en", "US");
DateFormatSymbols engSymbols = new DateFormatSymbols(eng);
//Synthetic comment -- @@ -117,7 +73,6 @@

assertTrue(engSymbols.getZoneStrings().length > 100);
}
    */

// This one makes sure we have all necessary locales installed.
// Suppress this flaky test for now.








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/OldTimeZoneTest.java b/luni/src/test/java/libcore/java/util/OldTimeZoneTest.java
//Synthetic comment -- index 240403e..4ed89de 100644

//Synthetic comment -- @@ -83,6 +83,7 @@
}

public void test_getDisplayName() {
TimeZone tz = TimeZone.getTimeZone("GMT-6");
assertEquals("GMT-06:00", tz.getDisplayName());
tz = TimeZone.getTimeZone("America/Los_Angeles");
//Synthetic comment -- @@ -99,6 +100,7 @@
}

public void test_getDisplayNameZI() {
TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
assertEquals("PST",                   tz.getDisplayName(false, 0));
assertEquals("Pacific Daylight Time", tz.getDisplayName(true, 1));
//Synthetic comment -- @@ -150,15 +152,4 @@
tz.setID("New ID for GMT-6");
assertEquals("New ID for GMT-6", tz.getID());
}

    Locale loc = null;

    protected void setUp() {
        loc = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    protected void tearDown() {
        Locale.setDefault(loc);
    }
}







