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

    public UiccCard(Context c, CommandsInterface ci, IccCardStatus ics, int slotId) {
if (DBG) log("Creating");
mCardState = ics.mCardState;
        update(c, ci, ics, slotId);
}

public void dispose() {
//Synthetic comment -- @@ -100,7 +100,7 @@
}
}

    public void update(Context c, CommandsInterface ci, IccCardStatus ics, int slotId) {
synchronized (mLock) {
if (mDestroyed) {
loge("Updated after destroyed! Fix me!");
//Synthetic comment -- @@ -133,16 +133,21 @@
}
}

            if (mCatService == null) {
                // Create CatService
                mCatService = new CatService(mCi, mContext, slotId);
            }

            if (mCatService != null) {
                if (mUiccApplications.length > 0 && mUiccApplications[0] != null) {
                    // Initialize or Reinitialize CatService
                    mCatService.update(mUiccApplications[0], mCardState);
                } else {
                    mCatService.update(null, mCardState);
}
            } else {
                // This is an error case.
                loge("CatService is null");
}

sanitizeApplicationIndexes();
//Synthetic comment -- @@ -359,6 +364,10 @@
return count;
}

    public CatService getCatService() {
        return mCatService;
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
import com.android.internal.telephony.IccCardStatus.CardState;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.UiccCard;
import com.android.internal.telephony.UiccCardApplication;
//Synthetic comment -- @@ -70,15 +71,19 @@
// Service members.
// Protects singleton instance lazy initialization.
private static final Object sInstanceLock = new Object();
    private HandlerThread mHandlerThread;
private CommandsInterface mCmdIf;
private Context mContext;
    private int mSlotId;
private CatCmdMessage mCurrntCmd = null;
private CatCmdMessage mMenuCmd = null;
    private IconLoader mIconLoader = null;

private RilMessageDecoder mMsgDecoder = null;
private boolean mStkAppInstalled = false;

    private CardState mCardState = CardState.CARDSTATE_ABSENT;

// Service constants.
static final int MSG_ID_SESSION_END              = 1;
static final int MSG_ID_PROACTIVE_COMMAND        = 2;
//Synthetic comment -- @@ -86,7 +91,6 @@
static final int MSG_ID_CALL_SETUP               = 4;
static final int MSG_ID_REFRESH                  = 5;
static final int MSG_ID_RESPONSE                 = 6;

static final int MSG_ID_RIL_MSG_DECODED          = 10;

//Synthetic comment -- @@ -103,18 +107,19 @@
static final String STK_DEFAULT = "Defualt Message";

/* Intentionally private for singleton */
    public CatService(CommandsInterface ci, Context context, int slotId) {
        if (ci == null || context == null) {
throw new NullPointerException(
"Service: Input parameters must not be null");
}
mCmdIf = ci;
mContext = context;
        mSlotId = slotId;
        mHandlerThread = new HandlerThread("Cat Telephony service" + slotId);
        mHandlerThread.start();

// Get the RilMessagesDecoder for decoding the messages.
        mMsgDecoder = RilMessageDecoder.getInstance();

// Register ril events handling.
mCmdIf.setOnCatSessionEnd(this, MSG_ID_SESSION_END, null);
//Synthetic comment -- @@ -123,17 +128,11 @@
mCmdIf.setOnCatCallSetUp(this, MSG_ID_CALL_SETUP, null);
//mCmdIf.setOnSimRefresh(this, MSG_ID_REFRESH, null);

// Check if STK application is availalbe
mStkAppInstalled = isStkAppInstalled();

        CatLog.d(this, "Running CAT service on Slotid: " + mSlotId +
                ". STK app installed:" + mStkAppInstalled);
}

public void dispose() {
//Synthetic comment -- @@ -142,10 +141,49 @@
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
mCmdIf.unSetOnCatCallSetUp(this);
        mHandlerThread.quit();
        mHandlerThread = null;

this.removeCallbacksAndMessages(null);
}

    public void update(UiccCardApplication ca, CardState newState) {

        CatLog.d(this,"New Card State = " + newState + " " + "Old Card State = " + mCardState);

        if (mCardState == CardState.CARDSTATE_PRESENT &&
            newState != CardState.CARDSTATE_PRESENT) {
            // Card moved to ABSENT state.
            broadcastCardStateAndIccRefreshResp(newState, null);
        } else if (mCardState != CardState.CARDSTATE_PRESENT &&
            newState == CardState.CARDSTATE_PRESENT) {
            // Card moved to PRESENT STATE.
            mCmdIf.reportStkServiceIsRunning(null);
        }
        mCardState = newState;

        IccRecords ir = null;
        IccFileHandler fh = null;
        mUiccApplication = ca;
        if (mUiccApplication != null) {
            fh = mUiccApplication.getIccFileHandler();
            ir = mUiccApplication.getIccRecords();
        }
        if ((ir != null) && (mIccRecords != ir)) {
            CatLog.d(this, "Reinitialize the Service with IccRecords");
            mIccRecords = ir;
        }

        if (fh != null) {
            CatLog.d(this, "Reinitialize the Service with new IccFilehandler");
            if (mIconLoader != null) {
                mIconLoader.updateIccFileHandler(fh);
            } else {
                mIconLoader = new IconLoader(fh, mSlotId);
            }
        }
    }

protected void finalize() {
CatLog.d(this, "Service finalized");
}
//Synthetic comment -- @@ -331,6 +369,8 @@
mCurrntCmd = cmdMsg;
Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
intent.putExtra("STK CMD", cmdMsg);
        intent.putExtra("SLOT_ID", mSlotId);
        CatLog.d(this, "Sending CmdMsg: "+cmdMsg+ " on slotid:"+ mSlotId);
mContext.sendBroadcast(intent);
}

