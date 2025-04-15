/*Telephony: add support for additional information

As per ETSI 102 223 section 8.12, for some general results, it is
mandatory for the terminal to provide a specific cause value as
additional information.

This patch adds this support to the framework.

Change-Id:Ie22e60412d674e24f9ebf13d02da4b39cb2dd253Author: Guillaume Lucas <guillaume.lucas@intel.com>
Signed-off-by: Guillaume Lucas <guillaume.lucas@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 29915*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatResponseMessage.java b/src/java/com/android/internal/telephony/cat/CatResponseMessage.java
//Synthetic comment -- index cfcac36..15309e4 100644

//Synthetic comment -- @@ -23,7 +23,8 @@
String usersInput  = null;
boolean usersYesNoSelection = false;
boolean usersConfirm = false;

public CatResponseMessage(CatCmdMessage cmdMsg) {
this.cmdDet = cmdMsg.mCmdDet;
}
//Synthetic comment -- @@ -48,7 +49,12 @@
usersConfirm = confirm;
}

CommandDetails getCmdDetails() {
return cmdDet;
}
    }
\ No newline at end of file








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 52af89e..4eacae3 100644

//Synthetic comment -- @@ -719,6 +719,7 @@
case PRFRMD_WITH_MODIFICATION:
case PRFRMD_NAA_NOT_ACTIVE:
case PRFRMD_TONE_NOT_PLAYED:
switch (AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
case SET_UP_MENU:
helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
//Synthetic comment -- @@ -763,7 +764,8 @@
default:
return;
}
        sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
mCurrntCmd = null;
}








