/*Request CTS waiver since the result of our device running Eclair is correct. (Back port the solution of bug 2139334 from Froyo,http://code.google.com/p/android/issues/detail?id=8014)

Change-Id:I034515a5990fa78013e9f77fcdb23396c22b6535*/




//Synthetic comment -- diff --git a/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StrictMathTest.java b/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/StrictMathTest.java
//Synthetic comment -- index c6edeb2..137676c 100644

//Synthetic comment -- @@ -17,11 +17,10 @@

package org.apache.harmony.luni.tests.java.lang;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(StrictMath.class) 
public class StrictMathTest extends junit.framework.TestCase {
//Synthetic comment -- @@ -1293,7 +1292,6 @@
method = "tanh",
args = {double.class}
)
public void test_tanh_D() {
// Test for special situations
assertTrue(Double.isNaN(StrictMath.tanh(Double.NaN)));







