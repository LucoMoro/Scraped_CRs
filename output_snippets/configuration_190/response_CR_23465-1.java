//<Beginning of snippet n. 0>


// the extra command parameter names in the Bundle
public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
public static final String NI_EXTRA_CMD_RESPONSE = "response";
public static final String NI_EXTRA_CMD_ADDRESS = "address"; // Added address parameter

// these need to match GpsNiType constants in gps_ni.h
public static final int GPS_NI_TYPE_VOICE = 1;

String address = decodeString(notif.address, mIsHexInput, notif.addressEncoding); // Decode address

if (address == null || address.trim().isEmpty() || !isValidAddress(address)) {
    address = "Address not provided"; // Handle empty or invalid address
}

String message = String.format(context.getString(R.string.gpsNotifMessage),
    decodeString(notif.requestorId, mIsHexInput, notif.requestorIdEncoding),
    decodeString(notif.text, mIsHexInput, notif.textEncoding),
    address); // Include address in message construction
return message;

private boolean isValidAddress(String address) {
    // Implement comprehensive validation logic here
    return address.length() <= 100 && !address.contains("<") && !address.contains(">");
}

//<End of snippet n. 0>








//<Beginning of snippet n. 1>


Log.i(TAG, "requestorId: " + requestorId +
", text: " + text +
", requestorIdEncoding: " + requestorIdEncoding +
                ", textEncoding: " + textEncoding +
                ", address: " + (address.isEmpty() ? "Address not provided" : "REDACTED")); // Log address with redaction

GpsNiNotification notification = new GpsNiNotification();
notification.setAddress(address); // Set address in notification
//<End of snippet n. 1>