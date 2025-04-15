/*Keep full pinyin of the last character of Chinese name instead of initial

One pinyin initial may match many contacts.
E.g. looking up BT can get Bai Tao, Bai Ting, Bi Teng,
Bao Tou and etc. So use all characters of the last
token to narrow the lookup results. That means BTao
for Bai Tao, BTing for Bai Ting, BTeng for Bi Teng
and so on.

Change-Id:I8d4cefb50edb7ebb98c202b82f02f72e26843f8c*/
//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactLocaleUtils.java b/src/com/android/providers/contacts/ContactLocaleUtils.java
//Synthetic comment -- index 1c2ad97..ec58be2 100644

//Synthetic comment -- @@ -102,7 +102,19 @@
final Token token = tokens.get(i);
if (Token.PINYIN == token.type) {
keyPinyin.insert(0, token.target);
                    keyInitial.insert(0, token.target.charAt(0));
} else if (Token.LATIN == token.type) {
// Avoid adding space at the end of String.
if (keyPinyin.length() > 0) {
//Synthetic comment -- @@ -112,12 +124,20 @@
keyOrignal.insert(0, ' ');
}
keyPinyin.insert(0, token.source);
                    keyInitial.insert(0, token.source.charAt(0));
}
keyOrignal.insert(0, token.source);
keys.add(keyOrignal.toString());
keys.add(keyPinyin.toString());
                keys.add(keyInitial.toString());
}
return keys.iterator();
}








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactLocaleUtilsTest.java b/tests/src/com/android/providers/contacts/ContactLocaleUtilsTest.java
//Synthetic comment -- index 0a0955e..c856a7b 100644

//Synthetic comment -- @@ -27,18 +27,27 @@

import com.android.providers.contacts.ContactLocaleUtils;

public class ContactLocaleUtilsTest extends AndroidTestCase {
private static final String LATIN_NAME = "John Smith";
private static final String CHINESE_NAME = "\u675C\u9D51";
private static final String CHINESE_LATIN_MIX_NAME_1 = "D\u675C\u9D51";
private static final String CHINESE_LATIN_MIX_NAME_2 = "MARY \u675C\u9D51";
private static final String[] CHINESE_NAME_KEY = {"\u9D51", "\u675C\u9D51", "JUAN", "DUJUAN",
            "J", "DJ"};
private static final String[] CHINESE_LATIN_MIX_NAME_1_KEY = {"\u9D51", "\u675C\u9D51",
        "D \u675C\u9D51", "JUAN", "DUJUAN", "J", "DJ", "D DUJUAN", "DDJ"};
private static final String[] CHINESE_LATIN_MIX_NAME_2_KEY = {"\u9D51", "\u675C\u9D51",
        "MARY \u675C\u9D51", "JUAN", "DUJUAN", "MARY DUJUAN", "J", "DJ", "MDJ"};
    private static final String[] LATIN_NAME_KEY = {"John Smith", "Smith", "JS", "S"};


private ContactLocaleUtils mContactLocaleUtils = ContactLocaleUtils.getIntance();








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/NameLookupBuilderTest.java b/tests/src/com/android/providers/contacts/NameLookupBuilderTest.java
//Synthetic comment -- index 1de34c0..2dd3657 100644

//Synthetic comment -- @@ -195,9 +195,8 @@
"(6:\u695A\u8FAD)" +
"(6:CI)" +
"(6:\u8FAD)" +
                "(6:CHUCI)" +
                "(6:CC)" +
                "(6:C)", mBuilder.inserted());
}

public void testMultiwordName() {







