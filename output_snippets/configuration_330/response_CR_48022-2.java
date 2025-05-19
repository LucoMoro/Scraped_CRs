//<Beginning of snippet n. 2>

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE =
            "Settings to use while capturing system level trace";

    private final SystraceOptions mOptions = new SystraceOptions();
    private String mDestinationPath;

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

    public void fetchCategories(Runnable callback) {
        new Thread(() -> {
            List<String> categories = new ArrayList<>();
            try {
                Process process = Runtime.getRuntime().exec("atrace --list_categories");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    categories.add(line);
                }
                process.waitFor();
                boolean isNewVersion = checkVersion(categories);
                Display.getDefault().asyncExec(() -> {
                    if (isNewVersion) {
                        updateCategoryUI(categories);
                    } else {
                        showErrorDialog("Old version detected, displaying static categories.");
                        displayStaticCaptureOptions();
                    }
                });
            } catch (IOException | InterruptedException e) {
                Display.getDefault().asyncExec(() -> {
                    showErrorDialog("Error fetching categories: " + e.getMessage());
                });
            }
        }).start();
    }

    private boolean checkVersion(List<String> categories) {
        for (String category : categories) {
            if (category.contains("new_version_indicator")) {
                return true;
            }
        }
        return false;
    }

    private void updateCategoryUI(List<String> categories) {
        // Example implementation for UI update
        // Assume there is some UI component to display category names
        // categoriesListView.clear();
        // categories.forEach(categoriesListView::add);
    }

    private void displayStaticCaptureOptions() {
        // Method to display static "Capture Options" dialog
    }

    private void showErrorDialog(String message) {
        // Method to show an error dialog to the user
    }

    public class SystraceOptions {
        private static final int TAG_GFX = 1 << 1;
        private static final int TAG_INPUT = 1 << 2;
        private int mTag = 0;

        public void addTraceTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);
            // More options to be added as needed
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
import com.android.ide.eclipse.ddms.systrace.SystraceOutputParser;
import com.android.ide.eclipse.ddms.systrace.SystraceTask;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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
        final String atraceOptions = options.getCommandLineOptions()
                + (COMPRESS_DATA ? " -z" : "");
        SystraceTask task = new SystraceTask(device, atraceOptions);
        Thread t = new Thread(task, "Systrace Output Receiver");
        t.start();
    }
}
//<End of snippet n. 5>