/*Telephony: Usat Phase 2 feature and Screen status support

Added changes to support Icon, Idle screen, Language Selection.

This also includes change to support Idle screen status information

Change-Id:I087ea069fac6cc5fae446a8c406e0494481a6a4d*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 98ab17b..f60e4be 100644

//Synthetic comment -- @@ -88,6 +88,8 @@
static protected final int EVENT_READ_IMG_DONE = 9;
/** Finished retrieving icon data; post result. */
static protected final int EVENT_READ_ICON_DONE = 10;
    /** Finished retrieving size of record for EFimg now. */
    static protected final int EVENT_GET_RECORD_SIZE_IMG_DONE = 11;

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,7 +164,7 @@
*
*/
public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        Message response = obtainMessage(EVENT_GET_RECORD_SIZE_IMG_DONE,
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

//Synthetic comment -- @@ -304,6 +306,24 @@
response.sendToTarget();
}

    private boolean processException(Message response, AsyncResult ar) {
        IccException iccException;
        boolean flag = false;
        IccIoResult result = (IccIoResult) ar.result;

        if (ar.exception != null) {
            sendResult(response, null, ar.exception);
            flag = true;
        } else {
            iccException = result.getException();
            if (iccException != null) {
                sendResult(response, null, iccException);
                flag = true;
            }
        }
        return flag;
    }

//***** Overridden from Handler

public void handleMessage(Message msg) {
//Synthetic comment -- @@ -322,26 +342,60 @@

try {
switch (msg.what) {
            case EVENT_GET_RECORD_SIZE_IMG_DONE:
                logd("IccFileHandler: get record size img done");
ar = (AsyncResult) msg.obj;
lc = (LoadLinearFixedContext) ar.userObj;
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (processException(response, (AsyncResult) msg.obj)) {
                    break;
}
                data = result.payload;
                lc.recordSize = data[RESPONSE_DATA_RECORD_LENGTH] & 0xFF;

                if ((TYPE_EF != data[RESPONSE_DATA_FILE_TYPE]) ||
                    (EF_TYPE_LINEAR_FIXED != data[RESPONSE_DATA_STRUCTURE])) {
                    loge("IccFileHandler: File type mismatch: Throw Exception");
                    throw new IccFileTypeMismatch();
                }

                logd("IccFileHandler: read EF IMG");
                mCi.iccIOForApp(COMMAND_READ_RECORD, lc.efid, getEFPath(lc.efid),
                        lc.recordNum,
                        READ_RECORD_MODE_ABSOLUTE,
                        lc.recordSize, null, null,
                        mAid, obtainMessage(EVENT_READ_IMG_DONE, IccConstants.EF_IMG,
                                0, response));
break;
           case EVENT_READ_IMG_DONE:
               logd("IccFileHandler: read IMG done");
               ar = (AsyncResult) msg.obj;
               response = (Message) ar.userObj;
               result = (IccIoResult) ar.result;

               if (processException(response, (AsyncResult) msg.obj)) {
                   break;
               }
               logd("IccFileHandler: read img success");
               sendResult(response, result.payload, null);
               iccException = result.getException();
               if (iccException != null) {
                   sendResult(response, result.payload, ar.exception);
               }
               break;
case EVENT_READ_ICON_DONE:
                logd("IccFileHandler: read icon done");
ar = (AsyncResult) msg.obj;
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                if (processException(response, (AsyncResult) msg.obj)) {
                    break;
}
                logd("IccFileHandler: read icon success");
                sendResult(response, result.payload, null);
break;
case EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE:
ar = (AsyncResult)msg.obj;
//Synthetic comment -- @@ -349,14 +403,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (processException(response, (AsyncResult) msg.obj)) {
break;
}

//Synthetic comment -- @@ -381,15 +428,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (processException(response, (AsyncResult) msg.obj)) {
break;
}

//Synthetic comment -- @@ -427,15 +466,7 @@
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                if (processException(response, (AsyncResult) msg.obj)) {
break;
}

//Synthetic comment -- @@ -467,15 +498,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (processException(response, (AsyncResult) msg.obj)) {
break;
}

