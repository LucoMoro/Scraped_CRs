/*Keep full pinyin of the last character of Chinese name instead of initial

One pinyin initial may match many contacts.
E.g. looking up BT can get Bai Tao, Bai Ting, Bi Teng,
Bao Tou and etc. So use all characters of the last
token to narrow the lookup results. That means BTao
for Bai Tao, BTing for Bai Ting, BTeng for Bi Teng
and so on.

Change-Id:I8d4cefb50edb7ebb98c202b82f02f72e26843f8c*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/ContactLocaleUtils.java b/src/com/android/providers/contacts/ContactLocaleUtils.java
//Synthetic comment -- index 1c2ad97..25610f9 100644

//Synthetic comment -- @@ -102,7 +102,19 @@
final Token token = tokens.get(i);
if (Token.PINYIN == token.type) {
keyPinyin.insert(0, token.target);
                    // One pinyin initial may match many contacts.
                    // E.g. looking up BT can get Bai Tao, Bai Ting, Bi Teng, Bao Tou and etc.
                    // Using all characters of the last token can narrow the lookup results 
                    // much more with an additional input.
                    // That means BTao for Bai Tao, BTing for Bai Ting, BTeng for Bi Teng
                    // and so on.
                    if (i == tokenCount - 1) {
                        // Use all characters of the last token
                        keyInitial.insert(0, token.target);
                    } else {
                        // Use the first character of other tokens
                        keyInitial.insert(0, token.target.charAt(0));
                    }
} else if (Token.LATIN == token.type) {
// Avoid adding space at the end of String.
if (keyPinyin.length() > 0) {
//Synthetic comment -- @@ -112,12 +124,20 @@
keyOrignal.insert(0, ' ');
}
keyPinyin.insert(0, token.source);
                    if (i == tokenCount - 1) {
                        // Use all characters of the last token
                        keyInitial.insert(0, token.source);
                    } else {
                        // Use the first character of other tokens
                        keyInitial.insert(0, token.source.charAt(0));
                    }
}
keyOrignal.insert(0, token.source);
keys.add(keyOrignal.toString());
keys.add(keyPinyin.toString());
                if (i != tokenCount - 1) {
                    keys.add(keyInitial.toString());
                }
}
return keys.iterator();
}








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/ContactLocaleUtilsTest.java b/tests/src/com/android/providers/contacts/ContactLocaleUtilsTest.java
//Synthetic comment -- index 0a0955e..c856a7b 100644

//Synthetic comment -- @@ -27,18 +27,27 @@

import com.android.providers.contacts.ContactLocaleUtils;

/**
 * Unit tests for {@link ContactLocaleUtils}.
 *
 * Run the test like this:
 * <code>
 * adb shell am instrument -e class com.android.providers.contacts.ContactLocaleUtilsTest -w \
 *         com.android.providers.contacts.tests/android.test.InstrumentationTestRunner
 * </code>
 */
public class ContactLocaleUtilsTest extends AndroidTestCase {
private static final String LATIN_NAME = "John Smith";
private static final String CHINESE_NAME = "\u675C\u9D51";
private static final String CHINESE_LATIN_MIX_NAME_1 = "D\u675C\u9D51";
private static final String CHINESE_LATIN_MIX_NAME_2 = "MARY \u675C\u9D51";
private static final String[] CHINESE_NAME_KEY = {"\u9D51", "\u675C\u9D51", "JUAN", "DUJUAN",
            "DJUAN"};
private static final String[] CHINESE_LATIN_MIX_NAME_1_KEY = {"\u9D51", "\u675C\u9D51",
        "D \u675C\u9D51", "JUAN", "DUJUAN", "DJUAN", "D DUJUAN", "DDJUAN"};
private static final String[] CHINESE_LATIN_MIX_NAME_2_KEY = {"\u9D51", "\u675C\u9D51",
        "MARY \u675C\u9D51", "JUAN", "DUJUAN", "MARY DUJUAN", "DJUAN", "MDJUAN"};
    private static final String[] LATIN_NAME_KEY = {"John Smith", "Smith", "JSmith"};


private ContactLocaleUtils mContactLocaleUtils = ContactLocaleUtils.getIntance();








//Synthetic comment -- diff --git a/tests/src/com/android/providers/contacts/NameLookupBuilderTest.java b/tests/src/com/android/providers/contacts/NameLookupBuilderTest.java
//Synthetic comment -- index 1de34c0..2dd3657 100644

//Synthetic comment -- @@ -195,9 +195,8 @@
"(6:\u695A\u8FAD)" +
"(6:CI)" +
"(6:\u8FAD)" +
                "(6:CCI)" +
                "(6:CHUCI)", mBuilder.inserted());
}

public void testMultiwordName() {







