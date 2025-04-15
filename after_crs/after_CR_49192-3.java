/*Track Bouncycastle fixes for subjectAltNames

Change-Id:Ib67395e8ec461feb8f4b1826d18e98d318a157d2*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 440140e..2ac05e9 100644

//Synthetic comment -- @@ -792,12 +792,6 @@
X509Certificate c = getCertificate(f, CERT_RSA);
Collection<List<?>> col = c.getSubjectAlternativeNames();

checkAlternativeNames(col);
}

//Synthetic comment -- @@ -867,12 +861,6 @@
X509Certificate c = getCertificate(f, CERT_IPV6);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -900,12 +888,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_OTHER);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -923,12 +905,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_EMAIL);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -945,12 +921,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_DNS);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -967,12 +937,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_DIRNAME);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -989,12 +953,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_URI);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1011,12 +969,6 @@
X509Certificate c = getCertificate(f, CERT_ALT_RID);
Collection<List<?>> col = c.getSubjectAlternativeNames();

assertNotNull(f.getProvider().getName(), col);

assertEquals(1, col.size());
//Synthetic comment -- @@ -1033,12 +985,6 @@
X509Certificate c = getCertificate(f, CERT_RSA);
Collection<List<?>> col = c.getSubjectAlternativeNames();

checkAlternativeNames(col);
}








