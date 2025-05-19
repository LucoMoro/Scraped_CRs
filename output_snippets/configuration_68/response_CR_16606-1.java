//<Beginning of snippet n. 0>
return null;
}

int start = end;
int len = mText.length();

for (; start > 0; start--) {
    char c = mTransformed.charAt(start - 1);
    if (Character.isLetter(c) || Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.HIRAGANA || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.KATAKANA || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.BOPOMOFO || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.COMMON || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.LATIN || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.OTHER || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.CJK_UNIFIED_IDEOGRAPHS || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.CJK_COMPATIBILITY_IDEOGRAPHS || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.OTHER_LETTER ||
        Character.isISOControl(c) || Character.isWhitespace(c) || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.COMBINING_SPACING_MARK) {
        continue;
    }
    if (c != '\'' && !Character.isDigit(c)) {
        break;
    }
}

for (; end < len; end++) {
    char c = mTransformed.charAt(end);
    if (Character.isLetter(c) || Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.HIRAGANA || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.KATAKANA || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.BOPOMOFO || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.COMMON || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.LATIN || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.OTHER || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.CJK_UNIFIED_IDEOGRAPHS || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.CJK_COMPATIBILITY_IDEOGRAPHS || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.OTHER_LETTER ||
        Character.isISOControl(c) || Character.isWhitespace(c) || 
        Character.UnicodeScript.of(c) == Character.UnicodeScript.COMBINING_SPACING_MARK) {
        continue;
    }
    if (c != '\'' && !Character.isDigit(c)) {
        break;
    }
}
//<End of snippet n. 0>