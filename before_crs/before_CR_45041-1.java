/*Telephony: Some Stk related bug fixes

1. As per TS 102.223 Annex C, Structure of CAT communications,
the APDU length can be max 255 bytes. This leaves only 239 bytes for user
input string. CMD details TLV + Device IDs TLV + Result TLV + Other
details of TextString TLV not including user input take 16 bytes.

If UCS2 encoding is used, maximum 118 UCS2 chars can be encoded in 238 bytes.
Each UCS2 char takes 2 bytes. Byte Order Mask(BOM), 0xFEFF takes 2 bytes.

If GSM 7 bit default(use 8 bits to represent a 7 bit char) format is used,
maximum 239 chars can be encoded in 239 bytes since each char takes 1 byte.

No issues for GSM 7 bit packed format encoding.

2.CR flag in command details(CD) TLV is always set as true and
sent to Stk app. When CR flag in CD TLV is false in the proactive
command, there is a mismatch of CD TLV data between Stk framework
and Stk app. Terminal Response (TR) is not sent for the proactive
command. Fixed this issue by sending correct CR flag to Stk App.

3.Launch Browser related testcases in 3GPP 31.124 Sec. 27.22.4.26.2
requires that additional information be sent in terminal response.

4.The GCF Testcase GCF-PTCRB SAT/USAT 27.22.4.1.1seq1.2 expects a screen busy
response if the ME is not in idle screen and if the display text message is a
low priority message. Hence sending screen busy response instead of
RESPONSE_TIMEOUT. In present implementation a USER_RESPONSE_TIMEOUT response
is sent for all timeout cases.

5.The Comprehension required bit is always set for result tag. This should
set if comprehension required field is set by the card.

Change-Id:I7d09c8c7b68b2fd337a0f54808530964ce645250*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatResponseMessage.java b/src/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..00349f4 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
//Synthetic comment -- @@ -48,6 +50,11 @@
usersConfirm = confirm;
}

CommandDetails getCmdDetails() {
return cmdDet;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..f5fb9c6 100644

//Synthetic comment -- @@ -385,7 +385,9 @@
buf.write(DEV_ID_UICC); // destination device id

// result
tag = 0x80 | ComprehensionTlvTag.RESULT.value();
buf.write(tag);
int length = includeAdditionalInfo ? 2 : 1;
buf.write(length);
//Synthetic comment -- @@ -719,6 +721,7 @@
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
case SET_UP_MENU:
helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
//Synthetic comment -- @@ -754,6 +757,11 @@
return;
}
break;
case NO_RESPONSE_FROM_USER:
case UICC_SESSION_TERM_BY_USER:
case BACKWARD_MOVE_BY_USER:
//Synthetic comment -- @@ -763,7 +771,8 @@
default:
return;
}
        sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
mCurrntCmd = null;
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index a554012..5eaacc8 100644

//Synthetic comment -- @@ -56,6 +56,22 @@
static final int DTTZ_SETTING                           = 0x03;
static final int LANGUAGE_SETTING                       = 0x04;

static synchronized CommandParamsFactory getInstance(RilMessageDecoder caller,
IccFileHandler fh) {
if (sInstance != null) {
//Synthetic comment -- @@ -480,6 +496,18 @@
input.packed = (cmdDet.commandQualifier & 0x08) != 0;
input.helpAvailable = (cmdDet.commandQualifier & 0x80) != 0;

mCmdParams = new GetInputParams(cmdDet, input);

if (iconId != null) {







