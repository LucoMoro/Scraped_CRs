//<Beginning of snippet n. 0>

Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    long sdcardSize;
    try {
        sdcardSize = Long.parseLong(m.group(1)); // pattern check
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid number format for sdcardSize", e);
    }
    String sdcardSizeModifier = m.group(2);
    
    if ("K".equals(sdcardSizeModifier)) {
        if (sdcardSize > Long.MAX_VALUE / 1024) {
            throw new IllegalArgumentException("sdcardSize too large for K modifier");
        }
        sdcardSize *= 1024;
    } else if ("M".equals(sdcardSizeModifier)) {
        if (sdcardSize > Long.MAX_VALUE / (1024 * 1024)) {
            throw new IllegalArgumentException("sdcardSize too large for M modifier");
        }
        sdcardSize *= 1024 * 1024;
    } else if (sdcardSizeModifier != null) {
        throw new IllegalArgumentException("Invalid sdcardSizeModifier: " + sdcardSizeModifier);
    }

    if (sdcardSize > 999L * 1024 * 1024 * 1024) {
        throw new IllegalArgumentException("sdcardSize too large");
    }

    if (sdcardSize < 9 * 1024 * 1024) {
        throw new IllegalArgumentException("sdcardSize is below minimum size");
    }

    if (sdcardSize < 0) {
        throw new IllegalArgumentException("sdcardSize is negative after modification");
    }
    
    // Final check against Long.MAX_VALUE
    if (sdcardSize > Long.MAX_VALUE) {
        throw new IllegalArgumentException("sdcardSize exceeds maximum value");
    }

//<End of snippet n. 0>