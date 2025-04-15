/*NativeCrypto: update curve names to match OpenSSL

Some of the curve names were incorrect in the ECKeyPairGenerator, so
renamed them to match what OpenSSL expects.

Change-Id:Ib56fe8ce30b95f7faee34a3e18add7c4037e4c47*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyPairGenerator.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyPairGenerator.java
//Synthetic comment -- index 394b3e1..bea46e6 100644

//Synthetic comment -- @@ -37,11 +37,10 @@
static {
/* NIST curves */
SIZE_TO_CURVE_NAME.put(192, "prime192v1");
        SIZE_TO_CURVE_NAME.put(224, "secp224r1");
SIZE_TO_CURVE_NAME.put(256, "prime256v1");
        SIZE_TO_CURVE_NAME.put(384, "secp384r1");
        SIZE_TO_CURVE_NAME.put(521, "secp521r1");
}

private OpenSSLECGroupContext group;








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java b/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java
//Synthetic comment -- index 93b58df..f582550 100644

//Synthetic comment -- @@ -113,7 +113,11 @@
putKeySize("DiffieHellman", 512);
putKeySize("DiffieHellman", 512+64);
putKeySize("DiffieHellman", 1024);
        putKeySize("EC", 192);
        putKeySize("EC", 224);
putKeySize("EC", 256);
        putKeySize("EC", 384);
        putKeySize("EC", 521);
}

private void test_KeyPairGenerator(KeyPairGenerator kpg) throws Exception {







