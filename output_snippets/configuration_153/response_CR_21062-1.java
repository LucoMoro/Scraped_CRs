//<Beginning of snippet n. 0>


public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private BrowserSettings mBrowserSettings = null;
private ToneSettings mToneSettings = null;
private CallSettings mCallSettings = null;

/*
* Container for Launch Browser command settings.
public TextMessage callMsg;
}

CatCmdMessage(CommandParams cmdParams) {
mCmdDet = cmdParams.cmdDet;
switch(getCmdType()) {
mCallSettings.confirmMsg = ((CallSetupParams) cmdParams).confirmMsg;
mCallSettings.callMsg = ((CallSetupParams) cmdParams).callMsg;
break;
}
}

mCallSettings.confirmMsg = in.readParcelable(null);
mCallSettings.callMsg = in.readParcelable(null);
break;
}
}

dest.writeParcelable(mCallSettings.confirmMsg, 0);
dest.writeParcelable(mCallSettings.callMsg, 0);
break;
}
}

public CallSettings getCallSettings() {
return mCallSettings;
}
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
this.usersInput = input;
}

public void setYesNo(boolean yesNo) {
usersYesNoSelection = yesNo;
}
usersConfirm = confirm;
}

CommandDetails getCmdDetails() {
return cmdDet;
}
    }
\ No newline at end of file

//<End of snippet n. 2>










//<Beginning of snippet n. 3>



import java.io.ByteArrayOutputStream;

/**
* Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
* you want to get the actual value, call {@link #value() value} method.
}
}

/**
* Handles RIL_UNSOL_STK_PROACTIVE_COMMAND unsolicited command from RIL.
* Sends valid proactive command data to the application using intents.
case SET_UP_IDLE_MODE_TEXT:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case PROVIDE_LOCAL_INFORMATION:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
return;

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

buf.write(sourceId); // source device id
buf.write(destinationId); // destination device id

// additional information
if (additionalInfo != null) {
for (byte b : additionalInfo) {

String hexString = IccUtils.bytesToHexString(rawData);

mCmdIf.sendEnvelope(hexString, null);
}

}

private boolean validateResponse(CatResponseMessage resMsg) {
        if (mCurrntCmd != null) {
            return (resMsg.cmdDet.compareTo(mCurrntCmd.mCmdDet));
}
        return false;
}

private boolean removeMenu(Menu menu) {
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

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


}
}

class PlayToneParams extends CommandParams {
TextMessage textMsg;
ToneSettings settings;

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


import java.util.Iterator;
import java.util.List;

/**
* Factory class, used for decoding raw byte arrays, received from baseband,
* into a CommandParams object.
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
case PROVIDE_LOCAL_INFORMATION:
cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
break;
* @param cmdDet Command Details object retrieved.
* @param ctlvs List of ComprehensionTlv objects following Command Details
*        object and Device Identities object within the proactive command
     * @return true if the command is processing is pending and additional
     *         asynchronous processing is required.
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
                // Further processing of rawValue as per the spec
                // Notify applications or send responses as necessary
            } catch (IndexOutOfBoundsException e) {
                CatLog.e(this, "Index out of bounds while processing EVENT_LIST: " + e.getMessage());
            }
        }
        return true;
}

/**

//<End of snippet n. 5>