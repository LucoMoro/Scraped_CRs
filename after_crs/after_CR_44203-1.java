/*OpenSSLCipher: account for padding on doFinal

Decrypting also needs to check padding on the last block, so special
case encrypting in getOutputSize

Change-Id:I0bfaf6f40f5d618e4dd1853668ec5400058e6b67*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index e628e4a..009e05b 100644

//Synthetic comment -- @@ -191,7 +191,7 @@
return inputLen;
} else {
final int buffered = NativeCrypto.get_EVP_CIPHER_CTX_buf_len(cipherCtx.getContext());
            if (padding == Padding.NOPADDING) {
return buffered + inputLen;
} else {
final int totalLen = inputLen + buffered + modeBlockSize;
//Synthetic comment -- @@ -417,7 +417,7 @@

final int maximumLen = getOutputSize(inputLen);
/* Assume that we'll output exactly on a byte boundary. */
        final byte[] output = new byte[maximumLen];
final int bytesWritten;
try {
bytesWritten = doFinalInternal(input, inputOffset, inputLen, output, 0, maximumLen);







