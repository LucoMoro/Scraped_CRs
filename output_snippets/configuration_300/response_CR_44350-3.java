//<Beginning of snippet n. 0>
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.modelmbean.Attributes;

class JarVerifier {

    private static final Logger logger = Logger.getLogger(JarVerifier.class.getName());
    private final String jarName;

    public JarVerifier(String jarName) {
        this.jarName = jarName;
    }

    public VerifierEntry verifyEntry(Attributes attributes, String name, List<Certificate> certs) {
        Certificate[] certificatesArray = certs.toArray(new Certificate[0]);

        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA SHA1 SHA-256 SHA-384 SHA-512";
        }
        String[] tokens = algorithms.split(" ");
        for (String algorithm : tokens) {
            String hash = attributes.getValue(algorithm + "-Digest");
            if (hash == null) {
                continue;
            }
            byte[] hashBytes = hash.getBytes(Charsets.ISO_8859_1);

            try {
                return new VerifierEntry(name, MessageDigest.getInstance(algorithm), hashBytes, certificatesArray);
            } catch (NoSuchAlgorithmException e) {
                logger.log(Level.SEVERE, "Unsupported algorithm: " + algorithm, e);
            }
        }
        throw new IllegalArgumentException("No supported algorithms matched."); 
    }

    private boolean verify(Attributes attributes, String entry, byte[] data, int start, int end, boolean ignoreSecondEndline, boolean ignorable) {
        String algorithms = attributes.getValue("Digest-Algorithms");
        if (algorithms == null) {
            algorithms = "SHA SHA1 SHA-256 SHA-384 SHA-512";
        }
        String[] tokens = algorithms.split(" ");
        for (String algorithm : tokens) {
            String hash = attributes.getValue(algorithm + entry);
            if (hash == null) {
                continue;
            }
            // Verification logic here...
        }
        return false; // In case verification fails
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