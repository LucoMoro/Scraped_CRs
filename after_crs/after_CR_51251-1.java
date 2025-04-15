/*Use pid to appname mapping from ddmlib.

Replace LogCatPidToNameMapper with a call to
IDevice.getClientName(pid)

Change-Id:I8c29b5d29cccf29ea22536a3f270f0f8963aba64*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java
//Synthetic comment -- index b69a433..f1a5816 100644

//Synthetic comment -- @@ -16,7 +16,9 @@

package com.android.ddmuilib.logcat;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -60,7 +62,7 @@
* @return list of LogMessage objects parsed from the input
*/
public List<LogCatMessage> processLogLines(String[] lines,
            IDevice device) {
List<LogCatMessage> messages = new ArrayList<LogCatMessage>(lines.length);

for (String line : lines) {
//Synthetic comment -- @@ -82,9 +84,13 @@
mCurLogLevel = LogLevel.ASSERT;
}
} else {
                String pkgName = ""; //$NON-NLS-1$
                if (device != null) {
                    Integer pid = Ints.tryParse(mCurPid);
                    pkgName = device.getClientName(pid == null ? 0 : pid.intValue());
                }
LogCatMessage m = new LogCatMessage(mCurLogLevel, mCurPid, mCurTid,
                        pkgName, mCurTag, mCurTime, line);
messages.add(m);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPidToNameMapper.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPidToNameMapper.java
deleted file mode 100644
//Synthetic comment -- index a4455d0..0000000

//Synthetic comment -- @@ -1,133 +0,0 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index da3e86f..8d4e7fe 100644

//Synthetic comment -- @@ -45,7 +45,6 @@
private LogCatOutputReceiver mCurrentLogCatOutputReceiver;
private Set<ILogCatBufferChangeListener> mLogCatMessageListeners;
private LogCatMessageParser mLogCatMessageParser;
private IPreferenceStore mPrefStore;

/**
//Synthetic comment -- @@ -62,8 +61,6 @@

mLogCatMessageListeners = new HashSet<ILogCatBufferChangeListener>();
mLogCatMessageParser = new LogCatMessageParser();
mLogMessages = new LogCatMessageList(getFifoSize());

startReceiverThread();
//Synthetic comment -- @@ -157,7 +154,7 @@

private void processLogLines(String[] lines) {
List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
                mCurrentDevice);
processLogMessages(newMessages);
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java
//Synthetic comment -- index dfde250..3b5029c 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
@Override
protected void setUp() throws Exception {
LogCatMessageParser parser = new LogCatMessageParser();
        mParsedMessages = parser.processLogLines(MESSAGES, null);
}

/** Check that the correct number of messages are received. */







