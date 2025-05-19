//<Beginning of snippet n. 0>
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.jar.Attributes;
import java.security.cert.Certificate;

class JarVerifier {

    private final String jarName;
    private static final Logger logger = Logger.getLogger(JarVerifier.class.getName());
    private static final List<String> SUPPORTED_ALGORITHMS = Arrays.asList("SHA-256", "SHA-384", "SHA-512");

    public JarVerifier(String jarName) {
        this.jarName = jarName;
    }

    private VerifierEntry verifyEntry(String name, Attributes attributes, List<Certificate> certs) {
        Certificate[] certificatesArray = certs.toArray(new Certificate[0]);

        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA-256 SHA-384 SHA-512";
        }
        String[] tokens = algorithms.split(" ");
        for (String algorithm : tokens) {
            validateAlgorithm(algorithm);
            String hash = attributes.getValue(algorithm + "-Digest");
            if (hash == null) {
                continue;
            }
            byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);
            validateHashLength(hashBytes, algorithm);
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                return new VerifierEntry(name, messageDigest, hashBytes, certificatesArray);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Algorithm not found: " + algorithm, e);
            }
        }
        return null; // Handle case where no verifier entry is found
    }

    private boolean verify(Attributes attributes, String entry, byte[] data,
                           int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA-256 SHA-384 SHA-512";
        }
        String[] tokens = algorithms.split(" ");
        for (String algorithm : tokens) {
            validateAlgorithm(algorithm);
            String hash = attributes.getValue(algorithm + "-Digest");
            if (hash == null) {
                continue;
            }
            byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                messageDigest.update(data, start, end - start);
                byte[] computedHash = messageDigest.digest();
                if (Arrays.equals(computedHash, hashBytes)) {
                    return true; // Return actual verification result
                }
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Algorithm not found: " + algorithm, e);
            }
        }
        return false; // Return false if no hashes matched
    }

    private void validateAlgorithm(String algorithm) {
        if (!SUPPORTED_ALGORITHMS.contains(algorithm)) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }

    private void validateHashLength(byte[] hashBytes, String algorithm) {
        switch (algorithm) {
            case "SHA-256":
                if (hashBytes.length != 32) {
                    throw new IllegalArgumentException("Invalid hash length for algorithm: " + algorithm);
                }
                break;
            case "SHA-384":
                if (hashBytes.length != 48) {
                    throw new IllegalArgumentException("Invalid hash length for algorithm: " + algorithm);
                }
                break;
            case "SHA-512":
                if (hashBytes.length != 64) {
                    throw new IllegalArgumentException("Invalid hash length for algorithm: " + algorithm);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm specified: " + algorithm);
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private final String jarName5 = "hyts_signed_inc.jar";
private final String entryName = "foo/bar/A.class";
private final String entryName3 = "coucou/FileAccess.class";
//<End of snippet n. 1>