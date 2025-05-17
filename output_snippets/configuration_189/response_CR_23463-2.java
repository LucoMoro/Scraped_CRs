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

private void processExtras(Bundle extras) {
    if (extras == null) {
        throw new IllegalArgumentException("Extras cannot be null");
    }

    int flags = 0;
    Set<String> validKeys = new HashSet<>(Arrays.asList("sadata", "rti", "celldb-info", "all"));

    for (String key : extras.keySet()) {
        if (!validKeys.contains(key)) {
            throw new IllegalArgumentException("Unrecognized key: " + key);
        }
        if (!(extras.get(key) instanceof Boolean)) {
            throw new IllegalArgumentException("Value for key " + key + " must be a Boolean");
        }
    }

    if (extras.containsKey("sadata") && extras.getBoolean("sadata")) flags |= GPS_DELETE_SADATA;
    if (extras.containsKey("rti") && extras.getBoolean("rti")) flags |= GPS_DELETE_RTI;
    if (extras.containsKey("celldb-info") && extras.getBoolean("celldb-info")) flags |= GPS_DELETE_CELLDB_INFO;
    if (extras.containsKey("all") && extras.getBoolean("all")) flags |= GPS_DELETE_ALL;
}

//<End of snippet n. 0>