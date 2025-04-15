/*To prevent the reference to null pointer

It is possible that the referencing to "key.toByteArray()" is occured when "key" is null.
To prevent the referencing to null pointer, check if the key is null or not.

Change-Id:I779eb388c9a2f5cd80013a7455cc9b68d153a54d*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/NameNormalizer.java b/src/com/android/providers/contacts/NameNormalizer.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6dfe8bd..6c46175

//Synthetic comment -- @@ -48,7 +48,11 @@
*/
public static String normalize(String name) {
CollationKey key = sCompressingCollator.getCollationKey(lettersAndDigitsOnly(name));
        return Hex.encodeHex(key.toByteArray(), true);
}

/**







