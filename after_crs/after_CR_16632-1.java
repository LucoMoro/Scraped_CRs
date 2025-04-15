/*Enhance Android Classpath Container

Change-Id:Ib17c40bb87445c330fb9bf3dfdbe2f934d9107d2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index ecd1c05..06d35e1 100644

//Synthetic comment -- @@ -16,6 +16,16 @@

package com.android.ide.eclipse.adt.internal.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
//Synthetic comment -- @@ -23,8 +33,8 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -52,27 +62,23 @@
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;

/**
* Classpath container initializer responsible for binding {@link AndroidClasspathContainer} to
* {@link IProject}s. This removes the hard-coded path to the android.jar.
*/
public class AndroidClasspathContainerInitializer extends ClasspathContainerInitializer {

    public static final String SOURCES_ZIP = "/sources.zip"; //$NON-NLS-1$

    public static final String COM_ANDROID_IDE_ECLIPSE_ADT_SOURCE = 
        "com.android.ide.eclipse.source"; //$NON-NLS-1$

    private static final String ANDROID_API_REFERENCE = 
        "http://developer.android.com/reference/"; //$NON-NLS-1$

    private final static String PROPERTY_ANDROID_API = "androidApi"; //$NON-NLS-1$

    private final static String PROPERTY_ANDROID_SOURCE = "androidSource"; //$NON-NLS-1$
	
	/** The container id for the android framework jar file */
public final static String CONTAINER_ID =
//Synthetic comment -- @@ -474,80 +480,82 @@
String androidSrc = null;
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
if (target != null) {
            androidSrc = ProjectHelper.loadStringProperty(root, getAndroidSourceProperty(target));
}
        if (androidSrc != null && androidSrc.trim().length() > 0) {
            android_src = new Path(androidSrc);
        }
        if (android_src == null) {
            android_src = new Path(paths[CACHE_INDEX_SRC]);
            File androidSrcFile = new File(paths[CACHE_INDEX_SRC]);
if (!androidSrcFile.isDirectory()) {
                android_src = null;
}
}

        if (android_src == null && target != null) {
            Bundle bundle = getSourceBundle();

            if (bundle != null) {
                AndroidVersion version = target.getVersion();
                String apiString = version.getApiString();
                String sourcePath = apiString + SOURCES_ZIP;
                // FIXME API level 6 (SDK 2.0.1) and API Level 5 (SDK 2.0) are
                // same
                if ("6".equals(apiString)) {
                    sourcePath = "5" + SOURCES_ZIP;
                }
                URL sourceURL = bundle.getEntry(sourcePath);
                if (sourceURL != null) {
                    URL url = null;
                    try {
                        url = FileLocator.resolve(sourceURL);
                    } catch (IOException ignore) {
                    }
                    if (url != null) {
                        androidSrc = url.getFile();
                        if (new File(androidSrc).isFile()) {
                            android_src = new Path(androidSrc);
                        }
                    }
                }
            }
        }

        // create the java doc link.
String androidApiURL = ProjectHelper.loadStringProperty(root, PROPERTY_ANDROID_API);
String apiURL = null;
        if (androidApiURL != null) {
            apiURL = androidApiURL;
        } else {
            if (testURL(androidApiURL)) {
                apiURL = androidApiURL;
            } else if (testURL(paths[CACHE_INDEX_DOCS_URI])) {
                apiURL = paths[CACHE_INDEX_DOCS_URI];
            } else if (testURL(ANDROID_API_REFERENCE)) {
                apiURL = ANDROID_API_REFERENCE;
            }
}
        IClasspathAttribute[] attributes = null;
        if (apiURL != null) {

            IClasspathAttribute cpAttribute = JavaCore.newClasspathAttribute(
                    IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, apiURL);
            attributes = new IClasspathAttribute[] {
                cpAttribute
            };
        }
        // create the access rule to restrict access to classes in
        // com.android.internal
        IAccessRule accessRule = JavaCore.newAccessRule(new Path("com/android/internal/**"), //$NON-NLS-1$
IAccessRule.K_NON_ACCESSIBLE);

IClasspathEntry frameworkClasspathEntry = JavaCore.newLibraryEntry(android_lib,
android_src, // source attachment path
                null, // default source attachment root path.
                new IAccessRule[] {
                    accessRule
                }, attributes, false // not exported.
);

list.add(frameworkClasspathEntry);
//Synthetic comment -- @@ -562,66 +570,66 @@
attributes = null;
if (docPath.length() > 0) {
attributes = new IClasspathAttribute[] {
                        JavaCore.newClasspathAttribute(
                                IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, docPath)
};
}

                IClasspathEntry entry = JavaCore.newLibraryEntry(jarPath, null, // source
                                                                                // attachment
                                                                                // path
null, // default source attachment root path.
                        null, attributes, false // not exported.
);
list.add(entry);
}
}

