/*Fix for GPS engines without separate session and engine status events.

GPS_STATUS_SESSION_BEGIN now implies GPS_STATUS_ENGINE_ON
and GPS_STATUS_ENGINE_OFF now implies GPS_STATUS_SESSION_END.

Change-Id:I7217dc5213ae9a5658ac81b0a14b61d3e36ca0f6Signed-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index 134756e..1fe3d05 100755

//Synthetic comment -- @@ -885,6 +885,7 @@
switch (status) {
case GPS_STATUS_SESSION_BEGIN:
mNavigating = true;
break;
case GPS_STATUS_SESSION_END:
mNavigating = false;
//Synthetic comment -- @@ -894,6 +895,7 @@
break;
case GPS_STATUS_ENGINE_OFF:
mEngineOn = false;
break;
}








