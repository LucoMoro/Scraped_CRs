//<Beginning of snippet n. 0>
if (statement == null) {
    throw new IllegalArgumentException("Statement cannot be null");
}
if (bindArgs == null) {
    throw new IllegalArgumentException("Bind arguments cannot be null");
}
if (count != statement.mNumParameters) {
    throw new SQLiteBindOrColumnIndexOutOfRangeException(
        "Expected " + statement.mNumParameters + " bind arguments but "
        + bindArgs.length + " were provided."
    );
}
if (count == 0) {
    return;
}
//<End of snippet n. 0>