//Synthetic comment -- @@ -339,10 +379,11 @@
*
*/
private void handleSessionEnd() {
        CatLog.d(this, "SESSION END on "+ mSlotId);

mCurrntCmd = mMenuCmd;
Intent intent = new Intent(AppInterface.CAT_SESSION_END_ACTION);
        intent.putExtra("SLOT_ID", mSlotId);
mContext.sendBroadcast(intent);
}

//Synthetic comment -- @@ -549,77 +590,11 @@
mCmdIf.sendEnvelope(hexString, null);
}

@Override
public void handleMessage(Message msg) {

        CatLog.d(this, msg.what + "arrived on slotid: "+ mSlotId);

switch (msg.what) {
case MSG_ID_SESSION_END:
case MSG_ID_PROACTIVE_COMMAND:
//Synthetic comment -- @@ -633,14 +608,17 @@
try {
data = (String) ar.result;
} catch (ClassCastException e) {
                        CatLog.d(this,"Exception caught for proactive cmd");
break;
}
}
}
            mMsgDecoder.sendStartDecodingMessageParams(this, mIconLoader,
                    new RilMessage(msg.what, data));
break;
case MSG_ID_CALL_SETUP:
            mMsgDecoder.sendStartDecodingMessageParams(this, mIconLoader,
                    new RilMessage(msg.what, null));
break;
case MSG_ID_ICC_RECORDS_LOADED:
break;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..a5f9e9c 100644

//Synthetic comment -- @@ -56,20 +56,18 @@
static final int DTTZ_SETTING                           = 0x03;
static final int LANGUAGE_SETTING                       = 0x04;

    static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller) {
if (sInstance != null) {
return sInstance;
}
if (fh != null) {
            return new CommandParamsFactory(caller);
}
return null;
}

    private CommandParamsFactory(RilMessageDecoder caller) {
mCaller = caller;
}

private CommandDetails processCommandDetails(List<ComprehensionTlv> ctlvs) {
//Synthetic comment -- @@ -91,11 +89,13 @@
return cmdDet;
}

    void make(BerTlv berTlv, IconLoader iconLoader) {
if (berTlv == null) {
return;
}
        CatLog.d(this, "IconLoader received: "+ iconLoader);
// reset global state parameters.
        mIconLoader = iconLoader;
mCmdParams = null;
mIconLoadState = LOAD_NO_ICON;
// only proactive command messages are processed.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/IconLoader.java b/src/java/com/android/internal/telephony/cat/IconLoader.java
//Synthetic comment -- index 2fa1811..3997e65 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
*/
class IconLoader extends Handler {
// members
    private int mSlotId;
private int mState = STATE_SINGLE_ICON;
private ImageDescriptor mId = null;
private Bitmap mCurrentIcon = null;
//Synthetic comment -- @@ -49,8 +50,6 @@
private Bitmap[] mIcons = null;
private HashMap<Integer, Bitmap> mIconsCache = null;

// Loader state values.
private static final int STATE_SINGLE_ICON = 1;
private static final int STATE_MULTI_ICONS = 2;
//Synthetic comment -- @@ -68,25 +67,16 @@
private static final int CLUT_ENTRY_SIZE = 3;


    public IconLoader(IccFileHandler fh, int slotId) {
        super();
        mSlotId = slotId;
        HandlerThread thread = new HandlerThread("Cat Icon Loader");
        thread.start();
mSimFH = fh;

mIconsCache = new HashMap<Integer, Bitmap>(50);
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
    private IconLoader mIconLoader = null;

// States
private StateStart mStateStart = new StateStart();
//Synthetic comment -- @@ -47,13 +48,12 @@
/**
* Get the singleton instance, constructing if necessary.
*
     * @param null
* @return RilMesssageDecoder
*/
    public static synchronized RilMessageDecoder getInstance() {
if (sInstance == null) {
            sInstance = new RilMessageDecoder();
sInstance.start();
}
return sInstance;
//Synthetic comment -- @@ -65,7 +65,10 @@
*
* @param rilMsg
*/
    public void sendStartDecodingMessageParams(Handler caller,
            IconLoader iconLoader, RilMessage rilMsg) {
        mCaller = caller;
        mIconLoader = iconLoader;
Message msg = obtainMessage(CMD_START);
msg.obj = rilMsg;
sendMessage(msg);
//Synthetic comment -- @@ -90,7 +93,7 @@
msg.sendToTarget();
}

    private RilMessageDecoder() {
super("RilMessageDecoder");

addState(mStateStart);
//Synthetic comment -- @@ -98,7 +101,7 @@
setInitialState(mStateStart);

mCaller = caller;
        mCmdParamsFactory = CommandParamsFactory.getInstance(this);
}

private class StateStart extends State {
//Synthetic comment -- @@ -158,7 +161,7 @@
}
try {
// Start asynch parsing of the command parameters.
                mCmdParamsFactory.make(BerTlv.decode(rawData), mIconLoader);
decodingStarted = true;
} catch (ResultException e) {
// send to Service for proper RIL communication.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index c66aef6..dc6d529 100644

//Synthetic comment -- @@ -285,10 +285,10 @@

if (mUiccCards[index] == null) {
//Create new card
            mUiccCards[index] = new UiccCard(mContext, mCi[index], status, index);
} else {
//Update already existing card
            mUiccCards[index].update(mContext, mCi[index] , status, index);
}

if (DBG) log("Notifying IccChangedRegistrants");







