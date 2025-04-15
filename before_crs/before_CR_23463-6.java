/*GPS Provider changes

Added support to delete aiding data test feature.
Added more data catagories, to now at the total of 21

Change-Id:Icb15d21b8204323021c47eac5249cde128e38246*/
//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index a754335..a217e60 100755

//Synthetic comment -- @@ -120,19 +120,28 @@
private static final int LOCATION_HAS_ACCURACY = 16;

// IMPORTANT - the GPS_DELETE_* symbols here must match constants in gps.h
    private static final int GPS_DELETE_EPHEMERIS = 0x0001;
    private static final int GPS_DELETE_ALMANAC = 0x0002;
    private static final int GPS_DELETE_POSITION = 0x0004;
    private static final int GPS_DELETE_TIME = 0x0008;
    private static final int GPS_DELETE_IONO = 0x0010;
    private static final int GPS_DELETE_UTC = 0x0020;
    private static final int GPS_DELETE_HEALTH = 0x0040;
    private static final int GPS_DELETE_SVDIR = 0x0080;
    private static final int GPS_DELETE_SVSTEER = 0x0100;
    private static final int GPS_DELETE_SADATA = 0x0200;
    private static final int GPS_DELETE_RTI = 0x0400;
    private static final int GPS_DELETE_CELLDB_INFO = 0x8000;
    private static final int GPS_DELETE_ALL = 0xFFFF;

// The GPS_CAPABILITY_* flags must match the values in gps.h
private static final int GPS_CAPABILITY_SCHEDULING = 0x0000001;
//Synthetic comment -- @@ -962,6 +971,15 @@
if (extras.getBoolean("sadata")) flags |= GPS_DELETE_SADATA;
if (extras.getBoolean("rti")) flags |= GPS_DELETE_RTI;
if (extras.getBoolean("celldb-info")) flags |= GPS_DELETE_CELLDB_INFO;
if (extras.getBoolean("all")) flags |= GPS_DELETE_ALL;
}








