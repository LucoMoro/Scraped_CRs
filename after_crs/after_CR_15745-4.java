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
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import java.io.File;
//Synthetic comment -- @@ -177,6 +174,10 @@
private final ArrayList<ProjectState> mParentProjects = new ArrayList<ProjectState>();

public ProjectState(IProject project, ProjectProperties properties) {
        if (project == null || properties == null) {
            throw new NullPointerException();
        }

mProject = project;
mProperties = properties;

//Synthetic comment -- @@ -224,11 +225,7 @@
return mTarget.hashString();
}

        return mProperties.getProperty(ProjectProperties.PROPERTY_TARGET);
}

public IAndroidTarget getTarget() {
//Synthetic comment -- @@ -552,29 +549,15 @@
}

/**
     * Update the value of a library dependency.
     * <p/>This loops on all current dependency looking for the value to replace and then replaces
     * it.
     * <p/>This both updates the in-memory {@link #mProperties} values and on-disk
     * default.properties file.
     * @param oldValue the old value to replace
     * @param newValue the new value to set.
     * @return the status of the replacement. If null, no replacement was done (value not found).
*/
private IStatus replaceLibraryProperty(String oldValue, String newValue) {
int index = 1;
while (true) {
//Synthetic comment -- @@ -586,9 +569,15 @@
}

if (rootPath.equals(oldValue)) {
                // need to update the properties. Get a working copy to change it and save it on
                // disk since ProjectProperties is read-only.
                ProjectPropertiesWorkingCopy workingCopy = mProperties.makeWorkingCopy();
                workingCopy.setProperty(propName, newValue);
try {
                    workingCopy.save();

                    // reload the properties with the new values from the disk.
                    mProperties.reload();
} catch (Exception e) {
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
"Failed to save %1$s for project %2$s",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index 44da3a9..ba69977 100644

//Synthetic comment -- @@ -16,14 +16,18 @@

package com.android.ide.eclipse.adt.internal.properties;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdkuilib.internal.widgets.SdkTargetSelector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -50,6 +54,7 @@
// APK-SPLIT: This is not yet supported, so we hide the UI
//    private Button mSplitByDensity;
private LibraryProperties mLibraryDependencies;
    private ProjectPropertiesWorkingCopy mPropertiesWorkingCopy;

public AndroidPropertyPage() {
// pass
//Synthetic comment -- @@ -123,22 +128,20 @@
if (currentSdk != null && mProject.isOpen()) {
ProjectState state = Sdk.getProjectState(mProject);

            // simply update the properties copy. Eclipse will be notified of the file change
// and will reload it smartly (detecting differences) and updating the ProjectState.
// See Sdk.mFileListener
boolean mustSaveProp = false;

IAndroidTarget newTarget = mSelector.getSelected();
if (newTarget != state.getTarget()) {
                mPropertiesWorkingCopy.setProperty(ProjectProperties.PROPERTY_TARGET,
                        newTarget.hashString());
mustSaveProp = true;
}

if (mIsLibrary.getSelection() != state.isLibrary()) {
                mPropertiesWorkingCopy.setProperty(ProjectProperties.PROPERTY_LIBRARY,
Boolean.toString(mIsLibrary.getSelection()));
mustSaveProp = true;
}
//Synthetic comment -- @@ -151,9 +154,15 @@

if (mustSaveProp) {
try {
                    mPropertiesWorkingCopy.save();

                    IResource defaultProp = mProject.findMember(SdkConstants.FN_DEFAULT_PROPERTIES);
                    defaultProp.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
                } catch (Exception e) {
                    String msg = String.format(
                            "Failed to save %1$s for project %2$s",
                            SdkConstants.FN_DEFAULT_PROPERTIES, mProject.getName());
                    AdtPlugin.log(e, msg);
}
}
}
//Synthetic comment -- @@ -171,6 +180,9 @@
if (Sdk.getCurrent() != null && mProject.isOpen()) {
ProjectState state = Sdk.getProjectState(mProject);

            // make a working copy of the properties
            mPropertiesWorkingCopy = state.getProperties().makeWorkingCopy();

// get the target
IAndroidTarget target = state.getTarget();;
if (target != null) {
//Synthetic comment -- @@ -178,7 +190,7 @@
}

mIsLibrary.setSelection(state.isLibrary());
            mLibraryDependencies.setContent(state, mPropertiesWorkingCopy);

/*
* APK-SPLIT: This is not yet supported, so we hide the UI








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java
//Synthetic comment -- index 67de298..1f5f2c6 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.eclipse.adt.internal.project.ProjectState.LibraryState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -65,7 +66,16 @@
private Button mDownButton;
private ProjectChooserHelper mProjectChooser;

    /**
     * Original ProjectState being edited. This is read-only.
     * @see #mPropertiesWorkingCopy
     */
private ProjectState mState;
    /**
     * read-write copy of the properties being edited.
     */
    private ProjectPropertiesWorkingCopy mPropertiesWorkingCopy;

private final List<ItemData> mItemDataList = new ArrayList<ItemData>();
private boolean mMustSave = false;

//Synthetic comment -- @@ -231,10 +241,12 @@

/**
* Sets or reset the content.
     * @param state the {@link ProjectState} to display. This is read-only.
     * @param propertiesWorkingCopy the working copy of {@link ProjectProperties} to modify.
*/
    void setContent(ProjectState state, ProjectPropertiesWorkingCopy propertiesWorkingCopy) {
mState = state;
        mPropertiesWorkingCopy = propertiesWorkingCopy;

// reset content
mTable.removeAll();
//Synthetic comment -- @@ -254,10 +266,12 @@
}

/**
     * Saves the state of the UI into the {@link ProjectProperties} object that was returned by
* {@link #setContent(ProjectState)}.
     * <p/>This does not update the {@link ProjectState} object that was provided, nor does it save
     * the new properties on disk. Saving the properties on disk, via
     * {@link ProjectProperties#save()}, and updating the {@link ProjectState} instance, via
     * {@link ProjectState#reloadProperties()} must be done by the caller.
* @return <code>true</code> if there was actually new data saved in the project state, false
* otherwise.
*/
//Synthetic comment -- @@ -265,18 +279,17 @@
boolean mustSave = mMustSave;
if (mMustSave) {
// remove all previous library dependencies.
            Set<String> keys = mPropertiesWorkingCopy.keySet();
for (String key : keys) {
if (key.startsWith(ProjectProperties.PROPERTY_LIB_REF)) {
                    mPropertiesWorkingCopy.removeProperty(key);
}
}

// now add the new libraries.
int index = 1;
for (ItemData data : mItemDataList) {
                mPropertiesWorkingCopy.setProperty(ProjectProperties.PROPERTY_LIB_REF + index++,
data.relativePath);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 9423ae7..9db385a 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.StreamException;

//Synthetic comment -- @@ -306,10 +307,10 @@
// check if there's already a state?
ProjectState state = getProjectState(project);

            ProjectPropertiesWorkingCopy properties = null;

if (state != null) {
                properties = state.getProperties().makeWorkingCopy();
}

if (properties == null) {
//Synthetic comment -- @@ -890,82 +891,88 @@
public void fileChanged(final IFile file, IMarkerDelta[] markerDeltas, int kind) {
if (SdkConstants.FN_DEFAULT_PROPERTIES.equals(file.getName()) &&
file.getParent() == file.getProject()) {
                try {
                    // reload the content of the default.properties file and update
                    // the target.
                    IProject iProject = file.getProject();
                    ProjectState state = Sdk.getProjectState(iProject);

                    // get the current target
                    IAndroidTarget oldTarget = state.getTarget();

                    // get the current library flag
                    boolean wasLibrary = state.isLibrary();

                    LibraryDifference diff = state.reloadProperties();

                    // load the (possibly new) target.
                    IAndroidTarget newTarget = loadTarget(state);

                    // check if this is a new library
                    if (state.isLibrary() && wasLibrary == false) {
                        setupLibraryProject(iProject);
                    }

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
                            ArrayList<IProject> libsToLink = new ArrayList<IProject>();
                            synchronized (sLock) {
                                for (ProjectState projectState : sProjectStateMap.values()) {
                                    if (projectState != state) {
                                        LibraryState libState = state.needs(projectState);

                                        if (libState != null) {
                                            IProject p = libState.getProjectState().getProject();
                                            if (libsToLink.contains(p) == false) {
                                                libsToLink.add(p);
}

                                            // now find the dependencies of the library itself.
                                            fillProjectDependenciesList(
                                                    libState.getMainProjectState(), libsToLink);
}
}
}
}

                            if (libsToLink.size() > 0) {
                                LinkLibraryBundle bundle = new LinkLibraryBundle();
                                bundle.mProject = iProject;
                                bundle.mLibraryProjects =
                                        libsToLink.toArray(new IProject[libsToLink.size()]);
                                bundle.mPreviousLibraryPath = null;
                                bundle.mCleanupCPE = false;
                                startActionBundle(bundle);
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
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index bd50e16..d17e3cf 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
//Synthetic comment -- @@ -1096,7 +1097,7 @@
// is tied to the current target, so changing it would invalidate the project we're
// trying to load in the first place.
if (currentTarget == null || !mInfo.isCreateFromSample()) {
            ProjectPropertiesWorkingCopy p = ProjectProperties.create(projectLocation, null);
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
     * Writes the receiver into a {@link ProjectPropertiesWorkingCopy}.
     * @param properties the {@link ProjectPropertiesWorkingCopy} in which to store the settings.
*/
    public void write(ProjectPropertiesWorkingCopy properties) {
properties.setProperty(ProjectProperties.PROPERTY_SPLIT_BY_DENSITY,
Boolean.toString(mSplitByDensity));
properties.setProperty(ProjectProperties.PROPERTY_SPLIT_BY_ABI,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 4eda46c..df58290 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.AndroidXPathFactory;

//Synthetic comment -- @@ -187,13 +186,13 @@
// first create the project properties.

// location of the SDK goes in localProperty
            ProjectPropertiesWorkingCopy localProperties = ProjectProperties.create(folderPath,
PropertyType.LOCAL);
localProperties.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
localProperties.save();

// target goes in default properties
            ProjectPropertiesWorkingCopy defaultProperties = ProjectProperties.create(folderPath,
PropertyType.DEFAULT);
defaultProperties.setProperty(ProjectProperties.PROPERTY_TARGET, target.hashString());
if (library) {
//Synthetic comment -- @@ -202,7 +201,7 @@
defaultProperties.save();

// create a build.properties file with just the application package
            ProjectPropertiesWorkingCopy buildProperties = ProjectProperties.create(folderPath,
PropertyType.BUILD);

// only put application.package for older target where the rules file didn't.
//Synthetic comment -- @@ -375,13 +374,13 @@

try {
// location of the SDK goes in localProperty
            ProjectPropertiesWorkingCopy localProperties = ProjectProperties.create(folderPath,
PropertyType.LOCAL);
localProperties.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
localProperties.save();

// package name goes in export properties
            ProjectPropertiesWorkingCopy exportProperties = ProjectProperties.create(folderPath,
PropertyType.EXPORT);
exportProperties.setProperty(ProjectProperties.PROPERTY_PACKAGE, packageName);
exportProperties.setProperty(ProjectProperties.PROPERTY_VERSIONCODE, "1");
//Synthetic comment -- @@ -506,21 +505,28 @@

boolean saveDefaultProps = false;

        ProjectPropertiesWorkingCopy propsWC = null;

// Update default.prop if --target was specified
if (target != null) {
// we already attempted to load the file earlier, if that failed, create it.
if (props == null) {
                propsWC = ProjectProperties.create(folderPath, PropertyType.DEFAULT);
            } else {
                propsWC = props.makeWorkingCopy();
}

// set or replace the target
            propsWC.setProperty(ProjectProperties.PROPERTY_TARGET, target.hashString());
saveDefaultProps = true;
}

if (libraryPath != null) {
// at this point, the default properties already exists, either because they were
// already there or because they were created with a new target
            if (propsWC == null) {
                propsWC = props.makeWorkingCopy();
            }

// check the reference is valid
File libProject = new File(libraryPath);
//Synthetic comment -- @@ -558,14 +564,14 @@
}

String propName = ProjectProperties.PROPERTY_LIB_REF + Integer.toString(index);
            propsWC.setProperty(propName, libraryPath);
saveDefaultProps = true;
}

// save the default props if needed.
if (saveDefaultProps) {
try {
                propsWC.save();
println("Updated %1$s", PropertyType.DEFAULT.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -580,13 +586,15 @@
// we first try to load it.
props = ProjectProperties.load(folderPath, PropertyType.LOCAL);
if (props == null) {
            propsWC = ProjectProperties.create(folderPath, PropertyType.LOCAL);
        } else {
            propsWC = props.makeWorkingCopy();
}

// set or replace the sdk location.
        propsWC.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
try {
            propsWC.save();
println("Updated %1$s", PropertyType.LOCAL.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -759,14 +767,17 @@

// add the test project specific properties.
ProjectProperties buildProps = ProjectProperties.load(folderPath, PropertyType.BUILD);
        ProjectPropertiesWorkingCopy buildWorkingCopy;
if (buildProps == null) {
            buildWorkingCopy = ProjectProperties.create(folderPath, PropertyType.BUILD);
        } else {
            buildWorkingCopy = buildProps.makeWorkingCopy();
}

// set or replace the path to the main project
        buildWorkingCopy.setProperty(ProjectProperties.PROPERTY_TESTED_PROJECT, pathToMainProject);
try {
            buildWorkingCopy.save();
println("Updated %1$s", PropertyType.BUILD.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
//Synthetic comment -- @@ -806,14 +817,17 @@
// because the file may already exist and contain other values (like apk config),
// we first try to load it.
ProjectProperties props = ProjectProperties.load(folderPath, PropertyType.LOCAL);
        ProjectPropertiesWorkingCopy localPropsWorkingCopy;
if (props == null) {
            localPropsWorkingCopy = ProjectProperties.create(folderPath, PropertyType.LOCAL);
        } else {
            localPropsWorkingCopy = props.makeWorkingCopy();
}

// set or replace the sdk location.
        localPropsWorkingCopy.setProperty(ProjectProperties.PROPERTY_SDK, mSdkFolder);
try {
            localPropsWorkingCopy.save();
println("Updated %1$s", PropertyType.LOCAL.getFilename());
} catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 40b055a..4aea001 100644

//Synthetic comment -- @@ -25,29 +25,34 @@
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing project properties for both ADT and Ant-based build.
 * <p/>The class is associated to a {@link PropertyType} that indicate which of the project
 * property file is represented.
 * <p/>To load an existing file, use {@link #load(IAbstractFolder, PropertyType)}.
 * <p/>The class is meant to be always in sync (or at least not newer) than the file it represents.
 * Once created, it can only be updated through {@link #reload()}
 *
 * <p/>The make modification or make new file, use a {@link ProjectPropertiesWorkingCopy} instance,
 * either through {@link #create(IAbstractFolder, PropertyType)} or through
 * {@link #makeWorkingCopy()}.
*
*/
public class ProjectProperties {
    protected final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

/** The property name for the project target */
//Synthetic comment -- @@ -55,6 +60,7 @@

public final static String PROPERTY_LIBRARY = "android.library";
public final static String PROPERTY_LIB_REF = "android.library.reference.";
    private final static String PROPERTY_LIB_REF_REGEX = "android.library.reference.\\d+";

public final static String PROPERTY_SDK = "sdk.dir";
// LEGACY - compatibility with 1.6 and before
//Synthetic comment -- @@ -83,7 +89,7 @@
PROPERTY_BUILD_SOURCE_DIR, PROPERTY_BUILD_OUT_DIR
}),
DEFAULT(SdkConstants.FN_DEFAULT_PROPERTIES, DEFAULT_HEADER, new String[] {
                PROPERTY_TARGET, PROPERTY_LIBRARY, PROPERTY_LIB_REF_REGEX,
PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
}),
LOCAL(SdkConstants.FN_LOCAL_PROPERTIES, LOCAL_HEADER, new String[] {
//Synthetic comment -- @@ -96,26 +102,35 @@

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
return mFilename;
}

        public String getHeader() {
            return mHeader;
        }

/**
         * Returns whether a given property is known for the property type.
*/
        public boolean isKnownProperty(String name) {
            for (String propRegex : mKnownProps) {
                if (propRegex.equals(name) || Pattern.matches(propRegex, name)) {
                    return true;
                }
            }

            return false;
}
}

//Synthetic comment -- @@ -178,33 +193,9 @@
"# The password will be asked during the build when you use the 'release' target.\n" +
"\n";

    protected final IAbstractFolder mProjectFolder;
    protected final Map<String, String> mProperties;
    protected final PropertyType mType;

/**
* Loads a project properties file and return a {@link ProjectProperties} object
//Synthetic comment -- @@ -239,48 +230,13 @@
}

/**
* Creates a new project properties object, with no properties.
     * <p/>The file is not created until {@link ProjectPropertiesWorkingCopy#save()} is called.
* @param projectFolderOsPath the project folder.
* @param type the type of property file to create
*/
    public static ProjectPropertiesWorkingCopy create(String projectFolderOsPath,
            PropertyType type) {
// create and return a ProjectProperties with an empty map.
IAbstractFolder folder = new FolderWrapper(projectFolderOsPath);
return create(folder, type);
//Synthetic comment -- @@ -288,13 +244,27 @@

/**
* Creates a new project properties object, with no properties.
     * <p/>The file is not created until {@link ProjectPropertiesWorkingCopy#save()} is called.
* @param projectFolder the project folder.
* @param type the type of property file to create
*/
    public static ProjectPropertiesWorkingCopy create(IAbstractFolder projectFolder,
            PropertyType type) {
// create and return a ProjectProperties with an empty map.
        return new ProjectPropertiesWorkingCopy(projectFolder, new HashMap<String, String>(), type);
    }

    /**
     * Creates and returns a copy of the current properties as a
     * {@link ProjectPropertiesWorkingCopy} that can be modified and saved.
     * @return a new instance of {@link ProjectPropertiesWorkingCopy}
     */
    public ProjectPropertiesWorkingCopy makeWorkingCopy() {
        // copy the current properties in a new map
        HashMap<String, String> propList = new HashMap<String, String>();
        propList.putAll(mProperties);

        return new ProjectPropertiesWorkingCopy(mProjectFolder, propList, mType);
}

/**
//Synthetic comment -- @@ -307,15 +277,6 @@
}

/**
* Returns the value of a property.
* @param name the name of the property.
* @return the property value or null if the property is not set.
//Synthetic comment -- @@ -325,14 +286,6 @@
}

/**
* Returns a set of the property keys. Unlike {@link Map#keySet()} this is not a view of the
* map keys. Modifying the returned {@link Set} will not impact the underlying {@link Map}.
*/
//Synthetic comment -- @@ -357,112 +310,6 @@
}

/**
* Parses a property file (using UTF-8 encoding) and returns a map of the content.
* <p/>If the file is not present, null is returned with no error messages sent to the log.
*
//Synthetic comment -- @@ -540,7 +387,7 @@
* Use {@link #load(String, PropertyType)} or {@link #create(String, PropertyType)}
* to instantiate.
*/
    protected ProjectProperties(IAbstractFolder projectFolder, Map<String, String> map,
PropertyType type) {
mProjectFolder = projectFolder;
mProperties = map;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
new file mode 100644
//Synthetic comment -- index 0000000..e0f713c

//Synthetic comment -- @@ -0,0 +1,244 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.project;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

/**
 * A modifyable and saveable copy of a {@link ProjectProperties}.
 * <p/>This copy gives access to modification method such as {@link #setProperty(String, String)}
 * and {@link #removeProperty(String)}.
 *
 * To get access to an instance, use {@link ProjectProperties#makeWorkingCopy()} or
 * {@link ProjectProperties#create(IAbstractFolder, PropertyType)}.
 */
@SuppressWarnings("deprecation")
public class ProjectPropertiesWorkingCopy extends ProjectProperties {

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


    /**
     * Sets a new properties. If a property with the same name already exists, it is replaced.
     * @param name the name of the property.
     * @param value the value of the property.
     */
    public synchronized void setProperty(String name, String value) {
        mProperties.put(name, value);
    }

    /**
     * Removes a property and returns its previous value (or null if the property did not exist).
     * @param name the name of the property to remove.
     */
    public synchronized String removeProperty(String name) {
        return mProperties.remove(name);
    }

    /**
     * Merges all properties from the given file into the current properties.
     * <p/>
     * This emulates the Ant behavior: existing properties are <em>not</em> overridden.
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
    public synchronized ProjectPropertiesWorkingCopy merge(PropertyType type) {
        if (mProjectFolder.exists() && mType != type) {
            IAbstractFile propFile = mProjectFolder.getFile(type.getFilename());
            if (propFile.exists()) {
                Map<String, String> map = parsePropertyFile(propFile, null /* log */);
                if (map != null) {
                    for (Entry<String, String> entry : map.entrySet()) {
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
     * Saves the property file, using UTF-8 encoding.
     * @throws IOException
     * @throws StreamException
     */
    public synchronized void save() throws IOException, StreamException {
        IAbstractFile toSave = mProjectFolder.getFile(mType.getFilename());

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
                            if (mType.isKnownProperty(key)) {
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
            writer.write(mType.getHeader());

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
     * Private constructor.
     * <p/>
     * Use {@link #load(String, PropertyType)} or {@link #create(String, PropertyType)}
     * to instantiate.
     */
    ProjectPropertiesWorkingCopy(IAbstractFolder projectFolder, Map<String, String> map,
            PropertyType type) {
        super(projectFolder, map, type);
    }

}







