/*Support for adding words with Asian characters to dictionary

TextView parsing of words is extended to include Unicode
class Letter Other which covers Asian languages among others.
Also added the possibility to add a text selection to the
dictionary if it is a valid word. Needed for languages
not using word separators, such as Chinese and Japanese.

Change-Id:I760ebbd6fb6fbe9a145ea01146b062b67fb12fdc*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index e6ed70a..b633c93 100644

//Synthetic comment -- @@ -6943,34 +6943,64 @@
return null;
}

        int start = end;
        int len = mText.length();

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








