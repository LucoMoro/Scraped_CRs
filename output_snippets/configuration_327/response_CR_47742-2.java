//<Beginning of snippet n. 0>
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmuilib.SysinfoPanel.BugReportParser.GfxProfileData;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.MessageBox;

private void loadFromDevice() {
    clearDataSet();

    final String command = getDumpsysCommand(mMode);
    if (command == null) {
        showMessage("Command generation failed.");
        return;
    }
    try {
        if (!isGraphicsProfilingEnabled()) {
            if (!enableGraphicsProfiling()) {
                showMessage("Failed to enable graphics profiling.");
                return;
            }
            showMessage("Graphics profiling has been enabled.");
        }
        executeShellCommand(command);
    } catch (AdbCommandRejectedException | ShellCommandUnresponsiveException | TimeoutException e) {
        Log.e("GraphicsProfiling", "Error executing shell command: " + e.getMessage());
        showMessage("Error executing command: " + e.getMessage());
    }
}

private String getDumpsysCommand(int mode) {
    if (mode == MODE_GFXINFO) {
        Client c = getCurrentClient();
        if (c == null) {
            showMessage("No current client available.");
            return null;
        }
        return "dumpsys gfxinfo " + c.getClientData().getName();
    }
    return null;
}

private void executeShellCommand(String command) throws AdbCommandRejectedException, ShellCommandUnresponsiveException, TimeoutException {
    // Logic to execute the shell command
}

private boolean enableGraphicsProfiling() {
    // Logic to enable graphics profiling
    boolean success = true; // Assume this tracks if enabling was successful
    // Add command execution for enabling graphics profiling
    return success;
}

private boolean isGraphicsProfilingEnabled() {
    // Logic to check if graphics profiling is currently enabled
    // This should return the actual state based on device conditions
    return getCurrentClient() != null && getCurrentClient().getClientData().isGraphicsProfilingEnabled();
}

private void showMessage(String message) {
    MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
    messageBox.setText("Graphics Profiling");
    messageBox.setMessage(message);
    messageBox.open();
}
//<End of snippet n. 0>