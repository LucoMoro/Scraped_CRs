/*To add a test for the Indian locales.

Change-Id:I167b1f624f0584f3325f186f791944c87fdb78c0*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DecimalFormatTest.java b/luni/src/test/java/libcore/java/text/DecimalFormatTest.java
//Synthetic comment -- index 3a1cc3c..2a67a18 100644

//Synthetic comment -- @@ -76,12 +76,11 @@
assertDecFmtWithMultiplierAndFraction("3333.33333333", -3, 4, "-10,000");
assertDecFmtWithMultiplierAndFraction("0.00333333", 3, 4, "0.01");

        Locale loc = Locale.getDefault();
        if (loc.getLanguage().equals("en") && loc.getCountry().equals("IN")) {
assertDecFmtWithMultiplierAndFraction("3330000000000000000000000000000000", 3, 4,
"9,99,00,00,00,00,00,00,00,00,00,00,00,00,00,00,000");
        } else {
assertDecFmtWithMultiplierAndFraction("3330000000000000000000000000000000", 3, 4,
"9,990,000,000,000,000,000,000,000,000,000,000");
}
//Synthetic comment -- @@ -89,8 +88,8 @@

public void testBigDecimalTestBigIntWithMultiplier() {
// Big integer tests.
        Locale loc = Locale.getDefault();
        if (loc.getLanguage().equals("en") && loc.getCountry().equals("IN")) {
assertDecFmtWithMultiplierAndFraction("123456789012345", 10, 0,
"1,23,45,67,89,01,23,450");
assertDecFmtWithMultiplierAndFraction("12345678901234567890", 10, 0,
//Synthetic comment -- @@ -104,7 +103,7 @@
"-12,34,56,78,90,12,34,56,78,900");
assertDecFmtWithMultiplierAndFraction("98765432109876543210987654321", -10, 0,
"-9,87,65,43,21,09,87,65,43,21,09,87,65,43,210");
        } else {
assertDecFmtWithMultiplierAndFraction("123456789012345", 10, 0,
"1,234,567,890,123,450");
assertDecFmtWithMultiplierAndFraction("12345678901234567890", 10, 0,







