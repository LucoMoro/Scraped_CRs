//<Beginning of snippet n. 0>
public static void testInvalidInfoTypes() throws Exception {
    for (int i : DrmInfoRequest.getValidInfoTypes()) {
        checkInvalidInfoType(i);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static void testInvalidStatusCodes() throws Exception {
    for (int i : DrmInfoStatus.getValidStatusCodes()) {
        checkInvalidStatusCode(i);
    }
}
//<End of snippet n. 1>