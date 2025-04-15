/*GPS: Add "address" field to GPS NI notification

In some test cases, the LCS External Address needs to be displayed. The
address is added to the notification in addition to the existing client
name and requestor id.

Change-Id:I3358d29407e400568d65f65bbe51394fd2871a08*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsNetInitiatedHandler.java b/location/java/com/android/internal/location/GpsNetInitiatedHandler.java
//Synthetic comment -- index ffc3346..f679f98 100755

//Synthetic comment -- @@ -59,6 +59,9 @@
// the extra command parameter names in the Bundle
public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
public static final String NI_EXTRA_CMD_RESPONSE = "response";

    // the extra fields to be displayed
	public static final String NI_NOTIFY_ADDR_KEY = "Address";

// these need to match GpsNiType constants in gps_ni.h
public static final int GPS_NI_TYPE_VOICE = 1;
//Synthetic comment -- @@ -410,6 +413,21 @@
String message = String.format(context.getString(R.string.gpsNotifMessage),
decodeString(notif.requestorId, mIsHexInput, notif.requestorIdEncoding),
decodeString(notif.text, mIsHexInput, notif.textEncoding));
         // Extra fields to be displayed
        StringBuffer extraBuf = new StringBuffer();
        String addrString = notif.extras.getString(NI_NOTIFY_ADDR_KEY);

        if (addrString != null)
        {
            extraBuf.append(" ");
            extraBuf.append(NI_NOTIFY_ADDR_KEY);
            extraBuf.append(": [");
            extraBuf.append(addrString); // no decoding needed for the address
            extraBuf.append("]");
        }

        message += extraBuf.toString();

return message;
}       









//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index d7f0e49..9bb9ab0 100755

//Synthetic comment -- @@ -1407,7 +1407,8 @@
Log.i(TAG, "requestorId: " + requestorId +
", text: " + text +
", requestorIdEncoding: " + requestorIdEncoding +
                ", textEncoding: " + textEncoding +
                ", extras: " + extras);

GpsNiNotification notification = new GpsNiNotification();








