/*Lazy loading (and reloading) of project resources.

Change-Id:I4a725d523ae14ba8b487076e230fe9d622d5d281*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 5183658..73d183c 100644

//Synthetic comment -- @@ -323,17 +323,22 @@

// Notify the ResourceManager:
ResourceManager resManager = ResourceManager.getInstance();
                    ProjectResources projectResources = resManager.getProjectResources(project);

if (ResourceManager.isAutoBuilding()) {
IdeScanningContext context = new IdeScanningContext(projectResources,
project, true);

                        resManager.processDelta(delta, context);

// Check whether this project or its dependencies (libraries) have
// resources that need compilation
                        if (context.needsFullAapt()) {
mMustCompileResources = true;

// Must also call markAaptRequested on the project to not just
//Synthetic comment -- @@ -735,6 +740,10 @@

// Also clean up lint
EclipseLintClient.clearMarkers(project);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index ccd1666..68c2257 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.annotations.NonNull;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.IntArrayWrapper;
//Synthetic comment -- @@ -26,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.resources.ResourceType;
import com.android.util.Pair;

//Synthetic comment -- @@ -48,6 +50,7 @@
*       on the fly.</li>
*</ul>
*/
public class ProjectResources extends ResourceRepository {
// project resources are defined as 0x7FXX#### where XX is the resource type (layout, drawable,
// etc...). Using FF as the type allows for 255 resource types before we get a collision
//Synthetic comment -- @@ -65,12 +68,18 @@

private final IProject mProject;

/**
* Makes a ProjectResources for a given <var>project</var>.
* @param project the project.
*/
    public ProjectResources(IProject project) {
        super(false /*isFrameworkRepository*/);
mProject = project;
}

//Synthetic comment -- @@ -85,6 +94,7 @@
@NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 1e12861..e407b6a 100644

//Synthetic comment -- @@ -49,7 +49,6 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//Synthetic comment -- @@ -151,14 +150,14 @@
/**
* Returns the resources of a project.
* @param project The project
     * @return a ProjectResources object or null if none was found.
*/
public ProjectResources getProjectResources(IProject project) {
synchronized (mMap) {
ProjectResources resources = mMap.get(project);

if (resources == null) {
                resources = new ProjectResources(project);
mMap.put(project, resources);
}

//Synthetic comment -- @@ -253,7 +252,7 @@

// if it doesn't exist, we create it.
if (resources == null) {
                                resources = new ProjectResources(project);
mMap.put(project, resources);
}
}
//Synthetic comment -- @@ -491,16 +490,11 @@

FolderWrapper frameworkRes = new FolderWrapper(osResourcesPath);
if (frameworkRes.exists()) {
            FrameworkResources resources = new FrameworkResources();

            try {
                resources.loadResources(frameworkRes);
                resources.loadPublicResources(frameworkRes, AdtPlugin.getDefault());
                return resources;
            } catch (IOException e) {
                // since we test that folders are folders, and files are files, this shouldn't
                // happen. We can ignore it.
            }
}

return null;
//Synthetic comment -- @@ -512,62 +506,13 @@
*/
private void createProject(IProject project) {
if (project.isOpen()) {
            try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
                    return;
                }
            } catch (CoreException e1) {
                // can't check the nature of the project? ignore it.
                return;
            }

            IFolder resourceFolder = project.getFolder(SdkConstants.FD_RESOURCES);

            ProjectResources projectResources;
synchronized (mMap) {
                projectResources = mMap.get(project);
if (projectResources == null) {
                    projectResources = new ProjectResources(project);
mMap.put(project, projectResources);
}
}
            IdeScanningContext context = new IdeScanningContext(projectResources, project, true);

            if (resourceFolder != null && resourceFolder.exists()) {
                try {
                    IResource[] resources = resourceFolder.members();

                    for (IResource res : resources) {
                        if (res.getType() == IResource.FOLDER) {
                            IFolder folder = (IFolder)res;
                            ResourceFolder resFolder = projectResources.processFolder(
                                    new IFolderWrapper(folder));

                            if (resFolder != null) {
                                // now we process the content of the folder
                                IResource[] files = folder.members();

                                for (IResource fileRes : files) {
                                    if (fileRes.getType() == IResource.FILE) {
                                        IFile file = (IFile)fileRes;

                                        context.startScanning(file);

                                        resFolder.processFile(new IFileWrapper(file),
                                                ResourceHelper.getResourceDeltaKind(
                                                        IResourceDelta.ADDED), context);

                                        context.finishScanning(file);
                                    }
                                }
                            }
                        }
                    }
                } catch (CoreException e) {
                    // This happens if the project is closed or if the folder doesn't exist.
                    // Since we already test for that, we can ignore this exception.
                }
            }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 5cb5647..30f23de 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -43,7 +44,6 @@
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
//Synthetic comment -- @@ -215,8 +215,13 @@
ResourceRepository framework = ResourceManager.getInstance().loadFrameworkResources(target);

// now load the project resources
        ProjectResources project = new ProjectResources(null /*project*/);
        project.loadResources(resFolder);

// Create a folder configuration that will be used for the rendering:
FolderConfiguration config = getConfiguration();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java
//Synthetic comment -- index 7ae96a8..cde12e5 100644

//Synthetic comment -- @@ -18,11 +18,15 @@
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.resources.ResourceType;
import com.android.utils.StdLogger;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -219,11 +223,14 @@
}

public void testResourcesExist() throws Exception {
AttributeInfo info = new AttributeInfo("test", Format.REFERENCE_SET);
        TestResourceRepository projectResources = new TestResourceRepository(false);
projectResources.addResource(ResourceType.STRING, "mystring");
projectResources.addResource(ResourceType.DIMEN, "mydimen");
        TestResourceRepository frameworkResources = new TestResourceRepository(true);
frameworkResources.addResource(ResourceType.LAYOUT, "mylayout");

assertTrue(info.isValid("@string/mystring", null, null));
//Synthetic comment -- @@ -247,8 +254,8 @@
private class TestResourceRepository extends ResourceRepository {
private Multimap<ResourceType, String> mResources = ArrayListMultimap.create();

        protected TestResourceRepository(boolean isFrameworkRepository) {
            super(isFrameworkRepository);
}

void addResource(ResourceType type, String name) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index b45242b..a6da135 100644

//Synthetic comment -- @@ -16,8 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.resources.manager;

import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.SingleResourceFile;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -25,6 +27,8 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.ide.eclipse.mock.Mocks;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
//Synthetic comment -- @@ -46,7 +50,7 @@
private static final String MISC2_FILENAME = "bar.xml"; //$NON-NLS-1$

private FolderConfiguration mDefaultConfig;
    private ProjectResources mResources;
private FolderConfiguration config4;
private FolderConfiguration config3;
private FolderConfiguration config2;
//Synthetic comment -- @@ -60,8 +64,16 @@
mDefaultConfig = new FolderConfiguration();
mDefaultConfig.createDefault();

// create the project resources.
        mResources = new ProjectResources(null /*project*/);

// create 2 arrays of IResource. one with the filename being looked up, and one without.
// Since the required API uses IResource, we can use MockFolder for them.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/Mocks.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/Mocks.java
//Synthetic comment -- index b57f3da..65e2144 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
//Synthetic comment -- @@ -145,6 +148,16 @@
return file;
}

/**
* Mock implementation of {@link IProject}.
* <p/>








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/FrameworkResources.java b/sdk_common/src/com/android/ide/common/resources/FrameworkResources.java
//Synthetic comment -- index fbf5926..0e7e58a 100755

//Synthetic comment -- @@ -59,8 +59,8 @@
protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap =
new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    public FrameworkResources() {
        super(true /*isFrameworkRepository*/);
}

