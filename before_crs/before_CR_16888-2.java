/*Doc Change: Updated usage doc for android tool

Change-Id:I7aefc6551d07005050991c29e6f1d06a33747dc5*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 4c30c3b..b0c0e50 100644

//Synthetic comment -- @@ -154,7 +154,7 @@
"Name of the new AVD", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_AVD, "t", KEY_TARGET_ID,
                "Target ID (API level) of the new AVD", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_AVD, "s", KEY_SKIN,
"Skin for the new AVD", null);
//Synthetic comment -- @@ -181,7 +181,7 @@
"New name of the AVD", null);
define(Mode.STRING, false,
VERB_MOVE, OBJECT_AVD, "p", KEY_PATH,
                "New path to the directory for the AVD", null);

// --- update avd ---

//Synthetic comment -- @@ -193,20 +193,21 @@

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "u", KEY_NO_UI,
                "Updates from command-line, without any UI", false);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,
                "Uses HTTP instead of the default HTTPS for downloads", false);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "f", KEY_FORCE,
                "Forces replacing things that have been modified (samples, adb)", false);

define(Mode.STRING, false,
VERB_UPDATE, OBJECT_SDK, "t", KEY_FILTER,
                "Comma-separated list of " + Arrays.toString(SdkRepository.NODES) +
                " to limit update to specified types of packages",
null);

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -216,7 +217,7 @@

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "n", KEY_DRY_MODE,
                "Only simulates what would be updated but does not download/install anything",
false);

// --- create project ---
//Synthetic comment -- @@ -234,7 +235,7 @@
"The new project's directory", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT, "t", KEY_TARGET_ID,
                "Target ID (API level) of the new project", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT, "k", KEY_PACKAGE,
"Android package name for the application", null);
//Synthetic comment -- @@ -266,7 +267,7 @@
"The new project's directory", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,
                "Target ID (API level) of the new project", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_LIB_PROJECT, "n", KEY_NAME,
"Project name", null);
//Synthetic comment -- @@ -298,7 +299,7 @@
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"t", KEY_TARGET_ID,
                "Target ID (API leve) to set for the project", null);
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"n", KEY_NAME,
//Synthetic comment -- @@ -321,7 +322,7 @@
define(Mode.STRING, true,
VERB_UPDATE, OBJECT_TEST_PROJECT,
"m", KEY_MAIN_PROJECT,
                "Path to directory of the app under test, relative to the test project directory", null);

// --- update lib project ---

//Synthetic comment -- @@ -332,7 +333,7 @@
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_LIB_PROJECT,
"t", KEY_TARGET_ID,
                "Target ID (API level) to set for the project", null);

// --- update export project ---
/*







