/*systrace: Support newer atrace options.

The atrace executable on the device now provides a list of
supported categories with the command atrace --list_categories.
This CL first tries to determine which version of atrace is
present on the device by running the list categories command
and attempting to parse its output.

For the older version, existing static "Capture Options" dialog
is displayed. For the newer version, only the supported categories
are displayed.

Change-Id:I300e7e652a9ae99f47d61a1669604802b74054a4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptions.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptions.java
new file mode 100644
//Synthetic comment -- index 0000000..f0e0804

//Synthetic comment -- @@ -0,0 +1,25 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.systrace;

public interface ISystraceOptions {
    /** Get tags to enable, returns null if no tags need to be enabled. */
    String getTags();

    /** Get the command line options to atrace. */
    String getOptions();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..4cc0faa

//Synthetic comment -- @@ -0,0 +1,23 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.systrace;

public interface ISystraceOptionsDialog {
    ISystraceOptions getSystraceOptions();
    String getTraceFilePath();
    int open();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialogV1.java
similarity index 93%
rename from eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java
rename to eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialogV1.java
//Synthetic comment -- index fd26f76..cacedd7 100644

//Synthetic comment -- @@ -1,3 +1,19 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
//Synthetic comment -- @@ -19,7 +35,7 @@

import java.io.File;

public class SystraceOptionsDialogV1 extends TitleAreaDialog implements ISystraceOptionsDialog {
private static final String TITLE = "Android System Trace";
private static final String DEFAULT_MESSAGE =
"Settings to use while capturing system level trace";
//Synthetic comment -- @@ -72,7 +88,7 @@

private final SystraceOptions mOptions = new SystraceOptions();

    public SystraceOptionsDialogV1(Shell parentShell) {
super(parentShell);
}

//Synthetic comment -- @@ -356,15 +372,17 @@
super.okPressed();
}

    @Override
public SystraceOptions getSystraceOptions() {
return mOptions;
}

    @Override
public String getTraceFilePath() {
return mDestinationPath;
}

    private class SystraceOptions implements ISystraceOptions {
// This list is based on the tags in frameworks/native/include/utils/Trace.h
private static final int TAG_GFX = 1 << 1;
private static final int TAG_INPUT = 1 << 2;
//Synthetic comment -- @@ -393,11 +411,13 @@
mTag |= tag;
}

        @Override
        public String getTags() {
return mTag == 0 ? null : Integer.toHexString(mTag);
}

