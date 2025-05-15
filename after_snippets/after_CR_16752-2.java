
//<Beginning of snippet n. 0>


filename = Constants.DEFAULT_DL_FILENAME;
}

        // The VFAT file system is assumed as target for downloads.
        // Replace invalid characters according to the specifications of VFAT.
        filename = replaceInvalidVfatCharacters(filename);

return filename;
}
(c >= '0' && c <= '9');
}
}

    /**
     * Replace invalid filename characters according to
     * specifications of the VFAT.
     * @note Package-private due to testing.
     */
    private static String replaceInvalidVfatCharacters(String filename) {
        final char START_CTRLCODE = 0x00;
        final char END_CTRLCODE = 0x1f;
        final char QUOTEDBL = 0x22;
        final char ASTERISK = 0x2A;
        final char SLASH = 0x2F;
        final char COLON = 0x3A;
        final char LESS = 0x3C;
        final char GREATER = 0x3E;
        final char QUESTION = 0x3F;
        final char BACKSLASH = 0x5C;
        final char BAR = 0x7C;
        final char DEL = 0x7F;
        final char UNDERSCORE = 0x5F;

        StringBuffer sb = new StringBuffer();
        char ch;
        boolean isRepetition = false;
        for (int i = 0; i < filename.length(); i++) {
            ch = filename.charAt(i);
            if ((START_CTRLCODE <= ch &&
                ch <= END_CTRLCODE) ||
                ch == QUOTEDBL ||
                ch == ASTERISK ||
                ch == SLASH ||
                ch == COLON ||
                ch == LESS ||
                ch == GREATER ||
                ch == QUESTION ||
                ch == BACKSLASH ||
                ch == BAR ||
                ch == DEL){
                if (!isRepetition) {
                    sb.append(UNDERSCORE);
                    isRepetition = true;
                }
            } else {
                sb.append(ch);
                isRepetition = false;
            }
        }
        return sb.toString();
    }
}

//<End of snippet n. 0>








