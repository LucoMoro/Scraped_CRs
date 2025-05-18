//<Beginning of snippet n. 0>
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
    long sdcardSize = Long.parseLong(m.group(1)); // changed to long
    String sdcardSizeModifier = m.group(2);

    if (sdcardSize < 9) {
        throw new IllegalArgumentException("Invalid sdcard size");
    }

    if ("K".equals(sdcardSizeModifier)) {
        if (sdcardSize > Long.MAX_VALUE / 1024) {
            throw new IllegalArgumentException("Size too large after conversion to bytes");
        }
        sdcardSize *= 1024;
    } else if ("M".equals(sdcardSizeModifier)) {
        if (sdcardSize > Long.MAX_VALUE / (1024 * 1024)) {
            throw new IllegalArgumentException("Size too large after conversion to bytes");
        }
        sdcardSize *= 1024 * 1024;
    } else {
        throw new IllegalArgumentException("Unsupported size modifier");
    }

    if (sdcardSize > 999L * 1024 * 1024 * 1024) {
        throw new IllegalArgumentException("Invalid sdcard size");
    }
//<End of snippet n. 0>