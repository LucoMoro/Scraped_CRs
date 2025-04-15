/*Fix Matcher.find(int) to ignore the region.

Also fix the tests, which were wrong.

Bug: 6404568
Change-Id:I8718aab754bdbc87c37d573d19c4999cc3c447ac*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Matcher.java b/luni/src/main/java/java/util/regex/Matcher.java
//Synthetic comment -- index 58bcf9e..cfd4432 100644

//Synthetic comment -- @@ -50,11 +50,6 @@
private int regionEnd;

/**
* Holds the position where the next append operation will take place.
*/
private int appendPos;
//Synthetic comment -- @@ -212,7 +207,6 @@
resetForInput();

matchFound = false;
appendPos = 0;

return this;
//Synthetic comment -- @@ -377,30 +371,17 @@
}

/**
     * Returns true if there is another match in the input, starting
     * from the given position. The region is ignored.
*
     * @throws IndexOutOfBoundsException if {@code start < 0 || start > input.length()}
*/
public boolean find(int start) {
        if (start < 0 || start > input.length()) {
            throw new IndexOutOfBoundsException("start=" + start + "; length=" + input.length());
}

        matchFound = findImpl(address, input, start, matchOffsets);
return matchFound;
}

//Synthetic comment -- @@ -414,9 +395,6 @@
*/
public boolean find() {
matchFound = findNextImpl(address, input, matchOffsets);
return matchFound;
}

//Synthetic comment -- @@ -429,9 +407,6 @@
*/
public boolean lookingAt() {
matchFound = lookingAtImpl(address, input, matchOffsets);
return matchFound;
}

//Synthetic comment -- @@ -444,9 +419,6 @@
*/
public boolean matches() {
matchFound = matchesImpl(address, input, matchOffsets);
return matchFound;
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java b/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java
//Synthetic comment -- index 54f976d..07d99e9 100644

//Synthetic comment -- @@ -247,19 +247,36 @@
}
}

        String string3 = "Brave new world";
Pattern pat3 = Pattern.compile("new");
        Matcher mat3 = pat3.matcher(string3);

        // find(int) throws for out of range indexes.
        try {
            mat3.find(-1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
        assertFalse(mat3.find(string3.length()));
        try {
            mat3.find(string3.length() + 1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

assertTrue(mat3.find(6));
assertFalse(mat3.find(7));

mat3.region(7, 10);
        assertFalse(mat3.find()); // No "new" in the region.

        assertTrue(mat3.find(3)); // find(int) ignores the region.
        assertTrue(mat3.find(6)); // find(int) ignores the region.
        assertFalse(mat3.find(7)); // No "new" >= 7.

        mat3.region(1, 4);
        assertFalse(mat3.find()); // No "new" in the region.
        assertTrue(mat3.find(5)); // find(int) ignores the region.
}

public void testSEOLsymbols() {







