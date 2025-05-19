
//<Beginning of snippet n. 0>


* </ul>
*/
class JarVerifier {

private final String jarName;

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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



private final String jarName5 = "hyts_signed_inc.jar";

private final String entryName = "foo/bar/A.class";

private final String entryName3 = "coucou/FileAccess.class";
} catch (Exception e) {
fail("Exception during test 5: " + e);
}
}

/*

//<End of snippet n. 1>








