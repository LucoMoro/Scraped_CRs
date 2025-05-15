
//<Beginning of snippet n. 0>


mDeletedLogCount = 0;
}

        if (mReceiver == null || mReceiver.getMessages() == null) {
return;
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;
import com.android.ddmlib.Log.LogLevel;

import org.eclipse.jface.preference.IPreferenceStore;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public final class LogCatReceiver {
private static final String LOGCAT_COMMAND = "logcat -v long";
private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;
    private static LogCatMessage DEVICE_DISCONNECTED_MESSAGE =
            new LogCatMessage(LogLevel.ERROR, "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    "", "", "Device disconnected");       //$NON-NLS-1$ //$NON-NLS-2$

private LogCatMessageList mLogMessages;
private IDevice mCurrentDevice;
/* stop the current logcat command */
mCurrentLogCatOutputReceiver.mIsCancelled = true;
mCurrentLogCatOutputReceiver = null;

            // add a message to the log indicating that the device has been disconnected.
            processLogMessages(Collections.singletonList(DEVICE_DISCONNECTED_MESSAGE));
}

mCurrentDevice = null;
}

private void processLogLines(String[] lines) {
List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
mPidToNameMapper);
        processLogMessages(newMessages);
    }

    private void processLogMessages(List<LogCatMessage> newMessages) {
if (newMessages.size() > 0) {
List<LogCatMessage> deletedMessages;
synchronized (mLogMessages) {

//<End of snippet n. 1>








