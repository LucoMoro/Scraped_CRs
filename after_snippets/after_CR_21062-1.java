
//<Beginning of snippet n. 0>


public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

    //This is broadcasted from BrowserActivity when Browser exists
    public static final String BROWSER_TERMINATE_ACTION =
                                   "android.intent.action.stk.browser_terminate_action";

    //This is used if Browser termination was erroneous
    public static final String BROWSER_TERMINATION_CAUSE =
                                   "browser_termination_cause";

/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private BrowserSettings mBrowserSettings = null;
private ToneSettings mToneSettings = null;
private CallSettings mCallSettings = null;
    private SetupEventListSettings mSetupEventListSettings = null;

/*
* Container for Launch Browser command settings.
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
        public static final int USER_TERMINATION             = 0x00;
        public static final int ERROR_TERMINATION            = 0x01;

    }

    public final class BrowserTerminationCauses {
        public static final int USER_TERMINATION             = 0x00;
        public static final int ERROR_TERMINATION            = 0x01;
    }

CatCmdMessage(CommandParams cmdParams) {
mCmdDet = cmdParams.cmdDet;
switch(getCmdType()) {
mCallSettings.confirmMsg = ((CallSetupParams) cmdParams).confirmMsg;
mCallSettings.callMsg = ((CallSetupParams) cmdParams).callMsg;
break;
        case SET_UP_EVENT_LIST:
            mSetupEventListSettings = new SetupEventListSettings();
            mSetupEventListSettings.eventList = ((SetEventListParams) cmdParams).eventInfo;
            break;
}
}

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

dest.writeParcelable(mCallSettings.confirmMsg, 0);
dest.writeParcelable(mCallSettings.callMsg, 0);
break;
        case SET_UP_EVENT_LIST:
            dest.writeIntArray(mSetupEventListSettings.eventList);
            break;
}
}

public CallSettings getCallSettings() {
return mCallSettings;
}

    public SetupEventListSettings getSetEventList() {
        return mSetupEventListSettings;
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;
        int eventValue = -1;
        byte[] addedInfo = null;
        boolean includeAdditionalInfo = false;
        int additionalInfo = 0;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
this.usersInput = input;
}

        public void setEventDownload(int event,byte[] addedInfo) {
            this.eventValue = event;
            this.addedInfo = addedInfo;
        }

public void setYesNo(boolean yesNo) {
usersYesNoSelection = yesNo;
}
usersConfirm = confirm;
}

        public void setAdditionalInfo(boolean includeAdditionalInfo, int additionalInfo) {
            this.includeAdditionalInfo = includeAdditionalInfo;
            this.additionalInfo = additionalInfo;
        }

CommandDetails getCmdDetails() {
return cmdDet;
}
\ No newline at end of file
    }

//<End of snippet n. 2>










//<Beginning of snippet n. 3>



import java.io.ByteArrayOutputStream;

import static  com.android.internal.telephony.cat.CatCmdMessage.SetupEventListConstants.*;

/**
* Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
* you want to get the actual value, call {@link #value() value} method.
}
}


    /**  This function validates the events in SETUP_EVENT_LIST which are currently
     *   supported by the Android framework. In case of SETUP_EVENT_LIST has NULL events
     *   or no events, all the events need to be reset.
     */
    private boolean isValidSetupEventList(CatCmdMessage cmdMsg) {
        boolean flag = true;

        for (int i = 0; i < cmdMsg.getSetEventList().eventList.length ; i++) {
            int eventval = cmdMsg.getSetEventList().eventList[i];
            CatLog.d(this,"Event: "+eventval);
            switch (eventval) {
                /* Currently android is supporting only the below events in SetupEventList
                 * Browser Termination,
                 * Idle Screen Available and
                 * Language Selection.  */
                case BROWSER_TERMINATION_EVENT:
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
* Handles RIL_UNSOL_STK_PROACTIVE_COMMAND unsolicited command from RIL.
* Sends valid proactive command data to the application using intents.
case SET_UP_IDLE_MODE_TEXT:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
            case SET_UP_EVENT_LIST:
                if (isValidSetupEventList(cmdMsg)) {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                } else {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY,
                            false, 0, null);
                }
                break;
case PROVIDE_LOCAL_INFORMATION:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
return;

private void encodeOptionalTags(CommandDetails cmdDet,
ResultCode resultCode, Input cmdInput, ByteArrayOutputStream buf) {
        AppInterface.CommandType cmdType = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        if (cmdType != null) {
            switch (cmdType) {
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
        } else {
            CatLog.d(this, "encodeOptionalTags() Unsupported Command Type:" + cmdDet.typeOfCommand);
}
}

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
            case BROWSER_TERMINATION_EVENT:
                CatLog.d(sInstance, " Sending Browser termination event download to ICC");
                tag = 0x80 | ComprehensionTlvTag.BROWSER_TERMINATION_CAUSE.value();
                buf.write(tag);
                // Browser Termination length should be 1 byte
                buf.write(0x01);
                break;
            case IDLE_SCREEN_AVAILABLE_EVENT:
                CatLog.d(sInstance, " Sending Idle Screen Available event download to ICC");
                break;
            case LANGUAGE_SELECTION_EVENT:
                CatLog.d(sInstance, " Sending Language Selection event download to ICC");
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

String hexString = IccUtils.bytesToHexString(rawData);

        if (Config.LOGD) {
            CatLog.d(this, "ENVELOPE COMMAND: " + hexString);
        }

mCmdIf.sendEnvelope(hexString, null);
}

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
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
        case LAUNCH_BROWSER_ERROR:
            AppInterface.CommandType cmdType = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
            if (cmdType != null) {
                switch (cmdType) {
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
                    case SET_UP_EVENT_LIST:
                        if (IDLE_SCREEN_AVAILABLE_EVENT == resMsg.eventValue) {
                            eventDownload(resMsg.eventValue, DEV_ID_DISPLAY, DEV_ID_UICC,
                                    resMsg.addedInfo, false);
                        } else {
                            eventDownload(resMsg.eventValue, DEV_ID_TERMINAL, DEV_ID_UICC,
                                    resMsg.addedInfo, false);
                        }
                        // No need to send the terminal response after event download.
                        mCurrntCmd = null;
                        return;
}
            } else {
                CatLog.d(this, "Unsupported Command Type:" + cmdDet.typeOfCommand);
}
break;
case NO_RESPONSE_FROM_USER:

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


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

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


import java.util.Iterator;
import java.util.List;

import static com.android.internal.telephony.cat.CatCmdMessage.SetupEventListConstants.*;

/**
* Factory class, used for decoding raw byte arrays, received from baseband,
* into a CommandParams object.
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
             case SET_UP_EVENT_LIST:
                    cmdPending = processSetUpEventList(cmdDet, ctlvs);
                    break;
case PROVIDE_LOCAL_INFORMATION:
cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
break;
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

//<End of snippet n. 5>








