
//<Beginning of snippet n. 0>

new file mode 100644


//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100644


//<End of snippet n. 1>










//<Beginning of snippet n. 2>

similarity index 93%
rename from eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java
rename to eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialogV1.java

package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;

import java.io.File;

public class SystraceOptionsDialog extends TitleAreaDialog {
private static final String TITLE = "Android System Trace";
private static final String DEFAULT_MESSAGE =
"Settings to use while capturing system level trace";

private final SystraceOptions mOptions = new SystraceOptions();

    public SystraceOptionsDialog(Shell parentShell) {
super(parentShell);
}

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
mTag |= tag;
}

        public String getTraceTag() {
return mTag == 0 ? null : Integer.toHexString(mTag);
}

        public String getCommandLineOptions() {
StringBuilder sb = new StringBuilder(20);

if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$

//<End of snippet n. 2>










//<Beginning of snippet n. 3>

new file mode 100644


//<End of snippet n. 3>










//<Beginning of snippet n. 4>

new file mode 100644


//<End of snippet n. 4>










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
boolean COMPRESS_DATA = true;

monitor.setTaskName("Collecting Trace Information");
                    final String atraceOptions = options.getCommandLineOptions()
+ (COMPRESS_DATA ? " -z" : "");
SystraceTask task = new SystraceTask(device, atraceOptions);
Thread t = new Thread(task, "Systrace Output Receiver");

//<End of snippet n. 5>










//<Beginning of snippet n. 6>

new file mode 100644


//<End of snippet n. 6>








