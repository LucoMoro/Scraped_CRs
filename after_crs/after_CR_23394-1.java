/*Refactor a method to extract an emulator port.

Change-Id:I0dbab2c8f44a2114364075a91f31aa0d8d18d3ba*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index 43d0c0c..d34f203 100644

//Synthetic comment -- @@ -202,46 +202,58 @@
*/
public static synchronized EmulatorConsole getConsole(IDevice d) {
// we need to make sure that the device is an emulator
        // get the port number. This is the console port.
        Integer port = getEmulatorPort(d.getSerialNumber());
        if (port == null) {
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

    /**
     * Return port of emulator given its serial number.
     *
     * @param serialNumber the emulator's serial number
     * @return the integer port or <code>null</code> if it could not be determined
     */
    public static Integer getEmulatorPort(String serialNumber) {
        Matcher m = sEmulatorRegexp.matcher(serialNumber);
if (m.matches()) {
// get the port number. This is the console port.
int port;
try {
port = Integer.parseInt(m.group(1));
                if (port > 0) {
                    return port;
}
} catch (NumberFormatException e) {
// looks like we failed to get the port number. This is a bit strange since
// it's coming from a regexp that only accept digit, but we handle the case
// and return null.
}
}
return null;
}









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/EmulatorConsoleTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/EmulatorConsoleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..1697acd

//Synthetic comment -- @@ -0,0 +1,45 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ddmlib;

import junit.framework.TestCase;

/**
 * Unit tests for {@link EmulatorConsole}.
 */
public class EmulatorConsoleTest extends TestCase {

    /**
     * Test success case for {@link EmulatorConsole#getEmulatorPort(String)}.
     */
    public void testGetEmulatorPort() {
        assertEquals(Integer.valueOf(5554), EmulatorConsole.getEmulatorPort("emulator-5554"));
    }

    /**
     * Test {@link EmulatorConsole#getEmulatorPort(String)} when input serial has invalid format.
     */
    public void testGetEmulatorPort_invalid() {
        assertNull(EmulatorConsole.getEmulatorPort("invalidserial"));
    }

    /**
     * Test {@link EmulatorConsole#getEmulatorPort(String)} when port is not a number.
     */
    public void testGetEmulatorPort_nan() {
        assertNull(EmulatorConsole.getEmulatorPort("emulator-NaN"));
    }
}







