/*Make test_SSLSocket_getSupportedCipherSuites_connect continue to next supported cipher suite on exception

Change-Id:I251c96bd033fa14a01880df20eba7f7626bfccb3*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 342c92f..0aa254d 100644

//Synthetic comment -- @@ -130,6 +130,7 @@
cipherSuites = cs.toArray(new String[cs.size()]);
}

for (String cipherSuite : cipherSuites) {
boolean errorExpected = StandardNames.IS_RI && cipherSuite.endsWith("_SHA256");
try {
//Synthetic comment -- @@ -182,13 +183,20 @@
assertFalse(errorExpected);
} catch (Exception maybeExpected) {
if (!errorExpected) {
                    throw new Exception("Problem trying to connect cipher suite " + cipherSuite
                                        + " client=" + clientProvider
                                        + " server=" + serverProvider,
                                        maybeExpected);
}
}
}
c.close();
}








