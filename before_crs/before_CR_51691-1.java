/*KeyChain: return null instead of throw

The API documentation says it will return null if the key isn't found.
We get null back from the keystore daemon when it can't retrieve the
data, so just return null back to the API caller.

Change-Id:I42248bd50cbc5f76864bd762aae3faab1c50529d*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyChain.java b/keystore/java/android/security/KeyChain.java
//Synthetic comment -- index 31c38d5..d7119fff 100644

//Synthetic comment -- @@ -336,7 +336,12 @@
KeyChainConnection keyChainConnection = bind(context);
try {
IKeyChainService keyChainService = keyChainConnection.getService();
            byte[] certificateBytes = keyChainService.getCertificate(alias);
TrustedCertificateStore store = new TrustedCertificateStore();
List<X509Certificate> chain = store
.getCertificateChain(toCertificate(certificateBytes));







