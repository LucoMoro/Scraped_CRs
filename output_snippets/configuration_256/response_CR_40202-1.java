//<Beginning of snippet n. 0>
provide("Signature", "SHA512WITHECDSA");
// SSLContext initialization
SSLContext sslContext = SSLContext.getInstance("TLS");
// Set up the context to only use secure versions, TLSv1.3 and above
sslContext.init(null, null, new SecureRandom());
SSLParameters sslParameters = new SSLParameters();
// Enable only the desired protocols
sslParameters.setProtocols(new String[]{"TLSv1.3"});
sslContext.setSSLParameters(sslParameters);
// Fixups for dalvik
if (!IS_RI) {
//<End of snippet n. 0>