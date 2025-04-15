/*Refactor a method to extract an emulator port.

Change-Id:I0dbab2c8f44a2114364075a91f31aa0d8d18d3ba*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index 43d0c0c..d34f203 100644

//Synthetic comment -- @@ -202,46 +202,58 @@
*/
public static synchronized EmulatorConsole getConsole(IDevice d) {
// we need to make sure that the device is an emulator
        Matcher m = sEmulatorRegexp.matcher(d.getSerialNumber());
if (m.matches()) {
// get the port number. This is the console port.
int port;
try {
port = Integer.parseInt(m.group(1));
                if (port <= 0) {
                    return null;
}
} catch (NumberFormatException e) {
// looks like we failed to get the port number. This is a bit strange since
// it's coming from a regexp that only accept digit, but we handle the case
// and return null.
                return null;
}

            EmulatorConsole console = sEmulators.get(port);

            if (console != null) {
                // if the console exist, we ping the emulator to check the connection.
                if (console.ping() == false) {
                    RemoveConsole(console.mPort);
                    console = null;
                }
            }

            if (console == null) {
                // no console object exists for this port so we create one, and start
                // the connection.
                console = new EmulatorConsole(port);
                if (console.start()) {
                    sEmulators.put(port, console);
                } else {
                    console = null;
                }
            }

            return console;
}

return null;
}









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/EmulatorConsoleTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/EmulatorConsoleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..1697acd

//Synthetic comment -- @@ -0,0 +1,45 @@







