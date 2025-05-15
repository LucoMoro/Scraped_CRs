//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE =
            "Settings to use while capturing system level trace";
    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;
    private String[] availableCategories;

    public SystraceOptionsDialog(Shell parentShell) {
        super(parentShell);
        detectAtraceVersion();
    }

    private void detectAtraceVersion() {
        try {
            Process process = Runtime.getRuntime().exec("atrace --list_categories");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            availableCategories = parseAtraceOutput(output.toString());
        } catch (Exception e) {
            availableCategories = new String[] {"Static Option 1", "Static Option 2"};
            setMessage("Unable to determine atrace version, using static options.");
        }
    }

    private String[] parseAtraceOutput(String output) {
        // Logic to parse the output into supported categories
        // This is a placeholder for the actual parsing logic
        return output.split("\n"); // Assume each line is a category
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(TITLE);
        setMessage(DEFAULT_MESSAGE);
    }

    @Override
    protected void okPressed() {
        // Implementation to handle "OK" action
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

        public void addTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);
            // Additional logic for command line options based on dialog state
            return sb.toString();
        }
    }
}

//<End of snippet n. 2>