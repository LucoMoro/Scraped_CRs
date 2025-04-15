/*NativeCrypto: serialize EC keys differently

Change-Id:Iff593c707723811347b5b7e91bed52b07c490c9d*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java
//Synthetic comment -- index 508354e..530fdd7 100644

//Synthetic comment -- @@ -149,18 +149,12 @@
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();

        byte[] encoded = (byte[]) stream.readObject();

        key = new OpenSSLKey(NativeCrypto.d2i_PKCS8_PRIV_KEY_INFO(encoded));

        final int origGroup = NativeCrypto.EC_KEY_get0_group(key.getPkeyContext());
        group = new OpenSSLECGroupContext(NativeCrypto.EC_GROUP_dup(origGroup));
}

private void writeObject(ObjectOutputStream stream) throws IOException {
//Synthetic comment -- @@ -169,7 +163,6 @@
}

stream.defaultWriteObject();
        stream.writeObject(getEncoded());
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java
//Synthetic comment -- index 951ee0b..72c7212 100644

//Synthetic comment -- @@ -154,23 +154,12 @@
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();

        byte[] encoded = (byte[]) stream.readObject();

        key = new OpenSSLKey(NativeCrypto.d2i_PUBKEY(encoded));

        final int origGroup = NativeCrypto.EC_KEY_get0_group(key.getPkeyContext());
        group = new OpenSSLECGroupContext(NativeCrypto.EC_GROUP_dup(origGroup));
}

private void writeObject(ObjectOutputStream stream) throws IOException {
//Synthetic comment -- @@ -179,9 +168,6 @@
}

stream.defaultWriteObject();
        stream.writeObject(getEncoded());
}
}







