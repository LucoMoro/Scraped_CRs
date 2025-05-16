//<Beginning of snippet n. 0>

if (statement == null || bindArgs == null) {
    throw new IllegalArgumentException("Statement or bindArgs cannot be null.");
}
if (count < 0) {
    throw new IllegalArgumentException("Count cannot be negative.");
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