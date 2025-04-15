/*NativeCrypto: serialize EC keys differently

Change-Id:Iff593c707723811347b5b7e91bed52b07c490c9d*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java
//Synthetic comment -- index 508354e..530fdd7 100644

//Synthetic comment -- @@ -149,18 +149,12 @@
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();

        final ECParameterSpec params = (ECParameterSpec) stream.readObject();
        final BigInteger privkey = (BigInteger) stream.readObject();

        final OpenSSLECGroupContext group;
        try {
            group = OpenSSLECGroupContext.getInstance(params);
        } catch (InvalidAlgorithmParameterException e) {
            throw new ClassNotFoundException("cannot restore field type", e);
        }

        key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(group.getContext(), 0,
                privkey.toByteArray()));
}

private void writeObject(ObjectOutputStream stream) throws IOException {
//Synthetic comment -- @@ -169,7 +163,6 @@
}

stream.defaultWriteObject();
        stream.writeObject(getParams());
        stream.writeObject(getPrivateKey());
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java
//Synthetic comment -- index 951ee0b..72c7212 100644

//Synthetic comment -- @@ -154,23 +154,12 @@
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();

        final ECParameterSpec params = (ECParameterSpec) stream.readObject();
        final BigInteger pubkeyX = (BigInteger) stream.readObject();
        final BigInteger pubkeyY = (BigInteger) stream.readObject();

        final OpenSSLECGroupContext group;
        try {
            group = OpenSSLECGroupContext.getInstance(params);
        } catch (InvalidAlgorithmParameterException e) {
            throw new ClassNotFoundException("cannot restore field type", e);
        }

        final int curveType = NativeCrypto.get_EC_GROUP_type(group.getContext());
        final ECPoint javaPubKey = new ECPoint(pubkeyX, pubkeyY);
        final OpenSSLECPointContext pubKey = OpenSSLECPointContext.getInstance(curveType, group,
                javaPubKey);
        key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(group.getContext(),
                pubKey.getContext(), null));
}

private void writeObject(ObjectOutputStream stream) throws IOException {
//Synthetic comment -- @@ -179,9 +168,6 @@
}

stream.defaultWriteObject();
        stream.writeObject(getParams());
        final ECPoint pubKey = getPublicKey();
        stream.writeObject(pubKey.getAffineX());
        stream.writeObject(pubKey.getAffineY());
}
}







