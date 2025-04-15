/*Track Bouncycastle fixes for subjectAltNames

Change-Id:Ib67395e8ec461feb8f4b1826d18e98d318a157d2*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 151c64a..80f7242 100644

//Synthetic comment -- @@ -803,12 +803,6 @@
return;
}

checkAlternativeNames(col);
}

//Synthetic comment -- @@ -878,12 +872,6 @@
X509Certificate c = getCertificate(f, CERT_IPV6);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -911,12 +899,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_OTHER);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -934,12 +916,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_EMAIL);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -956,12 +932,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_DNS);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -990,12 +960,6 @@
return;
}

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1012,12 +976,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_URI);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1034,12 +992,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_RID);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1067,12 +1019,6 @@
return;
}

checkAlternativeNames(col);
}








