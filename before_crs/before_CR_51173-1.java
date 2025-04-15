/*Fix BigDecimal precision when constructed from String.

Bug:https://code.google.com/p/android/issues/detail?id=43480Change-Id:Id42e4458323d7ab3dafb2dec84909096988e4365*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/math/BigDecimal.java b/luni/src/main/java/java/math/BigDecimal.java
//Synthetic comment -- index 335e3bc..6b716bc 100644

//Synthetic comment -- @@ -352,10 +352,6 @@
} else {
setUnscaledValue(new BigInteger(unscaledBuffer.toString()));
}
        precision = unscaledBuffer.length() - counter;
        if (unscaledBuffer.charAt(0) == '-') {
            precision --;
        }
}

/**
//Synthetic comment -- @@ -1825,13 +1821,11 @@
* @return the precision of this {@code BigDecimal}.
*/
public int precision() {
        // Checking if the precision already was calculated
        if (precision > 0) {
return precision;
}

        int bitLength = this.bitLength;

if (bitLength == 0) {
precision = 1;
} else if (bitLength < 64) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/math/BigDecimalTest.java b/luni/src/test/java/libcore/java/math/BigDecimalTest.java
//Synthetic comment -- index e771e9f..e9c2b0c 100644

//Synthetic comment -- @@ -64,4 +64,28 @@
BigDecimal rounded = bigDecimal.round(new MathContext(2, RoundingMode.FLOOR));
assertEquals("0.99", rounded.toString());
}
}







