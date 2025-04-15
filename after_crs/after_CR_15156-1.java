/*Fixed issue #3707: Added support for custom CursorJoiner comparators.

This change refactors CursorJoiner to support cursors sorted by
non-string types and/or in descending order.

Change-Id:I60250ffcb79137fda876551d67a39096b5b7f794*/




//Synthetic comment -- diff --git a/core/java/android/database/CursorJoiner.java b/core/java/android/database/CursorJoiner.java
//Synthetic comment -- index e3c2988..ce301e6 100644

//Synthetic comment -- @@ -16,12 +16,14 @@

package android.database;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Does a join on two cursors using the specified columns or a custom
 * comparator. The cursors must already be sorted on each of the compared
 * columns. This joiner only supports the case where the tuple of key column
 * values is unique.
* <p>
* Typical usage:
*
//Synthetic comment -- @@ -44,13 +46,12 @@
*/
public final class CursorJoiner
implements Iterator<CursorJoiner.Result>, Iterable<CursorJoiner.Result> {
    private final Cursor mCursorLeft;
    private final Cursor mCursorRight;
private boolean mCompareResultIsValid;
private Result mCompareResult;

    private final Comparator<Cursor> mComparator;

/**
* The result of a call to next().
//Synthetic comment -- @@ -66,33 +67,39 @@

/**
* Initializes the CursorJoiner and resets the cursors to the first row. The left and right
     * column name arrays must have the same number of columns. Assumes the columns being compared
     * are strings and are sorted in ascending order.
* @param cursorLeft The left cursor to compare
* @param columnNamesLeft The column names to compare from the left cursor
* @param cursorRight The right cursor to compare
* @param columnNamesRight The column names to compare from the right cursor
     * @see #CursorJoiner(Cursor, Cursor, Comparator)
*/
public CursorJoiner(
Cursor cursorLeft, String[] columnNamesLeft,
Cursor cursorRight, String[] columnNamesRight) {
        this(cursorLeft, cursorRight, new LegacyCursorComparator(cursorLeft, columnNamesLeft,
                cursorRight, columnNamesRight));
    }

    /**
     * Initializes the CursorJoiner and resets the cursors to the first row.
     * Sets a custom comparator to choose how to compare like cursors, allowing
     * control over sort order and support for data types other than string.
     * @param cursorLeft The left cursor to compare
     * @param cursorRight The right cursor to compare
     * @param comparator Comparator used to determine how to iterate through the sorted cursors.
     */
    public CursorJoiner(Cursor cursorLeft, Cursor cursorRight, Comparator<Cursor> comparator) {
mCursorLeft = cursorLeft;
mCursorRight = cursorRight;

        cursorLeft.moveToFirst();
        cursorRight.moveToFirst();

mCompareResultIsValid = false;

        mComparator = comparator;
}

public Iterator<Result> iterator() {
//Synthetic comment -- @@ -100,20 +107,6 @@
}

/**
* Returns whether or not there are more rows to compare using next().
* @return true if there are more rows to compare
*/
//Synthetic comment -- @@ -164,18 +157,13 @@
boolean hasRight = !mCursorRight.isAfterLast();

if (hasLeft && hasRight) {
            int result = mComparator.compare(mCursorLeft, mCursorRight);
            if (result < 0) {
                mCompareResult = Result.LEFT;
            } else if (result > 0) {
                mCompareResult = Result.RIGHT;
            } else {
                mCompareResult = Result.BOTH;
}
} else if (hasLeft) {
mCompareResult = Result.LEFT;
//Synthetic comment -- @@ -192,24 +180,6 @@
}

/**
* Increment the cursors past the rows indicated in the most recent call to next().
* This will only have an affect once per call to next().
*/
//Synthetic comment -- @@ -232,34 +202,103 @@
}

