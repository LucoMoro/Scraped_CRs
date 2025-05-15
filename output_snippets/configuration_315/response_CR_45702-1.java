//<Beginning of snippet n. 0>

if (bindArgs == null || statement == null) {
    throw new IllegalArgumentException("bindArgs or statement cannot be null");
}

if (count < 0 || count > bindArgs.length) {
    throw new SQLiteBindOrColumnIndexOutOfRangeException("Count must be non-negative and less than or equal to " + bindArgs.length); 
}

if (count != statement.mNumParameters) {
    throw new SQLiteBindOrColumnIndexOutOfRangeException(
        "Expected " + statement.mNumParameters + " bind arguments but "
        + bindArgs.length + " were provided.");
}
if (count == 0) {
    return;

//<End of snippet n. 0>