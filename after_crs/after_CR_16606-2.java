/*Support for adding words with Asian characters to dictionary

TextView parsing of words is extended to include Unicode
class Letter Other which covers Asian languages among others.
Also added the possibility to add a text selection to the
dictionary if it is a valid word. Needed for languages
not using word separators, such as Chinese and Japanese.

Change-Id:I760ebbd6fb6fbe9a145ea01146b062b67fb12fdc*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index e6ed70a..446e249 100644

//Synthetic comment -- @@ -6943,34 +6943,64 @@
return null;
}

        int start = getSelectionStart();

        // Use the selection if it is a valid word
        if (start != end && start >= 0) {
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
}

            for (int i = start; i < end; i++) {
                char c = mTransformed.charAt(i);
                int type = Character.getType(c);

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    return null;
                }
            }

        // Use the word around the cursor if no selection
        } else {
            start = end;

            for (; start > 0; start--) {
                char c = mTransformed.charAt(start - 1);
                int type = Character.getType(c);

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    break;
                }
            }

            int len = mText.length();
            for (; end < len; end++) {
                char c = mTransformed.charAt(end);
                int type = Character.getType(c);

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    break;
                }
}
}

//Synthetic comment -- @@ -7278,6 +7308,13 @@
case ID_ADD_TO_DICTIONARY:
String word = getWordForDictionary();

                if (mText instanceof Spannable) {
                    MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);
                    if (min != max) {
                        Selection.setSelection((Spannable) mText, max);
                    }
                }

if (word != null) {
Intent i = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
i.putExtra("word", word);







