/*Remember key aliases for OpenSSLKeys from ENGINEs

Since it's not easy (or sometimes impossible) to retrieve key IDs for
keys loaded from an ENGINE, remember them when we create them.

(cherry-picked from 86bdaf9b40263efae243d685d449e1ae30b0b161)

Change-Id:I3920f56214d9eade87d51d30b024f5aeda9e8344*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPrivateKey.java
//Synthetic comment -- index 7cd16f7..761b08e 100644

//Synthetic comment -- @@ -120,6 +120,10 @@
return key.getPkeyContext();
}

    public String getPkeyAlias() {
        return key.getAlias();
    }

@Override
public boolean equals(Object o) {
if (o == this) {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLEngine.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLEngine.java
//Synthetic comment -- index 83f0ff8..d01dc62 100644

//Synthetic comment -- @@ -69,9 +69,9 @@
final int keyType = NativeCrypto.EVP_PKEY_type(keyRef);
switch (keyType) {
case NativeCrypto.EVP_PKEY_RSA:
                return OpenSSLRSAPrivateKey.getInstance(new OpenSSLKey(keyRef, this, id));
case NativeCrypto.EVP_PKEY_DSA:
                return new OpenSSLDSAPrivateKey(new OpenSSLKey(keyRef, this, id));
default:
throw new InvalidKeyException("Unknown key type: " + keyType);
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLKey.java
//Synthetic comment -- index 90eb0e2..b8b9f69 100644

//Synthetic comment -- @@ -21,14 +21,18 @@

private final OpenSSLEngine engine;

    private final String alias;

OpenSSLKey(int ctx) {
this.ctx = ctx;
engine = null;
        alias = null;
}

    OpenSSLKey(int ctx, OpenSSLEngine engine, String alias) {
this.ctx = ctx;
this.engine = engine;
        this.alias = alias;
}

int getPkeyContext() {
//Synthetic comment -- @@ -43,6 +47,10 @@
return engine != null;
}

    String getAlias() {
        return alias;
    }

@Override
protected void finalize() throws Throwable {
try {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java
//Synthetic comment -- index 082bebd..5c2f075 100644

//Synthetic comment -- @@ -180,6 +180,10 @@
return key.getPkeyContext();
}

    public String getPkeyAlias() {
        return key.getAlias();
    }

@Override
public boolean equals(Object o) {
if (o == this) {







