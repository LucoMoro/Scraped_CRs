
//<Beginning of snippet n. 0>


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
if (extras.getBoolean("sadata")) flags |= GPS_DELETE_SADATA;
if (extras.getBoolean("rti")) flags |= GPS_DELETE_RTI;
if (extras.getBoolean("celldb-info")) flags |= GPS_DELETE_CELLDB_INFO;
if (extras.getBoolean("all")) flags |= GPS_DELETE_ALL;
}


//<End of snippet n. 0>








