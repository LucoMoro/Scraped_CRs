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

    /**
     * The key identifier for the authority.
     *
     * @return key identifier or {@code null}
     */
    public byte[] getKeyIdentifier() {
        return keyIdentifier;
    }

    /**
     * The GeneralNames for this authority key identifier.
     *
     * @return names for the authority certificate issuer or {@code null}
     */
    public GeneralNames getAuthorityCertIssuer() {
        return authorityCertIssuer;
    }

    /**
     * The serial number of the certificate identified by this authority key
     * identifier.
     *
     * @return authority's certificate serial number or {@code null}
     */
    public BigInteger getAuthorityCertSerialNumber() {
        return authorityCertSerialNumber;
    }

@Override public byte[] getEncoded() {
if (encoding == null) {
encoding = ASN1.encode(this);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/SubjectKeyIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/SubjectKeyIdentifier.java
//Synthetic comment -- index 7415002..1db9598 100644

//Synthetic comment -- @@ -58,6 +58,13 @@
return res;
}

    /**
     * The key identifier for this subject.
     */
    public byte[] getKeyIdentifier() {
        return keyIdentifier;
    }

@Override public byte[] getEncoded() {
if (encoding == null) {
encoding = ASN1OctetString.getInstance().encode(keyIdentifier);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStore.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustedCertificateStore.java
//Synthetic comment -- index 54116a7..abb6e55 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package org.apache.harmony.xnet.provider.jsse;

import org.apache.harmony.security.x501.Name;
import org.apache.harmony.security.x509.AuthorityKeyIdentifier;
import org.apache.harmony.security.x509.GeneralName;
import org.apache.harmony.security.x509.GeneralNames;
import org.apache.harmony.security.x509.SubjectKeyIdentifier;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -23,19 +28,20 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import libcore.io.IoUtils;
import libcore.util.Objects;

/**
* A source for trusted root certificate authority (CA) certificates
//Synthetic comment -- @@ -366,6 +372,109 @@
return null;
}

    private static AuthorityKeyIdentifier getAuthorityKeyIdentifier(X509Certificate cert) {
        final byte[] akidBytes = cert.getExtensionValue("2.5.29.35");
        if (akidBytes == null) {
            return null;
        }

        try {
            return AuthorityKeyIdentifier.decode(akidBytes);
        } catch (IOException e) {
            return null;
        }
    }

    private static SubjectKeyIdentifier getSubjectKeyIdentifier(X509Certificate cert) {
        final byte[] skidBytes = cert.getExtensionValue("2.5.29.14");
        if (skidBytes == null) {
            return null;
        }

        try {
            return SubjectKeyIdentifier.decode(skidBytes);
        } catch (IOException e) {
            return null;
        }
    }

    private static boolean isSelfSignedCertificate(X509Certificate cert) {
        if (!Objects.equal(cert.getSubjectX500Principal(), cert.getIssuerX500Principal())) {
            return false;
        }

        final AuthorityKeyIdentifier akid = getAuthorityKeyIdentifier(cert);
        if (akid != null) {
            final byte[] akidKeyId = akid.getKeyIdentifier();
            if (akidKeyId != null) {
                final SubjectKeyIdentifier skid = getSubjectKeyIdentifier(cert);
                if (!Arrays.equals(akidKeyId, skid.getKeyIdentifier())) {
                    return false;
                }
            }

            final BigInteger akidSerial = akid.getAuthorityCertSerialNumber();
            if (akidSerial != null && !akidSerial.equals(cert.getSerialNumber())) {
                return false;
            }

            final GeneralNames possibleIssuerNames = akid.getAuthorityCertIssuer();
            if (possibleIssuerNames != null) {
                GeneralName issuerName = null;

                /* Get the first Directory Name (DN) to match how OpenSSL works. */
                for (GeneralName possibleName : possibleIssuerNames.getNames()) {
                    if (possibleName.getTag() == GeneralName.DIR_NAME) {
                        issuerName = possibleName;
                        break;
                    }
                }

                if (issuerName != null) {
                    final String issuerCanonical = ((Name) issuerName.getName())
                            .getName(X500Principal.CANONICAL);

                    try {
                        final String subjectCanonical = new Name(cert.getSubjectX500Principal()
                                .getEncoded()).getName(X500Principal.CANONICAL);
                        if (!issuerCanonical.equals(subjectCanonical)) {
                            return false;
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        return true;
    }

    /**
     * Attempt to build a certificate chain from the supplied {@code leaf}
     * argument through the chain of issuers as high up as known. If the chain
     * can't be completed, the most complete chain available will be returned.
     * This means that a list with only the {@code leaf} certificate is returned
     * if no issuer certificates could be found.
     */
    public List<X509Certificate> getCertificateChain(X509Certificate leaf) {
        final List<X509Certificate> chain = new ArrayList<X509Certificate>();
        chain.add(leaf);

        for (int i = 0; true; i++) {
            X509Certificate cert = chain.get(i);
            if (isSelfSignedCertificate(cert)) {
                break;
            }
            X509Certificate issuer = findIssuer(cert);
            if (issuer == null) {
                break;
            }
            chain.add(issuer);
        }

        return chain;
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
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
//Synthetic comment -- @@ -411,6 +413,15 @@
assertAliases(alias1, alias2);
assertEquals(getChain()[2], store.findIssuer(getChain()[1]));
assertEquals(getChain()[1], store.findIssuer(getChain()[0]));

        X509Certificate[] expected = getChain();
        List<X509Certificate> actualList = store.getCertificateChain(expected[0]);

        assertEquals("Generated CA list should be same length", expected.length, actualList.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Chain value should be the same for position " + i, expected[i],
                    actualList.get(i));
        }
resetStore();
}








