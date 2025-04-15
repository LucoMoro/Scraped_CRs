/*Fix lossage in OldDecimalFormatSymbolsTest.

Change-Id:I5ed2c5784fcf545009f2b4d64c7db03933e8dc03*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/OldDecimalFormatSymbolsTest.java b/luni/src/test/java/libcore/java/text/OldDecimalFormatSymbolsTest.java
//Synthetic comment -- index 2f1a3cc..c078684 100644

//Synthetic comment -- @@ -33,16 +33,18 @@
public void test_RIHarmony_compatible() throws Exception {
ObjectInputStream i = null;
try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(
                    Locale.FRANCE);
i = new ObjectInputStream(
getClass()
.getClassLoader()
.getResourceAsStream(
"serialization/java/text/DecimalFormatSymbols.ser"));
            DecimalFormatSymbols symbolsD = (DecimalFormatSymbols) i
                    .readObject();
            assertEquals(symbols, symbolsD);
} catch(NullPointerException e) {
assertNotNull("Failed to load /serialization/java/text/" +
"DecimalFormatSymbols.ser", i);
//Synthetic comment -- @@ -54,7 +56,6 @@
} catch (Exception e) {
}
}
        assertDecimalFormatSymbolsRIFrance(dfs);
}


//Synthetic comment -- @@ -121,8 +122,7 @@
'#', dfs.getMonetaryDecimalSeparator());
}

    static void assertDecimalFormatSymbolsRIFrance(DecimalFormatSymbols dfs) {
        // Values based on Java 1.5 RI DecimalFormatSymbols for Locale.FRANCE
/*
* currency = [EUR]
* currencySymbol = [U+20ac] // EURO SIGN
//Synthetic comment -- @@ -133,12 +133,13 @@
* internationalCurrencySymbol = [EUR]
* minusSign = [-][U+002d]
* monetaryDecimalSeparator = [,][U+002c]
         * naN = [U+fffd] // REPLACEMENT CHARACTER
* patternSeparator = [;][U+003b]
* perMill = [U+2030] // PER MILLE
* percent = [%][U+0025]
* zeroDigit = [0][U+0030]
*/
assertEquals("EUR", dfs.getCurrency().getCurrencyCode());
assertEquals("\u20AC", dfs.getCurrencySymbol());
assertEquals(',', dfs.getDecimalSeparator());
//Synthetic comment -- @@ -151,7 +152,7 @@
// RI's default NaN is U+FFFD, Harmony's is based on ICU
// This suggests an RI bug, assuming that non-UTF8 bytes are UTF8 and
// getting a conversion failure.
        assertEquals("\uFFFD", dfs.getNaN());
assertEquals('\u003b', dfs.getPatternSeparator());
assertEquals('\u2030', dfs.getPerMill());
assertEquals('%', dfs.getPercent());







