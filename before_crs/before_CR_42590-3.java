/*Fix /proc/version parsing in Settings.

Change-Id:I4bde0e4935eaa4c27067062443a051976daa115d*/
//Synthetic comment -- diff --git a/src/com/android/settings/DeviceInfoSettings.java b/src/com/android/settings/DeviceInfoSettings.java
//Synthetic comment -- index 152b8e2..748865a 100644

//Synthetic comment -- @@ -189,7 +189,7 @@
* @return the first line, if any.
* @throws IOException if the file couldn't be read
*/
    private String readLine(String filename) throws IOException {
BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
try {
return reader.readLine();
//Synthetic comment -- @@ -198,37 +198,10 @@
}
}

    private String getFormattedKernelVersion() {
        String procVersionStr;

try {
            procVersionStr = readLine(FILENAME_PROC_VERSION);

            final String PROC_VERSION_REGEX =
                "\\w+\\s+" + /* ignore: Linux */
                "\\w+\\s+" + /* ignore: version */
                "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
                "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
                "([^\\s]+)\\s+" + /* group 3: #26 */
                "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
                "(.+)"; /* group 4: date */

            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
            Matcher m = p.matcher(procVersionStr);

            if (!m.matches()) {
                Log.e(LOG_TAG, "Regex did not match on /proc/version: " + procVersionStr);
                return "Unavailable";
            } else if (m.groupCount() < 4) {
                Log.e(LOG_TAG, "Regex match on /proc/version only returned " + m.groupCount()
                        + " groups");
                return "Unavailable";
            } else {
                return (new StringBuilder(m.group(1)).append("\n").append(
                        m.group(2)).append(" ").append(m.group(3)).append("\n")
                        .append(m.group(4))).toString();
            }
} catch (IOException e) {
Log.e(LOG_TAG,
"IO Exception when getting kernel version for Device Info screen",
//Synthetic comment -- @@ -238,6 +211,34 @@
}
}

/**
* Returns " (ENGINEERING)" if the msv file has a zero value, else returns "".
* @return a string to append to the model number description.








//Synthetic comment -- diff --git a/tests/src/com/android/settings/DeviceInfoSettingsTest.java b/tests/src/com/android/settings/DeviceInfoSettingsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..d2267c0

//Synthetic comment -- @@ -0,0 +1,54 @@







