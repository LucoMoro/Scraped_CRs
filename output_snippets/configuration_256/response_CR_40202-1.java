<<Beginning of snippet n. 0>>
provide("Signature", "SHA512WITHECDSA");
// Fixups for dalvik
if (!IS_RI) {
    // SSLContext configuration
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.getDefaultSSLParameters().setProtocols(new String[]{"TLSv1.3"});
}
// Additional code to use sslContext as needed
<<End of snippet n. 0>>