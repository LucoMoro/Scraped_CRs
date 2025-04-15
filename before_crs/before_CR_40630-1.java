/*Add chain building to TrustedCertificateStore

Since TrustedCertificateStore has information needed, use it to build
certificate chains.

OpenSSL uses Authority Key Identifier in extensions to determine if the
certificate is the same as itself. There are problems with key rotation
when a different certificate serial signs a key with the same subject
identifier. It appears to be the same with the old code, but it may
generate an invalid chain.

(cherry-picked from 3fb088d79e446063ef743362a030e1cfb80b2178)

Change-Id:I8149bed1a0ae537f75da5dc3f3d7e3ccab353f91*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java
//Synthetic comment -- index be43ba7..4afbb04 100644

//Synthetic comment -- @@ -70,6 +70,34 @@
return aki;
}

@Override public byte[] getEncoded() {
if (encoding == null) {
encoding = ASN1.encode(this);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/SubjectKeyIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/SubjectKeyIdentifier.java
//Synthetic comment -- index 7415002..1db9598 100644

//Synthetic comment -- @@ -58,6 +58,13 @@
return res;
}

@Override public byte[] getEncoded() {
if (encoding == null) {
encoding = ASN1OctetString.getInstance().encode(keyIdentifier);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStore.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStore.java
//Synthetic comment -- index 54116a7..abb6e55 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package org.apache.harmony.xnet.provider.jsse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -23,19 +28,20 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreSpi;
import java.security.PublicKey;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import libcore.io.IoUtils;

/**
* A source for trusted root certificate authority (CA) certificates
//Synthetic comment -- @@ -366,6 +372,109 @@
return null;
}

// like java.security.cert.CertSelector but with X509Certificate and without cloning
private static interface CertSelector {
public boolean match(X509Certificate cert);








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStoreTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStoreTest.java
//Synthetic comment -- index 6d0f50c..52880df 100644

//Synthetic comment -- @@ -22,11 +22,13 @@
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
//Synthetic comment -- @@ -411,6 +413,15 @@
assertAliases(alias1, alias2);
assertEquals(getChain()[2], store.findIssuer(getChain()[1]));
assertEquals(getChain()[1], store.findIssuer(getChain()[0]));
resetStore();
}








