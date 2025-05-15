//<Beginning of snippet n. 0>
mDeletedLogCount = 0;
}

if (mReceiver == null) {
    return;
}

// Detection for device disconnection
if (mCurrentDevice != null && !mCurrentDevice.isConnected()) {
    Log.i("LogCatReceiver", "Device has been disconnected.");
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;

import org.eclipse.jface.preference.IPreferenceStore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LogCatReceiver {
    private static final String LOGCAT_COMMAND = "logcat -v long";
    private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;

    private LogCatMessageList mLogMessages;
    private IDevice mCurrentDevice;

    /* stop the current logcat command */
    mCurrentLogCatOutputReceiver.mIsCancelled = true;
    mCurrentLogCatOutputReceiver = null;
}

mLogMessages = null;
mCurrentDevice = null;

private void processLogLines(String[] lines) {
    List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines,
            mPidToNameMapper);

    if (newMessages.size() > 0) {
        List<LogCatMessage> deletedMessages;
        synchronized (mLogMessages) {
//<End of snippet n. 1>