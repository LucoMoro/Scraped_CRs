/*Added test cases for a new vendor specific DRM type

Change-Id:Iaea1bf4c38e554eee053e52ca1241715a8780312*/
//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmEventTest.java b/tests/tests/drm/src/android/drm/cts/DrmEventTest.java
//Synthetic comment -- index 4df432f..ac80074 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
checkValidErrorType(DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED);
checkValidErrorType(DrmErrorEvent.TYPE_REMOVE_ALL_RIGHTS_FAILED);
checkValidErrorType(DrmErrorEvent.TYPE_ACQUIRE_DRM_INFO_FAILED);
}

public static void testValidInfoEventTypes() throws Exception {
//Synthetic comment -- @@ -101,6 +102,7 @@
DrmInfoEvent.TYPE_ACCOUNT_ALREADY_REGISTERED);

checkValidInfoType(DrmInfoEvent.TYPE_RIGHTS_REMOVED);


// DrmEvent should be just DrmInfoEvent
//Synthetic comment -- @@ -121,6 +123,7 @@
DrmInfoEvent.TYPE_ACCOUNT_ALREADY_REGISTERED);

checkInfoTypeInErrorEvent(DrmInfoEvent.TYPE_RIGHTS_REMOVED);


// DrmEvent should be just DrmInfoEvent
//Synthetic comment -- @@ -140,6 +143,7 @@
checkErrorTypeInInfoEvent(DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED);
checkErrorTypeInInfoEvent(DrmErrorEvent.TYPE_REMOVE_ALL_RIGHTS_FAILED);
checkErrorTypeInInfoEvent(DrmErrorEvent.TYPE_ACQUIRE_DRM_INFO_FAILED);
}

private static DrmEvent createDrmEvent(








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmInfoRequestTest.java b/tests/tests/drm/src/android/drm/cts/DrmInfoRequestTest.java
//Synthetic comment -- index cedc2d5..39cad0c 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
public static void testInvalidInfoTypes() throws Exception {
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
                DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);
}

public static void testValidInfoTypes() throws Exception {
//Synthetic comment -- @@ -41,6 +41,7 @@
checkValidInfoType(DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO);
checkValidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO);
}

public static void testGetInfoType() throws Exception {
//Synthetic comment -- @@ -49,6 +50,7 @@
checkGetInfoType(DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO);
checkGetInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO);
}

public static void testInvalidMimeTypes() throws Exception {








//Synthetic comment -- diff --git a/tests/tests/drm/src/android/drm/cts/DrmInfoTest.java b/tests/tests/drm/src/android/drm/cts/DrmInfoTest.java
//Synthetic comment -- index 656df5b..2da5b55 100644

//Synthetic comment -- @@ -34,7 +34,7 @@
public static void testInvalidInfoTypes() throws Exception {
checkInvalidInfoType(DrmInfoRequest.TYPE_REGISTRATION_INFO - 1);
checkInvalidInfoType(
                DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1);
}

public static void testValidInfoTypes() throws Exception {
//Synthetic comment -- @@ -43,6 +43,7 @@
checkValidInfoType(DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO);
checkValidInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO);
}

public static void testGetInfoType() throws Exception {
//Synthetic comment -- @@ -51,6 +52,7 @@
checkGetInfoType(DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO);
checkGetInfoType(
DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO);
}

public static void testInvalidMimeTypes() throws Exception {







