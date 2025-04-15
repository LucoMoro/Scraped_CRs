/*according to rfc2617, nc-value = 8LHEX

Change-Id:I8646fd8d68cfa4786ee6fc24a757b2af93955c4b*/
//Synthetic comment -- diff --git a/core/java/android/net/http/RequestHandle.java b/core/java/android/net/http/RequestHandle.java
//Synthetic comment -- index 103fd94..2c48a04 100644

//Synthetic comment -- @@ -308,7 +308,7 @@
String A2 = mMethod  + ":" + mUrl;

// because we do not preemptively send authorization headers, nc is always 1
        String nc = "000001";
String cnonce = computeCnonce();
String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);








