/*Adding minimum cryptographic strength check for cert chains.

Change-Id:Id8a3fc28a07c086182183090cd79372ac81582e6*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainAnalyzer.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainAnalyzer.java
new file mode 100644
//Synthetic comment -- index 0000000..f4ea92b

//Synthetic comment -- @@ -0,0 +1,65 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.harmony.xnet.provider.jsse;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

public final class ChainAnalyzer {

    private static final int MIN_MODULUS = 1024;
    private static final String[] OID_BLACKLIST = {"1.2.840.113549.1.1.4"}; // MD5withRSA


    /**
     * Checks the chain to make sure it doesn't contain weak certs.
     *
     * @param chain a list of X509Certificates not including the root certs
     */
    public void check(X509Certificate[] chain) throws CertificateException {
        for (X509Certificate cert : chain) {
            checkCert(cert);
        }
    }

    private void checkCert(X509Certificate cert) throws CertificateException {
        checkModulusLength(cert);
        checkNotMD5(cert);
    }

    private void checkModulusLength(X509Certificate cert) throws CertificateException {
        Object pubkey = cert.getPublicKey();
        if (pubkey instanceof RSAPublicKey) {
            int modulusLength = ((RSAPublicKey) pubkey).getModulus().bitLength();
            if(!(modulusLength >= MIN_MODULUS)) {
                throw new CertificateException("Modulus is < 1024 bits");
            }
        }
    }

    private void checkNotMD5(X509Certificate cert) throws CertificateException {
        String oid = cert.getSigAlgOID();
        for (String blacklisted : OID_BLACKLIST) {
            if (oid.equals(blacklisted)) {
                throw new CertificateException("Signature uses an insecure hash function");
            }
        }
    }
}









//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 3f362c5..0eb31e3 100644

//Synthetic comment -- @@ -52,6 +52,11 @@
private final KeyStore rootKeyStore;

/**
     * Checks the chain to make sure its sufficiently strong
     */
    private static final ChainAnalyzer chainAnalyzer = new ChainAnalyzer();

    /**
* The backing store for the AndroidCAStore if non-null. This will
* be null when the rootKeyStore is null, implying we are not
* using the AndroidCAStore.
//Synthetic comment -- @@ -193,6 +198,10 @@
"Trust anchor for certification path not found.", null, certPath, -1));
}

        // there's no point in checking trust anchors here, and it will throw off the MD5 check,
        // so we just hand it the chain sans anchors
        chainAnalyzer.check(newChain);

try {
PKIXParameters params = new PKIXParameters(trustAnchors);
params.setRevocationEnabled(false);







