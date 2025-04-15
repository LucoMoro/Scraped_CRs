/*The format test of Date below is only for Locale.US, so I suggest this case should query the value of locale from phone before actually doing the
test.

Change-Id:Iffb74cabae19615a8d48eb780e30e7df105efbe0*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDateFormatTest.java b/luni/src/test/java/libcore/java/text/OldDateFormatTest.java
//Synthetic comment -- index 3a3540c..51e12f0 100644

//Synthetic comment -- @@ -206,6 +206,12 @@
public void test_parseLString() {
DateFormat format = DateFormat.getInstance();

        Locale mDefaultLocale;
        mDefaultLocale = Locale.getDefault();
        if (!mDefaultLocale.equals(Locale.US)) {
            return;
        }

try {
format.parse("not a Date");
fail("should throw ParseException first");







