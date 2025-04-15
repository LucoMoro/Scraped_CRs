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
//Synthetic comment -- index 92caffe..019b3ce 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
//Synthetic comment -- @@ -40,6 +41,7 @@
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
//Synthetic comment -- @@ -52,6 +54,7 @@
import com.android.internal.telephony.CallManager;

import java.util.LinkedList;
import java.util.HashMap;

/**
* Bluetooth headset manager for the Phone app.
//Synthetic comment -- @@ -152,6 +155,35 @@
private static final int BRSF_HF_ENHANCED_CALL_STATUS = 1 <<  5;
private static final int BRSF_HF_ENHANCED_CALL_CONTROL = 1 << 6;

    // Tone Generator for DTMF local tones
    private ToneGenerator mToneGenerator;

    // indicate if we want to enable the DTMF tone playback.
    private boolean mDTMFToneEnabled;

    //  Short Dtmf tone duration
    private static final int DTMF_DURATION_MS = 120;

    /** Hash Map to map a character to a tone*/
    private static final HashMap<Character, Integer> mToneMap = new HashMap<Character, Integer>();

    /** Set up the static map*/
    static {
        // Map the key characters to tones
        mToneMap.put('1', ToneGenerator.TONE_DTMF_1);
        mToneMap.put('2', ToneGenerator.TONE_DTMF_2);
        mToneMap.put('3', ToneGenerator.TONE_DTMF_3);
        mToneMap.put('4', ToneGenerator.TONE_DTMF_4);
        mToneMap.put('5', ToneGenerator.TONE_DTMF_5);
        mToneMap.put('6', ToneGenerator.TONE_DTMF_6);
        mToneMap.put('7', ToneGenerator.TONE_DTMF_7);
        mToneMap.put('8', ToneGenerator.TONE_DTMF_8);
        mToneMap.put('9', ToneGenerator.TONE_DTMF_9);
        mToneMap.put('0', ToneGenerator.TONE_DTMF_0);
        mToneMap.put('#', ToneGenerator.TONE_DTMF_P);
        mToneMap.put('*', ToneGenerator.TONE_DTMF_S);
    }

public static String typeToString(int type) {
switch (type) {
case TYPE_UNKNOWN:
//Synthetic comment -- @@ -1818,8 +1850,12 @@
}
});

        // Send DTMF.
        // Generate and play the DTMF tone locally.
        // We are not handling AT+VTS command for long DTMF here
        // Therefore we always send short DTMF
        // Unlike long key press handled by DTMFTwelveKeyDialer
        // using "key down" and "key up" events
parser.register("+VTS", new AtCommandHandler() {
@Override
public AtCommandResult handleSetCommand(Object[] args) {
//Synthetic comment -- @@ -1831,7 +1867,18 @@
c = ((String) args[0]).charAt(0);
}
if (isValidDtmf(c)) {
                        playLocalDtmfTone(c);
                        // phone.sendDtmf() currently does not work for CDMA
                        // it takes a long time (up to 7 sec) for one DTMF tone
                        // and it fails for a sequence of quick DTMF tones
                        // Therefore we use CDMA specifics here
                        // the same way as DTMFTwelveKeyDialer does.
                        if (phone.getPhoneType() == Phone.PHONE_TYPE_CDMA) {
                            String dtmfStr = Character.toString(c);
                            phone.sendBurstDtmf(dtmfStr, 0, 0, null);
                        } else {
                            phone.sendDtmf(c);
                        }
return new AtCommandResult(AtCommandResult.OK);
}
}
//Synthetic comment -- @@ -2238,6 +2285,16 @@
}
}

    private void playLocalDtmfTone(char tone) {
        if (DBG) log("playLocalDtmfTone('" + tone + "')..."
                     + " mDTMFToneEnabled = " + mDTMFToneEnabled + " this = " + this);

        // if local tone playback is enabled, start it.
        if (mDTMFToneEnabled && mToneGenerator != null) {
            mToneGenerator.startTone(mToneMap.get(tone), DTMF_DURATION_MS);
        }
    }

private static final int START_CALL_TIMEOUT = 10000;  // ms

private synchronized void expectCallStart() {
//Synthetic comment -- @@ -2257,6 +2314,19 @@
mStartCallWakeLock.release();
}
}

        // see if we need to play DTMF local tones.
        if (PhoneApp.getInstance().getResources().getBoolean(R.bool.allow_local_dtmf_tones)) {
            mDTMFToneEnabled = Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
        } else {
            mDTMFToneEnabled = false;
        }
        if (DBG) log("mDTMFToneEnabled = " + mDTMFToneEnabled);

        if (mDTMFToneEnabled && mToneGenerator == null) {
            mToneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, 80);
        }
}

private static final int START_VOICE_RECOGNITION_TIMEOUT = 5000;  // ms







