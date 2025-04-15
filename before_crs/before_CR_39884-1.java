/*Fix even more CipherTest flakiness due to implicit random IV values

When Cipher.init is not passed AlgorithmParameters, it will implicitly
generate what it needs, leading to random values that occasionally
caused BadPaddingException not to be thrown when expected. Disable
that behavior by explictly passing in the static IV value.

Bug: 6354967
Change-Id:I804c7e30e1c29b92780a9b6271c35505835d58f5*/
//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/CipherTest.java
//Synthetic comment -- index 88fcdd7..88df7a5 100644

//Synthetic comment -- @@ -372,7 +372,7 @@
} catch (IllegalStateException expected) {
}
}

/**
* javax.crypto.Cipher#doFinal()
*/
//Synthetic comment -- @@ -423,9 +423,10 @@

byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] b1 = new byte[30];

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
c.update(b, 0, 10, b1, 5);
try {
c.doFinal();
//Synthetic comment -- @@ -441,13 +442,13 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);
c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);

c.update(b1, 0, 24, b, 0);
try {
//Synthetic comment -- @@ -508,9 +509,10 @@
public void testDoFinalbyteArrayintintbyteArrayint() throws Exception {
byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] b1 = new byte[30];

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
try {
c.doFinal(b, 0, 10, b1, 5);
fail();
//Synthetic comment -- @@ -525,11 +527,10 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);
c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
try {
//Synthetic comment -- @@ -620,9 +621,10 @@
byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
ByteBuffer bInput = ByteBuffer.allocate(64);
ByteBuffer bOutput = ByteBuffer.allocate(64);

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
bInput.put(b, 0, 10);
try {
c.doFinal(bInput, bOutput);
//Synthetic comment -- @@ -638,14 +640,12 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
bInput = ByteBuffer.allocate(16);
bInput.put(b, 0, 16);
int len = c.doFinal(bInput, bOutput);
assertEquals(0, len);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
bInput = ByteBuffer.allocate(64);
//Synthetic comment -- @@ -947,9 +947,10 @@
public void test_doFinal$BI() throws Exception {
byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] b1 = new byte[30];

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
c.update(b, 0, 10);
try {
c.doFinal(b1, 5);
//Synthetic comment -- @@ -965,13 +966,11 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
c.update(b, 3, 8);
int len = c.doFinal(b1, 0);
assertEquals(0, len);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);

c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
c.update(b1, 0, 24);
//Synthetic comment -- @@ -998,9 +997,10 @@
byte[] bI2 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
byte[] bI3 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] bI4 = {1,2,3};

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
try {
c.doFinal(bI1);
fail();
//Synthetic comment -- @@ -1015,14 +1015,13 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len1 = c.doFinal(bI2).length;
assertEquals(16, len1);
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len2 = c.doFinal(bI3, 0, 16, b1, 0);
assertEquals(16, len2);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);
c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
try {
//Synthetic comment -- @@ -1035,9 +1034,10 @@
public void test_doFinal$BII() throws Exception {
byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] b1 = new byte[30];

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
try {
c.doFinal(b, 0, 10);
fail();
//Synthetic comment -- @@ -1052,14 +1052,13 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len1 = c.doFinal(b, 0, 16).length;
assertEquals(16, len1);
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len2 = c.doFinal(b, 0, 16, b1, 0);
assertEquals(16, len2);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);
c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
try {
//Synthetic comment -- @@ -1072,9 +1071,10 @@
public void test_doFinal$BII$B() throws Exception {
byte[] b = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
byte[] b1 = new byte[30];

Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
try {
c.doFinal(b, 0, 10, b1);
fail();
//Synthetic comment -- @@ -1089,11 +1089,10 @@
}

c = Cipher.getInstance("DES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, CIPHER_KEY_DES);
int len = c.doFinal(b, 0, 16, b1);
assertEquals(16, len);

        AlgorithmParameterSpec ap = new IvParameterSpec(IV);
c = Cipher.getInstance("DES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, CIPHER_KEY_DES, ap);
try {







