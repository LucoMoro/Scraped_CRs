/*More testing of Cipher .doFinal variants

Change-Id:I5f94eac56da177de5d395277f246263af32c67c3*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 76a4176..aa84e07 100644

//Synthetic comment -- @@ -1716,17 +1716,47 @@
c.init(Cipher.ENCRYPT_MODE, key, spec);

final byte[] actualCiphertext = c.doFinal(p.plaintext);
        assertTrue(Arrays.equals(p.ciphertext, actualCiphertext));

c.init(Cipher.DECRYPT_MODE, key, spec);

        final byte[] actualPlaintext = c.doFinal(p.ciphertext);
        assertTrue(Arrays.equals(p.plaintext, actualPlaintext));

Cipher cNoPad = Cipher.getInstance(p.mode + "/NoPadding");
cNoPad.init(Cipher.DECRYPT_MODE, key, spec);

final byte[] actualPlaintextPadded = cNoPad.doFinal(p.ciphertext);
assertTrue(Arrays.equals(p.plaintextPadded, actualPlaintextPadded));
}








