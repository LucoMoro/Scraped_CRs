//<Beginning of snippet n. 0>

/**
* Returns the native pointer to the Buffer's data at its current
* position, or 0 if there is none
*
* @param Buffer b the Buffer to be queried
* @return the native pointer to the Buffer's data at its current position, or 0 if there is none
*/
/**
* Returns the underlying Java array containing the data of the
* given Buffer, or null if the Buffer is not backed by a Java array.
*
* @param Buffer b the Buffer to be queried
* @return the Java array containing the Buffer's data, or null if there is none
*/
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
    if (b.hasArray()) {
        int position = b.position();
        int offset = b.arrayOffset();
        int length = b.array().length;
        if (position + offset >= 0 && position + offset < length) {
            return (offset + position) << b._elementSizeShift;
        }
    }
    return 0;
}

//<End of snippet n. 0>