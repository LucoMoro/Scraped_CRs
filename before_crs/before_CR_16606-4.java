/*Support for adding words with Asian characters to dictionary

TextView parsing of words is extended to include Unicode
class Letter Other which covers Asian languages among others.
Also added the possibility to add a text selection to the
dictionary if it is a valid word. Needed for languages
not using word separators, such as Chinese and Japanese.

Change-Id:I760ebbd6fb6fbe9a145ea01146b062b67fb12fdc*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..38b5bd3 100644

//Synthetic comment -- @@ -7245,45 +7245,92 @@
return -1;
}

        int len = mText.length();
        int end = Math.min(offset, len);

        if (end < 0) {
            return -1;
        }

        int start = end;

        for (; start > 0; start--) {
            char c = mTransformed.charAt(start - 1);
            int type = Character.getType(c);

            if (c != '\'' &&
                type != Character.UPPERCASE_LETTER &&
                type != Character.LOWERCASE_LETTER &&
                type != Character.TITLECASE_LETTER &&
                type != Character.MODIFIER_LETTER &&
                type != Character.DECIMAL_DIGIT_NUMBER) {
                break;
}
        }

        for (; end < len; end++) {
            char c = mTransformed.charAt(end);
            int type = Character.getType(c);

            if (c != '\'' &&
                type != Character.UPPERCASE_LETTER &&
                type != Character.LOWERCASE_LETTER &&
                type != Character.TITLECASE_LETTER &&
                type != Character.MODIFIER_LETTER &&
                type != Character.DECIMAL_DIGIT_NUMBER) {
                break;
}
        }

        if (start == end) {
            return -1;
}

if (end - start > 48) {
//Synthetic comment -- @@ -7292,7 +7339,7 @@

boolean hasLetter = false;
for (int i = start; i < end; i++) {
            if (Character.isLetter(mTransformed.charAt(i))) {
hasLetter = true;
break;
}
//Synthetic comment -- @@ -7441,6 +7488,14 @@
setAlphabeticShortcut('v');
added = true;
}
} else {
MenuHandler handler = new MenuHandler();

//Synthetic comment -- @@ -7602,6 +7657,11 @@

case ID_ADD_TO_DICTIONARY:
String word = getWordForDictionary();
if (word != null) {
Intent i = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
i.putExtra("word", word);








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/TextViewWordLimitsTest.java b/core/tests/coretests/src/android/widget/TextViewWordLimitsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..38b38ddd

//Synthetic comment -- @@ -0,0 +1,235 @@







