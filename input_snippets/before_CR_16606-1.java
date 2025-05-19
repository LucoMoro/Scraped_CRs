
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


//<End of snippet n. 0>








