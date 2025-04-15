/*OpenSSLCipher: add Ciphers to StandardNames

Change-Id:Ib990dd170d4c94ceea065748aceeb3bb4a297438*/
//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index f01a9b9..0ad0262 100644

//Synthetic comment -- @@ -434,6 +434,26 @@
provide("SecretKeyFactory", "PBEWITHSHAAND40BITRC4");
provide("SecretKeyFactory", "PBEWITHSHAANDTWOFISH-CBC");

// removed LDAP
unprovide("CertStore", "LDAP");








