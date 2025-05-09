/*Add Test to Verify NX is Enabled

Bug 4083207

Change-Id:I246fca2b86c6ec6c8348c6590f0188c9eefe7a7a*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoExecutePermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoExecutePermissionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..5c0a4c3

//Synthetic comment -- @@ -0,0 +1,65 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.permission.cts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import junit.framework.TestCase;

/**
 * {@link TestCase} that checks that the NX (No Execute) feature is enabled. This feature makes it
 * harder to perform attacks against Android by marking certain data blocks as non-executable.
 */
public class NoExecutePermissionTest extends TestCase {

    public void testNoExecutePermission() throws FileNotFoundException {
        String heapPermissions = null;
        String stackPermissions = null;

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("/proc/self/maps"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] fields = line.split("\\s+");

                // Sample line:
                // 0001d000-00024000 rw-p 00000000 00:00 0          [heap]
                if (fields != null && fields.length >= 1) {
                    String permissions = fields[1];
                    if (fields.length >= 6) {
                        String tag = fields[5];
                        if ("[heap]".equals(tag)) {
                            heapPermissions = permissions;
                        } else if ("[stack]".equals(tag)) {
                            stackPermissions = permissions;
                        }
                    }
                }
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        assertEquals("NX (No Execute) not enabled for heap", "rw-p", heapPermissions);
        assertEquals("NX (No Execute) not enabled for stack", "rw-p", stackPermissions);
    }
}







