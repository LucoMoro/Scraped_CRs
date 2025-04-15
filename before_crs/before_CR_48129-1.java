/*Temporarily disable 256MB input in MessageDigestTest

libcore.java.security.MessageDigestTest#test_getInstance could not be
finished on x86/x86_64 devices before timeout. So temporarily disable
it.

Change-Id:If7badbf8a22856c2bd59b35bee7b9133d46dd768*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/MessageDigestTest.java b/luni/src/test/java/libcore/java/security/MessageDigestTest.java
//Synthetic comment -- index cd7b8db..18add39 100644

//Synthetic comment -- @@ -144,8 +144,13 @@
// Regression test input for problem SHA-1 with input of
// 256MBs.  However, its currently too slow to be practical on
// devices, so its disabled in that case. http://b/4501620
        boolean enabled256mb = (System.getProperty("os.arch").contains("x86") // host
                                || System.getProperty("os.arch").contains("amd64")); // RI
if (enabled256mb) {
// INPUT_256MB
putExpectation("MD2",







