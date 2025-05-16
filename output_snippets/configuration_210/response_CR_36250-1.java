//<Beginning of snippet n. 0>
mDeletedLogCount = 0;
}

if (mReceiver == null) {
    return;
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
    private static final long DISCONNECT_THRESHOLD_MS = 5000; // 5 seconds
    private LogCatMessageList mLogMessages;
    private IDevice mCurrentDevice;
    private long lastDisconnectTime = 0;

    public void stopLogCat() {
        if (mCurrentLogCatOutputReceiver != null) {
            mCurrentLogCatOutputReceiver.mIsCancelled = true;
            mCurrentLogCatOutputReceiver = null;
        }
        mLogMessages = null;
        mCurrentDevice = null;
    }

    private void processLogLines(String[] lines) {
        List<LogCatMessage> newMessages = mLogCatMessageParser.processLogLines(lines, mPidToNameMapper);

        if (newMessages.size() > 0) {
            List<LogCatMessage> deletedMessages;
            synchronized (mLogMessages) {
                // Process disconnection detection
                for (LogCatMessage message : newMessages) {
                    if (message.getMessage().contains("disconnected")) {
                        handleDisconnection();
                    }
                }
                // Existing logic for handling messages
            }
        }
    }

    private void handleDisconnection() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDisconnectTime > DISCONNECT_THRESHOLD_MS) {
            Log.d("LogCatReceiver", "Device disconnected.");
            lastDisconnectTime = currentTime;
        }
    }
}
//<End of snippet n. 1>