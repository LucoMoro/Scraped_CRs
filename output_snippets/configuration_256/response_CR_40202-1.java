<<Beginning of snippet n. 0>>
provide("Signature", "SHA512WITHECDSA");
}

// Fixups for dalvik
if (!IS_RI) {
    // Initialize SSLContext with only TLSv1.3
    SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
    sslContext.init(null, null, new SecureRandom());
    SSLParameters sslParameters = new SSLParameters();
    sslParameters.setProtocols(new String[]{"TLSv1.3"});
    sslContext.getDefaultSSLParameters().setProtocols(sslParameters.getProtocols());
}

// Further code...
<<End of snippet n. 0>>