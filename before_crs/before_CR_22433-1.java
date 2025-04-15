/*Performance of toHex() improved.

- Constant string with hex digits is not re-created on each call.
- Result concatenation is done by StringBuilder instead of + operator.

Change-Id:I90cc4a31db4272e245228cb3163846841adf427c*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/LockPatternUtils.java b/core/java/com/android/internal/widget/LockPatternUtils.java
//Synthetic comment -- index c788605..5c1b05c 100644

//Synthetic comment -- @@ -513,14 +513,15 @@
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







