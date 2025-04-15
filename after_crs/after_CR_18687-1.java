/*Make clear in the Javadoc comments of the `Cursor` get* methods that
implementations thereof can have implementation-defined behavior. In some cases,
these changes actually correct the documentation. For example, in the case of
`getShort` and the `SQLiteCursor` implementation thereof, non-numeric data is
*not* converted to a `short` via Short#valueOf or even in a functionally-
equivalent manner.

Change-Id:Ib2f81811a603680b52fc482eb9c0f3195447566f*/




//Synthetic comment -- diff --git a/core/java/android/database/Cursor.java b/core/java/android/database/Cursor.java
//Synthetic comment -- index 6539156..93e2c1f 100644

//Synthetic comment -- @@ -211,7 +211,9 @@
/**
* Returns the value of the requested column as a byte array.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null or the column type is not a blob type is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a byte array.
//Synthetic comment -- @@ -221,8 +223,9 @@
/**
* Returns the value of the requested column as a String.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null or the column type is not a string type is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a String.
//Synthetic comment -- @@ -242,8 +245,10 @@
/**
* Returns the value of the requested column as a short.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null, the column type is not an integral type, or the
     * integer value is outside the range [<code>Short.MIN_VALUE</code>, <code>Short.MAX_VALUE</code>] is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a short.
//Synthetic comment -- @@ -253,8 +258,10 @@
/**
* Returns the value of the requested column as an int.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null, the column type is not an integral type, or the
     * integer value is outside the range [<code>Integer.MIN_VALUE</code>, <code>Integer.MAX_VALUE</code>] is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as an int.
//Synthetic comment -- @@ -264,8 +271,10 @@
/**
* Returns the value of the requested column as a long.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null, the column type is not an integral type, or the
     * integer value is outside the range [<code>Long.MIN_VALUE</code>, <code>Long.MAX_VALUE</code>] is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a long.
//Synthetic comment -- @@ -275,8 +284,10 @@
/**
* Returns the value of the requested column as a float.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null, the column type is not a floating-point type, or the
     * floating-point value is not representable as a <code>float</code> value is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a float.
//Synthetic comment -- @@ -286,8 +297,10 @@
/**
* Returns the value of the requested column as a double.
*
     * <p>The result and whether this method throws an exception when the
     * column value is null, the column type is not a floating-point type, or the
     * floating-point value is not representable as a <code>double</code> value is
     * implementation-defined.
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a double.
//Synthetic comment -- @@ -573,7 +586,7 @@
* that are required to fetch data for the cursor.
*
* <p>These values may only change when requery is called.
     * @return cursor-defined values, or {@link android.os.Bundle#EMPTY Bundle.EMPTY} if there are no values. Never <code>null</code>.
*/
Bundle getExtras();

//Synthetic comment -- @@ -583,8 +596,8 @@
*
* <p>One use of this is to tell a cursor that it should retry its network request after it
* reported an error.
     * @param extras extra values, or {@link android.os.Bundle#EMPTY Bundle.EMPTY}. Never <code>null</code>.
     * @return extra values, or {@link android.os.Bundle#EMPTY Bundle.EMPTY}. Never <code>null</code>.
*/
Bundle respond(Bundle extras);
}







