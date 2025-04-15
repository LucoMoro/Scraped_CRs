/*Add SHA-256 digest support to JarVerifier

Bug:http://code.google.com/p/android/issues/detail?id=38321Change-Id:I9bf3f9cb2fa53b9f980e6c1cffcba81aa289a762*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/jar/JarVerifier.java b/luni/src/main/java/java/util/jar/JarVerifier.java
//Synthetic comment -- index ec0e088..59fa92b 100644

//Synthetic comment -- @@ -51,6 +51,12 @@
* </ul>
*/
class JarVerifier {
    private static final String[] DIGEST_ALGORITHMS = new String[] {
        "SHA-256",
        "SHA1",
        "SHA",
        "MD5",
    };

private final String jarName;

//Synthetic comment -- @@ -190,22 +196,17 @@
}
Certificate[] certificatesArray = certs.toArray(new Certificate[certs.size()]);

        for (int i = 0; i < DIGEST_ALGORITHMS.length; i++) {
            final String algorithm = DIGEST_ALGORITHMS[i];
            final String hash = attributes.getValue(algorithm + "-Digest");
if (hash == null) {
continue;
}
byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);

try {
                return new VerifierEntry(name, MessageDigest.getInstance(algorithm), hashBytes,
                        certificatesArray);
} catch (NoSuchAlgorithmException e) {
// ignored
}
//Synthetic comment -- @@ -378,13 +379,8 @@

private boolean verify(Attributes attributes, String entry, byte[] data,
int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        for (int i = 0; i < DIGEST_ALGORITHMS.length; i++) {
            String algorithm = DIGEST_ALGORITHMS[i];
String hash = attributes.getValue(algorithm + entry);
if (hash == null) {
continue;








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java b/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java
//Synthetic comment -- index 226ea66..7e4e2bd 100644

//Synthetic comment -- @@ -69,6 +69,10 @@

private final String jarName5 = "hyts_signed_inc.jar";

    private final String jarName6 = "hyts_signed_sha256withrsa.jar";

    private final String jarName7 = "hyts_signed_sha256digest_sha256withrsa.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
//Synthetic comment -- @@ -582,6 +586,40 @@
} catch (Exception e) {
fail("Exception during test 5: " + e);
}

        // SHA1 digest, SHA256withRSA signed JAR
        try {
            Support_Resources.copyFile(resources, null, jarName6);
            signedFile = new File(resources, jarName6);
        } catch (Exception e) {
            fail("Failed to create local file 6: " + e);
        }

        try {
            JarFile jar = new JarFile(signedFile);
            JarEntry entry = new JarEntry(entryName3);
            InputStream in = jar.getInputStream(entry);
            in.read();
        } catch (Exception e) {
            fail("Exception during test 6: " + e);
        }

        // SHA-256 digest, SHA256withRSA signed JAR
        try {
            Support_Resources.copyFile(resources, null, jarName7);
            signedFile = new File(resources, jarName7);
        } catch (Exception e) {
            fail("Failed to create local file 7: " + e);
        }

        try {
            JarFile jar = new JarFile(signedFile);
            JarEntry entry = new JarEntry(entryName3);
            InputStream in = jar.getInputStream(entry);
            in.read();
        } catch (Exception e) {
            fail("Exception during test 7: " + e);
        }
}

/*







