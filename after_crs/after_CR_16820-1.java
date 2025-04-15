/*Fix code style

Change-Id:Ia42824614b270bf2372c9c88f4af82f6d01dacda*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactLocaleUtils.java b/src/com/android/providers/contacts/ContactLocaleUtils.java
//Synthetic comment -- index a817724..32819b1 100644

//Synthetic comment -- @@ -102,7 +102,7 @@
final Token token = tokens.get(i);
if (Token.PINYIN == token.type) {
keyPinyin.insert(0, token.target);
                    if (i == tokenCount - 1) {
// Use all characters of the last token
keyInitial.insert(0, token.target);
} else {
//Synthetic comment -- @@ -118,7 +118,7 @@
keyOrignal.insert(0, ' ');
}
keyPinyin.insert(0, token.source);
                    if (i == tokenCount - 1) {
// Use all characters of the last token
keyInitial.insert(0, token.source);
} else {







