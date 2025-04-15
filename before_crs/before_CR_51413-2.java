/*Move core logcat functionality from ddmuilib to ddmlib

Requires Change-IdIe120f978e5c5646e086ec999c9ef5027b724cc7ain tools/base

Change-Id:Ib436fa6b70ba49e48b2e7c974094c27b77c9fbb9*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatBufferChangeListener.java
//Synthetic comment -- index 1a547c7..2804629 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import java.util.List;

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageSelectionListener.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/ILogCatMessageSelectionListener.java
//Synthetic comment -- index 6e814b0..728b518 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ddmuilib.logcat;

/**
* Classes interested in listening to user selection of logcat
* messages should implement this interface.








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatFilter.java
//Synthetic comment -- index 7bdd98a..3024978 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.ArrayList;
import java.util.List;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessage.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessage.java
deleted file mode 100644
//Synthetic comment -- index aea4ead..0000000

//Synthetic comment -- @@ -1,96 +0,0 @@
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

package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

/**
 * Model a single log message output from {@code logcat -v long}.
 * A logcat message has a {@link LogLevel}, the pid (process id) of the process
 * generating the message, the time at which the message was generated, and
 * the tag and message itself.
 */
public final class LogCatMessage {
    private final LogLevel mLogLevel;
    private final String mPid;
    private final String mTid;
    private final String mAppName;
    private final String mTag;
    private final String mTime;
    private final String mMessage;

    /**
     * Construct an immutable log message object.
     */
    public LogCatMessage(LogLevel logLevel, String pid, String tid, String appName,
            String tag, String time, String msg) {
        mLogLevel = logLevel;
        mPid = pid;
        mAppName = appName;
        mTag = tag;
        mTime = time;
        mMessage = msg;

        long tidValue;
        try {
            // Thread id's may be in hex on some platforms.
            // Decode and store them in radix 10.
            tidValue = Long.decode(tid.trim());
        } catch (NumberFormatException e) {
            tidValue = -1;
        }

        mTid = Long.toString(tidValue);
    }

    public LogLevel getLogLevel() {
        return mLogLevel;
    }

    public String getPid() {
        return mPid;
    }

    public String getTid() {
        return mTid;
    }

    public String getAppName() {
        return mAppName;
    }

    public String getTag() {
        return mTag;
    }

