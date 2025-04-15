/*Fix how the default.prop modifications from the UI are reloaded.

The UI to edit the target and library changed the ProjectState
directly, causing havoc on the filelistener which *must* be the
entry point to reload modification into ProjectState.

This patch makes the UI work on a copy of the properties which
are then saved separately from the ProjectState (whose save()
method disappeared).

A next patch will enforce the ProjectProperties of the ProjectState
to be read only so that this does not happen again (forcing
to clone it before modifying/saving it).

This patch also fixes the reloading/relinking of the libraries upon
modification of the default.prop but more work is needed here.

Change-Id:Ie6a23111242005eb91b7533b506c029ba602f0f3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java
//Synthetic comment -- index 5886692..d5cddc1 100644

//Synthetic comment -- @@ -19,15 +19,12 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import java.io.File;
//Synthetic comment -- @@ -177,6 +174,10 @@
private final ArrayList<ProjectState> mParentProjects = new ArrayList<ProjectState>();

public ProjectState(IProject project, ProjectProperties properties) {
mProject = project;
mProperties = properties;

//Synthetic comment -- @@ -224,11 +225,7 @@
return mTarget.hashString();
}

        if (mProperties != null) {
            return mProperties.getProperty(ProjectProperties.PROPERTY_TARGET);
        }

        return null;
}

public IAndroidTarget getTarget() {
//Synthetic comment -- @@ -552,29 +549,15 @@
}

/**
     * Saves the default.properties file and refreshes it to make sure that it gets reloaded
     * by Eclipse
     * @throws Exception
*/
    public void saveProperties() throws CoreException {
        try {
            mProperties.save();

            IResource defaultProp = mProject.findMember(SdkConstants.FN_DEFAULT_PROPERTIES);
            defaultProp.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
        } catch (Exception e) {
            String msg = String.format(
                    "Failed to save %1$s for project %2$s",
                    SdkConstants.FN_DEFAULT_PROPERTIES, mProject.getName());
            AdtPlugin.log(e, msg);
            if (e instanceof CoreException) {
                throw (CoreException)e;
            } else {
                throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, msg, e));
            }
        }
    }

