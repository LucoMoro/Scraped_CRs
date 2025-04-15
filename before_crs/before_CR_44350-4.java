/*Add stronger digest support to JarVerifier

Adds support for SHA-256, SHA-384, and SHA-512.

Bug:http://code.google.com/p/android/issues/detail?id=38321Change-Id:I9bf3f9cb2fa53b9f980e6c1cffcba81aa289a762*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/jar/JarVerifier.java b/luni/src/main/java/java/util/jar/JarVerifier.java
//Synthetic comment -- index ec0e088..3dfd272 100644

//Synthetic comment -- @@ -51,6 +51,16 @@
* </ul>
*/
class JarVerifier {

private final String jarName;

//Synthetic comment -- @@ -190,22 +200,17 @@
}
Certificate[] certificatesArray = certs.toArray(new Certificate[certs.size()]);

        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA SHA1";
        }
        StringTokenizer tokens = new StringTokenizer(algorithms);
        while (tokens.hasMoreTokens()) {
            String algorithm = tokens.nextToken();
            String hash = attributes.getValue(algorithm + "-Digest");
if (hash == null) {
continue;
}
byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);

try {
                return new VerifierEntry(name, MessageDigest
                        .getInstance(algorithm), hashBytes, certificatesArray);
} catch (NoSuchAlgorithmException e) {
// ignored
}
//Synthetic comment -- @@ -378,13 +383,8 @@

private boolean verify(Attributes attributes, String entry, byte[] data,
int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA SHA1";
        }
        StringTokenizer tokens = new StringTokenizer(algorithms);
        while (tokens.hasMoreTokens()) {
            String algorithm = tokens.nextToken();
String hash = attributes.getValue(algorithm + entry);
if (hash == null) {
continue;








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java b/luni/src/test/java/org/apache/harmony/archive/tests/java/util/jar/JarFileTest.java
//Synthetic comment -- index 226ea66..7e4e2bd 100644

//Synthetic comment -- @@ -69,6 +69,10 @@

private final String jarName5 = "hyts_signed_inc.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
//Synthetic comment -- @@ -582,6 +586,40 @@
} catch (Exception e) {
fail("Exception during test 5: " + e);
}
}

/*








//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index 0ad0262..d5ceedf 100644

//Synthetic comment -- @@ -190,6 +190,7 @@
provide("Mac", "HmacSHA256");
provide("Mac", "HmacSHA384");
provide("Mac", "HmacSHA512");
provide("MessageDigest", "MD2");
provide("MessageDigest", "MD5");
provide("MessageDigest", "SHA-256");







