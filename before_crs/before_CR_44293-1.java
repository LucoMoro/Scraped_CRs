/*Fix three harmony tests that failed on Android and the RI.

(cherry-pick of 55b505d649b4babcc8420673050cac50c9c41c9c.)

Bug: 7288264
Bug: 7307154
Change-Id:I4cde772e8860b98cb01230c7628446a58cebc8c4*/
//Synthetic comment -- diff --git a/text/src/test/java/org/apache/harmony/text/tests/java/text/BreakIteratorTest.java b/text/src/test/java/org/apache/harmony/text/tests/java/text/BreakIteratorTest.java
//Synthetic comment -- index bc30d1a..6593f5f 100644

//Synthetic comment -- @@ -94,11 +94,7 @@
fail("should throw illegal argument exception");
} catch (IllegalArgumentException e) {
}
        try {
            iterator.following(TEXT.length());
            fail("should throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
}

public void testIsBoundary() {
//Synthetic comment -- @@ -116,11 +112,7 @@
fail("should throw illegal argument exception");
} catch (IllegalArgumentException e) {
}
        try {
            iterator.isBoundary(TEXT.length());
            fail("should throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
}

public void testLast() {
//Synthetic comment -- @@ -158,11 +150,8 @@
fail("should throw illegal argument exception");
} catch (IllegalArgumentException e) {
}
        try {
            iterator.preceding(TEXT.length());
            fail("should throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
}

public void testPrevious() {







