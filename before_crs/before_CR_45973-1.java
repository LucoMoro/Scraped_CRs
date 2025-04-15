/*Telephony(DSDS): Changes to support Dual Stk.

These changes support Dual Stk feature.

TODO:
  Compile errors in CatService.java
  This change depends on other non dsds STK changes

Change-Id:I2bfb8866b94fc8becff5100134ddc8c30a9394ab*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCard.java b/src/java/com/android/internal/telephony/UiccCard.java
//Synthetic comment -- index 7be6f53..027d6e0 100644

//Synthetic comment -- @@ -80,10 +80,10 @@
private static final int EVENT_CARD_REMOVED = 13;
private static final int EVENT_CARD_ADDED = 14;

    public UiccCard(Context c, CommandsInterface ci, IccCardStatus ics) {
if (DBG) log("Creating");
mCardState = ics.mCardState;
        update(c, ci, ics);
}

public void dispose() {
//Synthetic comment -- @@ -100,7 +100,7 @@
}
}

    public void update(Context c, CommandsInterface ci, IccCardStatus ics) {
synchronized (mLock) {
if (mDestroyed) {
loge("Updated after destroyed! Fix me!");
//Synthetic comment -- @@ -133,16 +133,21 @@
}
}

            if (mUiccApplications.length > 0 && mUiccApplications[0] != null) {
                // Initialize or Reinitialize CatService
                mCatService = CatService.getInstance(mCi,
                                                     mContext,
                                                     this);
            } else {
                if (mCatService != null) {
                    mCatService.dispose();
}
                mCatService = null;
}

sanitizeApplicationIndexes();
//Synthetic comment -- @@ -359,6 +364,10 @@
return count;
}

private void log(String msg) {
Log.d(LOG_TAG, msg);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..eda81e5 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
//Synthetic comment -- @@ -70,15 +71,19 @@
// Service members.
// Protects singleton instance lazy initialization.
private static final Object sInstanceLock = new Object();
    private static CatService sInstance;
private CommandsInterface mCmdIf;
private Context mContext;
private CatCmdMessage mCurrntCmd = null;
private CatCmdMessage mMenuCmd = null;

private RilMessageDecoder mMsgDecoder = null;
private boolean mStkAppInstalled = false;

// Service constants.
static final int MSG_ID_SESSION_END              = 1;
static final int MSG_ID_PROACTIVE_COMMAND        = 2;
//Synthetic comment -- @@ -86,7 +91,6 @@
static final int MSG_ID_CALL_SETUP               = 4;
static final int MSG_ID_REFRESH                  = 5;
static final int MSG_ID_RESPONSE                 = 6;
    static final int MSG_ID_SIM_READY                = 7;

static final int MSG_ID_RIL_MSG_DECODED          = 10;

//Synthetic comment -- @@ -103,18 +107,19 @@
static final String STK_DEFAULT = "Defualt Message";

/* Intentionally private for singleton */
    private CatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir,
            Context context, IccFileHandler fh, UiccCard ic) {
        if (ci == null || ca == null || ir == null || context == null || fh == null
                || ic == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
}
mCmdIf = ci;
mContext = context;

// Get the RilMessagesDecoder for decoding the messages.
        mMsgDecoder = RilMessageDecoder.getInstance(this, fh);

// Register ril events handling.
mCmdIf.setOnCatSessionEnd(this, MSG_ID_SESSION_END, null);
//Synthetic comment -- @@ -123,17 +128,11 @@
mCmdIf.setOnCatCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

        mIccRecords = ir;
        mUiccApplication = ca;

        // Register for SIM ready event.
        mUiccApplication.registerForReady(this, MSG_ID_SIM_READY, null);
        mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

// Check if STK application is availalbe
mStkAppInstalled = isStkAppInstalled();

        CatLog.d(this, "Running CAT service. STK app installed:" + mStkAppInstalled);
}

public void dispose() {
//Synthetic comment -- @@ -142,10 +141,49 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);

this.removeCallbacksAndMessages(null);
}

protected void finalize() {
CatLog.d(this, "Service finalized");
}
//Synthetic comment -- @@ -331,6 +369,8 @@
mCurrntCmd = cmdMsg;
Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
intent.putExtra("STK CMD", cmdMsg);
mContext.sendBroadcast(intent);
}

//Synthetic comment -- @@ -339,10 +379,11 @@
*
*/
private void handleSessionEnd() {
        CatLog.d(this, "SESSION END");

mCurrntCmd = mMenuCmd;
Intent intent = new Intent(AppInterface.CAT_SESSION_END_ACTION);
mContext.sendBroadcast(intent);
}

//Synthetic comment -- @@ -549,77 +590,11 @@
mCmdIf.sendEnvelope(hexString, null);
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
    public static CatService getInstance(CommandsInterface ci,
            Context context, UiccCard ic) {
        UiccCardApplication ca = null;
        IccFileHandler fh = null;
        IccRecords ir = null;
        if (ic != null) {
            /* Since Cat is not tied to any application, but rather is Uicc application
             * in itself - just get first FileHandler and IccRecords object
             */
            ca = ic.getApplicationIndex(0);
            if (ca != null) {
                fh = ca.getIccFileHandler();
                ir = ca.getIccRecords();
            }
        }
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                if (ci == null || ca == null || ir == null || context == null || fh == null
                        || ic == null) {
                    return null;
                }
                HandlerThread thread = new HandlerThread("Cat Telephony service");
                thread.start();
                sInstance = new CatService(ci, ca, ir, context, fh, ic);
                CatLog.d(sInstance, "NEW sInstance");
            } else if ((ir != null) && (mIccRecords != ir)) {
                if (mIccRecords != null) {
                    mIccRecords.unregisterForRecordsLoaded(sInstance);
                }

                if (mUiccApplication != null) {
                    mUiccApplication.unregisterForReady(sInstance);
                }
                CatLog.d(sInstance,
                        "Reinitialize the Service with SIMRecords and UiccCardApplication");
                mIccRecords = ir;
                mUiccApplication = ca;

                // re-Register for SIM ready event.
                mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
                mUiccApplication.registerForReady(sInstance, MSG_ID_SIM_READY, null);
                CatLog.d(sInstance, "sr changed reinitialize and return current sInstance");
            } else {
                CatLog.d(sInstance, "Return current sInstance");
            }
            return sInstance;
        }
    }

    /**
     * Used by application to get an AppInterface object.
     *
     * @return The only Service object in the system
     */
    public static AppInterface getInstance() {
        return getInstance(null, null, null);
    }

