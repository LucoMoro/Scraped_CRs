/*Remove provider specified from JarVerifier

Change-Id:I137455e38c20144165c1981076dcbaa26f5bb4a2*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java b/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java
//Synthetic comment -- index f6efb8a..0f95484 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
if (da != null && dea != null) {
alg = da + "with" +  dea;
try {
                sig = Signature.getInstance(alg, OpenSSLProvider.PROVIDER_NAME);
} catch (NoSuchAlgorithmException e) {}
}
if (sig == null) {
//Synthetic comment -- @@ -130,7 +130,7 @@
return null;
}
try {
                sig = Signature.getInstance(alg, OpenSSLProvider.PROVIDER_NAME);
} catch (NoSuchAlgorithmException e) {
return null;
}







