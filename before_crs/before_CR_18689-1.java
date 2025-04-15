/*Added `throws` clauses to the signatures of the get* methods that retrieve
values from columns.

Change-Id:Ic1a538d50e3d581b44d061d8868b275458b6b8c6*/
//Synthetic comment -- diff --git a/core/java/android/database/AbstractWindowedCursor.java b/core/java/android/database/AbstractWindowedCursor.java
//Synthetic comment -- index 27a02e2..3c2b6f6 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
public abstract class AbstractWindowedCursor extends AbstractCursor
{
@Override
    public byte[] getBlob(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -36,7 +36,7 @@
}

@Override
    public String getString(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -64,7 +64,7 @@
}

@Override
    public short getShort(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -79,7 +79,7 @@
}

@Override
    public int getInt(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -94,7 +94,7 @@
}

@Override
    public long getLong(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -109,7 +109,7 @@
}

@Override
    public float getFloat(int columnIndex)
{
checkPosition();

//Synthetic comment -- @@ -124,7 +124,7 @@
}

@Override
    public double getDouble(int columnIndex)
{
checkPosition();









//Synthetic comment -- diff --git a/core/java/android/database/CursorWindow.java b/core/java/android/database/CursorWindow.java
//Synthetic comment -- index 962d942..46552d1 100644

//Synthetic comment -- @@ -236,7 +236,7 @@
* @param col the column to read from
* @return a String value for the given field
*/
    public byte[] getBlob(int row, int col) {
acquireReference();
try {
return getBlob_native(row - mStartPos, col);
//Synthetic comment -- @@ -254,7 +254,7 @@
* string value. If the type of column <code>col</code> is integral or floating-point,
* then an {@link SQLiteException} is thrown.
*/
    private native byte[] getBlob_native(int row, int col);

/**
* Checks if a field contains either a blob or is null.
//Synthetic comment -- @@ -332,7 +332,7 @@
* @param col the column to read from
* @return a String value for the given field
*/
    public String getString(int row, int col) {
acquireReference();
try {
return getString_native(row - mStartPos, col);
//Synthetic comment -- @@ -354,7 +354,7 @@
* If the type of column <code>col</code> is a blob type, then an
* {@link SQLiteException} is thrown.
*/
    private native String getString_native(int row, int col);

/**
* copy the text for the given field in the provided char array.
//Synthetic comment -- @@ -396,7 +396,7 @@
* @param col the column to read from
* @return a long value for the given field
*/
    public long getLong(int row, int col) {
acquireReference();
try {
return getLong_native(row - mStartPos, col);
//Synthetic comment -- @@ -417,7 +417,7 @@
* If the type of column <code>col</code> is a blob type, then an
* {@link SQLiteException} is thrown.
*/
    private native long getLong_native(int row, int col);

/**
* Returns a double for the given field.
//Synthetic comment -- @@ -427,7 +427,7 @@
* @param col the column to read from
* @return a double value for the given field
*/
    public double getDouble(int row, int col) {
acquireReference();
try {
return getDouble_native(row - mStartPos, col);
//Synthetic comment -- @@ -447,7 +447,7 @@
* If the type of column <code>col</code> is a blob type, then an
* {@link SQLiteException} is thrown.
*/
    private native double getDouble_native(int row, int col);

/**
* Returns a short for the given field.
//Synthetic comment -- @@ -457,7 +457,7 @@
* @param col the column to read from
* @return a short value for the given field
*/
    public short getShort(int row, int col) {
acquireReference();
try {
return (short) getLong_native(row - mStartPos, col);
//Synthetic comment -- @@ -473,7 +473,7 @@
* @param col the column to read from
* @return an int value for the given field
*/
    public int getInt(int row, int col) {
acquireReference();
try {
return (int) getLong_native(row - mStartPos, col);
//Synthetic comment -- @@ -490,7 +490,7 @@
* @param col the column to read from
* @return a float value for the given field
*/
    public float getFloat(int row, int col) {
acquireReference();
try {
return (float) getDouble_native(row - mStartPos, col);







