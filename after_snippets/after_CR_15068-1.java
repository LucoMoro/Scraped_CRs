
//<Beginning of snippet n. 0>

new file mode 100644

package android.net.cts;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import junit.framework.TestCase;

public class ListeningPortsTest extends TestCase {

    private static final String TAG = ListeningPortsTest.class.getSimpleName();

    private static final String[] IPV4_PROC_FILES = {
        "/proc/net/tcp",
        "/proc/net/udp",
    };

    private static final String[] IPV6_PROC_FILES = {
        "/proc/net/tcp6",
        "/proc/net/udp6",
    };

    /** Ports that are allowed to be listening on the emulator. */
    private static final List<String> EXCEPTIONS = new ArrayList<String>(3);

    static {
        EXCEPTIONS.add("0100007F:13AD"); // 127.0.0.1:5037 - emulator's adb daemon
        EXCEPTIONS.add("00000000:15B3"); // 0.0.0.0:5555   - emulator port
        EXCEPTIONS.add("0F02000A:15B3"); // 10.0.2.15:5555 - net forwarding for emulator to host
    }


    public static void testNoListeningPorts() throws FileNotFoundException {
        for (String procFilePath : IPV4_PROC_FILES) {
            assertNoListeningPorts(procFilePath);
        }

        // IPv6 not present on emulator...
        for (String procFilePath : IPV6_PROC_FILES) {
            try {
                assertNoListeningPorts(procFilePath);
            } catch (FileNotFoundException notFound) {
                Log.w(TAG, notFound);
            }
        }
    }

    private static void assertNoListeningPorts(String procFilePath) throws FileNotFoundException {
        /*
         * Sample output of "cat /proc/net/tcp" on emulator:
         *
         * sl  local_address rem_address   st tx_queue rx_queue tr tm->when retrnsmt   uid  timeout inode
         * 0: 0100007F:13AD 00000000:0000 0A 00000000:00000000 00:00000000 00000000     0        0 260 1 c5b83900 300 0 0 2 -1
         * 1: 00000000:15B3 00000000:0000 0A 00000000:00000000 00:00000000 00000000     0        0 269 1 c5b834e0 300 0 0 2 -1
         * 2: 0F02000A:15B3 0202000A:CE8A 01 00000000:00000000 00:00000000 00000000     0        0 270 3 c5b82040 21 4 29 4 -1
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

                String[] fields = line.split(" ");
                String localAddress = fields[1];
                String state = fields[3];

                if (!EXCEPTIONS.contains(localAddress) && isPortListening(state)) {
                    fail(String.format("Found port listening on %s in %s", localAddress, procFilePath));
                }
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static boolean isPortListening(String state) {
        // 0A = TCP_LISTEN from include/net/tcp_states.h
        return "0A".equals(state);
    }
}

//<End of snippet n. 0>








