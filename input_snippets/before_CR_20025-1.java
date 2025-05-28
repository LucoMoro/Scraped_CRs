
//<Beginning of snippet n. 0>


String A2 = mMethod  + ":" + mUrl;

// because we do not preemptively send authorization headers, nc is always 1
        String nc = "000001";
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);


//<End of snippet n. 0>








