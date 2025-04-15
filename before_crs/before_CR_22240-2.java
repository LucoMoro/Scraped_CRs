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
$ android update sdk --filter doc,3,7

This will install "all doc packages" as well as the
packages 3 and 7 mentioned in the "list sdk" output.

SDK Issue:http://code.google.com/p/android/issues/detail?id=15933Change-Id:I7626257c39602908058eb7359b4c98cc3f54eef3*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 8f5dec4..50fc496 100644

//Synthetic comment -- @@ -47,10 +47,10 @@
*/

/** Internal verb name for internally hidden flags. */
    public final static String GLOBAL_FLAG_VERB = "@@internal@@";

/** String to use when the verb doesn't need any object. */
    public final static String NO_VERB_OBJECT = "";

/** The global help flag. */
public static final String KEY_HELP = "help";
//Synthetic comment -- @@ -183,7 +183,7 @@
public Object getValue(String verb, String directObject, String longFlagName) {

if (verb != null && directObject != null) {
            String key = verb + "/" + directObject + "/" + longFlagName;
Arg arg = mArguments.get(key);
return arg.getCurrentValue();
}
//Synthetic comment -- @@ -216,7 +216,7 @@
*              argument mode.
*/
protected void setValue(String verb, String directObject, String longFlagName, Object value) {
        String key = verb + "/" + directObject + "/" + longFlagName;
Arg arg = mArguments.get(key);
arg.setCurrentValue(value);
}
//Synthetic comment -- @@ -238,16 +238,16 @@
for (int i = 0; i < n; i++) {
Arg arg = null;
String a = args[i];
                if (a.startsWith("--")) {
arg = findLongArg(verb, directObject, a.substring(2));
                } else if (a.startsWith("-")) {
arg = findShortArg(verb, directObject, a.substring(1));
}

// No matching argument name found
if (arg == null) {
// Does it looks like a dashed parameter?
                    if (a.startsWith("-")) {
if (verb == null || directObject == null) {
// It looks like a dashed parameter and we don't have a a verb/object
// set yet, the parameter was just given too early.
//Synthetic comment -- @@ -330,9 +330,9 @@
String b = args[i];

Arg dummyArg = null;
                        if (b.startsWith("--")) {
dummyArg = findLongArg(verb, directObject, b.substring(2));
                        } else if (b.startsWith("-")) {
dummyArg = findShortArg(verb, directObject, b.substring(1));
}
if (dummyArg != null) {
//Synthetic comment -- @@ -352,7 +352,7 @@
// used to print specific help.
// Setting a non-null error message triggers printing the help, however
// there is no specific error to print.
                            errorMsg = "";
}
}

//Synthetic comment -- @@ -392,9 +392,9 @@
arg.getDirectObject().equals(directObject)) {
if (arg.isMandatory() && arg.getCurrentValue() == null) {
if (missing == null) {
                                    missing = "--" + arg.getLongArg();
} else {
                                    missing += ", --" + arg.getLongArg();
plural = true;
}
}
//Synthetic comment -- @@ -432,7 +432,7 @@
if (directObject == null) {
directObject = NO_VERB_OBJECT;
}
        String key = verb + "/" + directObject + "/" + longName;
return mArguments.get(key);
}

//Synthetic comment -- @@ -497,7 +497,7 @@
"\n" +
"Global options:",
verb == null ? "action" :
                verb + (directObject == null ? "" : " " + directObject));
listOptions(GLOBAL_FLAG_VERB, NO_VERB_OBJECT);

if (verb == null || directObject == null) {
//Synthetic comment -- @@ -552,8 +552,8 @@
Arg arg = entry.getValue();
if (arg.getVerb().equals(verb) && arg.getDirectObject().equals(directObject)) {

                String value = "";
                String required = "";
if (arg.isMandatory()) {
required = " [required]";

//Synthetic comment -- @@ -828,7 +828,7 @@
* Internal helper to define a new argument for a give action.
*
* @param mode The {@link Mode} for the argument.
     * @param mandatory The argument is required (never if {@link Mode.BOOLEAN})
* @param verb The verb name. Can be #INTERNAL_VERB.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
* @param shortName The one-letter short argument name. Can be empty but not null.
//Synthetic comment -- @@ -853,7 +853,7 @@
directObject = NO_VERB_OBJECT;
}

        String key = verb + "/" + directObject + "/" + longName;
mArguments.put(key, new Arg(mode, mandatory,
verb, directObject, shortName, longName, description, defaultValue));
}
//Synthetic comment -- @@ -874,7 +874,7 @@
* @param args Format arguments.
*/
protected void stdout(String format, Object...args) {
        mLog.printf(format + "\n", args);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index bbefa99..3936286 100644

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
//Synthetic comment -- @@ -221,6 +227,15 @@
} else if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
displayAvdList();

} else {
displayTargetList();
displayAvdList();
//Synthetic comment -- @@ -260,7 +275,7 @@
updateExportProject();

} else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                if (mSdkCommandLine.getFlagNoUI()) {
updateSdkNoUI();
} else {
showMainWindow(true /*autoUpdate*/);
//Synthetic comment -- @@ -319,6 +334,18 @@
}
}

/**
* Updates the whole SDK without any UI, just using console output.
*/
//Synthetic comment -- @@ -327,40 +354,76 @@
boolean useHttp = mSdkCommandLine.getFlagNoHttps();
boolean dryMode = mSdkCommandLine.getFlagDryMode();
boolean obsolete = mSdkCommandLine.getFlagObsolete();
        String proxyHost = mSdkCommandLine.getProxyHost();
        String proxyPort = mSdkCommandLine.getProxyPort();

// Check filter types.
        ArrayList<String> pkgFilter = new ArrayList<String>();
        String filter = mSdkCommandLine.getParamFilter();
        if (filter != null && filter.length() > 0) {
            for (String t : filter.split(",")) {    //$NON-NLS-1$
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








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 5bb6c4e..157e896 100644

//Synthetic comment -- @@ -104,6 +104,8 @@
{ VERB_LIST, OBJECT_TARGET,
"Lists existing targets.",
OBJECT_TARGETS },

{ VERB_CREATE, OBJECT_AVD,
"Creates a new Android Virtual Device." },
//Synthetic comment -- @@ -195,6 +197,31 @@
VERB_UPDATE, OBJECT_AVD, "n", KEY_NAME,
"Name of the AVD to update", null);

// --- update sdk ---

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -273,7 +300,8 @@
"Project name", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_TEST_PROJECT, "m", KEY_MAIN_PROJECT,
                "Path to directory of the app under test, relative to the test project directory", null);

// --- create lib-project ---

//Synthetic comment -- @@ -327,7 +355,8 @@
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"l", KEY_LIBRARY,
                "Directory of an Android library to add, relative to this project's directory", null);

