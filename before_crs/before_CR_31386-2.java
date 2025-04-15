/*Fix crash when Wifi Direct device name starts with space

When Wifi Direct device name starts (or is just) space,
name=' ' from supplicant gets trimmed to "'" this
string has ' at end and beginnig. Then we call substring
with (1,0) causing crash.

Change-Id:I2b71fcfbd6fbe9d4794fb170823029c8abb8c70b*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java b/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java
//Synthetic comment -- index 1b0c301..18e6033 100644

//Synthetic comment -- @@ -306,6 +306,8 @@

private String trimQuotes(String str) {
str = str.trim();
if (str.startsWith("'") && str.endsWith("'")) {
return str.substring(1, str.length()-1);
}







