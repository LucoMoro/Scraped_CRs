//<Beginning of snippet n. 0>
public static void testInvalidInfoTypes() throws Exception {
    for (int type : DrmInfoRequest.getAllTypes()) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static void testInvalidStatusCodes() throws Exception {
    for (int status : DrmInfoStatus.getAllStatusCodes()) {
        checkInvalidStatusCode(status + 1);
        checkInvalidStatusCode(status - 1);
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public static void testInvalidInfoTypes() throws Exception {
    for (int type : DrmInfoRequest.getAllTypes()) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}

//<End of snippet n. 2>