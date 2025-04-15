/*KeyStore: add API to uid versions

Change-Id:I32098dc0eb42e09ace89f6b7455766842a72e9f4*/




//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index 9dd2b0d..3635c4d 100644

//Synthetic comment -- @@ -83,9 +83,22 @@
}
}

    public boolean put(String key, byte[] value, int uid) {
try {
            return mBinder.insert(key, value, uid) == NO_ERROR;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean put(String key, byte[] value) {
        return put(key, value, -1);
    }

    public boolean delete(String key, int uid) {
        try {
            return mBinder.del(key, uid) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -93,8 +106,12 @@
}

public boolean delete(String key) {
        return delete(key, -1);
    }

    public boolean contains(String key, int uid) {
try {
            return mBinder.exist(key, uid) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -102,23 +119,22 @@
}

public boolean contains(String key) {
        return contains(key, -1);
}

    public String[] saw(String prefix, int uid) {
try {
            return mBinder.saw(prefix, uid);
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return null;
}
}

    public String[] saw(String prefix) {
        return saw(prefix, -1);
    }

public boolean reset() {
try {
return mBinder.reset() == NO_ERROR;
//Synthetic comment -- @@ -165,9 +181,22 @@
}
}

    public boolean generate(String key, int uid) {
try {
            return mBinder.generate(key, uid) == NO_ERROR;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean generate(String key) {
        return generate(key, -1);
    }

    public boolean importKey(String keyName, byte[] key, int uid) {
        try {
            return mBinder.import_key(keyName, key, uid) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -175,12 +204,7 @@
}

public boolean importKey(String keyName, byte[] key) {
        return importKey(keyName, key, -1);
}

public byte[] getPubkey(String key) {
//Synthetic comment -- @@ -192,15 +216,19 @@
}
}

    public boolean delKey(String key, int uid) {
try {
            return mBinder.del_key(key, uid) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
}
}

    public boolean delKey(String key) {
        return delKey(key, -1);
    }

public byte[] sign(String key, byte[] data) {
try {
return mBinder.sign(key, data);







