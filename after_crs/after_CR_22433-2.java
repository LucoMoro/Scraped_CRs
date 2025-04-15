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
            hashed = toHex(sha1, md5);
} catch (NoSuchAlgorithmException e) {
Log.w(TAG, "Failed to encode string because of missing algorithm: " + algo);
}
return hashed;
}

    private static final byte[] HEX_CHARS = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static byte[] toHex(final byte[] array1, final byte[] array2) {
        final byte[] result = new byte[(array1.length + array2.length) * 2];
        int i = 0;
        for (final byte b : array1) {
            result[i++] = HEX_CHARS[(b >> 4) & 0xf];
            result[i++] = HEX_CHARS[b & 0xf];
}
        for (final byte b : array2) {
            result[i++] = HEX_CHARS[(b >> 4) & 0xf];
            result[i++] = HEX_CHARS[b & 0xf];
        }
        return result;
}

/**







