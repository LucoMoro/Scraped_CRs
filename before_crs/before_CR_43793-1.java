/*More testing of Cipher .doFinal variants

Change-Id:I5f94eac56da177de5d395277f246263af32c67c3*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 76a4176..b5d508f 100644

//Synthetic comment -- @@ -1720,8 +1720,34 @@

c.init(Cipher.DECRYPT_MODE, key, spec);

        final byte[] actualPlaintext = c.doFinal(p.ciphertext);
        assertTrue(Arrays.equals(p.plaintext, actualPlaintext));

Cipher cNoPad = Cipher.getInstance(p.mode + "/NoPadding");
cNoPad.init(Cipher.DECRYPT_MODE, key, spec);







