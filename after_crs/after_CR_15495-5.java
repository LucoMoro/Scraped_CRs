/*SIM toolkit enhancements and bug fixes

 - Correct the Terminal response for GET_INKEY variable timeout.
   GCF PTCRB Testcase GCF_PTCRB_USAT_GetInkey_27.22.4.2.8.1 checks for
   DURATION TLV in the terminal response. Add DURATION TLV in the terminal
   response for GET INKEY.

 - According to TS 102.223/TS 31.111 section 6.8, Structure of TERMINAL RESPONSE,
   "For all SIMPLE-TLV objects with Min=N, the ME should set the
   CR(comprehension required) flag to comprehension not required.(CR=0)"
   Since DEVICE_IDENTITIES and DURATION TLVs have Min=N, the CR flag is not set.

 - Add support for Provide Local Information(PLI) Language setting. Send
   terminal reponse with the locale language setting value read from
   "persist.sys.language" system property.

 - Send TR with BEYOND_TERMINAL_CAPABILITY for unsupported proactive commands

 - Fix length coding for Text String in terminal response for GET INPUT.

Change-Id:I762b064f02f44772809f8bb029e8cefb838e7766*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/AppInterface.java b/telephony/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 0ba3e11..2eb6ccb 100644

//Synthetic comment -- @@ -58,7 +58,8 @@
SET_UP_EVENT_LIST(0x05),
SET_UP_IDLE_MODE_TEXT(0x28),
SET_UP_MENU(0x25),
        SET_UP_CALL(0x10),
        PROVIDE_LOCAL_INFORMATION(0x26);

private int mValue;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index b916713..1e23e34 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
//Synthetic comment -- @@ -245,48 +246,46 @@

CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
switch (cmdParams.getCommandType()) {
            case SET_UP_MENU:
                if (removeMenu(cmdMsg.getMenu())) {
                    mMenuCmd = null;
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
                // ME side only handles refresh commands which meant to remove IDLE
                // MODE TEXT.
                cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT.value();
                break;
            case SET_UP_IDLE_MODE_TEXT:
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                break;
            case PROVIDE_LOCAL_INFORMATION:
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                return;
            case LAUNCH_BROWSER:
            case SELECT_ITEM:
            case GET_INPUT:
            case GET_INKEY:
            case SEND_DTMF:
            case SEND_SMS:
            case SEND_SS:
            case SEND_USSD:
            case PLAY_TONE:
            case SET_UP_CALL:
                // nothing to do on telephony!
                break;
            default:
                CatLog.d(this, "Unsupported command");
                return;
}
mCurrntCmd = cmdMsg;
Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
//Synthetic comment -- @@ -315,6 +314,11 @@
}
ByteArrayOutputStream buf = new ByteArrayOutputStream();

        Input cmdInput = null;
        if (mCurrntCmd != null) {
            cmdInput = mCurrntCmd.geInput();
        }

// command details
int tag = ComprehensionTlvTag.COMMAND_DETAILS.value();
if (cmdDet.compRequired) {
//Synthetic comment -- @@ -327,7 +331,13 @@
buf.write(cmdDet.commandQualifier);

// device identities
        // According to TS102.223/TS31.111 section 6.8 Structure of
        // TERMINAL RESPONSE, "For all SIMPLE-TLV objects with Min=N,
        // the ME should set the CR(comprehension required) flag to
        // comprehension not required.(CR=0)"
        // Since DEVICE_IDENTITIES and DURATION TLVs have Min=N,
        // the CR flag is not set.
        tag = ComprehensionTlvTag.DEVICE_IDENTITIES.value();
buf.write(tag);
buf.write(0x02); // length
buf.write(DEV_ID_TERMINAL); // source device id
//Synthetic comment -- @@ -348,6 +358,8 @@
// Fill optional data for each corresponding command
if (resp != null) {
resp.format(buf);
        } else {
            encodeOptionalTags(cmdDet, resultCode, cmdInput, buf);
}

byte[] rawData = buf.toByteArray();
//Synthetic comment -- @@ -359,6 +371,52 @@
mCmdIf.sendTerminalResponse(hexString, null);
}

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

    private void getInKeyResponse(ByteArrayOutputStream buf, Input cmdInput) {
        int tag = ComprehensionTlvTag.DURATION.value();

        buf.write(tag);
        buf.write(0x02); // length
        buf.write(cmdInput.duration.timeUnit.SECOND.value()); // Time (Unit,Seconds)
        buf.write(cmdInput.duration.timeInterval); // Time Duration
    }

    private void getPliResponse(ByteArrayOutputStream buf) {

        // Locale Language Setting
        String lang = SystemProperties.get("persist.sys.language");

        if (lang != null) {
            // tag
            int tag = ComprehensionTlvTag.LANGUAGE.value();
            buf.write(tag);
            ResponseData.writeLength(buf, lang.length());
            buf.write(lang.getBytes(), 0, lang.length());
        }
    }

private void sendMenuSelection(int menuId, boolean helpRequired) {









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index edb2dc8..12204a0 100644

//Synthetic comment -- @@ -52,6 +52,9 @@
static final int REFRESH_NAA_INIT                       = 0x03;
static final int REFRESH_UICC_RESET                     = 0x04;

    // Command Qualifier values for PLI command
    static final int LANGUAGE_SETTING                       = 0x04;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
IccFileHandler fh) {
if (sInstance != null) {
//Synthetic comment -- @@ -112,7 +115,10 @@
AppInterface.CommandType cmdType = AppInterface.CommandType
.fromInt(cmdDet.typeOfCommand);
if (cmdType == null) {
            // This PROACTIVE COMMAND is presently not handled. Hence set
            // result code as BEYOND_TERMINAL_CAPABILITY in TR.
            mCmdParams = new CommandParams(cmdDet);
            sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
return;
}

//Synthetic comment -- @@ -155,10 +161,13 @@
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
             case PROVIDE_LOCAL_INFORMATION:
                cmdPending = processProvideLocalInfo(cmdDet, ctlvs);
                break;
default:
// unsupported proactive commands
mCmdParams = new CommandParams(cmdDet);
                sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
return;
}
} catch (ResultException e) {
//Synthetic comment -- @@ -380,6 +389,12 @@
iconId = ValueParser.retrieveIconId(ctlv);
}

        // parse duration
        ctlv = searchForTag(ComprehensionTlvTag.DURATION, ctlvs);
        if (ctlv != null) {
            input.duration = ValueParser.retrieveDuration(ctlv);
        }

input.minLen = 1;
input.maxLen = 1;

//Synthetic comment -- @@ -863,4 +878,20 @@
}
return false;
}

    private boolean processProvideLocalInfo(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs)
            throws ResultException {
        CatLog.d(this, "process ProvideLocalInfo");
        switch (cmdDet.commandQualifier) {
            case LANGUAGE_SETTING:
                CatLog.d(this, "PLI [LANGUAGE_SETTING]");
                mCmdParams = new CommandParams(cmdDet);
                break;
            default:
                CatLog.d(this, "PLI[" + cmdDet.commandQualifier + "] Command Not Supported");
                mCmdParams = new CommandParams(cmdDet);
                throw new ResultException(ResultCode.BEYOND_TERMINAL_CAPABILITY);
        }
        return false;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/Input.java b/telephony/java/com/android/internal/telephony/cat/Input.java
//Synthetic comment -- index 8bcaab9..13a5ad4 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
public boolean echo;
public boolean yesNo;
public boolean helpAvailable;
    public Duration duration;

Input() {
text = "";
//Synthetic comment -- @@ -49,6 +50,7 @@
echo = false;
yesNo = false;
helpAvailable = false;
        duration = null;
}

private Input(Parcel in) {
//Synthetic comment -- @@ -63,6 +65,7 @@
echo = in.readInt() == 1 ? true : false;
yesNo = in.readInt() == 1 ? true : false;
helpAvailable = in.readInt() == 1 ? true : false;
        duration = in.readParcelable(null);
}

public int describeContents() {
//Synthetic comment -- @@ -81,6 +84,7 @@
dest.writeInt(echo ? 1 : 0);
dest.writeInt(yesNo ? 1 : 0);
dest.writeInt(helpAvailable ? 1 : 0);
        dest.writeParcelable(duration, 0);
}

public static final Parcelable.Creator<Input> CREATOR = new Parcelable.Creator<Input>() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/ResponseData.java b/telephony/java/com/android/internal/telephony/cat/ResponseData.java
//Synthetic comment -- index 84c08f8..677d66b 100644

//Synthetic comment -- @@ -28,6 +28,16 @@
* the ByteArrayOutputStream object.
*/
public abstract void format(ByteArrayOutputStream buf);

    public static void writeLength(ByteArrayOutputStream buf, int length) {
        // As per ETSI 102.220 Sec7.1.2, if the total length is greater
        // than 0x7F, it should be coded in two bytes and the first byte
        // should be 0x81.
        if (length > 0x7F) {
            buf.write(0x81);
        }
        buf.write(length);
    }
}

class SelectItemResponseData extends ResponseData {
//Synthetic comment -- @@ -120,7 +130,7 @@
}

// length - one more for data coding scheme.
        writeLength(buf, data.length + 1);

// data coding scheme
if (mIsUcs2) {







