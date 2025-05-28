
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
"Frame Render Time",
};

/**
* Generates the dataset to display.
*
private void loadFromDevice() {
clearDataSet();

final String command = getDumpsysCommand(mMode);
if (command == null) {
return;
t.start();
}

private String getDumpsysCommand(int mode) {
if (mode == MODE_GFXINFO) {
Client c = getCurrentClient();

//<End of snippet n. 0>








