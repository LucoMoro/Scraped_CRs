/*Tracking bouncycastle 1.48 upgrade

Change-Id:I3c43882dda27b5596c6823f5f0711049803ac985*/
//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/TestKeyStore.java b/support/src/test/java/libcore/java/security/TestKeyStore.java
//Synthetic comment -- index e24ee78..74c2840 100644

//Synthetic comment -- @@ -528,8 +528,12 @@
if (!permittedNameConstraints.isEmpty() || !excludedNameConstraints.isEmpty()) {
x509cg.addExtension(X509Extensions.NameConstraints,
true,
                                new NameConstraints(permittedNameConstraints,
                                                    excludedNameConstraints));
}

if (privateKey instanceof ECPrivateKey) {







