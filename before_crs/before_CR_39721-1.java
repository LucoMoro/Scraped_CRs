/*Address more CipherTest flakiness due to random IV values

In a recent change, we stopped generating random keys to prevent
flakiness in the BadPaddingException test cases. This change removes
the use of random initialization vector (IV) values. There still are
uses of SecureRandom where the API being tested requires it, but I
visually checked that none of the BadPaddingException cases fall in
this category.

Bug: 6873673

(cherry-picked from f19b9c467fb584a79463357e160c0a41c80d05c8)

Change-Id:Id09c322683972751e326569485330c23051ccf0c*/
//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java
//Synthetic comment -- index 4096a30..88fcdd7 100644

//Synthetic comment -- @@ -57,6 +57,9 @@

private static final Key CIPHER_KEY_DES;
private static final String ALGORITHM_DES = "DES";

static {
try {
//Synthetic comment -- @@ -203,7 +206,6 @@
*/
public void test_getOutputSizeI() throws Exception {

        SecureRandom sr = new SecureRandom();
Cipher cipher = Cipher.getInstance(ALGORITHM_3DES + "/ECB/PKCS5Padding");

try {
//Synthetic comment -- @@ -212,7 +214,7 @@
} catch (IllegalStateException expected) {
}

        cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, sr);

// A 25-byte input could result in at least 4 8-byte blocks
int result = cipher.getOutputSize(25);
//Synthetic comment -- @@ -244,13 +246,12 @@
*        java.security.SecureRandom)
*/
public void test_initWithSecureRandom() throws Exception {
        SecureRandom sr = new SecureRandom();
Cipher cipher = Cipher.getInstance(ALGORITHM_3DES + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, sr);

cipher = Cipher.getInstance("DES/CBC/NoPadding");
try {
            cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, sr);
fail();
} catch (InvalidKeyException expected) {
}
//Synthetic comment -- @@ -261,23 +262,12 @@
*        java.security.spec.AlgorithmParameterSpec)
*/
public void test_initWithAlgorithmParameterSpec() throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = null;

        byte[] iv = null;
        AlgorithmParameterSpec ap = null;

        iv = new byte[8];
        sr.nextBytes(iv);
        ap = new IvParameterSpec(iv);

        cipher = Cipher.getInstance(ALGORITHM_3DES + "/CBC/PKCS5Padding");

cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, ap);

byte[] cipherIV = cipher.getIV();

        assertTrue("IVs differ", Arrays.equals(cipherIV, iv));

cipher = Cipher.getInstance("DES/CBC/NoPadding");
try {
//Synthetic comment -- @@ -288,7 +278,6 @@

cipher = Cipher.getInstance("DES/CBC/NoPadding");
ap = new RSAKeyGenParameterSpec(10, new BigInteger("10"));

try {
cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES, ap);
fail();
//Synthetic comment -- @@ -302,26 +291,16 @@
*        java.security.SecureRandom)
*/
public void test_initWithKeyAlgorithmParameterSpecSecureRandom() throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = null;

        byte[] iv = null;
        AlgorithmParameterSpec ap = null;

        iv = new byte[8];
        sr.nextBytes(iv);
        ap = new IvParameterSpec(iv);

        cipher = Cipher.getInstance(ALGORITHM_3DES + "/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, ap, sr);

byte[] cipherIV = cipher.getIV();

        assertTrue("IVs differ", Arrays.equals(cipherIV, iv));
cipher = Cipher.getInstance("DES/CBC/NoPadding");
try {
            cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_3DES, ap, sr);
fail();
} catch (InvalidKeyException expected) {
}
//Synthetic comment -- @@ -330,7 +309,7 @@
ap = new RSAKeyGenParameterSpec(10, new BigInteger("10"));

try {
            cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES, ap, sr);
fail();
} catch (InvalidAlgorithmParameterException expected) {
}
//Synthetic comment -- @@ -466,11 +445,7 @@
int len = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

//Synthetic comment -- @@ -554,14 +529,9 @@
int len = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

