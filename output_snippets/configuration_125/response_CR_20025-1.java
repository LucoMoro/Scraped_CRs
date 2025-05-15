//<Beginning of snippet n. 0>

String A2 = mMethod  + ":" + mUrl;

int nonceCount = 1; // Initialize nonce count
String nc = String.format("%08x", nonceCount); // Format as 8-character hexadecimal string
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);

// Increment nonce count for subsequent requests
nonceCount++;

//<End of snippet n. 0>