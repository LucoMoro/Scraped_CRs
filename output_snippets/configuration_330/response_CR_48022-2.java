//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE =
            "Settings to use while capturing system level trace";
    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;
    private int mTag;
    private boolean mTraceCpuFreq;

    public SystraceOptionsDialog(Shell parentShell) {
        super(parentShell);
        setTitle(TITLE);
        setMessage(DEFAULT_MESSAGE);
        detectAtraceVersion();
    }

    private void detectAtraceVersion() {
        try {
            Process process = Runtime.getRuntime().exec("atrace --list_categories");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> categories = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
            process.waitFor();
            // Parse categories here and set supported options
        } catch (Exception e) {
            // Handle errors and provide user feedback
        }
    }

    @Override
    protected void okPressed() {
        // Validate options before proceeding
        super.okPressed();
    }

    public SystraceOptions getSystraceOptions() {
        return mOptions;
    }

    public String getTraceFilePath() {
        return mDestinationPath;
    }

    public class SystraceOptions {
        private static final int TAG_GFX = 1 << 1;
        private static final int TAG_INPUT = 1 << 2;

        public void addTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);
            if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$
            return sb.toString();
        }
    }
}

//<End of snippet n. 2>