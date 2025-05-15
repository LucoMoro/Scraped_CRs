
//<Beginning of snippet n. 0>


* </ul>
*/
class JarVerifier {
    /**
     * List of accepted digest algorithms. This list is in order from most
     * preferred to least preferred.
     */
    private static final String[] DIGEST_ALGORITHMS = new String[] {
        "SHA-512",
        "SHA-384",
        "SHA-256",
        "SHA1",
    };

private final String jarName;

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

private boolean verify(Attributes attributes, String entry, byte[] data,
int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        for (int i = 0; i < DIGEST_ALGORITHMS.length; i++) {
            String algorithm = DIGEST_ALGORITHMS[i];
String hash = attributes.getValue(algorithm + entry);
if (hash == null) {
continue;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



private final String jarName5 = "hyts_signed_inc.jar";

    private final String jarName6 = "hyts_signed_sha256withrsa.jar";

    private final String jarName7 = "hyts_signed_sha256digest_sha256withrsa.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
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

//<End of snippet n. 1>








