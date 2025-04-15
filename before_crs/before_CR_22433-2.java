/*Performance of toHex() improved.

- Constant array with hex digits instead of re-creation of String.
- Concatenating both values at one go.
- Without necessity of various wrapper objects.

According my benchmark, the new implementation is more than 5 times
faster.

Change-Id:I90cc4a31db4272e245228cb3163846841adf427c*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/LockPatternUtils.java b/core/java/com/android/internal/widget/LockPatternUtils.java
//Synthetic comment -- index c788605..1bcaafe 100644

//Synthetic comment -- @@ -506,21 +506,29 @@
byte[] saltedPassword = (password + getSalt()).getBytes();
byte[] sha1 = MessageDigest.getInstance(algo = "SHA-1").digest(saltedPassword);
byte[] md5 = MessageDigest.getInstance(algo = "MD5").digest(saltedPassword);
            hashed = (toHex(sha1) + toHex(md5)).getBytes();
} catch (NoSuchAlgorithmException e) {
Log.w(TAG, "Failed to encode string because of missing algorithm: " + algo);
}
return hashed;
}

    private static String toHex(byte[] ary) {
        final String hex = "0123456789ABCDEF";
        String ret = "";
        for (int i = 0; i < ary.length; i++) {
            ret += hex.charAt((ary[i] >> 4) & 0xf);
            ret += hex.charAt(ary[i] & 0xf);
}
        return ret;
}

/**







