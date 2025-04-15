/*OpenSSLCipher: only return block size multiples

There is a faulty test that called .getOutputSize(inputLen) and then
used the output of that to provide inputLen for the .doFinal(...) call.
Unfortunately, this is the only cipher that failed since we're not
returning exact multiples of block size for .getOutputSize(...) calls.

Instead we'll just return exact block size multiples so we don't run
afoul of any other broken code.

Change-Id:I1ca860d6df300ee67df90e575fc476d8291ec9c1*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index 7399a77..e1e2cc1 100644

//Synthetic comment -- @@ -190,7 +190,11 @@

@Override
protected int engineGetOutputSize(int inputLen) {
        if (modeBlockSize == 1) {
            return inputLen;
        } else {
            return inputLen + modeBlockSize;
        }
}

@Override







