/*Remove BouncyCastle exclusion of PBE ciphers from wrapping tests

Bug:https://code.google.com/p/android/issues/detail?id=41405Change-Id:Ie5942f4ef1d872a75d89c58ea0fd85f69c63d0cf*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index a98cdec..9f19eef 100644

//Synthetic comment -- @@ -210,11 +210,6 @@
return algorithm.endsWith("WRAP");
}

private static boolean isPBE(String algorithm) {
return algorithm.startsWith("PBE");
}
//Synthetic comment -- @@ -705,8 +700,8 @@

assertNull(cipherID, c.getExemptionMechanism());

        // Test wrapping a key.  Every cipher should be able to wrap.
        {
// Generate a small SecretKey for AES.
KeyGenerator kg = KeyGenerator.getInstance("AES");
kg.init(128);







