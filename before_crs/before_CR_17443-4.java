/*Support AP which only has a "space" character in its SSID

Now we can connect to an AP which only has a space character in its
SSID. Names with space at the beginning and/or will also work. The
earlier troublesome "-=^-^=-" still works.

Change-Id:Ie1de3bd162fbd992ccad5e1b5bca9c163e49e36d*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 4539c6b..202d6b5 100644

//Synthetic comment -- @@ -1349,7 +1349,7 @@
scanResult.frequency = frequency;
} else {
// Do not add scan results that have no SSID set
                        if (0 < ssid.trim().length()) {
scanResult =
new ScanResult(
ssid, bssid, flags, level, frequency);
//Synthetic comment -- @@ -1415,7 +1415,7 @@
// extract ssid from a series of "name=value"
String[] lines = status.split("\n");
for (String line : lines) {
            String[] prop = line.split(" *= *");
if (prop.length < 2) continue;
String name = prop[0];
String value = prop[1];







