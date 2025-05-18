//<Beginning of snippet n. 0>
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

class JarVerifier {

    private static final String[] SUPPORTED_ALGORITHMS = {"SHA-256", "SHA-384", "SHA-512"};
    private final String jarName;

    public JarVerifier(String jarName) {
        this.jarName = jarName;
    }

    public VerifierEntry verifyJar(Attributes attributes, String name, List<Certificate> certs) throws InvalidAlgorithmException {
        Certificate[] certificatesArray = certs.toArray(new Certificate[0]);

        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA-256 SHA-384 SHA-512";
        }

        StringTokenizer tokens = new StringTokenizer(algorithms);
        while (tokens.hasMoreTokens()) {
            String algorithm = tokens.nextToken();
            if (!isSupportedAlgorithm(algorithm)) {
                Logger.getLogger(JarVerifier.class.getName()).log(Level.WARNING, "Unsupported algorithm: " + algorithm);
                continue;
            }
            String hash = attributes.getValue(algorithm + "-Digest");
            if (hash == null) {
                Logger.getLogger(JarVerifier.class.getName()).log(Level.WARNING, "Hash not found for algorithm: " + algorithm);
                continue;
            }
            byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);

            try {
                return new VerifierEntry(name, MessageDigest.getInstance(algorithm), hashBytes, certificatesArray);
            } catch (NoSuchAlgorithmException e) {
                throw new InvalidAlgorithmException("Unsupported algorithm: " + algorithm, e);
            }
        }
        throw new InvalidAlgorithmException("No valid algorithms found.");
    }

    private boolean verify(Attributes attributes, String entry, byte[] data, int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA-256 SHA-384 SHA-512";
        }
        StringTokenizer tokens = new StringTokenizer(algorithms);
        while (tokens.hasMoreTokens()) {
            String algorithm = tokens.nextToken();
            if (!isSupportedAlgorithm(algorithm)) {
                Logger.getLogger(JarVerifier.class.getName()).log(Level.WARNING, "Unsupported algorithm: " + algorithm);
                continue;
            }
            String hash = attributes.getValue(algorithm + entry);
            if (hash == null) {
                Logger.getLogger(JarVerifier.class.getName()).log(Level.WARNING, "Hash not found for entry: " + entry + " and algorithm: " + algorithm);
                continue;
            }

            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] computedHash = md.digest(data);
                byte[] expectedHash = hash.getBytes(Charsets.ISO_8859_1);
                if (Arrays.equals(computedHash, expectedHash)) {
                    return true; // Successfully verified
                }
            } catch (NoSuchAlgorithmException e) {
                Logger.getLogger(JarVerifier.class.getName()).log(Level.SEVERE, "Algorithm error during verification: " + algorithm, e);
            }
        }
        return false; // Verification failed
    }

    private boolean isSupportedAlgorithm(String algorithm) {
        return Arrays.asList(SUPPORTED_ALGORITHMS).contains(algorithm);
    }
    
    // Custom exception for invalid algorithms
    public static class InvalidAlgorithmException extends Exception {
        public InvalidAlgorithmException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidAlgorithmException(String message) {
            super(message);
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private final String jarName5 = "hyts_signed_inc.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
} catch (Exception e) {
fail("Exception during test 5: " + e);
}
//<End of snippet n. 1>