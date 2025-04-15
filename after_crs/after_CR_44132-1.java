/*OpenSSLCipher: add Ciphers to StandardNames

Change-Id:Ib990dd170d4c94ceea065748aceeb3bb4a297438*/




//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index f01a9b9..0ad0262 100644

//Synthetic comment -- @@ -434,6 +434,26 @@
provide("SecretKeyFactory", "PBEWITHSHAAND40BITRC4");
provide("SecretKeyFactory", "PBEWITHSHAANDTWOFISH-CBC");

            // Needed by our OpenSSL provider
            provide("Cipher", "AES/CBC/NOPADDING");
            provide("Cipher", "AES/CBC/PKCS5PADDING");
            provide("Cipher", "AES/CFB/NOPADDING");
            provide("Cipher", "AES/CFB/PKCS5PADDING");
            provide("Cipher", "AES/CTR/NOPADDING");
            provide("Cipher", "AES/CTR/PKCS5PADDING");
            provide("Cipher", "AES/ECB/NOPADDING");
            provide("Cipher", "AES/ECB/PKCS5PADDING");
            provide("Cipher", "AES/OFB/NOPADDING");
            provide("Cipher", "AES/OFB/PKCS5PADDING");
            provide("Cipher", "DESEDE/CBC/NOPADDING");
            provide("Cipher", "DESEDE/CBC/PKCS5PADDING");
            provide("Cipher", "DESEDE/CFB/NOPADDING");
            provide("Cipher", "DESEDE/CFB/PKCS5PADDING");
            provide("Cipher", "DESEDE/ECB/NOPADDING");
            provide("Cipher", "DESEDE/ECB/PKCS5PADDING");
            provide("Cipher", "DESEDE/OFB/NOPADDING");
            provide("Cipher", "DESEDE/OFB/PKCS5PADDING");

// removed LDAP
unprovide("CertStore", "LDAP");








