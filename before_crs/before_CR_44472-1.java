/*Fix non-BC EC private key usage

Change-Id:I3ed2ecf7c52a8264069519cdda4165153018866b*/
//Synthetic comment -- diff --git a/src/main/java/org/bouncycastle/jce/provider/asymmetric/ec/ECUtil.java b/src/main/java/org/bouncycastle/jce/provider/asymmetric/ec/ECUtil.java
//Synthetic comment -- index 088dfad..f3c5928 100644

//Synthetic comment -- @@ -155,7 +155,15 @@
k.getD(),
new ECDomainParameters(s.getCurve(), s.getG(), s.getN(), s.getH(), s.getSeed()));
}
                        
throw new InvalidKeyException("can't identify EC private key.");
}









//Synthetic comment -- diff --git a/src/main/java/org/bouncycastle/jce/provider/asymmetric/ec/Signature.java b/src/main/java/org/bouncycastle/jce/provider/asymmetric/ec/Signature.java
//Synthetic comment -- index 0bb21f8..ecc4fa5 100644

//Synthetic comment -- @@ -6,6 +6,7 @@
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;

import org.bouncycastle.asn1.ASN1Encodable;
//Synthetic comment -- @@ -95,7 +96,21 @@
}
else
{
            throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
}

digest.reset();







