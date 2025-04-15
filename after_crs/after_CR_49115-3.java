/*Check the EE's eKU extension field, if present.

BUG=https://code.google.com/p/chromium/issues/detail?id=167607andhttps://b.corp.google.com/issue?id=7920492Change-Id:Ib917c3a4a8ea6a12f685c44056aa44aa414d45e6*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 1682df7..b6e593e 100644

//Synthetic comment -- @@ -179,12 +179,12 @@

@Override public void checkClientTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null, true);
}

@Override public void checkServerTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null, false);
}

/**
//Synthetic comment -- @@ -194,7 +194,7 @@
*/
public List<X509Certificate> checkServerTrusted(X509Certificate[] chain, String authType,
String host) throws CertificateException {
        return checkTrusted(chain, authType, host, false);
}

public void handleTrustStorageUpdate() {
//Synthetic comment -- @@ -205,7 +205,8 @@
}
}

    private List<X509Certificate> checkTrusted(X509Certificate[] chain, String authType,
                                               String host, boolean clientAuth)
throws CertificateException {
if (chain == null || chain.length == 0 || authType == null || authType.length() == 0) {
throw new IllegalArgumentException("null or zero-length parameter");
//Synthetic comment -- @@ -251,6 +252,9 @@
}
}

        // verify extendedKeyUsage if present, throws CertificateException on error
        checkExtendedKeyUsage(wholeChain.get(0), clientAuth);

// build the cert path from the array of certs sans trust anchors
CertPath certPath = factory.generateCertPath(Arrays.asList(newChain));

//Synthetic comment -- @@ -382,6 +386,71 @@
}

/**
     * If an EKU extension is present in the end-entity certificate,
     * it MUST contain an appropriate key usage. For servers, this
     * includes anyExtendedKeyUsage, serverAuth, or the historical
     * Server Gated Cryptography options of nsSGC or msSGC.  For
     * clients, this includes anyExtendedKeyUsage and clientAuth.
     */
    private static void checkExtendedKeyUsage(X509Certificate cert, boolean clientAuth)
            throws CertificateException {
        List<String> ekuOids = cert.getExtendedKeyUsage();
        if (ekuOids == null) {
            return;
        }
        for (String ekuOid : ekuOids) {
            // anyExtendedKeyUsage for clients and servers
            if (ekuOid.equals("2.5.29.37.0")) {
                return;
            }

            // clients
            if (clientAuth) {
                // clientAuth
                if (ekuOid.equals("1.3.6.1.5.5.7.3.2")) {
                    return;
                }
                continue;
            }

            // servers

            // serverAuth
            if (ekuOid.equals("1.3.6.1.5.5.7.3.1")) {
                return;
            }
            // nsSGC
            if (ekuOid.equals("2.16.840.1.113730.4.1")) {
                return;
            }
            // msSGC
            if (ekuOid.equals("1.3.6.1.4.1.311.10.3.3")) {
                return;
            }
        }
        throw new CertificateException("End-entity certificate does not have a valid "
                                       + "extendedKeyUsage.");
    }

    private TrustAnchor findTrustAnchorByIssuerAndSignature(X509Certificate lastCert) {
        TrustAnchor trustAnchor = trustedCertificateIndex.findByIssuerAndSignature(lastCert);
        if (trustAnchor != null) {
            return trustAnchor;
        }
        if (trustedCertificateStore == null) {
            return null;
        }
        // we have a KeyStore and the issuer of the last cert in
        // the chain seems to be missing from the
        // TrustedCertificateIndex, check the KeyStore for a hit
        X509Certificate issuer = trustedCertificateStore.findIssuer(lastCert);
        if (issuer != null) {
            return trustedCertificateIndex.index(issuer);
        }
        return null;
    }

    /**
* Check the trustedCertificateIndex for the cert to see if it is
* already trusted and failing that check the KeyStore if it is
* available.
//Synthetic comment -- @@ -406,24 +475,6 @@
return null;
}

@Override public X509Certificate[] getAcceptedIssuers() {
return (acceptedIssuers != null) ? acceptedIssuers.clone() : acceptedIssuers(rootKeyStore);
}







