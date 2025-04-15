/*Check if we are in daylight time when calculating timezone

The code that calculates the Kernel TimeZone does not check
if we are actually in daylite time prior updating the kernel
time zone. Add a call to TimeZone.inDaylightTime to get correct
time zone.

Change-Id:I44565bc8df48e81370ea3bfbb099a6a5675e343d*/




//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index e088417..2de09a6 100644

//Synthetic comment -- @@ -771,9 +771,11 @@
// based off of the current Zone gmt offset + userspace tracked
// daylight savings information.
TimeZone zone = TimeZone.getTimeZone(SystemProperties.get(TIMEZONE_PROPERTY));
                int gmtOffset = zone.getRawOffset();
                if (zone.inDaylightTime(new Date(System.currentTimeMillis()))) {
                    gmtOffset += zone.getDSTSavings();
                }
                setKernelTimezone(mDescriptor, -(gmtOffset / 60000));
	scheduleDateChangedEvent();
}
}







