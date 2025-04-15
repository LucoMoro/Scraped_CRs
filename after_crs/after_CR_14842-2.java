/*Fix SD Card size check to prevent int overflow.

Also added a check for sd card that are too big (>999GB) to prevent
long overflow.

Change-Id:Ie3ae57e679151dacf0e1fbbec23b29ccfdad3b6a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index c4914b2..35ce0b0 100644

//Synthetic comment -- @@ -607,18 +607,36 @@
Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
if (m.matches()) {
// get the sdcard values for checks
                        try {
                            long sdcardSize = Long.parseLong(m.group(1));

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
                                log.error(null, "SD Card size must be at least 9MB");
                                needCleanup = true;
                                return null;
                            }
                        } catch (NumberFormatException e) {
                            // this should never happen since the string is validated
                            // by the regexp
                            log.error(null, "Unable to parse SD Card size");
needCleanup = true;
return null;
}







