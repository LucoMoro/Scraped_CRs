/*GPS Provider Service changes

Added support to delete aiding data test feature.

Added more data catagories, to now at the total of 21

Change-Id:Icb15d21b8204323021c47eac5249cde128e38246*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index c7bf52d..0f9727e 100755

//Synthetic comment -- @@ -113,19 +113,28 @@
private static final int LOCATION_HAS_ACCURACY = 16;

// IMPORTANT - the GPS_DELETE_* symbols here must match constants in gps.h
    private static final int GPS_DELETE_EPHEMERIS = 0x00000001;
    private static final int GPS_DELETE_ALMANAC = 0x00000002;
    private static final int GPS_DELETE_POSITION = 0x00000004;
    private static final int GPS_DELETE_TIME = 0x00000008;
    private static final int GPS_DELETE_IONO = 0x00000010;
    private static final int GPS_DELETE_UTC = 0x00000020;
    private static final int GPS_DELETE_HEALTH = 0x00000040;
    private static final int GPS_DELETE_SVDIR = 0x00000080;
    private static final int GPS_DELETE_SVSTEER = 0x00000100;
    private static final int GPS_DELETE_SADATA = 0x00000200;
    private static final int GPS_DELETE_RTI = 0x00000400;
    private static final int GPS_DELETE_CELLDB_INFO = 0x00000800;
    private static final int GPS_DELETE_ALMANAC_CORR = 0x00001000;
    private static final int GPS_DELETE_FREQ_BIAS_EST = 0x00002000;
    private static final int GPS_DELETE_EPHEMERIS_GLO = 0x00004000;
    private static final int GPS_DELETE_ALMANAC_GLO = 0x00008000;
    private static final int GPS_DELETE_SVDIR_GLO = 0x00010000;
    private static final int GPS_DELETE_SVSTEER_GLO = 0x00020000;
    private static final int GPS_DELETE_ALMANAC_CORR_GLO = 0x00040000;
    private static final int GPS_DELETE_TIME_GPS = 0x00080000;
    private static final int GPS_DELETE_TIME_GLO = 0x00100000;
    private static final int GPS_DELETE_ALL = 0xFFFFFFFF;

// The GPS_CAPABILITY_* flags must match the values in gps.h
private static final int GPS_CAPABILITY_SCHEDULING = 0x0000001;
//Synthetic comment -- @@ -946,6 +955,15 @@
if (extras.getBoolean("sadata")) flags |= GPS_DELETE_SADATA;
if (extras.getBoolean("rti")) flags |= GPS_DELETE_RTI;
if (extras.getBoolean("celldb-info")) flags |= GPS_DELETE_CELLDB_INFO;
            if (extras.getBoolean("almanac-corr")) flags |= GPS_DELETE_ALMANAC_CORR;
            if (extras.getBoolean("freq-bias-est")) flags |= GPS_DELETE_FREQ_BIAS_EST;
            if (extras.getBoolean("ephemeris-GLO")) flags |= GPS_DELETE_EPHEMERIS_GLO;
            if (extras.getBoolean("almanac-GLO")) flags |= GPS_DELETE_ALMANAC_GLO;
            if (extras.getBoolean("svdir-GLO")) flags |= GPS_DELETE_SVDIR_GLO;
            if (extras.getBoolean("svsteer-GLO")) flags |= GPS_DELETE_SVSTEER_GLO;
            if (extras.getBoolean("almanac-corr-GLO")) flags |= GPS_DELETE_ALMANAC_CORR_GLO;
            if (extras.getBoolean("time-gps")) flags |= GPS_DELETE_TIME_GPS;
            if (extras.getBoolean("time-GLO")) flags |= GPS_DELETE_TIME_GLO;
if (extras.getBoolean("all")) flags |= GPS_DELETE_ALL;
}








