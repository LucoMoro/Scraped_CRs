/*Telephony/cat: Implementing BIP commands and displaying AlphaID

This change implements the BIP command OPEN_CHANNEL, CLOSE_CHANNEL,
SEND_DATA, RECEIVE_DATA and displays the AlphaId to user for confirmation.

Change-Id:Ibb6631e0ce9eaff6337261fe1ba4e05409759bfc*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/AppInterface.java b/telephony/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 2eb6ccb..89879cf 100644

//Synthetic comment -- @@ -59,6 +59,11 @@
SET_UP_IDLE_MODE_TEXT(0x28),
SET_UP_MENU(0x25),
SET_UP_CALL(0x10),
PROVIDE_LOCAL_INFORMATION(0x26);

private int mValue;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatCmdMessage.java b/telephony/java/com/android/internal/telephony/cat/CatCmdMessage.java
//Synthetic comment -- index 5155bb2..985112b 100644

//Synthetic comment -- @@ -80,6 +80,13 @@
mToneSettings = params.settings;
mTextMsg = params.textMsg;
break;
case SET_UP_CALL:
mCallSettings = new CallSettings();
mCallSettings.confirmMsg = ((CallSetupParams) cmdParams).confirmMsg;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 36059ad..d203fa5 100644

//Synthetic comment -- @@ -273,6 +273,13 @@
case PROVIDE_LOCAL_INFORMATION:
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
return;
case LAUNCH_BROWSER:
case SELECT_ITEM:
case GET_INPUT:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index 12204a0..e9a011c 100644

//Synthetic comment -- @@ -148,6 +148,11 @@
case SEND_USSD:
cmdPending = processEventNotify(cmdDet, ctlvs);
break;
case SET_UP_CALL:
cmdPending = processSetupCall(cmdDet, ctlvs);
break;







