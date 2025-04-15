/*Fix of CTS KnownFailure(value = "bug 2139334") in tanh tests

Change-Id:Icd2e4bcc7f8c120b992f3244fe3fd6464a14edcb*/




//Synthetic comment -- diff --git a/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/MathTest.java b/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/MathTest.java
//Synthetic comment -- index 330e0b5..d379f2b 100644

//Synthetic comment -- @@ -1104,8 +1104,8 @@
method = "tanh",
args = {double.class}
)
public void test_tanh_D() {
        final long SIGN_BIT = Double.doubleToLongBits(-0.0);
// Test for special situations
assertTrue("Should return NaN", Double.isNaN(Math.tanh(Double.NaN)));
assertEquals("Should return +1.0", +1.0, Math
//Synthetic comment -- @@ -1116,7 +1116,8 @@
.tanh(0.0)));
assertEquals(Double.doubleToLongBits(+0.0), Double
.doubleToLongBits(Math.tanh(+0.0)));
        // Math.tanh(-0.0) can return either -0.0 or +0.0, depending on hardware and compilation options
        assertEquals(Double.doubleToLongBits(+0.0), ~SIGN_BIT & Double
.doubleToLongBits(Math.tanh(-0.0)));

assertEquals("Should return 1.0", 1.0, Math.tanh(1234.56), 0D);








//Synthetic comment -- diff --git a/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StrictMathTest.java b/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StrictMathTest.java
//Synthetic comment -- index c6edeb2..e8dbf46 100644

//Synthetic comment -- @@ -1293,8 +1293,8 @@
method = "tanh",
args = {double.class}
)
public void test_tanh_D() {
        final long SIGN_BIT = Double.doubleToLongBits(-0.0);
// Test for special situations
assertTrue(Double.isNaN(StrictMath.tanh(Double.NaN)));
assertEquals("Should return +1.0", +1.0, StrictMath
//Synthetic comment -- @@ -1305,7 +1305,8 @@
.doubleToLongBits(StrictMath.tanh(0.0)));
assertEquals(Double.doubleToLongBits(+0.0), Double
.doubleToLongBits(StrictMath.tanh(+0.0)));
        // StrictMath.tanh(-0.0) can return either -0.0 or +0.0, depending on hardware and compilation options
        assertEquals(Double.doubleToLongBits(+0.0), ~SIGN_BIT & Double
.doubleToLongBits(StrictMath.tanh(-0.0)));

assertEquals("Should return 1.0", 1.0, StrictMath.tanh(1234.56), 0D);







