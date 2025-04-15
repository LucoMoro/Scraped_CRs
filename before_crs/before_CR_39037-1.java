/*Fix Matcher.find(int) to ignore the region.

Also fix the tests, which were wrong.

Bug: 6404568
Change-Id:I8718aab754bdbc87c37d573d19c4999cc3c447ac*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Matcher.java b/luni/src/main/java/java/util/regex/Matcher.java
//Synthetic comment -- index 58bcf9e..cfd4432 100644

//Synthetic comment -- @@ -50,11 +50,6 @@
private int regionEnd;

/**
     * Holds the position where the next find operation will take place.
     */
    private int findPos;

    /**
* Holds the position where the next append operation will take place.
*/
private int appendPos;
//Synthetic comment -- @@ -212,7 +207,6 @@
resetForInput();

matchFound = false;
        findPos = regionStart;
appendPos = 0;

return this;
//Synthetic comment -- @@ -377,30 +371,17 @@
}

/**
     * Returns the next occurrence of the {@link Pattern} in the input. The
     * method starts the search from the given character in the input.
*
     * @param start
     *            The index in the input at which the find operation is to
     *            begin. If this is less than the start of the region, it is
     *            automatically adjusted to that value. If it is beyond the end
     *            of the region, the method will fail.
     * @return true if (and only if) a match has been found.
*/
public boolean find(int start) {
        findPos = start;

        if (findPos < regionStart) {
            findPos = regionStart;
        } else if (findPos >= regionEnd) {
            matchFound = false;
            return false;
}

        matchFound = findImpl(address, input, findPos, matchOffsets);
        if (matchFound) {
            findPos = matchOffsets[1];
        }
return matchFound;
}

//Synthetic comment -- @@ -414,9 +395,6 @@
*/
public boolean find() {
matchFound = findNextImpl(address, input, matchOffsets);
        if (matchFound) {
            findPos = matchOffsets[1];
        }
return matchFound;
}

//Synthetic comment -- @@ -429,9 +407,6 @@
*/
public boolean lookingAt() {
matchFound = lookingAtImpl(address, input, matchOffsets);
        if (matchFound) {
            findPos = matchOffsets[1];
        }
return matchFound;
}

//Synthetic comment -- @@ -444,9 +419,6 @@
*/
public boolean matches() {
matchFound = matchesImpl(address, input, matchOffsets);
        if (matchFound) {
            findPos = matchOffsets[1];
        }
return matchFound;
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java b/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java
//Synthetic comment -- index 54f976d..07d99e9 100644

//Synthetic comment -- @@ -247,19 +247,36 @@
}
}

        // Starting index out of region
Pattern pat3 = Pattern.compile("new");
        Matcher mat3 = pat3.matcher("Brave new world");

        assertTrue(mat3.find(-1));
assertTrue(mat3.find(6));
assertFalse(mat3.find(7));

mat3.region(7, 10);

        assertFalse(mat3.find(3));
        assertFalse(mat3.find(6));
        assertFalse(mat3.find(7));
}

public void testSEOLsymbols() {







