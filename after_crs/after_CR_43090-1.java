/*Explain the (non-obvious) purpose of IntrinsicTest.

Change-Id:If336e3a8d3f99ffbce5e115a5ab82a044a3e282f*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/IntrinsicTest.java b/luni/src/test/java/libcore/java/lang/IntrinsicTest.java
//Synthetic comment -- index 75a4e42..6425b85 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import junit.framework.TestCase;

/**
 * Tests that all intrinsic methods are still invokable via reflection.
 */
public final class IntrinsicTest extends TestCase {
public void testString_charAt() throws Exception {
"hello".charAt(0);







