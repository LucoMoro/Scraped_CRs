/*wip: add systrace

Change-Id:I4564421b86ebb384e5bd2b3e2329249abd3771aa*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 17670e4..d58e70c 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -95,6 +96,7 @@
private Action mResetAdbAction;
private Action mCaptureAction;
private Action mViewUiAutomatorHierarchyAction;
    private Action mSystraceAction;
private Action mUpdateThreadAction;
private Action mUpdateHeapAction;
private Action mGcAction;
//Synthetic comment -- @@ -331,6 +333,18 @@
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
//Synthetic comment -- @@ -536,6 +550,12 @@
}
};

    private void launchSystrace(final IDevice device, final Shell parentShell) {
        SystraceOptionsDialog dlg = new SystraceOptionsDialog(parentShell);
        if (dlg.open() != SystraceOptionsDialog.OK) {
            return;
        }
    }

@Override
public void setFocus() {
//Synthetic comment -- @@ -635,8 +655,11 @@
}

private void doSelectionChanged(IDevice selectedDevice) {
        boolean validDevice = selectedDevice != null;

        mCaptureAction.setEnabled(validDevice);
        mViewUiAutomatorHierarchyAction.setEnabled(validDevice);
        // mSystraceAction.setEnabled(validDevice);
}

/**
//Synthetic comment -- @@ -663,6 +686,8 @@
menuManager.add(new Separator());
menuManager.add(mViewUiAutomatorHierarchyAction);
menuManager.add(new Separator());
        menuManager.add(mSystraceAction);
        menuManager.add(new Separator());
menuManager.add(mResetAdbAction);

// and then in the toolbar
//Synthetic comment -- @@ -682,6 +707,8 @@
toolBarManager.add(mCaptureAction);
toolBarManager.add(new Separator());
toolBarManager.add(mViewUiAutomatorHierarchyAction);
        toolBarManager.add(new Separator());
        toolBarManager.add(mSystraceAction);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..cd8507c

//Synthetic comment -- @@ -0,0 +1,347 @@
package com.android.ide.eclipse.ddms.views;

import java.io.File;

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

public class SystraceOptionsDialog extends TitleAreaDialog {
    private static final String TITLE = "Android System Trace";
    private static final String DEFAULT_MESSAGE = "Settings to use while capturing system level trace";
    private static final String DEFAULT_TRACE_FNAME = "trace.html"; //$NON-NLS-1$

    private Text mDestinationText;
    private Text mTraceDurationText;
    private Text mTraceBufferSizeText;

    private static String sSaveToFolder = System.getProperty("user.home"); //$NON-NLS-1$

    private Button mTraceCpuFreqBtn;
    private Button mTraceCpuIdleBtn;
    private Button mTraceCpuLoadBtn;
    private Button mTraceDiskIoBtn;
    private Button mTraceKernelWorkqueuesBtn;
    private Button mTraceCpuSchedulerBtn;

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
        lblTraceDurationseconds.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTraceDurationseconds.setText("Trace duration (seconds): ");

        mTraceDurationText = new Text(c, SWT.BORDER);
        mTraceDurationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        Label lblTraceBufferSize = new Label(c, SWT.NONE);
        lblTraceBufferSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTraceBufferSize.setText("Trace Buffer Size (kb): ");

        mTraceBufferSizeText = new Text(c, SWT.BORDER);
        mTraceBufferSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

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

        mTraceDiskIoBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceDiskIoBtn.setText("Disk I/O (requires root)");

        mTraceCpuIdleBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuIdleBtn.setText("CPU Idle Events");

        mTraceCpuLoadBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuLoadBtn.setText("CPU Load");

        mTraceKernelWorkqueuesBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceKernelWorkqueuesBtn.setText("Kernel Workqueues (requires root)");

        mTraceCpuSchedulerBtn = new Button(grpTraceEvents, SWT.CHECK);
        mTraceCpuSchedulerBtn.setText("CPU Scheduler");

        Group grpTraceTags = new Group(c, SWT.BORDER);
        grpTraceTags.setLayout(new GridLayout(5, false));
        grpTraceTags.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
        grpTraceTags.setText("Trace Tags");

        mGfxTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mGfxTagBtn.setText("gfx");

        mInputTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mInputTagBtn.setText("input");

        mViewTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mViewTagBtn.setText("view");

        mWebViewTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mWebViewTagBtn.setText("webview");

        mWmTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mWmTagBtn.setText("wm");

        mAmTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mAmTagBtn.setText("am");

        mSyncTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mSyncTagBtn.setText("sync");

        mAudioTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mAudioTagBtn.setText("audio");

        mVideoTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mVideoTagBtn.setText("video");

        mCameraTagBtn = new Button(grpTraceTags, SWT.CHECK);
        mCameraTagBtn.setText("camera");

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
        String traceDuration = mTraceDurationText.getText();
        if (!traceDuration.isEmpty()) {
            mOptions.mTraceDuration = Integer.parseInt(traceDuration);
        }

        String traceBufsize = mTraceBufferSizeText.getText();
        if (!traceBufsize.isEmpty()) {
            mOptions.mTraceBufferSize = Integer.parseInt(traceBufsize);
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

        super.okPressed();
    }

    public SystraceOptions getSystraceOptions() {
        return mOptions;
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

            if (mTraceCpuFreq) sb.append("--cpu-freq "); //$NON-NLS-1$
            if (mTraceCpuIdle) sb.append("--cpu-idle "); //$NON-NLS-1$
            if (mTraceCpuLoad) sb.append("--cpu-load "); //$NON-NLS-1$
            if (mTraceDiskIo) sb.append("--disk "); //$NON-NLS-1$
            if (mTraceKernelWorkqueues) sb.append("--workqueue "); //$NON-NLS-1$
            if (!mTraceCpuScheduler) sb.append("--no-cpu-sched "); //$NON-NLS-1$

            if (mTraceBufferSize > 0) {
                sb.append("-b ");	//$NON-NLS-1$
                sb.append(mTraceBufferSize);
                sb.append(' ');
            }

            if (mTraceDuration > 0) {
                sb.append("-t");	//$NON-NLS-1$
                sb.append(mTraceDuration);
                sb.append(' ');
            }

            return sb.toString().trim();
        }
    }
}







