/*Add raw RSA Cipher to OpenSSLProvider

Recent changes in the way that Android Keystore (accessed via KeyChain)
necessitate all key operations be done with a provider that understands
the new OpenSSLKey object.

This adds Cipher support for the RSA algorithm in "RSA/ECB/NoPadding"
and "RSA/None/NoPadding" modes.

Change-Id:I98a8eaf3514763a863b2751bba999fbd48609c96*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java
new file mode 100644
//Synthetic comment -- index 0000000..8312013

//Synthetic comment -- @@ -0,0 +1,227 @@








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index efb9387..e13ccda 100644

//Synthetic comment -- @@ -120,5 +120,9 @@
*/
put("SecureRandom.SHA1PRNG", OpenSSLRandom.class.getName());
put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 7919b35..a94a9d0 100644

//Synthetic comment -- @@ -17,9 +17,32 @@
package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;
import java.security.InvalidKeyException;
import java.security.cert.Certificate;
import javax.crypto.Cipher;
import junit.framework.TestCase;
import libcore.java.security.TestKeyStore;

//Synthetic comment -- @@ -108,4 +131,716 @@
.build()
.getPrivateKey("RSA", "RSA").getCertificate();
}
}







