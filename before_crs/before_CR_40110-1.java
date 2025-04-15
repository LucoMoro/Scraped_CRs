/*Merge branch 'contacts' into contactssnippet*/
//Synthetic comment -- diff --git a/core/java/android/provider/ContactsContract.java b/core/java/android/provider/ContactsContract.java
//Synthetic comment -- index 8e123ac..dbbdc3b 100644

//Synthetic comment -- @@ -8406,7 +8406,7 @@
sb.append(markedToken);
if (i < lastToken - 1) {
// Add the characters that appeared between this token and the next.
                            sb.append(contentLine.substring(
tokenOffsets.get(i) + originalToken.length(),
tokenOffsets.get(i + 1)));
}







