/*logcat: Add a device disconnected message.

When a device is disconnected, an explicit message informing
the user of this event is attached to the log.

Seehttp://code.google.com/p/android/issues/detail?id=25830for
the reason why this might be useful.

Change-Id:I969004cb72b877b8319a639607f82c1c9f9a8635*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index d82895f..a7772e7 100644

//Synthetic comment -- @@ -994,7 +994,7 @@
mDeletedLogCount = 0;
}

        if (mReceiver == null) {
return;
}









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index 8f2d52c..ba2c380 100644

//Synthetic comment -- @@ -20,9 +20,11 @@
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;

import org.eclipse.jface.preference.IPreferenceStore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -34,6 +36,9 @@
public final class LogCatReceiver {
private static final String LOGCAT_COMMAND = "logcat -v long";
private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;

private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
//Synthetic comment -- @@ -72,9 +77,11 @@
/* stop the current logcat command */
mCurrentLogCatOutputReceiver.mIsCancelled = true;
mCurrentLogCatOutputReceiver = null;
}

        mLogMessages = null;
mCurrentDevice = null;
}

//Synthetic comment -- @@ -149,7 +156,10 @@
private void processLogLines(String[] lines) {
List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
mPidToNameMapper);

if (newMessages.size() > 0) {
List<LogCatMessage> deletedMessages;
synchronized (mLogMessages) {







