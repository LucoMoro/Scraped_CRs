/*Fix the ssid display issue
Reproduce steps:
1. Set a special AP name that contains "=" (looks like "-=^-^=-");
2. Connect to this AP;
3. The display of ssid will be incorrect;

Change-Id:Idc7433a42cf59932f5fb147f5500e0175f745399*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index f97f21b..b097083 100644

//Synthetic comment -- @@ -1373,7 +1373,7 @@
int netId = -1;
String[] lines = reply.split("\n");
for (String line : lines) {
            String[] prop = line.split(" *= *", 2);
if (prop.length < 2)
continue;
String name = prop[0];







