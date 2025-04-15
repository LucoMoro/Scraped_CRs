/*SDK Manager command line install improvements.

1- In no-ui mode, the "update sdk" command was missing
the add-on filter type, e.g.:
$ android update sdk --no-ui --filter add-on

This restores it and adds a unit-test to make sure
the cmd line check is in sync with the array definitions.

2- Adds a new command line option "list sdk" to list
all possible packages that can be found and updated from
the remote sites. The list has indexes which can then
be used with the "update sdk --filter" option to pick
specific packages to install.

Example:

$ android list sdk
Packages available for install: 7
   1- Android SDK Tools, revision 10
   2- Documentation for Android SDK, API 11, revision 1
...
$ android update sdk --no-ui --filter doc,3,7

This will install "all doc packages" as well as the
packages 3 and 7 mentioned in the "list sdk" output.

3- Changed the "--obsolete" flag to list not only obsolete
packages but actually *all* packages. This works around the
issue that the SDK Manager tries, by default, to only show
unknown platforms that are higher than whatever is currently
installed, so there was no way with the UI to install an
older platform (e.g. if you had API 11 installed, it would
not list APIS 3-10 for new installation.)
I'll revisit this behavior later.

SDK Issue:http://code.google.com/p/android/issues/detail?id=15933Change-Id:I7626257c39602908058eb7359b4c98cc3f54eef3*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 8f5dec4..50fc496 100644

//Synthetic comment -- @@ -47,10 +47,10 @@
*/

/** Internal verb name for internally hidden flags. */
    public final static String GLOBAL_FLAG_VERB = "@@internal@@";   //$NON-NLS-1$

/** String to use when the verb doesn't need any object. */
    public final static String NO_VERB_OBJECT = "";                 //$NON-NLS-1$

