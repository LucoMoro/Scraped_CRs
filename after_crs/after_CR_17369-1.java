/*stk: launch browser commands send additional information in terminal response

Launch Browser related testcases in 3GPP 31.124 Sec. 27.22.4.26.2
requires that additional information be sent in terminal response.

Change-Id:I1154c1a829808e43bd33472f31856399fc8fda40*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatResponseMessage.java b/telephony/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..5451e03 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;
        boolean includeAdditionalInfo = false;
        int additionalInfo = 0;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
//Synthetic comment -- @@ -48,7 +50,12 @@
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..e98178c 100644

//Synthetic comment -- @@ -644,6 +644,7 @@
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
        case LAUNCH_BROWSER_ERROR:
switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
case SET_UP_MENU:
helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
//Synthetic comment -- @@ -687,7 +688,7 @@
default:
return;
}
        sendTerminalResponse(cmdDet, resMsg.resCode, resMsg.includeAdditionalInfo, resMsg.additionalInfo, resp);
mCurrntCmd = null;
}
}







