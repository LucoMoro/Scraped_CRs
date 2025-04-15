/*Keep full pinyin of the last character of Chinese name instead of initial

One pinyin initial may match many contacts.
E.g. looking up BT can get Bai Tao, Bai Ting, Bi Teng, Bao Tou and etc.
So use all characters of the last token to narrow the lookup results.
That means BTao for Bai Tao, BTing for Bai Ting, BTeng for Bi Teng and so on.

Change-Id:I8d4cefb50edb7ebb98c202b82f02f72e26843f8c*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactLocaleUtils.java b/src/com/android/providers/contacts/ContactLocaleUtils.java
//Synthetic comment -- index 1c2ad97..6802da6 100644

//Synthetic comment -- @@ -100,9 +100,9 @@
final StringBuilder keyOrignal = new StringBuilder();
for (int i = tokenCount - 1; i >= 0; i--) {
final Token token = tokens.get(i);
if (Token.PINYIN == token.type) {
                    keyPinyin.insert(0, token.target);
                    keyInitial.insert(0, token.target.charAt(0));
} else if (Token.LATIN == token.type) {
// Avoid adding space at the end of String.
if (keyPinyin.length() > 0) {
//Synthetic comment -- @@ -111,9 +111,26 @@
if (keyOrignal.length() > 0) {
keyOrignal.insert(0, ' ');
}
                    keyPinyin.insert(0, token.source);
                    keyInitial.insert(0, token.source.charAt(0));
}
keyOrignal.insert(0, token.source);
keys.add(keyOrignal.toString());
keys.add(keyPinyin.toString());







