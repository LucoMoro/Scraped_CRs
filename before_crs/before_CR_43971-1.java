/*Test key wrapping for all Ciphers

Change-Id:I1320f30602e17b730feae5676e34b1550f8eb8b8*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index cef625a..02f8b49 100644

//Synthetic comment -- @@ -85,20 +85,6 @@
return false;
}

    private synchronized static int getEncryptMode(String algorithm) throws Exception {
        if (isWrap(algorithm)) {
            return Cipher.WRAP_MODE;
        }
        return Cipher.ENCRYPT_MODE;
    }

    private synchronized static int getDecryptMode(String algorithm) throws Exception {
        if (isWrap(algorithm)) {
            return Cipher.UNWRAP_MODE;
        }
        return Cipher.DECRYPT_MODE;
    }

private static String getBaseAlgorithm(String algorithm) {
if (algorithm.equals("AESWRAP")) {
return "AES";
//Synthetic comment -- @@ -173,7 +159,7 @@
return getBaseAlgorithm(algorithm).equals("RSA");
}

    private static boolean isWrap(String algorithm) {
return algorithm.endsWith("WRAP");
}

//Synthetic comment -- @@ -562,7 +548,12 @@
} else {
spec = null;
}
        c.init(getEncryptMode(algorithm), encryptKey, spec);

assertEquals("getBlockSize()", getExpectedBlockSize(algorithm, encryptKey),
c.getBlockSize());
//Synthetic comment -- @@ -576,22 +567,35 @@

assertNull(c.getExemptionMechanism());

        if (isWrap(algorithm)) {
            byte[] cipherText = c.wrap(encryptKey);
            c.init(getDecryptMode(algorithm), getDecryptKey(algorithm));
            int keyType = (isAsymmetric(algorithm)) ? Cipher.PRIVATE_KEY : Cipher.SECRET_KEY;
            Key decryptedKey = c.unwrap(cipherText, encryptKey.getAlgorithm(), keyType);
            assertEquals("encryptKey.getAlgorithm()=" + encryptKey.getAlgorithm()
                         + " decryptedKey.getAlgorithm()=" + decryptedKey.getAlgorithm()
                         + " encryptKey.getEncoded()=" + Arrays.toString(encryptKey.getEncoded())
                         + " decryptedKey.getEncoded()=" + Arrays.toString(decryptedKey.getEncoded()),
                         encryptKey, decryptedKey);
        } else {
byte[] cipherText = c.doFinal(ORIGINAL_PLAIN_TEXT);
            c.init(getDecryptMode(algorithm), getDecryptKey(algorithm), spec);
byte[] decryptedPlainText = c.doFinal(cipherText);
assertEquals(Arrays.toString(getExpectedPlainText(algorithm)),
                         Arrays.toString(decryptedPlainText));
}
}








