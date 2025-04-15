/*Add locale sensitive int and double parsing methods

Use these to parse ints and doubles/floats from strings
rather than Integer.valueOf or Integer.parseInt (and ditto
for Float/Double) if the string represents a localized
string (e.g. using "," instead of "." in some locales,
and so on.)

Also fix some null warnings.

Change-Id:Ie89b8cddfda96ccaff597a79e8c7ae7aae4040fa*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/SdkUtils.java b/common/src/main/java/com/android/utils/SdkUtils.java
//Synthetic comment -- index 160f95d..91aa3bd 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
//Synthetic comment -- @@ -213,4 +216,71 @@
return sb.toString();
}

    /**
     * Returns the given localized string as an int. For example, in the
     * US locale, "1,000", will return 1000. In the French locale, "1.000" will return
     * 1000.
     * <p>
     * To parse a string without catching parser exceptions, call
     * {@link #parseLocalizedInt(String, int)} instead, passing the
     * default value to be returned if the format is invalid.
     *
     * @param string the string to be parsed
     * @return the integer value
     * @throws ParseException if the format is not correct
     */
    public static int parseLocalizedInt(String string) throws ParseException {
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
    public static int parseLocalizedInt(String string, int defaultValue) {
        try {
            return parseLocalizedInt(string);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the given localized string as a double. For example, in the
     * US locale, "3.14", will return 3.14. In the French locale, "3,14" will return
     * 3.14.
     * <p>
     * To parse a string without catching parser exceptions, call
     * {@link #parseLocalizedDouble(String, double)} instead, passing the
     * default value to be returned if the format is invalid.
     *
     * @param string the string to be parsed
     * @return the double value
     * @throws ParseException if the format is not correct
     */
    public static double parseLocalizedDouble(String string) throws ParseException {
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
    public static double parseLocalizedDouble(String string, double defaultValue) {
        try {
            return parseLocalizedDouble(string);
        } catch (ParseException e) {
            return defaultValue;
        }
    }
}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/SdkUtilsTest.java b/common/src/test/java/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 030e1b7..c3f7fd8 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import junit.framework.TestCase;

import java.util.Locale;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
//Synthetic comment -- @@ -129,5 +131,70 @@
wrapped);
}

    public void testParseInt() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000, SdkUtils.parseLocalizedInt("1000"));
        assertEquals(0, SdkUtils.parseLocalizedInt("0"));
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
    }

    public void testParseDoubleWithDefault() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000", 0)); // Valid
        assertEquals(2.0, SdkUtils.parseLocalizedDouble("2x", 3)); // Invalid
        assertEquals(4.0, SdkUtils.parseLocalizedDouble("", 4)); // Invalid
        assertEquals(5.0, SdkUtils.parseLocalizedDouble("test", 5)); // Invalid

        Locale.setDefault(Locale.FRANCE);
        assertEquals(1000.0, SdkUtils.parseLocalizedDouble("1000", -1)); // Valid
        assertEquals(8.0, SdkUtils.parseLocalizedDouble("", 8));
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/devices/DeviceParser.java b/sdklib/src/main/java/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index 1a663f1..511fed6 100644

//Synthetic comment -- @@ -193,7 +193,10 @@
mHardware.addCamera(mCamera);
mCamera = null;
} else if (DeviceSchema.NODE_LOCATION.equals(localName)) {
                CameraLocation location = CameraLocation.getEnum(getString(mStringAccumulator));
                if (location != null) {
                    mCamera.setLocation(location);
                }
} else if (DeviceSchema.NODE_AUTOFOCUS.equals(localName)) {
mCamera.setFlash(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_FLASH.equals(localName)) {
//Synthetic comment -- @@ -206,7 +209,10 @@
int val = getInteger(mStringAccumulator);
mHardware.setRam(new Storage(val, mUnit));
} else if (DeviceSchema.NODE_BUTTONS.equals(localName)) {
                ButtonType buttonType = ButtonType.getEnum(getString(mStringAccumulator));
                if (buttonType != null) {
                    mHardware.setButtonType(buttonType);
                }
} else if (DeviceSchema.NODE_INTERNAL_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
int val = Integer.parseInt(s);
//Synthetic comment -- @@ -238,7 +244,10 @@
}
}
} else if (DeviceSchema.NODE_POWER_TYPE.equals(localName)) {
                PowerType type = PowerType.getEnum(getString(mStringAccumulator));
                if (type != null) {
                    mHardware.setChargeType(type);
                }
} else if (DeviceSchema.NODE_API_LEVEL.equals(localName)) {
String val = getString(mStringAccumulator);
// Can be one of 5 forms:







