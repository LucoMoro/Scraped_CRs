/*Sanitise Bluetooth file name hint when receiving a contact

If a contact name is entered with any of the following characters

  :"<>*?|\n\t

the transfer of that contact fails.  This is due to the underlying
filesystem (FAT) not being able to handle these characters in a
file name.  This fix corrects that situation by replacing any
whitespace characters with a space and illegal FAT filesystem
characters with underscore.

Change-Id:I5021bd26a16c31810a61bac3f70439c1153451c2*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiveFileInfo.java b/src/com/android/bluetooth/opp/BluetoothOppReceiveFileInfo.java
//Synthetic comment -- index b5ac274..913e28c 100644

//Synthetic comment -- @@ -244,6 +244,11 @@
// Prevent abuse of path backslashes by converting all backlashes '\\' chars
// to UNIX-style forward-slashes '/'
hint = hint.replace('\\', '/');
if (V) Log.v(Constants.TAG, "getting filename from hint");
int index = hint.lastIndexOf('/') + 1;
if (index > 0) {







