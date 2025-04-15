/*Do better comparisons for ENGINE-based keys

ENGINE-based keys need only be compared by their modulus in actuality,
because given a good random number generator each modulus should be
unique.

Change-Id:Iea1f19126c5ce306d63b3a1bcb05a43139a86846*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateCrtKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateCrtKey.java
//Synthetic comment -- index 8376515..48e1494 100644

//Synthetic comment -- @@ -201,6 +201,16 @@
if (getOpenSSLKey().equals(other.getOpenSSLKey())) {
return true;
}
}

if (o instanceof RSAPrivateCrtKey) {
//Synthetic comment -- @@ -232,11 +242,11 @@
public String toString() {
final StringBuilder sb = new StringBuilder("OpenSSLRSAPrivateCrtKey{");

        if (getOpenSSLKey().isEngineBased()) {
sb.append("key=");
sb.append(getOpenSSLKey());
sb.append('}');
            return sb.toString();
}

ensureReadParams();
//Synthetic comment -- @@ -250,9 +260,11 @@
sb.append(',');
}

        sb.append("privateExponent=");
        sb.append(getPrivateExponent().toString(16));
        sb.append(',');

if (primeP != null) {
sb.append("primeP=");








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java
//Synthetic comment -- index c9fa178..082bebd 100644

//Synthetic comment -- @@ -196,6 +196,15 @@
if (key.equals(other.getOpenSSLKey())) {
return true;
}
}

if (o instanceof RSAPrivateKey) {
//Synthetic comment -- @@ -226,11 +235,11 @@
public String toString() {
final StringBuilder sb = new StringBuilder("OpenSSLRSAPrivateKey{");

        if (key.isEngineBased()) {
sb.append("key=");
sb.append(key);
sb.append('}');
            return sb.toString();
}

ensureReadParams();
//Synthetic comment -- @@ -238,9 +247,11 @@
sb.append(modulus.toString(16));
sb.append(',');

        sb.append("privateExponent=");
        sb.append(privateExponent.toString(16));
        sb.append(',');

return sb.toString();
}







