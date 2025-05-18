//<Beginning of snippet n. 0>
public void testInvalidInfoTypes() throws Exception {
    List<Integer> validInfoTypes = DrmInfoRequest.getValidInfoTypes();
    for (int type : validInfoTypes) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testInvalidStatusCodes() throws Exception {
    List<Integer> validStatusCodes = DrmInfoStatus.getValidStatusCodes();
    for (int status : validStatusCodes) {
        checkInvalidStatusCode(status + 1);
        checkInvalidStatusCode(status - 1);
    }
}
//<End of snippet n. 1>