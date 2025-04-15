/*Documented the implementation-defined behaviors of the native get* method
implementations.

Change-Id:I5d20d7796b85ce62750dd50dd79e47d76716b24c*/




//Synthetic comment -- diff --git a/core/java/android/database/CursorWindow.java b/core/java/android/database/CursorWindow.java
//Synthetic comment -- index c756825..962d942 100644

//Synthetic comment -- @@ -245,6 +245,15 @@
}
}

    /**
     * Returns the value at (<code>row</code>, <code>col</code>) as a <code>byte</code> array.
     *
     * <p>If the value is null, then <code>null</code> is returned. If the
     * type of column <code>col</code> is a string type, then the result
     * is the array of bytes that make up the internal representation of the
     * string value. If the type of column <code>col</code> is integral or floating-point,
     * then an {@link SQLiteException} is thrown.
     */
private native byte[] getBlob_native(int row, int col);

/**
//Synthetic comment -- @@ -332,6 +341,19 @@
}
}

    /**
     * Returns the value at (<code>row</code>, <code>col</code>) as a <code>String</code>.
     *
     * <p>If the value is null, then <code>null</code> is returned. If the
     * type of column <code>col</code> is integral, then the result is the string
     * that is obtained by formatting the integer value with the <code>printf</code>
     * family of functions using format specifier <code>%lld</code>. If the
     * type of column <code>col</code> is floating-point, then the result is the string
     * that is obtained by formatting the floating-point value with the
     * <code>printf</code> family of functions using format specifier <code>%g</code>.
     * If the type of column <code>col</code> is a blob type, then an
     * {@link SQLiteException} is thrown.
     */
private native String getString_native(int row, int col);

/**
//Synthetic comment -- @@ -383,6 +405,18 @@
}
}

    
    /**
     * Returns the value at (<code>row</code>, <code>col</code>) as a <code>long</code>.
     *
     * <p>If the value is null, then <code>0L</code> is returned. If the
     * type of column <code>col</code> is a string type, then the result
     * is the <code>long</code> that is obtained by parsing the string value with
     * <code>strtoll</code>. If the type of column <code>col</code> is
     * floating-point, then the result is the floating-point value casted to a <code>long</code>.
     * If the type of column <code>col</code> is a blob type, then an
     * {@link SQLiteException} is thrown.
     */
private native long getLong_native(int row, int col);

/**
//Synthetic comment -- @@ -402,6 +436,17 @@
}
}

    /**
     * Returns the value at (<code>row</code>, <code>col</code>) as a <code>double</code>.
     *
     * <p>If the value is null, then <code>0.0</code> is returned. If the
     * type of column <code>col</code> is a string type, then the result
     * is the <code>double</code> that is obtained by parsing the string value with
     * <code>strtod</code>. If the type of column <code>col</code> is
     * integral, then the result is the integer value casted to a <code>double</code>.
     * If the type of column <code>col</code> is a blob type, then an
     * {@link SQLiteException} is thrown.
     */
private native double getDouble_native(int row, int col);

/**







