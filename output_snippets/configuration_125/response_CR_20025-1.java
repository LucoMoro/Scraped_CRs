//<Beginning of snippet n. 0>


String A2 = mMethod  + ":" + mUrl;

private int ncCounter = 0; // Tracks the count for 'nc'

private String getNextNc() {
    ncCounter++;
    return String.format("%08x", ncCounter); // Format as 8-character hexadecimal string
}

String nc = getNextNc();
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);


//<End of snippet n. 0>