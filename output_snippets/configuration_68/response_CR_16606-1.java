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
        type != Character.HAN_LETTER &&
        type != Character.KATAKANA_LETTER &&
        type != Character.HALFWIDTH_KATAKANA &&
        type != Character.OTHER_LETTER &&
        type != Character.NON_SPACING_MARK &&
        type != Character.COMBINING_SPACING_MARK &&
        type != Character.CJK_UNIFIED_IDEOGRAPHS &&
        type != Character.CJK_COMPATIBILITY_IDEOGRAPHS &&
        type != Character.OTHER_SYMBOL &&
        type != Character.COMBINING_SPACING_MARK) {
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
        type != Character.HAN_LETTER &&
        type != Character.KATAKANA_LETTER &&
        type != Character.HALFWIDTH_KATAKANA &&
        type != Character.OTHER_LETTER &&
        type != Character.NON_SPACING_MARK &&
        type != Character.COMBINING_SPACING_MARK &&
        type != Character.CJK_UNIFIED_IDEOGRAPHS &&
        type != Character.CJK_COMPATIBILITY_IDEOGRAPHS &&
        type != Character.OTHER_SYMBOL &&
        type != Character.COMBINING_SPACING_MARK) {
        break;
    }
}

//<End of snippet n. 0>