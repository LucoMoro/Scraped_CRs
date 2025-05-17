//<Beginning of snippet n. 0>
int start = end;
int len = mText.length();

// Check boundaries before accessing characters
if (start < 0 || start > len || end < 0 || end > len) {
    return null;
}

for (; start > 0; start--) {
    char c = mTransformed.charAt(start - 1);
    int type = Character.getType(c);

    if (c != '\'' &&
        type != Character.UPPERCASE_LETTER &&
        type != Character.LOWERCASE_LETTER &&
        type != Character.TITLECASE_LETTER &&
        type != Character.MODIFIER_LETTER &&
        type != Character.DECIMAL_DIGIT_NUMBER &&
        type != Character.OTHER_LETTER &&
        type != Character.HAN_LETTER &&
        type != Character.SURROGATE) { // Added for Asian characters
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
        type != Character.DECIMAL_DIGIT_NUMBER &&
        type != Character.OTHER_LETTER &&
        type != Character.HAN_LETTER &&
        type != Character.SURROGATE) { // Added for Asian characters
        break;
    }
}
//<End of snippet n. 0>