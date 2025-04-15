/*Change KeyStore to use Modified UTF-8 to match NativeCrypto

Bug:http://code.google.com/p/android/issues/detail?id=35141Bug: 6869713

Change-Id:I61cb309786960072148ef97ea5afedb33dc45f4e*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index a32e469..f49c429 100644

//Synthetic comment -- @@ -22,8 +22,9 @@
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charsets;
import java.util.ArrayList;

/**
//Synthetic comment -- @@ -75,7 +76,7 @@
}

public byte[] get(String key) {
        return get(getBytes(key));
}

private boolean put(byte[] key, byte[] value) {
//Synthetic comment -- @@ -84,7 +85,7 @@
}

public boolean put(String key, byte[] value) {
        return put(getBytes(key), value);
}

private boolean delete(byte[] key) {
//Synthetic comment -- @@ -93,7 +94,7 @@
}

public boolean delete(String key) {
        return delete(getBytes(key));
}

private boolean contains(byte[] key) {
//Synthetic comment -- @@ -102,7 +103,7 @@
}

public boolean contains(String key) {
        return contains(getBytes(key));
}

public byte[][] saw(byte[] prefix) {
//Synthetic comment -- @@ -111,13 +112,13 @@
}

public String[] saw(String prefix) {
        byte[][] values = saw(getBytes(prefix));
if (values == null) {
return null;
}
String[] strings = new String[values.length];
for (int i = 0; i < values.length; ++i) {
            strings[i] = toString(values[i]);
}
return strings;
}
//Synthetic comment -- @@ -133,7 +134,7 @@
}

public boolean password(String password) {
        return password(getBytes(password));
}

public boolean lock() {
//Synthetic comment -- @@ -147,7 +148,7 @@
}

public boolean unlock(String password) {
        return unlock(getBytes(password));
}

public boolean isEmpty() {
//Synthetic comment -- @@ -161,7 +162,7 @@
}

public boolean generate(String key) {
        return generate(getBytes(key));
}

private boolean importKey(byte[] keyName, byte[] key) {
//Synthetic comment -- @@ -170,7 +171,7 @@
}

public boolean importKey(String keyName, byte[] key) {
        return importKey(getBytes(keyName), key);
}

private byte[] getPubkey(byte[] key) {
//Synthetic comment -- @@ -179,7 +180,7 @@
}

public byte[] getPubkey(String key) {
        return getPubkey(getBytes(key));
}

private boolean delKey(byte[] key) {
//Synthetic comment -- @@ -188,7 +189,7 @@
}

public boolean delKey(String key) {
        return delKey(getBytes(key));
}

private byte[] sign(byte[] keyName, byte[] data) {
//Synthetic comment -- @@ -197,7 +198,7 @@
}

public byte[] sign(String key, byte[] data) {
        return sign(getBytes(key), data);
}

private boolean verify(byte[] keyName, byte[] data, byte[] signature) {
//Synthetic comment -- @@ -206,7 +207,7 @@
}

public boolean verify(String key, byte[] data, byte[] signature) {
        return verify(getBytes(key), data, signature);
}

private boolean grant(byte[] key, byte[] uid) {
//Synthetic comment -- @@ -215,7 +216,7 @@
}

public boolean grant(String key, int uid) {
        return grant(getBytes(key), Integer.toString(uid).getBytes());
}

private boolean ungrant(byte[] key, byte[] uid) {
//Synthetic comment -- @@ -224,7 +225,7 @@
}

public boolean ungrant(String key, int uid) {
        return ungrant(getBytes(key), Integer.toString(uid).getBytes());
}

public int getLastError() {
//Synthetic comment -- @@ -291,11 +292,34 @@
return null;
}

    private static byte[] getBytes(String string) {
        return string.getBytes(Charsets.UTF_8);
}

    private static String toString(byte[] bytes) {
        return new String(bytes, Charsets.UTF_8);
}
}








//Synthetic comment -- diff --git a/keystore/tests/src/android/security/KeyStoreTest.java b/keystore/tests/src/android/security/KeyStoreTest.java
//Synthetic comment -- index 91c56d6..32cd6e2 100755

//Synthetic comment -- @@ -37,7 +37,7 @@
private static final String TEST_PASSWD2 = "87654321";
private static final String TEST_KEYNAME = "test-key";
private static final String TEST_KEYNAME1 = "test-key.1";
    private static final String TEST_KEYNAME2 = "test-key.2";
private static final byte[] TEST_KEYVALUE = "test value".getBytes(Charsets.UTF_8);

// "Hello, World" in Chinese







