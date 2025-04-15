/*Track libcore changes for OpenSSLKey

Change-Id:I39f60c34daa9ccc633efb02988ea238a84e6bbf1*/




//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 4dbca23..c3a1a17 100644

//Synthetic comment -- @@ -56,8 +56,8 @@
import java.util.Set;

import org.apache.harmony.security.provider.cert.X509CertImpl;
import org.apache.harmony.xnet.provider.jsse.OpenSSLKey;
import org.apache.harmony.xnet.provider.jsse.OpenSSLKeyHolder;

class BrowserFrame extends Handler {

//Synthetic comment -- @@ -1129,13 +1129,10 @@
if (table.IsAllowed(hostAndPort)) {
// previously allowed
PrivateKey pkey = table.PrivateKey(hostAndPort);
            if (pkey instanceof OpenSSLKeyHolder) {
                OpenSSLKey sslKey = ((OpenSSLKeyHolder) pkey).getOpenSSLKey();
nativeSslClientCert(handle,
                                    sslKey.getPkeyContext(),
table.CertificateChain(hostAndPort));
} else {
nativeSslClientCert(handle,








//Synthetic comment -- diff --git a/core/java/android/webkit/ClientCertRequestHandler.java b/core/java/android/webkit/ClientCertRequestHandler.java
//Synthetic comment -- index 6570a9b8..dac1510 100644

//Synthetic comment -- @@ -21,8 +21,8 @@
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.apache.harmony.xnet.provider.jsse.NativeCrypto;
import org.apache.harmony.xnet.provider.jsse.OpenSSLKey;
import org.apache.harmony.xnet.provider.jsse.OpenSSLKeyHolder;

/**
* ClientCertRequestHandler: class responsible for handling client
//Synthetic comment -- @@ -56,14 +56,11 @@
byte[][] chainBytes = NativeCrypto.encodeCertificates(chain);
mTable.Allow(mHostAndPort, privateKey, chainBytes);

            if (privateKey instanceof OpenSSLKeyHolder) {
                OpenSSLKey pkey = ((OpenSSLKeyHolder) privateKey).getOpenSSLKey();
                setSslClientCertFromCtx(pkey.getPkeyContext(), chainBytes);
} else {
                setSslClientCertFromPKCS8(privateKey.getEncoded(), chainBytes);
}
} catch (CertificateEncodingException e) {
post(new Runnable() {








//Synthetic comment -- diff --git a/keystore/java/android/security/AndroidKeyStore.java b/keystore/java/android/security/AndroidKeyStore.java
//Synthetic comment -- index aabfcae..b5ab1e3 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import org.apache.harmony.xnet.provider.jsse.OpenSSLDSAPrivateKey;
import org.apache.harmony.xnet.provider.jsse.OpenSSLEngine;
import org.apache.harmony.xnet.provider.jsse.OpenSSLKeyHolder;
import org.apache.harmony.xnet.provider.jsse.OpenSSLRSAPrivateKey;

import android.util.Log;
//Synthetic comment -- @@ -210,10 +211,8 @@
byte[] keyBytes = null;

final String pkeyAlias;
        if (key instanceof OpenSSLKeyHolder) {
            pkeyAlias = ((OpenSSLKeyHolder) key).getOpenSSLKey().getAlias();
} else {
pkeyAlias = null;
}







