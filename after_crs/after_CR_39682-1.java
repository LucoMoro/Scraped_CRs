/*Signature.verify should not throw if called twice

Bug:http://code.google.com/p/android/issues/detail?id=34933Change-Id:Iad18e46729dcd283f4cecd65994ac7b741bd3036*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSignature.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSignature.java
//Synthetic comment -- index 370e7a4..d9746c6 100644

//Synthetic comment -- @@ -241,7 +241,7 @@
key.getPkeyContext());
return result == 1;
} catch (Exception ex) {
            return false;
} finally {
/*
* Java expects the digest context to be reset completely after








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/SignatureTest.java b/luni/src/test/java/libcore/java/security/SignatureTest.java
//Synthetic comment -- index 7364c93..2cd9982 100644

//Synthetic comment -- @@ -112,12 +112,20 @@
sig.initSign(keyPair.getPrivate());
sig.update(DATA);
byte[] signature = sig.sign();
        assertNotNull(sig.getAlgorithm(), signature);
        assertTrue(sig.getAlgorithm(), signature.length > 0);

sig.initVerify(keyPair.getPublic());
sig.update(DATA);
        assertTrue(sig.getAlgorithm(), sig.verify(signature));

        // After verify, should be reusable as if we are after initVerify
        sig.update(DATA);
        assertTrue(sig.getAlgorithm(), sig.verify(signature));

        // Calling Signature.verify a second time should not throw
        // http://code.google.com/p/android/issues/detail?id=34933
        sig.verify(signature);
}

private static final byte[] PK_BYTES = hexToBytes(