private IStatus replaceLibraryProperty(String oldValue, String newValue) {
int index = 1;
while (true) {
//Synthetic comment -- @@ -586,9 +569,15 @@
}

if (rootPath.equals(oldValue)) {
                mProperties.setProperty(propName, newValue);
try {
                    mProperties.save();
} catch (Exception e) {
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
"Failed to save %1$s for project %2$s",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index 44da3a9..ba69977 100644

//Synthetic comment -- @@ -16,14 +16,18 @@

package com.android.ide.eclipse.adt.internal.properties;

import com.android.ide.eclipse.adt.internal.project.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdkuilib.internal.widgets.SdkTargetSelector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -50,6 +54,7 @@
// APK-SPLIT: This is not yet supported, so we hide the UI
//    private Button mSplitByDensity;
private LibraryProperties mLibraryDependencies;

public AndroidPropertyPage() {
// pass
//Synthetic comment -- @@ -123,22 +128,20 @@
if (currentSdk != null && mProject.isOpen()) {
ProjectState state = Sdk.getProjectState(mProject);

            // simply update the project properties. Eclipse will be notified of the file change
// and will reload it smartly (detecting differences) and updating the ProjectState.
// See Sdk.mFileListener
            ProjectProperties properties = null;
boolean mustSaveProp = false;

IAndroidTarget newTarget = mSelector.getSelected();
if (newTarget != state.getTarget()) {
                properties = state.getProperties();
                properties.setProperty(ProjectProperties.PROPERTY_TARGET, newTarget.hashString());
mustSaveProp = true;
}

if (mIsLibrary.getSelection() != state.isLibrary()) {
                properties = state.getProperties();
                properties.setProperty(ProjectProperties.PROPERTY_LIBRARY,
Boolean.toString(mIsLibrary.getSelection()));
mustSaveProp = true;
}
//Synthetic comment -- @@ -151,9 +154,15 @@

if (mustSaveProp) {
try {
                    state.saveProperties();
                } catch (CoreException e) {
                    // pass
}
}
}
//Synthetic comment -- @@ -171,6 +180,9 @@
if (Sdk.getCurrent() != null && mProject.isOpen()) {
ProjectState state = Sdk.getProjectState(mProject);

// get the target
IAndroidTarget target = state.getTarget();;
if (target != null) {
//Synthetic comment -- @@ -178,7 +190,7 @@
}

mIsLibrary.setSelection(state.isLibrary());
            mLibraryDependencies.setContent(state);

/*
* APK-SPLIT: This is not yet supported, so we hide the UI








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java
//Synthetic comment -- index 67de298..1f5f2c6 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.project.ProjectState.LibraryState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.internal.project.ProjectProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -65,7 +66,16 @@
private Button mDownButton;
private ProjectChooserHelper mProjectChooser;

private ProjectState mState;
private final List<ItemData> mItemDataList = new ArrayList<ItemData>();
private boolean mMustSave = false;

//Synthetic comment -- @@ -231,10 +241,12 @@

/**
* Sets or reset the content.
     * @param state the {@link ProjectState} to display
*/
    void setContent(ProjectState state) {
mState = state;

// reset content
mTable.removeAll();
//Synthetic comment -- @@ -254,10 +266,12 @@
}

/**
     * Saves the state of the UI into the {@link ProjectState} object that was given to
* {@link #setContent(ProjectState)}.
     * <p/>This only saves the data into the {@link ProjectProperties} of the state, but does
     * not update the {@link ProjectState} or the list of {@link LibraryState}.
* @return <code>true</code> if there was actually new data saved in the project state, false
* otherwise.
*/
//Synthetic comment -- @@ -265,18 +279,17 @@
boolean mustSave = mMustSave;
if (mMustSave) {
// remove all previous library dependencies.
            ProjectProperties props = mState.getProperties();
            Set<String> keys = props.keySet();
for (String key : keys) {
if (key.startsWith(ProjectProperties.PROPERTY_LIB_REF)) {
                    props.removeProperty(key);
}
}

// now add the new libraries.
int index = 1;
for (ItemData data : mItemDataList) {
                props.setProperty(ProjectProperties.PROPERTY_LIB_REF + index++,
data.relativePath);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 9423ae7..9db385a 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.StreamException;

//Synthetic comment -- @@ -306,10 +307,10 @@
// check if there's already a state?
ProjectState state = getProjectState(project);

            ProjectProperties properties = null;

if (state != null) {
                properties = state.getProperties();
}

if (properties == null) {
//Synthetic comment -- @@ -890,82 +891,88 @@
public void fileChanged(final IFile file, IMarkerDelta[] markerDeltas, int kind) {
if (SdkConstants.FN_DEFAULT_PROPERTIES.equals(file.getName()) &&
file.getParent() == file.getProject()) {
                // we can't do the change from the Workspace resource change notification
                // so we create build-type job for it.
                Job job = new Job("Project Update") {
                    @Override
                    protected IStatus run(IProgressMonitor monitor) {
                        try {
                            // reload the content of the default.properties file and update
                            // the target.
                            IProject iProject = file.getProject();
                            ProjectState state = Sdk.getProjectState(iProject);

                            // get the current target
                            IAndroidTarget oldTarget = state.getTarget();

                            LibraryDifference diff = state.reloadProperties();

                            // load the (possibly new) target.
                            IAndroidTarget newTarget = loadTarget(state);

                            // reload the libraries if needed
                            if (diff.hasDiff()) {
                                for (LibraryState removedState : diff.removed) {
                                    ProjectState removedPState = removedState.getProjectState();
                                    if (removedPState != null) {
                                        startActionBundle(
                                                new UnlinkLibraryBundle(
                                                        state, removedPState.getProject()));
                                    }
                                }

                                if (diff.added) {
                                    synchronized (sLock) {
                                        for (ProjectState projectState : sProjectStateMap.values()) {
                                            if (projectState != state) {
                                                LibraryState libState = state.needs(projectState);

                                                if (libState != null) {
                                                    IProject[] libArray = new IProject[] {
                                                            libState.getProjectState().getProject()
                                                    };
                                                    LinkLibraryBundle bundle =
                                                            new LinkLibraryBundle();
                                                    bundle.mProject = iProject;
                                                    bundle.mLibraryProjects = libArray;
                                                    bundle.mPreviousLibraryPath = null;
                                                    bundle.mCleanupCPE = false;
                                                    startActionBundle(bundle);
                                                }
}
}
}
}
}

                            // apply the new target if needed.
                            if (newTarget != oldTarget) {
                                IJavaProject javaProject = BaseProjectHelper.getJavaProject(
                                        file.getProject());
                                if (javaProject != null) {
                                    AndroidClasspathContainerInitializer.updateProjects(
                                            new IJavaProject[] { javaProject });
                                }

                                // update the editors to reload with the new target
                                AdtPlugin.getDefault().updateTargetListeners(iProject);
}
                        } catch (CoreException e) {
                            // This can't happen as it's only for closed project (or non existing)
                            // but in that case we can't get a fileChanged on this file.
}

                        return Status.OK_STATUS;
}
                };
                job.setPriority(Job.BUILD); // build jobs are run after other interactive jobs
                job.schedule();
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index bd50e16..d17e3cf 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
//Synthetic comment -- @@ -1096,7 +1097,7 @@
// is tied to the current target, so changing it would invalidate the project we're
// trying to load in the first place.
if (currentTarget == null || !mInfo.isCreateFromSample()) {
            ProjectProperties p = ProjectProperties.create(projectLocation, null);
if (p != null) {
// Check the {build|default}.properties files if present
p.merge(PropertyType.BUILD).merge(PropertyType.DEFAULT);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ApkSettings.java
//Synthetic comment -- index 3b8e85c..511b4ef 100644

//Synthetic comment -- @@ -64,18 +64,18 @@
}

public boolean isSplitByAbi() {
    	return mSplitByAbi;
}

public void setSplitByAbi(boolean split) {
    	mSplitByAbi = split;
}

/**
     * Writes the receiver into a {@link ProjectProperties}.
     * @param properties the {@link ProjectProperties} in which to store the settings.
*/
    public void write(ProjectProperties properties) {
properties.setProperty(ProjectProperties.PROPERTY_SPLIT_BY_DENSITY,
Boolean.toString(mSplitByDensity));
properties.setProperty(ProjectProperties.PROPERTY_SPLIT_BY_ABI,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 4eda46c..df58290 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.AndroidXPathFactory;

//Synthetic comment -- @@ -187,13 +186,13 @@
// first create the project properties.

// location of the SDK goes in localProperty
            ProjectProperties localProperties = ProjectProperties.create(folderPath,
PropertyType.LOCAL);
localProperties.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
localProperties.save();

// target goes in default properties
            ProjectProperties defaultProperties = ProjectProperties.create(folderPath,
PropertyType.DEFAULT);
defaultProperties.setProperty(ProjectProperties.PROPERTY_TARGET, target.hashString());
if (library) {
//Synthetic comment -- @@ -202,7 +201,7 @@
defaultProperties.save();

// create a build.properties file with just the application package
            ProjectProperties buildProperties = ProjectProperties.create(folderPath,
PropertyType.BUILD);

// only put application.package for older target where the rules file didn't.
//Synthetic comment -- @@ -375,13 +374,13 @@

try {
// location of the SDK goes in localProperty
            ProjectProperties localProperties = ProjectProperties.create(folderPath,
PropertyType.LOCAL);
localProperties.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
localProperties.save();

// package name goes in export properties
            ProjectProperties exportProperties = ProjectProperties.create(folderPath,
PropertyType.EXPORT);
exportProperties.setProperty(ProjectProperties.PROPERTY_PACKAGE, packageName);
exportProperties.setProperty(ProjectProperties.PROPERTY_VERSIONCODE, "1");
//Synthetic comment -- @@ -506,21 +505,28 @@

boolean saveDefaultProps = false;

// Update default.prop if --target was specified
if (target != null) {
// we already attempted to load the file earlier, if that failed, create it.
if (props == null) {
                props = ProjectProperties.create(folderPath, PropertyType.DEFAULT);
}

// set or replace the target
            props.setProperty(ProjectProperties.PROPERTY_TARGET, target.hashString());
saveDefaultProps = true;
}

if (libraryPath != null) {
// at this point, the default properties already exists, either because they were
// already there or because they were created with a new target

// check the reference is valid
File libProject = new File(libraryPath);
//Synthetic comment -- @@ -558,14 +564,14 @@
}

String propName = ProjectProperties.PROPERTY_LIB_REF + Integer.toString(index);
            props.setProperty(propName, libraryPath);
saveDefaultProps = true;
}

// save the default props if needed.
if (saveDefaultProps) {
try {
                props.save();
println("Updated %1$s", PropertyType.DEFAULT.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -580,13 +586,15 @@
// we first try to load it.
props = ProjectProperties.load(folderPath, PropertyType.LOCAL);
if (props == null) {
            props = ProjectProperties.create(folderPath, PropertyType.LOCAL);
}

// set or replace the sdk location.
        props.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
try {
            props.save();
println("Updated %1$s", PropertyType.LOCAL.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -759,14 +767,17 @@

// add the test project specific properties.
ProjectProperties buildProps = ProjectProperties.load(folderPath, PropertyType.BUILD);
if (buildProps == null) {
            buildProps = ProjectProperties.create(folderPath, PropertyType.BUILD);
}

// set or replace the path to the main project
        buildProps.setProperty(ProjectProperties.PROPERTY_TESTED_PROJECT, pathToMainProject);
try {
            buildProps.save();
println("Updated %1$s", PropertyType.BUILD.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -806,14 +817,17 @@
// because the file may already exist and contain other values (like apk config),
// we first try to load it.
ProjectProperties props = ProjectProperties.load(folderPath, PropertyType.LOCAL);
if (props == null) {
            props = ProjectProperties.create(folderPath, PropertyType.LOCAL);
}

// set or replace the sdk location.
        props.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
try {
            props.save();
println("Updated %1$s", PropertyType.LOCAL.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 40b055a..4aea001 100644

//Synthetic comment -- @@ -25,29 +25,34 @@
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to load and save project properties for both ADT and Ant-based build.
*
*/
public final class ProjectProperties {
    private final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

/** The property name for the project target */
//Synthetic comment -- @@ -55,6 +60,7 @@

public final static String PROPERTY_LIBRARY = "android.library";
public final static String PROPERTY_LIB_REF = "android.library.reference.";

public final static String PROPERTY_SDK = "sdk.dir";
// LEGACY - compatibility with 1.6 and before
//Synthetic comment -- @@ -83,7 +89,7 @@
PROPERTY_BUILD_SOURCE_DIR, PROPERTY_BUILD_OUT_DIR
}),
DEFAULT(SdkConstants.FN_DEFAULT_PROPERTIES, DEFAULT_HEADER, new String[] {
                PROPERTY_TARGET, PROPERTY_LIBRARY, PROPERTY_LIB_REF,
PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
}),
LOCAL(SdkConstants.FN_LOCAL_PROPERTIES, LOCAL_HEADER, new String[] {
//Synthetic comment -- @@ -96,26 +102,35 @@

private final String mFilename;
private final String mHeader;
        private final Set<String> mValidProps;

PropertyType(String filename, String header, String[] validProps) {
mFilename = filename;
mHeader = header;
HashSet<String> s = new HashSet<String>();
s.addAll(Arrays.asList(validProps));
            mValidProps = Collections.unmodifiableSet(s);
}

public String getFilename() {
return mFilename;
}

/**
         * Returns an unmodifyable {@link Set} of the known properties for that type of
         * property file.
*/
        public Set<String> getValidProps() {
            return mValidProps;
}
}

//Synthetic comment -- @@ -178,33 +193,9 @@
"# The password will be asked during the build when you use the 'release' target.\n" +
"\n";

    private final static Map<String, String> COMMENT_MAP = new HashMap<String, String>();
    static {
//               1-------10--------20--------30--------40--------50--------60--------70--------80
        COMMENT_MAP.put(PROPERTY_TARGET,
                "# Project target.\n");
        COMMENT_MAP.put(PROPERTY_SPLIT_BY_DENSITY,
                "# Indicates whether an apk should be generated for each density.\n");
        COMMENT_MAP.put(PROPERTY_SDK,
                "# location of the SDK. This is only used by Ant\n" +
                "# For customization when using a Version Control System, please read the\n" +
                "# header note.\n");
        COMMENT_MAP.put(PROPERTY_APP_PACKAGE,
                "# The name of your application package as defined in the manifest.\n" +
                "# Used by the 'uninstall' rule.\n");
        COMMENT_MAP.put(PROPERTY_PACKAGE,
                "# Package of the application being exported\n");
        COMMENT_MAP.put(PROPERTY_VERSIONCODE,
                "# Major version code\n");
        COMMENT_MAP.put(PROPERTY_PROJECTS,
                "# List of the Android projects being used for the export.\n" +
                "# The list is made of paths that are relative to this project,\n" +
                "# using forward-slash (/) as separator, and are separated by colons (:).\n");
    }

    private final IAbstractFolder mProjectFolder;
    private final Map<String, String> mProperties;
    private final PropertyType mType;

/**
* Loads a project properties file and return a {@link ProjectProperties} object
//Synthetic comment -- @@ -239,48 +230,13 @@
}

/**
     * Merges all properties from the given file into the current properties.
     * <p/>
     * This emulates the Ant behavior: existing properties are <em>not</em> overriden.
     * Only new undefined properties become defined.
     * <p/>
     * Typical usage:
     * <ul>
     * <li>Create a ProjectProperties with {@link PropertyType#BUILD}
     * <li>Merge in values using {@link PropertyType#DEFAULT}
     * <li>The result is that this contains all the properties from default plus those
     *     overridden by the build.properties file.
     * </ul>
     *
     * @param type One the possible {@link PropertyType}s.
     * @return this object, for chaining.
     */
    public synchronized ProjectProperties merge(PropertyType type) {
        if (mProjectFolder.exists()) {
            IAbstractFile propFile = mProjectFolder.getFile(type.mFilename);
            if (propFile.exists()) {
                Map<String, String> map = parsePropertyFile(propFile, null /* log */);
                if (map != null) {
                    for(Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (!mProperties.containsKey(key) && value != null) {
                            mProperties.put(key, value);
                        }
                    }
                }
            }
        }
        return this;
    }

    /**
* Creates a new project properties object, with no properties.
     * <p/>The file is not created until {@link #save()} is called.
* @param projectFolderOsPath the project folder.
* @param type the type of property file to create
*/
    public static ProjectProperties create(String projectFolderOsPath, PropertyType type) {
// create and return a ProjectProperties with an empty map.
IAbstractFolder folder = new FolderWrapper(projectFolderOsPath);
return create(folder, type);
//Synthetic comment -- @@ -288,13 +244,27 @@

/**
* Creates a new project properties object, with no properties.
     * <p/>The file is not created until {@link #save()} is called.
* @param projectFolder the project folder.
* @param type the type of property file to create
*/
    public static ProjectProperties create(IAbstractFolder projectFolder, PropertyType type) {
// create and return a ProjectProperties with an empty map.
        return new ProjectProperties(projectFolder, new HashMap<String, String>(), type);
}

/**
//Synthetic comment -- @@ -307,15 +277,6 @@
}

/**
     * Sets a new properties. If a property with the same name already exists, it is replaced.
     * @param name the name of the property.
     * @param value the value of the property.
     */
    public synchronized void setProperty(String name, String value) {
        mProperties.put(name, value);
    }

    /**
* Returns the value of a property.
* @param name the name of the property.
* @return the property value or null if the property is not set.
//Synthetic comment -- @@ -325,14 +286,6 @@
}

/**
     * Removes a property and returns its previous value (or null if the property did not exist).
     * @param name the name of the property to remove.
     */
    public synchronized String removeProperty(String name) {
        return mProperties.remove(name);
    }

    /**
* Returns a set of the property keys. Unlike {@link Map#keySet()} this is not a view of the
* map keys. Modifying the returned {@link Set} will not impact the underlying {@link Map}.
*/
//Synthetic comment -- @@ -357,112 +310,6 @@
}

/**
     * Saves the property file, using UTF-8 encoding.
     * @throws IOException
     * @throws StreamException
     */
    public synchronized void save() throws IOException, StreamException {
        IAbstractFile toSave = mProjectFolder.getFile(mType.mFilename);

        // write the whole file in a byte array before dumping it in the file. This
        // This is so that if the file already existing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, SdkConstants.INI_CHARSET);

        if (toSave.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toSave.getContents(),
                    SdkConstants.INI_CHARSET));

            // since we're reading the existing file and replacing values with new ones, or skipping
            // removed values, we need to record what properties have been visited, so that
            // we can figure later what new properties need to be added at the end of the file.
            HashSet<String> visitedProps = new HashSet<String>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                // check if this is a line containing a property.
                if (line.length() > 0 && line.charAt(0) != '#') {

                    Matcher m = PATTERN_PROP.matcher(line);
                    if (m.matches()) {
                        String key = m.group(1);
                        String value = m.group(2);

                        // record the prop
                        visitedProps.add(key);

                        // check if the property still exists.
                        if (mProperties.containsKey(key)) {
                            // put the new value.
                            value = mProperties.get(key);
                        } else {
                            // property doesn't exist. Check if it's a known property.
                            // if it's a known one, we'll remove it, otherwise, leave it untouched.
                            if (mType.getValidProps().contains(key)) {
                                value = null;
                            }
                        }

                        // if the value is still valid, write it down.
                        if (value != null) {
                            writeValue(writer, key, value, false /*addComment*/);
                        }
                    } else  {
                        // the line was wrong, let's just ignore it so that it's removed from the
                        // file.
                    }
                } else {
                    // non-property line: just write the line in the output as-is.
                    writer.append(line).append('\n');
                }
            }

            // now add the new properties.
            for (Entry<String, String> entry : mProperties.entrySet()) {
                if (visitedProps.contains(entry.getKey()) == false) {
                    String value = entry.getValue();
                    if (value != null) {
                        writeValue(writer, entry.getKey(), value, true /*addComment*/);
                    }
                }
            }

        } else {
            // new file, just write it all
            // write the header
            writer.write(mType.mHeader);

            // write the properties.
            for (Entry<String, String> entry : mProperties.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    writeValue(writer, entry.getKey(), value, true /*addComment*/);
                }
            }
        }

        writer.flush();

        // now put the content in the file.
        OutputStream filestream = toSave.getOutputStream();
        filestream.write(baos.toByteArray());
        filestream.flush();
    }

    private void writeValue(OutputStreamWriter writer, String key, String value,
            boolean addComment) throws IOException {
        if (addComment) {
            String comment = COMMENT_MAP.get(key);
            if (comment != null) {
                writer.write(comment);
            }
        }

        value = value.replaceAll("\\\\", "\\\\\\\\");
        writer.write(String.format("%s=%s\n", key, value));
    }

    /**
* Parses a property file (using UTF-8 encoding) and returns a map of the content.
* <p/>If the file is not present, null is returned with no error messages sent to the log.
*
//Synthetic comment -- @@ -540,7 +387,7 @@
* Use {@link #load(String, PropertyType)} or {@link #create(String, PropertyType)}
* to instantiate.
*/
    private ProjectProperties(IAbstractFolder projectFolder, Map<String, String> map,
PropertyType type) {
mProjectFolder = projectFolder;
mProperties = map;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
new file mode 100644
//Synthetic comment -- index 0000000..e0f713c

//Synthetic comment -- @@ -0,0 +1,244 @@







