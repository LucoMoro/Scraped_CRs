//<Beginning of snippet n. 0>
class JarVerifier {

private final String jarName;

public JarVerifier(String jarName) {
    this.jarName = jarName;
}

public VerifierEntry createVerifierEntry(Attributes attributes, String name, Set<Certificate> certs) {
    Certificate[] certificatesArray = certs.toArray(new Certificate[certs.size()]);

    String algorithms = attributes.getValue("Digest-Algorithms");
    if (algorithms == null) {
        algorithms = "SHA SHA1 SHA-256 SHA-384 SHA-512";
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
            return new VerifierEntry(name, MessageDigest.getInstance(algorithm), hashBytes, certificatesArray);
        } catch (NoSuchAlgorithmException e) {
            // log unsupported algorithm
            System.err.println("Unsupported algorithm: " + algorithm);
        }
    }
    return null; // return null if no valid VerifierEntry can be created
}

private boolean verify(Attributes attributes, String entry, byte[] data,
                       int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
    String algorithms = attributes.getValue("Digest-Algorithms");
    if (algorithms == null) {
        algorithms = "SHA SHA1 SHA-256 SHA-384 SHA-512";
    }
    StringTokenizer tokens = new StringTokenizer(algorithms);
    while (tokens.hasMoreTokens()) {
        String algorithm = tokens.nextToken();
        String hash = attributes.getValue(algorithm + "-Digest");
        if (hash == null) {
            continue;
        }
        // Implement verification logic based on algorithm
        // ...
    }
    return false; // return false if no valid hash is found
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private final String jarName5 = "hyts_signed_inc.jar";
private final String entryName = "foo/bar/A.class";
private final String entryName3 = "coucou/FileAccess.class";
//<End of snippet n. 1>