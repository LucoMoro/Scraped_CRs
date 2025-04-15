/*CTS : Avoid ScannerTest to crash if Local Germany is supported and not Chinese one
This append if config default is used in icu4c

Change-Id:I718f47d6e863532c8fb63e64aabff900eaaed223Signed-off-by: Fabien DUVOUX <fabien.duvoux@gmail.com>*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/ScannerTest.java b/luni/src/test/java/tests/api/java/util/ScannerTest.java
//Synthetic comment -- index 76e677f..8a1c2bb 100755

//Synthetic comment -- @@ -1964,6 +1964,7 @@
}
}
if (Support_Locale.areLocalesAvailable(Locale.GERMANY)) {
            s = new Scanner("23.45\u0666 23.456");
s.useLocale(Locale.GERMANY);
// If exception is thrown out, input will not be advanced.
assertEquals(new BigInteger("23456"), s.nextBigInteger(10));
//Synthetic comment -- @@ -2131,6 +2132,7 @@
}
}
if (Support_Locale.areLocalesAvailable(Locale.GERMANY)) {
            s = new Scanner("23.45\u0666 23.456");
s.useLocale(Locale.GERMANY);
// If exception is thrown out, input will not be advanced.
assertEquals(new BigInteger("23456"), s.nextBigInteger());
//Synthetic comment -- @@ -3517,6 +3519,7 @@
}
}
if (Support_Locale.areLocalesAvailable(Locale.GERMANY)) {
            s = new Scanner("23.45\u0666 23.456");
s.useLocale(Locale.GERMANY);
// If exception is thrown out, input will not be advanced.
assertTrue(s.hasNextBigInteger(10));
//Synthetic comment -- @@ -3740,6 +3743,7 @@
}
}
if (Support_Locale.areLocalesAvailable(Locale.GERMANY)) {
            s = new Scanner("23.45\u0666 23.456");
s.useLocale(Locale.GERMANY);
// If exception is thrown out, input will not be advanced.
assertTrue(s.hasNextBigInteger());







