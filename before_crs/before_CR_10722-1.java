/*Set parity in generated DES secrets

DES is 8 bytes with the LSB used as a parity bit leaving 56-bits of usable data.
The Bouncy Castle implementation fails to set the parity bits when generating a
DES secret key. Sun's JVM implementation will give you different results than
anything using Bouncy Castle.

This change adds in parity calculation for DES keys. DESede keys still need parity
in 3 different groupings.*/
//Synthetic comment -- diff --git a/libcore/security/src/main/java/org/bouncycastle/jce/provider/JCESecretKeyFactory.java b/libcore/security/src/main/java/org/bouncycastle/jce/provider/JCESecretKeyFactory.java
//Synthetic comment -- index 0a45b74..1f3b75f 100644

//Synthetic comment -- @@ -245,7 +245,9 @@
if (keySpec instanceof DESKeySpec)
{
DESKeySpec desKeySpec = (DESKeySpec)keySpec;
                return new SecretKeySpec(desKeySpec.getKey(), "DES");
}

return super.engineGenerateSecret(keySpec);







