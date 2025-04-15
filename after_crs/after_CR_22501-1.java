/*Script-compatible output for list avds and list targets.

This adds the following options to the "android"
command line to make it easier to use the outputs
from scripts:

$ android list avds    --compact --null
$ android list targets --compact --null

The short version is '-c -0'.

--compact or -c outputs *justs* the AVD names or the
target identifiers (usable for --target arguments).
with no other information, one per line.

--null or -0 switches EOL from \n to \0, which allows
scripts to process target names with spaces using
xargs -0 or similar.

Change-Id:I18e6ee6b431ed69913a6df37ce34e17ecc721035*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3936286..c05b9b0 100644

//Synthetic comment -- @@ -784,6 +784,18 @@
* Displays the list of available Targets (Platforms and Add-ons)
*/
private void displayTargetList() {

        // Compact output, suitable for scripts.
        if (mSdkCommandLine.getFlagCompact()) {
            char eol = mSdkCommandLine.getFlagEolNull() ? '\0' : '\n';

            for (IAndroidTarget target : mSdkManager.getTargets()) {
                mSdkLog.printf("%1$s%2$c", target.hashString(), eol);
            }

            return;
        }

mSdkLog.printf("Available Android targets:\n");

int index = 1;
//Synthetic comment -- @@ -862,9 +874,23 @@
* @param avdManager
*/
public void displayAvdList(AvdManager avdManager) {

AvdInfo[] avds = avdManager.getValidAvds();

        // Compact output, suitable for scripts.
        if (mSdkCommandLine.getFlagCompact()) {
            char eol = mSdkCommandLine.getFlagEolNull() ? '\0' : '\n';

            for (int index = 0 ; index < avds.length ; index++) {
                AvdInfo info = avds[index];
                mSdkLog.printf("%1$s%2$c", info.getName(), eol);
            }

            return;
        }

        mSdkLog.printf("Available Android Virtual Devices:\n");

for (int index = 0 ; index < avds.length ; index++) {
AvdInfo info = avds[index];
if (index > 0) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 157e896..fb15cb5 100644

//Synthetic comment -- @@ -38,47 +38,49 @@
*   or optional) for the given action.
*/

    public final static String VERB_LIST   = "list";                                //$NON-NLS-1$
    public final static String VERB_CREATE = "create";                              //$NON-NLS-1$
    public final static String VERB_MOVE   = "move";                                //$NON-NLS-1$
    public final static String VERB_DELETE = "delete";                              //$NON-NLS-1$
    public final static String VERB_UPDATE = "update";                              //$NON-NLS-1$

    public static final String OBJECT_SDK            = "sdk";                       //$NON-NLS-1$
    public static final String OBJECT_AVD            = "avd";                       //$NON-NLS-1$
    public static final String OBJECT_AVDS           = "avds";                      //$NON-NLS-1$
    public static final String OBJECT_TARGET         = "target";                    //$NON-NLS-1$
    public static final String OBJECT_TARGETS        = "targets";                   //$NON-NLS-1$
    public static final String OBJECT_PROJECT        = "project";                   //$NON-NLS-1$
    public static final String OBJECT_TEST_PROJECT   = "test-project";              //$NON-NLS-1$
    public static final String OBJECT_LIB_PROJECT    = "lib-project";               //$NON-NLS-1$
    public static final String OBJECT_EXPORT_PROJECT = "export-project";            //$NON-NLS-1$
    public static final String OBJECT_ADB            = "adb";                       //$NON-NLS-1$

    public static final String ARG_ALIAS        = "alias";                          //$NON-NLS-1$
    public static final String ARG_ACTIVITY     = "activity";                       //$NON-NLS-1$

public static final String KEY_ACTIVITY     = ARG_ACTIVITY;
    public static final String KEY_PACKAGE      = "package";                        //$NON-NLS-1$
    public static final String KEY_MODE         = "mode";                           //$NON-NLS-1$
public static final String KEY_TARGET_ID    = OBJECT_TARGET;
    public static final String KEY_NAME         = "name";                           //$NON-NLS-1$
    public static final String KEY_LIBRARY      = "library";                        //$NON-NLS-1$
    public static final String KEY_PATH         = "path";                           //$NON-NLS-1$
    public static final String KEY_FILTER       = "filter";                         //$NON-NLS-1$
    public static final String KEY_SKIN         = "skin";                           //$NON-NLS-1$
    public static final String KEY_SDCARD       = "sdcard";                         //$NON-NLS-1$
    public static final String KEY_FORCE        = "force";                          //$NON-NLS-1$
    public static final String KEY_RENAME       = "rename";                         //$NON-NLS-1$
    public static final String KEY_SUBPROJECTS  = "subprojects";                    //$NON-NLS-1$
    public static final String KEY_MAIN_PROJECT = "main";                           //$NON-NLS-1$
    public static final String KEY_NO_UI        = "no-ui";                          //$NON-NLS-1$
    public static final String KEY_NO_HTTPS     = "no-https";                       //$NON-NLS-1$
    public static final String KEY_PROXY_PORT   = "proxy-port";                     //$NON-NLS-1$
    public static final String KEY_PROXY_HOST   = "proxy-host";                     //$NON-NLS-1$
    public static final String KEY_DRY_MODE     = "dry-mode";                       //$NON-NLS-1$
    public static final String KEY_OBSOLETE     = "obsolete";                       //$NON-NLS-1$
    public static final String KEY_SNAPSHOT     = "snapshot";                       //$NON-NLS-1$
    public static final String KEY_COMPACT      = "compact";                        //$NON-NLS-1$
    public static final String KEY_EOL_NULL     = "null";                           //$NON-NLS-1$

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -149,117 +151,139 @@

// The following defines the parameters of the actions defined in mAction.

        // --- list avds ---

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_AVD, "c", KEY_COMPACT,                            //$NON-NLS-1$
                "Compact output (suitable for scripts)", false);

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_AVD, "0", KEY_EOL_NULL,                           //$NON-NLS-1$
                "Terminates lines with \\0 instead of \\n (e.g. for xargs -0). Only used by --" + KEY_COMPACT + ".",
                false);

        // --- list targets ---

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_TARGET, "c", KEY_COMPACT,                         //$NON-NLS-1$
                "Compact output (suitable for scripts)", false);

        define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_TARGET, "0", KEY_EOL_NULL,                        //$NON-NLS-1$
                "Terminates lines with \\0 instead of \\n (e.g. for xargs -0) Only used by --" + KEY_COMPACT + ".",
                false);

