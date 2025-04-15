/*Skip the tests if it includes unsupported locale/charset

A device may not support any specific locale (i.e. China, German, etc,.)

Change-Id:I894bfc76d3503d879913ff33a2b5e8887ea2ca49*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/io/InputStreamReaderTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/io/InputStreamReaderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index e4e8046..36b3022

//Synthetic comment -- @@ -423,14 +423,14 @@
"tests/api/java/io/testfile.txt");
try {
reader = new InputStreamReader(in, "gb18030");
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            assertEquals(source, sb.toString());
} catch (UnsupportedEncodingException e) {
System.out
.println("GB18030 is not supported, abort test InputStreamReaderTest.testSpecialCharsetReading().");
}
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StringBufferTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StringBufferTest.java
//Synthetic comment -- index c5499e7..0067d4c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package org.apache.harmony.luni.tests.java.lang;

import java.io.Serializable;
import java.nio.charset.Charset;

import junit.framework.TestCase;

//Synthetic comment -- @@ -97,10 +98,12 @@

buffer.append("abcde");
assertEquals("abcde", buffer.toString());
        if (Charset.isSupported("GB18030")) {
            buffer.setLength(1000);
            byte[] bytes = buffer.toString().getBytes("GB18030");
            for (int i = 5; i < bytes.length; i++) {
                assertEquals(0, bytes[i]);
            }
}

buffer.setLength(5);








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StringBuilderTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StringBuilderTest.java
//Synthetic comment -- index d9acab0..b376a38 100644

//Synthetic comment -- @@ -19,7 +19,7 @@

import java.io.Serializable;
import java.util.Arrays;
import java.nio.charset.Charset;
import junit.framework.TestCase;

import org.apache.harmony.testframework.serialization.SerializationTest;
//Synthetic comment -- @@ -1912,10 +1912,12 @@
sb.setLength(0);
sb.append("abcde");
assertEquals("abcde", sb.toString());
        if (Charset.isSupported("GB18030")) {
            sb.setLength(1000);
            byte[] bytes = sb.toString().getBytes("GB18030");
            for (int i = 5; i < bytes.length; i++) {
                assertEquals(0, bytes[i]);
            }
}

sb.setLength(5);








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/CalendarTest.java b/luni/src/test/java/tests/api/java/util/CalendarTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index bca091f..5f0a161

//Synthetic comment -- @@ -647,18 +647,32 @@
assertTrue(exist);
}

    private boolean isAvailableLocale(Locale locale) {
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale l : availableLocales) {
            if (locale.equals(l)) {
                return true;
            }
        }
        return false;
    }

/**
* @tests java.util.Calendar#getInstance(Locale)
* @tests java.util.Calendar#getInstance(TimeZone, Locale)
*/
public void test_getInstance() {
// test getInstance(Locale)
        if (isAvailableLocale(Locale.US)) {
            Calendar calendar = Calendar.getInstance(Locale.US);
            assertEquals(Calendar.SUNDAY, calendar
                    .getFirstDayOfWeek());
        }
        if (isAvailableLocale(Locale.CHINESE)) {
            Calendar calendar = Calendar.getInstance(Locale.CHINESE);
            assertEquals(Calendar.MONDAY, calendar
                    .getFirstDayOfWeek());
        }

// test getInstance(Locale, TimeZone)
Calendar gmt_calendar = Calendar.getInstance(TimeZone








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/ScannerTest.java b/luni/src/test/java/tests/api/java/util/ScannerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5587a42..6d702d2

//Synthetic comment -- @@ -1901,34 +1901,40 @@
* Different locale can only recognize corresponding locale sensitive
* string. ',' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.ENGLISH})) {
            s = new Scanner("23,456 23,456");
            s.useLocale(Locale.GERMANY);
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.ENGLISH);
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

