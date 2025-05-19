//<Beginning of snippet n. 0>
public class GpsNiNotification {
    private String address;

    public GpsNiNotification(String address) {
        if (address == null || address.trim().isEmpty() || !isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address provided.");
        }
        this.address = address;
    }

    private boolean isValidAddress(String address) {
        return address.length() <= 255 && address.matches("^[a-zA-Z0-9.,\\s-]+$"); 
    }

    // other class methods...

    public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
    public static final String NI_EXTRA_CMD_RESPONSE = "response";
    public static final int GPS_NI_TYPE_VOICE = 1;

    public String createNotificationMessage(Context context, NotificationNotif notif, boolean mIsHexInput) {
        String address = decodeString(notif.address, mIsHexInput, notif.addressEncoding);
        if (address == null || address.trim().isEmpty() || !isValidAddress(address)) {
            throw new IllegalArgumentException("Address cannot be null, empty, or invalid.");
        }
        String message = String.format(context.getString(R.string.gpsNotifMessage),
            decodeString(notif.requestorId, mIsHexInput, notif.requestorIdEncoding),
            decodeString(notif.text, mIsHexInput, notif.textEncoding),
            address);
        return message;
    }       
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
Log.i(TAG, "requestorId: " + requestorId +
    ", text: " + text +
    ", requestorIdEncoding: " + requestorIdEncoding +
    ", textEncoding: " + textEncoding +
    ", address: " + (address != null ? "****" : "null")); // Masked for security

GpsNiNotification notification = new GpsNiNotification(address != null ? address : ""); // Ensure address is initialized with empty string if null
//<End of snippet n. 1>