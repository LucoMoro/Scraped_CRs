/*Force Android to use the offset in NITZ

If Android has no country-code set, and receives a NITZ-message,
it will wait with adjusting the timezone-offset until it gets a
proper country-code. This fix will make Android, when it has no country-code
set, use the offset in the NITZ message to temporarily guess a country and timezone.
The timezone and country will update when the network sends a country-code.

Change-Id:I2d7c1512d95ac74ea3c09ab18f9ac9d1e83fa1d9Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6ddb312..7688a72 100644

//Synthetic comment -- @@ -1478,6 +1478,12 @@
mZoneOffset  = tzOffset;
mZoneDst     = dst != 0;
mZoneTime    = c.getTimeInMillis();

                // Guess the timezone based on NITZ offset.
                if (TimeZone.getAvailableIDs(tzOffset).length > 0) {
                    TimeZone.setDefault(TimeZone.getTimeZone(TimeZone
                     .getAvailableIDs(tzOffset)[0]));
                }
}

if (zone != null) {