/*
* ''' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, new Locale("de", "CH")})) {
            s = new Scanner("23'456 23'456");
            s.useLocale(Locale.GERMANY);
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(new Locale("de", "CH"));
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -1947,63 +1953,74 @@
* '.' is used in many locales as group separator. The input string
* has Arabic-Indic digits .
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[]{Locale.CHINESE, Locale.GERMANY})) {
            s = new Scanner("23.45\u0666 23.456");
            s.useLocale(Locale.CHINESE);
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.GERMANY);
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

// The input string starts with zero
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.ENGLISH})) {
            s = new Scanner("03,456");
            s.useLocale(Locale.ENGLISH);
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }

            s = new Scanner("03456");
            assertEquals(new BigInteger("3456"), s.nextBigInteger(10));

            s = new Scanner("\u06603,456");
            s.useLocale(Locale.ENGLISH);
            assertEquals(new BigInteger("3456"), s.nextBigInteger(10));

            s = new Scanner("E34");
            assertEquals(new BigInteger("3636"), s.nextBigInteger(16));
}

/*
* There are 3 types of zero digit in all locales, '0' '\u0966' '\u0e50'
* respectively, but they are not differentiated.
*/
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.CHINESE})) {
            s = new Scanner("12300");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

            s = new Scanner("123\u0966\u0966");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

            s = new Scanner("123\u0e50\u0e50");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));
        }

        if (Support_Locale.areLocalesAvailable(new Locale[] {new Locale("ar", "AE")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("ar", "AE"));
            assertEquals(new BigInteger("-123"), s.nextBigInteger(10));
        }


        if (Support_Locale.areLocalesAvailable(new Locale[] { new Locale("mk", "MK")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("mk", "MK"));
            assertEquals(new BigInteger("-123"), s.nextBigInteger(10));
        }

s.close();
try {
//Synthetic comment -- @@ -2050,34 +2067,40 @@
* Different locale can only recognize corresponding locale sensitive
* string. ',' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.ENGLISH})) {
            s = new Scanner("23,456 23,456");
            s.useLocale(Locale.GERMANY);
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.ENGLISH);
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

/*
* ''' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, new Locale("de", "CH")})) {
            s = new Scanner("23'456 23'456");
            s.useLocale(Locale.GERMANY);
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(new Locale("de", "CH"));
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -2098,63 +2121,76 @@
* '.' is used in many locales as group separator. The input string
* has Arabic-Indic digits .
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.CHINESE, Locale.GERMANY})) {
            s = new Scanner("23.45\u0666 23.456");
            s.useLocale(Locale.CHINESE);
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.GERMANY);
            // If exception is thrown out, input will not be advanced.
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

// The input string starts with zero
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.ENGLISH})) {
            s = new Scanner("03,456");
            s.useLocale(Locale.ENGLISH);
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }

            s = new Scanner("03456");
            assertEquals(new BigInteger("3456"), s.nextBigInteger());

            s = new Scanner("\u06603,456");
            s.useLocale(Locale.ENGLISH);
            assertEquals(new BigInteger("3456"), s.nextBigInteger());

            s = new Scanner("E34");
            s.useRadix(16);
            assertEquals(new BigInteger("3636"), s.nextBigInteger());
}

/*
* There are 3 types of zero digit in all locales, '0' '\u0966' '\u0e50'
* respectively, but they are not differentiated.
*/
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.CHINESE})) {
            s = new Scanner("12300");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger());

            s = new Scanner("123\u0966\u0966");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger());

            s = new Scanner("123\u0e50\u0e50");
            s.useLocale(Locale.CHINESE);
            assertEquals(new BigInteger("12300"), s.nextBigInteger());
        }

        if (Support_Locale.areLocalesAvailable(
                new Locale[] {new Locale("ar", "AE")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("ar", "AE"));
            assertEquals(new BigInteger("-123"), s.nextBigInteger());
        }

        if (Support_Locale.areLocalesAvailable(
                new Locale[] { new Locale("mk", "MK")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("mk", "MK"));
            assertEquals(new BigInteger("-123"), s.nextBigInteger());
        }

s.close();
try {
//Synthetic comment -- @@ -3412,40 +3448,46 @@
* Different locale can only recognize corresponding locale sensitive
* string. ',' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.ENGLISH})) {
            s = new Scanner("23,456 23,456");
            s.useLocale(Locale.GERMANY);
            assertFalse(s.hasNextBigInteger(10));
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.ENGLISH);
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