/** The global help flag. */
public static final String KEY_HELP = "help";
//Synthetic comment -- @@ -183,7 +183,7 @@
public Object getValue(String verb, String directObject, String longFlagName) {

if (verb != null && directObject != null) {
            String key = verb + '/' + directObject + '/' + longFlagName;
Arg arg = mArguments.get(key);
return arg.getCurrentValue();
}
//Synthetic comment -- @@ -216,7 +216,7 @@
*              argument mode.
*/
protected void setValue(String verb, String directObject, String longFlagName, Object value) {
        String key = verb + '/' + directObject + '/' + longFlagName;
Arg arg = mArguments.get(key);
arg.setCurrentValue(value);
}
//Synthetic comment -- @@ -238,16 +238,16 @@
for (int i = 0; i < n; i++) {
Arg arg = null;
String a = args[i];
                if (a.startsWith("--")) {                                       //$NON-NLS-1$
arg = findLongArg(verb, directObject, a.substring(2));
                } else if (a.startsWith("-")) {                                 //$NON-NLS-1$
arg = findShortArg(verb, directObject, a.substring(1));
}

// No matching argument name found
if (arg == null) {
// Does it looks like a dashed parameter?
                    if (a.startsWith("-")) {                                    //$NON-NLS-1$
if (verb == null || directObject == null) {
// It looks like a dashed parameter and we don't have a a verb/object
// set yet, the parameter was just given too early.
//Synthetic comment -- @@ -330,9 +330,9 @@
String b = args[i];

Arg dummyArg = null;
                        if (b.startsWith("--")) {                                   //$NON-NLS-1$
dummyArg = findLongArg(verb, directObject, b.substring(2));
                        } else if (b.startsWith("-")) {                             //$NON-NLS-1$
dummyArg = findShortArg(verb, directObject, b.substring(1));
}
if (dummyArg != null) {
//Synthetic comment -- @@ -352,7 +352,7 @@
// used to print specific help.
// Setting a non-null error message triggers printing the help, however
// there is no specific error to print.
                            errorMsg = "";                                          //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -392,9 +392,9 @@
arg.getDirectObject().equals(directObject)) {
if (arg.isMandatory() && arg.getCurrentValue() == null) {
if (missing == null) {
                                    missing = "--" + arg.getLongArg();              //$NON-NLS-1$
} else {
                                    missing += ", --" + arg.getLongArg();           //$NON-NLS-1$
plural = true;
}
}
//Synthetic comment -- @@ -432,7 +432,7 @@
if (directObject == null) {
directObject = NO_VERB_OBJECT;
}
        String key = verb + '/' + directObject + '/' + longName;                    //$NON-NLS-1$
return mArguments.get(key);
}

//Synthetic comment -- @@ -497,7 +497,7 @@
"\n" +
"Global options:",
verb == null ? "action" :
                verb + (directObject == null ? "" : " " + directObject));           //$NON-NLS-1$
listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

if (verb == null || directObject == null) {
//Synthetic comment -- @@ -552,8 +552,8 @@
Arg arg = entry.getValue();
if (arg.getVerb().equals(verb) && arg.getDirectObject().equals(directObject)) {

                String value = "";                                              //$NON-NLS-1$
                String required = "";                                           //$NON-NLS-1$
if (arg.isMandatory()) {
required = " [required]";

//Synthetic comment -- @@ -828,7 +828,7 @@
* Internal helper to define a new argument for a give action.
*
* @param mode The {@link Mode} for the argument.
     * @param mandatory The argument is required (never if {@link Mode#BOOLEAN})
* @param verb The verb name. Can be #INTERNAL_VERB.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
* @param shortName The one-letter short argument name. Can be empty but not null.
//Synthetic comment -- @@ -853,7 +853,7 @@
directObject = NO_VERB_OBJECT;
}

        String key = verb + '/' + directObject + '/' + longName;
mArguments.put(key, new Arg(mode, mandatory,
verb, directObject, shortName, longName, description, defaultValue));
}
//Synthetic comment -- @@ -874,7 +874,7 @@
* @param args Format arguments.
*/
protected void stdout(String format, Object...args) {
        mLog.printf(format + '\n', args);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index bbefa99..3936286 100644

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
//Synthetic comment -- @@ -221,6 +227,15 @@
} else if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
displayAvdList();

            } else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                // We don't support a specific GUI for this.
                // If the user forces a gui mode to see this list, simply launch the regular GUI.
                if (!mSdkCommandLine.getFlagNoUI(verb)) {
                    showMainWindow(false /*autoUpdate*/);
                } else {
                    displayRemoteSdkListNoUI();
                }

} else {
displayTargetList();
displayAvdList();
//Synthetic comment -- @@ -260,7 +275,7 @@
updateExportProject();

} else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                if (mSdkCommandLine.getFlagNoUI(verb)) {
updateSdkNoUI();
} else {
showMainWindow(true /*autoUpdate*/);
//Synthetic comment -- @@ -319,6 +334,18 @@
}
}

    private void displayRemoteSdkListNoUI() {
        boolean force = mSdkCommandLine.getFlagForce();
        boolean useHttp = mSdkCommandLine.getFlagNoHttps();
        boolean obsolete = mSdkCommandLine.getFlagObsolete();
        String proxyHost = mSdkCommandLine.getParamProxyHost();
        String proxyPort = mSdkCommandLine.getParamProxyPort();

        UpdateNoWindow upd = new UpdateNoWindow(mOsSdkFolder, mSdkManager, mSdkLog,
                force, useHttp, proxyHost, proxyPort);
        upd.listRemotePackages(obsolete);
    }

/**
* Updates the whole SDK without any UI, just using console output.
*/
//Synthetic comment -- @@ -327,40 +354,76 @@
boolean useHttp = mSdkCommandLine.getFlagNoHttps();
boolean dryMode = mSdkCommandLine.getFlagDryMode();
boolean obsolete = mSdkCommandLine.getFlagObsolete();
        String proxyHost = mSdkCommandLine.getParamProxyHost();
        String proxyPort = mSdkCommandLine.getParamProxyPort();

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
     * <p/>
     * Note that this is a quick sanity check of the --filter parameter *before* we
     * start loading the remote repository sources. Loading the remotes takes a while
     * so it's worth doing a quick sanity check before hand.
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

                if (t.replaceAll("[0-9]+", "").length() == 0) { //$NON-NLS-1$ //$NON-NLS-2$
                    // If the filter argument *only* contains digits, accept it.
                    // It's probably an index for the remote repository list,
                    // which we can't validate yet.
                    pkgFilter.add(t);
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
                       Arrays.toString(filterTypes.toArray())),
                    null);
            }
        }

        return Pair.of(null, pkgFilter);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 5bb6c4e..157e896 100644

//Synthetic comment -- @@ -104,6 +104,8 @@
{ VERB_LIST, OBJECT_TARGET,
"Lists existing targets.",
OBJECT_TARGETS },
            { VERB_LIST, OBJECT_SDK,
                "Lists remote SDK repository." },

{ VERB_CREATE, OBJECT_AVD,
"Creates a new Android Virtual Device." },
//Synthetic comment -- @@ -195,6 +197,31 @@
VERB_UPDATE, OBJECT_AVD, "n", KEY_NAME,
"Name of the AVD to update", null);

        // --- list sdk ---

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "u", KEY_NO_UI,
                "Displays list result on console (no GUI)", true);

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "s", KEY_NO_HTTPS,
                "Uses HTTP instead of HTTPS (the default) for downloads", false);

        define(Mode.STRING, false,
                VERB_LIST, OBJECT_SDK, "", KEY_PROXY_PORT,
                "HTTP/HTTPS proxy port (overrides settings if defined)",
                null);

        define(Mode.STRING, false,
                VERB_LIST, OBJECT_SDK, "", KEY_PROXY_HOST,
                "HTTP/HTTPS proxy host (overrides settings if defined)",
                null);

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "o", KEY_OBSOLETE,
                "Installs obsolete packages",
                false);

