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
//Synthetic comment -- index 0000000..87bdf88

//Synthetic comment -- @@ -0,0 +1,9 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/ISystraceOptionsDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..4cc0faa

//Synthetic comment -- @@ -0,0 +1,23 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialogV1.java
similarity index 93%
rename from eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialog.java
rename to eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOptionsDialogV1.java
//Synthetic comment -- index fd26f76..cacedd7 100644

//Synthetic comment -- @@ -1,3 +1,19 @@
package com.android.ide.eclipse.ddms.systrace;

import org.eclipse.jface.dialogs.TitleAreaDialog;
//Synthetic comment -- @@ -19,7 +35,7 @@

import java.io.File;

public class SystraceOptionsDialog extends TitleAreaDialog {
private static final String TITLE = "Android System Trace";
private static final String DEFAULT_MESSAGE =
"Settings to use while capturing system level trace";
//Synthetic comment -- @@ -72,7 +88,7 @@

private final SystraceOptions mOptions = new SystraceOptions();

    public SystraceOptionsDialog(Shell parentShell) {
super(parentShell);
}

//Synthetic comment -- @@ -356,15 +372,17 @@
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
//Synthetic comment -- @@ -393,11 +411,13 @@
mTag |= tag;
}

        public String getTraceTag() {
return mTag == 0 ? null : Integer.toHexString(mTag);
}

        public String getCommandLineOptions() {
StringBuilder sb = new StringBuilder(20);

if (mTraceCpuFreq) sb.append("-f "); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceTag.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceTag.java
new file mode 100644
//Synthetic comment -- index 0000000..0fc03ef

//Synthetic comment -- @@ -0,0 +1,27 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceVersionDetector.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceVersionDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..646a454

//Synthetic comment -- @@ -0,0 +1,100 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index 5f7d0dd..e9cafce 100644

//Synthetic comment -- @@ -42,10 +42,12 @@
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
//Synthetic comment -- @@ -562,16 +564,32 @@
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
//Synthetic comment -- @@ -602,7 +620,7 @@
boolean COMPRESS_DATA = true;

monitor.setTaskName("Collecting Trace Information");
                    final String atraceOptions = options.getCommandLineOptions()
+ (COMPRESS_DATA ? " -z" : "");
SystraceTask task = new SystraceTask(device, atraceOptions);
Thread t = new Thread(task, "Systrace Output Receiver");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialogV2.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/SystraceOptionsDialogV2.java
new file mode 100644
//Synthetic comment -- index 0000000..887e352

//Synthetic comment -- @@ -0,0 +1,304 @@







