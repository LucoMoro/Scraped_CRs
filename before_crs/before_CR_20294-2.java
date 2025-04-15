/*1. The format test of Date below is only for Locale.US, so I suggest this case should query the value of locale from phone before actually doing the
test.

2. Use Locale.US DateFormat instance to run the test below.

Change-Id:Iffb74cabae19615a8d48eb780e30e7df105efbe0*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDateFormatTest.java b/luni/src/test/java/libcore/java/text/OldDateFormatTest.java
//Synthetic comment -- index 3a3540c..ea2457a 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
* @tests java.text.DateFormat#parse(String)
*/
public void test_parseLString() {
        DateFormat format = DateFormat.getInstance();

try {
format.parse("not a Date");
//Synthetic comment -- @@ -303,7 +303,7 @@
fail("ParseException was thrown for current Date.");
}

        format = DateFormat.getTimeInstance();
try {
Date date = format.parse(format.format(current).toString());
assertEquals(1, date.getDate());







