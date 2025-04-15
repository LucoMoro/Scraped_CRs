/*wip: add systrace

Change-Id:I4564421b86ebb384e5bd2b3e2329249abd3771aa*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java
//Synthetic comment -- index 71639ec..fe8c709 100644

//Synthetic comment -- @@ -464,7 +464,7 @@
return sAdbLocation;
}

    public static String getToolsFolder() {
return sToolsFolder;
}

//Synthetic comment -- @@ -505,6 +505,8 @@
sHprofConverter = hprofConverter.getAbsolutePath();
DdmUiPreferences.setTraceviewLocation(traceview.getAbsolutePath());

        sToolsFolder = traceview.getParent();

return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceHtmlWriter.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceHtmlWriter.java
new file mode 100644
//Synthetic comment -- index 0000000..bf1f145

//Synthetic comment -- @@ -0,0 +1,161 @@
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

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class SystraceHtmlWriter {
    private static final String CR = "\r"; //$NON-NLS-1$
    private static final String NL = "\n"; //$NON-NLS-1$
    private static final String TRACE_START = "TRACE:\n"; //$NON-NLS-1$

    private static final String HTML_PREFIX = "<!DOCTYPE HTML>\n"
            + "<html>\n"
            + "<head i18n-values=\"dir:textdirection;\">\n"
            + "<title>Android System Trace</title>\n"
            + "%s\n"
            + "%s\n"
            + "<style>\n"
            + "  .view {\n"
            + "    overflow: hidden;\n"
            + "    position: absolute;\n"
            + "    top: 0;\n"
            + "    bottom: 0;\n"
            + "    left: 0;\n"
            + "    right: 0;\n"
            + "  }\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div class=\"view\">\n"
            + "  </div>\n"
            + "  <script>\n"
            + "  var linuxPerfData = \"\\\n";

    private static final String HTML_SUFFIX =
              "           dummy-0000  [000] 0.0: 0: trace_event_clock_sync: parent_ts=0.0\\n\";\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>\n";

    private final File mDest;
    private final File mAssetsFolder;

    private boolean mTraceStarted;
    private boolean mTraceCompleted;
    private String mLeftovers = "";

    public SystraceHtmlWriter(File dest, File systraceAssetsFolder) {
        mDest = dest;
        mAssetsFolder = systraceAssetsFolder;
    }

    public void addAdbOutput(String s) {
        s = mLeftovers + s;
        s = s.replaceAll(CR + NL, NL);

        if (s.endsWith(CR)) {
            s = s.substring(0, s.length() - 1);
            mLeftovers = CR;
        } else {
            mLeftovers = "";
        }

        if (!mTraceStarted) {
            int i = s.indexOf(TRACE_START);
            if (i < 0) {
                mLeftovers = s;
            } else {
                mTraceStarted = true;
                writeHtmlHeader();
                if (s.length() > i + TRACE_START.length()) {
                    s = s.substring(i + TRACE_START.length());
                } else {
                    s = "";
                }
            }
        }

        if (mTraceStarted && s.length() > 0) {
            // each line should end with the characters \n\ followed by a newline
            String html_out = s.replaceAll(NL, "\\\\n\\\\" + NL);
            try {
                Files.append(html_out, mDest, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void flush() {
        if (mTraceStarted) {
            writeHtmlFooter();
            mTraceCompleted = true;
        }
    }

    private void writeHtmlHeader() {
        String buf = String.format(HTML_PREFIX, getCss(), getJs());
        try {
            Files.write(buf.getBytes(), mDest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJs() {
        try {
            return String.format("<script language=\"javascript\">%s</script>",
                    Files.toString(new File(mAssetsFolder, "script.js"), Charsets.UTF_8));
        } catch (IOException e) {
            return "";
        }
    }

    private String getCss() {
        try {
            return String.format("<style type=\"text/css\">%s</style>",
                    Files.toString(new File(mAssetsFolder, "style.css"), Charsets.UTF_8));
        } catch (IOException e) {
            return "";
        }
    }

    private void writeHtmlFooter() {
        try {
            Files.append(HTML_SUFFIX, mDest, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean traceStarted() {
        return mTraceStarted;
    }

    public boolean traceCompleted() {
        return mTraceCompleted;
    }

    public String getLog() {
        return mLeftovers;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..98b5b34

//Synthetic comment -- @@ -0,0 +1,420 @@
package com.android.ide.eclipse.ddms.systrace;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;

public class SystraceOptionsDialog extends TitleAreaDialog {
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

    private Button mTraceCpuFreqBtn;
    private Button mTraceCpuIdleBtn;
    private Button mTraceCpuLoadBtn;
    private Button mTraceDiskIoBtn;
    private Button mTraceKernelWorkqueuesBtn;
    private Button mTraceCpuSchedulerBtn;

    private static boolean sTraceCpuFreq;
    private static boolean sTraceCpuIdle;
    private static boolean sTraceCpuLoad;
    private static boolean sTraceDiskIo;
    private static boolean sTraceKernelWorkqueues;
    private static boolean sTraceCpuScheduler;

    private Button mGfxTagBtn;
    private Button mInputTagBtn;
    private Button mViewTagBtn;
    private Button mWebViewTagBtn;
    private Button mWmTagBtn;
    private Button mAmTagBtn;
    private Button mSyncTagBtn;
    private Button mAudioTagBtn;
    private Button mVideoTagBtn;
    private Button mCameraTagBtn;

    private static boolean sGfxTag;
    private static boolean sInputTag;
    private static boolean sViewTag;
    private static boolean sWebViewTag;
    private static boolean sWmTag;
    private static boolean sAmTag;
    private static boolean sSyncTag;
    private static boolean sAudioTag;
    private static boolean sVideoTag;
    private static boolean sCameraTag;

    private final SystraceOptions mOptions = new SystraceOptions();

    public SystraceOptionsDialog(Shell parentShell) {
        super(parentShell);
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

        Group grpTraceEvents = new Group(c, SWT.BORDER);
        grpTraceEvents.setLayout(new GridLayout(3, false));
        grpTraceEvents.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
        grpTraceEvents.setText("Trace Events");

        mTraceCpuFreqBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuFreqBtn.setText("CPU Frequency Changes");
        mTraceCpuFreqBtn.setSelection(sTraceCpuFreq);

        mTraceDiskIoBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceDiskIoBtn.setText("Disk I/O (requires root)");
        mTraceDiskIoBtn.setSelection(sTraceDiskIo);

        mTraceCpuIdleBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuIdleBtn.setText("CPU Idle Events");
        mTraceCpuIdleBtn.setSelection(sTraceCpuIdle);

        mTraceCpuLoadBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuLoadBtn.setText("CPU Load");
        mTraceCpuLoadBtn.setSelection(sTraceCpuLoad);

        mTraceKernelWorkqueuesBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceKernelWorkqueuesBtn.setText("Kernel Workqueues (requires root)");
        mTraceKernelWorkqueuesBtn.setSelection(sTraceKernelWorkqueues);

        mTraceCpuSchedulerBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuSchedulerBtn.setText("CPU Scheduler");
        mTraceCpuSchedulerBtn.setSelection(sTraceCpuScheduler);

        Group grpTraceTags = new Group(c, SWT.BORDER);
        grpTraceTags.setLayout(new GridLayout(5, false));
        grpTraceTags.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
        grpTraceTags.setText("Trace Tags");

        mGfxTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mGfxTagBtn.setText("gfx");
        mGfxTagBtn.setSelection(sGfxTag);

        mInputTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mInputTagBtn.setText("input");
        mInputTagBtn.setSelection(sInputTag);

        mViewTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mViewTagBtn.setText("view");
        mViewTagBtn.setSelection(sViewTag);

        mWebViewTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mWebViewTagBtn.setText("webview");
        mWebViewTagBtn.setSelection(sWebViewTag);

        mWmTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mWmTagBtn.setText("wm");
        mWmTagBtn.setSelection(sWmTag);

        mAmTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mAmTagBtn.setText("am");
        mAmTagBtn.setSelection(sAmTag);

        mSyncTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mSyncTagBtn.setText("sync");
        mSyncTagBtn.setSelection(sSyncTag);

        mAudioTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mAudioTagBtn.setText("audio");
        mAudioTagBtn.setSelection(sAudioTag);

        mVideoTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mVideoTagBtn.setText("video");
        mVideoTagBtn.setSelection(sVideoTag);

        mCameraTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mCameraTagBtn.setText("camera");
        mCameraTagBtn.setSelection(sCameraTag);

        Label lblTraceTagsWarning = new Label(grpTraceTags, SWT.NONE);
        lblTraceTagsWarning.setText(
                "Changes to trace tags will likely need a restart of the Android framework to take effect:\n"
                + "    $ adb shell stop\n"
                + "    $ adb shell start");
        lblTraceTagsWarning.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 5, 1));

        ModifyListener m = new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        };

        mDestinationText.addModifyListener(m);
        mTraceBufferSizeText.addModifyListener(m);
        mTraceDurationText.addModifyListener(m);

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

        mOptions.mTraceCpuFreq = mTraceCpuFreqBtn.getSelection();
        mOptions.mTraceCpuIdle = mTraceCpuIdleBtn.getSelection();
        mOptions.mTraceCpuLoad = mTraceCpuLoadBtn.getSelection();
        mOptions.mTraceDiskIo = mTraceDiskIoBtn.getSelection();
        mOptions.mTraceKernelWorkqueues = mTraceKernelWorkqueuesBtn.getSelection();
        mOptions.mTraceCpuScheduler = mTraceCpuSchedulerBtn.getSelection();

        if (mGfxTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_GFX);
        if (mInputTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_INPUT);
        if (mViewTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_VIEW);
        if (mWebViewTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_WEBVIEW);
        if (mWmTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_WM);
        if (mAmTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_AM);
        if (mSyncTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_SYNC);
        if (mAudioTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_AUDIO);
        if (mViewTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_VIDEO);
        if (mCameraTagBtn.getSelection()) mOptions.enableTag(SystraceOptions.TAG_CAMERA);

        // save current selections to be restored if the dialog is invoked again
        sTraceCpuFreq = mTraceCpuFreqBtn.getSelection();
        sTraceCpuIdle = mTraceCpuIdleBtn.getSelection();
        sTraceCpuLoad = mTraceCpuLoadBtn.getSelection();
        sTraceDiskIo = mTraceDiskIoBtn.getSelection();
        sTraceKernelWorkqueues = mTraceKernelWorkqueuesBtn.getSelection();
        sTraceCpuScheduler = mTraceCpuSchedulerBtn.getSelection();

        sGfxTag = mGfxTagBtn.getSelection();
        sInputTag = mInputTagBtn.getSelection();
        sViewTag = mViewTagBtn.getSelection();
        sWebViewTag = mWebViewTagBtn.getSelection();
        sWmTag = mWmTagBtn.getSelection();
        sAmTag = mAmTagBtn.getSelection();
        sSyncTag = mSyncTagBtn.getSelection();
        sAudioTag = mAudioTagBtn.getSelection();
        sViewTag = mViewTagBtn.getSelection();
        sCameraTag = mCameraTagBtn.getSelection();

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
        private static final int TAG_VIEW = 1 << 3;
        private static final int TAG_WEBVIEW = 1 << 4;
        private static final int TAG_WM = 1 << 5;
        private static final int TAG_AM = 1 << 6;
        private static final int TAG_SYNC = 1 << 7;
        private static final int TAG_AUDIO = 1 << 8;
        private static final int TAG_VIDEO = 1 << 9;
        private static final int TAG_CAMERA = 1 << 10;

        private int mTraceBufferSize;
        private int mTraceDuration;

        private boolean mTraceCpuFreq;
        private boolean mTraceCpuIdle;
        private boolean mTraceCpuLoad;
        private boolean mTraceDiskIo;
        private boolean mTraceKernelWorkqueues;
        private boolean mTraceCpuScheduler;

        private int mTag;

        private void enableTag(int tag) {
            mTag |= tag;
        }

        public String getTraceTag() {
            return mTag == 0 ? null : Integer.toHexString(mTag);
        }

        public String getCommandLineOptions() {
            StringBuilder sb = new StringBuilder(20);

            if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$
            if (mTraceCpuIdle) sb.append("-i "); //$NON-NLS-1$
            if (mTraceCpuLoad) sb.append("-l "); //$NON-NLS-1$
            if (mTraceDiskIo) sb.append("-d ");  //$NON-NLS-1$
            if (mTraceKernelWorkqueues) sb.append("-w "); //$NON-NLS-1$
            if (mTraceCpuScheduler) sb.append("-s "); //$NON-NLS-1$

            if (mTraceDuration > 0) {
                sb.append("-t");    //$NON-NLS-1$
                sb.append(mTraceDuration);
                sb.append(' ');
            }

            if (mTraceBufferSize > 0) {
                sb.append("-b ");	//$NON-NLS-1$
                sb.append(mTraceBufferSize);
                sb.append(' ');
            }

            return sb.toString().trim();
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputReceiver.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputReceiver.java
new file mode 100644
//Synthetic comment -- index 0000000..4659767

//Synthetic comment -- @@ -0,0 +1,58 @@
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

import com.android.ddmlib.IShellOutputReceiver;

import java.io.UnsupportedEncodingException;

public class SystraceOutputReceiver implements IShellOutputReceiver {
    private volatile boolean mCancelled;

    private final SystraceHtmlWriter mWriter;

    public SystraceOutputReceiver(SystraceHtmlWriter writer) {
        mWriter = writer;
    }

    @Override
    public void addOutput(byte[] data, int offset, int length) {
        String s = null;
        try {
            s = new String(data, offset, length, "UTF-8"); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            // normal encoding didn't work, try the default one
            s = new String(data, offset,length);
        }

        mWriter.addAdbOutput(s);
    }

    @Override
    public void flush() {
        mWriter.flush();
    }

    @Override
    public boolean isCancelled() {
        return mCancelled;
    }

    public void cancel() {
        mCancelled = true;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 17670e4..18b74ae 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ddmlib.ClientData;
import com.android.ddmlib.ClientData.IHprofDumpHandler;
import com.android.ddmlib.ClientData.MethodProfilingStatus;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncException;
//Synthetic comment -- @@ -41,6 +42,10 @@
import com.android.ide.eclipse.ddms.editors.UiAutomatorViewer;
import com.android.ide.eclipse.ddms.i18n.Messages;
import com.android.ide.eclipse.ddms.preferences.PreferenceInitializer;
import com.android.ide.eclipse.ddms.systrace.SystraceHtmlWriter;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialog;
import com.android.ide.eclipse.ddms.systrace.SystraceOptionsDialog.SystraceOptions;
import com.android.ide.eclipse.ddms.systrace.SystraceOutputReceiver;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;
//Synthetic comment -- @@ -80,6 +85,8 @@
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DeviceView extends ViewPart implements IUiSelectionListener, IClientChangeListener {

//Synthetic comment -- @@ -95,6 +102,7 @@
private Action mResetAdbAction;
private Action mCaptureAction;
private Action mViewUiAutomatorHierarchyAction;
    private Action mSystraceAction;
private Action mUpdateThreadAction;
private Action mUpdateHeapAction;
private Action mGcAction;
//Synthetic comment -- @@ -331,6 +339,18 @@
mViewUiAutomatorHierarchyAction.setImageDescriptor(
DdmsPlugin.getImageDescriptor("icons/uiautomator.png")); //$NON-NLS-1$

        mSystraceAction = new Action("Capture System Wide Trace") {
            @Override
            public void run() {
                launchSystrace(mDeviceList.getSelectedDevice(),
                        DdmsPlugin.getDisplay().getActiveShell());
            }
        };
        mSystraceAction.setToolTipText("Capture system wide trace using Android systrace");
        mSystraceAction.setImageDescriptor(
                DdmsPlugin.getImageDescriptor("icons/systrace.png")); //$NON-NLS-1$
        mSystraceAction.setEnabled(true);

mResetAdbAction = new Action(Messages.DeviceView_Reset_ADB) {
@Override
public void run() {
//Synthetic comment -- @@ -536,6 +556,70 @@
}
};

    private void launchSystrace(final IDevice device, final Shell parentShell) {
        final SystraceOptionsDialog dlg = new SystraceOptionsDialog(parentShell);
        if (dlg.open() != SystraceOptionsDialog.OK) {
            return;
        }

        final SystraceOptions options = dlg.getSystraceOptions();

        // set trace tag if necessary:
        //      adb shell setprop debug.atrace.tags.enableflags <tag>
        String tag = options.getTraceTag();
        if (tag != null) {
            CountDownLatch setTagLatch = new CountDownLatch(1);
            CollectingOutputReceiver receiver = new CollectingOutputReceiver(setTagLatch);
            try {
                String cmd = "setprop debug.atrace.tags.enableflags " + tag;
                device.executeShellCommand(cmd, receiver);
                setTagLatch.await(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                MessageDialog.openError(parentShell,
                        "Systrace",
                        "Unexpected error while setting trace tags: " + e);
                return;
            }

            String shellOutput = receiver.getOutput();
            if (shellOutput.contains("Error type")) {                   //$NON-NLS-1$
                throw new RuntimeException(receiver.getOutput());
            }
        }

        // adb shell atrace <trace-options>
        ProgressMonitorDialog d = new ProgressMonitorDialog(parentShell);
        try {
            d.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                        InterruptedException {
                    SystraceHtmlWriter writer = new SystraceHtmlWriter(
                            new File(dlg.getTraceFilePath()),
                            new File(DdmsPlugin.getToolsFolder(), "systrace"));  //$NON-NLS-1$
                    final SystraceOutputReceiver receiver = new SystraceOutputReceiver(writer);
                    String cmd = "atrace " + options.getCommandLineOptions(); //$NON-NLS-1$
                    try {
                        device.executeShellCommand(cmd, receiver, 0);
                    } catch (Exception e) {
                        throw new InvocationTargetException(e);
                    }

                    if (!writer.traceStarted() || !writer.traceCompleted()) {
                        throw new InvocationTargetException(new RuntimeException(writer.getLog()));
                    }
                }
            });
        } catch (InvocationTargetException e) {
            ErrorDialog.openError(parentShell, "Systrace",
                    "Unable to collect system trace.",
                    new Status(Status.ERROR,
                            DdmsPlugin.PLUGIN_ID,
                            "Unexpected error while collecting system trace.",
                            e.getCause()));
        } catch (InterruptedException ignore) {
        }
    }

@Override
public void setFocus() {
//Synthetic comment -- @@ -635,8 +719,11 @@
}

private void doSelectionChanged(IDevice selectedDevice) {
        boolean validDevice = selectedDevice != null;

        mCaptureAction.setEnabled(validDevice);
        mViewUiAutomatorHierarchyAction.setEnabled(validDevice);
        // mSystraceAction.setEnabled(validDevice);
}

/**
//Synthetic comment -- @@ -663,6 +750,8 @@
menuManager.add(new Separator());
menuManager.add(mViewUiAutomatorHierarchyAction);
menuManager.add(new Separator());
        menuManager.add(mSystraceAction);
        menuManager.add(new Separator());
menuManager.add(mResetAdbAction);

// and then in the toolbar
//Synthetic comment -- @@ -682,6 +771,8 @@
toolBarManager.add(mCaptureAction);
toolBarManager.add(new Separator());
toolBarManager.add(mViewUiAutomatorHierarchyAction);
        toolBarManager.add(new Separator());
        toolBarManager.add(mSystraceAction);
}

@Override







