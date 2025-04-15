/*Pass the APN protocol type to LinkProperties

Change-Id:I1fc6b00ffb57a3fe338410adc61f41bd3edffd4cSigned-off-by: Daniel Drown <dan-android@drown.org>
Depends-on:I2dd8ed9c7f18c654cc20343120f1f4c23559138c*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataCallState.java b/src/java/com/android/internal/telephony/DataCallState.java
//Synthetic comment -- index efbf608..d9c2702 100644

//Synthetic comment -- @@ -123,6 +123,8 @@
// set interface name
linkProperties.setInterfaceName(ifname);

// set link addresses
if (addresses != null && addresses.length > 0) {
for (String addr : addresses) {