        @Override
        public String getOptions() {
StringBuilder sb = new StringBuilder(20);

if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceTag.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceTag.java
new file mode 100644
//Synthetic comment -- index 0000000..0fc03ef

//Synthetic comment -- @@ -0,0 +1,27 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.systrace;

public class SystraceTag {
    public final String tag;
    public final String info;

    public SystraceTag(String tagName, String details) {
        tag = tagName;
        info = details;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceVersionDetector.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceVersionDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..646a454

//Synthetic comment -- @@ -0,0 +1,100 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.systrace;

import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystraceVersionDetector implements IRunnableWithProgress {
    public static final int SYSTRACE_V1 = 1;
    public static final int SYSTRACE_V2 = 2;

    private final IDevice mDevice;
    private List<SystraceTag> mTags;

    public SystraceVersionDetector(IDevice device) {
        mDevice = device;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        monitor.beginTask("Checking systrace version on device..",
                IProgressMonitor.UNKNOWN);

        CountDownLatch setTagLatch = new CountDownLatch(1);
        CollectingOutputReceiver receiver = new CollectingOutputReceiver(setTagLatch);
        try {
            String cmd = "atrace --list_categories";
            mDevice.executeShellCommand(cmd, receiver);
            setTagLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }

        String shellOutput = receiver.getOutput();
        mTags = parseSupportedTags(shellOutput);

        monitor.done();
    }

    public int getVersion() {
        if (mTags == null) {
            return SYSTRACE_V1;
        } else {
            return SYSTRACE_V2;
        }
    }

    public List<SystraceTag> getTags() {
        return mTags;
    }

    private List<SystraceTag> parseSupportedTags(String listCategoriesOutput) {
        if (listCategoriesOutput == null) {
            return null;
        }

        if (listCategoriesOutput.contains("unknown option")) {
            return null;
        }

        String[] categories = listCategoriesOutput.split("\n");
        List<SystraceTag> tags = new ArrayList<SystraceTag>(categories.length);

        Pattern p = Pattern.compile("([^-]+) - (.*)"); //$NON-NLS-1$
        for (String category : categories) {
            Matcher m = p.matcher(category);
            if (m.find()) {
                tags.add(new SystraceTag(m.group(1).trim(), m.group(2).trim()));
            }
        }

        return tags;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 5f7d0dd..e9cafce 100644

//Synthetic comment -- @@ -42,10 +42,12 @@
import com.android.ide.eclipse.ddms.editors.UiAutomatorViewer;
import com.android.ide.eclipse.ddms.i18n.Messages;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
import com.android.ide.eclipse.ddms.systrace.ISystraceOptions;
import com.android.ide.eclipse.ddms.systrace.ISystraceOptionsDialog;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialogV1;
import com.android.ide.eclipse.ddms.systrace.SystraceOutputParser;
import com.android.ide.eclipse.ddms.systrace.SystraceTask;
import com.android.ide.eclipse.ddms.systrace.SystraceVersionDetector;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;
//Synthetic comment -- @@ -562,16 +564,32 @@
};

private void launchSystrace(final IDevice device, final Shell parentShell) {
        SystraceVersionDetector detector = new SystraceVersionDetector(device);
        try {
            new ProgressMonitorDialog(parentShell).run(true, false, detector);
        } catch (InvocationTargetException e) {
            MessageDialog.openError(parentShell,
                    "Systrace",
                    "Unexpected error while detecting atrace version: " + e);
            return;
        } catch (InterruptedException e) {
return;
}

        final ISystraceOptionsDialog dlg =
                (detector.getVersion() == SystraceVersionDetector.SYSTRACE_V1) ?
                        new SystraceOptionsDialogV1(parentShell) :
                            new SystraceOptionsDialogV2(parentShell, detector.getTags());

        if (dlg.open() != SystraceOptionsDialogV1.OK) {
            return;
        }

        final ISystraceOptions options = dlg.getSystraceOptions();

// set trace tag if necessary:
//      adb shell setprop debug.atrace.tags.enableflags <tag>
        String tag = options.getTags();
if (tag != null) {
CountDownLatch setTagLatch = new CountDownLatch(1);
CollectingOutputReceiver receiver = new CollectingOutputReceiver(setTagLatch);
//Synthetic comment -- @@ -602,7 +620,7 @@
boolean COMPRESS_DATA = true;

monitor.setTaskName("Collecting Trace Information");
                    final String atraceOptions = options.getOptions()
+ (COMPRESS_DATA ? " -z" : "");
SystraceTask task = new SystraceTask(device, atraceOptions);
Thread t = new Thread(task, "Systrace Output Receiver");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialogV2.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialogV2.java
new file mode 100644
//Synthetic comment -- index 0000000..887e352

//Synthetic comment -- @@ -0,0 +1,304 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.ddms.views;

import com.android.ddmuilib.TableHelper;
import com.android.ide.eclipse.ddms.systrace.ISystraceOptions;
import com.android.ide.eclipse.ddms.systrace.ISystraceOptionsDialog;
import com.android.ide.eclipse.ddms.systrace.SystraceTag;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystraceOptionsDialogV2 extends TitleAreaDialog implements ISystraceOptionsDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE =
            "Settings to use while capturing system level trace";
    private static final String DEFAULT_TRACE_FNAME = "trace.html"; //$NON-NLS-1$

    private Text mDestinationText;
    private String mDestinationPath;
    private Text mTraceDurationText;
    private Text mTraceBufferSizeText;

    private static String sSaveToFolder = System.getProperty("user.home"); //$NON-NLS-1$
    private static String sTraceDuration = "";
    private static String sTraceBufferSize = "";
    private static Set<String> sEnabledTags = new HashSet<String>();

    private final List<SystraceTag> mSupportedTags;

    private final SystraceOptions mOptions = new SystraceOptions();
    private Table mTable;

    public SystraceOptionsDialogV2(Shell parentShell, List<SystraceTag> tags) {
        super(parentShell);
        mSupportedTags = tags;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(TITLE);
        setMessage(DEFAULT_MESSAGE);

        Composite c = new Composite(parent, SWT.BORDER);
        c.setLayout(new GridLayout(3, false));
        c.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label l = new Label(c, SWT.NONE);
        l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        l.setText("Destination File: ");

        mDestinationText = new Text(c, SWT.BORDER);
        mDestinationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mDestinationText.setText(sSaveToFolder + File.separator + DEFAULT_TRACE_FNAME);

        final Button browse = new Button(c, SWT.NONE);
        browse.setText("Browse...");
        browse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String path = openBrowseDialog(browse.getShell());
                if (path != null) mDestinationText.setText(path);
            }
        });

        Label lblTraceDurationseconds = new Label(c, SWT.NONE);
        lblTraceDurationseconds.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTraceDurationseconds.setText("Trace duration (seconds): ");

        mTraceDurationText = new Text(c, SWT.BORDER);
        mTraceDurationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        mTraceDurationText.setText(sTraceDuration);

        Label lblTraceBufferSize = new Label(c, SWT.NONE);
        lblTraceBufferSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTraceBufferSize.setText("Trace Buffer Size (kb): ");

        mTraceBufferSizeText = new Text(c, SWT.BORDER);
        mTraceBufferSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        mTraceBufferSizeText.setText(sTraceBufferSize);

        Label separator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        separator.setLayoutData(gd);

        ModifyListener m = new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        };

        mDestinationText.addModifyListener(m);
        mTraceBufferSizeText.addModifyListener(m);
        mTraceDurationText.addModifyListener(m);

        l = new Label(c, SWT.NONE);
        l.setText("Select tags to enable: ");
        l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        mTable = new Table(c, SWT.CHECK | SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        mTable.setLayoutData(gd);
        mTable.setHeaderVisible(false);
        mTable.setLinesVisible(false);

        for (SystraceTag tag : mSupportedTags) {
            TableItem item = new TableItem(mTable, SWT.NONE);
            item.setText(tag.info);
        }

        TableHelper.createTableColumn(mTable,
                "TagHeaderNotDisplayed",                //$NON-NLS-1$
                SWT.LEFT,
                "SampleTagForColumnLengthCalculation",  //$NON-NLS-1$
                null,
                null);

        return c;
    }

    private void validateFields() {
        // validate trace destination path
        String msg = validatePath(mDestinationText.getText());
        if (msg != null) {
            setErrorMessage(msg);
            getButton(OK).setEnabled(false);
            return;
        }

        // validate the trace duration
        if (!validateInteger(mTraceDurationText.getText())) {
            setErrorMessage("Trace Duration should be a valid integer (seconds)");
            getButton(OK).setEnabled(false);
            return;
        }

        // validate the trace buffer size
        if (!validateInteger(mTraceBufferSizeText.getText())) {
            setErrorMessage("Trace Buffer Size should be a valid integer (kilobytes)");
            getButton(OK).setEnabled(false);
            return;
        }

        getButton(OK).setEnabled(true);
        setErrorMessage(null);
    }

    private boolean validateInteger(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }

        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String validatePath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        File f = new File(path);
        if (f.isDirectory()) {
            return String.format("The path '%s' points to a folder", path);
        }

        if (!f.exists()) { // if such a file doesn't exist, make sure the parent folder is valid
            if (!f.getParentFile().isDirectory()) {
                return String.format("That path '%s' is not a valid folder.", f.getParent());
            }
        }

        return null;
    }

    private String openBrowseDialog(Shell parentShell) {
        FileDialog fd = new FileDialog(parentShell, SWT.SAVE);

        fd.setText("Save To");
        fd.setFileName(DEFAULT_TRACE_FNAME);

        fd.setFilterPath(sSaveToFolder);
        fd.setFilterExtensions(new String[] { "*.html" }); //$NON-NLS-1$

        String fname = fd.open();
        if (fname == null || fname.trim().length() == 0) {
            return null;
        }

        sSaveToFolder = fd.getFilterPath();
        return fname;
    }

    @Override
    protected void okPressed() {
        mDestinationPath = mDestinationText.getText().trim();

        sTraceDuration = mTraceDurationText.getText();
        if (!sTraceDuration.isEmpty()) {
            mOptions.mTraceDuration = Integer.parseInt(sTraceDuration);
        }

        sTraceBufferSize = mTraceBufferSizeText.getText();
        if (!sTraceBufferSize.isEmpty()) {
            mOptions.mTraceBufferSize = Integer.parseInt(sTraceBufferSize);
        }

        sEnabledTags.clear();
        for (int i = 0; i < mTable.getItemCount(); i++) {
            TableItem it = mTable.getItem(i);
            if (it.getChecked()) {
                sEnabledTags.add(mSupportedTags.get(i).tag);
            }
        }

        super.okPressed();
    }

    @Override
    public ISystraceOptions getSystraceOptions() {
        return mOptions;
    }

    @Override
    public String getTraceFilePath() {
        return mDestinationPath;
    }

    private class SystraceOptions implements ISystraceOptions {
        private int mTraceBufferSize;
        private int mTraceDuration;

        @Override
        public String getTags() {
            return null;
        }

        @Override
        public String getOptions() {
            StringBuilder sb = new StringBuilder(5 * mSupportedTags.size());

            if (mTraceDuration > 0) {
                sb.append("-t");    //$NON-NLS-1$
                sb.append(mTraceDuration);
                sb.append(' ');
            }

            if (mTraceBufferSize > 0) {
                sb.append("-b ");   //$NON-NLS-1$
                sb.append(mTraceBufferSize);
                sb.append(' ');
            }

            for (String s : sEnabledTags) {
                sb.append(s);
                sb.append(' ');
            }

            return sb.toString().trim();
        }
    }
}







