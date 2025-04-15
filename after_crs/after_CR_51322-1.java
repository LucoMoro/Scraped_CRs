/*Update KeyPairGeneratorTest for EC keys

EC keys can have two different formats: explicit parameters or named
curve. When you generate a key, it's typically a named curve. When you
write out the parameters of the named curve and read them back, it's no
longer named.

Change the comparison to match the EC parameters themselves instead of
concerning the test with named curve stuff.

Change-Id:I4fc2fe38cf497b2ce96d1258c54833c717152ad2*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java b/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java
//Synthetic comment -- index e8bd1aa..7e17dd1 100644

//Synthetic comment -- @@ -34,8 +34,10 @@
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
//Synthetic comment -- @@ -207,8 +209,19 @@
PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
KeyFactory kf = KeyFactory.getInstance(k.getAlgorithm(), p);
PrivateKey privKey = kf.generatePrivate(spec);
                    assertNotNull(k.getAlgorithm() + ", provider=" + p.getName(), privKey);

                    /*
                     * EC keys are unique because they can have explicit parameters or a curve
                     * name. Check them specially so this test can continue to function.
                     */
                    if (k instanceof ECPrivateKey) {
                        assertECPrivateKeyEquals((ECPrivateKey) k, (ECPrivateKey) privKey);
                    } else {
                        assertEquals(k.getAlgorithm() + ", provider=" + p.getName(),
                                Arrays.toString(encoded),
                                Arrays.toString(privKey.getEncoded()));
                    }
} else if ("X.509".equals(k.getFormat())) {
X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
KeyFactory kf = KeyFactory.getInstance(k.getAlgorithm(), p);
//Synthetic comment -- @@ -220,6 +233,18 @@
}
}

    private static void assertECPrivateKeyEquals(ECPrivateKey expected, ECPrivateKey actual) {
        assertEquals(expected.getS(), actual.getS());
        assertECParametersEquals(expected.getParams(), actual.getParams());
    }

    private static void assertECParametersEquals(ECParameterSpec expected, ECParameterSpec actual) {
        assertEquals(expected.getCurve(), actual.getCurve());
        assertEquals(expected.getGenerator(), actual.getGenerator());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCofactor(), actual.getCofactor());
    }

/**
* DH parameters pre-generated so that the test doesn't take too long.
* These parameters were generated with:







