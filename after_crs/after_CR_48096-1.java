/*CatService: Add support for GET_CHANNEL_STATUS p-cmd.

Change-Id:Ib0782748fdbdc94e5c13cffadb40556e3f324eff*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 299e140..01b86ef 100644

//Synthetic comment -- @@ -64,7 +64,8 @@
OPEN_CHANNEL(0x40),
CLOSE_CHANNEL(0x41),
RECEIVE_DATA(0x42),
        SEND_DATA(0x43),
        GET_CHANNEL_STATUS(0x44);

private int mValue;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatCmdMessage.java b/src/java/com/android/internal/telephony/cat/CatCmdMessage.java
//Synthetic comment -- index 48c2e2b..9284d03 100644

//Synthetic comment -- @@ -80,6 +80,9 @@
mToneSettings = params.settings;
mTextMsg = params.textMsg;
break;
        case GET_CHANNEL_STATUS:
            mTextMsg = ((CallSetupParams) cmdParams).confirmMsg;
            break;
case SET_UP_CALL:
mCallSettings = new CallSettings();
mCallSettings.confirmMsg = ((CallSetupParams) cmdParams).confirmMsg;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index bd06a75..ed30279 100644

//Synthetic comment -- @@ -150,6 +150,7 @@
case SEND_USSD:
cmdPending = processEventNotify(cmdDet, ctlvs);
break;
             case GET_CHANNEL_STATUS:
case SET_UP_CALL:
cmdPending = processSetupCall(cmdDet, ctlvs);
break;







