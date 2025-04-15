/*Fix nullability annotations

Eclipse 3.8/4.2 requires that any method which overrides another
method with a @NonNull parameter (seehttps://bugs.eclipse.org/bugs/show_bug.cgi?id=381443).

This changeset adds @NonNull on various overriding methods in newly
added code such that Eclispe 4.2 doesn't show errors.

Change-Id:Ice4a4b4dc31ba68c4e0911bb37c15da090076a0d*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/GetTargetTask.java b/anttasks/src/com/android/ant/GetTargetTask.java
//Synthetic comment -- index 426b01b..d79ffee 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ant;

import com.android.SdkConstants;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
//Synthetic comment -- @@ -129,17 +130,17 @@
}

@Override
            public void info(String msgFormat, Object... args) {
messages.add(String.format(msgFormat, args));
}

@Override
            public void verbose(String msgFormat, Object... args) {
info(msgFormat, args);
}

@Override
            public void warning(String warningFormat, Object... args) {
messages.add(String.format("Warning: " + warningFormat, args));
}
});








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/GetUiTargetTask.java b/anttasks/src/com/android/ant/GetUiTargetTask.java
//Synthetic comment -- index 5c34fa1..e45d2bc 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ant;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.SdkManager;
//Synthetic comment -- @@ -78,17 +79,17 @@
}

@Override
            public void info(String msgFormat, Object... args) {
messages.add(String.format(msgFormat, args));
}

@Override
            public void verbose(String msgFormat, Object... args) {
info(msgFormat, args);
}

@Override
            public void warning(String warningFormat, Object... args) {
messages.add(String.format("Warning: " + warningFormat, args));
}
});








//Synthetic comment -- diff --git a/common/src/com/android/utils/ILogger.java b/common/src/com/android/utils/ILogger.java
//Synthetic comment -- index df3a636..7df5d10 100644

//Synthetic comment -- @@ -72,6 +72,6 @@
* @param msgFormat is a string format to be used with a {@link Formatter}. Cannot be null.
* @param args provides the arguments for msgFormat.
*/
    void verbose(String msgFormat, Object... args);

}








//Synthetic comment -- diff --git a/common/src/com/android/utils/NullLogger.java b/common/src/com/android/utils/NullLogger.java
//Synthetic comment -- index 77f1ad5..8b1a3d9 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.utils;

