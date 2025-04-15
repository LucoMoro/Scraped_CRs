/*More testing of Cipher .doFinal variants

Change-Id:I5f94eac56da177de5d395277f246263af32c67c3*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 76a4176..b5d508f 100644

//Synthetic comment -- @@ -1720,8 +1720,34 @@

c.init(Cipher.DECRYPT_MODE, key, spec);

        // Try the normal .doFinal(input) call
        {
            final byte[] actualPlaintext = c.doFinal(p.ciphertext);
            assertTrue(Arrays.equals(p.plaintext, actualPlaintext));
        }

        // .doFinal(input, offset, len, output),
        {
            final byte[] largerThanCiphertext = new byte[p.ciphertext.length + 5];
            System.arraycopy(p.ciphertext, 0, largerThanCiphertext, 5, p.ciphertext.length);
            final byte[] actualPlaintext = new byte[p.plaintext.length];

            assertEquals(p.plaintext.length,
                    c.doFinal(largerThanCiphertext, 5, p.ciphertext.length, actualPlaintext));
            assertTrue(Arrays.equals(p.plaintext, actualPlaintext));
        }

        // Try .doFinal(input, offset, len, output, offset)
        {
            final byte[] largerThanCiphertext = new byte[p.ciphertext.length + 10];
            System.arraycopy(p.ciphertext, 0, largerThanCiphertext, 5, p.ciphertext.length);
            final byte[] actualPlaintext = new byte[p.plaintext.length + 2];

            assertEquals(p.plaintext.length,
                    c.doFinal(largerThanCiphertext, 5, p.ciphertext.length, actualPlaintext, 1));
            assertTrue(Arrays.equals(p.plaintext,
                    Arrays.copyOfRange(actualPlaintext, 1, p.plaintext.length + 2)));
        }

Cipher cNoPad = Cipher.getInstance(p.mode + "/NoPadding");
cNoPad.init(Cipher.DECRYPT_MODE, key, spec);







