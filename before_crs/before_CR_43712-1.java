/*Add cmd line support to create ui automator projects.

Change-Id:I34ffff7d7828c171444c40e48fc3dcf093391324*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index c4e71b3..8f58612 100644

//Synthetic comment -- @@ -24,17 +24,18 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkManager;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectCreator;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
//Synthetic comment -- @@ -44,8 +45,8 @@
import com.android.sdkuilib.internal.repository.SdkUpdaterNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.AvdManagerWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.utils.ILogger;
import com.android.utils.Pair;
//Synthetic comment -- @@ -275,6 +276,9 @@
} else if (SdkCommandLine.OBJECT_LIB_PROJECT.equals(directObject)) {
createProject(true /*library*/);

}
} else if (SdkCommandLine.VERB_UPDATE.equals(verb)) {
if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
//Synthetic comment -- @@ -691,6 +695,66 @@
}

/**
* Updates an existing Android project based on command-line parameters
* @param library whether the project is a library project.
*/








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 68b1943..6a74c29 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
public static final String OBJECT_TARGETS        = "targets";                   //$NON-NLS-1$
public static final String OBJECT_PROJECT        = "project";                   //$NON-NLS-1$
public static final String OBJECT_TEST_PROJECT   = "test-project";              //$NON-NLS-1$
public static final String OBJECT_LIB_PROJECT    = "lib-project";               //$NON-NLS-1$
public static final String OBJECT_ADB            = "adb";                       //$NON-NLS-1$

//Synthetic comment -- @@ -150,6 +151,9 @@
{ VERB_UPDATE, OBJECT_LIB_PROJECT,
"Updates an Android library project (must already have an AndroidManifest.xml)." },

{ VERB_UPDATE, OBJECT_ADB,
"Updates adb to support the USB devices declared in the SDK add-ons." },

//Synthetic comment -- @@ -363,6 +367,18 @@
"Path to directory of the app under test, relative to the test project directory.",
null);

// --- create lib-project ---

define(Mode.STRING, true,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index fee9472..8a201d2 100644

//Synthetic comment -- @@ -77,7 +77,7 @@
* "ACTIVITY_TESTED_CLASS_NAME".*/
private final static String PH_ACTIVITY_TESTED_CLASS_NAME = "ACTIVITY_TESTED_CLASS_NAME";
/** Project name substitution string used in template files, i.e. "PROJECT_NAME". */
    private final static String PH_PROJECT_NAME = "PROJECT_NAME";
/** Application icon substitution string used in the manifest template */
private final static String PH_ICON = "ICON";
/** Version tag name substitution string used in template files, i.e. "VERSION_TAG". */
//Synthetic comment -- @@ -202,25 +202,25 @@
localProperties.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
localProperties.save();

            // target goes in default properties
            ProjectPropertiesWorkingCopy defaultProperties = ProjectProperties.create(folderPath,
PropertyType.PROJECT);
            defaultProperties.setProperty(ProjectProperties.PROPERTY_TARGET, target.hashString());
if (library) {
                defaultProperties.setProperty(ProjectProperties.PROPERTY_LIBRARY, "true");
}
            defaultProperties.save();

            // create a build.properties file with just the application package
            ProjectPropertiesWorkingCopy buildProperties = ProjectProperties.create(folderPath,
PropertyType.ANT);

if (isTestProject) {
                buildProperties.setProperty(ProjectProperties.PROPERTY_TESTED_PROJECT,
pathToMainProject);
}

            buildProperties.save();

// create the map for place-holders of values to replace in the templates
final HashMap<String, String> keywords = new HashMap<String, String>();
//Synthetic comment -- @@ -1095,7 +1095,7 @@
* @param placeholderMap a map of (place-holder, value) to create the file from the template.
* @throws ProjectCreateException
*/
    private void installTemplate(String templateName, File destFile,
Map<String, String> placeholderMap)
throws ProjectCreateException {
// query the target for its template directory







