/*Icon support for proactive commands according to 3GPP 31.111

GET_INKEY
GET_INPUT
PLAY_TONE
SETUP_MENU
SELECT_ITEM
SETUP_IDLE_MODE_TEXT
LAUNCH_BROWSER

SEND_SMS*
SEND_SS*
SEND_USSD*
SEND_DTMF*

SETUP_CALL* ,**

-Fixed loading of icons from SIM
-Fixed terminal responses when loading fails for proactive commands
 terminated by the STK, see * for details

*Terminal response with PRFRMD_ICON_NOT_DISPLAYED will not be sent if
 loading of icons fails for proactive commands that are not terminated
 by the STK.

**Terminal response with PRFRMD_ICON_NOT_DISPLAYED will not be sent
  for SETUP_CALL in the case that either the 'confirm call' or the
  'setup call' icon couldent be loaded. This will require update of the
  com.android.internal.telephony.CommandsInterface

Change-Id:I11a696402aab001da1edf247b1dfbe0e207cfcbdSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 93b9b79..2bb9524 100644

//Synthetic comment -- @@ -163,8 +163,7 @@
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        phone.mCM.iccIO(COMMAND_READ_RECORD, IccConstants.EF_IMG, getEFPath(EF_IMG),
recordNum, READ_RECORD_MODE_ABSOLUTE,
GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}
//Synthetic comment -- @@ -236,8 +235,8 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(EF_IMG),
                highOffset, lowOffset, length, null, null, response);
}

/**
//Synthetic comment -- @@ -310,6 +309,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
                } else {
                    sendResult(response, result.payload, null);
}
break;
case EVENT_READ_ICON_DONE:
//Synthetic comment -- @@ -320,6 +321,8 @@
iccException = result.getException();
if (iccException != null) {
sendResult(response, result.payload, ar.exception);
                } else {
                    sendResult(response, result.payload, null);
}
break;
case EVENT_GET_EF_LINEAR_RECORD_SIZE_DONE:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 74af9fa..97f7f12 100644

//Synthetic comment -- @@ -176,7 +176,8 @@
break;
}
if (cmdParams != null) {
                if (rilMsg.mResCode == ResultCode.OK ||
                    rilMsg.mResCode == ResultCode.PRFRMD_ICON_NOT_DISPLAYED) {
handleCommand(cmdParams, true);
} else {
// for proactive commands that couldn't be decoded
//Synthetic comment -- @@ -212,6 +213,7 @@
*/
private void handleCommand(CommandParams cmdParams, boolean isProactiveCmd) {
CatLog.d(this, cmdParams.getCommandType().name());
        ResultCode result = ResultCode.OK;

CharSequence message;
CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
//Synthetic comment -- @@ -221,13 +223,19 @@
mMenuCmd = null;
} else {
mMenuCmd = cmdMsg;
                    if (cmdMsg.getMenu().iconLoadingFailed) {
                        result = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                    }
}
                sendTerminalResponse(cmdParams.cmdDet, result, false, 0, null);
break;
case DISPLAY_TEXT:
// when application is not required to respond, send an immediate response.
if (!cmdMsg.geTextMessage().responseNeeded) {
                    if (cmdMsg.geTextMessage().iconLoadingFailed) {
                        result = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                    }
                    sendTerminalResponse(cmdParams.cmdDet, result, false, 0, null);
}
break;
case REFRESH:
//Synthetic comment -- @@ -236,7 +244,10 @@
cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT.value();
break;
case SET_UP_IDLE_MODE_TEXT:
                if (cmdMsg.geTextMessage().iconLoadingFailed) {
                    result = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                }
                sendTerminalResponse(cmdParams.cmdDet, result, false, 0, null);
break;
case PROVIDE_LOCAL_INFORMATION:
ResponseData resp;
//Synthetic comment -- @@ -694,15 +705,21 @@
case PRFRMD_TONE_NOT_PLAYED:
switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
case SET_UP_MENU:
sendMenuSelection(resMsg.usersMenuSelection, helpRequired);
return;
case SELECT_ITEM:
                Menu menu = mCurrntCmd.getMenu();
                if (resMsg.resCode == ResultCode.OK && menu.iconLoadingFailed) {
                    resMsg.resCode = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                }
resp = new SelectItemResponseData(resMsg.usersMenuSelection);
break;
case GET_INPUT:
case GET_INKEY:
Input input = mCurrntCmd.geInput();
                if (resMsg.resCode == ResultCode.OK && input.iconLoadingFailed) {
                    resMsg.resCode = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                }
