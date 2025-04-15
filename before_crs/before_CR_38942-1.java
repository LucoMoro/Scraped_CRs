/*Fix SealedObject.readObject.

Using readUnshared on the byte[]s seems like a reasonable security precaution.
Using readUnshared on the algorithm Strings is just plain wrong.

Bug:http://code.google.com/p/android/issues/detail?id=4834Change-Id:I73d32a14521de62ce9e19871fd30b619cf3ff6eb*/
//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/SealedObject.java b/luni/src/main/java/javax/crypto/SealedObject.java
//Synthetic comment -- index c9c1534..cfb970b 100644

//Synthetic comment -- @@ -33,12 +33,12 @@
/**
* A {@code SealedObject} is a wrapper around a {@code serializable} object
* instance and encrypts it using a cryptographic cipher.
 * <p>
 * Since a {@code SealedObject} instance is a serializable object itself it can
* either be stored or transmitted over an insecure channel.
 * <p>
 * The wrapped object can later be decrypted (unsealed) using the corresponding
 * key and then be deserialized to retrieve the original object.The sealed
* object itself keeps track of the cipher and corresponding parameters.
*/
public class SealedObject implements Serializable {
//Synthetic comment -- @@ -46,19 +46,25 @@
private static final long serialVersionUID = 4482838265551344752L;

/**
     * The {@link AlgorithmParameters} in encoded format.
*/
protected byte[] encodedParams;
private byte[] encryptedContent;
private String sealAlg;
private String paramsAlg;

    private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
        encodedParams = (byte []) s.readUnshared();
        encryptedContent = (byte []) s.readUnshared();
        sealAlg = (String) s.readUnshared();
        paramsAlg = (String) s.readUnshared();
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/util/SerializationTester.java b/luni/src/test/java/libcore/util/SerializationTester.java
new file mode 100644
//Synthetic comment -- index 0000000..59f319c

//Synthetic comment -- @@ -0,0 +1,125 @@








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/SealedObjectTest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/SealedObjectTest.java
//Synthetic comment -- index b3b2931..3ea57bf 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.Cipher;
//Synthetic comment -- @@ -43,6 +44,8 @@
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
*/
public class SealedObjectTest extends TestCase {
//Synthetic comment -- @@ -291,4 +294,23 @@
}
}

}







