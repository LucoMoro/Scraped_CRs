/*CatService: OPEN_CHANNEL p-cmd to send event confirmation and not T.R

Upon handling OPEN_CHANNEL command, send User confirmation instead of
terminal response as lower layers expect confirmation information.
CRs-Fixed: 390425

Change-Id:Ie9e162d87bae18c011edede238ded7eee65f8766*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index f327d31..76bf07d 100644

//Synthetic comment -- @@ -745,6 +745,8 @@
case DISPLAY_TEXT:
case LAUNCH_BROWSER:
break;
            // 3GPP TS.102.223: Open Channel alpha confirmation should not send TR
            case OPEN_CHANNEL:
case SET_UP_CALL:
mCmdIf.handleCallSetupRequestFromSim(resMsg.usersConfirm, null);
// No need to send terminal response for SET UP CALL. The user's







