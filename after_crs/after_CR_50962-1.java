/*NativeCrypto: fix up Channel ID test

Previously there was no way to construct an OpenSSLECPrivateKey from a
ECPrivateKeySpec, but that was added in a previous commit. Switch the
Channel ID test to use this instead.

Change-Id:I2483af79641ba95965ea3432a7d415df513b3fd3*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java
//Synthetic comment -- index de73ea1..5434702 100644

//Synthetic comment -- @@ -135,17 +135,16 @@
}

private static synchronized void initChannelIdKey() throws Exception {
        if (CHANNEL_ID_PRIVATE_KEY != null) {
            return;
        }

// NIST P-256 aka SECG secp256r1 aka X9.62 prime256v1
OpenSSLECGroupContext openSslSpec = OpenSSLECGroupContext.getCurveByName("prime256v1");
BigInteger s = new BigInteger(
"229cdbbf489aea584828a261a23f9ff8b0f66f7ccac98bf2096ab3aee41497c5", 16);
CHANNEL_ID_PRIVATE_KEY = new OpenSSLECPrivateKey(
                new ECPrivateKeySpec(s, openSslSpec.getECParameterSpec()));

// Channel ID is the concatenation of the X and Y coordinates of the public key.
CHANNEL_ID = new BigInteger(







