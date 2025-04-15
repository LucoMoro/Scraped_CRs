/*1. The format test of Date below is only for Locale.US, so I suggest this case should query the value of locale from phone before actually doing the
test.

2. Use Locale.US DateFormat instance to run the test below.

Change-Id:Iffb74cabae19615a8d48eb780e30e7df105efbe0*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDateFormatTest.java b/luni/src/test/java/libcore/java/text/OldDateFormatTest.java
//Synthetic comment -- index 3a3540c..30c9e67 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
* @tests java.text.DateFormat#parse(String)
*/
public void test_parseLString() {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);

try {
format.parse("not a Date");
//Synthetic comment -- @@ -247,7 +247,7 @@
//expected
}

        format = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(current.getDate(), date.getDate());
//Synthetic comment -- @@ -275,7 +275,7 @@
//expected
}

        format = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(current.getDate(), date.getDate());
//Synthetic comment -- @@ -289,7 +289,7 @@
fail("ParseException was thrown for current Date.");
}

        format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(current.getDate(), date.getDate());
//Synthetic comment -- @@ -303,7 +303,7 @@
fail("ParseException was thrown for current Date.");
}

        format = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(1, date.getDate());
//Synthetic comment -- @@ -322,7 +322,7 @@
//expected
}

        format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(current.getDate(), date.getDate());
//Synthetic comment -- @@ -349,7 +349,7 @@
//expected
}

        format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
try {
Date date = format.parse(format.format(current).toString());
assertEquals(current.getDate(), date.getDate());







