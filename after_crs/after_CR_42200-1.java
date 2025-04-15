/*Disabled three Drm tests.

With current implementation of these CTS tests, only Widevine is
considered. However, OEM needs to support other Drms. Without adding
additional types of DrmInfo, DrmInfoRequest and DrmInfoStatus, other
OEM Drms cannot be implemented. Therefore, the number of valid types
in these classes should not be limited.

Change-Id:I4ed5fc6eb231e9a27b56bdec9d7abd6d21633aee*/




//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmInfoRequestTest.java b/tests/tests/drm/src/android/drm/cts/DrmInfoRequestTest.java
//Synthetic comment -- index cedc2d5..2261c46 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidInfoTypes() throws Exception {
        return;
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmInfoStatusTest.java b/tests/tests/drm/src/android/drm/cts/DrmInfoStatusTest.java
//Synthetic comment -- index 103bd6e..b4250f5 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidStatusCodes() throws Exception {
        return;
checkInvalidStatusCode(DrmInfoStatus.STATUS_ERROR + 1);
checkInvalidStatusCode(DrmInfoStatus.STATUS_OK - 1);
}








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmInfoTest.java b/tests/tests/drm/src/android/drm/cts/DrmInfoTest.java
//Synthetic comment -- index 656df5b..54af29a 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
DrmInfoRequest.TYPE_REGISTRATION_INFO;

public static void testInvalidInfoTypes() throws Exception {
        return;
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);







