/*Support AP which only has a "space" character in its SSID

Now we can connect to an AP which only has a space character in its
SSID. Names with space at the beginning and/or will also work. The
earlier troublesome "-=^-^=-" still works.

Change-Id:Ie1de3bd162fbd992ccad5e1b5bca9c163e49e36d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index 8d6ad93..7951786 100644

//Synthetic comment -- @@ -1472,7 +1472,7 @@
scanResult.frequency = frequency;
} else {
// Do not add scan results that have no SSID set
                        if (0 < ssid.trim().length()) {
scanResult =
new ScanResult(
ssid, bssid, flags, level, frequency);








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index aef93c6..3306293 100644

//Synthetic comment -- @@ -1349,7 +1349,7 @@
int netId = -1;
String[] lines = reply.split("\n");
for (String line : lines) {
            String[] prop = line.split(" *= *", 2);
if (prop.length < 2)
continue;
String name = prop[0];







