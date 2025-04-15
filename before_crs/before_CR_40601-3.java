/*Fix SimpleDateFormatTest.java and SimpleTimeZoneTest.java's assumption

Not all timezone would fit standard calendar day and daylight time requirement

Change-Id:Ifbfad9c2e05dffab9b2576bbf83fa07a933fe248*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/SimpleDateFormatTest.java b/luni/src/test/java/libcore/java/text/SimpleDateFormatTest.java
//Synthetic comment -- index c6296ff..0975a08 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
// The RI fails this test because this is an ICU-compatible Android extension.
// Necessary for correct localization in various languages (http://b/2633414).
public void testStandAloneNames() throws Exception {
Locale en = Locale.ENGLISH;
Locale pl = new Locale("pl");
Locale ru = new Locale("ru");








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/SimpleTimeZoneTest.java b/luni/src/test/java/tests/api/java/util/SimpleTimeZoneTest.java
//Synthetic comment -- index e27ec0d..618cbe4 100644

//Synthetic comment -- @@ -691,6 +691,7 @@
* java.util.SimpleTimeZone#setStartRule(int, int, int, int, boolean)
*/
public void test_setStartRuleIIIIZ() {
// Test for method void java.util.SimpleTimeZone.setStartRule(int, int,
// int, int, boolean)
SimpleTimeZone st = new SimpleTimeZone(TimeZone.getTimeZone("EST").getRawOffset(), "EST");







