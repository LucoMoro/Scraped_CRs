//<Beginning of snippet n. 0>
public void testInvalidInfoTypes() throws Exception {
    for (int type : DrmInfoRequest.getValidInfoTypes()) {
        checkInvalidInfoType(type - 1);
        checkInvalidInfoType(type + 1);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void testInvalidStatusCodes() throws Exception {
    for (int status : DrmInfoStatus.getValidStatusCodes()) {
        checkInvalidStatusCode(status + 1);
        checkInvalidStatusCode(status - 1);
    }
}
//<End of snippet n. 1>