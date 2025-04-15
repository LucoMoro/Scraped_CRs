/*Project property cleanup.

Remove obsolete sdk-location on project update, don't
use it as backup location anymore (main_rules.xml won't work with
it anyway).

Remove the old application.package properties since older platforms
will use the new rules anyway.

Change-Id:I5a5ec3d1289cf793dd0f98fb778bd84086976c52*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/TaskHelper.java b/anttasks/src/com/android/ant/TaskHelper.java
//Synthetic comment -- index 8c50e49..a360eaf 100644

//Synthetic comment -- @@ -37,18 +37,7 @@

// check if it's valid and exists
if (sdkOsPath == null || sdkOsPath.length() == 0) {
            // LEGACY support: project created with 1.6 or before may be using a different
            // property to declare the location of the SDK. At this point, we cannot
            // yet check which target is running so we check both always.
            sdkOsPath = antProject.getProperty(ProjectProperties.PROPERTY_SDK_LEGACY);
            if (sdkOsPath == null || sdkOsPath.length() == 0) {
                throw new BuildException("SDK Location is not set.");
            }
        }

        // Make sure the OS sdk path ends with a directory separator
        if (sdkOsPath.length() > 0 && !sdkOsPath.endsWith(File.separator)) {
            sdkOsPath += File.separator;
}

File sdk = new File(sdkOsPath);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index e9f65ab..16dd8eb 100644

//Synthetic comment -- @@ -170,7 +170,6 @@
* @param pathToMainProject if non-null the project will be setup to test a main project
* located at the given path.
*/
    @SuppressWarnings("deprecation")
public void createProject(String folderPath, String projectName,
String packageName, String activityEntry, IAndroidTarget target, boolean library,
String pathToMainProject) {
//Synthetic comment -- @@ -205,12 +204,6 @@
ProjectPropertiesWorkingCopy buildProperties = ProjectProperties.create(folderPath,
PropertyType.BUILD);

            // only put application.package for older target where the rules file didn't.
            // grab it through xpath
            if (target.getVersion().getApiLevel() < 4) {
                buildProperties.setProperty(ProjectProperties.PROPERTY_APP_PACKAGE, packageName);
            }

if (isTestProject) {
buildProperties.setProperty(ProjectProperties.PROPERTY_TESTED_PROJECT,
pathToMainProject);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 5870db9..7334e07 100644

//Synthetic comment -- @@ -63,11 +63,8 @@
public final static String PROPERTY_PROGUARD_CONFIG = "proguard.config";

public final static String PROPERTY_SDK = "sdk.dir";
    // LEGACY - compatibility with 1.6 and before
    public final static String PROPERTY_SDK_LEGACY = "sdk-location";

    @Deprecated //This is not needed by the new Ant rules
    public final static String PROPERTY_APP_PACKAGE = "application.package";

public final static String PROPERTY_SPLIT_BY_DENSITY = "split.density";
public final static String PROPERTY_SPLIT_BY_ABI = "split.abi";
//Synthetic comment -- @@ -87,29 +84,39 @@
public static enum PropertyType {
BUILD(SdkConstants.FN_BUILD_PROPERTIES, BUILD_HEADER, new String[] {
PROPERTY_BUILD_SOURCE_DIR, PROPERTY_BUILD_OUT_DIR
            }),
DEFAULT(SdkConstants.FN_DEFAULT_PROPERTIES, DEFAULT_HEADER, new String[] {
PROPERTY_TARGET, PROPERTY_LIBRARY, PROPERTY_LIB_REF_REGEX,
PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS, PROPERTY_PROGUARD_CONFIG
            }),
LOCAL(SdkConstants.FN_LOCAL_PROPERTIES, LOCAL_HEADER, new String[] {
PROPERTY_SDK
            }),
EXPORT(SdkConstants.FN_EXPORT_PROPERTIES, EXPORT_HEADER, new String[] {
PROPERTY_PACKAGE, PROPERTY_VERSIONCODE, PROPERTY_PROJECTS,
PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
            });

private final String mFilename;
private final String mHeader;
private final Set<String> mKnownProps;

        PropertyType(String filename, String header, String[] validProps) {
mFilename = filename;
mHeader = header;
HashSet<String> s = new HashSet<String>();
            s.addAll(Arrays.asList(validProps));
mKnownProps = Collections.unmodifiableSet(s);
}

public String getFilename() {
//Synthetic comment -- @@ -132,6 +139,19 @@

return false;
}
}

private final static String LOCAL_HEADER =








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index e0f713c..23cdd09 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
* To get access to an instance, use {@link ProjectProperties#makeWorkingCopy()} or
* {@link ProjectProperties#create(IAbstractFolder, PropertyType)}.
*/
@SuppressWarnings("deprecation")
public class ProjectPropertiesWorkingCopy extends ProjectProperties {

private final static Map<String, String> COMMENT_MAP = new HashMap<String, String>();
//Synthetic comment -- @@ -56,9 +55,6 @@
"# location of the SDK. This is only used by Ant\n" +
"# For customization when using a Version Control System, please read the\n" +
"# header note.\n");
        COMMENT_MAP.put(PROPERTY_APP_PACKAGE,
                "# The name of your application package as defined in the manifest.\n" +
                "# Used by the 'uninstall' rule.\n");
COMMENT_MAP.put(PROPERTY_PACKAGE,
"# Package of the application being exported\n");
COMMENT_MAP.put(PROPERTY_VERSIONCODE,
//Synthetic comment -- @@ -159,8 +155,10 @@
// record the prop
visitedProps.add(key);

                        // check if the property still exists.
                        if (mProperties.containsKey(key)) {
// put the new value.
value = mProperties.get(key);
} else {







