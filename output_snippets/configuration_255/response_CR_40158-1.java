//<Beginning of snippet n. 0>
/**
 * Returns the offset in bytes from the start of the underlying
 * Java array object containing the data of the given Buffer to
 * the actual start of the data. This method is only meaningful if
 * getBaseArray() returns non-null.
 *
 * @param Buffer b the Buffer to be queried
 * @return the data offset in bytes to the start of this Buffer's data
 */
static int getBaseArrayOffset(Buffer b) {
    if (b == null) {
        throw new IllegalArgumentException("Buffer cannot be null");
    }
    if (!b.hasArray()) {
        return 0;
    }

    int position = b.position();
    int limit = b.limit();
    
    if (position < 0 || position >= limit) {
        throw new IndexOutOfBoundsException("Buffer position out of bounds");
    }
    
    return (b.arrayOffset() + position) << b._elementSizeShift;
}
//<End of snippet n. 0>