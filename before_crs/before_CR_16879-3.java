/*Check if we are in daylight time when calculating timezone

The code that calculates the Kernel TimeZone does not check
if we are actually in daylite time prior updating the kernel
time zone. Use TimeZone.getOffset which checks for daylight
savings. Also updated setTimeZone for consistency.

Change-Id:I44565bc8df48e81370ea3bfbb099a6a5675e343d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 4931cc7..cf3ecdc 100644

//Synthetic comment -- @@ -281,10 +281,7 @@

// Update the kernel timezone information
// Kernel tracks time offsets as 'minutes west of GMT'
            int gmtOffset = zone.getRawOffset();
            if (zone.inDaylightTime(new Date(System.currentTimeMillis()))) {
                gmtOffset += zone.getDSTSavings();
            }
setKernelTimezone(mDescriptor, -(gmtOffset / 60000));
}

//Synthetic comment -- @@ -784,9 +781,8 @@
// based off of the current Zone gmt offset + userspace tracked
// daylight savings information.
TimeZone zone = TimeZone.getTimeZone(SystemProperties.get(TIMEZONE_PROPERTY));
                int gmtOffset = (zone.getRawOffset() + zone.getDSTSavings()) / 60000;

                setKernelTimezone(mDescriptor, -(gmtOffset));
	scheduleDateChangedEvent();
}
}







