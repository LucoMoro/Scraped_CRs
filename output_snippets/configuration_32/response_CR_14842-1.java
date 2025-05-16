//<Beginning of snippet n. 0>
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    long sdcardSize = Long.parseLong(m.group(1)); // updated to long
    String sdcardSizeModifier = m.group(2);
    if ("K".equals(sdcardSizeModifier)) {
        sdcardSize *= 1024;
    } else { // must be "M" per the pattern
        sdcardSize *= 1024 * 1024;
    }

    // Validation check for sdcardSize
    if (sdcardSize > 999L * 1024 * 1024 * 1024) {
        throw new IllegalArgumentException("SD Card size exceeds 999GB limit");
    }

    if (sdcardSize < 9 * 1024 * 1024) {
//<End of snippet n. 0>