//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SystraceOptionsDialogV1 extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE = 
    "Settings to use while capturing system level trace";

    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;

    public SystraceOptionsDialogV1(Shell parentShell) {
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

    public class SystraceOptions {
        // This list is based on the tags in frameworks/native/include/utils/Trace.h
        private static final int TAG_GFX = 1 << 1;
        private static final int TAG_INPUT = 1 << 2;
        private int mTag;

        public void addTraceTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);
            // Sample options, to be customized further
            return sb.toString();
        }
    }

    public static String getAtraceVersion() {
        try {
            Process process = Runtime.getRuntime().exec("atrace --list_categories");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> categories = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
            process.waitFor();
            return String.join(", ", categories);
        } catch (Exception e) {
            return null; // Handle error appropriately
        }
    }
}

//<End of snippet n. 2>

//<Beginning of snippet n. 5>

import com.android.ide.eclipse.ddms.editors.UiAutomatorViewer;
import com.android.ide.eclipse.ddms.i18n.Messages;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialogV1;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialogV1.SystraceOptions;
import com.android.ide.eclipse.ddms.systrace.SystraceOutputParser;
import com.android.ide.eclipse.ddms.systrace.SystraceTask;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;

import org.eclipse.swt.widgets.Shell;

private void launchSystrace(final IDevice device, final Shell parentShell) {
    String atraceVersion = SystraceOptionsDialogV1.getAtraceVersion();
    final SystraceOptionsDialogV1 dlg = new SystraceOptionsDialogV1(parentShell);
    
    if (atraceVersion == null) {
        // Handle error (e.g., log it, notify user)
        return;
    }

    if (atraceVersion.contains("older_version_identifier")) {
        // Display existing capture options dialog
    } else {
        // Display new dialog with supported categories
        String supportedCategories = atraceVersion; // Use the captured categories
        // Customize dialog to reflect these categories
    }

    if (dlg.open() != SystraceOptionsDialogV1.OK) {
        return;
    }

    final SystraceOptions options = dlg.getSystraceOptions();
    // set trace tag if necessary:
    // adb shell setprop debug.atrace.tags.enableflags <tag>
    String tag = options.getTraceTag();
    if (tag != null) {
        CountDownLatch setTagLatch = new CountDownLatch(1);
        CollectingOutputReceiver receiver = new CollectingOutputReceiver(setTagLatch);
        boolean COMPRESS_DATA = true;

        monitor.setTaskName("Collecting Trace Information");
        final String atraceOptions = options.getCommandLineOptions() + (COMPRESS_DATA ? " -z" : "");
        SystraceTask task = new SystraceTask(device, atraceOptions);
        Thread t = new Thread(task, "Systrace Output Receiver");
        // Add more threading and execution logic as necessary
    }
}

//<End of snippet n. 5>