/*Telephony: Support for SetupEventList Proactive command

These changes provide support for SetupEventList proactive command.
Ref: ETSI TS 102 223 Section:6.4.16.

Change-Id:If7b2c68c612d7fd293e044cfce91f7d1565ef385*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/AppInterface.java b/telephony/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 2eb6ccb..e5fe4d18 100644

//Synthetic comment -- @@ -32,6 +32,14 @@
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatCmdMessage.java b/telephony/java/com/android/internal/telephony/cat/CatCmdMessage.java
//Synthetic comment -- index 5155bb2..0b1697a6 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
private BrowserSettings mBrowserSettings = null;
private ToneSettings mToneSettings = null;
private CallSettings mCallSettings = null;

/*
* Container for Launch Browser command settings.
//Synthetic comment -- @@ -50,6 +51,27 @@
public TextMessage callMsg;
}

CatCmdMessage(CommandParams cmdParams) {
mCmdDet = cmdParams.cmdDet;
switch(getCmdType()) {
//Synthetic comment -- @@ -85,6 +107,10 @@
mCallSettings.confirmMsg = ((CallSetupParams) cmdParams).confirmMsg;
mCallSettings.callMsg = ((CallSetupParams) cmdParams).callMsg;
break;
}
}

//Synthetic comment -- @@ -107,6 +133,14 @@
mCallSettings.confirmMsg = in.readParcelable(null);
mCallSettings.callMsg = in.readParcelable(null);
break;
}
}

//Synthetic comment -- @@ -127,6 +161,9 @@
dest.writeParcelable(mCallSettings.confirmMsg, 0);
dest.writeParcelable(mCallSettings.callMsg, 0);
break;
}
}

//Synthetic comment -- @@ -172,4 +209,8 @@
public CallSettings getCallSettings() {
return mCallSettings;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatResponseMessage.java b/telephony/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..61a53a5 100644

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
//Synthetic comment -- @@ -48,7 +57,12 @@
usersConfirm = confirm;
}

CommandDetails getCmdDetails() {
return cmdDet;
}
    }
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..174f30f 100644

//Synthetic comment -- @@ -34,6 +34,8 @@

import java.io.ByteArrayOutputStream;

/**
* Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
* you want to get the actual value, call {@link #value() value} method.
//Synthetic comment -- @@ -236,6 +238,33 @@
}
}

/**
* Handles RIL_UNSOL_STK_PROACTIVE_COMMAND unsolicited command from RIL.
* Sends valid proactive command data to the application using intents.
//Synthetic comment -- @@ -268,6 +297,14 @@
case SET_UP_IDLE_MODE_TEXT:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case PROVIDE_LOCAL_INFORMATION:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
return;
//Synthetic comment -- @@ -373,25 +410,30 @@

private void encodeOptionalTags(CommandDetails cmdDet,
ResultCode resultCode, Input cmdInput, ByteArrayOutputStream buf) {
        switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
            case GET_INKEY:
                // ETSI TS 102 384,27.22.4.2.8.4.2.
                // If it is a response for GET_INKEY command and the response timeout
                // occured, then add DURATION TLV for variable timeout case.
                if ((resultCode.value() == ResultCode.NO_RESPONSE_FROM_USER.value()) &&
                    (cmdInput != null) && (cmdInput.duration != null)) {
                    getInKeyResponse(buf, cmdInput);
                }
                break;
            case PROVIDE_LOCAL_INFORMATION:
                if ((cmdDet.commandQualifier == CommandParamsFactory.LANGUAGE_SETTING) &&
                    (resultCode.value() == ResultCode.OK.value())) {
                    getPliResponse(buf);
                }
                break;
            default:
                CatLog.d(this, "encodeOptionalTags() Unsupported Cmd:" + cmdDet.typeOfCommand);
                break;
}
}

//Synthetic comment -- @@ -485,6 +527,42 @@
buf.write(sourceId); // source device id
buf.write(destinationId); // destination device id

// additional information
if (additionalInfo != null) {
for (byte b : additionalInfo) {
//Synthetic comment -- @@ -500,6 +578,10 @@

String hexString = IccUtils.bytesToHexString(rawData);

mCmdIf.sendEnvelope(hexString, null);
}

//Synthetic comment -- @@ -644,39 +726,56 @@
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
            switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
            case SET_UP_MENU:
                helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
                sendMenuSelection(resMsg.usersMenuSelection, helpRequired);
                return;
            case SELECT_ITEM:
                resp = new SelectItemResponseData(resMsg.usersMenuSelection);
                break;
            case GET_INPUT:
            case GET_INKEY:
                Input input = mCurrntCmd.geInput();
                if (!input.yesNo) {
                    // when help is requested there is no need to send the text
                    // string object.
                    if (!helpRequired) {
                        resp = new GetInkeyInputResponseData(resMsg.usersInput,
input.ucs2, input.packed);
                    }
                } else {
                    resp = new GetInkeyInputResponseData(
resMsg.usersYesNoSelection);
}
                break;
            case DISPLAY_TEXT:
            case LAUNCH_BROWSER:
                break;
            case SET_UP_CALL:
                mCmdIf.handleCallSetupRequestFromSim(resMsg.usersConfirm, null);
                // No need to send terminal response for SET UP CALL. The user's
                // confirmation result is send back using a dedicated ril message
                // invoked by the CommandInterface call above.
                mCurrntCmd = null;
                return;
}
break;
case NO_RESPONSE_FROM_USER:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParams.java b/telephony/java/com/android/internal/telephony/cat/CommandParams.java
//Synthetic comment -- index 22a5c8c..339b669 100644

//Synthetic comment -- @@ -75,6 +75,14 @@
}
}

class PlayToneParams extends CommandParams {
TextMessage textMsg;
ToneSettings settings;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index 12204a0..347b354 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.util.Iterator;
import java.util.List;

/**
* Factory class, used for decoding raw byte arrays, received from baseband,
* into a CommandParams object.
//Synthetic comment -- @@ -161,6 +163,9 @@
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
case PROVIDE_LOCAL_INFORMATION:
cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
break;
//Synthetic comment -- @@ -654,25 +659,48 @@
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







