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

private static final String OTACERT_NAME = "META-INF/com/android/otacert";

//Synthetic comment -- @@ -90,7 +92,8 @@

// Files matching this pattern are not copied to the output.
private static Pattern stripPattern =
            Pattern.compile("^META-INF/(.*)[.](SF|RSA|DSA)$");

private static X509Certificate readPublicKey(File file)
throws IOException, GeneralSecurityException {
//Synthetic comment -- @@ -208,11 +211,8 @@

for (JarEntry entry: byName.values()) {
String name = entry.getName();
            if (!entry.isDirectory() && !name.equals(JarFile.MANIFEST_NAME) &&
                !name.equals(CERT_SF_NAME) && !name.equals(CERT_RSA_NAME) &&
                !name.equals(OTACERT_NAME) &&
                (stripPattern == null ||
                 !stripPattern.matcher(name).matches())) {
InputStream data = jar.getInputStream(entry);
while ((num = data.read(buffer)) > 0) {
md.update(buffer, 0, num);
//Synthetic comment -- @@ -499,13 +499,16 @@
}
}

public static void main(String[] args) {
        if (args.length != 4 && args.length != 5) {
            System.err.println("Usage: signapk [-w] " +
                    "publickey.x509[.pem] privatekey.pk8 " +
                    "input.jar output.jar");
            System.exit(2);
        }

sBouncyCastleProvider = new BouncyCastleProvider();
Security.addProvider(sBouncyCastleProvider);
//Synthetic comment -- @@ -517,25 +520,46 @@
argstart = 1;
}

JarFile inputJar = null;
JarOutputStream outputJar = null;
FileOutputStream outputFile = null;

try {
            File publicKeyFile = new File(args[argstart+0]);
            X509Certificate publicKey = readPublicKey(publicKeyFile);

            // Assume the certificate is valid for at least an hour.
            long timestamp = publicKey.getNotBefore().getTime() + 3600L * 1000;

            PrivateKey privateKey = readPrivateKey(new File(args[argstart+1]));
            inputJar = new JarFile(new File(args[argstart+2]), false);  // Don't verify.

OutputStream outputStream = null;
if (signWholeFile) {
outputStream = new ByteArrayOutputStream();
} else {
                outputStream = outputFile = new FileOutputStream(args[argstart+3]);
}
outputJar = new JarOutputStream(outputStream);

//Synthetic comment -- @@ -558,7 +582,7 @@

// otacert
if (signWholeFile) {
                addOtacert(outputJar, publicKeyFile, timestamp, manifest);
}

// MANIFEST.MF
//Synthetic comment -- @@ -567,30 +591,40 @@
outputJar.putNextEntry(je);
manifest.write(outputJar);

            // CERT.SF
            je = new JarEntry(CERT_SF_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
ByteArrayOutputStream baos = new ByteArrayOutputStream();
writeSignatureFile(manifest, baos);
byte[] signedData = baos.toByteArray();
            outputJar.write(signedData);

            // CERT.RSA
            je = new JarEntry(CERT_RSA_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            writeSignatureBlock(new CMSProcessableByteArray(signedData),
                                publicKey, privateKey, outputJar);

outputJar.close();
outputJar = null;
outputStream.flush();

if (signWholeFile) {
                outputFile = new FileOutputStream(args[argstart+3]);
signWholeOutputFile(((ByteArrayOutputStream)outputStream).toByteArray(),
                                    outputFile, publicKey, privateKey);
}
} catch (Exception e) {
e.printStackTrace();







