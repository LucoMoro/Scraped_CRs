//<Beginning of snippet n. 0>
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    long sdcardSize = Long.parseLong(m.group(1)); // pattern check
    String sdcardSizeModifier = m.group(2);
    if ("K".equals(sdcardSizeModifier)) {
        if (sdcardSize > Long.MAX_VALUE / 1024) {
            throw new IllegalArgumentException("Calculated size exceeds maximum limit.");
        }
        sdcardSize *= 1024;
    } else { // must be "M" per the pattern
        if (sdcardSize > Long.MAX_VALUE / (1024 * 1024)) {
            throw new IllegalArgumentException("Calculated size exceeds maximum limit.");
        }
        sdcardSize *= 1024 * 1024;
    }

    if (sdcardSize < 9L * 1024 * 1024) {
        throw new IllegalArgumentException("Size is too small.");
    }
    
    if (sdcardSize > 999L * 1024 * 1024 * 1024) {
        throw new IllegalArgumentException("Size exceeds 999GB limit.");
    }
//<End of snippet n. 0>