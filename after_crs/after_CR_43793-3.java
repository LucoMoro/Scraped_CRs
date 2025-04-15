/*More testing of Cipher .doFinal variants

Change-Id:I5f94eac56da177de5d395277f246263af32c67c3*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 76a4176..cef625a 100644

//Synthetic comment -- @@ -1716,17 +1716,45 @@
c.init(Cipher.ENCRYPT_MODE, key, spec);

final byte[] actualCiphertext = c.doFinal(p.plaintext);
        assertEquals(Arrays.toString(p.ciphertext), Arrays.toString(actualCiphertext));

c.init(Cipher.DECRYPT_MODE, key, spec);

        // .doFinal(input)
        {
            final byte[] actualPlaintext = c.doFinal(p.ciphertext);
            assertEquals(Arrays.toString(p.plaintext), Arrays.toString(actualPlaintext));
        }

        // .doFinal(input, offset, len, output)
        {
            final byte[] largerThanCiphertext = new byte[p.ciphertext.length + 5];
            System.arraycopy(p.ciphertext, 0, largerThanCiphertext, 5, p.ciphertext.length);

            final byte[] actualPlaintext = new byte[c.getOutputSize(p.ciphertext.length)];
            assertEquals(p.plaintext.length,
                    c.doFinal(largerThanCiphertext, 5, p.ciphertext.length, actualPlaintext));
            assertEquals(Arrays.toString(p.plaintext),
                    Arrays.toString(Arrays.copyOfRange(actualPlaintext, 0, p.plaintext.length)));
        }

        // .doFinal(input, offset, len, output, offset)
        {
            final byte[] largerThanCiphertext = new byte[p.ciphertext.length + 10];
            System.arraycopy(p.ciphertext, 0, largerThanCiphertext, 5, p.ciphertext.length);

            final byte[] actualPlaintext = new byte[c.getOutputSize(p.ciphertext.length) + 2];
            assertEquals(p.plaintext.length,
                    c.doFinal(largerThanCiphertext, 5, p.ciphertext.length, actualPlaintext, 1));
            assertEquals(Arrays.toString(p.plaintext),
                    Arrays.toString(Arrays.copyOfRange(actualPlaintext, 1, p.plaintext.length + 1)));
        }

Cipher cNoPad = Cipher.getInstance(p.mode + "/NoPadding");
cNoPad.init(Cipher.DECRYPT_MODE, key, spec);

final byte[] actualPlaintextPadded = cNoPad.doFinal(p.ciphertext);
        assertEquals(Arrays.toString(p.plaintextPadded), Arrays.toString(actualPlaintextPadded));
assertTrue(Arrays.equals(p.plaintextPadded, actualPlaintextPadded));
}








