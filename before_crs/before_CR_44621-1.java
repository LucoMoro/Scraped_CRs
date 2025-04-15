/*Adding minimum cryptographic strength check for cert chains.

Change-Id:Id8a3fc28a07c086182183090cd79372ac81582e6*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainAnalyzer.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainAnalyzer.java
new file mode 100644
//Synthetic comment -- index 0000000..f4ea92b

//Synthetic comment -- @@ -0,0 +1,65 @@








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 3f362c5..0eb31e3 100644

//Synthetic comment -- @@ -52,6 +52,11 @@
private final KeyStore rootKeyStore;

/**
* The backing store for the AndroidCAStore if non-null. This will
* be null when the rootKeyStore is null, implying we are not
* using the AndroidCAStore.
//Synthetic comment -- @@ -193,6 +198,10 @@
"Trust anchor for certification path not found.", null, certPath, -1));
}

try {
PKIXParameters params = new PKIXParameters(trustAnchors);
params.setRevocationEnabled(false);







