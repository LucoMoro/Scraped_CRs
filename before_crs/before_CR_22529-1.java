/*`/system/bin/input text <string>` now sends all text to the device even if it contains spaces

Change-Id:Icc25a668b7ee931a83246da7466f32e2781bec4d*/
//Synthetic comment -- diff --git a/cmds/input/src/com/android/commands/input/Input.java b/cmds/input/src/com/android/commands/input/Input.java
//Synthetic comment -- index 3a1accd..b41dbe5 100755

//Synthetic comment -- @@ -49,7 +49,7 @@
String command = args[0];

if (command.equals("text")) {
            sendText(args[1]);
} else if (command.equals("keyevent")) {
sendKeyEvent(args[1]);
} else if (command.equals("motionevent")) {
//Synthetic comment -- @@ -64,16 +64,28 @@
}

/**
     * Convert the characters of string text into key event's and send to
     * device.
*
     * @param text is a string of characters you want to input to the device.
*/

    private void sendText(String text) {

        StringBuffer buff = new StringBuffer(text);

boolean escapeFlag = false;
for (int i=0; i<buff.length(); i++) {
if (escapeFlag) {







