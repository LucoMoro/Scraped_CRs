/*Track Bouncycastle fixes for subjectAltNames

Change-Id:Ib67395e8ec461feb8f4b1826d18e98d318a157d2*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..0474f0e 100644

//Synthetic comment -- @@ -795,12 +795,6 @@
return;
}

checkAlternativeNames(col);
}

//Synthetic comment -- @@ -870,12 +864,6 @@
X509Certificate c = getCertificate(f, CERT_IPV6);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -905,6 +893,11 @@
if (!"DRLCertFactory".equals(f.getProvider().getName())) {
fail("Non-Harmony shouldn't throw");
}
        } catch (CertificateParsingException e) {
            if (!"BC".equals(f.getProvider().getName())) {
                fail("Non-BC shouldn't throw");
            }
            return;
}
assertNull(col);
}
//Synthetic comment -- @@ -913,12 +906,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_OTHER);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -936,12 +923,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_EMAIL);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -958,12 +939,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_DNS);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -992,12 +967,6 @@
return;
}

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1014,12 +983,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_URI);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1036,12 +999,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_RID);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1069,12 +1026,6 @@
return;
}

checkAlternativeNames(col);
}








