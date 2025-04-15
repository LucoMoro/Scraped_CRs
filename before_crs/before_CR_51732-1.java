/*KeyStore: add API to uid versions

Change-Id:I32098dc0eb42e09ace89f6b7455766842a72e9f4*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index 9dd2b0d..3635c4d 100644

//Synthetic comment -- @@ -83,9 +83,22 @@
}
}

    public boolean put(String key, byte[] value) {
try {
            return mBinder.insert(key, value, -1) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -93,8 +106,12 @@
}

public boolean delete(String key) {
try {
            return mBinder.del(key, -1) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -102,23 +119,22 @@
}

public boolean contains(String key) {
        try {
            return mBinder.exist(key, -1) == NO_ERROR;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
}

    public String[] saw(String prefix) {
try {
            return mBinder.saw(prefix, -1);
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return null;
}
}

public boolean reset() {
try {
return mBinder.reset() == NO_ERROR;
//Synthetic comment -- @@ -165,9 +181,22 @@
}
}

    public boolean generate(String key) {
try {
            return mBinder.generate(key, -1) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -175,12 +204,7 @@
}

public boolean importKey(String keyName, byte[] key) {
        try {
            return mBinder.import_key(keyName, key, -1) == NO_ERROR;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
}

public byte[] getPubkey(String key) {
//Synthetic comment -- @@ -192,15 +216,19 @@
}
}

    public boolean delKey(String key) {
try {
            return mBinder.del_key(key, -1) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
}
}

public byte[] sign(String key, byte[] data) {
try {
return mBinder.sign(key, data);







