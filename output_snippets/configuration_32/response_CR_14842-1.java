//<Beginning of snippet n. 0>


Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
// get the sdcard values for checks
                        long sdcardSize = Long.parseLong(m.group(1)); // pattern check
                        if (sdcardSize > 999L * 1024 * 1024 * 1024) {
                            throw new IllegalArgumentException("SD card size exceeds maximum limit of 999GB.");
                        }
String sdcardSizeModifier = m.group(2);
if ("K".equals(sdcardSizeModifier)) {
                            sdcardSize *= 1024L;
} else { // must be "M" per the pattern
                            sdcardSize *= 1024L * 1024;
}

if (sdcardSize < 9L * 1024 * 1024) {

//<End of snippet n. 0>