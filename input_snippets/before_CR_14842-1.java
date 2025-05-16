
//<Beginning of snippet n. 0>


Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
// get the sdcard values for checks
                        int sdcardSize = Integer.parseInt(m.group(1)); // pattern check
// above makes
// this unlikely to fail
String sdcardSizeModifier = m.group(2);
if ("K".equals(sdcardSizeModifier)) {
                            sdcardSize *= 1024;
} else { // must be "M" per the pattern
                            sdcardSize *= 1024 * 1024;
}

if (sdcardSize < 9 * 1024 * 1024) {

//<End of snippet n. 0>








