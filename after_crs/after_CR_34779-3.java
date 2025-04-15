/*Modify the behavior of test. It always test en_US, and it will test en_US when en_US is available.

Change-Id:I41afb0973a3a4b19c12d6d01bc752f67bde38870*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DecimalFormatTest.java b/luni/src/test/java/libcore/java/text/DecimalFormatTest.java
//Synthetic comment -- index 76b057b..a2f93a8 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import tests.support.Support_Locale;

public class DecimalFormatTest extends junit.framework.TestCase {
public void test_exponentSeparator() throws Exception {
//Synthetic comment -- @@ -75,25 +76,49 @@
assertDecFmtWithMultiplierAndFraction("3333.33333333", 3, 4, "10,000");
assertDecFmtWithMultiplierAndFraction("3333.33333333", -3, 4, "-10,000");
assertDecFmtWithMultiplierAndFraction("0.00333333", 3, 4, "0.01");

        assertDecFmtWithMultiplierAndFractionByLocale("3330000000000000000000000000000000", 3, 4,
                    Locale.US, "9,990,000,000,000,000,000,000,000,000,000,000");

        Locale inLocale = new Locale("en", "IN");
        if (Support_Locale.isLocaleAvailable(inLocale)) {
            assertDecFmtWithMultiplierAndFractionByLocale("3330000000000000000000000000000000", 3, 4,
                    inLocale, "9,99,00,00,00,00,00,00,00,00,00,00,00,00,00,00,000");
        }
}

public void testBigDecimalTestBigIntWithMultiplier() {
// Big integer tests.
        assertDecFmtWithMultiplierAndFractionByLocale("123456789012345", 10, 0,
                Locale.US, "1,234,567,890,123,450");
        assertDecFmtWithMultiplierAndFractionByLocale("12345678901234567890", 10, 0,
                Locale.US, "123,456,789,012,345,678,900");
        assertDecFmtWithMultiplierAndFractionByLocale("98765432109876543210987654321", 10, 0,
                Locale.US, "987,654,321,098,765,432,109,876,543,210");

        assertDecFmtWithMultiplierAndFractionByLocale("123456789012345", -10, 0,
                Locale.US, "-1,234,567,890,123,450");
        assertDecFmtWithMultiplierAndFractionByLocale("12345678901234567890", -10, 0,
                Locale.US, "-123,456,789,012,345,678,900");
        assertDecFmtWithMultiplierAndFractionByLocale("98765432109876543210987654321", -10, 0,
                Locale.US, "-987,654,321,098,765,432,109,876,543,210");

        Locale inLocale = new Locale("en", "IN");
        if (Support_Locale.isLocaleAvailable(inLocale)) {
            assertDecFmtWithMultiplierAndFractionByLocale("123456789012345", 10, 0,
                    inLocale, "1,23,45,67,89,01,23,450");
            assertDecFmtWithMultiplierAndFractionByLocale("12345678901234567890", 10, 0,
                    inLocale, "12,34,56,78,90,12,34,56,78,900");
            assertDecFmtWithMultiplierAndFractionByLocale("98765432109876543210987654321", 10, 0,
                    inLocale, "9,87,65,43,21,09,87,65,43,21,09,87,65,43,210");

            assertDecFmtWithMultiplierAndFractionByLocale("123456789012345", -10, 0,
                    inLocale, "-1,23,45,67,89,01,23,450");
            assertDecFmtWithMultiplierAndFractionByLocale("12345678901234567890", -10, 0,
                    inLocale, "-12,34,56,78,90,12,34,56,78,900");
            assertDecFmtWithMultiplierAndFractionByLocale("98765432109876543210987654321", -10, 0,
                    inLocale, "-9,87,65,43,21,09,87,65,43,21,09,87,65,43,210");
        }
}

public void testBigDecimalICUConsistency() {
//Synthetic comment -- @@ -143,4 +168,12 @@
BigDecimal d = new BigDecimal(value);
assertEquals(expectedResult, df.format(d));
}

    private void assertDecFmtWithMultiplierAndFractionByLocale(String value, int multiplier, int fraction, Locale locale, String expectedResult) {
        DecimalFormat df = (DecimalFormat)NumberFormat.getIntegerInstance(locale);
        df.setMultiplier(multiplier);
        df.setMaximumFractionDigits(fraction);
        BigDecimal d = new BigDecimal(value);
        assertEquals(expectedResult, df.format(d));
    }
}







