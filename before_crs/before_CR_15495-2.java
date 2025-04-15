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

 - Send SCREEN_BUSY response for DISPLAY_TEXT proactive command

Change-Id:I762b064f02f44772809f8bb029e8cefb838e7766*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/AppInterface.java b/telephony/java/com/android/internal/telephony/gsm/stk/AppInterface.java
//Synthetic comment -- index 58f1f97..cafdc64 100644

//Synthetic comment -- @@ -58,7 +58,8 @@
SET_UP_EVENT_LIST(0x05),
SET_UP_IDLE_MODE_TEXT(0x28),
SET_UP_MENU(0x25),
        SET_UP_CALL(0x10);

private int mValue;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/gsm/stk/CommandParamsFactory.java
//Synthetic comment -- index ce4c459..8f8d4f3 100644

//Synthetic comment -- @@ -52,6 +52,9 @@
static final int REFRESH_NAA_INIT                       = 0x03;
static final int REFRESH_UICC_RESET                     = 0x04;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
SIMFileHandler fh) {
if (sInstance != null) {
//Synthetic comment -- @@ -112,7 +115,10 @@
AppInterface.CommandType cmdType = AppInterface.CommandType
.fromInt(cmdDet.typeOfCommand);
if (cmdType == null) {
            sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
return;
}

//Synthetic comment -- @@ -155,10 +161,13 @@
case PLAY_TONE:
cmdPending = processPlayTone(cmdDet, ctlvs);
break;
default:
// unsupported proactive commands
mCmdParams = new CommandParams(cmdDet);
                sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
return;
}
} catch (ResultException e) {
//Synthetic comment -- @@ -380,6 +389,12 @@
iconId = ValueParser.retrieveIconId(ctlv);
}

input.minLen = 1;
input.maxLen = 1;

//Synthetic comment -- @@ -863,4 +878,20 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/Input.java b/telephony/java/com/android/internal/telephony/gsm/stk/Input.java
//Synthetic comment -- index 19f724b..2901264 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
public boolean echo;
public boolean yesNo;
public boolean helpAvailable;

Input() {
text = "";
//Synthetic comment -- @@ -49,6 +50,7 @@
echo = false;
yesNo = false;
helpAvailable = false;
}

private Input(Parcel in) {
//Synthetic comment -- @@ -63,6 +65,7 @@
echo = in.readInt() == 1 ? true : false;
yesNo = in.readInt() == 1 ? true : false;
helpAvailable = in.readInt() == 1 ? true : false;
}

public int describeContents() {
//Synthetic comment -- @@ -81,6 +84,7 @@
dest.writeInt(echo ? 1 : 0);
dest.writeInt(yesNo ? 1 : 0);
dest.writeInt(helpAvailable ? 1 : 0);
}

public static final Parcelable.Creator<Input> CREATOR = new Parcelable.Creator<Input>() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/ResponseData.java b/telephony/java/com/android/internal/telephony/gsm/stk/ResponseData.java
//Synthetic comment -- index afd1bba..1a481f2 100644

//Synthetic comment -- @@ -120,6 +120,12 @@
}

// length - one more for data coding scheme.
buf.write(data.length + 1);

// data coding scheme








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java b/telephony/java/com/android/internal/telephony/gsm/stk/StkService.java
//Synthetic comment -- index 29ed95c..97d735c 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
//Synthetic comment -- @@ -245,48 +246,46 @@

StkCmdMessage cmdMsg = new StkCmdMessage(cmdParams);
switch (cmdParams.getCommandType()) {
        case SET_UP_MENU:
            if (removeMenu(cmdMsg.getMenu())) {
                mMenuCmd = null;
            } else {
                mMenuCmd = cmdMsg;
            }
            sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0,
                    null);
            break;
        case DISPLAY_TEXT:
            // when application is not required to respond, send an immediate
            // response.
            if (!cmdMsg.geTextMessage().responseNeeded) {
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false,
                        0, null);
            }
            break;
        case REFRESH:
            // ME side only handles refresh commands which meant to remove IDLE
            // MODE TEXT.
            cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT
                    .value();
            break;
        case SET_UP_IDLE_MODE_TEXT:
            sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false,
                    0, null);
            break;
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
            StkLog.d(this, "Unsupported command");
            return;
}
mCurrntCmd = cmdMsg;
Intent intent = new Intent(AppInterface.STK_CMD_ACTION);
//Synthetic comment -- @@ -315,6 +314,11 @@
}
ByteArrayOutputStream buf = new ByteArrayOutputStream();

// command details
int tag = ComprehensionTlvTag.COMMAND_DETAILS.value();
if (cmdDet.compRequired) {
//Synthetic comment -- @@ -327,7 +331,13 @@
buf.write(cmdDet.commandQualifier);

// device identities
        tag = 0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value();
buf.write(tag);
buf.write(0x02); // length
buf.write(DEV_ID_TERMINAL); // source device id
//Synthetic comment -- @@ -348,6 +358,8 @@
// Fill optional data for each corresponding command
if (resp != null) {
resp.format(buf);
}

byte[] rawData = buf.toByteArray();
//Synthetic comment -- @@ -359,6 +371,53 @@
mCmdIf.sendTerminalResponse(hexString, null);
}


private void sendMenuSelection(int menuId, boolean helpRequired) {

//Synthetic comment -- @@ -571,6 +630,8 @@
ResponseData resp = null;
boolean helpRequired = false;
CommandDetails cmdDet = resMsg.getCmdDetails();

switch (resMsg.resCode) {
case HELP_INFO_REQUIRED:
//Synthetic comment -- @@ -621,6 +682,12 @@
return;
}
break;
case NO_RESPONSE_FROM_USER:
case UICC_SESSION_TERM_BY_USER:
case BACKWARD_MOVE_BY_USER:
//Synthetic comment -- @@ -629,7 +696,7 @@
default:
return;
}
        sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
mCurrntCmd = null;
}
}







