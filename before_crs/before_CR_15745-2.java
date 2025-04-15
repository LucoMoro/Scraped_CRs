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
//Synthetic comment -- index 5886692..af64b1a 100644

//Synthetic comment -- @@ -19,15 +19,11 @@
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
//Synthetic comment -- @@ -551,30 +547,6 @@
mParentProjects.remove(parentState);
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index 44da3a9..094a36e 100644

//Synthetic comment -- @@ -16,14 +16,17 @@

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
//Synthetic comment -- @@ -50,6 +53,7 @@
// APK-SPLIT: This is not yet supported, so we hide the UI
//    private Button mSplitByDensity;
private LibraryProperties mLibraryDependencies;

public AndroidPropertyPage() {
// pass
//Synthetic comment -- @@ -123,22 +127,19 @@
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
//Synthetic comment -- @@ -151,9 +152,15 @@

if (mustSaveProp) {
try {
                    state.saveProperties();
                } catch (CoreException e) {
                    // pass
}
}
}
//Synthetic comment -- @@ -171,6 +178,9 @@
if (Sdk.getCurrent() != null && mProject.isOpen()) {
ProjectState state = Sdk.getProjectState(mProject);

// get the target
IAndroidTarget target = state.getTarget();;
if (target != null) {
//Synthetic comment -- @@ -178,7 +188,7 @@
}

mIsLibrary.setSelection(state.isLibrary());
            mLibraryDependencies.setContent(state);

/*
* APK-SPLIT: This is not yet supported, so we hide the UI








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/LibraryProperties.java
//Synthetic comment -- index 67de298..13433c1 100644

//Synthetic comment -- @@ -65,7 +65,16 @@
private Button mDownButton;
private ProjectChooserHelper mProjectChooser;

private ProjectState mState;
private final List<ItemData> mItemDataList = new ArrayList<ItemData>();
private boolean mMustSave = false;

//Synthetic comment -- @@ -231,10 +240,12 @@

/**
* Sets or reset the content.
     * @param state the {@link ProjectState} to display
*/
    void setContent(ProjectState state) {
mState = state;

// reset content
mTable.removeAll();
//Synthetic comment -- @@ -254,10 +265,12 @@
}

/**
     * Saves the state of the UI into the {@link ProjectState} object that was given to
* {@link #setContent(ProjectState)}.
     * <p/>This only saves the data into the {@link ProjectProperties} of the state, but does
     * not update the {@link ProjectState} or the list of {@link LibraryState}.
* @return <code>true</code> if there was actually new data saved in the project state, false
* otherwise.
*/
//Synthetic comment -- @@ -265,18 +278,17 @@
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
//Synthetic comment -- index 9423ae7..c142daa 100644

//Synthetic comment -- @@ -890,82 +890,88 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 40b055a..b24166e 100644

//Synthetic comment -- @@ -55,6 +55,7 @@

public final static String PROPERTY_LIBRARY = "android.library";
public final static String PROPERTY_LIB_REF = "android.library.reference.";

public final static String PROPERTY_SDK = "sdk.dir";
// LEGACY - compatibility with 1.6 and before
//Synthetic comment -- @@ -83,7 +84,7 @@
PROPERTY_BUILD_SOURCE_DIR, PROPERTY_BUILD_OUT_DIR
}),
DEFAULT(SdkConstants.FN_DEFAULT_PROPERTIES, DEFAULT_HEADER, new String[] {
                PROPERTY_TARGET, PROPERTY_LIBRARY, PROPERTY_LIB_REF,
PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
}),
LOCAL(SdkConstants.FN_LOCAL_PROPERTIES, LOCAL_HEADER, new String[] {
//Synthetic comment -- @@ -96,14 +97,14 @@

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
//Synthetic comment -- @@ -111,11 +112,16 @@
}

/**
         * Returns an unmodifyable {@link Set} of the known properties for that type of
         * property file.
*/
        public Set<String> getValidProps() {
            return mValidProps;
}
}

//Synthetic comment -- @@ -297,6 +303,14 @@
return new ProjectProperties(projectFolder, new HashMap<String, String>(), type);
}

/**
* Returns the type of the property file.
*
//Synthetic comment -- @@ -398,7 +412,7 @@
} else {
// property doesn't exist. Check if it's a known property.
// if it's a known one, we'll remove it, otherwise, leave it untouched.
                            if (mType.getValidProps().contains(key)) {
value = null;
}
}







