//<Beginning of snippet n. 0>
static int getBaseArrayOffset(Buffer b) {
    if (b.hasArray()) {
        int position = b.position();
        int offset = b.arrayOffset();
        if (position < 0 || position >= b.array().length) {
            throw new IllegalArgumentException("Invalid position");
        }
        return (position + offset) << b._elementSizeShift;
    } else if (b instanceof DalvikBuffer) {
        int position = b.position();
        int size = ((DalvikBuffer) b).getArrayLength();
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Invalid position");
        }
        return position << b._elementSizeShift;
    }
    throw new IllegalArgumentException("Buffer does not have array or is invalid");
}

static Object getArrayData(Buffer b) {
    if (b.hasArray()) {
        int position = b.position();
        Object array = b.array();
        if (position >= 0 && position < array.length) {
            return Arrays.copyOfRange(array, position, array.length); // Consider using views instead of copying
        }
    } else if (b instanceof DalvikBuffer) {
        int position = b.position();
        int size = ((DalvikBuffer) b).getArrayLength();
        if (position >= 0 && position < size) {
            return Arrays.copyOfRange(((DalvikBuffer) b).getBackingArray(), position, size); // Consider using views instead of copying
        }
    }
    throw new IllegalArgumentException("Invalid position or buffer type");
}
//<End of snippet n. 0>