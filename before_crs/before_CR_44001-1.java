/*OpenSSLCipher: Add DESede support

Change-Id:I81f1bec8e3562c3ed90b35a60829ca0dfc4d8341*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index c65cbdc..ce50553 100644

//Synthetic comment -- @@ -687,4 +687,131 @@
return AES_BLOCK_SIZE;
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index 4a2f9b7..7e56b96 100644

//Synthetic comment -- @@ -140,5 +140,14 @@
put("Cipher.AES/CTR/PKCS5Padding", OpenSSLCipher.AES.CTR.PKCS5Padding.class.getName());
put("Cipher.AES/OFB/NoPadding", OpenSSLCipher.AES.OFB.NoPadding.class.getName());
put("Cipher.AES/OFB/PKCS5Padding", OpenSSLCipher.AES.OFB.PKCS5Padding.class.getName());
}
}







