/*Do not use OpenSSLCipherContext in tests

Change-Id:I422954e7e9a9d1021d4281a254cdd732f37ca2bf*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherContext.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherContext.java
//Synthetic comment -- index 81933dc..b978b42 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package org.apache.harmony.xnet.provider.jsse;

public class OpenSSLCipherContext {
private final int context;

    public OpenSSLCipherContext(int ctx) {
if (ctx == 0) {
throw new NullPointerException("ctx == 0");
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java
//Synthetic comment -- index 6c0b434..c5be834 100644

//Synthetic comment -- @@ -1746,28 +1746,36 @@
};

public void test_EVP_CipherInit_ex_Null_Failure() throws Exception {
        OpenSSLCipherContext context = new OpenSSLCipherContext(NativeCrypto.EVP_CIPHER_CTX_new());
        int evpCipher = NativeCrypto.EVP_get_cipherbyname("aes-128-ecb");

try {
            NativeCrypto.EVP_CipherInit_ex(NULL, evpCipher, null, null, true);
            fail("Null context should throw NullPointerException");
        } catch (NullPointerException expected) {
}

        /* Initialize encrypting. */
        NativeCrypto.EVP_CipherInit_ex(context.getContext(), evpCipher, null, null, true);
        NativeCrypto.EVP_CipherInit_ex(context.getContext(), NULL, null, null, true);

        /* Initialize decrypting. */
        NativeCrypto.EVP_CipherInit_ex(context.getContext(), evpCipher, null, null, false);
        NativeCrypto.EVP_CipherInit_ex(context.getContext(), NULL, null, null, false);
}

public void test_EVP_CipherInit_ex_Success() throws Exception {
        OpenSSLCipherContext context = new OpenSSLCipherContext(NativeCrypto.EVP_CIPHER_CTX_new());
        int evpCipher = NativeCrypto.EVP_get_cipherbyname("aes-128-ecb");
        NativeCrypto.EVP_CipherInit_ex(context.getContext(), evpCipher, AES_128_KEY, null, true);
}

public void test_EVP_CIPHER_iv_length() throws Exception {