if (!input.yesNo) {
// when help is requested there is no need to send the text
// string object.
//Synthetic comment -- @@ -715,10 +732,24 @@
resMsg.usersYesNoSelection);
}
break;
            case SEND_DTMF:
            case SEND_SMS:
            case SEND_SS:
            case SEND_USSD:
            case PLAY_TONE:
case DISPLAY_TEXT:
case LAUNCH_BROWSER:
                TextMessage text = mCurrntCmd.geTextMessage();
                if (resMsg.resCode == ResultCode.OK && text.iconLoadingFailed) {
                    resMsg.resCode = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
                }
break;
case SET_UP_CALL:
                // FIXME Add support to transport indication of icon loading
                // failed to handleCallSetupRequestFromSim. If any of
                // confirmMsg.iconLoadingFailed or callMsg.iconLoadingFailed is
                // true then a terminal response with PRFRMD_ICON_NOT_DISPLAYED
                // shall be sent.
mCmdIf.handleCallSetupRequestFromSim(resMsg.usersConfirm, null);
// No need to send terminal response for SET UP CALL. The user's
// confirmation result is send back using a dedicated ril message








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParams.java b/telephony/java/com/android/internal/telephony/cat/CommandParams.java
//Synthetic comment -- index 959c9e2..d717725 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
}

boolean setIcon(Bitmap icon) { return true; }

}

class DisplayTextParams extends CommandParams {
//Synthetic comment -- @@ -45,9 +46,13 @@
}

boolean setIcon(Bitmap icon) {
        if (textMsg != null) {
            if (icon != null) {
                textMsg.icon = icon;
                return true;
            } else {
                textMsg.iconLoadingFailed = true;
            }
}
return false;
}
//Synthetic comment -- @@ -67,9 +72,13 @@
}

boolean setIcon(Bitmap icon) {
        if (confirmMsg != null) {
            if (icon != null) {
                confirmMsg.icon = icon;
                return true;
            } else {
                confirmMsg.iconLoadingFailed = true;
            }
}
return false;
}
//Synthetic comment -- @@ -87,9 +96,13 @@
}

boolean setIcon(Bitmap icon) {
        if (textMsg != null) {
            if (icon != null) {
                textMsg.icon = icon;
                return true;
            } else {
                textMsg.iconLoadingFailed = true;
            }
}
return false;
}
//Synthetic comment -- @@ -107,15 +120,20 @@
}

boolean setIcon(Bitmap icon) {
if (confirmMsg != null && confirmMsg.icon == null) {
            if (icon == null) {
                confirmMsg.iconLoadingFailed = true;
            } else {
                confirmMsg.icon = icon;
                return true;
            }
} else if (callMsg != null && callMsg.icon == null) {
            if (icon == null) {
                callMsg.iconLoadingFailed = true;
            } else {
                callMsg.icon = icon;
                return true;
            }
}
return false;
}
//Synthetic comment -- @@ -132,19 +150,23 @@
}

boolean setIcon(Bitmap icon) {
        if (menu != null) {
            if (icon != null) {
                if (loadTitleIcon && menu.titleIcon == null) {
                    menu.titleIcon = icon;
                } else {
                    for (Item item : menu.items) {
                        if (item.icon != null) {
                            continue;
                        }
                        item.icon = icon;
                        break;
}
}
                return true;
            } else {
                menu.iconLoadingFailed = true;
}
}
return false;
}
//Synthetic comment -- @@ -159,10 +181,15 @@
}

boolean setIcon(Bitmap icon) {
        if (input != null) {
            if (icon != null) {
                input.icon = icon;
                return true;
            } else {
                input.iconLoadingFailed = true;
            }
}
        return false;
}
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index 89c1329..336056e 100644

//Synthetic comment -- @@ -198,9 +198,9 @@

