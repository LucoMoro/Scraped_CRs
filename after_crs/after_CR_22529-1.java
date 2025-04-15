/*`/system/bin/input text <string>` now sends all text to the device even if it contains spaces

Change-Id:Icc25a668b7ee931a83246da7466f32e2781bec4d*/




//Synthetic comment -- diff --git a/cmds/input/src/com/android/commands/input/Input.java b/cmds/input/src/com/android/commands/input/Input.java
//Synthetic comment -- index 3a1accd..b41dbe5 100755

//Synthetic comment -- @@ -49,7 +49,7 @@
String command = args[0];

if (command.equals("text")) {
            sendText(collectArguments(args, 1));
} else if (command.equals("keyevent")) {
sendKeyEvent(args[1]);
} else if (command.equals("motionevent")) {
//Synthetic comment -- @@ -64,16 +64,28 @@
}

/**
     * Collect remaining arguments into a StringBuffer.
*
     * @param args The argument array
     * @param from The index of the first argument which should be collected
*/
    private StringBuffer collectArguments(String[] args, int from) {
        StringBuffer buff = new StringBuffer();
        // copy all arguments starting from `from` into the buffer
        for (int i=from; i<args.length; i++) {
            if (buff.length() > 0)
                buff.append(' ');
            buff.append(args[i]);
        }
        return buff;
    }

    /**
     * Convert a text into key events and send them to the device.
     *
     * @param buff is a StringBuffer of characters to send to the device
     */
    private void sendText(StringBuffer buff) {
boolean escapeFlag = false;
for (int i=0; i<buff.length(); i++) {
if (escapeFlag) {