//Synthetic comment -- @@ -504,15 +527,7 @@
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                if (processException(response, (AsyncResult) msg.obj)) {
break;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 299e140..762bcbb 100644

//Synthetic comment -- @@ -32,6 +32,15 @@
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

    // This is broadcast from the ActivityManagerService when the screen
    // switches to idle or busy state
    public static final String CAT_IDLE_SCREEN_ACTION =
                                    "android.intent.action.stk.idle_screen";

    // This is broadcast from the Stk Apps to ActivityManagerService when the screen
    // status is requested.
    public static final String CHECK_SCREEN_IDLE_ACTION =
                                    "android.intent.action.stk.check_screen_idle";
/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatCmdMessage.java b/src/java/com/android/internal/telephony/cat/CatCmdMessage.java
//Synthetic comment -- index 48c2e2b..8526722 100644

//Synthetic comment -- @@ -33,6 +33,8 @@
private BrowserSettings mBrowserSettings = null;
private ToneSettings mToneSettings = null;
private CallSettings mCallSettings = null;
    private boolean mloadIconFailed = false;
    private SetupEventListSettings mSetupEventListSettings = null;

/*
* Container for Launch Browser command settings.
//Synthetic comment -- @@ -50,8 +52,27 @@
public TextMessage callMsg;
}

    public class SetupEventListSettings {
        public int[] eventList;
    }

    public final class SetupEventListConstants {
        // Event values in SETUP_EVENT_LIST Proactive Command as per ETSI 102.223
        public static final int USER_ACTIVITY_EVENT          = 0x04;
        public static final int IDLE_SCREEN_AVAILABLE_EVENT  = 0x05;
        public static final int LANGUAGE_SELECTION_EVENT     = 0x07;
        public static final int BROWSER_TERMINATION_EVENT    = 0x08;
        public static final int BROWSING_STATUS_EVENT        = 0x0F;
    }

    public final class BrowserTerminationCauses {
        public static final int USER_TERMINATION             = 0x00;
        public static final int ERROR_TERMINATION            = 0x01;
    }

CatCmdMessage(CommandParams cmdParams) {
mCmdDet = cmdParams.cmdDet;
        mloadIconFailed =  cmdParams.loadIconFailed;
switch(getCmdType()) {
case SET_UP_MENU:
case SELECT_ITEM:
//Synthetic comment -- @@ -92,6 +113,10 @@
BIPClientParams param = (BIPClientParams) cmdParams;
mTextMsg = param.textMsg;
break;
        case SET_UP_EVENT_LIST:
            mSetupEventListSettings = new SetupEventListSettings();
            mSetupEventListSettings.eventList = ((SetEventListParams) cmdParams).eventInfo;
            break;
}
}

//Synthetic comment -- @@ -100,6 +125,7 @@
mTextMsg = in.readParcelable(null);
mMenu = in.readParcelable(null);
mInput = in.readParcelable(null);
        mloadIconFailed = (Boolean)in.readValue(null);
switch (getCmdType()) {
case LAUNCH_BROWSER:
mBrowserSettings = new BrowserSettings();
//Synthetic comment -- @@ -114,6 +140,14 @@
mCallSettings.confirmMsg = in.readParcelable(null);
mCallSettings.callMsg = in.readParcelable(null);
break;
        case SET_UP_EVENT_LIST:
            mSetupEventListSettings = new SetupEventListSettings();
            int length = in.readInt();
            mSetupEventListSettings.eventList = new int[length];
            for (int i = 0; i < length; i++) {
                mSetupEventListSettings.eventList[i] = in.readInt();
            }
            break;
}
}

//Synthetic comment -- @@ -122,6 +156,7 @@
dest.writeParcelable(mTextMsg, 0);
dest.writeParcelable(mMenu, 0);
dest.writeParcelable(mInput, 0);
        dest.writeValue(mloadIconFailed);
switch(getCmdType()) {
case LAUNCH_BROWSER:
dest.writeString(mBrowserSettings.url);
//Synthetic comment -- @@ -134,6 +169,9 @@
dest.writeParcelable(mCallSettings.confirmMsg, 0);
dest.writeParcelable(mCallSettings.callMsg, 0);
break;
        case SET_UP_EVENT_LIST:
            dest.writeIntArray(mSetupEventListSettings.eventList);
            break;
}
}

//Synthetic comment -- @@ -179,4 +217,14 @@
public CallSettings getCallSettings() {
return mCallSettings;
}

    /* API to be used by application to check if loading optional icon
     * has failed  */
    public boolean hasIconLoadFailed() {
        return mloadIconFailed;
    }

    public SetupEventListSettings getSetEventList() {
        return mSetupEventListSettings;
    }
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatResponseMessage.java b/src/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..31e8c63 100644

//Synthetic comment -- @@ -23,6 +23,10 @@
String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;
        int eventValue = -1;
        byte[] addedInfo = null;
        boolean includeAdditionalInfo = false;
        int additionalInfo = 0;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
//Synthetic comment -- @@ -40,6 +44,11 @@
this.usersInput = input;
}

        public void setEventDownload(int event,byte[] addedInfo) {
            this.eventValue = event;
            this.addedInfo = addedInfo;
        }

