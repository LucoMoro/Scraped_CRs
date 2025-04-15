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

// member variables
protected final CommandsInterface mCi;
//Synthetic comment -- @@ -162,7 +164,7 @@
*
*/
public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        Message response = obtainMessage(EVENT_READ_IMG_DONE,
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

//Synthetic comment -- @@ -304,6 +306,24 @@
response.sendToTarget();
}

//***** Overridden from Handler

public void handleMessage(Message msg) {
//Synthetic comment -- @@ -322,26 +342,60 @@

try {
switch (msg.what) {
            case EVENT_READ_IMG_DONE:
ar = (AsyncResult) msg.obj;
lc = (LoadLinearFixedContext) ar.userObj;
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                iccException = result.getException();
                if (iccException != null) {
                    sendResult(response, result.payload, ar.exception);
}
break;
case EVENT_READ_ICON_DONE:
ar = (AsyncResult) msg.obj;
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                iccException = result.getException();
                if (iccException != null) {
                    sendResult(response, result.payload, ar.exception);
}
break;
case EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE:
ar = (AsyncResult)msg.obj;
//Synthetic comment -- @@ -349,14 +403,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (ar.exception != null) {
                    sendResult(response, null, ar.exception);
                    break;
                }

                iccException = result.getException();
                if (iccException != null) {
                    sendResult(response, null, iccException);
break;
}

//Synthetic comment -- @@ -381,15 +428,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (ar.exception != null) {
                    sendResult(response, null, ar.exception);
                    break;
                }

                iccException = result.getException();

                if (iccException != null) {
                    sendResult(response, null, iccException);
break;
}

//Synthetic comment -- @@ -427,15 +466,7 @@
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                if (ar.exception != null) {
                    sendResult(response, null, ar.exception);
                    break;
                }

                iccException = result.getException();

                if (iccException != null) {
                    sendResult(response, null, iccException);
break;
}

//Synthetic comment -- @@ -467,15 +498,7 @@
result = (IccIoResult) ar.result;
response = lc.onLoaded;

                if (ar.exception != null) {
                    sendResult(response, null, ar.exception);
                    break;
                }

                iccException = result.getException();

                if (iccException != null) {
                    sendResult(response, null, iccException);
break;
}

//Synthetic comment -- @@ -504,15 +527,7 @@
response = (Message) ar.userObj;
result = (IccIoResult) ar.result;

                if (ar.exception != null) {
                    sendResult(response, null, ar.exception);
                    break;
                }

                iccException = result.getException();

                if (iccException != null) {
                    sendResult(response, null, iccException);
break;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 299e140..762bcbb 100644

//Synthetic comment -- @@ -32,6 +32,15 @@
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatCmdMessage.java b/src/java/com/android/internal/telephony/cat/CatCmdMessage.java
//Synthetic comment -- index 48c2e2b..8526722 100644

//Synthetic comment -- @@ -33,6 +33,8 @@
private BrowserSettings mBrowserSettings = null;
private ToneSettings mToneSettings = null;
private CallSettings mCallSettings = null;

/*
* Container for Launch Browser command settings.
//Synthetic comment -- @@ -50,8 +52,27 @@
public TextMessage callMsg;
}

CatCmdMessage(CommandParams cmdParams) {
mCmdDet = cmdParams.cmdDet;
switch(getCmdType()) {
case SET_UP_MENU:
case SELECT_ITEM:
//Synthetic comment -- @@ -92,6 +113,10 @@
BIPClientParams param = (BIPClientParams) cmdParams;
mTextMsg = param.textMsg;
break;
}
}

//Synthetic comment -- @@ -100,6 +125,7 @@
mTextMsg = in.readParcelable(null);
mMenu = in.readParcelable(null);
mInput = in.readParcelable(null);
switch (getCmdType()) {
case LAUNCH_BROWSER:
mBrowserSettings = new BrowserSettings();
//Synthetic comment -- @@ -114,6 +140,14 @@
mCallSettings.confirmMsg = in.readParcelable(null);
mCallSettings.callMsg = in.readParcelable(null);
break;
}
}

//Synthetic comment -- @@ -122,6 +156,7 @@
dest.writeParcelable(mTextMsg, 0);
dest.writeParcelable(mMenu, 0);
dest.writeParcelable(mInput, 0);
switch(getCmdType()) {
case LAUNCH_BROWSER:
dest.writeString(mBrowserSettings.url);
//Synthetic comment -- @@ -134,6 +169,9 @@
dest.writeParcelable(mCallSettings.confirmMsg, 0);
dest.writeParcelable(mCallSettings.callMsg, 0);
break;
}
}

//Synthetic comment -- @@ -179,4 +217,14 @@
public CallSettings getCallSettings() {
return mCallSettings;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatResponseMessage.java b/src/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..31e8c63 100644

//Synthetic comment -- @@ -23,6 +23,10 @@
String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
//Synthetic comment -- @@ -40,6 +44,11 @@
this.usersInput = input;
}

public void setYesNo(boolean yesNo) {
usersYesNoSelection = yesNo;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..b06d73f 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
import java.util.List;
import java.util.Locale;

class RilMessage {
int mId;
Object mData;
//Synthetic comment -- @@ -207,6 +209,32 @@
}
}

/**
* Handles RIL_UNSOL_STK_EVENT_NOTIFY or RIL_UNSOL_STK_PROACTIVE_COMMAND command
* from RIL.
//Synthetic comment -- @@ -218,6 +246,7 @@
CatLog.d(this, cmdParams.getCommandType().name());

CharSequence message;
CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
switch (cmdParams.getCommandType()) {
case SET_UP_MENU:
//Synthetic comment -- @@ -226,12 +255,14 @@
} else {
mMenuCmd = cmdMsg;
}
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case DISPLAY_TEXT:
// when application is not required to respond, send an immediate response.
if (!cmdMsg.geTextMessage().responseNeeded) {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
}
break;
case REFRESH:
//Synthetic comment -- @@ -240,7 +271,16 @@
cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT.value();
break;
case SET_UP_IDLE_MODE_TEXT:
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case PROVIDE_LOCAL_INFORMATION:
ResponseData resp;
//Synthetic comment -- @@ -531,6 +571,35 @@
buf.write(sourceId); // source device id
buf.write(destinationId); // destination device id

// additional information
if (additionalInfo != null) {
for (byte b : additionalInfo) {
//Synthetic comment -- @@ -546,6 +615,8 @@

String hexString = IccUtils.bytesToHexString(rawData);

mCmdIf.sendEnvelope(hexString, null);
}

//Synthetic comment -- @@ -669,10 +740,16 @@
}

private boolean validateResponse(CatResponseMessage resMsg) {
        if (mCurrntCmd != null) {
            return (resMsg.cmdDet.compareTo(mCurrntCmd.mCmdDet));
}
        return false;
}

private boolean removeMenu(Menu menu) {
//Synthetic comment -- @@ -696,8 +773,14 @@
// by the framework inside the history stack. That activity will be
// available for relaunch using the latest application dialog
// (long press on the home button). Relaunching that activity can send
        // the same command's result again to the CatService and can cause it to
        // get out of sync with the SIM.
if (!validateResponse(resMsg)) {
return;
}
//Synthetic comment -- @@ -752,6 +835,16 @@
// invoked by the CommandInterface call above.
mCurrntCmd = null;
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

CommandParams(CommandDetails cmdDet) {
this.cmdDet = cmdDet;
//Synthetic comment -- @@ -80,6 +82,14 @@
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

/**
* Factory class, used for decoding raw byte arrays, received from baseband,
//Synthetic comment -- @@ -37,6 +38,7 @@
private CommandParams mCmdParams = null;
private int mIconLoadState = LOAD_NO_ICON;
private RilMessageDecoder mCaller = null;

// constants
static final int MSG_ID_LOAD_ICON_DONE = 1;
//Synthetic comment -- @@ -163,6 +165,9 @@
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
case PROVIDE_LOCAL_INFORMATION:
cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
break;
//Synthetic comment -- @@ -203,6 +208,14 @@
int iconIndex = 0;

if (data == null) {
return ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
}
switch(mIconLoadState) {
//Synthetic comment -- @@ -214,6 +227,10 @@
// set each item icon.
for (Bitmap icon : icons) {
mCmdParams.setIcon(icon);
}
break;
}
//Synthetic comment -- @@ -316,6 +333,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -359,6 +377,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -416,6 +435,7 @@
mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -483,6 +503,7 @@
mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -596,6 +617,7 @@
case LOAD_NO_ICON:
return false;
case LOAD_SINGLE_ICON:
mIconLoader.loadIcon(titleIconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
break;
//Synthetic comment -- @@ -608,6 +630,7 @@
System.arraycopy(itemsIconId.recordNumbers, 0, recordNumbers,
1, itemsIconId.recordNumbers.length);
}
mIconLoader.loadIcons(recordNumbers, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
break;
//Synthetic comment -- @@ -646,6 +669,7 @@
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
mIconLoadState = LOAD_SINGLE_ICON;
mIconLoader.loadIcon(iconId.recordNumber, this
.obtainMessage(MSG_ID_LOAD_ICON_DONE));
//Synthetic comment -- @@ -660,25 +684,48 @@
* @param cmdDet Command Details object retrieved.
* @param ctlvs List of ComprehensionTlv objects following Command Details
*        object and Device Identities object within the proactive command
     * @return true if the command is processing is pending and additional
     *         asynchronous processing is required.
*/
private boolean processSetUpEventList(CommandDetails cmdDet,
List<ComprehensionTlv> ctlvs) {

CatLog.d(this, "process SetUpEventList");
        //
        // ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.EVENT_LIST,
        // ctlvs);
        // if (ctlv != null) {
        // try {
        // byte[] rawValue = ctlv.getRawValue();
        // int valueIndex = ctlv.getValueIndex();
        // int valueLen = ctlv.getLength();
        //
        // } catch (IndexOutOfBoundsException e) {}
        // }
        return true;
}

/**








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ImageDescriptor.java b/src/java/com/android/internal/telephony/cat/ImageDescriptor.java
//Synthetic comment -- index 711d977..0da1d46 100644

//Synthetic comment -- @@ -68,6 +68,11 @@
d.lowOffset = rawData[valueIndex++] & 0xff; // low byte offset

d.length = ((rawData[valueIndex++] & 0xff) << 8 | (rawData[valueIndex++] & 0xff));
} catch (IndexOutOfBoundsException e) {
CatLog.d("ImageDescripter", "parse; failed parsing image descriptor");
d = null;







