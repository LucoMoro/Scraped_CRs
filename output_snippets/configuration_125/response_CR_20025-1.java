//<Beginning of snippet n. 0>
String A2 = mMethod + ":" + mUrl;
if (requestCounter >= 0xFFFFFFFF) {
    requestCounter = 0;
}
String nc = String.format("%08x", requestCounter);
requestCounter++;
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);
//<End of snippet n. 0>