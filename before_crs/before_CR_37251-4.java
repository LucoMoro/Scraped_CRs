/*Fix "Resource leak: <Foo> is never closed"

This changeset fixes various code fragments opening
resources without closing them.

Change-Id:I6ed48a32dc5de4c11cab394dd3883ebbb54d2938*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index f3ab28c..2f4175f 100644

//Synthetic comment -- @@ -532,9 +532,13 @@

// need to make sure the string format uses dot and not comma
Formatter formatter = new Formatter(Locale.US);
        formatter.format(COMMAND_GPS, longitude, latitude, elevation);

        return processCommand(formatter.toString());
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index bf0b4e1..f207567 100644

//Synthetic comment -- @@ -523,51 +523,55 @@
FileOutputStream fos = null;
try {
fos = new FileOutputStream(f);
} catch (IOException e) {
Log.e("ddms", String.format("Failed to open local file %s for writing, Reason: %s",
f.getAbsolutePath(), e.toString()));
throw new SyncException(SyncError.FILE_WRITE_ERROR);
}

        // the buffer to read the data
        byte[] data = new byte[SYNC_DATA_MAX];

        // loop to get data until we're done.
        while (true) {
            // check if we're cancelled
            if (monitor.isCanceled() == true) {
                throw new SyncException(SyncError.CANCELED);
            }

            // if we're done, we stop the loop
            if (checkResult(pullResult, ID_DONE)) {
                break;
            }
            if (checkResult(pullResult, ID_DATA) == false) {
                // hmm there's an error
                throw new SyncException(SyncError.TRANSFER_PROTOCOL_ERROR,
                        readErrorMessage(pullResult, timeOut));
            }
            int length = ArrayHelper.swap32bitFromArray(pullResult, 4);
            if (length > SYNC_DATA_MAX) {
                // buffer overrun!
                // error and exit
                throw new SyncException(SyncError.BUFFER_OVERRUN);
            }

            // now read the length we received
            AdbHelper.read(mChannel, data, length, timeOut);

            // get the header for the next packet.
            AdbHelper.read(mChannel, pullResult, -1, timeOut);

            // write the content in the file
            fos.write(data, 0, length);

            monitor.advance(length);
        }

        fos.flush();
}


//Synthetic comment -- @@ -637,48 +641,51 @@

// create the header for the action
msg = createSendFileReq(ID_SEND, remotePathContent, 0644);
} catch (UnsupportedEncodingException e) {
throw new SyncException(SyncError.REMOTE_PATH_ENCODING, e);
        }

        // and send it. We use a custom try/catch block to make the difference between
        // file and network IO exceptions.
        AdbHelper.write(mChannel, msg, -1, timeOut);

        // create the buffer used to read.
        // we read max SYNC_DATA_MAX, but we need 2 4 bytes at the beginning.
        if (mBuffer == null) {
            mBuffer = new byte[SYNC_DATA_MAX + 8];
        }
        System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);

        // look while there is something to read
        while (true) {
            // check if we're canceled
            if (monitor.isCanceled() == true) {
                throw new SyncException(SyncError.CANCELED);
}

            // read up to SYNC_DATA_MAX
            int readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);

            if (readCount == -1) {
                // we reached the end of the file
                break;
            }

            // now send the data to the device
            // first write the amount read
            ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);

            // now write it
            AdbHelper.write(mChannel, mBuffer, readCount+8, timeOut);

            // and advance the monitor
            monitor.advance(readCount);
}
        // close the local file
        fis.close();

// create the DONE message
long time = System.currentTimeMillis() / 1000;








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/log/EventLogParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/log/EventLogParser.java
//Synthetic comment -- index 22c0703..b2d8256 100644

//Synthetic comment -- @@ -124,8 +124,9 @@
* @return <code>true</code> if success, <code>false</code> if failure.
*/
public boolean init(String filePath)  {
try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

String line = null;
do {
//Synthetic comment -- @@ -138,6 +139,14 @@
return true;
} catch (IOException e) {
return false;
}
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/EventLogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/EventLogPanel.java
//Synthetic comment -- index 4faac3a..937ee40 100644

//Synthetic comment -- @@ -914,10 +914,13 @@
byte[] buffer = new byte[256];

FileInputStream fis = new FileInputStream(fileName);

        int count;
        while ((count = fis.read(buffer)) != -1) {
            logReceiver.parseNewData(buffer, 0, count);
}
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index d60bae8..a347155 100644

//Synthetic comment -- @@ -730,8 +730,9 @@
Arrays.sort(selection);

// loop on the selection and output the file.
try {
                    FileWriter writer = new FileWriter(fileName);

for (int i : selection) {
TableItem item = currentTable.getItem(i);
//Synthetic comment -- @@ -744,6 +745,14 @@

} catch (IOException e) {
return false;
}
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java
//Synthetic comment -- index f8cb7a3..febd99c 100644

//Synthetic comment -- @@ -439,7 +439,9 @@
} else {
final int size = mTrackedItems.size();
item.color = nextSeriesColor(size);
            item.label = "0x" + new Formatter().format("%08x", tag);
}

// create color chip to display as legend in table








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java
//Synthetic comment -- index 594912b..b002ff5 100644

//Synthetic comment -- @@ -74,12 +74,11 @@
int minMajorVersion = -1;
int minMinorVersion = -1;
int minMicroVersion = -1;
        FileReader reader = null;
try {
            reader = new FileReader(osLibs + SdkConstants.FN_PLUGIN_PROP);
            BufferedReader bReader = new BufferedReader(reader);
String line;
            while ((line = bReader.readLine()) != null) {
Matcher m = sPluginVersionPattern.matcher(line);
if (m.matches()) {
minMajorVersion = Integer.parseInt(m.group(1));
//Synthetic comment -- @@ -139,10 +138,9 @@
String osTools = osSdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER;
FullRevision toolsRevision = new FullRevision(Integer.MAX_VALUE);
try {
            reader = new FileReader(osTools + SdkConstants.FN_SOURCE_PROP);
            BufferedReader bReader = new BufferedReader(reader);
String line;
            while ((line = bReader.readLine()) != null) {
Matcher m = sSourcePropPattern.matcher(line);
if (m.matches()) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java
//Synthetic comment -- index 48e01df..5fe47d9 100755

//Synthetic comment -- @@ -165,40 +165,43 @@
final Process process = Runtime.getRuntime().exec(command);

final BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));

                final String lineSep = AdtUtils.getLineSeparator();

                int err = GrabProcessOutput.grabProcessOutput(
                        process,
                        Wait.WAIT_FOR_READERS,
                        new IProcessOutput() {
                            @Override
                            public void out(@Nullable String line) {
                                if (line != null) {
                                    try {
                                        writer.write(line);
                                        writer.write(lineSep);
                                    } catch (IOException ignore) {}
}
                            }

                            @Override
                            public void err(@Nullable String line) {
                                if (line != null) {
                                    AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                            project, line);
}
                            }
                        });

                if (err == 0) {
                    // The command worked. In this case we don't remove the
                    // temp file in the finally block.
                    removeDstFile = false;
                } else {
                    AdtPlugin.printErrorToConsole(project,
                        "DexDump failed with code " + Integer.toString(err));       //$NON-NLS-1$
                    return Status.OK_STATUS;
}
} catch (InterruptedException e) {
// ?








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java
//Synthetic comment -- index 409210d..e2f8a57 100644

//Synthetic comment -- @@ -354,26 +354,29 @@
// create streams to read the intermediary archive
FileInputStream fis = new FileInputStream(mOsFrameworkLocation);
ZipInputStream zis = new ZipInputStream(fis);

        // loop on the entries of the intermediary package and put them in the final package.
        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {
            // get the name of the entry.
            String currEntryName = entry.getName();

            if (currEntryName.equals(entryName)) {
                long entrySize = entry.getSize();
                if (entrySize > Integer.MAX_VALUE) {
                    throw new InvalidAttributeValueException();
}

                data = readZipData(zis, (int)entrySize);
                return data;
}
        }

        return null;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/JarListSanitizer.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/JarListSanitizer.java
//Synthetic comment -- index fa7b00b..4d1dcdb 100644

//Synthetic comment -- @@ -435,10 +435,11 @@
*/
private static String getSha1(File f) throws Sha1Exception {
synchronized (sBuffer) {
try {
MessageDigest md = MessageDigest.getInstance("SHA-1");

                FileInputStream fis = new FileInputStream(f);
while (true) {
int length = fis.read(sBuffer);
if (length > 0) {
//Synthetic comment -- @@ -452,15 +453,27 @@

} catch (Exception e) {
throw new Sha1Exception(f, e);
}
}
}

private static String byteArray2Hex(final byte[] hash) {
Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
}
        return formatter.toString();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index c2c9bed..88495e0 100644

//Synthetic comment -- @@ -121,9 +121,10 @@
* @return the map of (key,value) pairs, or null if the parsing failed.
*/
public static Map<String, HardwareProperty> parseHardwareDefinitions(File file, ISdkLog log) {
try {
FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

Map<String, HardwareProperty> map = new TreeMap<String, HardwareProperty>();

//Synthetic comment -- @@ -173,6 +174,14 @@
} catch (IOException e) {
log.warning("Error parsing '%1$s': %2$s.", file.getAbsolutePath(),
e.getMessage());
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 721d165..f01c63a 100644

//Synthetic comment -- @@ -929,8 +929,9 @@
private Matcher checkFileContainsRegexp(File file, String regexp) {
Pattern p = Pattern.compile(regexp);

try {
            BufferedReader in = new BufferedReader(new FileReader(file));
String line;

while ((line = in.readLine()) != null) {
//Synthetic comment -- @@ -943,6 +944,14 @@
in.close();
} catch (Exception e) {
// ignore
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/mock/MockLog.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/mock/MockLog.java
//Synthetic comment -- index 24b120f..95a2693 100644

//Synthetic comment -- @@ -32,7 +32,9 @@
private ArrayList<String> mMessages = new ArrayList<String>();

private void add(String code, String format, Object... args) {
        mMessages.add(new Formatter().format(code + format, args).toString());
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 5d24a07..72488a3 100644

//Synthetic comment -- @@ -1081,6 +1081,7 @@
Formatter formatter = new Formatter(Locale.US);
formatter.format("%.2f", scale);   //$NON-NLS-1$
list.add(formatter.toString());
}

// convert the list into an array for the call to exec.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index e2ac96b..63787f9 100644

//Synthetic comment -- @@ -515,8 +515,9 @@
* @return true if both sizes where found.
*/
private boolean parseLayoutFile(File layoutFile) {
try {
            BufferedReader input = new BufferedReader(new FileReader(layoutFile));
String line;

while ((line = input.readLine()) != null) {
//Synthetic comment -- @@ -563,6 +564,14 @@
// false is returned below.
} catch (IOException e) {
// ignore.
}

return false;








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index 285897b..b49d75e 100644

//Synthetic comment -- @@ -103,12 +103,16 @@
private MappedByteBuffer mapFile(String filename, long offset) throws IOException {
MappedByteBuffer buffer = null;
FileInputStream dataFile = new FileInputStream(filename);
        File file = new File(filename);
        FileChannel fc = dataFile.getChannel();
        buffer = fc.map(FileChannel.MapMode.READ_ONLY, offset, file.length() - offset);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        return buffer;
}

private void readDataFileHeader(MappedByteBuffer buffer) {
//Synthetic comment -- @@ -402,63 +406,68 @@
static final int PARSE_OPTIONS = 4;

long parseKeys() throws IOException {
BufferedReader in = null;
try {
in = new BufferedReader(new InputStreamReader(
new FileInputStream(mTraceFileName), "US-ASCII"));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }

        long offset = 0;
        int mode = PARSE_VERSION;
        String line = null;
        while (true) {
            line = in.readLine();
            if (line == null) {
                throw new IOException("Key section does not have an *end marker");
            }

            // Calculate how much we have read from the file so far.  The
            // extra byte is for the line ending not included by readLine().
            offset += line.length() + 1;
            if (line.startsWith("*")) {
                if (line.equals("*version")) {
                    mode = PARSE_VERSION;
                    continue;
}
                if (line.equals("*threads")) {
                    mode = PARSE_THREADS;
                    continue;
                }
                if (line.equals("*methods")) {
                    mode = PARSE_METHODS;
                    continue;
                }
                if (line.equals("*end")) {
break;
}
}
            switch (mode) {
            case PARSE_VERSION:
                mVersionNumber = Integer.decode(line);
                mode = PARSE_OPTIONS;
                break;
            case PARSE_THREADS:
                parseThread(line);
                break;
            case PARSE_METHODS:
                parseMethod(line);
                break;
            case PARSE_OPTIONS:
                parseOption(line);
                break;
}
}

if (mClockSource == null) {
mClockSource = ClockSource.THREAD_CPU;
}
return offset;
}









//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/MainWindow.java b/traceview/src/com/android/traceview/MainWindow.java
//Synthetic comment -- index 3414d84..ebab72b 100644

//Synthetic comment -- @@ -146,20 +146,36 @@
// write into it.
File temp = File.createTempFile(base, ".trace");
temp.deleteOnExit();
        FileChannel dstChannel = new FileOutputStream(temp).getChannel();

        // First copy the contents of the key file into our temp file.
        FileChannel srcChannel = new FileInputStream(base + ".key").getChannel();
        long size = dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();

        // Then concatenate the data file.
        srcChannel = new FileInputStream(base + ".data").getChannel();
        dstChannel.transferFrom(srcChannel, size, srcChannel.size());

        // Clean up.
        srcChannel.close();
        dstChannel.close();

// Return the path of the temp file.
return temp.getPath();







