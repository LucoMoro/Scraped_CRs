//<Beginning of snippet n. 0>

public static void testInvalidInfoTypes() throws Exception {
    for (int type : getAllDrmInfoRequestTypes()) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}

private static int[] getAllDrmInfoRequestTypes() {
    // Return an array of all valid DRM types for dynamic checking
    return new int[] {
        DrmInfoRequest.TYPE_REGISTRATION_INFO,
        DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO,
        // Add any additional DRM types here as needed
    };
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static void testInvalidStatusCodes() throws Exception {
    for (int status : getAllDrmInfoStatusCodes()) {
        checkInvalidStatusCode(status + 1);
        checkInvalidStatusCode(status - 1);
    }
}

private static int[] getAllDrmInfoStatusCodes() {
    // Return an array of all valid DRM status codes for dynamic checking
    return new int[] {
        DrmInfoStatus.STATUS_ERROR,
        DrmInfoStatus.STATUS_OK,
        // Add any additional DRM status codes here as needed
    };
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

public static void testInvalidInfoTypes() throws Exception {
    for (int type : getAllDrmInfoRequestTypes()) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}

//<End of snippet n. 2>