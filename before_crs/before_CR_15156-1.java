/*Fixed issue #3707: Added support for custom CursorJoiner comparators.

This change refactors CursorJoiner to support cursors sorted by
non-string types and/or in descending order.

Change-Id:I60250ffcb79137fda876551d67a39096b5b7f794*/
//Synthetic comment -- diff --git a/core/java/android/database/CursorJoiner.java b/core/java/android/database/CursorJoiner.java
//Synthetic comment -- index e3c2988..ce301e6 100644

//Synthetic comment -- @@ -16,12 +16,14 @@

package android.database;

import java.util.Iterator;

/**
 * Does a join on two cursors using the specified columns. The cursors must already
 * be sorted on each of the specified columns in ascending order. This joiner only
 * supports the case where the tuple of key column values is unique.
* <p>
* Typical usage:
*
//Synthetic comment -- @@ -44,13 +46,12 @@
*/
public final class CursorJoiner
implements Iterator<CursorJoiner.Result>, Iterable<CursorJoiner.Result> {
    private Cursor mCursorLeft;
    private Cursor mCursorRight;
private boolean mCompareResultIsValid;
private Result mCompareResult;
    private int[] mColumnsLeft;
    private int[] mColumnsRight;
    private String[] mValues;

/**
* The result of a call to next().
//Synthetic comment -- @@ -66,33 +67,39 @@

/**
* Initializes the CursorJoiner and resets the cursors to the first row. The left and right
     * column name arrays must have the same number of columns.
* @param cursorLeft The left cursor to compare
* @param columnNamesLeft The column names to compare from the left cursor
* @param cursorRight The right cursor to compare
* @param columnNamesRight The column names to compare from the right cursor
*/
public CursorJoiner(
Cursor cursorLeft, String[] columnNamesLeft,
Cursor cursorRight, String[] columnNamesRight) {
        if (columnNamesLeft.length != columnNamesRight.length) {
            throw new IllegalArgumentException(
                    "you must have the same number of columns on the left and right, "
                            + columnNamesLeft.length + " != " + columnNamesRight.length);
        }

mCursorLeft = cursorLeft;
mCursorRight = cursorRight;

        mCursorLeft.moveToFirst();
        mCursorRight.moveToFirst();

mCompareResultIsValid = false;

        mColumnsLeft = buildColumnIndiciesArray(cursorLeft, columnNamesLeft);
        mColumnsRight = buildColumnIndiciesArray(cursorRight, columnNamesRight);

        mValues = new String[mColumnsLeft.length * 2];
}

public Iterator<Result> iterator() {
//Synthetic comment -- @@ -100,20 +107,6 @@
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

    /**
* Returns whether or not there are more rows to compare using next().
* @return true if there are more rows to compare
*/
//Synthetic comment -- @@ -164,18 +157,13 @@
boolean hasRight = !mCursorRight.isAfterLast();

if (hasLeft && hasRight) {
            populateValues(mValues, mCursorLeft, mColumnsLeft, 0 /* start filling at index 0 */);
            populateValues(mValues, mCursorRight, mColumnsRight, 1 /* start filling at index 1 */);
            switch (compareStrings(mValues)) {
                case -1:
                    mCompareResult = Result.LEFT;
                    break;
                case 0:
                    mCompareResult = Result.BOTH;
                    break;
                case 1:
                    mCompareResult = Result.RIGHT;
                    break;
}
} else if (hasLeft) {
mCompareResult = Result.LEFT;
//Synthetic comment -- @@ -192,24 +180,6 @@
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
* Increment the cursors past the rows indicated in the most recent call to next().
* This will only have an affect once per call to next().
*/
//Synthetic comment -- @@ -232,34 +202,103 @@
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








//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/CursorJoinerTest.java b/tests/AndroidTests/src/com/android/unit_tests/CursorJoinerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..69f7bd03

//Synthetic comment -- @@ -0,0 +1,115 @@







