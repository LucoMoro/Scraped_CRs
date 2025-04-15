/*Track keystore binder changes

Change-Id:Id6133be059a8a0901d16355a9152e40e4a255454*/
//Synthetic comment -- diff --git a/core/java/android/security/IKeystoreService.java b/core/java/android/security/IKeystoreService.java
//Synthetic comment -- index f8a49e6..651693a 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
return _result;
}

            public int insert(String name, byte[] item) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
//Synthetic comment -- @@ -86,6 +86,7 @@
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeByteArray(item);
mRemote.transact(Stub.TRANSACTION_insert, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -96,13 +97,14 @@
return _result;
}

            public int del(String name) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_del, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -113,13 +115,14 @@
return _result;
}

            public int exist(String name) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_exist, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -130,13 +133,14 @@
return _result;
}

            public String[] saw(String name) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_saw, _data, _reply, 0);
_reply.readException();
int size = _reply.readInt();
//Synthetic comment -- @@ -235,13 +239,14 @@
return _result;
}

            public int generate(String name) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_generate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -252,7 +257,7 @@
return _result;
}

            public int import_key(String name, byte[] data) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
//Synthetic comment -- @@ -260,6 +265,7 @@
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeByteArray(data);
mRemote.transact(Stub.TRANSACTION_import, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -324,13 +330,14 @@
return _result;
}

            public int del_key(String name) throws RemoteException {
Parcel _data = Parcel.obtain();
Parcel _reply = Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_del_key, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
//Synthetic comment -- @@ -467,13 +474,13 @@

public byte[] get(String name) throws RemoteException;

    public int insert(String name, byte[] item) throws RemoteException;

    public int del(String name) throws RemoteException;

    public int exist(String name) throws RemoteException;

    public String[] saw(String name) throws RemoteException;

public int reset() throws RemoteException;

//Synthetic comment -- @@ -485,9 +492,9 @@

public int zero() throws RemoteException;

    public int generate(String name) throws RemoteException;

    public int import_key(String name, byte[] data) throws RemoteException;

public byte[] sign(String name, byte[] data) throws RemoteException;

//Synthetic comment -- @@ -495,7 +502,7 @@

public byte[] get_pubkey(String name) throws RemoteException;

    public int del_key(String name) throws RemoteException;

public int grant(String name, int granteeUid) throws RemoteException;









//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index ceaff37..9dd2b0d 100644

//Synthetic comment -- @@ -85,7 +85,7 @@

public boolean put(String key, byte[] value) {
try {
            return mBinder.insert(key, value) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -94,7 +94,7 @@

public boolean delete(String key) {
try {
            return mBinder.del(key) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -103,7 +103,7 @@

public boolean contains(String key) {
try {
            return mBinder.exist(key) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -112,7 +112,7 @@

public String[] saw(String prefix) {
try {
            return mBinder.saw(prefix);
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return null;
//Synthetic comment -- @@ -167,7 +167,7 @@

public boolean generate(String key) {
try {
            return mBinder.generate(key) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -176,7 +176,7 @@

public boolean importKey(String keyName, byte[] key) {
try {
            return mBinder.import_key(keyName, key) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;
//Synthetic comment -- @@ -194,7 +194,7 @@

public boolean delKey(String key) {
try {
            return mBinder.del_key(key) == NO_ERROR;
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return false;







