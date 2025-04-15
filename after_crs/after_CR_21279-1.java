/*Request CTS waiver since the result of our device running Eclair is correct. (Back port the solution of bug 2139334 from Froyo,http://code.google.com/p/android/issues/detail?id=8014)

Change-Id:Iee7fa7ae40ba20ef7fcb2b7ff418876710964ec9*/




//Synthetic comment -- diff --git a/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/MathTest.java b/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/MathTest.java
//Synthetic comment -- index 330e0b5..02bed3c 100644

//Synthetic comment -- @@ -17,10 +17,9 @@

package org.apache.harmony.luni.tests.java.lang;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(Math.class) 
public class MathTest extends junit.framework.TestCase {
//Synthetic comment -- @@ -1104,7 +1103,6 @@
method = "tanh",
args = {double.class}
)
public void test_tanh_D() {
// Test for special situations
assertTrue("Should return NaN", Double.isNaN(Math.tanh(Double.NaN)));