/**
     * Cursor comparator used with the original implementation of CursorJoiner.
     * By assuming that the values being compared are strings, this
     * implementation has some major flaws as the default implementation.
*/
    private static class LegacyCursorComparator implements Comparator<Cursor> {
        private final int[] mColumnsLeft;
        private final int[] mColumnsRight;
        private final String[] mValues;

        public LegacyCursorComparator(Cursor cursorLeft, String[] columnNamesLeft,
                Cursor cursorRight, String[] columnNamesRight) {
            if (columnNamesLeft.length != columnNamesRight.length) {
                throw new IllegalArgumentException(
                        "you must have the same number of columns on the left and right, "
                        + columnNamesLeft.length + " != " + columnNamesRight.length);
            }

            mColumnsLeft = buildColumnIndiciesArray(cursorLeft, columnNamesLeft);
            mColumnsRight = buildColumnIndiciesArray(cursorRight, columnNamesRight);

            mValues = new String[mColumnsLeft.length * 2];
}

        /**
         * Lookup the indicies of the each column name and return them in an array.
         * @param cursor the cursor that contains the columns
         * @param columnNames the array of names to lookup
         * @return an array of column indices
         */
        private int[] buildColumnIndiciesArray(Cursor cursor, String[] columnNames) {
            int[] columns = new int[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                columns[i] = cursor.getColumnIndexOrThrow(columnNames[i]);
}
            return columns;
        }

        public int compare(Cursor leftCursor, Cursor rightCursor) {
            /*
             * Why do we copy our values first into mValues then compare? We
             * could be more efficient by failing fast if we compared each
             * column one-at-time. For now, I'm leaving the original code in
             * tact though to more easily verify correctness of this patch.
             */
            populateValues(mValues, leftCursor, mColumnsLeft, 0 /* start filling at index 0 */);
            populateValues(mValues, rightCursor, mColumnsRight, 1 /* start filling at index 1 */);
            return compareStrings(mValues);
        }

        /**
         * Reads the strings from the cursor that are specifed in the columnIndicies
         * array and saves them in values beginning at startingIndex, skipping a slot
         * for each value. If columnIndicies has length 3 and startingIndex is 1, the
         * values will be stored in slots 1, 3, and 5.
         * @param values the String[] to populate
         * @param cursor the cursor from which to read
         * @param columnIndicies the indicies of the values to read from the cursor
         * @param startingIndex the slot in which to start storing values, and must be either 0 or 1.
         */
        private static void populateValues(String[] values, Cursor cursor, int[] columnIndicies,
                int startingIndex) {
            assert startingIndex == 0 || startingIndex == 1;
            for (int i = 0; i < columnIndicies.length; i++) {
                values[startingIndex + i*2] = cursor.getString(columnIndicies[i]);
}
}

        /**
         * Compare the values. Values contains n pairs of strings. If all the pairs of strings match
         * then returns 0. Otherwise returns the comparison result of the first non-matching pair
         * of values, -1 if the first of the pair is less than the second of the pair or 1 if it
         * is greater.
         * @param values the n pairs of values to compare
         * @return -1, 0, or 1 as described above.
         */
        private static int compareStrings(String... values) {
            if ((values.length % 2) != 0) {
                throw new IllegalArgumentException("you must specify an even number of values");
            }

            for (int index = 0; index < values.length; index+=2) {
                if (values[index] == null) {
                    if (values[index+1] == null) continue;
                    return -1;
                }

                if (values[index+1] == null) {
                    return 1;
                }

                int comp = values[index].compareTo(values[index+1]);
                if (comp != 0) {
                    return comp < 0 ? -1 : 1;
                }
            }

            return 0;
        }
}
}








//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/CursorJoinerTest.java b/tests/AndroidTests/src/com/android/unit_tests/CursorJoinerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..69f7bd03

//Synthetic comment -- @@ -0,0 +1,115 @@
package com.android.unit_tests;

import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;

import java.util.Comparator;
import java.util.Iterator;

import junit.framework.TestCase;

