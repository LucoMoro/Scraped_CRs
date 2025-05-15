
//<Beginning of snippet n. 0>


// the extra command parameter names in the Bundle
public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
public static final String NI_EXTRA_CMD_RESPONSE = "response";

    // the extra fields to be displayed
	public static final String NI_NOTIFY_ADDR_KEY = "Address";

// these need to match GpsNiType constants in gps_ni.h
public static final int GPS_NI_TYPE_VOICE = 1;
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


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


Log.i(TAG, "requestorId: " + requestorId +
", text: " + text +
", requestorIdEncoding: " + requestorIdEncoding +
                ", textEncoding: " + textEncoding +
                ", extras: " + extras);

GpsNiNotification notification = new GpsNiNotification();


//<End of snippet n. 1>








