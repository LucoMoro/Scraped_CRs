/*Add SignatureTest case for OpenSSL ENGINE

Change-Id:Ifd43cac2a4430c4c34c743291fd653ad1d782057*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/SignatureTest.java b/luni/src/test/java/libcore/java/security/SignatureTest.java
//Synthetic comment -- index d7cb596..ac8b172 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package libcore.java.security;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
//Synthetic comment -- @@ -27,6 +31,7 @@
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
//Synthetic comment -- @@ -38,6 +43,7 @@
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;

public class SignatureTest extends TestCase {
//Synthetic comment -- @@ -80,6 +86,78 @@
}
}

private final Map<String, KeyPair> keypairAlgorithmToInstance
= new HashMap<String, KeyPair>();