public class CursorJoinerTest extends TestCase {
    /**
     * Tests joining on integer columns.
     */
    public void testIntegerColumn() {
        final String[] columnNames = { "key1" };

        MatrixCursor cursorLeft = new MatrixCursor(columnNames);
        cursorLeft.newRow().add(4);
        cursorLeft.newRow().add(32);
        cursorLeft.newRow().add(200);

        MatrixCursor cursorRight = new MatrixCursor(columnNames);
        cursorRight.newRow().add(5);
        cursorRight.newRow().add(32);
        cursorRight.newRow().add(200);
        cursorRight.newRow().add(1000);

        CursorJoiner.Result[] expectedResults = {
            CursorJoiner.Result.LEFT,
            CursorJoiner.Result.RIGHT,
            CursorJoiner.Result.BOTH,
            CursorJoiner.Result.BOTH,
            CursorJoiner.Result.RIGHT,
        };

        CursorJoiner joiner = new CursorJoiner(cursorLeft, cursorRight, new Comparator<Cursor>() {
            public int compare(Cursor a, Cursor b) {
                return compareLong(a.getLong(0), b.getLong(0));
            }
        });

        compareResults(joiner, expectedResults);
    }

    /**
     * Tests a custom comparator using two integer join columns; key1,
     * sorted in a descending fashion; key2, ascending.
     */
    public void testCustomComparator() {
        final String[] columnNamesLeft = { "key1", "key2", "foo" };
        final String[] columnNamesRight = { "foo", "bar", "key2", "key1" };

        MatrixCursor cursorLeft = new MatrixCursor(columnNamesLeft);
        cursorLeft.newRow().add(234).add(10).add("234-10");
        cursorLeft.newRow().add(230).add(100).add("230-100");
        cursorLeft.newRow().add(89).add(999).add("89-999");
        cursorLeft.newRow().add(9).add(1000).add("9-1000");

        MatrixCursor cursorRight = new MatrixCursor(columnNamesRight);
        cursorRight.newRow().add("dummy1").add("234-9").add(9).add(234);
        cursorRight.newRow().add("dummy2").add("230-100").add(100).add(230);
        cursorRight.newRow().add("dummy3").add("100-100").add(100).add(100);
        cursorRight.newRow().add("dummy4").add("1-1").add(1).add(1);

        CursorJoiner.Result[] expectedResults = {
            CursorJoiner.Result.RIGHT,
            CursorJoiner.Result.LEFT,
            CursorJoiner.Result.BOTH,
            CursorJoiner.Result.RIGHT,
            CursorJoiner.Result.LEFT,
            CursorJoiner.Result.LEFT,
            CursorJoiner.Result.RIGHT,
        };

        CursorJoiner joiner = new CursorJoiner(cursorLeft, cursorRight, new Comparator<Cursor>() {
            public int compare(Cursor leftCursor, Cursor rightCursor) {
                int resultKey1 = compareLong(leftCursor.getLong(0), rightCursor.getLong(3));
                if (resultKey1 != 0) {
                    return -resultKey1;
                }
                int resultKey2 = compareLong(leftCursor.getLong(1), rightCursor.getLong(2));
                if (resultKey2 != 0) {
                    return resultKey2;
                }
                return 0;
            }
        });

        compareResults(joiner, expectedResults);
    }

    private static int compareLong(long a, long b) {
        if (a < b) {
            return -1;
        } else if (a > b) {
            return 1;
        } else {
            return 0;
        }
    }

    private static void compareResults(CursorJoiner joiner, CursorJoiner.Result[] expectedResults) {
        Iterator<CursorJoiner.Result> iterator = joiner.iterator();
        int n = expectedResults.length;
        for (int i = 0; i < n; i++) {
            CursorJoiner.Result expectedResult = expectedResults[i];
            assertTrue("Insufficient results from joiner at position " + i, iterator.hasNext());
            CursorJoiner.Result actualResult = iterator.next();
            assertEquals("Mismatch at position " + i, expectedResult, actualResult);
        }
        assertFalse("Too many results from joiner", iterator.hasNext());
    }
}







