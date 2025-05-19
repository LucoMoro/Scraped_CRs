//<Beginning of snippet n. 0>


import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.Client;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.util.concurrent.CompletableFuture;

private void loadFromDevice() {
    clearDataSet();

    final String command = getDumpsysCommand(mMode);
    if (command == null) {
        return;
    }

    CompletableFuture.runAsync(() -> {
        try {
            Client c = getCurrentClient();
            if (c != null) {
                if (!verifyGraphicsProfilingUserSettings() && !verifyGraphicsProfiling()) {
                    enableGraphicsProfiling();
                }
                if (!verifyGraphicsProfiling()) {
                    Log.e("GraphicsProfiling", "Failed to enable graphics profiling.");
                    return;
                }
                executeShellCommand(c, command);
            }
        } catch (AdbCommandRejectedException | ShellCommandUnresponsiveException | TimeoutException e) {
            Log.e("GraphicsProfiling", "Error during profiling process: " + e.getMessage(), e);
            // User notification could be handled here
        }
    });
}

private void enableGraphicsProfiling() throws AdbCommandRejectedException, ShellCommandUnresponsiveException, TimeoutException {
    String profilingCommand = "setprop debug.hwui.profile true";
    executeShellCommand(getCurrentClient(), profilingCommand);
}

private void executeShellCommand(Client c, String command) throws AdbCommandRejectedException, ShellCommandUnresponsiveException, TimeoutException {
    IShellOutputReceiver receiver = new IShellOutputReceiver() {
        @Override
        public void addOutput(byte[] data, int offset, int length) {
            // Handle command output if necessary
        }

        @Override
        public void flush() {
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    };
    c.runShellCommand(command, receiver);
}

private boolean verifyGraphicsProfilingUserSettings() {
    // Implement your logic to validate existing user settings concerning profiling
    return false; // Set this to true if user settings inhibit automatic activation
}

private boolean verifyGraphicsProfiling() {
    String verifyCommand = "getprop debug.hwui.profile";
    try {
        String result = executeShellCommandForResult(getCurrentClient(), verifyCommand);
        return "true".equals(result.trim());
    } catch (Exception e) {
        Log.e("GraphicsProfiling", "Error verifying graphics profiling: " + e.getMessage(), e);
        return false;
    }
}

private String executeShellCommandForResult(Client c, String command) throws AdbCommandRejectedException, ShellCommandUnresponsiveException, TimeoutException {
    final StringBuilder output = new StringBuilder();
    IShellOutputReceiver receiver = new IShellOutputReceiver() {
        @Override
        public void addOutput(byte[] data, int offset, int length) {
            output.append(new String(data, offset, length));
        }

        @Override
        public void flush() {
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    };
    c.runShellCommand(command, receiver);
    return output.toString();
}

//<End of snippet n. 0>