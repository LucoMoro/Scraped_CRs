
//<Beginning of snippet n. 0>


DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidInfoTypes() throws Exception {
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidStatusCodes() throws Exception {
checkInvalidStatusCode(DrmInfoStatus.STATUS_ERROR + 1);
checkInvalidStatusCode(DrmInfoStatus.STATUS_OK - 1);
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidInfoTypes() throws Exception {
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);

//<End of snippet n. 2>








