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
 *
 * <p>Since a {@code SealedObject} instance is serializable it can
* either be stored or transmitted over an insecure channel.
 *
 * <p>The wrapped object can later be decrypted (unsealed) using the corresponding
 * key and then be deserialized to retrieve the original object. The sealed
* object itself keeps track of the cipher and corresponding parameters.
*/
public class SealedObject implements Serializable {
//Synthetic comment -- @@ -46,19 +46,25 @@
private static final long serialVersionUID = 4482838265551344752L;

/**
     * The cipher's {@link AlgorithmParameters} in encoded format.
     * Equivalent to {@code cipher.getParameters().getEncoded()},
     * or null if the cipher did not use any parameters.
*/
protected byte[] encodedParams;

private byte[] encryptedContent;
private String sealAlg;
private String paramsAlg;

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        // We do unshared reads here to ensure we have our own clones of the byte[]s.
        encodedParams = (byte[]) s.readUnshared();
        encryptedContent = (byte[]) s.readUnshared();
        // These are regular shared reads because the algorithms used by a given stream are
        // almost certain to the be same for each object, and String is immutable anyway,
        // so there's no security concern about sharing.
        sealAlg = (String) s.readObject();
        paramsAlg = (String) s.readObject();
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/util/SerializationTester.java b/luni/src/test/java/libcore/util/SerializationTester.java
new file mode 100644
//Synthetic comment -- index 0000000..59f319c

//Synthetic comment -- @@ -0,0 +1,125 @@
/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libcore.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import junit.framework.AssertionFailedError;

public class SerializationTester<T> {
    private final String golden;
    private final T value;

    public SerializationTester(T value, String golden) {
        this.golden = golden;
        this.value = value;
    }

    /**
     * Returns true if {@code a} and {@code b} are equal. Override this if
     * {@link Object#equals} isn't appropriate or sufficient for this tester's
     * value type.
     */
    protected boolean equals(T a, T b) {
        return a.equals(b);
    }

    /**
     * Verifies that {@code deserialized} is valid. Implementations of this
     * method may mutate {@code deserialized}.
     */
    protected void verify(T deserialized) throws Exception {}

    public void test() {
        try {
            if (golden == null || golden.length() == 0) {
                fail("No golden value supplied! Consider using this: "
                        + hexEncode(serialize(value)));
            }

            @SuppressWarnings("unchecked") // deserialize should return the proper type
            T deserialized = (T) deserialize(hexDecode(golden));
            assertTrue("User-constructed value doesn't equal deserialized golden value",
                    equals(value, deserialized));

            @SuppressWarnings("unchecked") // deserialize should return the proper type
            T reserialized = (T) deserialize(serialize(value));
            assertTrue("User-constructed value doesn't equal itself, reserialized",
                    equals(value, reserialized));

            // just a sanity check! if this fails, verify() is probably broken
            verify(value);
            verify(deserialized);
            verify(reserialized);

        } catch (Exception e) {
            Error failure = new AssertionFailedError();
            failure.initCause(e);
            throw failure;
        }
    }

    private static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(object);
        return out.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object result = in.readObject();
        assertEquals(-1, in.read());
        return result;
    }

    private static String hexEncode(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static byte[] hexDecode(String s) {
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(s.substring(i*2, i*2 + 2), 16);
        }
        return result;
    }

    /**
     * Returns a serialized-and-deserialized copy of {@code object}.
     */
    public static Object reserialize(Object object) throws IOException, ClassNotFoundException {
        return deserialize(serialize(object));
    }

    public static String serializeHex(Object object) throws IOException {
        return hexEncode(serialize(object));
    }

    public static Object deserializeHex(String hex) throws IOException, ClassNotFoundException {
        return deserialize(hexDecode(hex));
    }
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/SealedObjectTest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/SealedObjectTest.java
//Synthetic comment -- index b3b2931..3ea57bf 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
//Synthetic comment -- @@ -43,6 +44,8 @@
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import libcore.util.SerializationTester;

/**
*/
public class SealedObjectTest extends TestCase {
//Synthetic comment -- @@ -291,4 +294,23 @@
}
}

    // http://code.google.com/p/android/issues/detail?id=4834
    public void testDeserialization() throws Exception {
        // (Boilerplate so we can create SealedObject instances.)
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        Key key = kg.generateKey();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Incorrect use of readUnshared meant you couldn't have two SealedObjects
        // with the same algorithm or parameters algorithm...
        ArrayList<SealedObject> sealedObjects = new ArrayList<SealedObject>();
        for (int i = 0; i < 10; ++i) {
            sealedObjects.add(new SealedObject("hello", cipher));
        }
        String serializedForm = SerializationTester.serializeHex(sealedObjects);

        // ...so this would throw "java.io.InvalidObjectException: Unshared read of back reference".
        SerializationTester.deserializeHex(serializedForm);
    }
}







