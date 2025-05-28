//<Beginning of snippet n. 0>
static int getBaseArrayOffset(Buffer b) {
    if (b.hasArray()) {
        int position = b.position();
        int offset = b.arrayOffset();
        if (position < 0 || position >= b.array().length) {
            return -1;
        }
        return (position + offset) << b._elementSizeShift;
    } else if (b instanceof DalvikBuffer) {
        int position = b.position();
        int size = ((DalvikBuffer) b).getArrayLength();
        if (position < 0 || position >= size) {
            return -1;
        }
        return position << b._elementSizeShift;
    }
    return -1;
}

static Object getArrayData(Buffer b) {
    if (b.hasArray()) {
        int position = b.position();
        Object array = b.array();
        if (position >= 0 && position < array.length) {
            return Arrays.copyOfRange(array, position, array.length);
        }
    } else if (b instanceof DalvikBuffer) {
        int position = b.position();
        int size = ((DalvikBuffer) b).getArrayLength();
        if (position >= 0 && position < size) {
            return Arrays.copyOfRange(((DalvikBuffer) b).getBackingArray(), position, size);
        }
    }
    return null;
}
//<End of snippet n. 0>