/*Fix SD Card size check to prevent int overflow.

Also added a check for sd card that are too big (>999GB) to prevent
long overflow.

Change-Id:Ie3ae57e679151dacf0e1fbbec23b29ccfdad3b6a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index c4914b2..352e500 100644

//Synthetic comment -- @@ -607,14 +607,25 @@
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







