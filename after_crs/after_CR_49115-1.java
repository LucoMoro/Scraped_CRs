/*Check the EE's eKU extension field, if present.

BUG=https://code.google.com/p/chromium/issues/detail?id=167607andhttps://b.corp.google.com/issue?id=7920492Change-Id:Ib917c3a4a8ea6a12f685c44056aa44aa414d45e6*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 1682df7..f90840d 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
//Synthetic comment -- @@ -179,12 +180,12 @@

@Override public void checkClientTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null, true);
}

@Override public void checkServerTrusted(X509Certificate[] chain, String authType)
throws CertificateException {
        checkTrusted(chain, authType, null, false);
}

/**
//Synthetic comment -- @@ -194,7 +195,7 @@
*/
public List<X509Certificate> checkServerTrusted(X509Certificate[] chain, String authType,
String host) throws CertificateException {
        return checkTrusted(chain, authType, host, false);
}

public void handleTrustStorageUpdate() {
//Synthetic comment -- @@ -205,7 +206,8 @@
}
}

    private List<X509Certificate> checkTrusted(X509Certificate[] chain, String authType,
                                               String host, boolean clientAuth)
throws CertificateException {
if (chain == null || chain.length == 0 || authType == null || authType.length() == 0) {
throw new IllegalArgumentException("null or zero-length parameter");
//Synthetic comment -- @@ -251,6 +253,45 @@
}
}

        // If an EKU extension is present in the end-entity certificate, it
        // MUST contain either the anyEKU or {client,server}Auth or
        // netscapeSGC or Microsoft SGC EKUs.
        try {
            List<String> ekuOids = wholeChain.get(0).getExtendedKeyUsage();
            if (ekuOids != null) {
                boolean goodEkuFound = false;
                String peerTypeOid = clientAuth ?
                    // TLS client authentication:
                    "1.3.6.1.5.5.7.3.2" :
                    // TLS server authentication:
                    "1.3.6.1.5.5.7.3.1"

                for (String ekuOid : ekuOids) {
                    if (
                        ekuOid.equals(peerTypeOid) ||
                        // anyExtendedKeyUsage:
                        ekuOid.equals("2.5.29.37.0") ||
                        // Server-Gated Cryptography (necessary to support a few legacy issuers):
                        //    Netscape:
                        ekuOid.equals("2.16.840.1.113730.4.1") ||
                        //    Microsoft:
                        ekuOid.equals("1.3.6.1.4.1.311.10.3.3")
                       ) {
                        goodEkuFound = true;
                        break;
                    }
                }

                if (!goodEkuFound) {
                    throw new CertificateException(
                        "End-entity certificate does not have a valid " +
                        "extendedKeyUsage.");
                }
            }
        } catch (CertificateParsingException e) {
            throw new CertificateException(e);
        }

// build the cert path from the array of certs sans trust anchors
CertPath certPath = factory.generateCertPath(Arrays.asList(newChain));








