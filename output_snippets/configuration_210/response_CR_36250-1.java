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

    public void deviceDisconnected() {
        Log.d("LogCatReceiver", "Device disconnected" + (mCurrentDevice != null ? ": " + mCurrentDevice.getId() : " (unknown device)"));
        if (mCurrentDevice != null) {
            mCurrentDevice = null; 
            mLogMessages = null;
        }
    }

    private void processLogLines(String[] lines) {
        List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines, mPidToNameMapper);

        if (newMessages.size() > 0) {
            List<LogCatMessage> deletedMessages;
            synchronized (mLogMessages) {
                // Add further processing logic here if needed
            }
        }
    }

    /* stop the current logcat command */
    private void stopLogCat() {
        if (mCurrentLogCatOutputReceiver != null) {
            mCurrentLogCatOutputReceiver.mIsCancelled = true;
            mCurrentLogCatOutputReceiver = null;
        }
    }
}

//<End of snippet n. 1>