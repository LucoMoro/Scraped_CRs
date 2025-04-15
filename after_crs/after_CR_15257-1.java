/*Detect Unwhitelisted Root Processes

Bug 2738144

Add a test that checks for root processes currently running on the
phone. It uses a large whitelist since a lot of system processes
run as root...this test will probably raise a lof of waiver
questions since these processes often correspond to hardware
drivers.

Change-Id:I28ee6cf563c9e7073836e3534703c33c8925458f*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/RootProcessTest.java b/tests/tests/os/src/android/os/cts/RootProcessTest.java
new file mode 100644
//Synthetic comment -- index 0000000..beaf21c

//Synthetic comment -- @@ -0,0 +1,242 @@
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

package android.os.cts;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RootProcessTest extends TestCase {

    /** Processes that are allowed to run as root. */
    private static final Pattern ROOT_PROCESS_WHITELIST_PATTERN = getRootProcessWhitelistPattern(
            "adbd",
            "aio/\\d+",
            "als_wq",
            "app_process",
            "async/mgr",
            "battd",
            "bdi-default",
            "binder",
            "bluetooth",
            "bridge_recovery",
            "bridge_work-que",
            "bu52014hfv_wq",
            "button/\\d+",
            "cpcap_irq/\\d+",
            "cqueue",
            "crypto/\\d+",
            "debuggerd",
            "detection/\\d+",
            "dhcp_sysioc",
            "dhd_dpc",
            "dhd_sysioc",
            "dhd_watchdog",
            "dock",
            "ds2784-battery",
            "dsi",
            "events/0",
            "flush-\\d+:0",
            "hid_compat",
            "init",
            "init.goldfish.s",
            "installd",
            "iscan_sysioc",
            "kadspd",
            "kblockd/\\d+",
            "khelper",
            "khubd",
            "khungtaskd",
            "kmmcd",
            "kondemand/\\d+",
            "krfcommd",
            "krpcserversd",
            "kseriod",
            "ksoftirqd/\\d+",
            "kstriped",
            "ksuspend_usbd",
            "kswapd0",
            "kthreadd",
            "mdm_panicd",
            "mmcqd",
            "msm_serial_hs",
            "mtdblockd",
            "netd",
            "omap2_mcspi",
            "omap_mdm_ctrl_w",
            "omap_serial",
            "panel_on/\\d+",
            "pdflush",
            "pvrflip/\\d+",
            "qemu-props",
            "qemud",
            "qmi",
            "qtouch_obp_ts_w",
            "rmnet/\\d+",
            "rpciod/\\d+",
            "rpcrouter",
            "servicemanager",
            "sfh7743_wq",
            "sh", // Why is sh running as root?
            "suspend",
            "synaptics_wq",
            "sync_supers",
            "usb_mass_storag",
            "usbhid_resumer",
            "vold",
            "w1_bus_master1",
            "watchdog/\\d+",
            "zygote"
    );

    /** Combine the individual patterns into one super pattern. */
    private static Pattern getRootProcessWhitelistPattern(String... patterns) {
        StringBuilder rootProcessPattern = new StringBuilder();
        for (int i = 0; i < patterns.length; i++) {
            rootProcessPattern.append(patterns[i]);
            if (i + 1 < patterns.length) {
                rootProcessPattern.append('|');
            }
        }
        return Pattern.compile(rootProcessPattern.toString());
    }

    /** Test that there are no unapproved root processes running on the system. */
    public void testNoRootProcesses() throws FileNotFoundException {
        File proc = new File("/proc");
        assertTrue(proc + " is missing", proc.exists());

        File[] rootProcessDirs = proc.listFiles(new RootProcessFilter());
        assertEquals(getFailMessage(rootProcessDirs), 0, rootProcessDirs.length);
    }

    /** Filters out processes that are not running as root or are whitelisted. */
    private static class RootProcessFilter implements FileFilter {

        public boolean accept(File pathname) {
            return isPidDirectory(pathname) && isRootProcess(pathname);
        }

        private static boolean isPidDirectory(File pathname) {
            return pathname.isDirectory() && Pattern.matches("\\d+", pathname.getName());
        }

        /**
         * Check to see if the process is a whitelisted root process or runs as root user or group.
         *
         * @param processDir with the status file
         * @return whether or not it is a unwhitelisted root process
         */
        private static boolean isRootProcess(File processDir) {
            File status = getProcessStatus(processDir);
            Scanner scanner = null;
            try {
                scanner = new Scanner(status);

                scanner = findToken(scanner, "Name:");
                String name = scanner.next();

                scanner = findToken(scanner, "Uid:");
                boolean rootUid = hasRootId(scanner);

                scanner = findToken(scanner, "Gid:");
                boolean rootGid = hasRootId(scanner);

                return !ROOT_PROCESS_WHITELIST_PATTERN.matcher(name).matches()
                        && (rootUid || rootGid);
            } catch (FileNotFoundException notFound) {
                fail(processDir + " does not have a status file");
                return false;
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }

        /**
         * Uid and Gid lines have four values: "Uid:    0       0       0       0"
         *
         * @param scanner that has just processed the "Uid:" or "Gid:" token
         * @return whether or not any of the ids are root
         */
        private static boolean hasRootId(Scanner scanner) {
            int realUid = scanner.nextInt();
            int effectiveUid = scanner.nextInt();
            int savedSetUid = scanner.nextInt();
            int fileSystemUid = scanner.nextInt();
            return realUid == 0 || effectiveUid == 0 || savedSetUid == 0 || fileSystemUid == 0;
        }
    }

    /**
     * Get the status {@link File} that has name:value pairs.
     * <pre>
     * Name:   init
     * ...
     * Uid:    0       0       0       0
     * Gid:    0       0       0       0
     * </pre>
     */
    private static File getProcessStatus(File processDir) {
        return new File(processDir, "status");
    }

    /**
     * Convenience method to move the scanner's position to the point after the given token.
     *
     * @param scanner to call next() until the token is found
     * @param token to find like "Name:"
     * @return scanner after finding token
     */
    private static Scanner findToken(Scanner scanner, String token) {
        while (true) {
            String next = scanner.next();
            if (next.equals(token)) {
                return scanner;
            }
        }

        // Scanner will exhaust input and throw an exception before getting here.
    }

    /** Generates message like "Root process found: 1337 (evil_program) 3007 (dangerous_virus)" */
    private static String getFailMessage(File[] rootProcesses) throws FileNotFoundException {
        StringBuilder fail = new StringBuilder("Root processes found: ");
        for (File rootProcess : rootProcesses) {
            String processName = getProcessName(rootProcess);
            fail.append(rootProcess.getName()).append(" (").append(processName).append(") ");
        }
        return fail.toString();
    }

    /** Returns name of the process corresponding to the process's directory in /proc. */
    private static String getProcessName(File processDir) throws FileNotFoundException {
        File status = getProcessStatus(processDir);
        Scanner scanner = new Scanner(status);
        try {
            scanner = findToken(scanner, "Name:");
            return scanner.next();
        } finally {
            scanner.close();
        }
    }
}







