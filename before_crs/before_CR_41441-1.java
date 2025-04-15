/*Fix test flakiness by not assuming we're in en_US.

Change-Id:I585bb21a300876a83a6e61139e8a1cd04398788d*/
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







