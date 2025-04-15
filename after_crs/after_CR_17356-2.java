/*Telephony: Proper handling of Incorrect Proactive Commands.

For Incorrect Proactive Commands that couldn't be decoded successfully,
fill 0x00 for Command Details. As per spec TS 102.223 section 6.8.1, the
UICC shall interpret a Terminal Response with a Command Number '00' as
belonging to the last sent proactive command.

Change-Id:I188ac28ee0f73b99928e7e99e44e9669dc3ebce1*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 5a994f3..fbef1ca 100644

//Synthetic comment -- @@ -178,6 +178,19 @@
sendTerminalResponse(cmdParams.cmdDet, rilMsg.mResCode,
false, 0, null);
}
            } else {
                // Sometimes decoder could not even decode the COMMAND DETAILS
                // because of invalid data. In that case fill 0x00 for COMMAND
                // DETAILS. As per spec TS 102.223 section 6.8.1, the UICC shall
                // interpret a Terminal Response with a command number '00' as
                // belonging to the last sent proactive command.
                CommandDetails lastCmdDet = new CommandDetails();
                lastCmdDet.compRequired = true;
                lastCmdDet.commandNumber = 0x00;
                lastCmdDet.typeOfCommand = 0x00;
                lastCmdDet.commandQualifier = 0x00;
                sendTerminalResponse(lastCmdDet, rilMsg.mResCode,
                      false, 0, null);
}
break;
case MSG_ID_REFRESH:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/RilMessageDecoder.java b/telephony/java/com/android/internal/telephony/cat/RilMessageDecoder.java
//Synthetic comment -- index 2a1f508..f763708 100644

//Synthetic comment -- @@ -163,6 +163,11 @@
} catch (ResultException e) {
// send to Service for proper RIL communication.
mCurrentRilMessage.mResCode = e.result();
                // Incase of incorrect PROACTIVE COMMAND DATA, CommandParams
                // will not be decoded. So initialize it to null, otherwise
                // this will lead to Class Cast Exception when type casted to
                // RilMessage in CatService.
                mCurrentRilMessage.mData = null;
sendCmdForExecution(mCurrentRilMessage);
decodingStarted = false;
}







