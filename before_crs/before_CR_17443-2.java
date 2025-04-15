/*Support AP which only has a "space" character in its SSID

Now we can connect to an AP which only has a space character in its
SSID. Names with space at the beginning and/or will also work. The
earlier troublesome "-=^-^=-" still works.

Change-Id:Ie1de3bd162fbd992ccad5e1b5bca9c163e49e36d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index 5aa0111..2413cd8 100644

//Synthetic comment -- @@ -1477,7 +1477,7 @@
scanResult.frequency = frequency;
} else {
// Do not add scan results that have no SSID set
                        if (0 < ssid.trim().length()) {
scanResult =
new ScanResult(
ssid, bssid, flags, level, frequency);








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bf2d033..eefd7fd 100644

//Synthetic comment -- @@ -1478,7 +1478,7 @@

String[] lines = reply.split("\n");
for (String line : lines) {
                String[] prop = line.split(" *= *");
if (prop.length < 2)
continue;
String name = prop[0];