// --- update test project ---

//Synthetic comment -- @@ -464,9 +493,9 @@

// -- some helpers for update sdk flags

    /** Helper to retrieve the --force flag. */
    public boolean getFlagNoUI() {
        return ((Boolean) getValue(null, null, KEY_NO_UI)).booleanValue();
}

/** Helper to retrieve the --no-https flag. */
//Synthetic comment -- @@ -490,12 +519,12 @@
}

/** Helper to retrieve the --proxy-host value. */
    public String getProxyHost() {
return ((String) getValue(null, null, KEY_PROXY_HOST));
}

/** Helper to retrieve the --proxy-port value. */
    public String getProxyPort() {
return ((String) getValue(null, null, KEY_PROXY_PORT));
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index c4a7a53..3acb01a 100644

//Synthetic comment -- @@ -17,15 +17,20 @@
package com.android.sdkmanager;


import static java.io.File.createTempFile;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.SdkConstants;

import java.io.File;

import junit.framework.TestCase;

//Synthetic comment -- @@ -42,7 +47,8 @@
@Override
public void setUp() throws Exception {
mLog = new MockLog();
        fakeSdkDir = createTempFile(this.getClass().getSimpleName() + "_" + this.getName(), null);
mFakeSdk = SdkManagerTestUtil.makeFakeSdk(fakeSdkDir);
mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
assertNotNull("sdkManager location was invalid", mSdkManager);
//Synthetic comment -- @@ -57,7 +63,7 @@
SdkManagerTestUtil.deleteDir(mFakeSdk);
}

    public void txestDisplayEmptyAvdList() {
Main main = new Main();
main.setLogger(mLog);
mLog.clear();
//Synthetic comment -- @@ -122,4 +128,81 @@
+ "]",
mLog.toString());
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index f3fd347..4d5d616 100755

//Synthetic comment -- @@ -161,7 +161,7 @@
// if the file exists, check its checksum & size. Use it if complete
if (tmpFile.exists()) {
if (tmpFile.length() == archive.getSize()) {
                String chksum = "";
try {
chksum = fileChecksum(archive.getChecksumType().getMessageDigest(),
tmpFile,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 1a8a9f4..a9b7481 100755

//Synthetic comment -- @@ -258,7 +258,7 @@
url = url.replaceAll("https://", "http://");  //$NON-NLS-1$ //$NON-NLS-2$
}

        monitor.setDescription("Fetching %1$s", url);
monitor.incProgress(1);

mFetchError = null;
//Synthetic comment -- @@ -284,7 +284,7 @@
}

if (xml != null) {
            monitor.setDescription("Validate XML");

for (int tryOtherUrl = 0; tryOtherUrl < 2; tryOtherUrl++) {
// Explore the XML to find the potential XML schema version
//Synthetic comment -- @@ -432,7 +432,7 @@
monitor.incProgress(1);

if (xml != null) {
            monitor.setDescription("Parse XML");
monitor.incProgress(1);
parsePackages(validatedDoc, validatedUri, monitor);
if (mPackages == null || mPackages.length == 0) {
//Synthetic comment -- @@ -748,7 +748,10 @@

if (p != null) {
packages.add(p);
                            monitor.setDescription("Found %1$s", p.getShortDescription());
}
} catch (Exception e) {
// Ignore invalid packages








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
//Synthetic comment -- index 2279b2d..ddf1e2e 100755

//Synthetic comment -- @@ -28,11 +28,6 @@

/**
* Performs an update using only a non-interactive console output with no GUI.
 * <p/>
 * TODO: It may be useful in the future to let the filter specify packages names
 * rather than package types, typically to let the user upgrade to a new platform.
 * This can be achieved easily by simply allowing package names in the pkgFilter
 * argument.
*/
public class UpdateNoWindow {

//Synthetic comment -- @@ -111,6 +106,15 @@
mUpdaterData.updateOrInstallAll_NoGUI(pkgFilter, includeObsoletes, dryMode);
}

// -----

/**
//Synthetic comment -- @@ -173,18 +177,18 @@
public void setDescription(String descriptionFormat, Object...args) {

String last = mLastDesc;
            String line = String.format("  " + descriptionFormat, args);

// If the description contains a %, it generally indicates a recurring
// progress so we want a \r at the end.
if (line.indexOf('%') > -1) {
if (mLastProgressBase != null && line.startsWith(mLastProgressBase)) {
                    line = "    " + line.substring(mLastProgressBase.length());
}
                line += "\r";
} else {
mLastProgressBase = line;
                line += "\n";
}

// Skip line if it's the same as the last one.
//Synthetic comment -- @@ -198,10 +202,10 @@
if (last != null &&
last.endsWith("\r") &&
!line.endsWith("\r")) {
                line = "\n" + line;
}

            mSdkLog.printf("%s", line);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 20ceab7..9985be8 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

//Synthetic comment -- @@ -709,22 +710,15 @@
}

/**
     * Tries to update all the *existing* local packages.
     * This version is intended to run without a GUI and
     * only outputs to the current {@link ISdkLog}.
*
     * @param pkgFilter A list of {@link SdkRepoConstants#NODES} to limit the type of packages
     *   we can update. A null or empty list means to update everything possible.
     * @param includeObsoletes True to also list and install obsolete packages.
     * @param dryMode True to check what would be updated/installed but do not actually
     *   download or install anything.
*/
    @SuppressWarnings("unchecked")
    public void updateOrInstallAll_NoGUI(
            Collection<String> pkgFilter,
            boolean includeObsoletes,
            boolean dryMode) {

refreshSources(true);

UpdaterLogic ul = new UpdaterLogic(this);
//Synthetic comment -- @@ -742,66 +736,81 @@
includeObsoletes);

Collections.sort(archives);

// Filter the selected archives to only keep the ones matching the filter
if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
            // Map filter types to an SdkRepository Package type.
HashMap<String, Class<? extends Package>> pkgMap =
new HashMap<String, Class<? extends Package>>();

            // Automatically find the classes matching the node names
            ClassLoader classLoader = getClass().getClassLoader();
            String basePackage = Package.class.getPackage().getName();
            for (String node : SdkRepoConstants.NODES) {
                // Capitalize the name
                String name = node.substring(0, 1).toUpperCase() + node.substring(1);

                // We can have one dash at most in a name. If it's present, we'll try
                // with the dash or with the next letter capitalized.
                int dash = name.indexOf('-');
                if (dash > 0) {
                    name = name.replaceFirst("-", "");                   //$NON-NLS-1$ //$NON-NLS-2$
                }

                for (int alternatives = 0; alternatives < 2; alternatives++) {

                    String fqcn = basePackage + "." + name + "Package";  //$NON-NLS-1$ //$NON-NLS-2$
                    try {
                        Class<? extends Package> clazz =
                            (Class<? extends Package>) classLoader.loadClass(fqcn);
                        if (clazz != null) {
                            pkgMap.put(node, clazz);
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

            if (SdkRepoConstants.NODES.length != pkgMap.size()) {
                // Sanity check in case we forget to update this node array.
                // We don't cancel the operation though.
                mSdkLog.printf(
                    "*** Filter Mismatch! ***\n" +
                    "*** The package filter list has changed. Please report this.");
            }

            // Now make a set of the types that are allowed by the filter.
            HashSet<Class<? extends Package>> allowedPkgSet =
new HashSet<Class<? extends Package>>();
for (String type : pkgFilter) {
                if (pkgMap.containsKey(type)) {
                    allowedPkgSet.add(pkgMap.get(type));
} else {
// This should not happen unless there's a mismatch in the package map.
mSdkLog.error(null, "Ignoring unknown package filter '%1$s'", type);
//Synthetic comment -- @@ -811,15 +820,24 @@
// we don't need the map anymore
pkgMap = null;

            Iterator<ArchiveInfo> it = archives.iterator();
            while (it.hasNext()) {
boolean keep = false;
ArchiveInfo ai = it.next();
Archive a = ai.getNewArchive();
if (a != null) {
Package p = a.getParentPackage();
                    if (p != null && allowedPkgSet.contains(p.getClass())) {
                        keep = true;
}
}

//Synthetic comment -- @@ -856,6 +874,52 @@
}
}

/**
* Refresh all sources. This is invoked either internally (reusing an existing monitor)
* or as a UI callback on the remote page "Refresh" button (in which case the monitor is
//Synthetic comment -- @@ -931,7 +995,7 @@
u = u.trim();
// This is an URL that comes from the env var. We expect it to either
// end with a / or the canonical name, otherwise we don't use it.
                if (u.endsWith("/")) {                  //$NON-NLS-1$
url = u + SdkAddonsListConstants.URL_DEFAULT_FILENAME;
break;
} else if (u.endsWith(SdkAddonsListConstants.URL_DEFAULT_FILENAME)) {
//Synthetic comment -- @@ -943,7 +1007,7 @@

if (url != null) {
if (getSettingsController().getForceHttp()) {
                url = url.replaceAll("https://", "http://");  //$NON-NLS-1$ //$NON-NLS-2$
}

AddonsListFetcher fetcher = new AddonsListFetcher();







