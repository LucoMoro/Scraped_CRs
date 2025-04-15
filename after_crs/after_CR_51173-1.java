/*Fix BigDecimal precision when constructed from String.

Bug:https://code.google.com/p/android/issues/detail?id=43480Change-Id:Id42e4458323d7ab3dafb2dec84909096988e4365*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/math/BigDecimal.java b/luni/src/main/java/java/math/BigDecimal.java
//Synthetic comment -- index 335e3bc..6b716bc 100644

//Synthetic comment -- @@ -352,10 +352,6 @@
} else {
setUnscaledValue(new BigInteger(unscaledBuffer.toString()));
}
}

/**
//Synthetic comment -- @@ -1825,13 +1821,11 @@
* @return the precision of this {@code BigDecimal}.
*/
public int precision() {
        // Return the cached value if we have one.
        if (precision != 0) {
return precision;
}

if (bitLength == 0) {
precision = 1;
} else if (bitLength < 64) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/math/BigDecimalTest.java b/luni/src/test/java/libcore/java/math/BigDecimalTest.java
//Synthetic comment -- index e771e9f..e9c2b0c 100644

//Synthetic comment -- @@ -64,4 +64,28 @@
BigDecimal rounded = bigDecimal.round(new MathContext(2, RoundingMode.FLOOR));
assertEquals("0.99", rounded.toString());
}

    // https://code.google.com/p/android/issues/detail?id=43480
    public void testPrecisionFromString() {
      BigDecimal a = new BigDecimal("-0.011111111111111111111");
      BigDecimal b = a.multiply(BigDecimal.ONE);

      assertEquals("-0.011111111111111111111", a.toString());
      assertEquals("-0.011111111111111111111", b.toString());

      assertEquals(20, a.precision());
      assertEquals(20, b.precision());

      assertEquals(21, a.scale());
      assertEquals(21, b.scale());

      assertEquals("-11111111111111111111", a.unscaledValue().toString());
      assertEquals("-11111111111111111111", b.unscaledValue().toString());

      assertEquals(a, b);
      assertEquals(b, a);

      assertEquals(0, a.subtract(b).signum());
      assertEquals(0, a.compareTo(b));
    }
}