// --- create avd ---

define(Mode.STRING, false,
                VERB_CREATE, OBJECT_AVD, "p", KEY_PATH,                             //$NON-NLS-1$
"Directory where the new AVD will be created", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_AVD, "n", KEY_NAME,                             //$NON-NLS-1$
"Name of the new AVD", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_AVD, "t", KEY_TARGET_ID,                        //$NON-NLS-1$
"Target ID of the new AVD", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_AVD, "s", KEY_SKIN,                             //$NON-NLS-1$
"Skin for the new AVD", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_AVD, "c", KEY_SDCARD,                           //$NON-NLS-1$
"Path to a shared SD card image, or size of a new sdcard for the new AVD", null);
define(Mode.BOOLEAN, false,
                VERB_CREATE, OBJECT_AVD, "f", KEY_FORCE,                            //$NON-NLS-1$
"Forces creation (overwrites an existing AVD)", false);
define(Mode.BOOLEAN, false,
                VERB_CREATE, OBJECT_AVD, "a", KEY_SNAPSHOT,                         //$NON-NLS-1$
"Place a snapshots file in the AVD, to enable persistence.", false);

// --- delete avd ---

define(Mode.STRING, true,
                VERB_DELETE, OBJECT_AVD, "n", KEY_NAME,                             //$NON-NLS-1$
"Name of the AVD to delete", null);

