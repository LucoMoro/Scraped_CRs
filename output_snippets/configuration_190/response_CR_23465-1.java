//<Beginning of snippet n. 0>


// the extra command parameter names in the Bundle
public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
public static final String NI_EXTRA_CMD_RESPONSE = "response";

// these need to match GpsNiType constants in gps_ni.h
public static final int GPS_NI_TYPE_VOICE = 1;

String address = decodeString(notif.address, mIsHexInput, notif.addressEncoding);
if (address == null || address.isEmpty()) {
    throw new IllegalArgumentException("Address must not be null or empty");
}

String message = String.format(context.getString(R.string.gpsNotifMessage),
        decodeString(notif.requestorId, mIsHexInput, notif.requestorIdEncoding),
        decodeString(notif.text, mIsHexInput, notif.textEncoding), 
        address);
return message;
}       


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


Log.i(TAG, "requestorId: " + requestorId +
", text: " + text +
", requestorIdEncoding: " + requestorIdEncoding +
                ", textEncoding: " + textEncoding +
                ", address: " + address);

GpsNiNotification notification = new GpsNiNotification();
notification.setAddress(address);

//<End of snippet n. 1>