if (apiURL != null) {
            ProjectHelper.saveStringProperty(root, PROPERTY_ANDROID_API, apiURL);
}
if (android_src != null && target != null) {
            ProjectHelper.saveStringProperty(root, getAndroidSourceProperty(target),
                    android_src.toOSString());
}
return list.toArray(new IClasspathEntry[list.size()]);
}

    private static Bundle getSourceBundle() {
        String bundleId = System.getProperty(COM_ANDROID_IDE_ECLIPSE_ADT_SOURCE,
                COM_ANDROID_IDE_ECLIPSE_ADT_SOURCE);
        Bundle bundle = Platform.getBundle(bundleId);
        return bundle;
    }

private static String getAndroidSourceProperty(IAndroidTarget target) {
        if (target == null) {
            return null;
        }
        String androidSourceProperty = PROPERTY_ANDROID_SOURCE + "_"
                + target.getVersion().getApiString();
        return androidSourceProperty;
    }

private static boolean testURL(String androidApiURL) {
        boolean valid = false;
InputStream is = null;
try {
            URL testURL = new URL(androidApiURL);
            is = testURL.openStream();
            valid = true;
        } catch (Exception ignore) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
        return valid;
    }

/**
* Checks the projects' caches. If the cache was valid, the project is removed from the list.
* @param projects the list of projects to check.
//Synthetic comment -- @@ -782,79 +790,84 @@
}

@Override
    public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
        return true;
    }

@Override
    public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project,
            IClasspathContainer containerSuggestion) throws CoreException {
        AdtPlugin plugin = AdtPlugin.getDefault();

        synchronized (Sdk.getLock()) {
            boolean sdkIsLoaded = plugin.getSdkLoadStatus() == LoadStatus.LOADED;

            // check if the project has a valid target.
            IAndroidTarget target = null;
            if (sdkIsLoaded) {
                target = Sdk.getCurrent().getTarget(project.getProject());
            }
            if (sdkIsLoaded && target != null) {
                String[] paths = getTargetPaths(target);
                IPath android_lib = new Path(paths[CACHE_INDEX_JAR]);
                IClasspathEntry[] entries = containerSuggestion.getClasspathEntries();
                for (int i = 0; i < entries.length; i++) {
                    IClasspathEntry entry = entries[i];
                    if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                        IPath entryPath = entry.getPath();

                        if (entryPath != null) {
                            if (entryPath.equals(android_lib)) {
                                IPath entrySrcPath = entry.getSourceAttachmentPath();
                                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                                if (entrySrcPath != null) {
                                    ProjectHelper.saveStringProperty(root,
                                            getAndroidSourceProperty(target),
                                            entrySrcPath.toString());
                                } else {
                                    ProjectHelper.saveStringProperty(root,
                                            getAndroidSourceProperty(target), null);
                                }
                                IClasspathAttribute[] extraAttributtes = entry.getExtraAttributes();
                                for (int j = 0; j < extraAttributtes.length; j++) {
                                    IClasspathAttribute extraAttribute = extraAttributtes[j];
                                    if (IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME
                                            .equals(extraAttribute.getName())) {
                                        ProjectHelper.saveStringProperty(root,
                                                PROPERTY_ANDROID_API, extraAttribute.getValue());

                                    }
                                }
                            }
                        }
                    }
                }
                rebindClasspathEntries(project.getJavaModel(), containerPath);
            }
        }
    }

    private static void rebindClasspathEntries(IJavaModel model, IPath containerPath)
            throws JavaModelException {
        ArrayList affectedProjects = new ArrayList();

        IJavaProject[] projects = model.getJavaProjects();
        for (int i = 0; i < projects.length; i++) {
            IJavaProject project = projects[i];
            IClasspathEntry[] entries = project.getRawClasspath();
            for (int k = 0; k < entries.length; k++) {
                IClasspathEntry curr = entries[k];
                if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER
                        && containerPath.equals(curr.getPath())) {
                    affectedProjects.add(project);
                }
            }
        }
        if (!affectedProjects.isEmpty()) {
            IJavaProject[] affected = (IJavaProject[]) affectedProjects
                    .toArray(new IJavaProject[affectedProjects.size()]);
            updateProjects(affected);
        }
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerPage.java
//Synthetic comment -- index d0b26c9..53f5809 100644

//Synthetic comment -- @@ -13,9 +13,8 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*
*******************************************************************************/

package com.android.ide.eclipse.adt.internal.project;

import java.util.Arrays;
//Synthetic comment -- @@ -40,155 +39,155 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AndroidClasspathContainerPage extends WizardPage implements IClasspathContainerPage,
        IClasspathContainerPageExtension