/**
* Dummy implementation of an {@link ILogger}.
* <p/>
//Synthetic comment -- @@ -35,17 +37,17 @@
}

@Override
    public void warning(String warningFormat, Object... args) {
// ignore
}

@Override
    public void info(String msgFormat, Object... args) {
// ignore
}

@Override
    public void verbose(String msgFormat, Object... args) {
// ignore
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index dddffef..ce6ef7c 100644

//Synthetic comment -- @@ -1890,17 +1890,17 @@
}

@Override
    public void info(String format, Object... args) {
log(IStatus.INFO, format, args);
}

@Override
    public void verbose(String format, Object... args) {
log(IStatus.INFO, format, args);
}

@Override
    public void warning(String format, Object... args) {
log(IStatus.WARNING, format, args);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 91bc411..9cae8a4 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
//Synthetic comment -- @@ -264,11 +265,11 @@
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog() {
@Override
                    public void info(String msgFormat, Object... args) {
// Do not show non-error/warning log in Eclipse.
};
@Override
                    public void verbose(String msgFormat, Object... args) {
// Do not show non-error/warning log in Eclipse.
};
},








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 4da8004..d5b1b9a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.SdkConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -830,17 +831,17 @@
ManifestMerger merger = new ManifestMerger(MergerLog.wrapSdkLog(new ILogger() {

@Override
                public void warning(String warningFormat, Object... args) {
AdtPlugin.printToConsole(getProject(), String.format(warningFormat, args));
}

@Override
                public void info(String msgFormat, Object... args) {
AdtPlugin.printToConsole(getProject(), String.format(msgFormat, args));
}

@Override
                public void verbose(String msgFormat, Object... args) {
info(msgFormat, args);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AdtConsoleSdkLog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AdtConsoleSdkLog.java
//Synthetic comment -- index 268fc53..26afebd 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.utils.ILogger;

//Synthetic comment -- @@ -36,7 +37,7 @@
}

@Override
    public void info(String msgFormat, Object... args) {
String msg = String.format(msgFormat, args);
for (String s : msg.split("\n")) {
if (s.trim().length() > 0) {
//Synthetic comment -- @@ -46,12 +47,12 @@
}

@Override
    public void verbose(String msgFormat, Object... args) {
info(msgFormat, args);
}

@Override
    public void warning(String warningFormat, Object... args) {
AdtPlugin.printToConsole(TAG, String.format("Warning: " + warningFormat, args));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 8c2ad4d..29ad9fa 100644

//Synthetic comment -- @@ -245,17 +245,17 @@
}

@Override
                public void warning(String warningFormat, Object... arg) {
logMessages.add(String.format("Warning: " + warningFormat, arg));
}

@Override
                public void info(String msgFormat, Object... arg) {
logMessages.add(String.format(msgFormat, arg));
}

@Override
                public void verbose(String msgFormat, Object... arg) {
info(msgFormat, arg);
}
};








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 91d544f..3b2afc8 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkmanager;

import com.android.SdkConstants;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.io.FileWrapper;
//Synthetic comment -- @@ -139,7 +140,7 @@
}

@Override
            public void warning(String warningFormat, Object... args) {
if (mSdkCommandLine.isVerbose()) {
System.out.printf("Warning: " + warningFormat, args);
if (!warningFormat.endsWith("\n")) {
//Synthetic comment -- @@ -149,12 +150,12 @@
}

@Override
            public void info(String msgFormat, Object... args) {
System.out.printf(msgFormat, args);
}

@Override
            public void verbose(String msgFormat, Object... args) {
System.out.printf(msgFormat, args);
}
};








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java
//Synthetic comment -- index 6464484..e0defb3 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository;

import com.android.utils.ILogger;


//Synthetic comment -- @@ -116,17 +117,17 @@
}

@Override
    public void warning(String warningFormat, Object... args) {
mLog.warning(warningFormat, args);
}

@Override
    public void info(String msgFormat, Object... args) {
mLog.info(msgFormat, args);
}

@Override
    public void verbose(String msgFormat, Object... args) {
mLog.verbose(msgFormat, args);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java
//Synthetic comment -- index cae5921..72e7d93 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository;

/**
* Mock implementation of {@link ITaskMonitor} that simply captures
* the output in local strings. Does not provide any UI and has no
//Synthetic comment -- @@ -102,15 +104,15 @@
}

@Override
    public void info(String msgFormat, Object... args) {
}

@Override
    public void verbose(String msgFormat, Object... args) {
}

@Override
    public void warning(String warningFormat, Object... args) {
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/mock/MockLog.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/mock/MockLog.java
//Synthetic comment -- index e92db51..f2e30d2 100644

//Synthetic comment -- @@ -38,17 +38,17 @@
}

@Override
    public void warning(String format, Object... args) {
add("W ", format, args);
}

@Override
    public void info(String format, Object... args) {
add("P ", format, args);
}

@Override
    public void verbose(String format, Object... args) {
add("V ", format, args);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java
//Synthetic comment -- index b5bd011..5e7143d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
//Synthetic comment -- @@ -267,17 +268,17 @@
}

@Override
        public void warning(String warningFormat, Object... args) {
mSdkLog.warning(warningFormat, args);
}

@Override
        public void info(String msgFormat, Object... args) {
mSdkLog.info(msgFormat, args);
}

@Override
        public void verbose(String msgFormat, Object... args) {
mSdkLog.verbose(msgFormat, args);
}

//Synthetic comment -- @@ -606,17 +607,17 @@
}

@Override
        public void warning(String warningFormat, Object... args) {
mRoot.warning(warningFormat, args);
}

@Override
        public void info(String msgFormat, Object... args) {
mRoot.info(msgFormat, args);
}

@Override
        public void verbose(String msgFormat, Object... args) {
mRoot.verbose(msgFormat, args);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java
//Synthetic comment -- index e77ec13..4d4f3c9 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.tasks;

import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;

//Synthetic comment -- @@ -217,17 +218,17 @@
}

@Override
    public void warning(String warningFormat, Object... arg) {
log("Warning: " + warningFormat, arg);
}

@Override
    public void info(String msgFormat, Object... arg) {
log(msgFormat, arg);
}

@Override
    public void verbose(String msgFormat, Object... arg) {
log(msgFormat, arg);
}

//Synthetic comment -- @@ -351,17 +352,17 @@
}

@Override
        public void warning(String warningFormat, Object... arg) {
mRoot.warning(warningFormat, arg);
}

@Override
        public void info(String msgFormat, Object... arg) {
mRoot.info(msgFormat, arg);
}

@Override
        public void verbose(String msgFormat, Object... arg) {
mRoot.verbose(msgFormat, arg);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/MessageBoxLog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/MessageBoxLog.java
//Synthetic comment -- index 147f7ce..f5b75e0 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.widgets;

import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -74,21 +75,21 @@
}

@Override
    public void warning(String warningFormat, Object... arg) {
if (!mLogErrorsOnly) {
logMessages.add(String.format("Warning: " + warningFormat, arg));
}
}

@Override
    public void info(String msgFormat, Object... arg) {
if (!mLogErrorsOnly) {
logMessages.add(String.format(msgFormat, arg));
}
}

@Override
    public void verbose(String msgFormat, Object... arg) {
if (!mLogErrorsOnly) {
logMessages.add(String.format(msgFormat, arg));
}







