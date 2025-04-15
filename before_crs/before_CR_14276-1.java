/*Allow Am to start services

Change-Id:I31d066ae2c980cc293e55034446a63a0f42088ad*/
//Synthetic comment -- diff --git a/cmds/am/src/com/android/commands/am/Am.java b/cmds/am/src/com/android/commands/am/Am.java
//Synthetic comment -- index eca5af9..3566caf 100644

//Synthetic comment -- @@ -88,6 +88,8 @@

if (op.equals("start")) {
runStart();
} else if (op.equals("instrument")) {
runInstrument();
} else if (op.equals("broadcast")) {
//Synthetic comment -- @@ -238,6 +240,19 @@
}
}

private void sendBroadcast() throws Exception {
Intent intent = makeIntent();
IntentReceiver receiver = new IntentReceiver();







