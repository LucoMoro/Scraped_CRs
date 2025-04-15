/*OldDateFormatTest is flaky; improve its output.

Hopefully this will let us debug the flakiness.

Bug: 6838286
Change-Id:I501c4ad726e5d8f0e11b4e69feace97dc70cc775*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDateFormatTest.java b/luni/src/test/java/libcore/java/text/OldDateFormatTest.java
//Synthetic comment -- index f614d6f..6b3885c 100644

//Synthetic comment -- @@ -203,7 +203,7 @@
/**
* java.text.DateFormat#parse(String)
*/
    public void test_parseLString() {
DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);

try {
//Synthetic comment -- @@ -338,35 +338,29 @@
try {
format.parse("January 31 1970 7:52:34 AM PST");
fail("ParseException was not thrown.");
        } catch(ParseException pe) {
            //expected
}

try {
format.parse("January 31 1970");
fail("ParseException was not thrown.");
        } catch(ParseException pe) {
            //expected
}

format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
        try {
            Date date = format.parse(format.format(current).toString());
            assertEquals(current.getDate(), date.getDate());
            assertEquals(current.getDay(), date.getDay());
            assertEquals(current.getMonth(), date.getMonth());
            assertEquals(current.getYear(), date.getYear());
            assertEquals(current.getHours(), date.getHours());
            assertEquals(current.getMinutes(), date.getMinutes());
        } catch(ParseException pe) {
            fail("ParseException was thrown for current Date.");
}

try {
format.parse("January 16, 1970 8:03:52 PM CET");
fail("ParseException was not thrown.");
        } catch(ParseException pe) {
            //expected
}
}








