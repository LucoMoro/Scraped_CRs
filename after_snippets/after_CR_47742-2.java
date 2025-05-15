
//<Beginning of snippet n. 0>


import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmuilib.SysinfoPanel.BugReportParser.GfxProfileData;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
"Frame Render Time",
};

    /** Shell property that controls whether graphics profiling is enabled or not. */
    private static final String PROP_GFX_PROFILING = "debug.hwui.profile"; //$NON-NLS-1$

/**
* Generates the dataset to display.
*
private void loadFromDevice() {
clearDataSet();

        if (mMode == MODE_GFXINFO) {
            boolean en = isGfxProfilingEnabled();
            if (!en) {
                if (enableGfxProfiling()) {
                    MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
                            "DDMS",
                            "Graphics profiling was enabled on the device.\n" +
                            "It may be necessary to relaunch your application to see profile information.");
                } else {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            "DDMS",
                            "Unexpected error enabling graphics profiling on device.\n");
                    return;
                }
            }
        }

final String command = getDumpsysCommand(mMode);
if (command == null) {
return;
t.start();
}

    private boolean isGfxProfilingEnabled() {
        IDevice device = getCurrentDevice();
        if (device == null) {
            return false;
        }

        String prop;
        try {
            prop = device.getPropertySync(PROP_GFX_PROFILING);
            return Boolean.valueOf(prop);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean enableGfxProfiling() {
        IDevice device = getCurrentDevice();
        if (device == null) {
            return false;
        }

        try {
            device.executeShellCommand("setprop " + PROP_GFX_PROFILING + " true",
                    new NullOutputReceiver());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

private String getDumpsysCommand(int mode) {
if (mode == MODE_GFXINFO) {
Client c = getCurrentClient();

//<End of snippet n. 0>








