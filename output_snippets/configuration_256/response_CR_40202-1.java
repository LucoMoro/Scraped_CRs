//<Beginning of snippet n. 0>

provide("Signature", "SHA512WITHECDSA");

// Fixups for dalvik
if (!IS_RI) {
    SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
    sslContext.init(null, null, new SecureRandom());
}

//<End of snippet n. 0>