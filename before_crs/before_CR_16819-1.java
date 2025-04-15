/*Keep full pinyin of the last character of name instead of initial

Change-Id:I8d4cefb50edb7ebb98c202b82f02f72e26843f8c*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactLocaleUtils.java b/src/com/android/providers/contacts/ContactLocaleUtils.java
//Synthetic comment -- index 1c2ad97..a817724 100644

//Synthetic comment -- @@ -102,7 +102,13 @@
final Token token = tokens.get(i);
if (Token.PINYIN == token.type) {
keyPinyin.insert(0, token.target);
                    keyInitial.insert(0, token.target.charAt(0));
} else if (Token.LATIN == token.type) {
// Avoid adding space at the end of String.
if (keyPinyin.length() > 0) {
//Synthetic comment -- @@ -112,7 +118,13 @@
keyOrignal.insert(0, ' ');
}
keyPinyin.insert(0, token.source);
                    keyInitial.insert(0, token.source.charAt(0));
}
keyOrignal.insert(0, token.source);
keys.add(keyOrignal.toString());







