/*Fix AT+VTS=n DTMF tones for BT handsfree on CDMA networks.

When calling voice mail or bank using a BT carkit with numeric keypad.
When pressing 0-9,#,* in a voice menu the AT+VTS=n command is sent
from BT carkit to the device. For CDMA no DTMF are sent over the network
and no tones are generated. For UMTS it sends a correct DTMF command,
but still no tones are generated.
For CDMA phone the AT+VTS=n command handler should send correct
DTMF commands to the network by using correct method. And
for both phones CDMA and UMTS we should generate local DTMF tones

Change-Id:Ib461cafa50cc53d2940e118b5c6751ef8affe0d9*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 0c01301..ab43e40 100755

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
//Synthetic comment -- @@ -44,6 +45,7 @@
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
//Synthetic comment -- @@ -58,6 +60,7 @@
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
* Bluetooth headset manager for the Phone app.
//Synthetic comment -- @@ -176,6 +179,35 @@

private HandsfreeMessageHandler mHandler;

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
//Synthetic comment -- @@ -2265,8 +2297,12 @@
}
});

        // Send DTMF. I don't know if we are also expected to play the DTMF tone
        // locally, right now we don't
parser.register("+VTS", new AtCommandHandler() {
@Override
public AtCommandResult handleSetCommand(Object[] args) {
//Synthetic comment -- @@ -2278,7 +2314,18 @@
c = ((String) args[0]).charAt(0);
}
if (isValidDtmf(c)) {
                        phone.sendDtmf(c);
return new AtCommandResult(AtCommandResult.OK);
}
}
//Synthetic comment -- @@ -2712,6 +2759,16 @@
}
}

private static final int START_CALL_TIMEOUT = 10000;  // ms

private synchronized void expectCallStart() {
//Synthetic comment -- @@ -2731,6 +2788,19 @@
mStartCallWakeLock.release();
}
}
}

private static final int START_VOICE_RECOGNITION_TIMEOUT = 5000;  // ms







