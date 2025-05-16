//<Beginning of snippet n. 0>
class JarVerifier {

private final String jarName;

JarVerifier(String jarName) {
    this.jarName = jarName;
}

private Certificate[] certificatesArray = certs.toArray(new Certificate[certs.size()]);
private static final Set<String> SUPPORTED_ALGORITHMS = Set.of("SHA", "SHA1", "SHA-256", "SHA-384", "SHA-512");

private VerifierEntry createVerifierEntry(String name, Attributes attributes) {
    String algorithms = attributes.getValue("Digest-Algorithms");
    if (algorithms == null) {
        algorithms = "SHA SHA1";
    }
    StringTokenizer tokens = new StringTokenizer(algorithms);
    while (tokens.hasMoreTokens()) {
        String algorithm = tokens.nextToken();
        if (!SUPPORTED_ALGORITHMS.contains(algorithm)) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        String hash = attributes.getValue(algorithm + "-Digest");
        if (hash == null) {
            continue;
        }
        byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);
        try {
            return new VerifierEntry(name, MessageDigest.getInstance(algorithm), hashBytes, certificatesArray);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error creating MessageDigest for algorithm: " + algorithm, e);
        }
    }
    return null; // or handle accordingly
}

private boolean verify(Attributes attributes, String entry, byte[] data,
                       int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
    String algorithms = attributes.getValue("Digest-Algorithms");
    if (algorithms == null) {
        algorithms = "SHA SHA1";
    }
    StringTokenizer tokens = new StringTokenizer(algorithms);
    while (tokens.hasMoreTokens()) {
        String algorithm = tokens.nextToken();
        if (!SUPPORTED_ALGORITHMS.contains(algorithm)) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        String hash = attributes.getValue(algorithm + entry);
        if (hash == null) {
            continue;
        }
        // Add validation to check if hash length matches expected lengths
    }
    return false; // or handle accordingly
}

//<End of snippet n. 0>