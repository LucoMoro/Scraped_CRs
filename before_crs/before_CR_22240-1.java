/*SDK Manager command line: support --filter add-on

In no-ui mode, the "update sdk" command was missing
the add-on filter type, e.g.:
$ android update sdk --no-ui --filter add-on

This restores it and adds a unit-test to make sure
the cmd line check is in sync with the array definitions.

SDK Issue:http://code.google.com/p/android/issues/detail?id=15933Change-Id:I7626257c39602908058eb7359b4c98cc3f54eef3*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index bbefa99..0d66845 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkmanager;

import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -32,6 +34,7 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
//Synthetic comment -- @@ -41,6 +44,7 @@
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.swt.widgets.Display;
import org.xml.sax.InputSource;
//Synthetic comment -- @@ -53,6 +57,8 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
//Synthetic comment -- @@ -330,6 +336,7 @@
String proxyHost = mSdkCommandLine.getProxyHost();
String proxyPort = mSdkCommandLine.getProxyPort();

// Check filter types.
ArrayList<String> pkgFilter = new ArrayList<String>();
String filter = mSdkCommandLine.getParamFilter();
//Synthetic comment -- @@ -338,29 +345,75 @@
if (t != null) {
t = t.trim();
if (t.length() > 0) {
                        boolean found = false;
                        for (String t2 : SdkRepoConstants.NODES) {
                            if (t2.equals(t)) {
                                pkgFilter.add(t2);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
errorAndExit(
"Unknown package filter type '%1$s'.\nAccepted values are: %2$s",
t,
                                Arrays.toString(SdkRepoConstants.NODES));
return;
}
}
}
}
}

UpdateNoWindow upd = new UpdateNoWindow(mOsSdkFolder, mSdkManager, mSdkLog,
force, useHttp, proxyHost, proxyPort);
        upd.updateAll(pkgFilter, obsolete, dryMode);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index c4a7a53..d14b6ab 100644

//Synthetic comment -- @@ -23,9 +23,17 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.SdkConstants;

import java.io.File;

import junit.framework.TestCase;

//Synthetic comment -- @@ -122,4 +130,89 @@
+ "]",
mLog.toString());
}
}







