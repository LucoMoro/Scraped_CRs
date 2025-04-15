/*OpenSSLCipher: 3DES cipher name depends on key size

OpenSSL doesn't infer from the key size whether to use two-key or
three-key 3DES, so explicitly call it out.

Change-Id:Ibd93088844e7585e72a7c7857dd2af8a150b3780*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index ce50553..03a32e6 100644

//Synthetic comment -- @@ -769,10 +769,17 @@

@Override
protected String getCipherName(int keySize, Mode mode) {
            final String baseCipherName;
            if (keySize == 16) {
                baseCipherName = "des-ede";
} else {
                baseCipherName = "des-ede3";
            }

            if (mode == Mode.ECB) {
                return baseCipherName;
            } else {
                return baseCipherName + "-" + mode.toString().toLowerCase(Locale.US);
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java
//Synthetic comment -- index 7442210..c0f15d2 100644

//Synthetic comment -- @@ -417,8 +417,8 @@

byte[] cipherText = loadBytes("hyts_" + "des-ede3-cbc.test" + index
+ ".ciphertext");
            assertEquals("Operation produced incorrect results for index " + index,
                    Arrays.toString(cipherText), Arrays.toString(encryptedPlaintext));
}

byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};







