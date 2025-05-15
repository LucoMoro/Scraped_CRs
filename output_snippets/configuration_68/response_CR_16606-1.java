//<Beginning of snippet n. 0>

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
        type != Character.DECIMAL_DIGIT_NUMBER &&
        type != Character.OTHER_LETTER) {
        break;
    }
    if (c >= '\u4E00' && c <= '\u9FFF') break; // CJK Unified Ideographs
    if (c >= '\u3040' && c <= '\u309F') break; // Hiragana
    if (c >= '\u30A0' && c <= '\u30FF') break; // Katakana
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
        type != Character.OTHER_LETTER) {
        break;
    }
    if (c >= '\u4E00' && c <= '\u9FFF') break; // CJK Unified Ideographs
    if (c >= '\u3040' && c <= '\u309F') break; // Hiragana
    if (c >= '\u30A0' && c <= '\u30FF') break; // Katakana
}

//<End of snippet n. 0>