{
    private IProject mOwnerProject;

    private String mLibsProjectName;

    private Combo mProjectsCombo;

    private IStatus mCurrStatus;

    private boolean mPageVisible;

    public AndroidClasspathContainerPage() {
        super("AndroidClasspathContainerPage"); //$NON-NLS-1$
        mPageVisible = false;
        mCurrStatus = new StatusInfo();
        setTitle("Android Libraries");
        setDescription("This container manages classpath entries for Android container");
    }

    public IClasspathEntry getSelection() {
        IPath path = new Path(AndroidClasspathContainerInitializer.CONTAINER_ID);

        final int index = this.mProjectsCombo.getSelectionIndex();
        if (index != -1) {
            final String selectedProjectName = this.mProjectsCombo.getItem(index);

            if (this.mOwnerProject == null
                    || !selectedProjectName.equals(this.mOwnerProject.getName())) {
                path = path.append(selectedProjectName);
            }
        }

        return JavaCore.newContainerEntry(path);
    }

    public void setSelection(final IClasspathEntry cpentry) {
        final IPath path = cpentry == null ? null : cpentry.getPath();

        if (path == null || path.segmentCount() == 1) {
            if (this.mOwnerProject != null) {
                this.mLibsProjectName = this.mOwnerProject.getName();
            }
        } else {
            this.mLibsProjectName = path.segment(1);
        }
    }

    public void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        final Label label = new Label(composite, SWT.NONE);
        label.setText("Project:");

        final String[] androidProjects = getAndroidProjects();

        this.mProjectsCombo = new Combo(composite, SWT.READ_ONLY);
        this.mProjectsCombo.setItems(androidProjects);

        final int index;

        if (this.mOwnerProject != null) {
            index = indexOf(androidProjects, this.mLibsProjectName);
        } else {
            if (this.mProjectsCombo.getItemCount() > 0) {
                index = 0;
            } else {
                index = -1;
            }
        }

        if (index != -1) {
            this.mProjectsCombo.select(index);
        }

        final GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.minimumWidth = 100;

        this.mProjectsCombo.setLayoutData(gd);

        setControl(composite);
    }

    public boolean finish() {
        return true;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        mPageVisible = visible;
        // policy: wizards are not allowed to come up with an error message
        if (visible && mCurrStatus.matches(IStatus.ERROR)) {
            StatusInfo status = new StatusInfo();
            status.setError(""); //$NON-NLS-1$
            mCurrStatus = status;
        }
        updateStatus(mCurrStatus);
    }

    /**
     * Updates the status line and the OK button according to the given status
     * 
     * @param status status to apply
     */
    protected void updateStatus(IStatus status) {
        mCurrStatus = status;
        setPageComplete(!status.matches(IStatus.ERROR));
        if (mPageVisible) {
            StatusUtil.applyToStatusLine(this, status);
        }
    }

    /**
     * Updates the status line and the OK button according to the status
     * evaluate from an array of status. The most severe error is taken. In case
     * that two status with the same severity exists, the status with lower
     * index is taken.
     * 
     * @param status the array of status
     */
    protected void updateStatus(IStatus[] status) {
        updateStatus(StatusUtil.getMostSevere(status));
    }

    public void initialize(final IJavaProject project, final IClasspathEntry[] currentEntries) {
        this.mOwnerProject = (project == null ? null : project.getProject());
    }

    private static String[] getAndroidProjects() {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        final String[] names = new String[projects.length];
        for (int i = 0; i < projects.length; i++) {
            names[i] = projects[i].getName();
        }
        Arrays.sort(names);
        return names;
    }

    private static int indexOf(final String[] array, final String str) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sourcelookup/AdtSourceLookupDirector.java
//Synthetic comment -- index 9a4cd39..ae863f5 100755

//Synthetic comment -- @@ -13,13 +13,14 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*
*******************************************************************************/

package com.android.ide.eclipse.adt.internal.sourcelookup;

import java.io.File;

import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -33,64 +34,61 @@
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;

/**
* @author snpe
*/
public class AdtSourceLookupDirector extends JavaSourceLookupDirector {

    @Override
    public void initializeDefaults(ILaunchConfiguration configuration) throws CoreException {
        dispose();
        setLaunchConfiguration(configuration);
        String projectName = configuration.getAttribute("org.eclipse.jdt.launching.PROJECT_ATTR",
                "");
        if (projectName != null && projectName.length() > 0) {

            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            if (project != null && project.isOpen()) {
                IJavaProject javaProject = JavaCore.create(project);
                if (javaProject != null && javaProject.isOpen()) {
                    IClasspathEntry[] entries = javaProject.getRawClasspath();
                    IClasspathEntry androidEntry = null;
                    for (int i = 0; i < entries.length; i++) {
                        IClasspathEntry entry = entries[i];
                        if (entry.getPath() != null
                                && AndroidClasspathContainerInitializer.CONTAINER_ID.equals(entry
                                        .getPath().toString())) {
                            androidEntry = entry;
                            break;
                        }
                    }
                    if (androidEntry != null) {
                        IPath sourceAttachmentPath = androidEntry.getSourceAttachmentPath();
                        if (sourceAttachmentPath != null) {
                            String androidSrc = sourceAttachmentPath.toString();
                            if (androidSrc != null && androidSrc.trim().length() > 0) {
                                File srcFile = new File(androidSrc);
                                if (srcFile.isFile() && srcFile.exists()) {
                                    ISourceContainer adtContainer = 
                                        new ExternalArchiveSourceContainer(androidSrc, true);
                                    ISourceContainer defaultContainer = 
                                        new DefaultSourceContainer();
                                    setSourceContainers(new ISourceContainer[] {
                                            adtContainer, defaultContainer
                                    });
                                    initializeParticipants();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        setSourceContainers(new ISourceContainer[] {
            new DefaultSourceContainer()
        });
        initializeParticipants();
    }

}