public void setYesNo(boolean yesNo) {
usersYesNoSelection = yesNo;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..b06d73f 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import java.util.List;
import java.util.Locale;

import static  com.android.internal.telephony.cat.CatCmdMessage.SetupEventListConstants.*;

class RilMessage {
int mId;
Object mData;
//Synthetic comment -- @@ -207,6 +209,32 @@
}
}

    /**  This function validates the events in SETUP_EVENT_LIST which are currently
     *   supported by the Android framework. In case of SETUP_EVENT_LIST has NULL events
     *   or no events, all the events need to be reset.
     */
    private boolean isSupportedSetupEventCommand(CatCmdMessage cmdMsg) {
        boolean flag = true;
        int eventval;

        for (int i = 0; i < cmdMsg.getSetEventList().eventList.length ; i++) {
            eventval = cmdMsg.getSetEventList().eventList[i];
            CatLog.d(this,"Event: "+eventval);
            switch (eventval) {
                /* Currently android is supporting only the below events in SetupEventList
                 * Browser Termination,
                 * Idle Screen Available and
                 * Language Selection.  */
                case IDLE_SCREEN_AVAILABLE_EVENT:
                case LANGUAGE_SELECTION_EVENT:
                    break;
                default:
                    flag = false;
            }
        }
        return flag;
    }

/**
* Handles RIL_UNSOL_STK_EVENT_NOTIFY or RIL_UNSOL_STK_PROACTIVE_COMMAND command
* from RIL.
//Synthetic comment -- @@ -218,6 +246,7 @@
CatLog.d(this, cmdParams.getCommandType().name());

CharSequence message;
        ResultCode resultCode;
CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
switch (cmdParams.getCommandType()) {
case SET_UP_MENU:
//Synthetic comment -- @@ -226,12 +255,14 @@
} else {
mMenuCmd = cmdMsg;
}
                resultCode = cmdParams.loadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK;
                sendTerminalResponse(cmdParams.cmdDet,resultCode, false, 0,null);
break;
case DISPLAY_TEXT:
// when application is not required to respond, send an immediate response.
if (!cmdMsg.geTextMessage().responseNeeded) {
                    resultCode = cmdParams.loadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK;
                    sendTerminalResponse(cmdParams.cmdDet,resultCode, false, 0,null);
}
break;
case REFRESH:
//Synthetic comment -- @@ -240,7 +271,16 @@
cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT.value();
break;
case SET_UP_IDLE_MODE_TEXT:
                resultCode = cmdParams.loadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK;
                sendTerminalResponse(cmdParams.cmdDet,resultCode, false, 0,null);
                break;
            case SET_UP_EVENT_LIST:
                if (isSupportedSetupEventCommand(cmdMsg)) {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                } else {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY,
                            false, 0, null);
                }
break;
case PROVIDE_LOCAL_INFORMATION:
ResponseData resp;
//Synthetic comment -- @@ -531,6 +571,35 @@
buf.write(sourceId); // source device id
buf.write(destinationId); // destination device id

        /*
         * Check for type of event download to be sent to UICC - Browser
         * termination,Idle screen available, User activity, Language selection
         * etc as mentioned under ETSI TS 102 223 section 7.5
         */

        /*
         * Currently the below events are supported:
         * Browser Termination,
         * Idle Screen Available and
         * Language Selection Event.
         * Other event download commands should be encoded similar way
         */
        /* TODO: eventDownload should be extended for other Envelope Commands */
        switch (event) {
            case IDLE_SCREEN_AVAILABLE_EVENT:
                CatLog.d(this, " Sending Idle Screen Available event download to ICC");
                break;
            case LANGUAGE_SELECTION_EVENT:
                CatLog.d(this, " Sending Language Selection event download to ICC");
                tag = 0x80 | ComprehensionTlvTag.LANGUAGE.value();
                buf.write(tag);
                // Language length should be 2 byte
                buf.write(0x02);
                break;
            default:
                break;
        }

