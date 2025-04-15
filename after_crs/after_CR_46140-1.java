/*ddms: Fix CPU load reader in SysinfoPanel

The sysinfo panel provides a pie chart visualizer for a few
"adb shell dumpsys" options. Unfortunately, most of them don't
work with newer devices since the output format of the dumpsys
command has changed. This CL updates the CPU load parser to work
for both old and new output formats.http://code.google.com/p/android/issues/detail?id=39243Change-Id:Ie76eedcaa7cfe13edf930104308ac824319dafa8*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java
//Synthetic comment -- index 3ca5ff3..cc20ab7 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -76,15 +78,29 @@
private static final int MODE_SYNC = 4;

// argument to dumpsys; section in the bugreport holding the data
    private static final String BUGREPORT_SECTION[] = {
        "cpuinfo",
        "alarm",
        "batteryinfo",
        "MEMORY INFO",
        "content"
    };

    private static final String DUMP_COMMAND[] = {
        "dumpsys cpuinfo",
        "dumpsys alarm",
        "dumpsys batteryinfo",
        "cat /proc/meminfo ; procrank",
        "dumpsys content"
    };

    private static final String CAPTIONS[] = {
        "CPU load",
        "Alarms",
        "Wakelocks",
        "Memory usage",
        "Sync"
    };

/**
* Generates the dataset to display.
//Synthetic comment -- @@ -188,8 +204,7 @@
public void addOutput(byte[] data, int offset, int length) {
try {
mTempStream.write(data, offset, length);
        } catch (IOException e) {
Log.e("DDMS", e);
}
}
//Synthetic comment -- @@ -204,6 +219,7 @@
if (mTempStream != null) {
try {
mTempStream.close();
                System.out.println("opening data source: " + mDataFile.getAbsolutePath());
generateDataset(mDataFile);
mTempStream = null;
mDataFile = null;
//Synthetic comment -- @@ -319,20 +335,7 @@
*/
private BufferedReader getBugreportReader(File file) throws
IOException {
        return new BufferedReader(new FileReader(file));
}

