/*Ccat: Initial CCAT implementation

Launch Stk service for Cdma phone.

Change-Id:I1d0e00a3a31af59239b1355babb37f05cb7e5e07*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 0c591e4..40c3b4e 100755

//Synthetic comment -- @@ -49,6 +49,7 @@
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.gsm.stk.StkService;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccException;
import com.android.internal.telephony.IccFileHandler;
//Synthetic comment -- @@ -109,7 +110,7 @@
PhoneSubInfo mSubInfo;
EriManager mEriManager;
WakeLock mWakeLock;
    StkService mStkService;

// mNvLoadedRegistrants are informed after the EVENT_NV_READY
private RegistrantList mNvLoadedRegistrants = new RegistrantList();
//Synthetic comment -- @@ -161,6 +162,8 @@
mRuimSmsInterfaceManager = new RuimSmsInterfaceManager(this);
mSubInfo = new PhoneSubInfo(this);
mEriManager = new EriManager(this, context, EriManager.ERI_FROM_XML);
        mStkService = StkService.getInstance(mCM, mRuimRecords, mContext,
                mIccFileHandler, mRuimCard);

mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
mRuimRecords.registerForRecordsLoaded(this, EVENT_RUIM_RECORDS_LOADED, null);
//Synthetic comment -- @@ -236,6 +239,7 @@
mRuimSmsInterfaceManager.dispose();
mSubInfo.dispose();
mEriManager.dispose();
            mStkService.dispose();
}
}

//Synthetic comment -- @@ -251,6 +255,7 @@
this.mCT = null;
this.mSST = null;
this.mEriManager = null;
            this.mStkService = null;
}

protected void finalize() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java
//Synthetic comment -- index ce4c459..2364387 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import android.os.Message;

import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccFileHandler;

import java.util.Iterator;
import java.util.List;
//Synthetic comment -- @@ -53,7 +53,7 @@
static final int REFRESH_UICC_RESET                     = 0x04;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
            IccFileHandler fh) {
if (sInstance != null) {
return sInstance;
}
//Synthetic comment -- @@ -63,7 +63,7 @@
return null;
}

    private CommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh) {
mCaller = caller;
mIconLoader = IconLoader.getInstance(this, fh);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/IconLoader.java b/telephony/java/com/android/internal/telephony/gsm/stk/IconLoader.java
//Synthetic comment -- index fc02d2a..500b8f6 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.gsm.stk;

import com.android.internal.telephony.IccFileHandler;

import android.graphics.Bitmap;
import android.graphics.Color;
//Synthetic comment -- @@ -40,7 +40,7 @@
private ImageDescriptor mId = null;
private Bitmap mCurrentIcon = null;
private int mRecordNumber;
    private IccFileHandler mSimFH = null;
private Message mEndMsg = null;
private byte[] mIconData = null;
// multi icons state members
//Synthetic comment -- @@ -68,14 +68,14 @@
private static final int CLUT_ENTRY_SIZE = 3;


    private IconLoader(Looper looper , IccFileHandler fh) {
super(looper);
mSimFH = fh;

mIconsCache = new HashMap<Integer, Bitmap>(50);
}

    static IconLoader getInstance(Handler caller, IccFileHandler fh) {
if (sLoader != null) {
return sLoader;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/RilMessageDecoder.java b/telephony/java/com/android/internal/telephony/gsm/stk/RilMessageDecoder.java
//Synthetic comment -- index a82177c..02852cc 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.internal.telephony.gsm.stk;

import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccUtils;

import android.os.Handler;
//Synthetic comment -- @@ -51,7 +51,7 @@
* @param fh
* @return RilMesssageDecoder
*/
    public static synchronized RilMessageDecoder getInstance(Handler caller, IccFileHandler fh) {
if (sInstance == null) {
sInstance = new RilMessageDecoder(caller, fh);
sInstance.start();
//Synthetic comment -- @@ -90,7 +90,7 @@
msg.sendToTarget();
}

    private RilMessageDecoder(Handler caller, IccFileHandler fh) {
super("RilMessageDecoder");

addState(mStateStart);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java b/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java
//Synthetic comment -- index 29ed95c..5efa7a6 100644

//Synthetic comment -- @@ -25,9 +25,9 @@

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;

import android.util.Config;

//Synthetic comment -- @@ -114,7 +114,7 @@
public class StkService extends Handler implements AppInterface {

// Class members
    private static IccRecords mIccRecords;

// Service members.
private static StkService sInstance;
//Synthetic comment -- @@ -136,7 +136,7 @@
static final int MSG_ID_RIL_MSG_DECODED          = 10;

// Events to signal SIM presence or absent in the device.
    private static final int MSG_ID_ICC_RECORDS_LOADED       = 20;

private static final int DEV_ID_KEYPAD      = 0x01;
private static final int DEV_ID_DISPLAY     = 0x02;
//Synthetic comment -- @@ -146,10 +146,10 @@
private static final int DEV_ID_NETWORK     = 0x83;

/* Intentionally private for singleton */
    private StkService(CommandsInterface ci, IccRecords ir, Context context,
            IccFileHandler fh, IccCard ic) {
        if (ci == null || ir == null || context == null || fh == null
                || ic == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
}
//Synthetic comment -- @@ -166,17 +166,17 @@
mCmdIf.setOnStkCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

        mIccRecords = ir;

// Register for SIM ready event.
        mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

mCmdIf.reportStkServiceIsRunning(null);
StkLog.d(this, "StkService: is running");
}

public void dispose() {
        mIccRecords.unregisterForRecordsLoaded(this);
mCmdIf.unSetOnStkSessionEnd(this);
mCmdIf.unSetOnStkProactiveCmd(this);
mCmdIf.unSetOnStkEvent(this);
//Synthetic comment -- @@ -446,32 +446,32 @@
}

/**
     * Used for instantiating/updating the Service from the GsmPhone or CdmaPhone constructor.
*
* @param ci CommandsInterface object
     * @param ir IccRecords object
* @param context phone app context
     * @param fh Icc file handler
     * @param ic Icc card
* @return The only Service object in the system
*/
    public static StkService getInstance(CommandsInterface ci, IccRecords ir,
            Context context, IccFileHandler fh, IccCard ic) {
if (sInstance == null) {
            if (ci == null || ir == null || context == null || fh == null
                    || ic == null) {
return null;
}
HandlerThread thread = new HandlerThread("Stk Telephony service");
thread.start();
            sInstance = new StkService(ci, ir, context, fh, ic);
StkLog.d(sInstance, "NEW sInstance");
        } else if ((ir != null) && (mIccRecords != ir)) {
StkLog.d(sInstance, "Reinitialize the Service with SIMRecords");
            mIccRecords = ir;

// re-Register for SIM ready event.
            mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
StkLog.d(sInstance, "sr changed reinitialize and return current sInstance");
} else {
StkLog.d(sInstance, "Return current sInstance");
//Synthetic comment -- @@ -513,7 +513,7 @@
case MSG_ID_CALL_SETUP:
mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, null));
break;
        case MSG_ID_ICC_RECORDS_LOADED:
break;
case MSG_ID_RIL_MSG_DECODED:
handleRilMsg((RilMessage) msg.obj);







