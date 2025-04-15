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
     * <p>If the native content of that column is not blob exception may throw
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a byte array.
//Synthetic comment -- @@ -221,8 +223,9 @@
/**
* Returns the value of the requested column as a String.
*
     * <p>If the native content of that column is not text the result will be
     * the result of passing the column value to String.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a String.
//Synthetic comment -- @@ -242,8 +245,10 @@
/**
* Returns the value of the requested column as a short.
*
     * <p>If the native content of that column is not numeric the result will be
     * the result of passing the column value to Short.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a short.
//Synthetic comment -- @@ -253,8 +258,10 @@
/**
* Returns the value of the requested column as an int.
*
     * <p>If the native content of that column is not numeric the result will be
     * the result of passing the column value to Integer.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as an int.
//Synthetic comment -- @@ -264,8 +271,10 @@
/**
* Returns the value of the requested column as a long.
*
     * <p>If the native content of that column is not numeric the result will be
     * the result of passing the column value to Long.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a long.
//Synthetic comment -- @@ -275,8 +284,10 @@
/**
* Returns the value of the requested column as a float.
*
     * <p>If the native content of that column is not numeric the result will be
     * the result of passing the column value to Float.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a float.
//Synthetic comment -- @@ -286,8 +297,10 @@
/**
* Returns the value of the requested column as a double.
*
     * <p>If the native content of that column is not numeric the result will be
     * the result of passing the column value to Double.valueOf(x).
*
* @param columnIndex the zero-based index of the target column.
* @return the value of that column as a double.
//Synthetic comment -- @@ -573,7 +586,7 @@
* that are required to fetch data for the cursor.
*
* <p>These values may only change when requery is called.
     * @return cursor-defined values, or Bundle.EMTPY if there are no values. Never null.
*/
Bundle getExtras();

//Synthetic comment -- @@ -583,8 +596,8 @@
*
* <p>One use of this is to tell a cursor that it should retry its network request after it
* reported an error.
     * @param extras extra values, or Bundle.EMTPY. Never null.
     * @return extra values, or Bundle.EMTPY. Never null.
*/
Bundle respond(Bundle extras);
}







