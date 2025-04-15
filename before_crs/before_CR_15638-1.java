/*Disable unsupported feature for now.

Change-Id:I2f2af2a0ba6087b99a7d0b9ed3acea62a48991f4*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 906c1ac..9589060 100644

//Synthetic comment -- @@ -125,12 +125,13 @@
"Creates a new Android Library Project." },
{ VERB_UPDATE, OBJECT_LIB_PROJECT,
"Updates an Android Library Project (must have an AndroidManifest.xml)." },

{ VERB_CREATE, OBJECT_EXPORT_PROJECT,
"Creates a new Android Export Project." },
{ VERB_UPDATE, OBJECT_EXPORT_PROJECT,
"Updates an Android Export Project (must have an export.properties)." },

{ VERB_UPDATE, OBJECT_ADB,
"Updates adb to support the USB devices declared in the SDK add-ons." },

//Synthetic comment -- @@ -274,6 +275,8 @@
"Package name", null);

// --- create export-project ---

define(Mode.STRING, true,
VERB_CREATE, OBJECT_EXPORT_PROJECT,
//Synthetic comment -- @@ -285,7 +288,7 @@
define(Mode.STRING, true,
VERB_CREATE, OBJECT_EXPORT_PROJECT, "k", KEY_PACKAGE,
"Package name", null);

// --- update project ---

define(Mode.STRING, true,
//Synthetic comment -- @@ -332,7 +335,8 @@
"Target id to set for the project", null);

// --- update export project ---

define(Mode.STRING, true,
VERB_UPDATE, OBJECT_EXPORT_PROJECT,
"p", KEY_PATH,
//Synthetic comment -- @@ -344,6 +348,7 @@
define(Mode.BOOLEAN, false,
VERB_UPDATE, OBJECT_EXPORT_PROJECT, "f", KEY_FORCE,
"Force replacing the build.xml file", false);
}

@Override







