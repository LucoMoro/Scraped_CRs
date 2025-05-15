//<Beginning of snippet n. 0>
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    try {
        int sdcardSize = Integer.parseInt(m.group(1)); // pattern check
        String sdcardSizeModifier = m.group(2);
        
        if (sdcardSizeModifier != null) {
            if ("K".equals(sdcardSizeModifier)) {
                if (sdcardSize > (Integer.MAX_VALUE / 1024)) {
                    throw new IllegalArgumentException("Size exceeds maximum limit");
                }
                sdcardSize *= 1024;
            } else if ("M".equals(sdcardSizeModifier)) {
                if (sdcardSize > (Integer.MAX_VALUE / (1024 * 1024))) {
                    throw new IllegalArgumentException("Size exceeds maximum limit");
                }
                sdcardSize *= 1024 * 1024;
            } else {
                throw new IllegalArgumentException("Invalid size modifier");
            }
        } else {
            throw new IllegalArgumentException("Size modifier is null");
        }

        if (sdcardSize < 9 * 1024 * 1024 || sdcardSize > 999 * 1024 * 1024) {
            throw new IllegalArgumentException("Size is out of valid range");
        }

    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid sdcard size format", e);
    }
}
//<End of snippet n. 0>