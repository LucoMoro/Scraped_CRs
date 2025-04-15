/*CTS failure for CertBlackListTest

    The fix is necessasery for all the PubkeyBlack* test cases
    Issue comes when comparing (asserEqual) a string object (key) with
    hex_encode value of same string object.
Signed-off-by: Jatin Majmudar <fjm225@motorola.com>*/
//Synthetic comment -- diff --git a/luni/src/test/java/com/android/org/bouncycastle/jce/provider/CertBlacklistTest.java b/luni/src/test/java/com/android/org/bouncycastle/jce/provider/CertBlacklistTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 85e360e..bd12aea

//Synthetic comment -- @@ -64,7 +64,7 @@
// convert the results to a hashset of strings
Set<String> results = new HashSet<String>();
for (byte[] value: arr) {
            results.add(new String(Hex.encode(value)));
}
return results;
}







