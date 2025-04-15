/*Ccat: Initial CCAT implementation

Launch Stk service for Cdma phone.

Change-Id:I1d0e00a3a31af59239b1355babb37f05cb7e5e07*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 0c591e4..40c3b4e 100755

//Synthetic comment -- @@ -49,6 +49,7 @@
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -109,7 +110,7 @@
PhoneSubInfo mSubInfo;
EriManager mEriManager;
WakeLock mWakeLock;


// mNvLoadedRegistrants are informed after the EVENT_NV_READY
private RegistrantList mNvLoadedRegistrants = new RegistrantList();
//Synthetic comment -- @@ -161,6 +162,8 @@
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mRuimRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
//Synthetic comment -- @@ -236,6 +239,7 @@
mRuimSmsInterfaceManager.dispose();
mSubInfo.dispose();
mEriManager.dispose();
}
}

//Synthetic comment -- @@ -251,6 +255,7 @@
this.mCT = null;
this.mSST = null;
this.mEriManager = null;
}

protected void finalize() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java
//Synthetic comment -- index ce4c459..2364387 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.Message;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.SIMFileHandler;

import java.util.Iterator;
import java.util.List;
//Synthetic comment -- @@ -53,7 +53,7 @@
static final int REFRESH_UICC_RESET                     = 0x04;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
            SIMFileHandler fh) {
if (sInstance != null) {
return sInstance;
}
//Synthetic comment -- @@ -63,7 +63,7 @@
return null;
}

    private CommandParamsFactory(RilMessageDecoder caller, SIMFileHandler fh) {
mCaller = caller;
mIconLoader = IconLoader.getInstance(this, fh);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/IconLoader.java b/telephony/java/com/android/internal/telephony/gsm/stk/IconLoader.java
//Synthetic comment -- index fc02d2a..500b8f6 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.gsm.stk;

import com.android.internal.telephony.gsm.SIMFileHandler;

import android.graphics.Bitmap;
import android.graphics.Color;
//Synthetic comment -- @@ -40,7 +40,7 @@
private ImageDescriptor mId = null;
private Bitmap mCurrentIcon = null;
private int mRecordNumber;
    private SIMFileHandler mSimFH = null;
private Message mEndMsg = null;
private byte[] mIconData = null;
// multi icons state members
//Synthetic comment -- @@ -68,14 +68,14 @@
private static final int CLUT_ENTRY_SIZE = 3;


    private IconLoader(Looper looper , SIMFileHandler fh) {
super(looper);
mSimFH = fh;

mIconsCache = new HashMap<Integer, Bitmap>(50);
}

    static IconLoader getInstance(Handler caller, SIMFileHandler fh) {
if (sLoader != null) {
return sLoader;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/RilMessageDecoder.java b/telephony/java/com/android/internal/telephony/gsm/stk/RilMessageDecoder.java
//Synthetic comment -- index a82177c..02852cc 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.gsm.stk;

import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.IccUtils;

import android.os.Handler;
//Synthetic comment -- @@ -51,7 +51,7 @@
* @param fh
* @return RilMesssageDecoder
*/
    public static synchronized RilMessageDecoder getInstance(Handler caller, SIMFileHandler fh) {
if (sInstance == null) {
sInstance = new RilMessageDecoder(caller, fh);
sInstance.start();
//Synthetic comment -- @@ -90,7 +90,7 @@
msg.sendToTarget();
}

    private RilMessageDecoder(Handler caller, SIMFileHandler fh) {
super("RilMessageDecoder");

addState(mStateStart);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java b/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java
//Synthetic comment -- index 29ed95c..5efa7a6 100644

//Synthetic comment -- @@ -25,9 +25,9 @@

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.gsm.SimCard;
import com.android.internal.telephony.gsm.SIMFileHandler;
import com.android.internal.telephony.gsm.SIMRecords;

import android.util.Config;

//Synthetic comment -- @@ -114,7 +114,7 @@
public class StkService extends Handler implements AppInterface {

// Class members
    private static SIMRecords mSimRecords;

// Service members.
private static StkService sInstance;
//Synthetic comment -- @@ -136,7 +136,7 @@
static final int MSG_ID_RIL_MSG_DECODED          = 10;

// Events to signal SIM presence or absent in the device.
    private static final int MSG_ID_SIM_LOADED       = 20;

private static final int DEV_ID_KEYPAD      = 0x01;
private static final int DEV_ID_DISPLAY     = 0x02;
//Synthetic comment -- @@ -146,10 +146,10 @@
private static final int DEV_ID_NETWORK     = 0x83;

/* Intentionally private for singleton */
    private StkService(CommandsInterface ci, SIMRecords sr, Context context,
            SIMFileHandler fh, SimCard sc) {
        if (ci == null || sr == null || context == null || fh == null
                || sc == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
}
//Synthetic comment -- @@ -166,17 +166,17 @@
mCmdIf.setOnStkCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

        mSimRecords = sr;

// Register for SIM ready event.
        mSimRecords.registerForRecordsLoaded(this, MSG_ID_SIM_LOADED, null);

mCmdIf.reportStkServiceIsRunning(null);
StkLog.d(this, "StkService: is running");
}

public void dispose() {
        mSimRecords.unregisterForRecordsLoaded(this);
mCmdIf.unSetOnStkSessionEnd(this);
mCmdIf.unSetOnStkProactiveCmd(this);
mCmdIf.unSetOnStkEvent(this);
//Synthetic comment -- @@ -446,32 +446,32 @@
}

/**
     * Used for instantiating/updating the Service from the GsmPhone constructor.
*
* @param ci CommandsInterface object
     * @param sr SIMRecords object
* @param context phone app context
     * @param fh SIM file handler
     * @param sc GSM SIM card
* @return The only Service object in the system
*/
    public static StkService getInstance(CommandsInterface ci, SIMRecords sr,
            Context context, SIMFileHandler fh, SimCard sc) {
if (sInstance == null) {
            if (ci == null || sr == null || context == null || fh == null
                    || sc == null) {
return null;
}
HandlerThread thread = new HandlerThread("Stk Telephony service");
thread.start();
            sInstance = new StkService(ci, sr, context, fh, sc);
StkLog.d(sInstance, "NEW sInstance");
        } else if ((sr != null) && (mSimRecords != sr)) {
StkLog.d(sInstance, "Reinitialize the Service with SIMRecords");
            mSimRecords = sr;

// re-Register for SIM ready event.
            mSimRecords.registerForRecordsLoaded(sInstance, MSG_ID_SIM_LOADED, null);
StkLog.d(sInstance, "sr changed reinitialize and return current sInstance");
} else {
StkLog.d(sInstance, "Return current sInstance");
//Synthetic comment -- @@ -513,7 +513,7 @@
case MSG_ID_CALL_SETUP:
mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, null));
break;
        case MSG_ID_SIM_LOADED:
break;
case MSG_ID_RIL_MSG_DECODED:
handleRilMsg((RilMessage) msg.obj);







