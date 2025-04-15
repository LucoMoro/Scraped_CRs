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

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -32,6 +34,7 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
//Synthetic comment -- @@ -41,6 +44,7 @@
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.util.Pair;

import org.eclipse.swt.widgets.Display;
import org.xml.sax.InputSource;
//Synthetic comment -- @@ -53,6 +57,8 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
//Synthetic comment -- @@ -330,6 +336,7 @@
String proxyHost = mSdkCommandLine.getProxyHost();
String proxyPort = mSdkCommandLine.getProxyPort();

/*
// Check filter types.
ArrayList<String> pkgFilter = new ArrayList<String>();
String filter = mSdkCommandLine.getParamFilter();
//Synthetic comment -- @@ -338,29 +345,75 @@
if (t != null) {
t = t.trim();
if (t.length() > 0) {
                        if (filterTypes.contains(t)) {
                            pkgFilter.add(t);
                        } else {
errorAndExit(
"Unknown package filter type '%1$s'.\nAccepted values are: %2$s",
t,
                                Arrays.toString(filterTypes.toArray()));
return;
}
}
}
}
}
*/
        // Check filter types.
        Pair<String, ArrayList<String>> filterResult =
            checkFilterValues(mSdkCommandLine.getParamFilter());
        if (filterResult.getFirst() != null) {
            // We got an error.
            errorAndExit(filterResult.getFirst());
        }

UpdateNoWindow upd = new UpdateNoWindow(mOsSdkFolder, mSdkManager, mSdkLog,
force, useHttp, proxyHost, proxyPort);
        upd.updateAll(filterResult.getSecond(), obsolete, dryMode);
    }

    /**
     * Checks the values from the filter parameter and returns a tuple
     * (error , accepted values). Either error is null and accepted values is not,
     * or the reverse.
     *
     * @param filter A comma-separated list of keywords
     * @return A pair <error string, usable values>, only one must be null and the other non-null.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    Pair<String, ArrayList<String>> checkFilterValues(String filter) {
        ArrayList<String> pkgFilter = new ArrayList<String>();

        if (filter != null && filter.length() > 0) {
            // Available types
            Set<String> filterTypes = new TreeSet<String>();
            filterTypes.addAll(Arrays.asList(SdkRepoConstants.NODES));
            filterTypes.addAll(Arrays.asList(SdkAddonConstants.NODES));

            for (String t : filter.split(",")) {    //$NON-NLS-1$
                if (t == null) {
                    continue;
                }
                t = t.trim();
                if (t.length() <= 0) {
                    continue;
                }

                if (filterTypes.contains(t)) {
                    pkgFilter.add(t);
                    continue;
                }

                return Pair.of(
                    String.format(
                       "Unknown package filter type '%1$s'.\nAccepted values are: %2$s",
                       t,
                       Arrays.toString(SdkRepoConstants.NODES)),
                    null);
            }
        }

        return Pair.of(null, pkgFilter);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index c4a7a53..d14b6ab 100644

//Synthetic comment -- @@ -23,9 +23,17 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.SdkConstants;
import com.android.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

//Synthetic comment -- @@ -122,4 +130,89 @@
+ "]",
mLog.toString());
}

    public void testCheckFilterValues() {
        // These are the values we expect checkFilterValues() to match.
        String[] expectedValues = {
                "platform",
                "tool",
                "platform-tool",
                "doc",
                "sample",
                "add-on",
                "extra"
        };

        Set<String> expectedSet = new TreeSet<String>(Arrays.asList(expectedValues));

        // First check the values are actually defined in the proper arrays
        // in the Sdk*Constants.NODES
        for (String node : SdkRepoConstants.NODES) {
            assertTrue(
                String.format(
                    "Error: value '%1$s' from SdkRepoConstants.NODES should be used in unit-test",
                    node),
                expectedSet.contains(node));
        }
        for (String node : SdkAddonConstants.NODES) {
            assertTrue(
                String.format(
                    "Error: value '%1$s' from SdkAddonConstants.NODES should be used in unit-test",
                    node),
                expectedSet.contains(node));
        }

        // Now check none of these values are NOT present in the NODES arrays
        for (String node : SdkRepoConstants.NODES) {
            expectedSet.remove(node);
        }
        for (String node : SdkAddonConstants.NODES) {
            expectedSet.remove(node);
        }
        assertTrue(
            String.format(
                    "Error: values %1$s are missing from Sdk[Repo|Addons]Constants.NODES",
                    Arrays.toString(expectedSet.toArray())),
            expectedSet.isEmpty());

        // We're done with expectedSet now
        expectedSet = null;

        // Finally check that checkFilterValues accepts all these values, one by one.
        Main main = new Main();
        main.setLogger(mLog);

        for (int step = 0; step < 4; step++) {
            for (String value : expectedValues) {
                switch(step) {
                // step 0: use value as-is
                case 1:
                    // add some whitespace before
                    value = " " + value;
                    break;
                case 2:
                    // add some whitespace after
                    value = value + " ";
                    break;
                case 3:
                    // add some whitespace before and after
                    value = "  " + value + "   ";
                    break;
                case 4:
                    // same with some empty arguments that should get ignored
                    value = "  ," + value + " ,  ";
                    break;
                    }

                Pair<String, ArrayList<String>> result = main.checkFilterValues(value);
                assertNull(
                        String.format("Expected error to be null for value '%1$s', got: %2$s",
                                value, result.getFirst()),
                        result.getFirst());
                assertEquals(
                        String.format("[%1$s]", value.trim()),
                        Arrays.toString(result.getSecond().toArray()));
            }
        }
    }
}







