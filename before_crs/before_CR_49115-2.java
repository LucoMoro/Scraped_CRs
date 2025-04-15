/*Check the EE's eKU extension field, if present.

BUG=https://code.google.com/p/chromium/issues/detail?id=167607andhttps://b.corp.google.com/issue?id=7920492Change-Id:Ib917c3a4a8ea6a12f685c44056aa44aa414d45e6*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 1682df7..b6e593e 100644

//Synthetic comment -- @@ -179,12 +179,12 @@

@Override public void checkClientTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null);
}

@Override public void checkServerTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null);
}

/**
//Synthetic comment -- @@ -194,7 +194,7 @@
*/
public List<X509Certificate> checkServerTrusted(X509Certificate[] chain, String authType,
String host) throws CertificateException {
        return checkTrusted(chain, authType, host);
}

public void handleTrustStorageUpdate() {
//Synthetic comment -- @@ -205,7 +205,8 @@
}
}

    private List<X509Certificate> checkTrusted(X509Certificate[] chain, String authType, String host)
throws CertificateException {
if (chain == null || chain.length == 0 || authType == null || authType.length() == 0) {
throw new IllegalArgumentException("null or zero-length parameter");
//Synthetic comment -- @@ -251,6 +252,9 @@
}
}

// build the cert path from the array of certs sans trust anchors
CertPath certPath = factory.generateCertPath(Arrays.asList(newChain));

//Synthetic comment -- @@ -382,6 +386,71 @@
}

/**
* Check the trustedCertificateIndex for the cert to see if it is
* already trusted and failing that check the KeyStore if it is
* available.
//Synthetic comment -- @@ -406,24 +475,6 @@
return null;
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

@Override public X509Certificate[] getAcceptedIssuers() {
return (acceptedIssuers != null) ? acceptedIssuers.clone() : acceptedIssuers(rootKeyStore);
}







