/*Fix /proc/version parsing in Settings.

Change-Id:I4bde0e4935eaa4c27067062443a051976daa115d*/




//Synthetic comment -- diff --git a/src/com/android/settings/DeviceInfoSettings.java b/src/com/android/settings/DeviceInfoSettings.java
//Synthetic comment -- index 152b8e2..748865a 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
* @return the first line, if any.
* @throws IOException if the file couldn't be read
*/
    private static String readLine(String filename) throws IOException {
BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
try {
return reader.readLine();
//Synthetic comment -- @@ -198,37 +198,10 @@
}
}

    public static String getFormattedKernelVersion() {
try {
            return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

} catch (IOException e) {
Log.e(LOG_TAG,
"IO Exception when getting kernel version for Device Info screen",
//Synthetic comment -- @@ -238,6 +211,34 @@
}
}

    public static String formatKernelVersion(String rawKernelVersion) {
        // Example (see tests for more):
        // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
        //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
        //     Thu Jun 28 11:02:39 PDT 2012

        final String PROC_VERSION_REGEX =
            "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
            "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
            "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
            "(#\\d+) " +              /* group 3: "#1" */
            "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
            "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.e(LOG_TAG, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.e(LOG_TAG, "Regex match on /proc/version only returned " + m.groupCount()
                    + " groups");
            return "Unavailable";
        }
        return m.group(1) + "\n" +                 // 3.0.31-g6fb96c9
            m.group(2) + " " + m.group(3) + "\n" + // x@y.com #1
            m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
    }

/**
* Returns " (ENGINEERING)" if the msv file has a zero value, else returns "".
* @return a string to append to the model number description.








//Synthetic comment -- diff --git a/tests/src/com/android/settings/DeviceInfoSettingsTest.java b/tests/src/com/android/settings/DeviceInfoSettingsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d2267c0

//Synthetic comment -- @@ -0,0 +1,54 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.settings;

import android.test.AndroidTestCase;

import com.android.settings.DeviceInfoSettings;

public class DeviceInfoSettingsTest extends AndroidTestCase {

    public void testGetFormattedKernelVersion() throws Exception {
        if ("Unavailable".equals(DeviceInfoSettings.getFormattedKernelVersion())) {
            fail("formatKernelVersion can't cope with this device's /proc/version");
        }
    }

    public void testFormatKernelVersion() throws Exception {
        assertEquals("Unavailable", DeviceInfoSettings.formatKernelVersion(""));
        assertEquals("2.6.38.8-gg784\n" +
                     "root@hpao4.eem.corp.google.com #2\n" +
                     "Fri Feb 24 03:31:23 PST 2012",
                     DeviceInfoSettings.formatKernelVersion("Linux version 2.6.38.8-gg784 " +
                         "(root@hpao4.eem.corp.google.com) " +
                         "(gcc version 4.4.3 (Ubuntu 4.4.3-4ubuntu5) ) #2 SMP " +
                         "Fri Feb 24 03:31:23 PST 2012"));
        assertEquals("3.0.31-g6fb96c9\n" +
                     "android-build@vpbs1.mtv.corp.google.com #1\n" +
                     "Thu Jun 28 11:02:39 PDT 2012",
                     DeviceInfoSettings.formatKernelVersion("Linux version 3.0.31-g6fb96c9 " +
                         "(android-build@vpbs1.mtv.corp.google.com) " +
                         "(gcc version 4.6.x-google 20120106 (prerelease) (GCC) ) #1 " +
                         "SMP PREEMPT Thu Jun 28 11:02:39 PDT 2012"));
        assertEquals("2.6.38.8-a-b-jellybean+\n" +
                     "x@y #1\n" +
                     "Tue Aug 28 22:10:46 CDT 2012",
                     DeviceInfoSettings.formatKernelVersion("Linux version " +
                         "2.6.38.8-a-b-jellybean+ (x@y) " +
                         "(gcc version 4.4.3 (GCC) ) #1 PREEMPT Tue Aug 28 22:10:46 CDT 2012"));
    }
}







