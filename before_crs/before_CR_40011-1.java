/*Tests for the CertBlacklist.

(cherry picked from commit d1725822187cb9bbe4d93fe39135b17ecb3130ee)
(cherry picked from commit f9ace732615171a9852c86ad4bcb7cf7616c1052)
Bug:http://code.google.com/p/android/issues/detail?id=35547Change-Id:I68cf95f1b8cd17df2236b49396edf4718026d5df*/
//Synthetic comment -- diff --git a/luni/src/test/java/com/android/org/bouncycastle/jce/provider/CertBlacklistTest.java b/luni/src/test/java/com/android/org/bouncycastle/jce/provider/CertBlacklistTest.java
//Synthetic comment -- index 85e360e..bd12aea 100644

//Synthetic comment -- @@ -64,7 +64,7 @@
// convert the results to a hashset of strings
Set<String> results = new HashSet<String>();
for (byte[] value: arr) {
            results.add(new String(Hex.encode(value)));
}
return results;
}







