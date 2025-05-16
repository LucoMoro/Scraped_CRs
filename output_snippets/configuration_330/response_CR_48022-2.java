//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE = "Settings to use while capturing system level trace";

    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;
    private int mTag;

    public SystraceOptionsDialog(Shell parentShell) {
        super(parentShell);
        setTitle(TITLE);
        setMessage(DEFAULT_MESSAGE);
    }

    protected void okPressed() {
        super.okPressed();
    }

    public SystraceOptions getSystraceOptions() {
        return mOptions;
    }

    public String getTraceFilePath() {
        return mDestinationPath;
    }

    public void checkAtraceVersion() {
        try {
            Process process = Runtime.getRuntime().exec("atrace --list_categories");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse line for supported categories
                // ...
            }
        } catch (IOException e) {
            // Handle error
        }
    }

    public class SystraceOptions {
        private static final int TAG_GFX = 1 << 1;
        private static final int TAG_INPUT = 1 << 2;

        public void setTraceTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);
            if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$
            // Add additional options as necessary
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
        final String atraceOptions = options.getCommandLineOptions() + (COMPRESS_DATA ? " -z" : "");
        SystraceTask task = new SystraceTask(device, atraceOptions);
        Thread t = new Thread(task, "Systrace Output Receiver");
        t.start(); 
    }
}

//<End of snippet n. 5>