// additional information
if (additionalInfo != null) {
for (byte b : additionalInfo) {
//Synthetic comment -- @@ -546,6 +615,8 @@

String hexString = IccUtils.bytesToHexString(rawData);

        CatLog.d(this, "ENVELOPE COMMAND: " + hexString);

mCmdIf.sendEnvelope(hexString, null);
}

//Synthetic comment -- @@ -669,10 +740,16 @@
}

private boolean validateResponse(CatResponseMessage resMsg) {
        boolean validResponse = false;
        if ((resMsg.cmdDet.typeOfCommand == CommandType.SET_UP_EVENT_LIST.value())
                || (resMsg.cmdDet.typeOfCommand == CommandType.SET_UP_MENU.value())) {
            CatLog.d(this, "CmdType: " + resMsg.cmdDet.typeOfCommand);
            validResponse = true;
        } else if (mCurrntCmd != null) {
            validResponse = resMsg.cmdDet.compareTo(mCurrntCmd.mCmdDet);
            CatLog.d(this, "isResponse for last valid cmd: " + validResponse);
}
        return validResponse;
}

private boolean removeMenu(Menu menu) {
//Synthetic comment -- @@ -696,8 +773,14 @@
// by the framework inside the history stack. That activity will be
// available for relaunch using the latest application dialog
// (long press on the home button). Relaunching that activity can send
        // the same command's result again to the StkService and can cause it to
        // get out of sync with the SIM. This can happen in case of
        // non-interactive type Setup Event List and SETUP_MENU proactive commands.
        // Stk framework would have already sent Terminal Response to Setup Event
        // List and SETUP_MENU proactive commands. After sometime Stk app will send
        // Envelope Command/Event Download. In which case, the response details doesn't
        // match with last valid command (which are not related).
        // However, we should allow Stk framework to send the message to ICC.
if (!validateResponse(resMsg)) {
return;
}
//Synthetic comment -- @@ -752,6 +835,16 @@
// invoked by the CommandInterface call above.
mCurrntCmd = null;
return;
            case SET_UP_EVENT_LIST:
                if (IDLE_SCREEN_AVAILABLE_EVENT == resMsg.eventValue) {
                    eventDownload(resMsg.eventValue, DEV_ID_DISPLAY, DEV_ID_UICC,
                            resMsg.addedInfo, false);
                } else {
                    eventDownload(resMsg.eventValue, DEV_ID_TERMINAL, DEV_ID_UICC,
                            resMsg.addedInfo, false);
                }
                // No need to send the terminal response after event download.
                return;
}
break;
case NO_RESPONSE_FROM_USER:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParams.java b/src/java/com/android/internal/telephony/cat/CommandParams.java
//Synthetic comment -- index 79f6ad2..4bf2fc2 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
*/
class CommandParams {
CommandDetails cmdDet;
    //Variable to track if an optional icon load has failed.
    boolean loadIconFailed = false;

CommandParams(CommandDetails cmdDet) {
this.cmdDet = cmdDet;
//Synthetic comment -- @@ -80,6 +82,14 @@
}
}

class SetEventListParams extends CommandParams {
    int[] eventInfo;
    SetEventListParams(CommandDetails cmdDet, int[] eventInfo) {
        super(cmdDet);
        this.eventInfo = eventInfo;
    }
}

class PlayToneParams extends CommandParams {
TextMessage textMsg;
ToneSettings settings;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..a4518cc 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import java.util.Iterator;
import java.util.List;
import static com.android.internal.telephony.cat.CatCmdMessage.SetupEventListConstants.*;

/**
* Factory class, used for decoding raw byte arrays, received from baseband,
//Synthetic comment -- @@ -37,6 +38,7 @@
private CommandParams mCmdParams = null;
private int mIconLoadState = LOAD_NO_ICON;
private RilMessageDecoder mCaller = null;
    private boolean mloadIcon = false;

// constants
static final int MSG_ID_LOAD_ICON_DONE = 1;
//Synthetic comment -- @@ -163,6 +165,9 @@
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
             case SET_UP_EVENT_LIST:
                    cmdPending = processSetUpEventList(cmdDet, ctlvs);
                    break;
case PROVIDE_LOCAL_INFORMATION:
cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
break;
//Synthetic comment -- @@ -203,6 +208,14 @@
int iconIndex = 0;

if (data == null) {
            if (mloadIcon) {
                CatLog.d(this, "Optional Icon data is NULL");
                mCmdParams.loadIconFailed = true;
                mloadIcon = false;
                /** In case of icon load fail consider the
                 ** received proactive command as valid (sending RESULT OK) */
                return ResultCode.OK;
            }
return ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
}
switch(mIconLoadState) {
//Synthetic comment -- @@ -214,6 +227,10 @@
// set each item icon.
for (Bitmap icon : icons) {
mCmdParams.setIcon(icon);
                if (icon == null && mloadIcon) {
                    CatLog.d(this, "Optional Icon data is NULL while loading multi icons");
                    mCmdParams.loadIconFailed = true;
                }
}
break;
}
//Synthetic comment -- @@ -316,6 +333,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
            mloadIcon = true;
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -359,6 +377,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
            mloadIcon = true;
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -416,6 +435,7 @@
mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {
            mloadIcon = true;
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -483,6 +503,7 @@
mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {
            mloadIcon = true;
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -596,6 +617,7 @@
case LOAD_NO_ICON:
return false;
case LOAD_SINGLE_ICON:
            mloadIcon = true;
mIconLoader.loadIcon(titleIconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
break;
//Synthetic comment -- @@ -608,6 +630,7 @@
System.arraycopy(itemsIconId.recordNumbers, 0, recordNumbers,
1, itemsIconId.recordNumbers.length);
}
            mloadIcon = true;
mIconLoader.loadIcons(recordNumbers, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
break;
//Synthetic comment -- @@ -646,6 +669,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
            mloadIcon = true;
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -660,25 +684,48 @@
* @param cmdDet Command Details object retrieved.
* @param ctlvs List of ComprehensionTlv objects following Command Details
*        object and Device Identities object within the proactive command
     * @return false. This function always returns false meaning that the command
     *         processing is  not pending and additional asynchronous processing
     *         is not required.
*/
private boolean processSetUpEventList(CommandDetails cmdDet,
List<ComprehensionTlv> ctlvs) {

CatLog.d(this, "process SetUpEventList");
        ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.EVENT_LIST, ctlvs);
        if (ctlv != null) {
            try {
                byte[] rawValue = ctlv.getRawValue();
                int valueIndex = ctlv.getValueIndex();
                int valueLen = ctlv.getLength();
                int[] eventList = new int[valueLen];
                int eventValue = -1;
                int i = 0;
                while (valueLen > 0) {
                    eventValue = rawValue[valueIndex] & 0xff;
                    valueIndex++;
                    valueLen--;

                    switch (eventValue) {
                        case USER_ACTIVITY_EVENT:
                        case IDLE_SCREEN_AVAILABLE_EVENT:
                        case LANGUAGE_SELECTION_EVENT:
                        case BROWSER_TERMINATION_EVENT:
                        case BROWSING_STATUS_EVENT:
                            eventList[i] = eventValue;
                            i++;
                            break;
                        default:
                            break;
                    }

                }
                mCmdParams = new SetEventListParams(cmdDet, eventList);
            } catch (IndexOutOfBoundsException e) {
                CatLog.d(this, " IndexOutofBoundException in processSetUpEventList");
            }
        }
        return false;
}

/**








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ImageDescriptor.java b/src/java/com/android/internal/telephony/cat/ImageDescriptor.java
//Synthetic comment -- index 711d977..0da1d46 100644

//Synthetic comment -- @@ -68,6 +68,11 @@
d.lowOffset = rawData[valueIndex++] & 0xff; // low byte offset

d.length = ((rawData[valueIndex++] & 0xff) << 8 | (rawData[valueIndex++] & 0xff));

            CatLog.d("ImageDescripter", "parse; Descriptor : " + d.width + ", " + d.height +
                     ", " + d.codingScheme + ", 0x" + Integer.toHexString(d.imageId) + ", " +
                     d.highOffset + ", " + d.lowOffset + ", " + d.length);

} catch (IndexOutOfBoundsException e) {
CatLog.d("ImageDescripter", "parse; failed parsing image descriptor");
d = null;







