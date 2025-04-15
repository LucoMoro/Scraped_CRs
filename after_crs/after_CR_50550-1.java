/*Add locale sensitive int and double parsing methods

Use these to parse ints and doubles/floats from strings
rather than Integer.valueOf or Integer.parseInt (and ditto
for Float/Double) if the string represents a localized
string (e.g. using "," instead of "." in some locales,
and so on.)

Change-Id:I231cdb79a2d09c776ec7894639dde6adf77afd10*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/SdkUtils.java b/common/src/main/java/com/android/utils/SdkUtils.java
//Synthetic comment -- index 160f95d..d610527 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
//Synthetic comment -- @@ -213,4 +216,77 @@
return sb.toString();
}

    /**
     * Returns the given localized string as an int. For example, in the
     * US locale, "1,000", will return 1000. In the French locale, "1.000" will return
     * 1000. It will return 0 for empty strings.
     * <p>
     * To parse a string without catching parser exceptions, call
     * {@link #parseLocalizedInt(String, int)} instead, passing the
     * default value to be returned if the format is invalid.
     *
     * @param string the string to be parsed
     * @return the integer value
     * @throws ParseException if the format is not correct
     */
    public static int parseLocalizedInt(@NonNull String string) throws ParseException {
        if (string.isEmpty()) {
            return 0;
        }
        return NumberFormat.getIntegerInstance().parse(string).intValue();
    }

    /**
     * Returns the given localized string as an int. For example, in the
     * US locale, "1,000", will return 1000. In the French locale, "1.000" will return
     * 1000.  If the format is invalid, returns the supplied default value instead.
     *
     * @param string the string to be parsed
     * @param defaultValue the value to be returned if there is a parsing error
     * @return the integer value
     */
    public static int parseLocalizedInt(@NonNull String string, int defaultValue) {
        try {
            return parseLocalizedInt(string);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the given localized string as a double. For example, in the
     * US locale, "3.14", will return 3.14. In the French locale, "3,14" will return
     * 3.14. It will return 0 for empty strings.
     * <p>
     * To parse a string without catching parser exceptions, call
     * {@link #parseLocalizedDouble(String, double)} instead, passing the
     * default value to be returned if the format is invalid.
     *
     * @param string the string to be parsed
     * @return the double value
     * @throws ParseException if the format is not correct
     */
    public static double parseLocalizedDouble(@NonNull String string) throws ParseException {
        if (string.isEmpty()) {
            return 0.0;
        }
        return NumberFormat.getNumberInstance().parse(string).doubleValue();
    }

    /**
     * Returns the given localized string as a double. For example, in the
     * US locale, "3.14", will return 3.14. In the French locale, "3,14" will return
     * 3.14. If the format is invalid, returns the supplied default value instead.
     *
     * @param string the string to be parsed
     * @param defaultValue the value to be returned if there is a parsing error
     * @return the double value
     */
    public static double parseLocalizedDouble(@NonNull String string, double defaultValue) {
        try {
            return parseLocalizedDouble(string);
        } catch (ParseException e) {
            return defaultValue;
        }
    }
}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/SdkUtilsTest.java b/common/src/test/java/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 030e1b7..b250972 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.Locale;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
//Synthetic comment -- @@ -129,5 +132,88 @@
wrapped);
}

    public void testParseInt() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000, SdkUtils.parseLocalizedInt("1000"));
        assertEquals(0, SdkUtils.parseLocalizedInt("0"));
        assertEquals(0, SdkUtils.parseLocalizedInt(""));
        assertEquals(1, SdkUtils.parseLocalizedInt("1"));
        assertEquals(-1, SdkUtils.parseLocalizedInt("-1"));
        assertEquals(1000, SdkUtils.parseLocalizedInt("1,000"));
        assertEquals(1000000, SdkUtils.parseLocalizedInt("1,000,000"));

        Locale.setDefault(Locale.ITALIAN);
        assertSame(Locale.ITALIAN, Locale.getDefault());
        assertEquals(1000, SdkUtils.parseLocalizedInt("1000"));
        assertEquals(0, SdkUtils.parseLocalizedInt("0"));
        assertEquals(1, SdkUtils.parseLocalizedInt("1"));
        assertEquals(-1, SdkUtils.parseLocalizedInt("-1"));
        assertEquals(1000, SdkUtils.parseLocalizedInt("1.000"));
        assertEquals(1000000, SdkUtils.parseLocalizedInt("1.000.000"));

        // Make sure it throws exceptions
        try {
            SdkUtils.parseLocalizedInt("X");
            fail("Should have thrown exception");
        } catch (ParseException e) {
            // Expected
        }
    }

    public void testParseIntWithDefault() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000, SdkUtils.parseLocalizedInt("1000", 0)); // Valid
        assertEquals(2, SdkUtils.parseLocalizedInt("2.X", 2)); // Parses prefix
        assertEquals(5, SdkUtils.parseLocalizedInt("X", 5)); // Parses prefix

        Locale.setDefault(Locale.ITALIAN);
        assertEquals(1000, SdkUtils.parseLocalizedInt("1000", -1)); // Valid
        assertEquals(7, SdkUtils.parseLocalizedInt("X", 7));
    }

    public void testParseDouble() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000"));
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000.0"));
        assertEquals(1000.5, SdkUtils.parseLocalizedDouble("1000.5"));
        assertEquals(0.0, SdkUtils.parseLocalizedDouble("0"));
        assertEquals(0.0, SdkUtils.parseLocalizedDouble(""));
        assertEquals(1.0, SdkUtils.parseLocalizedDouble("1"));
        assertEquals(-1.0, SdkUtils.parseLocalizedDouble("-1"));
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1,000"));
        assertEquals(1000.5, SdkUtils.parseLocalizedDouble("1,000.5"));
        assertEquals(1000000.0, SdkUtils.parseLocalizedDouble("1,000,000"));
        assertEquals(1000000.5, SdkUtils.parseLocalizedDouble("1,000,000.5"));

        Locale.setDefault(Locale.ITALIAN);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000"));
        assertEquals(1000.5, SdkUtils.parseLocalizedDouble("1000,5"));
        assertEquals(0.0, SdkUtils.parseLocalizedDouble("0"));
        assertEquals(1.0, SdkUtils.parseLocalizedDouble("1"));
        assertEquals(-1.0, SdkUtils.parseLocalizedDouble("-1"));
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1.000"));
        assertEquals(1000.5, SdkUtils.parseLocalizedDouble("1.000,5"));
        assertEquals(1000000.0, SdkUtils.parseLocalizedDouble("1.000.000"));
        assertEquals(1000000.5, SdkUtils.parseLocalizedDouble("1.000.000,5"));

        // Make sure it throws exceptions
        try {
            SdkUtils.parseLocalizedDouble("X");
            fail("Should have thrown exception");
        } catch (ParseException e) {
            // Expected
        }
    }

    public void testParseDoubleWithDefault() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000", 0)); // Valid
        assertEquals(2.0, SdkUtils.parseLocalizedDouble("2x", 3)); // Uses prefix
        assertEquals(0.0, SdkUtils.parseLocalizedDouble("", 4));
        assertEquals(5.0, SdkUtils.parseLocalizedDouble("test", 5)); // Invalid

        Locale.setDefault(Locale.FRANCE);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000", -1)); // Valid
        assertEquals(0.0, SdkUtils.parseLocalizedDouble("", 8));
    }
}







