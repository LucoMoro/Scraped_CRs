//<Beginning of snippet n. 0>
public static final int TYPE_REGISTRATION_INFO = 1;
public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 2;
public static final int TYPE_OEM_DRM_INFO_1 = 3; // New OEM DRM Type
public static final int TYPE_OEM_DRM_INFO_2 = 4; // New OEM DRM Type

public static void testInvalidInfoTypes() throws Exception {
    checkInvalidInfoType(TYPE_REGISTRATION_INFO - 1);
    checkInvalidInfoType(TYPE_REGISTRATION_INFO + 1);
    checkInvalidInfoType(TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO - 1);
    checkInvalidInfoType(TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_1 - 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_1 + 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_2 - 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_2 + 1);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static final int STATUS_OK = 0;
public static final int STATUS_ERROR = 1;
public static final int STATUS_PENDING = 2; // New Status Code

public static void testInvalidStatusCodes() throws Exception {
    checkInvalidStatusCode(STATUS_ERROR + 1);
    checkInvalidStatusCode(STATUS_OK - 1);
    checkInvalidStatusCode(STATUS_PENDING - 1);
    checkInvalidStatusCode(STATUS_PENDING + 1);
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public static final int TYPE_REGISTRATION_INFO = 1;
public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 2;
public static final int TYPE_OEM_DRM_INFO_1 = 3;
public static final int TYPE_OEM_DRM_INFO_2 = 4;

public static void testInvalidInfoTypes() throws Exception {
    checkInvalidInfoType(TYPE_REGISTRATION_INFO - 1);
    checkInvalidInfoType(TYPE_REGISTRATION_INFO + 1);
    checkInvalidInfoType(TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO - 1);
    checkInvalidInfoType(TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_1 - 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_1 + 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_2 - 1);
    checkInvalidInfoType(TYPE_OEM_DRM_INFO_2 + 1);
}
//<End of snippet n. 2>