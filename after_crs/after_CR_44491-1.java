/*Add support for ECDSA signatures on jar files

Change-Id:If928f2244b3a0809255d6619c25268beb84f76d3*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/jar/JarFile.java b/luni/src/main/java/java/util/jar/JarFile.java
//Synthetic comment -- index e129e82..0b270bc 100644

//Synthetic comment -- @@ -337,7 +337,8 @@
if (verifier != null
&& (endsWithIgnoreCase(entryName, ".SF")
|| endsWithIgnoreCase(entryName, ".DSA")
                                || endsWithIgnoreCase(entryName, ".RSA")
                                || endsWithIgnoreCase(entryName, ".EC"))) {
signed = true;
InputStream is = super.getInputStream(entry);
verifier.addMetaEntry(entryName, Streams.readFully(is));








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/jar/JarVerifier.java b/luni/src/main/java/java/util/jar/JarVerifier.java
//Synthetic comment -- index 3dfd272..dc37b72 100644

//Synthetic comment -- @@ -259,7 +259,7 @@
Iterator<String> it = metaEntries.keySet().iterator();
while (it.hasNext()) {
String key = it.next();
            if (key.endsWith(".DSA") || key.endsWith(".RSA") || key.endsWith(".EC")) {
verifyCertificate(key);
// Check for recursive class load
if (metaEntries == null) {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index 888fe2c..717b79e 100644

//Synthetic comment -- @@ -137,19 +137,23 @@
put("Alg.Alias.Signature.ECDSAwithSHA1", "ECDSA");
// iso(1) member-body(2) us(840) ansi-x962(10045) signatures(4) ecdsa-with-SHA1(1)
put("Alg.Alias.Signature.1.2.840.10045.4.1", "ECDSA");
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10045.2.1", "SHA256withECDSA");

// iso(1) member-body(2) us(840) ansi-x962(10045) signatures(4) ecdsa-with-SHA2(3)
put("Signature.SHA256withECDSA", OpenSSLSignature.SHA256ECDSA.class.getName());
// ecdsa-with-SHA256(2)
put("Alg.Alias.Signature.1.2.840.10045.4.3.2", "SHA256withECDSA");
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.10045.2.1", "SHA256withECDSA");

put("Signature.SHA384withECDSA", OpenSSLSignature.SHA384ECDSA.class.getName());
// ecdsa-with-SHA384(3)
put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.2with1.2.840.10045.2.1", "SHA384withECDSA");

put("Signature.SHA512withECDSA", OpenSSLSignature.SHA512ECDSA.class.getName());
// ecdsa-with-SHA512(4)
put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.3with1.2.840.10045.2.1", "SHA512withECDSA");

/* === SecureRandom === */
/*








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java b/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java
//Synthetic comment -- index 7e4e2bd..64a179b 100644

//Synthetic comment -- @@ -73,6 +73,8 @@

private final String jarName7 = "hyts_signed_sha256digest_sha256withrsa.jar";

    private final String jarName8 = "hyts_signed_sha512digest_sha512withecdsa.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
//Synthetic comment -- @@ -524,7 +526,7 @@
// This test doesn't pass on RI. If entry size is set up incorrectly,
// SecurityException is thrown. But SecurityException is thrown on RI only
// if jar file is signed incorrectly.
    public void test_getInputStreamLjava_util_jar_JarEntry_subtest0() throws Exception {
File signedFile = null;
try {
Support_Resources.copyFile(resources, null, jarName4);
//Synthetic comment -- @@ -588,38 +590,40 @@
}

// SHA1 digest, SHA256withRSA signed JAR
        checkSignedJar(jarName6);

// SHA-256 digest, SHA256withRSA signed JAR
        checkSignedJar(jarName7);

        // SHA-512 digest, SHA512withECDSA signed JAR
        checkSignedJar(jarName8);
    }

    private void checkSignedJar(String jarName) throws Exception {
        Support_Resources.copyFile(resources, null, jarName);

        File file = new File(resources, jarName);

        JarFile jarFile = new JarFile(file, true);

        boolean foundCerts = false;

        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            InputStream is = jarFile.getInputStream(entry);
            is.skip(100000);
            is.close();
            Certificate[] certs = entry.getCertificates();
            if (certs != null && certs.length > 0) {
                foundCerts = true;
                break;
            }
}

        assertTrue(
                "No certificates found during signed jar test for jar \""
                        + jarName + "\"", foundCerts);
}

/*







