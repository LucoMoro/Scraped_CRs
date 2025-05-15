//<Beginning of snippet n. 0>


String A2 = mMethod + ":" + mUrl;

// Initialize nc to "00000001"
int ncCount = 1;

// Increment logic for nc
String nc = String.format("%08x", ncCount++);
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);


//<End of snippet n. 0>