/**
//Synthetic comment -- @@ -102,8 +102,8 @@
* @param resFolder The root folder of the resources
* @param logger a logger to report issues to
*/
    public void loadPublicResources(@NonNull IAbstractFolder resFolder, @Nullable ILogger logger) {
        IAbstractFolder valueFolder = resFolder.getFolder(SdkConstants.FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 4f50f63..02c61d1 100755

//Synthetic comment -- @@ -31,7 +31,6 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -59,32 +58,95 @@
*/
public abstract class ResourceRepository {

    protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, Map<String, ResourceItem>> mResourceMap =
new EnumMap<ResourceType, Map<String, ResourceItem>>(
            ResourceType.class);

    private final Map<Map<String, ResourceItem>, Collection<ResourceItem>> mReadOnlyListMap =
new IdentityHashMap<Map<String, ResourceItem>, Collection<ResourceItem>>();

private final boolean mFrameworkRepository;

protected final IntArrayWrapper mWrapper = new IntArrayWrapper(null);

/**
* Makes a resource repository
* @param isFrameworkRepository whether the repository is for framework resources.
*/
    protected ResourceRepository(boolean isFrameworkRepository) {
mFrameworkRepository = isFrameworkRepository;
}

public boolean isFrameworkRepository() {
return mFrameworkRepository;
}

/**
* Adds a Folder Configuration to the project.
* @param type The resource type.
//Synthetic comment -- @@ -140,6 +202,8 @@
@NonNull ResourceFolderType type,
@NonNull IAbstractFolder removedFolder,
@Nullable ScanningContext context) {
// get the list of folders for the resource type.
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -173,6 +237,8 @@
public boolean hasResourceItem(@NonNull String url) {
assert url.startsWith("@") || url.startsWith("?") : url;

int typeEnd = url.indexOf('/', 1);
if (typeEnd != -1) {
int nameBegin = typeEnd + 1;
//Synthetic comment -- @@ -204,6 +270,8 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull ResourceType type, @NonNull String name) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {
//Synthetic comment -- @@ -227,6 +295,8 @@
*/
@NonNull
protected ResourceItem getResourceItem(@NonNull ResourceType type, @NonNull String name) {
// looking for an existing ResourceItem with this type and name
ResourceItem item = findDeclaredResourceItem(type, name);

//Synthetic comment -- @@ -308,6 +378,8 @@
*/
@Nullable
public ResourceFolder processFolder(@NonNull IAbstractFolder folder) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(SdkConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -332,11 +404,15 @@
*/
@Nullable
public List<ResourceFolder> getFolders(@NonNull ResourceFolderType type) {
return mFolderMap.get(type);
}

@NonNull
public List<ResourceType> getAvailableResourceTypes() {
List<ResourceType> list = new ArrayList<ResourceType>();

// For each key, we check if there's a single ResourceType match.
//Synthetic comment -- @@ -381,6 +457,8 @@
*/
@NonNull
public Collection<ResourceItem> getResourceItemsOfType(@NonNull ResourceType type) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map == null) {
//Synthetic comment -- @@ -402,6 +480,8 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(@NonNull ResourceType type) {
Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}
//Synthetic comment -- @@ -413,15 +493,9 @@
*/
@Nullable
public ResourceFolder getResourceFolder(@NonNull IAbstractFolder folder) {
        Collection<List<ResourceFolder>> values = mFolderMap.values();

        if (values.isEmpty()) { // This shouldn't be necessary, but has been observed
            try {
                loadResources(folder.getParentFolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

for (List<ResourceFolder> list : values) {
for (ResourceFolder resFolder : list) {
//Synthetic comment -- @@ -445,6 +519,8 @@
@Nullable
public ResourceFile getMatchingFile(@NonNull String name, @NonNull ResourceFolderType type,
@NonNull FolderConfiguration config) {
// get the folders for the given type
List<ResourceFolder> folders = mFolderMap.get(type);

//Synthetic comment -- @@ -486,6 +562,7 @@
@Nullable
public List<ResourceFile> getSourceFiles(@NonNull ResourceType type, @NonNull String name,
@Nullable FolderConfiguration referenceConfig) {

Collection<ResourceItem> items = getResourceItemsOfType(type);

//Synthetic comment -- @@ -517,6 +594,8 @@
@NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {
return doGetConfiguredResources(referenceConfig);
}

//Synthetic comment -- @@ -530,6 +609,7 @@
@NonNull
protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> map =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -547,6 +627,8 @@
*/
@NonNull
public SortedSet<String> getLanguages() {
SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -569,6 +651,8 @@
*/
@NonNull
public SortedSet<String> getRegions(@NonNull String currentLanguage) {
SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -591,41 +675,17 @@
}

/**
     * Loads the resources from a resource folder.
     * <p/>
     *
     * @param rootFolder The folder to read the resources from. This is the top level
     * resource folder (res/)
     * @throws IOException
*/
    public void loadResources(@NonNull IAbstractFolder rootFolder)
            throws IOException {
        ScanningContext context = new ScanningContext(this);

        IAbstractResource[] files = rootFolder.listMembers();
        for (IAbstractResource file : files) {
            if (file instanceof IAbstractFolder) {
                IAbstractFolder folder = (IAbstractFolder) file;
                ResourceFolder resFolder = processFolder(folder);

                if (resFolder != null) {
                    // now we process the content of the folder
                    IAbstractResource[] children = folder.listMembers();

                    for (IAbstractResource childRes : children) {
                        if (childRes instanceof IAbstractFile) {
                            resFolder.processFile((IAbstractFile) childRes,
                                    ResourceDeltaKind.ADDED, context);
                        }
                    }
                }
            }
        }
}


protected void removeFile(@NonNull Collection<ResourceType> types,
@NonNull ResourceFile file) {
for (ResourceType type : types) {
removeFile(type, file);
}







