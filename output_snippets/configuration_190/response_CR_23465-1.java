//<Beginning of snippet n. 0>


// the extra command parameter names in the Bundle
public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
public static final String NI_EXTRA_CMD_RESPONSE = "response";
public static final String NI_EXTRA_CMD_ADDRESS = "address"; // Added address constant

// these need to match GpsNiType constants in gps_ni.h
public static final int GPS_NI_TYPE_VOICE = 1;

public String createNotificationMessage(Context context, Notif notif) {
    if (notif.address == null || notif.address.isEmpty()) {
        throw new IllegalArgumentException("Address cannot be null or empty.");
    }
    String message = String.format(context.getString(R.string.gpsNotifMessage),
            decodeString(notif.requestorId, mIsHexInput, notif.requestorIdEncoding),
            decodeString(notif.text, mIsHexInput, notif.textEncoding),
            decodeString(notif.address, mIsHexInput, notif.addressEncoding)); // Included address
    return message;
}       


//<End of snippet n. 0>





//<Beginning of snippet n. 1>


Log.i(TAG, "requestorId: " + requestorId +
        ", text: " + text +
        ", requestorIdEncoding: " + requestorIdEncoding +
        ", textEncoding: " + textEncoding +
        ", address: " + address); // Included address in logging

public class GpsNiNotification {
    private String requestorId;
    private String text;
    private String address;
    private String requestorIdEncoding;
    private String textEncoding;

    public GpsNiNotification(String requestorId, String text, String address, String requestorIdEncoding, String textEncoding) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.requestorId = requestorId;
        this.text = text;
        this.address = address;
        this.requestorIdEncoding = requestorIdEncoding;
        this.textEncoding = textEncoding;
    }
}

//<End of snippet n. 1>