private ResultCode setIcons(Object data) {
Bitmap[] icons = null;

if (data == null) {
            mCmdParams.setIcon(null);
return ResultCode.PRFRMD_ICON_NOT_DISPLAYED;
}
switch(mIconLoadState) {
//Synthetic comment -- @@ -394,6 +394,7 @@
ctlv = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
if (ctlv != null) {
iconId = ValueParser.retrieveIconId(ctlv);
            input.iconSelfExplanatory = iconId.selfExplanatory;
}

// parse duration
//Synthetic comment -- @@ -470,6 +471,7 @@
ctlv = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
if (ctlv != null) {
iconId = ValueParser.retrieveIconId(ctlv);
            input.iconSelfExplanatory = iconId.selfExplanatory;
}

input.digitOnly = (cmdDet.commandQualifier & 0x01) == 0;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/IconLoader.java b/telephony/java/com/android/internal/telephony/cat/IconLoader.java
//Synthetic comment -- index 2fa1811..03b8aa3 100644

//Synthetic comment -- @@ -151,6 +151,8 @@
} else if (mId.codingScheme == ImageDescriptor.CODING_SCHEME_COLOUR) {
mIconData = rawData;
readClut();
                } else {
                    throw new Exception("Unsupported coding scheme");
}
break;
case EVENT_READ_CLUT_DONE:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/Input.java b/telephony/java/com/android/internal/telephony/cat/Input.java
//Synthetic comment -- index 13a5ad4..ccaf729 100644

//Synthetic comment -- @@ -28,6 +28,8 @@
public String text;
public String defaultText;
public Bitmap icon;
    public boolean iconSelfExplanatory;
    public boolean iconLoadingFailed;
public int minLen;
public int maxLen;
public boolean ucs2;
//Synthetic comment -- @@ -42,6 +44,8 @@
text = "";
defaultText = null;
icon = null;
        iconSelfExplanatory = false;
        iconLoadingFailed = false;
minLen = 0;
maxLen = 1;
ucs2 = false;
//Synthetic comment -- @@ -57,6 +61,8 @@
text = in.readString();
defaultText = in.readString();
icon = in.readParcelable(null);
        iconSelfExplanatory = in.readInt() == 1 ? true : false;
        iconLoadingFailed = in.readInt() == 1 ? true : false;
minLen = in.readInt();
maxLen = in.readInt();
ucs2 = in.readInt() == 1 ? true : false;
//Synthetic comment -- @@ -76,6 +82,8 @@
dest.writeString(text);
dest.writeString(defaultText);
dest.writeParcelable(icon, 0);
        dest.writeInt(iconSelfExplanatory ? 1 : 0);
        dest.writeInt(iconLoadingFailed ? 1 : 0);
dest.writeInt(minLen);
dest.writeInt(maxLen);
dest.writeInt(ucs2 ? 1 : 0);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/Menu.java b/telephony/java/com/android/internal/telephony/cat/Menu.java
//Synthetic comment -- index 7bbae01..15e37dcd 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
public PresentationType presentationType;
public String title;
public Bitmap titleIcon;
    public boolean iconLoadingFailed;
public int defaultItem;
public boolean softKeyPreferred;
public boolean helpAvailable;
//Synthetic comment -- @@ -50,6 +51,7 @@
titleIconSelfExplanatory = false;
itemsIconSelfExplanatory = false;
titleIcon = null;
        iconLoadingFailed = false;
// set default style to be navigation menu.
presentationType = PresentationType.NAVIGATION_OPTIONS;
}
//Synthetic comment -- @@ -57,6 +59,7 @@
private Menu(Parcel in) {
title = in.readString();
titleIcon = in.readParcelable(null);
        iconLoadingFailed = in.readInt() == 1 ? true : false;
// rebuild items list.
items = new ArrayList<Item>();
int size = in.readInt();
//Synthetic comment -- @@ -79,6 +82,7 @@
public void writeToParcel(Parcel dest, int flags) {
dest.writeString(title);
dest.writeParcelable(titleIcon, flags);
        dest.writeInt(iconLoadingFailed ? 1 : 0);
// write items list to the parcel.
int size = items.size();
dest.writeInt(size);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/TextMessage.java b/telephony/java/com/android/internal/telephony/cat/TextMessage.java
//Synthetic comment -- index 5ffd076..e2db0ef 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
public String text = null;
public Bitmap icon = null;
public boolean iconSelfExplanatory = false;
    public boolean iconLoadingFailed = false;
public boolean isHighPriority = false;
public boolean responseNeeded = true;
public boolean userClear = false;
//Synthetic comment -- @@ -38,6 +39,7 @@
text = in.readString();
icon = in.readParcelable(null);
iconSelfExplanatory = in.readInt() == 1 ? true : false;
        iconLoadingFailed = in.readInt() == 1 ? true : false;
isHighPriority = in.readInt() == 1 ? true : false;
responseNeeded = in.readInt() == 1 ? true : false;
userClear = in.readInt() == 1 ? true : false;
//Synthetic comment -- @@ -53,6 +55,7 @@
dest.writeString(text);
dest.writeParcelable(icon, 0);
dest.writeInt(iconSelfExplanatory ? 1 : 0);
        dest.writeInt(iconLoadingFailed ? 1 : 0);
dest.writeInt(isHighPriority ? 1 : 0);
dest.writeInt(responseNeeded ? 1 : 0);
dest.writeInt(userClear ? 1 : 0);