@Override
public void handleMessage(Message msg) {

switch (msg.what) {
case MSG_ID_SESSION_END:
case MSG_ID_PROACTIVE_COMMAND:
//Synthetic comment -- @@ -633,14 +608,17 @@
try {
data = (String) ar.result;
} catch (ClassCastException e) {
break;
}
}
}
            mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, data));
break;
case MSG_ID_CALL_SETUP:
            mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, null));
break;
case MSG_ID_ICC_RECORDS_LOADED:
break;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..a5f9e9c 100644

//Synthetic comment -- @@ -56,20 +56,18 @@
static final int DTTZ_SETTING                           = 0x03;
static final int LANGUAGE_SETTING                       = 0x04;

    static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
            IccFileHandler fh) {
if (sInstance != null) {
return sInstance;
}
if (fh != null) {
            return new CommandParamsFactory(caller, fh);
}
return null;
}

    private CommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh) {
mCaller = caller;
        mIconLoader = IconLoader.getInstance(this, fh);
}

private CommandDetails processCommandDetails(List<ComprehensionTlv> ctlvs) {
//Synthetic comment -- @@ -91,11 +89,13 @@
return cmdDet;
}

    void make(BerTlv berTlv) {
if (berTlv == null) {
return;
}
// reset global state parameters.
mCmdParams = null;
mIconLoadState = LOAD_NO_ICON;
// only proactive command messages are processed.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/IconLoader.java b/src/java/com/android/internal/telephony/cat/IconLoader.java
//Synthetic comment -- index 2fa1811..3997e65 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
*/
class IconLoader extends Handler {
// members
private int mState = STATE_SINGLE_ICON;
private ImageDescriptor mId = null;
private Bitmap mCurrentIcon = null;
//Synthetic comment -- @@ -49,8 +50,6 @@
private Bitmap[] mIcons = null;
private HashMap<Integer, Bitmap> mIconsCache = null;

    private static IconLoader sLoader = null;

// Loader state values.
private static final int STATE_SINGLE_ICON = 1;
private static final int STATE_MULTI_ICONS = 2;
//Synthetic comment -- @@ -68,25 +67,16 @@
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
        if (fh != null) {
            HandlerThread thread = new HandlerThread("Cat Icon Loader");
            thread.start();
            return new IconLoader(thread.getLooper(), fh);
        }
        return null;
    }

void loadIcons(int[] recordNumbers, Message msg) {
if (recordNumbers == null || recordNumbers.length == 0 || msg == null) {
return;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/RilMessageDecoder.java b/src/java/com/android/internal/telephony/cat/RilMessageDecoder.java
//Synthetic comment -- index fb33a8e..e2ed37c 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
private CommandParamsFactory mCmdParamsFactory = null;
private RilMessage mCurrentRilMessage = null;
private Handler mCaller = null;

// States
private StateStart mStateStart = new StateStart();
//Synthetic comment -- @@ -47,13 +48,12 @@
/**
* Get the singleton instance, constructing if necessary.
*
     * @param caller
     * @param fh
* @return RilMesssageDecoder
*/
    public static synchronized RilMessageDecoder getInstance(Handler caller, IccFileHandler fh) {
if (sInstance == null) {
            sInstance = new RilMessageDecoder(caller, fh);
sInstance.start();
}
return sInstance;
//Synthetic comment -- @@ -65,7 +65,10 @@
*
* @param rilMsg
*/
    public void sendStartDecodingMessageParams(RilMessage rilMsg) {
Message msg = obtainMessage(CMD_START);
msg.obj = rilMsg;
sendMessage(msg);
//Synthetic comment -- @@ -90,7 +93,7 @@
msg.sendToTarget();
}

    private RilMessageDecoder(Handler caller, IccFileHandler fh) {
super("RilMessageDecoder");

addState(mStateStart);
//Synthetic comment -- @@ -98,7 +101,7 @@
setInitialState(mStateStart);

mCaller = caller;
        mCmdParamsFactory = CommandParamsFactory.getInstance(this, fh);
}

private class StateStart extends State {
//Synthetic comment -- @@ -158,7 +161,7 @@
}
try {
// Start asynch parsing of the command parameters.
                mCmdParamsFactory.make(BerTlv.decode(rawData));
decodingStarted = true;
} catch (ResultException e) {
// send to Service for proper RIL communication.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index c66aef6..dc6d529 100644

//Synthetic comment -- @@ -285,10 +285,10 @@

if (mUiccCards[index] == null) {
//Create new card
            mUiccCards[index] = new UiccCard(mContext, mCi[index], status);
} else {
//Update already existing card
            mUiccCards[index].update(mContext, mCi[index] , status);
}

if (DBG) log("Notifying IccChangedRegistrants");







