/*Fix Some ScannerTest Locale Issues

Change-Id:I422331c3cf7703e1da73d2b483e1722f45909488*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/ScannerTest.java b/luni/src/test/java/tests/api/java/util/ScannerTest.java
//Synthetic comment -- index 5587a42..4a16e9e 100644

//Synthetic comment -- @@ -1877,6 +1877,12 @@
args = {int.class}
)
public void test_nextBigIntegerI() throws IOException {
s = new Scanner("123 456");
assertEquals(new BigInteger("123"), s.nextBigInteger(10));
assertEquals(new BigInteger("456"), s.nextBigInteger(10));
//Synthetic comment -- @@ -1925,10 +1931,12 @@
} catch (InputMismatchException e) {
// Expected
}
        s.useLocale(new Locale("de", "CH"));
        // If exception is thrown out, input will not be advanced.
        assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
        assertEquals(new BigInteger("23456"), s.nextBigInteger(10));

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -1996,14 +2004,17 @@
s.useLocale(Locale.CHINESE);
assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

        s = new Scanner("-123");
        s.useLocale(new Locale("ar", "AE"));
        assertEquals(new BigInteger("-123"), s.nextBigInteger(10));


        s = new Scanner("-123");
        s.useLocale(new Locale("mk", "MK"));
        assertEquals(new BigInteger("-123"), s.nextBigInteger(10));

s.close();
try {
//Synthetic comment -- @@ -2025,6 +2036,12 @@
args = {}
)
public void test_nextBigInteger() throws IOException {
s = new Scanner("123 456");
assertEquals(new BigInteger("123"), s.nextBigInteger());
assertEquals(new BigInteger("456"), s.nextBigInteger());
//Synthetic comment -- @@ -2074,10 +2091,12 @@
} catch (InputMismatchException e) {
// Expected
}
        s.useLocale(new Locale("de", "CH"));
        // If exception is thrown out, input will not be advanced.
        assertEquals(new BigInteger("23456"), s.nextBigInteger());
        assertEquals(new BigInteger("23456"), s.nextBigInteger());

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -2148,13 +2167,17 @@
s.useLocale(Locale.CHINESE);
assertEquals(new BigInteger("12300"), s.nextBigInteger());

        s = new Scanner("-123");
        s.useLocale(new Locale("ar", "AE"));
        assertEquals(new BigInteger("-123"), s.nextBigInteger());

        s = new Scanner("-123");
        s.useLocale(new Locale("mk", "MK"));
        assertEquals(new BigInteger("-123"), s.nextBigInteger());

s.close();
try {
//Synthetic comment -- @@ -3383,6 +3406,12 @@
args = {int.class}
)
public void test_hasNextBigIntegerI() throws IOException {
s = new Scanner("123 456");
assertTrue(s.hasNextBigInteger(10));
assertEquals(new BigInteger("123"), s.nextBigInteger(10));
//Synthetic comment -- @@ -3440,12 +3469,14 @@
} catch (InputMismatchException e) {
// Expected
}
        s.useLocale(new Locale("de", "CH"));
        // If exception is thrown out, input will not be advanced.
        assertTrue(s.hasNextBigInteger(10));
        assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
        assertTrue(s.hasNextBigInteger(10));
        assertEquals(new BigInteger("23456"), s.nextBigInteger(10));

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -3526,16 +3557,19 @@
assertTrue(s.hasNextBigInteger(10));
assertEquals(new BigInteger("12300"), s.nextBigInteger(10));

        s = new Scanner("-123");
        s.useLocale(new Locale("ar", "AE"));
        assertTrue(s.hasNextBigInteger(10));
        assertEquals(new BigInteger("-123"), s.nextBigInteger(10));


        s = new Scanner("-123");
        s.useLocale(new Locale("mk", "MK"));
        assertTrue(s.hasNextBigInteger(10));
        assertEquals(new BigInteger("-123"), s.nextBigInteger(10));
}

/**
//Synthetic comment -- @@ -3588,6 +3622,12 @@
args = {}
)
public void test_hasNextBigInteger() throws IOException {
s = new Scanner("123 456");
assertTrue(s.hasNextBigInteger());
assertEquals(new BigInteger("123"), s.nextBigInteger());
//Synthetic comment -- @@ -3646,12 +3686,15 @@
} catch (InputMismatchException e) {
// Expected
}
        s.useLocale(new Locale("de", "CH"));
        // If exception is thrown out, input will not be advanced.
        assertTrue(s.hasNextBigInteger());
        assertEquals(new BigInteger("23456"), s.nextBigInteger());
        assertTrue(s.hasNextBigInteger());
        assertEquals(new BigInteger("23456"), s.nextBigInteger());

/*
* The input string has Arabic-Indic digits.
//Synthetic comment -- @@ -3734,15 +3777,19 @@
assertTrue(s.hasNextBigInteger());
assertEquals(new BigInteger("12300"), s.nextBigInteger());

        s = new Scanner("-123");
        s.useLocale(new Locale("ar", "AE"));
        assertTrue(s.hasNextBigInteger());
        assertEquals(new BigInteger("-123"), s.nextBigInteger());

        s = new Scanner("-123");
        s.useLocale(new Locale("mk", "MK"));
        assertTrue(s.hasNextBigInteger());
        assertEquals(new BigInteger("-123"), s.nextBigInteger());

s.close();
try {







