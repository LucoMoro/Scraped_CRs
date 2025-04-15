/*Monkey: Fix to write iteration counter to log only when requested

Change-Id:I1c79aaed56c3d5d9c88d8f296e2ade7091acce83*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 68fb2a5..b3038e0 100644

//Synthetic comment -- @@ -1087,13 +1087,17 @@
eventCounter++;
if (mCountEvents) {
cycleCounter++;
                        if (mScriptLog) {
                            writeScriptLog(cycleCounter);
                        }
}
}
} else {
if (!mCountEvents) {
cycleCounter++;
                    if (mScriptLog) {
                        writeScriptLog(cycleCounter);
                    }
} else {
// Event Source has signaled that we have no more events to process
break;







