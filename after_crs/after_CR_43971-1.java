/*Test key wrapping for all Ciphers

Change-Id:I1320f30602e17b730feae5676e34b1550f8eb8b8*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index cef625a..02f8b49 100644

//Synthetic comment -- @@ -85,20 +85,6 @@
return false;
}

private static String getBaseAlgorithm(String algorithm) {
if (algorithm.equals("AESWRAP")) {
return "AES";
//Synthetic comment -- @@ -173,7 +159,7 @@
return getBaseAlgorithm(algorithm).equals("RSA");
}

    private static boolean isOnlyWrappingAlgorithm(String algorithm) {
return algorithm.endsWith("WRAP");
}

//Synthetic comment -- @@ -562,7 +548,12 @@
} else {
spec = null;
}

        if (!isOnlyWrappingAlgorithm(algorithm)) {
            c.init(Cipher.ENCRYPT_MODE, encryptKey, spec);
        } else {
            c.init(Cipher.WRAP_MODE, encryptKey, spec);
        }

assertEquals("getBlockSize()", getExpectedBlockSize(algorithm, encryptKey),
c.getBlockSize());
//Synthetic comment -- @@ -576,22 +567,35 @@

assertNull(c.getExemptionMechanism());

        // Test wrapping a key. Every cipher should be able to wrap.
        {
            // Generate a small SecretKey for AES.
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey sk = kg.generateKey();

            // Wrap it
            c.init(Cipher.WRAP_MODE, encryptKey, spec);
            byte[] cipherText = c.wrap(sk);

            // Unwrap it
            c.init(Cipher.UNWRAP_MODE, getDecryptKey(algorithm), spec);
            Key decryptedKey = c.unwrap(cipherText, sk.getAlgorithm(), Cipher.SECRET_KEY);

            assertEquals("sk.getAlgorithm()=" + sk.getAlgorithm()
                    + " decryptedKey.getAlgorithm()=" + decryptedKey.getAlgorithm()
                    + " encryptKey.getEncoded()=" + Arrays.toString(sk.getEncoded())
                    + " decryptedKey.getEncoded()=" + Arrays.toString(decryptedKey.getEncoded()),
                    sk, decryptedKey);
        }

        if (!isOnlyWrappingAlgorithm(algorithm)) {
            c.init(Cipher.ENCRYPT_MODE, encryptKey, spec);
byte[] cipherText = c.doFinal(ORIGINAL_PLAIN_TEXT);
            c.init(Cipher.DECRYPT_MODE, getDecryptKey(algorithm), spec);
byte[] decryptedPlainText = c.doFinal(cipherText);
assertEquals(Arrays.toString(getExpectedPlainText(algorithm)),
                    Arrays.toString(decryptedPlainText));
}
}