try {
c.doFinal(b1, 0, 24, new byte[42], 0);
fail();
//Synthetic comment -- @@ -674,15 +644,11 @@
int len = c.doFinal(bInput, bOutput);
assertEquals(0, len);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
bInput = ByteBuffer.allocate(64);

try {
c.doFinal(bOutput, bInput);
fail();
//Synthetic comment -- @@ -720,10 +686,7 @@
}

public void test_initWithKeyAlgorithmParameters() throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
//Synthetic comment -- @@ -743,13 +706,10 @@
}

public void test_initWithKeyAlgorithmParametersSecureRandom() throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap, sr);
assertNotNull(c.getParameters());

try {
//Synthetic comment -- @@ -759,7 +719,8 @@
}

try {
            c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, (AlgorithmParameters)null, sr);
fail();
} catch (InvalidAlgorithmParameterException expected) {
}
//Synthetic comment -- @@ -829,14 +790,11 @@
}

public void test_unwrap$BLjava_lang_StringI() throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");

        c.init(Cipher.WRAP_MODE, CIPHER_KEY_DES, ap, sr);
byte[] arDES = c.wrap(CIPHER_KEY_DES);
byte[] ar    = c.wrap(CIPHER_KEY_3DES);

//Synthetic comment -- @@ -846,7 +804,7 @@
} catch (IllegalStateException expected) {
}

        c.init(Cipher.UNWRAP_MODE, CIPHER_KEY_DES, ap, sr);
assertTrue(CIPHER_KEY_DES.equals(c.unwrap(arDES, "DES", Cipher.SECRET_KEY)));
assertFalse(CIPHER_KEY_DES.equals(c.unwrap(ar, "DES", Cipher.SECRET_KEY)));

//Synthetic comment -- @@ -857,7 +815,7 @@
}

c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        c.init(Cipher.UNWRAP_MODE, CIPHER_KEY_3DES, ap, sr);
try {
c.unwrap(arDES, "DESede", Cipher.SECRET_KEY);
fail();
//Synthetic comment -- @@ -892,10 +850,7 @@
bOutput.rewind();
c.update(bInput, bOutput);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
//Synthetic comment -- @@ -951,14 +906,11 @@
}

public void test_wrap_java_security_Key() throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");

        c.init(Cipher.WRAP_MODE, CIPHER_KEY_DES, ap, sr);
assertNotNull(c.wrap(CIPHER_KEY_DES));
assertNotNull(c.wrap(CIPHER_KEY_3DES));
String certName = Support_Resources.getURL("test.cert");
//Synthetic comment -- @@ -969,14 +921,14 @@
assertNotNull(c.wrap(cert.getPublicKey()));

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.WRAP_MODE, CIPHER_KEY_DES, ap, sr);
try {
assertNotNull(c.wrap(cert.getPublicKey()));
fail();
} catch (IllegalBlockSizeException expected) {
}

        c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap, sr);

try {
c.wrap(CIPHER_KEY_DES);
//Synthetic comment -- @@ -984,7 +936,7 @@
} catch (IllegalStateException expected) {
}

        c.init(Cipher.WRAP_MODE, CIPHER_KEY_DES, ap, sr);
try {
c.wrap(new Mock_Key());
fail();
//Synthetic comment -- @@ -1018,14 +970,10 @@
int len = c.doFinal(b1, 0);
assertEquals(0, len);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

c.update(b1, 0, 24);
try {
c.doFinal(b, 0);
//Synthetic comment -- @@ -1074,14 +1022,9 @@
int len2 = c.doFinal(bI3, 0, 16, b1, 0);
assertEquals(16, len2);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

try {
c.doFinal(b1);
fail();
//Synthetic comment -- @@ -1116,14 +1059,9 @@
int len2 = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len2);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

try {
c.doFinal(b1, 0, 24);
fail();
//Synthetic comment -- @@ -1155,14 +1093,9 @@
int len = c.doFinal(b, 0, 16, b1);
assertEquals(16, len);

        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[8];
        sr.nextBytes(iv);
        AlgorithmParameterSpec ap = new IvParameterSpec(iv);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

try {
c.doFinal(b1, 0, 24, new byte[42]);
fail();