// --- move avd ---

define(Mode.STRING, true,
                VERB_MOVE, OBJECT_AVD, "n", KEY_NAME,                               //$NON-NLS-1$
"Name of the AVD to move or rename", null);
define(Mode.STRING, false,
                VERB_MOVE, OBJECT_AVD, "r", KEY_RENAME,                             //$NON-NLS-1$
"New name of the AVD", null);
define(Mode.STRING, false,
                VERB_MOVE, OBJECT_AVD, "p", KEY_PATH,                               //$NON-NLS-1$
"Path to the AVD's new directory", null);

// --- update avd ---

define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_AVD, "n", KEY_NAME,                             //$NON-NLS-1$
"Name of the AVD to update", null);

// --- list sdk ---

define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "u", KEY_NO_UI,                              //$NON-NLS-1$
"Displays list result on console (no GUI)", true);

define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "s", KEY_NO_HTTPS,                           //$NON-NLS-1$
"Uses HTTP instead of HTTPS (the default) for downloads", false);

define(Mode.STRING, false,
                VERB_LIST, OBJECT_SDK, "", KEY_PROXY_PORT,                          //$NON-NLS-1$
"HTTP/HTTPS proxy port (overrides settings if defined)",
null);

define(Mode.STRING, false,
                VERB_LIST, OBJECT_SDK, "", KEY_PROXY_HOST,                          //$NON-NLS-1$
"HTTP/HTTPS proxy host (overrides settings if defined)",
null);

define(Mode.BOOLEAN, false,
                VERB_LIST, OBJECT_SDK, "o", KEY_OBSOLETE,                           //$NON-NLS-1$
"Installs obsolete packages",
false);

// --- update sdk ---

define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "u", KEY_NO_UI,                            //$NON-NLS-1$
"Updates from command-line (does not display the GUI)", false);

define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,                         //$NON-NLS-1$
"Uses HTTP instead of HTTPS (the default) for downloads", false);

define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "", KEY_PROXY_PORT,                        //$NON-NLS-1$
"HTTP/HTTPS proxy port (overrides settings if defined)",
null);

define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "", KEY_PROXY_HOST,                        //$NON-NLS-1$
"HTTP/HTTPS proxy host (overrides settings if defined)",
null);

define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "f", KEY_FORCE,                            //$NON-NLS-1$
"Forces replacement of a package or its parts, even if something has been modified",
false);

define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "t", KEY_FILTER,                           //$NON-NLS-1$
"A filter that limits the update to the specified types of packages in the form of\n" +
"a comma-separated list of " + Arrays.toString(SdkRepoConstants.NODES),
null);

define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "o", KEY_OBSOLETE,                         //$NON-NLS-1$
"Installs obsolete packages",
false);

define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "n", KEY_DRY_MODE,                         //$NON-NLS-1$
"Simulates the update but does not download or install anything",
false);

//Synthetic comment -- @@ -269,7 +293,7 @@
This currently does not work, the alias build rules need to be fixed.

define(Mode.ENUM, true,
                VERB_CREATE, OBJECT_PROJECT, "m", KEY_MODE,                         //$NON-NLS-1$
"Project mode", new String[] { ARG_ACTIVITY, ARG_ALIAS });
*/
define(Mode.STRING, true,
//Synthetic comment -- @@ -277,46 +301,44 @@
"p", KEY_PATH,
"The new project's directory", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_PROJECT, "t", KEY_TARGET_ID,                    //$NON-NLS-1$
"Target ID of the new project", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_PROJECT, "k", KEY_PACKAGE,                      //$NON-NLS-1$
"Android package name for the application", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_PROJECT, "a", KEY_ACTIVITY,                     //$NON-NLS-1$
"Name of the default Activity that is created", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_PROJECT, "n", KEY_NAME,                         //$NON-NLS-1$
"Project name", null);

// --- create test-project ---