// --- update sdk ---

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -273,7 +300,8 @@
"Project name", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_TEST_PROJECT, "m", KEY_MAIN_PROJECT,
                "Path to directory of the app under test, relative to the test project directory",
                null);

// --- create lib-project ---

//Synthetic comment -- @@ -327,7 +355,8 @@
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"l", KEY_LIBRARY,
                "Directory of an Android library to add, relative to this project's directory",
                null);

// --- update test project ---

//Synthetic comment -- @@ -464,9 +493,9 @@

// -- some helpers for update sdk flags

    /** Helper to retrieve the --no-ui flag. */
    public boolean getFlagNoUI(String verb) {
        return ((Boolean) getValue(verb, null, KEY_NO_UI)).booleanValue();
}

/** Helper to retrieve the --no-https flag. */
//Synthetic comment -- @@ -490,12 +519,12 @@
}

/** Helper to retrieve the --proxy-host value. */
    public String getParamProxyHost() {
return ((String) getValue(null, null, KEY_PROXY_HOST));
}

/** Helper to retrieve the --proxy-port value. */
    public String getParamProxyPort() {
return ((String) getValue(null, null, KEY_PROXY_PORT));
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index c4a7a53..3acb01a 100644

//Synthetic comment -- @@ -17,15 +17,20 @@
package com.android.sdkmanager;


import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

//Synthetic comment -- @@ -42,7 +47,8 @@
@Override
public void setUp() throws Exception {
mLog = new MockLog();
        fakeSdkDir = File.createTempFile(
                this.getClass().getSimpleName() + '_' + this.getName(), null);
mFakeSdk = SdkManagerTestUtil.makeFakeSdk(fakeSdkDir);
mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
assertNotNull("sdkManager location was invalid", mSdkManager);
//Synthetic comment -- @@ -57,7 +63,7 @@
SdkManagerTestUtil.deleteDir(mFakeSdk);
}

    public void testDisplayEmptyAvdList() {
Main main = new Main();
main.setLogger(mLog);
mLog.clear();
//Synthetic comment -- @@ -122,4 +128,81 @@
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

        for (int step = 0; step < 3; step++) {
            for (String value : expectedValues) {
                switch(step) {
                // step 0: use value as-is
                case 1:
                    // add some whitespace before and after
                    value = "  " + value + "   ";
                    break;
                case 2:
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
                        String.format("[%1$s]", value.replace(',', ' ').trim()),
                        Arrays.toString(result.getSecond().toArray()));
            }
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index f3fd347..4d5d616 100755

//Synthetic comment -- @@ -161,7 +161,7 @@
// if the file exists, check its checksum & size. Use it if complete
if (tmpFile.exists()) {
if (tmpFile.length() == archive.getSize()) {
                String chksum = "";                             //$NON-NLS-1$
try {
chksum = fileChecksum(archive.getChecksumType().getMessageDigest(),
tmpFile,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 1a8a9f4..a9b7481 100755

//Synthetic comment -- @@ -258,7 +258,7 @@
url = url.replaceAll("https://", "http://");  //$NON-NLS-1$ //$NON-NLS-2$
}

        monitor.setDescription("Fetching URL: %1$s", url);
monitor.incProgress(1);

mFetchError = null;
//Synthetic comment -- @@ -284,7 +284,7 @@
}

if (xml != null) {
            monitor.setDescription(String.format("Validate XML: %1$s", url));

for (int tryOtherUrl = 0; tryOtherUrl < 2; tryOtherUrl++) {
// Explore the XML to find the potential XML schema version
//Synthetic comment -- @@ -432,7 +432,7 @@
monitor.incProgress(1);

if (xml != null) {
            monitor.setDescription(String.format("Parse XML:    %1$s", url));
monitor.incProgress(1);
parsePackages(validatedDoc, validatedUri, monitor);
if (mPackages == null || mPackages.length == 0) {
//Synthetic comment -- @@ -748,7 +748,10 @@

if (p != null) {
packages.add(p);
                            // TODO: change this to a monitor.print() in sdkman2. In between
                            // simply remove this which isn't very useful since it hides
                            // which source is being loaded in the progress dialog.
                            // monitor.setDescription("Found %1$s", p.getShortDescription());
}
} catch (Exception e) {
// Ignore invalid packages








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index 2279b2d..ddf1e2e 100755

//Synthetic comment -- @@ -28,11 +28,6 @@

/**
* Performs an update using only a non-interactive console output with no GUI.
*/
public class UpdateNoWindow {

//Synthetic comment -- @@ -111,6 +106,15 @@
mUpdaterData.updateOrInstallAll_NoGUI(pkgFilter, includeObsoletes, dryMode);
}

    /**
     * Lists remote packages available for install using 'android update sdk --no-ui'.
     *
     * @param includeObsoletes True to also list and install obsolete packages.
     */
    public void listRemotePackages(boolean includeObsoletes) {
        mUpdaterData.listRemotePackages_NoGUI(includeObsoletes);
    }

// -----

/**
//Synthetic comment -- @@ -173,18 +177,18 @@
public void setDescription(String descriptionFormat, Object...args) {

String last = mLastDesc;
            String line = String.format("  " + descriptionFormat, args);            //$NON-NLS-1$

// If the description contains a %, it generally indicates a recurring
// progress so we want a \r at the end.
if (line.indexOf('%') > -1) {
if (mLastProgressBase != null && line.startsWith(mLastProgressBase)) {
                    line = "    " + line.substring(mLastProgressBase.length());     //$NON-NLS-1$
}
                line += '\r';
} else {
mLastProgressBase = line;
                line += '\n';
}

// Skip line if it's the same as the last one.
//Synthetic comment -- @@ -198,10 +202,10 @@
if (last != null &&
last.endsWith("\r") &&
!line.endsWith("\r")) {
                line = '\n' + line;
}

            mSdkLog.printf("%s", line);                                             //$NON-NLS-1$
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 20ceab7..04cc947 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

//Synthetic comment -- @@ -709,6 +710,63 @@
}

/**
     * Fetches all archives available on the known remote sources.
     *
     * Used by {@link UpdaterData#listRemotePackages_NoGUI} and
     * {@link UpdaterData#updateOrInstallAll_NoGUI}.
     *
     * @param includeObsoletes True to also list obsolete packages.
     * @return A list of potential {@link ArchiveInfo} to install.
     */
    private List<ArchiveInfo> getRemoteArchives_NoGUI(boolean includeObsoletes) {
        refreshSources(true);
        loadRemoteAddonsList();

        UpdaterLogic ul = new UpdaterLogic(this);
        List<ArchiveInfo> archives = ul.computeUpdates(
                null /*selectedArchives*/,
                getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

        ul.addNewPlatforms(
                archives,
                getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

        Collections.sort(archives);
        return archives;
    }

    /**
     * Lists remote packages available for install using
     * {@link UpdaterData#updateOrInstallAll_NoGUI}.
     *
     * @param includeObsoletes True to also list obsolete packages.
     */
    public void listRemotePackages_NoGUI(boolean includeObsoletes) {

        List<ArchiveInfo> archives = getRemoteArchives_NoGUI(includeObsoletes);

        mSdkLog.printf("Packages available for installation or update: %1$d\n", archives.size());

        int index = 1;
        for (ArchiveInfo ai : archives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p != null) {
                    mSdkLog.printf("%1$ 4d- %2$s\n",
                            index,
                            p.getShortDescription());
                    index++;
                }
            }
        }
    }

    /**
* Tries to update all the *existing* local packages.
* This version is intended to run without a GUI and
* only outputs to the current {@link ISdkLog}.
//Synthetic comment -- @@ -719,89 +777,40 @@
* @param dryMode True to check what would be updated/installed but do not actually
*   download or install anything.
*/
public void updateOrInstallAll_NoGUI(
Collection<String> pkgFilter,
boolean includeObsoletes,
boolean dryMode) {

        List<ArchiveInfo> archives = getRemoteArchives_NoGUI(includeObsoletes);

// Filter the selected archives to only keep the ones matching the filter
if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
            // Map filter types to an SdkRepository Package type,
            // e.g. create a map "platform" => PlatformPackage.class
HashMap<String, Class<? extends Package>> pkgMap =
new HashMap<String, Class<? extends Package>>();

            mapFilterToPackageClass(pkgMap, SdkRepoConstants.NODES);
            mapFilterToPackageClass(pkgMap, SdkAddonConstants.NODES);

            // Now intersect this with the pkgFilter requested by the user, in order to
            // only keep the classes that the user wants to install.
            // We also create a set with the package indices requested by the user.

            HashSet<Class<? extends Package>> userFilteredClasses =
new HashSet<Class<? extends Package>>();
            SparseIntArray userFilteredIndices = new SparseIntArray();

for (String type : pkgFilter) {
                if (type.replaceAll("[0-9]+", "").length() == 0) { //$NON-NLS-1$ //$NON-NLS-2$
                    // An all-digit number is a package index requested by the user.
                    int index = Integer.parseInt(type);
                    userFilteredIndices.put(index, index);

                } else if (pkgMap.containsKey(type)) {
                    userFilteredClasses.add(pkgMap.get(type));

} else {
// This should not happen unless there's a mismatch in the package map.
mSdkLog.error(null, "Ignoring unknown package filter '%1$s'", type);
//Synthetic comment -- @@ -811,15 +820,24 @@
// we don't need the map anymore
pkgMap = null;

            // Now filter the remote archives list to keep:
            // - any package which class matches userFilteredClasses
            // - any package index which matches userFilteredIndices

            int index = 1;
            for (Iterator<ArchiveInfo> it = archives.iterator(); it.hasNext(); ) {
boolean keep = false;
ArchiveInfo ai = it.next();
Archive a = ai.getNewArchive();
if (a != null) {
Package p = a.getParentPackage();
                    if (p != null) {
                        if (userFilteredClasses.contains(p.getClass()) ||
                                userFilteredIndices.get(index) > 0) {
                            keep = true;
                        }

                        index++;
}
}

//Synthetic comment -- @@ -856,6 +874,52 @@
}
}

    @SuppressWarnings("unchecked")
    private void mapFilterToPackageClass(
            HashMap<String, Class<? extends Package>> inOutPkgMap,
            String[] nodes) {

        // Automatically find the classes matching the node names
        ClassLoader classLoader = getClass().getClassLoader();
        String basePackage = Package.class.getPackage().getName();

        for (String node : nodes) {
            // Capitalize the name
            String name = node.substring(0, 1).toUpperCase() + node.substring(1);

            // We can have one dash at most in a name. If it's present, we'll try
            // with the dash or with the next letter capitalized.
            int dash = name.indexOf('-');
            if (dash > 0) {
                name = name.replaceFirst("-", "");
            }

            for (int alternatives = 0; alternatives < 2; alternatives++) {

                String fqcn = basePackage + '.' + name + "Package";  //$NON-NLS-1$
                try {
                    Class<? extends Package> clazz =
                        (Class<? extends Package>) classLoader.loadClass(fqcn);
                    if (clazz != null) {
                        inOutPkgMap.put(node, clazz);
                        continue;
                    }
                } catch (Throwable ignore) {
                }

                if (alternatives == 0 && dash > 0) {
                    // Try an alternative where the next letter after the dash
                    // is converted to an upper case.
                    name = name.substring(0, dash) +
                           name.substring(dash, dash + 1).toUpperCase() +
                           name.substring(dash + 1);
                } else {
                    break;
                }
            }
        }
    }

/**
* Refresh all sources. This is invoked either internally (reusing an existing monitor)
* or as a UI callback on the remote page "Refresh" button (in which case the monitor is
//Synthetic comment -- @@ -943,7 +1007,7 @@

if (url != null) {
if (getSettingsController().getForceHttp()) {
                url = url.replaceAll("https://", "http://");    //$NON-NLS-1$ //$NON-NLS-2$
}

AddonsListFetcher fetcher = new AddonsListFetcher();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 91afed6..aa2be48 100755

//Synthetic comment -- @@ -120,6 +120,20 @@
/**
* Finds new packages that the user does not have in his/her local SDK
* and adds them to the list of archives to install.
     * <p/>
     * The default is to only find "new" platforms, that is anything more
     * recent than the highest platform currently installed.
     * A side effect is that for an empty SDK install this will list *all*
     * platforms available (since there's no "highest" installed platform.)
     *
     * @param archives The in-out list of archives to install. Typically the
     *  list is not empty at first as it should contain any archives that is
     *  already scheduled for install. This method will add to the list.
     * @param sources The list of all sources, to fetch them as necessary.
     * @param localPkgs The list of all currently installed packages.
     * @param includeObsoletes When true, this will list all platform
     * (included these lower than the highest installed one) as well as
     * all obsolete packages of these platforms.
*/
public void addNewPlatforms(
Collection<ArchiveInfo> archives,
//Synthetic comment -- @@ -136,32 +150,34 @@
float currentAddonScore = 0;
float currentDocScore = 0;
HashMap<String, Float> currentExtraScore = new HashMap<String, Float>();
        if (!includeObsoletes) {
            if (localPkgs != null) {
                for (Package p : localPkgs) {
                    int rev = p.getRevision();
                    int api = 0;
                    boolean isPreview = false;
                    if (p instanceof IPackageVersion) {
                        AndroidVersion vers = ((IPackageVersion) p).getVersion();
                        api = vers.getApiLevel();
                        isPreview = vers.isPreview();
                    }

                    // The score is 10*api + (1 if preview) + rev/100
                    // This allows previews to rank above a non-preview and
                    // allows revisions to rank appropriately.
                    float score = api * 10 + (isPreview ? 1 : 0) + rev/100.f;

                    if (p instanceof PlatformPackage) {
                        currentPlatformScore = Math.max(currentPlatformScore, score);
                    } else if (p instanceof SamplePackage) {
                        currentSampleScore = Math.max(currentSampleScore, score);
                    } else if (p instanceof AddonPackage) {
                        currentAddonScore = Math.max(currentAddonScore, score);
                    } else if (p instanceof ExtraPackage) {
                        currentExtraScore.put(((ExtraPackage) p).getPath(), score);
                    } else if (p instanceof DocPackage) {
                        currentDocScore = Math.max(currentDocScore, score);
                    }
}
}
}







