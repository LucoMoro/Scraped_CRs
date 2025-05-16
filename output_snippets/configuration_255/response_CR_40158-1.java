//<Beginning of snippet n. 0>
/**
 * Returns the native pointer to the Buffer's data at its current
 * position, or 0 if there is none.
 *
 * @param b the Buffer to be queried
 * @return the native pointer to the Buffer's data at its current position, or 0 if there is none
 */
static long getNativePointer(Buffer b) {
    if (b == null || b.position() >= b.limit()) {
        return 0;
    }
    return b.address() + b.position() * b._elementSize;
}

/**
 * Returns the underlying Java array containing the data of the
 * given Buffer, or null if the Buffer is not backed by a Java array.
 *
 * @param b the Buffer to be queried
 * @return the Java array containing the Buffer's data, or null if there is none
 */
static Object getBaseArray(Buffer b) {
    return b.hasArray() ? b.array() : null;
}

/**
 * Returns the offset in bytes from the start of the underlying
 * Java array object containing the data of the given Buffer to
 * the actual start of the data. This method is only meaningful if
 * getBaseArray() returns non-null.
 *
 * @param b the Buffer to be queried
 * @return the data offset in bytes to the start of this Buffer's data
 */
static int getBaseArrayOffset(Buffer b) {
    return b.hasArray() ? (b.arrayOffset() + b.position() << b._elementSizeShift) : 0;
}

//<End of snippet n. 0>