/*Adding minimum cryptographic strength check for cert chains.

Change-Id:Id8a3fc28a07c086182183090cd79372ac81582e6*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainStrengthAnalyzer.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ChainStrengthAnalyzer.java
new file mode 100644
//Synthetic comment -- index 0000000..29b55a6

//Synthetic comment -- @@ -0,0 +1,59 @@








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/TrustManagerImpl.java
//Synthetic comment -- index 3f362c5..8217fb5 100644

//Synthetic comment -- @@ -193,6 +193,10 @@
"Trust anchor for certification path not found.", null, certPath, -1));
}

try {
PKIXParameters params = new PKIXParameters(trustAnchors);
params.setRevocationEnabled(false);








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/ChainStrengthAnalyzerTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/ChainStrengthAnalyzerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..42585b9

//Synthetic comment -- @@ -0,0 +1,128 @@
\ No newline at end of file







