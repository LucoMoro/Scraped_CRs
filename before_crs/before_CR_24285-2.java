/*SdkManager: pretty-print the 'android' command line help.

This reflows the command line help so that it wraps nicely
at 78 chars. Also properly finish sentences with dots.
Mere details...

Also added a proper unit test to test various edge cases.

Change-Id:Id2f9eab5014ea9c5f4aae5ed4427b9aebc3c70bf*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 50fc496..7e71c77 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;

import java.util.HashMap;
//Synthetic comment -- @@ -108,15 +110,20 @@
mLog = logger;
mActions = actions;

define(Mode.BOOLEAN, false, GLOBAL_FLAG_VERB, NO_VERB_OBJECT, "v", KEY_VERBOSE,
                "Verbose mode: errors, warnings and informational messages are printed.",
                false);
define(Mode.BOOLEAN, false, GLOBAL_FLAG_VERB, NO_VERB_OBJECT, "s", KEY_SILENT,
                "Silent mode: only errors are printed out.",
                false);
define(Mode.BOOLEAN, false, GLOBAL_FLAG_VERB, NO_VERB_OBJECT, "h", KEY_HELP,
                "Help on a specific command.",
                false);
}

/**
//Synthetic comment -- @@ -504,7 +511,7 @@
stdout("\nValid actions are composed of a verb and an optional direct object:");
for (String[] action : mActions) {
if (verb == null || verb.equals(action[ACTION_VERB_INDEX])) {
                    stdout("- %1$6s %2$-14s: %3$s",
action[ACTION_VERB_INDEX],
action[ACTION_OBJECT_INDEX],
action[ACTION_DESC_INDEX]);
//Synthetic comment -- @@ -580,23 +587,20 @@
// width "manually" in the printf format string.
String longArgWidth = Integer.toString(longArgLen + 2);

                // For multi-line descriptions, pad new lines with the necessary whitespace.
                String desc = arg.getDescription();
                desc = desc.replaceAll("\n",                                           //$NON-NLS-1$
                        String.format("\n      %" + longArgWidth + "s", " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

// Print a line in the form " -1_letter_arg --long_arg description"
// where either the 1-letter arg or the long arg are optional.
                stdout("  %1$-2s %2$-" + longArgWidth + "s %3$s%4$s%5$s",              //$NON-NLS-1$
arg.getShortArg().length() > 0 ?
"-" + arg.getShortArg() :                              //$NON-NLS-1$
"",                                                    //$NON-NLS-1$
arg.getLongArg().length() > 0 ?
"--" + arg.getLongArg() :                              //$NON-NLS-1$
"",                                                    //$NON-NLS-1$
                        desc,
value,
required);
numOptions++;
}
}
//Synthetic comment -- @@ -608,6 +612,7 @@

//----

/**
* The mode of an argument specifies the type of variable it represents,
* whether an extra parameter is required after the flag and how to parse it.
//Synthetic comment -- @@ -874,7 +879,9 @@
* @param args Format arguments.
*/
protected void stdout(String format, Object...args) {
        mLog.printf(format + '\n', args);
}

