/*Monkey: Fix to write iteration counter to log only when requested

Change-Id:Ic303532c6c563417ed22bce9c0b9aca523727131*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 3aa2fc1..5dc07d2 100644

//Synthetic comment -- @@ -1101,7 +1101,9 @@
} else {
if (!mCountEvents) {
cycleCounter++;
                    if (mScriptLog) {
                        writeScriptLog(cycleCounter);
                    }
//Capture the bugreport after n iteration
if (mGetPeriodicBugreport) {
if ((cycleCounter % mBugreportFrequency) == 0) {







