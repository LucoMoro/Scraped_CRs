//<Beginning of snippet n. 0>
return null;
}

int start = end;
int len = mText.length();

for (; start > 0; start--) {
    char c = mTransformed.charAt(start - 1);
    int type = Character.getType(c);
    Character.UnicodeScript script = Character.UnicodeScript.of(c);

    if (c != '\'' &&
        type != Character.UPPERCASE_LETTER &&
        type != Character.LOWERCASE_LETTER &&
        type != Character.TITLECASE_LETTER &&
        type != Character.MODIFIER_LETTER &&
        type != Character.DECIMAL_DIGIT_NUMBER &&
        script != Character.UnicodeScript.HAN && // CJK
        script != Character.UnicodeScript.HIRAGANA && // Japanese
        script != Character.UnicodeScript.KATAKANA && // Japanese
        script != Character.UnicodeScript.BOPOMOFO) { // Chinese
        break;
    }
}

for (; end < len; end++) {
    char c = mTransformed.charAt(end);
    int type = Character.getType(c);
    Character.UnicodeScript script = Character.UnicodeScript.of(c);

    if (c != '\'' &&
        type != Character.UPPERCASE_LETTER &&
        type != Character.LOWERCASE_LETTER &&
        type != Character.TITLECASE_LETTER &&
        type != Character.MODIFIER_LETTER &&
        type != Character.DECIMAL_DIGIT_NUMBER &&
        script != Character.UnicodeScript.HAN && // CJK
        script != Character.UnicodeScript.HIRAGANA && // Japanese
        script != Character.UnicodeScript.KATAKANA && // Japanese
        script != Character.UnicodeScript.BOPOMOFO) { // Chinese
        break;
    }
}

// Method to validate if the text consists solely of valid Asian characters
private boolean isValidAsianCharacterSequence(String text) {
    for (char c : text.toCharArray()) {
        Character.UnicodeScript script = Character.UnicodeScript.of(c);
        if (script != Character.UnicodeScript.HAN && 
            script != Character.UnicodeScript.HIRAGANA && 
            script != Character.UnicodeScript.KATAKANA &&
            script != Character.UnicodeScript.BOPOMOFO &&
            Character.getType(c) != Character.LOWERCASE_LETTER &&
            Character.getType(c) != Character.UPPERCASE_LETTER &&
            Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER) {
            return false;
        }
    }
    return true;
}
//<End of snippet n. 0>