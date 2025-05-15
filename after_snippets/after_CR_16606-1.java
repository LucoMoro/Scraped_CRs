
//<Beginning of snippet n. 0>


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


//<End of snippet n. 0>