    public String getTime() {
        return mTime;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return mTime + ": "
                + mLogLevel.getPriorityLetter() + "/"
                + mTag + "("
                + mPid + "): "
                + mMessage;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageList.java
//Synthetic comment -- index 080dbc1..c5cd548 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmuilib.logcat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatMessageParser.java
deleted file mode 100644
//Synthetic comment -- index f1a5816..0000000

//Synthetic comment -- @@ -1,100 +0,0 @@
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

package com.android.ddmuilib.logcat;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to parse raw output of {@code adb logcat -v long} to {@link LogCatMessage} objects.
 */
public final class LogCatMessageParser {
    private LogLevel mCurLogLevel = LogLevel.WARN;
    private String mCurPid = "?";
    private String mCurTid = "?";
    private String mCurTag = "?";
    private String mCurTime = "?:??";

    /**
     * This pattern is meant to parse the first line of a log message with the option
     * 'logcat -v long'. The first line represents the date, tag, severity, etc.. while the
     * following lines are the message (can be several lines).<br>
     * This first line looks something like:<br>
     * {@code "[ 00-00 00:00:00.000 <pid>:0x<???> <severity>/<tag>]"}
     * <br>
     * Note: severity is one of V, D, I, W, E, A? or F. However, there doesn't seem to be
     *       a way to actually generate an A (assert) message. Log.wtf is supposed to generate
     *       a message with severity A, however it generates the undocumented F level. In
     *       such a case, the parser will change the level from F to A.<br>
     * Note: the fraction of second value can have any number of digit.<br>
     * Note: the tag should be trimmed as it may have spaces at the end.
     */
    private static Pattern sLogHeaderPattern = Pattern.compile(
            "^\\[\\s(\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\.\\d+)"
          + "\\s+(\\d*):\\s*(\\S+)\\s([VDIWEAF])/(.*)\\]$");

    /**
     * Parse a list of strings into {@link LogCatMessage} objects. This method
     * maintains state from previous calls regarding the last seen header of
     * logcat messages.
     * @param lines list of raw strings obtained from logcat -v long
     * @param pidToNameMapper mapper to obtain the app name given a pid
     * @return list of LogMessage objects parsed from the input
     */
    public List<LogCatMessage> processLogLines(String[] lines,
            IDevice device) {
        List<LogCatMessage> messages = new ArrayList<LogCatMessage>(lines.length);

        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }

            Matcher matcher = sLogHeaderPattern.matcher(line);
            if (matcher.matches()) {
                mCurTime = matcher.group(1);
                mCurPid = matcher.group(2);
                mCurTid = matcher.group(3);
                mCurLogLevel = LogLevel.getByLetterString(matcher.group(4));
                mCurTag = matcher.group(5).trim();

                /* LogLevel doesn't support messages with severity "F". Log.wtf() is supposed
                 * to generate "A", but generates "F". */
                if (mCurLogLevel == null && matcher.group(4).equals("F")) {
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

        return messages;
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 4f27f02..c978de7 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.ITableFocusListener;








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index 8d4e7fe..a85cd03 100644

//Synthetic comment -- @@ -17,10 +17,10 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.MultiLineReceiver;

import org.eclipse.jface.preference.IPreferenceStore;

//Synthetic comment -- @@ -33,18 +33,15 @@
* A class to monitor a device for logcat messages. It stores the received
* log messages in a circular buffer.
*/
public final class LogCatReceiver {
    private static final String LOGCAT_COMMAND = "logcat -v long";
    private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;
private static LogCatMessage DEVICE_DISCONNECTED_MESSAGE =
new LogCatMessage(LogLevel.ERROR, "", "", "",
"", "", "Device disconnected");

private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
    private LogCatOutputReceiver mCurrentLogCatOutputReceiver;
private Set<ILogCatBufferChangeListener> mLogCatMessageListeners;
    private LogCatMessageParser mLogCatMessageParser;
private IPreferenceStore mPrefStore;

/**
//Synthetic comment -- @@ -60,7 +57,6 @@
mPrefStore = prefStore;

mLogCatMessageListeners = new HashSet<ILogCatBufferChangeListener>();
        mLogCatMessageParser = new LogCatMessageParser();
mLogMessages = new LogCatMessageList(getFifoSize());

startReceiverThread();
//Synthetic comment -- @@ -70,13 +66,14 @@
* Stop receiving messages from currently active device.
*/
public void stop() {
        if (mCurrentLogCatOutputReceiver != null) {
/* stop the current logcat command */
            mCurrentLogCatOutputReceiver.mIsCancelled = true;
            mCurrentLogCatOutputReceiver = null;

// add a message to the log indicating that the device has been disconnected.
            processLogMessages(Collections.singletonList(DEVICE_DISCONNECTED_MESSAGE));
}

mCurrentDevice = null;
//Synthetic comment -- @@ -88,85 +85,26 @@
}

private void startReceiverThread() {
        mCurrentLogCatOutputReceiver = new LogCatOutputReceiver();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                /* wait while the device comes online */
                while (mCurrentDevice != null && !mCurrentDevice.isOnline()) {
                    try {
                        Thread.sleep(DEVICE_POLL_INTERVAL_MSEC);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                try {
                    if (mCurrentDevice != null) {
                        mCurrentDevice.executeShellCommand(LOGCAT_COMMAND,
                            mCurrentLogCatOutputReceiver, 0);
                    }
                } catch (Exception e) {
                    /* There are 4 possible exceptions: TimeoutException,
                     * AdbCommandRejectedException, ShellCommandUnresponsiveException and
                     * IOException. In case of any of them, the only recourse is to just
                     * log this unexpected situation and move on.
                     */
                    Log.e("Unexpected error while launching logcat. Try reselecting the device.",
                            e);
                }
            }
        });
t.setName("LogCat output receiver for " + mCurrentDevice.getSerialNumber());
t.start();
}

    /**
     * LogCatOutputReceiver implements {@link MultiLineReceiver#processNewLines(String[])},
     * which is called whenever there is output from logcat. It simply redirects this output
     * to {@link LogCatReceiver#processLogLines(String[])}. This class is expected to be
     * used from a different thread, and the only way to stop that thread is by using the
     * {@link LogCatOutputReceiver#mIsCancelled} variable.
     * See {@link IDevice#executeShellCommand(String, IShellOutputReceiver, int)} for more
     * details.
     */
    private class LogCatOutputReceiver extends MultiLineReceiver {
        private boolean mIsCancelled;

        public LogCatOutputReceiver() {
            setTrimLine(false);
}

        /** Implements {@link IShellOutputReceiver#isCancelled() }. */
        @Override
        public boolean isCancelled() {
            return mIsCancelled;
        }

        @Override
        public void processNewLines(String[] lines) {
            if (!mIsCancelled) {
                processLogLines(lines);
            }
        }
    }

    private void processLogLines(String[] lines) {
        List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
                mCurrentDevice);
        processLogMessages(newMessages);
    }

    private void processLogMessages(List<LogCatMessage> newMessages) {
        if (newMessages.size() > 0) {
            List<LogCatMessage> deletedMessages;
            synchronized (mLogMessages) {
                deletedMessages = mLogMessages.ensureSpace(newMessages.size());
                mLogMessages.appendMessages(newMessages);
            }
            sendLogChangedEvent(newMessages, deletedMessages);
        }
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatFilterTest.java
//Synthetic comment -- index 7fedb08..98b186e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.List;









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/LogCatMessageParserTest.java
deleted file mode 100644
//Synthetic comment -- index 3b5029c..0000000

//Synthetic comment -- @@ -1,99 +0,0 @@
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
package com.android.ddmuilib.logcat;

import com.android.ddmlib.Log.LogLevel;

import java.util.List;

import junit.framework.TestCase;

/**
 * Unit tests for {@link LogCatMessageParser}.
 */
public final class LogCatMessageParserTest extends TestCase {
    private List<LogCatMessage> mParsedMessages;

    /** A list of messages generated with the following code:
     * <pre>
     * {@code
     * Log.d("dtag", "debug message");
     * Log.e("etag", "error message");
     * Log.i("itag", "info message");
     * Log.v("vtag", "verbose message");
     * Log.w("wtag", "warning message");
     * Log.wtf("wtftag", "wtf message");
     * Log.d("dtag", "debug message");
     * }
     *  </pre>
     *  Note: On Android 2.3, Log.wtf doesn't really generate the message.
     *  It only produces the message header, but swallows the message tag.
     *  This string has been modified to include the message.
     */
    private static final String[] MESSAGES = new String[] {
            "[ 08-11 19:11:07.132   495:0x1ef D/dtag     ]", //$NON-NLS-1$
            "debug message",                                 //$NON-NLS-1$
            "[ 08-11 19:11:07.132   495:  234 E/etag     ]", //$NON-NLS-1$
            "error message",                                 //$NON-NLS-1$
            "[ 08-11 19:11:07.132   495:0x1ef I/itag     ]", //$NON-NLS-1$
            "info message",                                  //$NON-NLS-1$
            "[ 08-11 19:11:07.132   495:0x1ef V/vtag     ]", //$NON-NLS-1$
            "verbose message",                               //$NON-NLS-1$
            "[ 08-11 19:11:07.132   495:0x1ef W/wtag     ]", //$NON-NLS-1$
            "warning message",                               //$NON-NLS-1$
            "[ 08-11 19:11:07.132   495:0x1ef F/wtftag   ]", //$NON-NLS-1$
            "wtf message",                                   //$NON-NLS-1$
            "[ 08-11 21:15:35.7524  540:0x21c D/dtag     ]", //$NON-NLS-1$
            "debug message",                                 //$NON-NLS-1$
    };

    @Override
    protected void setUp() throws Exception {
        LogCatMessageParser parser = new LogCatMessageParser();
        mParsedMessages = parser.processLogLines(MESSAGES, null);
    }

    /** Check that the correct number of messages are received. */
    public void testMessageCount() {
        assertEquals(7, mParsedMessages.size());
    }

    /** Check the log level in a few of the parsed messages. */
    public void testLogLevel() {
        assertEquals(mParsedMessages.get(0).getLogLevel(), LogLevel.DEBUG);
        assertEquals(mParsedMessages.get(5).getLogLevel(), LogLevel.ASSERT);
    }

    /** Check the parsed tag. */
    public void testTag() {
        assertEquals(mParsedMessages.get(1).getTag(), "etag");  //$NON-NLS-1$
    }

    /** Check the time field. */
    public void testTime() {
        assertEquals(mParsedMessages.get(6).getTime(), "08-11 21:15:35.7524"); //$NON-NLS-1$
    }

    /** Check the message field. */
    public void testMessage() {
        assertEquals(mParsedMessages.get(2).getMessage(), MESSAGES[5]);
    }

    public void testTid() {
        assertEquals(mParsedMessages.get(0).getTid(), Integer.toString(0x1ef));
        assertEquals(mParsedMessages.get(1).getTid(), "234");
    }
}







