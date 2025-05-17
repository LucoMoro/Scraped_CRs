//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE = "Settings to use while capturing system level trace";
    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;
    private String mAtraceVersion;

    public SystraceOptionsDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void okPressed() {
        super.okPressed();
    }

    public SystraceOptions getSystraceOptions() {
        return mOptions;
    }

    public String getTraceFilePath() {
        return mDestinationPath;
    }

    public void detectAtraceVersion() {
        try {
            Process process = Runtime.getRuntime().exec("atrace --list_categories");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            parseAtraceOutput(output.toString());
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Failed to detect atrace version: " + e.getMessage());
        }
    }

    private void parseAtraceOutput(String output) {
        if (output.contains("Version")) {
            mAtraceVersion = output.substring(output.indexOf("Version") + 8, output.indexOf("\n", output.indexOf("Version")));
            // Implement logic to determine supported categories
        } else {
            JOptionPane.showMessageDialog(null, "Unable to parse atrace output");
        }
    }

    public class SystraceOptions {
        private static final int TAG_GFX = 1 << 1;
        private static final int TAG_INPUT = 1 << 2;
        private int mTag = 0;

        public void addTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder();
            if (mTag != 0) sb.append("-t ").append(getTraceTag()).append(" "); // Add tag options if set
            return sb.toString();
        }
    }
}

//<End of snippet n. 2>

//<Beginning of snippet n. 5>

import com.android.ide.eclipse.ddms.editors.UiAutomatorViewer;
import com.android.ide.eclipse.ddms.i18n.Messages;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialog;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialog.SystraceOptions;
import com.android.ide.eclipse.ddms.systrace.SystraceOutputParser;
import com.android.ide.eclipse.ddms.systrace.SystraceTask;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;

private void launchSystrace(final IDevice device, final Shell parentShell) {
    final SystraceOptionsDialog dlg = new SystraceOptionsDialog(parentShell);
    dlg.detectAtraceVersion();
    if (dlg.open() != SystraceOptionsDialog.OK) {
        return;
    }

    final SystraceOptions options = dlg.getSystraceOptions();
    String tag = options.getTraceTag();
    if (tag != null) {
        CountDownLatch setTagLatch = new CountDownLatch(1);
        CollectingOutputReceiver receiver = new CollectingOutputReceiver(setTagLatch);
        boolean COMPRESS_DATA = true;

        monitor.setTaskName("Collecting Trace Information");
        final String atraceOptions = options.getCommandLineOptions() + 
            (COMPRESS_DATA ? " -z" : "");
        SystraceTask task = new SystraceTask(device, atraceOptions);
        Thread t = new Thread(task, "Systrace Output Receiver");
        t.start();
    }
}

//<End of snippet n. 5>