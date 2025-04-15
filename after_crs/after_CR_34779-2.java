/*To add a test for the Indian locales.

Change-Id:I41afb0973a3a4b19c12d6d01bc752f67bde38870*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DecimalFormatTest.java b/luni/src/test/java/libcore/java/text/DecimalFormatTest.java
//Synthetic comment -- index 76b057b..2a67a18 100644

//Synthetic comment -- @@ -75,25 +75,49 @@
assertDecFmtWithMultiplierAndFraction("3333.33333333", 3, 4, "10,000");
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
}

public void testBigDecimalTestBigIntWithMultiplier() {
// Big integer tests.
        Locale loc = Locale.getDefault();
        if (loc.getLanguage().equals("en") && loc.getCountry().equals("IN")) {
            assertDecFmtWithMultiplierAndFraction("123456789012345", 10, 0,
                    "1,23,45,67,89,01,23,450");
            assertDecFmtWithMultiplierAndFraction("12345678901234567890", 10, 0,
                    "12,34,56,78,90,12,34,56,78,900");
            assertDecFmtWithMultiplierAndFraction("98765432109876543210987654321", 10, 0,
                    "9,87,65,43,21,09,87,65,43,21,09,87,65,43,210");

            assertDecFmtWithMultiplierAndFraction("123456789012345", -10, 0,
                    "-1,23,45,67,89,01,23,450");
            assertDecFmtWithMultiplierAndFraction("12345678901234567890", -10, 0,
                    "-12,34,56,78,90,12,34,56,78,900");
            assertDecFmtWithMultiplierAndFraction("98765432109876543210987654321", -10, 0,
                    "-9,87,65,43,21,09,87,65,43,21,09,87,65,43,210");
        } else {
            assertDecFmtWithMultiplierAndFraction("123456789012345", 10, 0,
                    "1,234,567,890,123,450");
            assertDecFmtWithMultiplierAndFraction("12345678901234567890", 10, 0,
                    "123,456,789,012,345,678,900");
            assertDecFmtWithMultiplierAndFraction("98765432109876543210987654321", 10, 0,
                    "987,654,321,098,765,432,109,876,543,210");

            assertDecFmtWithMultiplierAndFraction("123456789012345", -10, 0,
                    "-1,234,567,890,123,450");
            assertDecFmtWithMultiplierAndFraction("12345678901234567890", -10, 0,
                    "-123,456,789,012,345,678,900");
            assertDecFmtWithMultiplierAndFraction("98765432109876543210987654321", -10, 0,
                    "-987,654,321,098,765,432,109,876,543,210");
        }
}

public void testBigDecimalICUConsistency() {







