/*add multiple key support to signapk

Support signing .apks (but not OTA packages) with multiple keys.

Bug: 7350459
Change-Id:I794e1da0555e2bb9247a59c756656d4ca7ee04cf*/




//Synthetic comment -- diff --git a/tools/signapk/SignApk.java b/tools/signapk/SignApk.java
//Synthetic comment -- index 07aefa7..1055704 100644

//Synthetic comment -- @@ -83,6 +83,8 @@
class SignApk {
private static final String CERT_SF_NAME = "META-INF/CERT.SF";
private static final String CERT_RSA_NAME = "META-INF/CERT.RSA";
    private static final String CERT_SF_MULTI_NAME = "META-INF/CERT%d.SF";
    private static final String CERT_RSA_MULTI_NAME = "META-INF/CERT%d.RSA";

private static final String OTACERT_NAME = "META-INF/com/android/otacert";

//Synthetic comment -- @@ -90,7 +92,8 @@

// Files matching this pattern are not copied to the output.
private static Pattern stripPattern =
        Pattern.compile("^(META-INF/((.*)[.](SF|RSA|DSA)|com/android/otacert))|(" +
                        Pattern.quote(JarFile.MANIFEST_NAME) + ")$");

private static X509Certificate readPublicKey(File file)
throws IOException, GeneralSecurityException {
//Synthetic comment -- @@ -208,11 +211,8 @@

for (JarEntry entry: byName.values()) {
String name = entry.getName();
            if (!entry.isDirectory() &&
                (stripPattern == null || !stripPattern.matcher(name).matches())) {
InputStream data = jar.getInputStream(entry);
while ((num = data.read(buffer)) > 0) {
md.update(buffer, 0, num);
//Synthetic comment -- @@ -499,13 +499,16 @@
}
}

    private static void usage() {
        System.err.println("Usage: signapk [-w] " +
                           "publickey.x509[.pem] privatekey.pk8 " +
                           "[publickey2.x509[.pem] privatekey2.pk8 ...] " +
                           "input.jar output.jar");
        System.exit(2);
    }

public static void main(String[] args) {
        if (args.length < 4) usage();

sBouncyCastleProvider = new BouncyCastleProvider();
Security.addProvider(sBouncyCastleProvider);
//Synthetic comment -- @@ -517,25 +520,46 @@
argstart = 1;
}

        if ((args.length - argstart) % 2 == 1) usage();
        int numKeys = ((args.length - argstart) / 2) - 1;
        if (signWholeFile && numKeys > 1) {
            System.err.println("Only one key may be used with -w.");
            System.exit(2);
        }

        String inputFilename = args[args.length-2];
        String outputFilename = args[args.length-1];

JarFile inputJar = null;
JarOutputStream outputJar = null;
FileOutputStream outputFile = null;

try {
            File firstPublicKeyFile = new File(args[argstart+0]);

            X509Certificate[] publicKey = new X509Certificate[numKeys];
            for (int i = 0; i < numKeys; ++i) {
                int argNum = argstart + i*2;
                publicKey[i] = readPublicKey(new File(args[argNum]));
            }

            // Set the ZIP file timestamp to the starting valid time
            // of the 0th certificate plus one hour (to match what
            // we've historically done).
            long timestamp = publicKey[0].getNotBefore().getTime() + 3600L * 1000;

            PrivateKey[] privateKey = new PrivateKey[numKeys];
            for (int i = 0; i < numKeys; ++i) {
                int argNum = argstart + i*2 + 1;
                privateKey[i] = readPrivateKey(new File(args[argNum]));
            }
            inputJar = new JarFile(new File(inputFilename), false);  // Don't verify.

OutputStream outputStream = null;
if (signWholeFile) {
outputStream = new ByteArrayOutputStream();
} else {
                outputStream = outputFile = new FileOutputStream(outputFilename);
}
outputJar = new JarOutputStream(outputStream);

//Synthetic comment -- @@ -558,7 +582,7 @@

// otacert
if (signWholeFile) {
                addOtacert(outputJar, firstPublicKeyFile, timestamp, manifest);
}

// MANIFEST.MF
//Synthetic comment -- @@ -567,30 +591,40 @@
outputJar.putNextEntry(je);
manifest.write(outputJar);

            // In the case of multiple keys, all the .SF files will be
            // identical, but as far as I can tell the jarsigner docs
            // don't allow there to be just one copy in the zipfile;
            // there hase to be one per .RSA file.

ByteArrayOutputStream baos = new ByteArrayOutputStream();
writeSignatureFile(manifest, baos);
byte[] signedData = baos.toByteArray();

            for (int k = 0; k < numKeys; ++k) {
                // CERT.SF / CERT#.SF
                je = new JarEntry(numKeys == 1 ? CERT_SF_NAME :
                                  (String.format(CERT_SF_MULTI_NAME, k)));
                je.setTime(timestamp);
                outputJar.putNextEntry(je);
                outputJar.write(signedData);

                // CERT.RSA / CERT#.RSA
                je = new JarEntry(numKeys == 1 ? CERT_RSA_NAME :
                                  (String.format(CERT_RSA_MULTI_NAME, k)));
                je.setTime(timestamp);
                outputJar.putNextEntry(je);
                writeSignatureBlock(new CMSProcessableByteArray(signedData),
                                    publicKey[k], privateKey[k], outputJar);
            }

outputJar.close();
outputJar = null;
outputStream.flush();

if (signWholeFile) {
                outputFile = new FileOutputStream(outputFilename);
signWholeOutputFile(((ByteArrayOutputStream)outputStream).toByteArray(),
                                    outputFile, publicKey[0], privateKey[0]);
}
} catch (Exception e) {
e.printStackTrace();







