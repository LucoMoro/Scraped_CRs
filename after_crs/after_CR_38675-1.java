/*ADT fix: support Compatibility library in NPW

This fixes the NPW so that it supports the older
"Compatibility" library, not just the new "Support"
library. One issue is that the NPW prompts users to
update, but the code only checks for the support
lib in the new folder (SDK/extras/android/support).
During an upgrade the folder isn't changed and the
code fails to find it at the android/compatibility
location.

SDK Bug: 33859 and duplicates

Change-Id:I2a69d411e3875607859eb42ad2380ba069faf3b3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index 1d84ad2..3fb49b5 100755

//Synthetic comment -- @@ -68,7 +68,7 @@
import java.util.Map;

/**
 * An action to add the android-support-v4.jar support library
* to the selected project.
* <p/>
* This should be used by the GLE. The action itself is currently more
//Synthetic comment -- @@ -78,8 +78,12 @@
*/
public class AddCompatibilityJarAction implements IObjectActionDelegate {

    /** The vendor ID of the support library. */
    private static final String VENDOR_ID = "android";                             //$NON-NLS-1$
    /** The path ID of the support library. */
    private static final String SUPPORT_ID = "support";                            //$NON-NLS-1$
    /** The path ID of the compatibility library (which was its id for releases 1-3). */
    private static final String COMPATIBILITY_ID = "compatibility";                //$NON-NLS-1$
private static final String FD_GRIDLAYOUT = "gridlayout";                      //$NON-NLS-1$
private static final String FD_V7 = "v7";                                      //$NON-NLS-1$
private static final String FD_V4 = "v4";                                      //$NON-NLS-1$
//Synthetic comment -- @@ -120,9 +124,9 @@
}

/**
     * Install the support jar into the given project.
*
     * @param project The Android project to install the support jar into
* @return true if the installation was successful
*/
public static boolean install(final IProject project) {
//Synthetic comment -- @@ -164,9 +168,9 @@

String sdkLocation = sdk.getSdkLocation();
if (minimumRevision > 0) {
            File path = getSupportJarFile();
if (path != null) {
                assert path.exists(); // guaranteed by the getSupportJarFile call
int installedRevision = getInstalledRevision();
if (installedRevision != -1 && minimumRevision <= installedRevision) {
return path;
//Synthetic comment -- @@ -184,14 +188,13 @@
new AdtConsoleSdkLog(),
sdkLocation);

        Pair<Boolean, File> result = window.installExtraPackage(VENDOR_ID, SUPPORT_ID);

// TODO: Make sure the version is at the required level; we know we need at least one
// containing the v7 support

if (!result.getFirst().booleanValue()) {
            AdtPlugin.printErrorToConsole("Failed to install Android Support library");
return null;
}

//Synthetic comment -- @@ -203,7 +206,7 @@
final File jarPath = new File(path, ANDROID_SUPPORT_V4_JAR);

if (!jarPath.isFile()) {
            AdtPlugin.printErrorToConsole("Android Support Jar not found:",
jarPath.getAbsolutePath());
return null;
}
//Synthetic comment -- @@ -212,7 +215,7 @@
}

/**
     * Returns the installed revision number of the Android Support
* library, or -1 if the package is not installed.
*
* @return the installed revision number, or -1
//Synthetic comment -- @@ -223,7 +226,13 @@
String sdkLocation = sdk.getSdkLocation();
SdkManager manager = SdkManager.createManager(sdkLocation, new NullSdkLog());
Map<String, Integer> versions = manager.getExtrasVersions();
            Integer version = versions.get(VENDOR_ID + '/' + SUPPORT_ID);
            if (version == null) {
                // Check the old compatibility library. When the library is updated in-place
                // the manager doesn't change its folder name (since that is a source of
                // endless issues on Windows.)
                version = versions.get(VENDOR_ID + '/' + COMPATIBILITY_ID);
            }
if (version != null) {
return version.intValue();
}
//Synthetic comment -- @@ -235,7 +244,7 @@
/**
* Similar to {@link #install}, but rather than copy a jar into the given
* project, it creates a new library project in the workspace for the
     * support library, and adds a library dependency on the newly
* installed library from the given project.
*
* @param project the project to add a dependency on the library to
//Synthetic comment -- @@ -249,7 +258,7 @@
final IJavaProject javaProject = JavaCore.create(project);
if (javaProject != null) {

            File supportPath = getSupportPackageDir();
if (!supportPath.isDirectory()) {
File path = installSupport(8); // GridLayout arrived in rev 7 and fixed in rev 8
if (path == null) {
//Synthetic comment -- @@ -281,28 +290,53 @@
/**
* Returns the directory containing the support libraries (v4, v7, v13,
* ...), which may or may not exist
     *
     * @return a path to the support library or null
*/
    private static File getSupportPackageDir() {
        final Sdk sdk = Sdk.getCurrent();
        if (sdk != null) {
            String sdkLocation = sdk.getSdkLocation();
            SdkManager manager = SdkManager.createManager(sdkLocation, new NullSdkLog());
            Map<String, Integer> versions = manager.getExtrasVersions();
            Integer version = versions.get(VENDOR_ID + '/' + SUPPORT_ID);
            if (version != null) {
                File supportPath = new File(sdkLocation,
                        SdkConstants.FD_EXTRAS + File.separator
                        + VENDOR_ID + File.separator
                        + SUPPORT_ID);
                return supportPath;
            }

            // Check the old compatibility library. When the library is updated in-place
            // the manager doesn't change its folder name (since that is a source of
            // endless issues on Windows.)
            version = versions.get(VENDOR_ID + '/' + COMPATIBILITY_ID);
            if (version != null) {
                File supportPath = new File(sdkLocation,
                        SdkConstants.FD_EXTRAS + File.separator
                        + VENDOR_ID + File.separator
                        + COMPATIBILITY_ID);
                return supportPath;
            }
        }
        return null;
}

/**
     * Returns a path to the installed jar file for the support library,
* or null if it does not exist
*
     * @return a path to the v4.jar or null
*/
@Nullable
    public static File getSupportJarFile() {
        File supportDir = getSupportPackageDir();
        if (supportDir != null) {
            File path = new File(supportDir, FD_V4 + File.separator + ANDROID_SUPPORT_V4_JAR);
            if (path.exists()) {
                return path;
            }
}

return null;
//Synthetic comment -- @@ -323,7 +357,7 @@
boolean waitForFinish) {

// Install a new library into the workspace. This is a copy rather than
        // a reference to the support library version such that modifications
// do not modify the pristine copy in the SDK install area.

final IProject newProject;
//Synthetic comment -- @@ -390,7 +424,7 @@
// Now add library dependency

// Run an Eclipse asynchronous job to update the project
        Job job = new Job("Add Support Library Dependency to Project") {
@Override
protected IStatus run(IProgressMonitor monitor) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 3e5d23d..7f05f84 100644

//Synthetic comment -- @@ -411,7 +411,7 @@
if (dependencyName.equals(SUPPORT_LIBRARY_NAME)) {
// We assume the revision requirement has been satisfied
// by the wizard
                            File path = AddCompatibilityJarAction.getSupportJarFile();
if (path != null) {
IPath to = getTargetPath(FD_NATIVE_LIBS +'/' + path.getName());
try {