/**
//Synthetic comment -- @@ -887,4 +894,74 @@
protected void stderr(String format, Object...args) {
mLog.error(null, format, args);
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 66e05f1..6dabb84 100644

//Synthetic comment -- @@ -100,6 +100,13 @@
* </ul>
*/
private final static String[][] ACTIONS = {
{ VERB_LIST, NO_VERB_OBJECT,
"Lists existing targets or virtual devices." },
{ VERB_LIST, OBJECT_AVD,
//Synthetic comment -- @@ -146,12 +153,6 @@

{ VERB_UPDATE, OBJECT_SDK,
"Updates the SDK by suggesting new platforms to install if available." },

            { VERB_SDK, NO_VERB_OBJECT,
                "Displays the SDK Manager window." },
            { VERB_AVD, NO_VERB_OBJECT,
                "Displays the AVD Manager window.",
                },
};

public SdkCommandLine(ISdkLog logger) {
//Synthetic comment -- @@ -185,19 +186,19 @@

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
//Synthetic comment -- @@ -209,19 +210,19 @@

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

//Synthetic comment -- @@ -237,7 +238,7 @@

define(Mode.BOOLEAN, false,
VERB_LIST, OBJECT_SDK, "s", KEY_NO_HTTPS,                           //$NON-NLS-1$
                "Uses HTTP instead of HTTPS (the default) for downloads", false);

define(Mode.STRING, false,
VERB_LIST, OBJECT_SDK, "", KEY_PROXY_PORT,                          //$NON-NLS-1$
//Synthetic comment -- @@ -251,7 +252,7 @@

define(Mode.BOOLEAN, false,
VERB_LIST, OBJECT_SDK, "o", KEY_OBSOLETE,                           //$NON-NLS-1$
                "Installs obsolete packages",
false);

// --- update sdk ---
//Synthetic comment -- @@ -262,7 +263,7 @@

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,                         //$NON-NLS-1$
                "Uses HTTP instead of HTTPS (the default) for downloads", false);

define(Mode.STRING, false,
VERB_UPDATE, OBJECT_SDK, "", KEY_PROXY_PORT,                        //$NON-NLS-1$
//Synthetic comment -- @@ -276,23 +277,23 @@

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

// --- create project ---
//Synthetic comment -- @@ -307,47 +308,47 @@
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT,
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
/*
//Synthetic comment -- @@ -355,63 +356,63 @@

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









//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java b/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java
//Synthetic comment -- index 688ce52..02d905e 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
public class CommandLineProcessorTest extends TestCase {

private StdSdkLog mLog;
    
/**
* A mock version of the {@link CommandLineProcessor} class that does not
* exits and captures its stdout/stderr output.
//Synthetic comment -- @@ -35,7 +35,7 @@
private boolean mHelpCalled;
private String mStdOut = "";
private String mStdErr = "";
        
public MockCommandLineProcessor(ISdkLog logger) {
super(logger,
new String[][] {
//Synthetic comment -- @@ -47,45 +47,45 @@
define(Mode.STRING, true /*mandatory*/,
"verb1", "action1", "2", "second", "mandatory flag", null);
}
        
@Override
public void printHelpAndExitForAction(String verb, String directObject,
String errorFormat, Object... args) {
mHelpCalled = true;
super.printHelpAndExitForAction(verb, directObject, errorFormat, args);
}
        
@Override
protected void exit() {
mExitCalled = true;
}
        
@Override
protected void stdout(String format, Object... args) {
String s = String.format(format, args);
mStdOut += s + "\n";
// don't call super to avoid printing stuff
}
        
@Override
protected void stderr(String format, Object... args) {
String s = String.format(format, args);
mStdErr += s + "\n";
// don't call super to avoid printing stuff
}
        
public boolean wasHelpCalled() {
return mHelpCalled;
}
        
public boolean wasExitCalled() {
return mExitCalled;
}
        
public String getStdOut() {
return mStdOut;
}
        
public String getStdErr() {
return mStdErr;
}
//Synthetic comment -- @@ -102,8 +102,8 @@
super.tearDown();
}

    public final void testPrintHelpAndExit() {
        MockCommandLineProcessor c = new MockCommandLineProcessor(mLog);        
assertFalse(c.wasExitCalled());
assertFalse(c.wasHelpCalled());
assertTrue(c.getStdOut().equals(""));
//Synthetic comment -- @@ -114,7 +114,7 @@
assertTrue(c.getStdErr().equals(""));
assertTrue(c.wasExitCalled());

        c = new MockCommandLineProcessor(mLog);        
assertFalse(c.wasExitCalled());
assertTrue(c.getStdOut().equals(""));
assertTrue(c.getStdErr().indexOf("Missing parameter") == -1);
//Synthetic comment -- @@ -124,9 +124,9 @@
assertFalse(c.getStdOut().equals(""));
assertTrue(c.getStdErr().indexOf("Missing parameter") != -1);
}
    
    public final void testVerbose() {
        MockCommandLineProcessor c = new MockCommandLineProcessor(mLog);        

assertFalse(c.isVerbose());
c.parseArgs(new String[] { "-v" });
//Synthetic comment -- @@ -135,31 +135,31 @@
assertTrue(c.wasHelpCalled());
assertTrue(c.getStdErr().indexOf("Missing verb name.") != -1);

        c = new MockCommandLineProcessor(mLog);        
c.parseArgs(new String[] { "--verbose" });
assertTrue(c.isVerbose());
assertTrue(c.wasExitCalled());
assertTrue(c.wasHelpCalled());
assertTrue(c.getStdErr().indexOf("Missing verb name.") != -1);
}
    
    public final void testHelp() {
        MockCommandLineProcessor c = new MockCommandLineProcessor(mLog);        

c.parseArgs(new String[] { "-h" });
assertTrue(c.wasExitCalled());
assertTrue(c.wasHelpCalled());
assertTrue(c.getStdErr().indexOf("Missing verb name.") == -1);

        c = new MockCommandLineProcessor(mLog);        
c.parseArgs(new String[] { "--help" });
assertTrue(c.wasExitCalled());
assertTrue(c.wasHelpCalled());
assertTrue(c.getStdErr().indexOf("Missing verb name.") == -1);
}

    public final void testMandatory() {
        MockCommandLineProcessor c = new MockCommandLineProcessor(mLog);        

c.parseArgs(new String[] { "verb1", "action1", "-1", "value1", "-2", "value2" });
assertFalse(c.wasExitCalled());
//Synthetic comment -- @@ -168,7 +168,7 @@
assertEquals("value1", c.getValue("verb1", "action1", "first"));
assertEquals("value2", c.getValue("verb1", "action1", "second"));

        c = new MockCommandLineProcessor(mLog);        
c.parseArgs(new String[] { "verb1", "action1", "-2", "value2" });
assertFalse(c.wasExitCalled());
assertFalse(c.wasHelpCalled());
//Synthetic comment -- @@ -176,7 +176,7 @@
assertEquals(null, c.getValue("verb1", "action1", "first"));
assertEquals("value2", c.getValue("verb1", "action1", "second"));

        c = new MockCommandLineProcessor(mLog);        
c.parseArgs(new String[] { "verb1", "action1" });
assertTrue(c.wasExitCalled());
assertTrue(c.wasHelpCalled());
//Synthetic comment -- @@ -184,4 +184,50 @@
assertEquals(null, c.getValue("verb1", "action1", "first"));
assertEquals(null, c.getValue("verb1", "action1", "second"));
}
}