/*
* ''' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, new Locale("de", "CH")})) {
            s = new Scanner("23'456 23'456");
            s.useLocale(Locale.GERMANY);
            assertFalse(s.hasNextBigInteger(10));
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(new Locale("de", "CH"));
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -3467,75 +3509,88 @@
* '.' is used in many locales as group separator. The input string
* has Arabic-Indic digits .
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.CHINESE})) {
            s = new Scanner("23.45\u0666 23.456");
            s.useLocale(Locale.CHINESE);
            assertFalse(s.hasNextBigInteger(10));
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.GERMANY);
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
}

// The input string starts with zero
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.ENGLISH})) {
            s = new Scanner("03,456");
            s.useLocale(Locale.ENGLISH);
            assertFalse(s.hasNextBigInteger(10));
            try {
                s.nextBigInteger(10);
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }

            s = new Scanner("03456");
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("3456"), s.nextBigInteger(10));

            s = new Scanner("\u06603,456");
            s.useLocale(Locale.ENGLISH);
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("3456"), s.nextBigInteger(10));

            s = new Scanner("E34");
            assertTrue(s.hasNextBigInteger(16));
            assertEquals(new BigInteger("3636"), s.nextBigInteger(16));
}

/*
* There are 3 types of zero digit in all locales, '0' '\u0966' '\u0e50'
* respectively, but they are not differentiated.
*/
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.CHINESE})) {
            s = new Scanner("12300");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

            s = new Scanner("123\u0966\u0966");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

            s = new Scanner("123\u0e50\u0e50");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("12300"), s.nextBigInteger(10));
        }

        if (Support_Locale.areLocalesAvailable(
                new Locale[] {new Locale("ar", "AE")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("ar", "AE"));
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("-123"), s.nextBigInteger(10));
        }


        if (Support_Locale.areLocalesAvailable(
                new Locale[] { new Locale("mk", "MK")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("mk", "MK"));
            assertTrue(s.hasNextBigInteger(10));
            assertEquals(new BigInteger("-123"), s.nextBigInteger(10));
        }
}

/**
//Synthetic comment -- @@ -3618,40 +3673,46 @@
* Different locale can only recognize corresponding locale sensitive
* string. ',' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.ENGLISH})) {
            s = new Scanner("23,456 23,456");
            s.useLocale(Locale.GERMANY);
            assertFalse(s.hasNextBigInteger());
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.ENGLISH);
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

/*
* ''' is used in many locales as group separator.
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, new Locale("de", "CH")})) {
            s = new Scanner("23'456 23'456");
            s.useLocale(Locale.GERMANY);
            assertFalse(s.hasNextBigInteger());
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(new Locale("de", "CH"));
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -3674,75 +3735,86 @@
* '.' is used in many locales as group separator. The input string
* has Arabic-Indic digits .
*/
        if (Support_Locale.areLocalesAvailable(
                new Locale[] {Locale.GERMANY, Locale.CHINESE})) {
            s = new Scanner("23.45\u0666 23.456");
            s.useLocale(Locale.CHINESE);
            assertFalse(s.hasNextBigInteger());
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }
            s.useLocale(Locale.GERMANY);
            // If exception is thrown out, input will not be advanced.
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("23456"), s.nextBigInteger());
}

// The input string starts with zero
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.ENGLISH})) {
            s = new Scanner("03,456");
            s.useLocale(Locale.ENGLISH);
            assertFalse(s.hasNextBigInteger());
            try {
                s.nextBigInteger();
                fail("Should throw InputMismatchException");
            } catch (InputMismatchException e) {
                // Expected
            }

            s = new Scanner("03456");
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("3456"), s.nextBigInteger());

            s = new Scanner("\u06603,456");
            s.useLocale(Locale.ENGLISH);
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("3456"), s.nextBigInteger());

            s = new Scanner("E34");
            s.useRadix(16);
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("3636"), s.nextBigInteger());
}

/*
* There are 3 types of zero digit in all locales, '0' '\u0966' '\u0e50'
* respectively, but they are not differentiated.
*/
        if (Support_Locale.areLocalesAvailable(new Locale[] {Locale.CHINESE})) {
            s = new Scanner("12300");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("12300"), s.nextBigInteger());

            s = new Scanner("123\u0966\u0966");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("12300"), s.nextBigInteger());

            s = new Scanner("123\u0e50\u0e50");
            s.useLocale(Locale.CHINESE);
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("12300"), s.nextBigInteger());
        }

        if (Support_Locale.areLocalesAvailable(new Locale[] {new Locale("ar", "AE")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("ar", "AE"));
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("-123"), s.nextBigInteger());
        }

        if (Support_Locale.areLocalesAvailable(new Locale[] { new Locale("mk", "MK")})) {
            s = new Scanner("-123");
            s.useLocale(new Locale("mk", "MK"));
            assertTrue(s.hasNextBigInteger());
            assertEquals(new BigInteger("-123"), s.nextBigInteger());
        }

s.close();
try {







