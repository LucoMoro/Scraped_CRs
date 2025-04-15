/*Added a vendor specific DRM event type

Added a vendor specific DRM event type. Vendors can now use
this togther with key/value information instead of adding
their own defined event types.

Change-Id:Ia805ff884a53424f020c2fa384b2746b3078dfe3*/




//Synthetic comment -- diff --git a/drm/java/android/drm/DrmErrorEvent.java b/drm/java/android/drm/DrmErrorEvent.java
//Synthetic comment -- index c61819d..0c8920e 100644

//Synthetic comment -- @@ -63,14 +63,10 @@
* {@link DrmManagerClient#acquireDrmInfo acquireDrmInfo()} call fails.
*/
public static final int TYPE_ACQUIRE_DRM_INFO_FAILED = 2008;
    /**
     * Vendor specific error occurs.
     */
    public static final int TYPE_VENDOR_SPECIFIC_ERROR = 2009;

/**
* Creates a <code>DrmErrorEvent</code> object with the specified parameters.
//Synthetic comment -- @@ -101,7 +97,7 @@

private void checkTypeValidity(int type) {
if (type < TYPE_RIGHTS_NOT_INSTALLED ||
            type > TYPE_VENDOR_SPECIFIC_ERROR) {
final String msg = "Unsupported type: " + type;
throw new IllegalArgumentException(msg);
}








//Synthetic comment -- diff --git a/drm/java/android/drm/DrmInfoEvent.java b/drm/java/android/drm/DrmInfoEvent.java
//Synthetic comment -- index 2826dce..b1c079f 100644

//Synthetic comment -- @@ -53,13 +53,10 @@
* The rights have been removed.
*/
public static final int TYPE_RIGHTS_REMOVED = 6;
    /**
     * DRM vendor specific event.
     */
    public static final int TYPE_VENDOR_SPECIFIC = 7;

/**
* Creates a <code>DrmInfoEvent</code> object with the specified parameters.
//Synthetic comment -- @@ -97,7 +94,7 @@
*/
private void checkTypeValidity(int type) {
if (type < TYPE_ALREADY_REGISTERED_BY_ANOTHER_ACCOUNT ||
            type > TYPE_VENDOR_SPECIFIC) {

if (type != TYPE_ALL_RIGHTS_REMOVED &&
type != TYPE_DRM_INFO_PROCESSED) {








//Synthetic comment -- diff --git a/drm/java/android/drm/DrmInfoRequest.java b/drm/java/android/drm/DrmInfoRequest.java
//Synthetic comment -- index 621da41..7d108d5 100644

//Synthetic comment -- @@ -43,6 +43,10 @@
* Acquires the progress of the rights acquisition.
*/
public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 4;
    /**
     * Acquires vendor specific information.
     */
    public static final int TYPE_VENDOR_SPECIFIC_INFO = 5;

/**
* Key that is used to pass the unique session ID for the account or the user.
//Synthetic comment -- @@ -154,6 +158,7 @@
case TYPE_UNREGISTRATION_INFO:
case TYPE_RIGHTS_ACQUISITION_INFO:
case TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO:
        case TYPE_VENDOR_SPECIFIC_INFO:
isValid = true;
break;
}








//Synthetic comment -- diff --git a/drm/java/android/drm/DrmManagerClient.java b/drm/java/android/drm/DrmManagerClient.java
//Synthetic comment -- index b78725e..91b19fe 100644

//Synthetic comment -- @@ -259,7 +259,8 @@
case DrmInfoEvent.TYPE_RIGHTS_INSTALLED:
case DrmInfoEvent.TYPE_WAIT_FOR_RIGHTS:
case DrmInfoEvent.TYPE_ACCOUNT_ALREADY_REGISTERED:
                case DrmInfoEvent.TYPE_RIGHTS_REMOVED:
                case DrmInfoEvent.TYPE_VENDOR_SPECIFIC: {
info = (DrmInfoEvent)msg.obj;
break;
}
//Synthetic comment -- @@ -832,6 +833,7 @@
case DrmInfoRequest.TYPE_REGISTRATION_INFO:
case DrmInfoRequest.TYPE_UNREGISTRATION_INFO:
case DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO:
        case DrmInfoRequest.TYPE_VENDOR_SPECIFIC_INFO:
eventType = DrmEvent.TYPE_DRM_INFO_PROCESSED;
break;
}
//Synthetic comment -- @@ -845,6 +847,7 @@
case DrmInfoRequest.TYPE_REGISTRATION_INFO:
case DrmInfoRequest.TYPE_UNREGISTRATION_INFO:
case DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO:
        case DrmInfoRequest.TYPE_VENDOR_SPECIFIC_INFO:
error = DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED;
break;
}







