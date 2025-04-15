/*Check for US Locale in DateUtilsTest

Bug 2951291

Change-Id:I8502b430f696a11b14cd5d30d53b4aef5c0e5662*/
//Synthetic comment -- diff --git a/tests/src/android/text/format/cts/LocaleUtils.java b/tests/src/android/text/format/cts/LocaleUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..789ad74

//Synthetic comment -- @@ -0,0 +1,33 @@








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index 22ef413..91bef2d 100644

//Synthetic comment -- @@ -198,6 +198,11 @@
})
@SuppressWarnings("deprecation")
public void testFormatMethods() {
long elapsedTime = 2 * 60 * 60;
String expected = "2:00:00";
assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));







