/*Remove SSLContext TLSv1.1 and TLSv1.2 from expected list for RI

Change-Id:Ie506337882a878df77073c0c8117dfdc8d33b670*/
//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index e8b29e4..6539e2c 100644

//Synthetic comment -- @@ -274,6 +274,12 @@
provide("Signature", "SHA512WITHECDSA");
}

// Fixups for dalvik
if (!IS_RI) {








