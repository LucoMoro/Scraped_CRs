//<Beginning of snippet n. 0>
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    // get the sdcard values for checks
    long sdcardSize = Long.parseLong(m.group(1)); // pattern check
    String sdcardSizeModifier = m.group(2);
    if ("K".equals(sdcardSizeModifier)) {
        sdcardSize *= 1024;
    } else { // must be "M" per the pattern
        sdcardSize *= 1024 * 1024;
    }

    if (sdcardSize < 9 * 1024 * 1024 || sdcardSize > 999 * 1024 * 1024 * 1024) {
        throw new IllegalArgumentException("sdcardSize must be between 9MB and 999GB");
    }
//<End of snippet n. 0>