/*Fix cert blacklisting by public key

Previously, public keys were compared to the blacklisted keys
by a HashSet.compare(), which compares by reference. This replaces
that with the correct Arrays.equals check.

(cherry picked from commit ea82c4ad99e7fa267c4bfa05f6f8312f85ceb8ce)
(cherry picked from commit 806ca813c0327a5d673914883853f55440b39cc7)

Bug:http://code.google.com/p/android/issues/detail?id=35547Change-Id:I5394d2999dd29fabf76e5d4492b0c188b3cd6c51*/




//Synthetic comment -- diff --git a/src/main/java/org/bouncycastle/jce/provider/CertBlacklist.java b/src/main/java/org/bouncycastle/jce/provider/CertBlacklist.java
//Synthetic comment -- index 795fa1a..1dea522 100644

//Synthetic comment -- @@ -144,8 +144,9 @@
String pubkeyBlacklist = readBlacklist(path);
if (!pubkeyBlacklist.equals("")) {
for (String value : pubkeyBlacklist.split(",")) {
                value = value.trim();
if (isPubkeyHash(value)) {
                    bl.add(value.getBytes());
} else {
System.logW("Tried to blacklist invalid pubkey " + value);
}
//Synthetic comment -- @@ -161,7 +162,12 @@
digest.update(encoded, 0, encoded.length);
byte[] out = new byte[digest.getDigestSize()];
digest.doFinal(out, 0);
        for (byte[] blacklisted : pubkeyBlacklist) {
            if (Arrays.equals(blacklisted, Hex.encode(out))) {
                return true;
            }
        }
        return false;
}

public boolean isSerialNumberBlackListed(BigInteger serial) {







