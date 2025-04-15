/*Unify am startservice commands.

This replaces the implementation with an equivalent one.

Change-Id:I1343ddee9414a67906cd426b8381ddbace873894*/
//Synthetic comment -- diff --git a/cmds/am/src/com/android/commands/am/Am.java b/cmds/am/src/com/android/commands/am/Am.java
//Synthetic comment -- index 3566caf..13e6d5c 100644

//Synthetic comment -- @@ -88,7 +88,7 @@

if (op.equals("start")) {
runStart();
        } else if (op.equals("start-service")) {
runStartService();
} else if (op.equals("instrument")) {
runInstrument();
//Synthetic comment -- @@ -181,6 +181,15 @@
return intent;
}

private void runStart() throws Exception {
Intent intent = makeIntent();
System.out.println("Starting: " + intent);
//Synthetic comment -- @@ -240,19 +249,6 @@
}
}

    private void runStartService() throws Exception {
        Intent intent = makeIntent();

        if (intent != null) {
            System.out.println("Starting: " + intent);
            try {
                mAm.startService(null, intent, intent.getType());
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        }
    }

private void sendBroadcast() throws Exception {
Intent intent = makeIntent();
IntentReceiver receiver = new IntentReceiver();
//Synthetic comment -- @@ -507,6 +503,8 @@
"    start an Activity: am start [-D] <INTENT>\n" +
"        -D: enable debugging\n" +
"\n" +
"    send a broadcast Intent: am broadcast <INTENT>\n" +
"\n" +
"    start an Instrumentation: am instrument [flags] <COMPONENT>\n" +







