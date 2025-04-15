/*Doc Change: Updated usage help for Android tool

Change-Id:Ie23d54738ba5f20da57431b81aadcc4f21ca85b0*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 9589060..4c30c3b 100644

//Synthetic comment -- @@ -112,19 +112,19 @@
"Updates an Android Virtual Device to match the folders of a new SDK." },

{ VERB_CREATE, OBJECT_PROJECT,
                "Creates a new Android Project." },
{ VERB_UPDATE, OBJECT_PROJECT,
                "Updates an Android Project (must have an AndroidManifest.xml)." },

{ VERB_CREATE, OBJECT_TEST_PROJECT,
                "Creates a new Android Test Project." },
{ VERB_UPDATE, OBJECT_TEST_PROJECT,
                "Updates an Android Test Project (must have an AndroidManifest.xml)." },

{ VERB_CREATE, OBJECT_LIB_PROJECT,
                "Creates a new Android Library Project." },
{ VERB_UPDATE, OBJECT_LIB_PROJECT,
                "Updates an Android Library Project (must have an AndroidManifest.xml)." },
/*
* disabled until the feature is officially supported.
{ VERB_CREATE, OBJECT_EXPORT_PROJECT,
//Synthetic comment -- @@ -148,22 +148,22 @@

define(Mode.STRING, false,
VERB_CREATE, OBJECT_AVD, "p", KEY_PATH,
                "Location path of the directory where the new AVD will be created", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_AVD, "n", KEY_NAME,
"Name of the new AVD", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_AVD, "t", KEY_TARGET_ID,
                "Target id of the new AVD", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_AVD, "s", KEY_SKIN,
                "Skin of the new AVD", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_AVD, "c", KEY_SDCARD,
"Path to a shared SD card image, or size of a new sdcard for the new AVD", null);
define(Mode.BOOLEAN, false,
VERB_CREATE, OBJECT_AVD, "f", KEY_FORCE,
                "Force creation (override an existing AVD)", false);

// --- delete avd ---

//Synthetic comment -- @@ -178,10 +178,10 @@
"Name of the AVD to move or rename", null);
define(Mode.STRING, false,
VERB_MOVE, OBJECT_AVD, "r", KEY_RENAME,
                "New name of the AVD to rename", null);
define(Mode.STRING, false,
VERB_MOVE, OBJECT_AVD, "p", KEY_PATH,
                "New location path of the directory where to move the AVD", null);

// --- update avd ---

//Synthetic comment -- @@ -193,25 +193,25 @@

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "u", KEY_NO_UI,
                "Update from command-line, without any UI", false);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,
                "Use HTTP instead of the default HTTPS for downloads", false);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "f", KEY_FORCE,
                "Force replacing things that have been modified (samples, adb)", false);

define(Mode.STRING, false,
VERB_UPDATE, OBJECT_SDK, "t", KEY_FILTER,
                "A coma-separated list of " + Arrays.toString(SdkRepository.NODES) +
" to limit update to specified types of packages",
null);

define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_SDK, "o", KEY_OBSOLETE,
                "Install obsolete packages",
false);

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -231,16 +231,16 @@
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT,
"p", KEY_PATH,
                "Location path of new project", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT, "t", KEY_TARGET_ID,
                "Target id of the new project", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT, "k", KEY_PACKAGE,
                "Package name", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_PROJECT, "a", KEY_ACTIVITY,
                "Activity name", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_PROJECT, "n", KEY_NAME,
"Project name", null);
//Synthetic comment -- @@ -250,29 +250,29 @@
define(Mode.STRING, true,
VERB_CREATE, OBJECT_TEST_PROJECT,
"p", KEY_PATH,
                "Location path of new project", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_TEST_PROJECT, "n", KEY_NAME,
"Project name", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_TEST_PROJECT, "m", KEY_MAIN_PROJECT,
                "Location path of the project to test, relative to the new project", null);

// --- create lib-project ---

define(Mode.STRING, true,
VERB_CREATE, OBJECT_LIB_PROJECT,
"p", KEY_PATH,
                "Location path of new project", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_LIB_PROJECT, "t", KEY_TARGET_ID,
                "Target id of the new project", null);
define(Mode.STRING, false,
VERB_CREATE, OBJECT_LIB_PROJECT, "n", KEY_NAME,
"Project name", null);
define(Mode.STRING, true,
VERB_CREATE, OBJECT_LIB_PROJECT, "k", KEY_PACKAGE,
                "Package name", null);

// --- create export-project ---
/*
//Synthetic comment -- @@ -294,11 +294,11 @@
define(Mode.STRING, true,
VERB_UPDATE, OBJECT_PROJECT,
"p", KEY_PATH,
                "Location path of the project", null);
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"t", KEY_TARGET_ID,
                "Target id to set for the project", null);
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"n", KEY_NAME,
//Synthetic comment -- @@ -306,33 +306,33 @@
define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_PROJECT,
"s", KEY_SUBPROJECTS,
                "Also update any projects in sub-folders, such as test projects.", false);
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_PROJECT,
"l", KEY_LIBRARY,
                "Location path of an Android Library to add, relative to the main project", null);

// --- update test project ---

define(Mode.STRING, true,
VERB_UPDATE, OBJECT_TEST_PROJECT,
"p", KEY_PATH,
                "Location path of the project", null);
define(Mode.STRING, true,
VERB_UPDATE, OBJECT_TEST_PROJECT,
"m", KEY_MAIN_PROJECT,
                "Location path of the project to test, relative to the new project", null);

// --- update lib project ---

define(Mode.STRING, true,
VERB_UPDATE, OBJECT_LIB_PROJECT,
"p", KEY_PATH,
                "Location path of the project", null);
define(Mode.STRING, false,
VERB_UPDATE, OBJECT_LIB_PROJECT,
"t", KEY_TARGET_ID,
                "Target id to set for the project", null);

// --- update export project ---
/*







