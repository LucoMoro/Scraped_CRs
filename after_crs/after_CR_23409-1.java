/*Stk App: Implementing BIP commands and displaying AlphaID

This change implements the BIP commands
OPEN_CHANNEL, CLOSE_CHANNEL,RECEIVE_DATA,SEND_DATA,GET_CHANNEL_STATUS
and displays the AlphaId to user for confirmation.

Depends on changes:
Ibb663

Change-Id:Ie25e01866c835b11e8ed15c10275dd6a4e88bfce*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..1abb357 100644

//Synthetic comment -- @@ -432,6 +432,16 @@
case LAUNCH_BROWSER:
launchConfirmationDialog(mCurrentCmd.geTextMessage());
break;
        case CLOSE_CHANNEL:
        case RECEIVE_DATA:
        case SEND_DATA:
        case GET_CHANNEL_STATUS:
            waitForUsersResponse = false;
            launchEventMessage();
            break;
        case OPEN_CHANNEL:
            launchConfirmationDialog(mCurrentCmd.getCallSettings().confirmMsg);
            break;
case SET_UP_CALL:
launchConfirmationDialog(mCurrentCmd.getCallSettings().confirmMsg);
break;







