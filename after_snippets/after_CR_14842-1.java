
//<Beginning of snippet n. 0>


Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
// get the sdcard values for checks
                        long sdcardSize = Long.parseLong(m.group(1)); // pattern check
// above makes
// this unlikely to fail
                        // prevent overflow: no more than 999GB
                        // 10 digit for MiB, 13 for KiB
                        int digitCount = m.group(1).length();

String sdcardSizeModifier = m.group(2);
if ("K".equals(sdcardSizeModifier)) {
                            sdcardSize *= 1024L;
} else { // must be "M" per the pattern
                            sdcardSize *= 1024L * 1024L;
                            digitCount += 3; // convert the number of digit into "KiB"
                        }

                        if (digitCount >= 13) {
                            log.error(null, "SD Card size is too big!");
                            needCleanup = true;
                            return null;
}

if (sdcardSize < 9 * 1024 * 1024) {

//<End of snippet n. 0>








