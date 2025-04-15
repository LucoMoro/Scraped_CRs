/*ddms: Fix CPU load reader in SysinfoPanel

The sysinfo panel provides a pie chart visualizer for a few
"adb shell dumpsys" options. Unfortunately, most of them don't
work with newer devices since the output format of the dumpsys
command has changed. This CL updates the CPU load parser to work
for both old and new output formats.http://code.google.com/p/android/issues/detail?id=39243Change-Id:Ie76eedcaa7cfe13edf930104308ac824319dafa8*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java
//Synthetic comment -- index 3ca5ff3..adeb424 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -76,15 +78,29 @@
private static final int MODE_SYNC = 4;

// argument to dumpsys; section in the bugreport holding the data
    private static final String BUGREPORT_SECTION[] = {"cpuinfo", "alarm",
            "batteryinfo", "MEMORY INFO", "content"};

    private static final String DUMP_COMMAND[] = {"dumpsys cpuinfo",
            "dumpsys alarm", "dumpsys batteryinfo", "cat /proc/meminfo ; procrank",
            "dumpsys content"};

    private static final String CAPTIONS[] = {"CPU load", "Alarms",
            "Wakelocks", "Memory usage", "Sync"};

/**
* Generates the dataset to display.
//Synthetic comment -- @@ -188,8 +204,7 @@
public void addOutput(byte[] data, int offset, int length) {
try {
mTempStream.write(data, offset, length);
        }
        catch (IOException e) {
Log.e("DDMS", e);
}
}
//Synthetic comment -- @@ -319,20 +334,7 @@
*/
private BufferedReader getBugreportReader(File file) throws
IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        // Skip over the unwanted bugreport sections
        while (true) {
            String line = br.readLine();
            if (line == null) {
                Log.d("DDMS", "Service not found " + line);
                break;
            }
            if ((line.startsWith("DUMP OF SERVICE ") || line.startsWith("-----")) &&
                    line.indexOf(BUGREPORT_SECTION[mMode]) > 0) {
                break;
            }
        }
        return br;
}

/**
//Synthetic comment -- @@ -368,228 +370,322 @@
}
return total;
}
    /**
     * Processes wakelock information from bugreport. Updates mDataset with the
     * new data.
     *
     * @param br Reader providing the content
     * @throws IOException if error reading file
     */
    void readWakelockDataset(BufferedReader br) throws IOException {
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
                    mDataset.setValue(m.group(1), value);
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
            mDataset.setValue("Unlocked", total);
}
    }

    /**
     * Processes alarm information from bugreport. Updates mDataset with the new
     * data.
     *
     * @param br Reader providing the content
     * @throws IOException if error reading file
     */
    void readAlarmDataset(BufferedReader br) throws IOException {
        Pattern pattern = Pattern
                .compile("(\\d+) alarms: Intent .*\\.([^. ]+) flags");

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
                mDataset.setValue(name, count);
            }
        }
    }

    /**
     * Processes cpu load information from bugreport. Updates mDataset with the
     * new data.
     *
     * @param br Reader providing the content
     * @throws IOException if error reading file
     */
    void readCpuDataset(BufferedReader br) throws IOException {
        Pattern pattern = Pattern
                .compile("(\\S+): (\\S+)% = (.+)% user . (.+)% kernel");

        while (true) {
            String line = br.readLine();
            if (line == null || line.startsWith("DUMP OF SERVICE")) {
                // Done, or moved on to the next service
                break;
            }
            if (line.startsWith("Load:")) {
                mLabel.setText(line);
                continue;
            }
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                String name = m.group(1);
                long both = Long.parseLong(m.group(2));
                long user = Long.parseLong(m.group(3));
                long kernel = Long.parseLong(m.group(4));
if ("TOTAL".equals(name)) {
if (both < 100) {
                        mDataset.setValue("Idle", (100 - both));
}
} else {
// Try to make graphs more useful even with rounding;
// log often has 0% user + 0% kernel = 1% total
// We arbitrarily give extra to kernel
if (user > 0) {
                        mDataset.setValue(name + " (user)", user);
}
if (kernel > 0) {
                        mDataset.setValue(name + " (kernel)" , both - user);
}
if (user == 0 && kernel == 0 && both > 0) {
                        mDataset.setValue(name, both);
}
}
}
}
}

    /**
     * Processes meminfo information from bugreport. Updates mDataset with the
     * new data.
     *
     * @param br Reader providing the content
     * @throws IOException if error reading file
     */
    void readMeminfoDataset(BufferedReader br) throws IOException {
        Pattern valuePattern = Pattern.compile("(\\d+) kB");
        long total = 0;
        long other = 0;
mLabel.setText("PSS in kB");

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
                    mDataset.setValue("Free", kb);
                    total -= kb;
                } else if (line.startsWith("Slab")) {
                    mDataset.setValue("Slab", kb);
                    total -= kb;
                } else if (line.startsWith("PageTables")) {
                    mDataset.setValue("PageTables", kb);
                    total -= kb;
                } else if (line.startsWith("Buffers") && kb > 0) {
                    mDataset.setValue("Buffers", kb);
                    total -= kb;
                } else if (line.startsWith("Inactive")) {
                    mDataset.setValue("Inactive", kb);
                    total -= kb;
                } else if (line.startsWith("MemFree")) {
                    mDataset.setValue("Free", kb);
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
                mDataset.setValue(cmdline, pss);
            } else {
                other += pss;
            }
            total -= pss;
        }
        mDataset.setValue("Other", other);
        mDataset.setValue("Unknown", total);
}

    /**
     * Processes sync information from bugreport. Updates mDataset with the new
     * data.
     *
     * @param br Reader providing the content
     * @throws IOException if error reading file
     */
void readSyncDataset(BufferedReader br) throws IOException {
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
                    mDataset.setValue(authority, dur);
                } else if (duration.length() == 3) {
                    long dur = Long.parseLong(durParts[0]) * 3600
                            + Long.parseLong(durParts[1]) * 60 + Long
                            .parseLong(durParts[2]);
                    mDataset.setValue(authority, dur);
                }
            }
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/BugReportParserTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/BugReportParserTest.java
new file mode 100644
//Synthetic comment -- index 0000000..73a2fce

//Synthetic comment -- @@ -0,0 +1,66 @@







