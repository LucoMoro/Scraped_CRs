//<Beginning of snippet n. 0>
if (statement == null) {
    throw new IllegalArgumentException("Statement cannot be null");
}
if (bindArgs == null) {
    throw new IllegalArgumentException("Bind arguments cannot be null");
}

if (statement.mNumParameters < 0) {
    throw new IllegalArgumentException("Statement parameters count cannot be negative");
}

if (count < 0 || count >= statement.mNumParameters) {
    throw new SQLiteBindOrColumnIndexOutOfRangeException(
        "Expected 0 to " + (statement.mNumParameters - 1) + " bind arguments but "
        + bindArgs.length + " were provided.");
}
//<End of snippet n. 0>