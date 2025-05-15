//<Beginning of snippet n. 0>
static int getBaseArrayOffset(Buffer b) {
    if (b.hasArray()) {
        return (b.arrayOffset() + b.position()) << b._elementSizeShift;
    }
    return 0;
}
//<End of snippet n. 0>