/**
//Synthetic comment -- @@ -368,228 +371,322 @@
}
return total;
}

    public static final class BugReportParser {
        public static final class DataValue {
            final String name;
            final double value;

            public DataValue(String n, double v) {
                name = n;
                value = v;
}
        };

        /**
         * Processes wakelock information from bugreport. Updates mDataset with the
         * new data.
         *
         * @param br Reader providing the content
         * @throws IOException if error reading file
         */
        public static List<DataValue> readWakelockDataset(BufferedReader br) throws IOException {
            List<DataValue> results = new ArrayList<DataValue>();

            Pattern lockPattern = Pattern.compile("Wake lock (\\S+): (.+) partial");
            Pattern totalPattern = Pattern.compile("Total: (.+) uptime");
            double total = 0;
            boolean inCurrent = false;

            while (true) {
                String line = br.readLine();
                if (line == null || line.startsWith("DUMP OF SERVICE")) {
                    // Done, or moved on to the next service
                    break;
                }
                if (line.startsWith("Current Battery Usage Statistics")) {
                    inCurrent = true;
                } else if (inCurrent) {
                    Matcher m = lockPattern.matcher(line);
if (m.find()) {
                        double value = parseTimeMs(m.group(2)) / 1000.;
                        results.add(new DataValue(m.group(1), value));
                        total -= value;
                    } else {
                        m = totalPattern.matcher(line);
                        if (m.find()) {
                            total += parseTimeMs(m.group(1)) / 1000.;
                        }
}
}
}
            if (total > 0) {
                results.add(new DataValue("Unlocked", total));
            }

            return results;
}

        /**
         * Processes alarm information from bugreport. Updates mDataset with the new
         * data.
         *
         * @param br Reader providing the content
         * @throws IOException if error reading file
         */
        public static List<DataValue> readAlarmDataset(BufferedReader br) throws IOException {
            List<DataValue> results = new ArrayList<DataValue>();
            Pattern pattern = Pattern.compile("(\\d+) alarms: Intent .*\\.([^. ]+) flags");

            while (true) {
                String line = br.readLine();
                if (line == null || line.startsWith("DUMP OF SERVICE")) {
                    // Done, or moved on to the next service
                    break;
                }
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    long count = Long.parseLong(m.group(1));
                    String name = m.group(2);
                    results.add(new DataValue(name, count));
                }
            }

            return results;
}

        /**
         * Processes cpu load information from bugreport. Updates mDataset with the
         * new data.
         *
         * @param br Reader providing the content
         * @throws IOException if error reading file
         */
        public static List<DataValue> readCpuDataset(BufferedReader br) throws IOException {
            List<DataValue> results = new ArrayList<DataValue>();
            Pattern pattern1 = Pattern.compile("(\\S+): (\\S+)% = (.+)% user . (.+)% kernel");
            Pattern pattern2 = Pattern.compile("(\\S+)% (\\S+): (.+)% user . (.+)% kernel");

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();

                if (line.startsWith("Load:")) {
                    continue;
                }

                String name = "";
                double user = 0, kernel = 0, both = 0;
                boolean found = false;

                // try pattern1
                Matcher m = pattern1.matcher(line);
                if (m.find()) {
                    found = true;
                    name = m.group(1);
                    both = safeParseLong(m.group(2));
                    user = safeParseLong(m.group(3));
                    kernel = safeParseLong(m.group(4));
                }

                // try pattern2
                m = pattern2.matcher(line);
                if (m.find()) {
                    found = true;
                    name = m.group(2);
                    both = safeParseDouble(m.group(1));
                    user = safeParseDouble(m.group(3));
                    kernel = safeParseDouble(m.group(4));
                }

                if (!found) {
                    System.out.println("no match: " + line);
                    continue;
                }

if ("TOTAL".equals(name)) {
if (both < 100) {
                        results.add(new DataValue("Idle", (100 - both)));
}
} else {
// Try to make graphs more useful even with rounding;
// log often has 0% user + 0% kernel = 1% total
// We arbitrarily give extra to kernel
if (user > 0) {
                        results.add(new DataValue(name + " (user)", user));
}
if (kernel > 0) {
                        results.add(new DataValue(name + " (kernel)" , both - user));
}
if (user == 0 && kernel == 0 && both > 0) {
                        results.add(new DataValue(name, both));
                    }
                }

            }

            return results;
        }

        private static long safeParseLong(String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        private static double safeParseDouble(String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        /**
         * Processes meminfo information from bugreport. Updates mDataset with the
         * new data.
         *
         * @param br Reader providing the content
         * @throws IOException if error reading file
         */
        public static List<DataValue> readMeminfoDataset(BufferedReader br) throws IOException {
            List<DataValue> results = new ArrayList<DataValue>();
            Pattern valuePattern = Pattern.compile("(\\d+) kB");
            long total = 0;
            long other = 0;

            // Scan meminfo
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    // End of file
                    break;
                }
                Matcher m = valuePattern.matcher(line);
                if (m.find()) {
                    long kb = Long.parseLong(m.group(1));
                    if (line.startsWith("MemTotal")) {
                        total = kb;
                    } else if (line.startsWith("MemFree")) {
                        results.add(new DataValue("Free", kb));
                        total -= kb;
                    } else if (line.startsWith("Slab")) {
                        results.add(new DataValue("Slab", kb));
                        total -= kb;
                    } else if (line.startsWith("PageTables")) {
                        results.add(new DataValue("PageTables", kb));
                        total -= kb;
                    } else if (line.startsWith("Buffers") && kb > 0) {
                        results.add(new DataValue("Buffers", kb));
                        total -= kb;
                    } else if (line.startsWith("Inactive")) {
                        results.add(new DataValue("Inactive", kb));
                        total -= kb;
                    } else if (line.startsWith("MemFree")) {
                        results.add(new DataValue("Free", kb));
                        total -= kb;
                    }
                } else {
                    break;
                }
            }
            // Scan procrank
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (line.indexOf("PROCRANK") >= 0 || line.indexOf("PID") >= 0) {
                    // procrank header
                    continue;
                }
                if  (line.indexOf("----") >= 0) {
                    //end of procrank section
                    break;
                }
                // Extract pss field from procrank output
                long pss = Long.parseLong(line.substring(23, 31).trim());
                String cmdline = line.substring(43).trim().replace("/system/bin/", "");
                // Arbitrary minimum size to display
                if (pss > 2000) {
                    results.add(new DataValue(cmdline, pss));
                } else {
                    other += pss;
                }
                total -= pss;
            }
            results.add(new DataValue("Other", other));
            results.add(new DataValue("Unknown", total));

            return results;
        }

        /**
         * Processes sync information from bugreport. Updates mDataset with the new
         * data.
         *
         * @param br Reader providing the content
         * @throws IOException if error reading file
         */
        public static List<DataValue> readSyncDataset(BufferedReader br) throws IOException {
            List<DataValue> results = new ArrayList<DataValue>();

            while (true) {
                String line = br.readLine();
                if (line == null || line.startsWith("DUMP OF SERVICE")) {
                    // Done, or moved on to the next service
                    break;
                }
                if (line.startsWith(" |") && line.length() > 70) {
                    String authority = line.substring(3, 18).trim();
                    String duration = line.substring(61, 70).trim();
                    // Duration is MM:SS or HH:MM:SS (DateUtils.formatElapsedTime)
                    String durParts[] = duration.split(":");
                    if (durParts.length == 2) {
                        long dur = Long.parseLong(durParts[0]) * 60 + Long
                                .parseLong(durParts[1]);
                        results.add(new DataValue(authority, dur));
                    } else if (duration.length() == 3) {
                        long dur = Long.parseLong(durParts[0]) * 3600
                                + Long.parseLong(durParts[1]) * 60 + Long
                                .parseLong(durParts[2]);
                        results.add(new DataValue(authority, dur));
}
}
}

            return results;
}
}

    private void readWakelockDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readWakelockDataset(br));    }

    private void readAlarmDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readAlarmDataset(br));
    }

    private void readCpuDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readCpuDataset(br));
    }

    private void readMeminfoDataset(BufferedReader br) throws IOException {
mLabel.setText("PSS in kB");
        updateDataSet(BugReportParser.readMeminfoDataset(br));
}

void readSyncDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readSyncDataset(br));
    }

    private void updateDataSet(List<BugReportParser.DataValue> data) {
        for (BugReportParser.DataValue d : data) {
            mDataset.setValue(d.name, d.value);
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/BugReportParserTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/BugReportParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..73a2fce

//Synthetic comment -- @@ -0,0 +1,66 @@
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

package com.android.ddmuilib;

import com.android.ddmuilib.SysinfoPanel.BugReportParser;
import com.android.ddmuilib.SysinfoPanel.BugReportParser.DataValue;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class BugReportParserTest extends TestCase {
    public void testParseEclairCpuDataSet() throws IOException {
        String cpuInfo =
                "Currently running services:\n" +
                "   cpuinfo\n" +
                " ----------------------------------------------------------------------------\n" +
                " DUMP OF SERVICE cpuinfo:\n" +
                " Load: 0.53 / 0.11 / 0.04\n" +
                " CPU usage from 33406ms to 28224ms ago:\n" +
                "   system_server: 56% = 42% user + 13% kernel / faults: 6724 minor 9 major\n" +
                "   bootanimation: 1% = 0% user + 0% kernel\n" +
                "   zygote: 0% = 0% user + 0% kernel / faults: 146 minor\n" +
                " TOTAL: 98% = 67% user + 30% kernel;\n";
        BufferedReader br = new BufferedReader(new StringReader(cpuInfo));
        List<DataValue> data = BugReportParser.readCpuDataset(br);

        assertEquals(4, data.size());
        assertEquals("system_server (user)", data.get(0).name);
        assertEquals("Idle", data.get(3).name);
    }

    public void testParseJbCpuDataSet() throws IOException {
        String cpuInfo =
                "Load: 1.0 / 1.02 / 0.97\n" +
                "CPU usage from 96307ms to 36303ms ago:\n" +
                "  0.4% 675/system_server: 0.3% user + 0.1% kernel / faults: 198 minor\n" +
                "  0.1% 173/mpdecision: 0% user + 0.1% kernel\n" +
                "  0% 2856/kworker/0:2: 0% user + 0% kernel\n" +
                "  0% 3128/kworker/0:0: 0% user + 0% kernel\n" +
                "0.3% TOTAL: 0.1% user + 0% kernel + 0% iowait\n";
        BufferedReader br = new BufferedReader(new StringReader(cpuInfo));
        List<DataValue> data = BugReportParser.readCpuDataset(br);

        assertEquals(4, data.size());
        assertEquals("675/system_server (user)", data.get(0).name);
        assertEquals("Idle", data.get(3).name);
    }
}







