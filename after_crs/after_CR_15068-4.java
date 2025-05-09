/*Prohibit Listening Ports on Devices

Bug 2732034

Check that devices do not have any listening ports open by
scanning files in the /proc/net directory.

Change-Id:Ic6204667809b3a0c136e38f35fe536bc6d79dcad*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ListeningPortsTest.java b/tests/tests/net/src/android/net/cts/ListeningPortsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..ff6b4e9

//Synthetic comment -- @@ -0,0 +1,131 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package android.net.cts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ListeningPortsTest extends TestCase {

    /** Address patterns used to check whether we're checking the right column in /proc/net. */
    private static final List<String> ADDRESS_PATTERNS = new ArrayList<String>(2);

    static {
        ADDRESS_PATTERNS.add("[0-9A-F]{8}:[0-9A-F]{4}");
        ADDRESS_PATTERNS.add("[0-9A-F]{32}:[0-9A-F]{4}");
    }

    /** Ports that are allowed to be listening on the emulator. */
    private static final List<String> EXCEPTION_PATTERNS = new ArrayList<String>(6);

    static {
        // IPv4 exceptions
        EXCEPTION_PATTERNS.add("00000000:15B3"); // 0.0.0.0:5555   - emulator port
        EXCEPTION_PATTERNS.add("0F02000A:15B3"); // 10.0.2.15:5555 - net forwarding for emulator
        EXCEPTION_PATTERNS.add("[0-9A-F]{6}7F:[0-9A-F]{4}"); // IPv4 Loopback

        // IPv6 exceptions
        EXCEPTION_PATTERNS.add("[0]{31}1:[0-9A-F]{4}"); // IPv6 Loopback
        EXCEPTION_PATTERNS.add("[0]{16}[0]{4}[0]{4}[0-9A-F]{6}7F:[0-9A-F]{4}"); // IPv4-6 Conversion
        EXCEPTION_PATTERNS.add("[0]{16}[F]{4}[0]{4}[0-9A-F]{6}7F:[0-9A-F]{4}"); // IPv4-6 Conversion
    }

    public static void testNoListeningPorts() {
        final boolean isTcp = true;
        assertNoListeningPorts("/proc/net/tcp", isTcp);
        assertNoListeningPorts("/proc/net/tcp6", isTcp);
        assertNoListeningPorts("/proc/net/udp", !isTcp);
        assertNoListeningPorts("/proc/net/udp6", !isTcp);
    }

    private static void assertNoListeningPorts(String procFilePath, boolean isTcp) {

        /*
         * Sample output of "cat /proc/net/tcp" on emulator:
         *
         * sl  local_address rem_address   st tx_queue rx_queue tr tm->when retrnsmt   uid  ...
         * 0: 0100007F:13AD 00000000:0000 0A 00000000:00000000 00:00000000 00000000     0   ...
         * 1: 00000000:15B3 00000000:0000 0A 00000000:00000000 00:00000000 00000000     0   ...
         * 2: 0F02000A:15B3 0202000A:CE8A 01 00000000:00000000 00:00000000 00000000     0   ...
         *
         */

        File procFile = new File(procFilePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(procFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip column headers
                if (line.startsWith("sl")) {
                    continue;
                }

                String[] fields = line.split("\\s+");
                final int expectedNumColumns = 12;
                assertTrue(procFilePath + " should have at least " + expectedNumColumns
                        + " columns of output " + fields, fields.length >= expectedNumColumns);

                String localAddress = fields[1];
                String state = fields[3];

                assertTrue(procFilePath + " should have an IP address in the second column",
                        isAddress(localAddress));

                if (!isException(localAddress) && isPortListening(state, isTcp)) {
                    fail("Found port listening on " + localAddress + " in " + procFilePath);
                }
            }
        } catch (FileNotFoundException notFound) {
            fail("Could not open file " + procFilePath + " to check for listening ports.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static boolean isAddress(String localAddress) {
        return isPatternMatch(ADDRESS_PATTERNS, localAddress);
    }

    private static boolean isException(String localAddress) {
        return isPatternMatch(EXCEPTION_PATTERNS, localAddress);
    }

    private static boolean isPatternMatch(List<String> patterns, String input) {
        for (String pattern : patterns) {
            if (Pattern.matches(pattern, input)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPortListening(String state, boolean isTcp) {
        // 0A = TCP_LISTEN from include/net/tcp_states.h
        String listeningState = isTcp ? "0A" : "07";
        return listeningState.equals(state);
    }
}







