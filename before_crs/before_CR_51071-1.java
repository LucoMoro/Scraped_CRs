/*OpenSSLCipher: fix short buffer error message

Change-Id:I4f16bee3c57c80a113bd92509451606d5fd2b666*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index 46d3559..007ff41 100644

//Synthetic comment -- @@ -315,7 +315,7 @@
final int bytesLeft = output.length - outputOffset;
if (bytesLeft < maximumLen) {
throw new ShortBufferException("output buffer too small during update: " + bytesLeft
                    + " < " + output.length);
}

outputOffset += NativeCrypto.EVP_CipherUpdate(cipherCtx.getContext(), output, outputOffset,







