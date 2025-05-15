
//<Beginning of snippet n. 0>


String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;
        boolean includeAdditionalInfo = false;
        int additionalInfo = 0;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
usersConfirm = confirm;
}

        public void setAdditionalInfo(boolean includeAdditionalInfo, int additionalInfo) {
            this.includeAdditionalInfo = includeAdditionalInfo;
            this.additionalInfo = additionalInfo;
        }

CommandDetails getCmdDetails() {
return cmdDet;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


buf.write(DEV_ID_UICC); // destination device id

// result
        if (cmdDet.compRequired) {
tag = 0x80 | ComprehensionTlvTag.RESULT.value();
        }
buf.write(tag);
int length = includeAdditionalInfo ? 2 : 1;
buf.write(length);
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
        case LAUNCH_BROWSER_ERROR:
switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
case SET_UP_MENU:
helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
return;
}
break;
        case TERMINAL_CRNTLY_UNABLE_TO_PROCESS:
            //For screenbusy case there will be addtional information in the terminal
            //response. And the value of the additional information byte is 0x01.
            resMsg.includeAdditionalInfo = true;
            resMsg.additionalInfo = 0x01;
case NO_RESPONSE_FROM_USER:
case UICC_SESSION_TERM_BY_USER:
case BACKWARD_MOVE_BY_USER:
default:
return;
}
        sendTerminalResponse(cmdDet, resMsg.resCode, resMsg.includeAdditionalInfo,
                resMsg.additionalInfo, resp);
mCurrntCmd = null;
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


static final int DTTZ_SETTING                           = 0x03;
static final int LANGUAGE_SETTING                       = 0x04;

    // As per TS 102.223 Annex C, Structure of CAT communications,
    // the APDU length can be max 255 bytes. This leaves only 239 bytes for user
    // input string. CMD details TLV + Device IDs TLV + Result TLV + Other
    // details of TextString TLV not including user input take 16 bytes.
    //
    // If UCS2 encoding is used, maximum 118 UCS2 chars can be encoded in 238 bytes.
    // Each UCS2 char takes 2 bytes. Byte Order Mask(BOM), 0xFEFF takes 2 bytes.
    //
    // If GSM 7 bit default(use 8 bits to represent a 7 bit char) format is used,
    // maximum 239 chars can be encoded in 239 bytes since each char takes 1 byte.
    //
    // No issues for GSM 7 bit packed format encoding.

    private static final int MAX_GSM7_DEFAULT_CHARS = 239;
    private static final int MAX_UCS2_CHARS = 118;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
IccFileHandler fh) {
if (sInstance != null) {
input.packed = (cmdDet.commandQualifier & 0x08) != 0;
input.helpAvailable = (cmdDet.commandQualifier & 0x80) != 0;

        // Truncate the maxLen if it exceeds the max number of chars that can
        // be encoded. Limit depends on DCS in Command Qualifier.
        if (input.ucs2 && input.maxLen > MAX_UCS2_CHARS) {
            CatLog.d(this, "UCS2: received maxLen = " + input.maxLen +
                  ", truncating to " + MAX_UCS2_CHARS);
            input.maxLen = MAX_UCS2_CHARS;
        } else if (!input.packed && input.maxLen > MAX_GSM7_DEFAULT_CHARS) {
            CatLog.d(this, "GSM 7Bit Default: received maxLen = " + input.maxLen +
                  ", truncating to " + MAX_GSM7_DEFAULT_CHARS);
            input.maxLen = MAX_GSM7_DEFAULT_CHARS;
        }

mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {

//<End of snippet n. 2>








