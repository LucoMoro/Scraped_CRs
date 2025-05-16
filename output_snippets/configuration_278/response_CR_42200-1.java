//<Beginning of snippet n. 0>
public class DrmInfoRequest {
    public static final int TYPE_REGISTRATION_INFO = 0;
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 1;
    public static final int TYPE_OTHER_INFO = 2; // additional type
    // Add more constants as needed
}

public static void testInvalidInfoTypes() throws Exception {
    for (int invalidType = -1; invalidType <= 100; invalidType++) {
        checkInvalidInfoType(invalidType);
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public class DrmInfoStatus {
    public static final int STATUS_OK = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_OTHER = 2; // additional status
    // Add more constants as needed
}

public static void testInvalidStatusCodes() throws Exception {
    for (int invalidStatus = -1; invalidStatus <= 100; invalidStatus++) {
        checkInvalidStatusCode(invalidStatus);
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public class DrmInfoRequest {
    public static final int TYPE_REGISTRATION_INFO = 0;
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 1;
    public static final int TYPE_OTHER_INFO = 2; // additional type
    // Add more constants as needed
}

public static void testInvalidInfoTypes() throws Exception {
    for (int invalidType = -1; invalidType <= 100; invalidType++) {
        checkInvalidInfoType(invalidType);
    }
}

//<End of snippet n. 2>