define(Mode.STRING, true,
                VERB_CREATE, OBJECT_TEST_PROJECT, "p", KEY_PATH,                    //$NON-NLS-1$
"The new project's directory", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_TEST_PROJECT, "n", KEY_NAME,                    //$NON-NLS-1$
"Project name", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_TEST_PROJECT, "m", KEY_MAIN_PROJECT,            //$NON-NLS-1$
"Path to directory of the app under test, relative to the test project directory",
null);

// --- create lib-project ---

define(Mode.STRING, true,
                VERB_CREATE, OBJECT_LIB_PROJECT, "p", KEY_PATH,                     //$NON-NLS-1$
"The new project's directory", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,                //$NON-NLS-1$
"Target ID of the new project", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_LIB_PROJECT, "n", KEY_NAME,                     //$NON-NLS-1$
"Project name", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_LIB_PROJECT, "k", KEY_PACKAGE,                  //$NON-NLS-1$
"Android package name for the library", null);

// --- create export-project ---
//Synthetic comment -- @@ -324,75 +346,63 @@
* disabled until the feature is officially supported.

define(Mode.STRING, true,
                VERB_CREATE, OBJECT_EXPORT_PROJECT, "p", KEY_PATH,                  //$NON-NLS-1$
"Location path of new project", null);
define(Mode.STRING, false,
                VERB_CREATE, OBJECT_EXPORT_PROJECT, "n", KEY_NAME,                  //$NON-NLS-1$
"Project name", null);
define(Mode.STRING, true,
                VERB_CREATE, OBJECT_EXPORT_PROJECT, "k", KEY_PACKAGE,               //$NON-NLS-1$
"Package name", null);
*/
// --- update project ---

define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_PROJECT, "p", KEY_PATH,                         //$NON-NLS-1$
"The project's directory", null);
define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_PROJECT, "t", KEY_TARGET_ID,                    //$NON-NLS-1$
"Target ID to set for the project", null);
define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_PROJECT, "n", KEY_NAME,                         //$NON-NLS-1$
"Project name", null);
define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_PROJECT, "s", KEY_SUBPROJECTS,                  //$NON-NLS-1$
"Also updates any projects in sub-folders, such as test projects.", false);
define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_PROJECT, "l", KEY_LIBRARY,                      //$NON-NLS-1$
"Directory of an Android library to add, relative to this project's directory",
null);

// --- update test project ---

define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_TEST_PROJECT, "p", KEY_PATH,                    //$NON-NLS-1$
"The project's directory", null);
define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_TEST_PROJECT, "m", KEY_MAIN_PROJECT,            //$NON-NLS-1$
"Directory of the app under test, relative to the test project directory", null);

// --- update lib project ---

define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_LIB_PROJECT, "p", KEY_PATH,                     //$NON-NLS-1$
"The project's directory", null);
define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,                //$NON-NLS-1$
"Target ID to set for the project", null);

// --- update export project ---
/*
* disabled until the feature is officially supported.
define(Mode.STRING, true,
                VERB_UPDATE, OBJECT_EXPORT_PROJECT, "p", KEY_PATH,                  //$NON-NLS-1$
"Location path of the project", null);
define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_EXPORT_PROJECT, "n", KEY_NAME,                  //$NON-NLS-1$
"Project name", null);
define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_EXPORT_PROJECT, "f", KEY_FORCE,                 //$NON-NLS-1$
"Force replacing the build.xml file", false);
*/
}
//Synthetic comment -- @@ -527,4 +537,16 @@
public String getParamProxyPort() {
return ((String) getValue(null, null, KEY_PROXY_PORT));
}

    // -- some helpers for list avds and list targets flags

    /** Helper to retrieve the --compact value. */
    public boolean getFlagCompact() {
        return ((Boolean) getValue(null, null, KEY_COMPACT)).booleanValue();
    }

    /** Helper to retrieve the --null value. */
    public boolean getFlagEolNull() {
        return ((Boolean) getValue(null, null, KEY_EOL_NULL)).booleanValue();
    }
}







