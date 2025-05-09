/*Ban setuid or setgid tcpdump on devices.

Disallow a setuid or setgid tcpdump from appearing on devices.
tcpdump can be used by programs to monitor all traffic into and out
of the phone, potentially compromising private data.

Change-Id:Icb59d1382984138929d66e19f84659e13ee6093c*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/BannedFilesTest.java b/tests/tests/security/src/android/security/cts/BannedFilesTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7a9c761

//Synthetic comment -- @@ -0,0 +1,44 @@
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

package android.security.cts;

import android.os.cts.FileUtils;

import junit.framework.TestCase;

public class BannedFilesTest extends TestCase {

    /**
     * setuid or setgid tcpdump can be used maliciously to monitor
     * all traffic in and out of the device.
     */
    public void testNoSetuidTcpdump() {
        assertNotSetugid("/system/bin/tcpdump");
        assertNotSetugid("/system/bin/tcpdump-arm");
        assertNotSetugid("/system/xbin/tcpdump");
        assertNotSetugid("/system/xbin/tcpdump-arm");
    }

    private static void assertNotSetugid(String file) {
        FileUtils.FileStatus fs = new FileUtils.FileStatus();
        if (!FileUtils.getFileStatus(file, fs, false)) {
            return;
        }
        assertTrue((fs.mode & FileUtils.S_ISUID) == 0);
        assertTrue((fs.mode & FileUtils.S_ISGID) == 0);
    }
}







