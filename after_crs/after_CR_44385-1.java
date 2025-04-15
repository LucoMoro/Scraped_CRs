/*Lazy loading (and reloading) of project resources.

Change-Id:I4a725d523ae14ba8b487076e230fe9d622d5d281*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 5183658..73d183c 100644

//Synthetic comment -- @@ -323,17 +323,22 @@

// Notify the ResourceManager:
ResourceManager resManager = ResourceManager.getInstance();

if (ResourceManager.isAutoBuilding()) {
                        ProjectResources projectResources = resManager.getProjectResources(project);

IdeScanningContext context = new IdeScanningContext(projectResources,
project, true);

                        boolean wasCleared = projectResources.ensureInitialized();

                        if (!wasCleared) {
                            resManager.processDelta(delta, context);
                        }

// Check whether this project or its dependencies (libraries) have
// resources that need compilation
                        if (wasCleared || context.needsFullAapt()) {
mMustCompileResources = true;

// Must also call markAaptRequested on the project to not just
//Synthetic comment -- @@ -735,6 +740,10 @@

// Also clean up lint
EclipseLintClient.clearMarkers(project);

        // clean the project repo
        ProjectResources res = ResourceManager.getInstance().getProjectResources(project);
        res.clear();
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index ccd1666..68c2257 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.IntArrayWrapper;
//Synthetic comment -- @@ -26,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFolder;
import com.android.resources.ResourceType;
import com.android.util.Pair;

//Synthetic comment -- @@ -48,6 +50,7 @@
*       on the fly.</li>
*</ul>
*/
@SuppressWarnings("deprecation")
public class ProjectResources extends ResourceRepository {
// project resources are defined as 0x7FXX#### where XX is the resource type (layout, drawable,
// etc...). Using FF as the type allows for 255 resource types before we get a collision
//Synthetic comment -- @@ -65,12 +68,18 @@

private final IProject mProject;

    public static ProjectResources create(IProject project) {
        IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);

        return new ProjectResources(project, new IFolderWrapper(resFolder));
    }

/**
* Makes a ProjectResources for a given <var>project</var>.
* @param project the project.
*/
    private ProjectResources(IProject project, IAbstractFolder resFolder) {
        super(resFolder, false /*isFrameworkRepository*/);
mProject = project;
}

//Synthetic comment -- @@ -85,6 +94,7 @@
@NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {
        ensureInitialized();

Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 1e12861..e407b6a 100644

//Synthetic comment -- @@ -49,7 +49,6 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//Synthetic comment -- @@ -151,14 +150,14 @@
/**
* Returns the resources of a project.
* @param project The project
     * @return a ProjectResources object
*/
public ProjectResources getProjectResources(IProject project) {
synchronized (mMap) {
ProjectResources resources = mMap.get(project);

if (resources == null) {
                resources = ProjectResources.create(project);
mMap.put(project, resources);
}

//Synthetic comment -- @@ -253,7 +252,7 @@

// if it doesn't exist, we create it.
if (resources == null) {
                                resources = ProjectResources.create(project);
mMap.put(project, resources);
}
}
//Synthetic comment -- @@ -491,16 +490,11 @@

FolderWrapper frameworkRes = new FolderWrapper(osResourcesPath);
if (frameworkRes.exists()) {
            FrameworkResources resources = new FrameworkResources(frameworkRes);

            resources.loadResources();
            resources.loadPublicResources(AdtPlugin.getDefault());
            return resources;
}

return null;
//Synthetic comment -- @@ -512,62 +506,13 @@
*/
private void createProject(IProject project) {
if (project.isOpen()) {
synchronized (mMap) {
                ProjectResources projectResources = mMap.get(project);
if (projectResources == null) {
                    projectResources = ProjectResources.create(project);
mMap.put(project, projectResources);
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
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -43,7 +44,6 @@
import com.android.ide.common.resources.configuration.TextInputMethodQualifier;
import com.android.ide.common.resources.configuration.TouchScreenQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.tests.SdkTestCase;
//Synthetic comment -- @@ -215,8 +215,13 @@
ResourceRepository framework = ResourceManager.getInstance().loadFrameworkResources(target);

// now load the project resources
        ResourceRepository project = new ResourceRepository(resFolder, false) {
            @Override
            protected ResourceItem createResourceItem(String name) {
                return new ResourceItem(name);
            }

        };

// Create a folder configuration that will be used for the rendering:
FolderConfiguration config = getConfiguration();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttributeInfoTest.java
//Synthetic comment -- index 7ae96a8..cde12e5 100644

//Synthetic comment -- @@ -18,11 +18,15 @@
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.DOT_XML;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.mock.Mocks;
import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;
import com.android.resources.ResourceType;
import com.android.utils.StdLogger;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -219,11 +223,14 @@
}

public void testResourcesExist() throws Exception {
        IAbstractFolder folder = Mocks.createAbstractFolder(
                SdkConstants.FD_RESOURCES, new IAbstractResource[0]);

AttributeInfo info = new AttributeInfo("test", Format.REFERENCE_SET);
        TestResourceRepository projectResources = new TestResourceRepository(folder,false);
projectResources.addResource(ResourceType.STRING, "mystring");
projectResources.addResource(ResourceType.DIMEN, "mydimen");
        TestResourceRepository frameworkResources = new TestResourceRepository(folder, true);
frameworkResources.addResource(ResourceType.LAYOUT, "mylayout");

assertTrue(info.isValid("@string/mystring", null, null));
//Synthetic comment -- @@ -247,8 +254,8 @@
private class TestResourceRepository extends ResourceRepository {
private Multimap<ResourceType, String> mResources = ArrayListMultimap.create();

        protected TestResourceRepository(IAbstractFolder resFolder, boolean isFrameworkRepository) {
            super(resFolder, isFrameworkRepository);
}

void addResource(ResourceType type, String name) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index b45242b..a6da135 100644

//Synthetic comment -- @@ -16,8 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.resources.manager;

import com.android.SdkConstants;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.SingleResourceFile;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -25,6 +27,8 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.ide.eclipse.mock.Mocks;
import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;
import com.android.resources.Keyboard;
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
//Synthetic comment -- @@ -46,7 +50,7 @@
private static final String MISC2_FILENAME = "bar.xml"; //$NON-NLS-1$

private FolderConfiguration mDefaultConfig;
    private ResourceRepository mResources;
private FolderConfiguration config4;
private FolderConfiguration config3;
private FolderConfiguration config2;
//Synthetic comment -- @@ -60,8 +64,16 @@
mDefaultConfig = new FolderConfiguration();
mDefaultConfig.createDefault();

        IAbstractFolder folder = Mocks.createAbstractFolder(
                SdkConstants.FD_RESOURCES, new IAbstractResource[0]);

// create the project resources.
        mResources = new ResourceRepository(folder, false) {
            @Override
            protected ResourceItem createResourceItem(String name) {
                return new ResourceItem(name);
            }
        };

// create 2 arrays of IResource. one with the filename being looked up, and one without.
// Since the required API uses IResource, we can use MockFolder for them.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/Mocks.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/Mocks.java
//Synthetic comment -- index b57f3da..9a44210 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
//Synthetic comment -- @@ -145,6 +148,17 @@
return file;
}

    public static IAbstractFolder createAbstractFolder(String name, IAbstractResource[] members) {
        //mock res folder
        IAbstractFolder folder = createNiceMock(IAbstractFolder.class);
        expect(folder.getName()).andReturn(name).anyTimes();
        // expect(file.getLocation()).andReturn(new Path(name)).anyTimes();
        expect(folder.listMembers()).andReturn(members).anyTimes();
        replay(folder);

        return folder;
    }

/**
* Mock implementation of {@link IProject}.
* <p/>








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/FrameworkResources.java b/sdk_common/src/com/android/ide/common/resources/FrameworkResources.java
//Synthetic comment -- index fbf5926..0e7e58a 100755

//Synthetic comment -- @@ -59,8 +59,8 @@
protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap =
new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    public FrameworkResources(@NonNull IAbstractFolder resFolder) {
        super(resFolder, true /*isFrameworkRepository*/);
}

/**
//Synthetic comment -- @@ -102,8 +102,8 @@
* @param resFolder The root folder of the resources
* @param logger a logger to report issues to
*/
    public void loadPublicResources(@Nullable ILogger logger) {
        IAbstractFolder valueFolder = getResFolder().getFolder(SdkConstants.FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 4f50f63..75d5f41 100755

//Synthetic comment -- @@ -31,7 +31,6 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -59,6 +58,8 @@
*/
public abstract class ResourceRepository {

    private final IAbstractFolder mResourceFolder;

protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

//Synthetic comment -- @@ -70,21 +71,77 @@
new IdentityHashMap<Map<String, ResourceItem>, Collection<ResourceItem>>();

private final boolean mFrameworkRepository;
    private boolean mCleared = true;
    private boolean mInitializing = false;

protected final IntArrayWrapper mWrapper = new IntArrayWrapper(null);

/**
* Makes a resource repository
     * @param resFolder the resource folder of the repository.
* @param isFrameworkRepository whether the repository is for framework resources.
*/
    protected ResourceRepository(@NonNull IAbstractFolder resFolder,
            boolean isFrameworkRepository) {
        mResourceFolder = resFolder;
mFrameworkRepository = isFrameworkRepository;
}

    public IAbstractFolder getResFolder() {
        return mResourceFolder;
    }

public boolean isFrameworkRepository() {
return mFrameworkRepository;
}

    public void clear() {
        mCleared = true;
        mFolderMap.clear();
        mResourceMap.clear();
        mReadOnlyListMap.clear();
    }

    /**
     * Ensures that the repository has been initialized again after a call to
     * {@link ResourceRepository#clear()}
     *
     * @return true if the repository was just re-initialized.
     */
    public boolean ensureInitialized() {
        if (mCleared && !mInitializing) {
            ScanningContext context = new ScanningContext(this);
            mInitializing = true;

            IAbstractResource[] resources = mResourceFolder.listMembers();

            for (IAbstractResource res : resources) {
                if (res instanceof IAbstractFolder) {
                    IAbstractFolder folder = (IAbstractFolder)res;
                    ResourceFolder resFolder = processFolder(folder);

                    if (resFolder != null) {
                        // now we process the content of the folder
                        IAbstractResource[] files = folder.listMembers();

                        for (IAbstractResource fileRes : files) {
                            if (fileRes instanceof IAbstractFile) {
                                IAbstractFile file = (IAbstractFile)fileRes;

                                resFolder.processFile(file, ResourceDeltaKind.ADDED, context);
                            }
                        }
                    }
                }
            }
            mInitializing = false;
            mCleared = false;
            return true;
        }

        return false;
    }

/**
* Adds a Folder Configuration to the project.
* @param type The resource type.
//Synthetic comment -- @@ -140,6 +197,8 @@
@NonNull ResourceFolderType type,
@NonNull IAbstractFolder removedFolder,
@Nullable ScanningContext context) {
        ensureInitialized();

// get the list of folders for the resource type.
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -173,6 +232,8 @@
public boolean hasResourceItem(@NonNull String url) {
assert url.startsWith("@") || url.startsWith("?") : url;

        ensureInitialized();

int typeEnd = url.indexOf('/', 1);
if (typeEnd != -1) {
int nameBegin = typeEnd + 1;
//Synthetic comment -- @@ -204,6 +265,8 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull ResourceType type, @NonNull String name) {
        ensureInitialized();

Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {
//Synthetic comment -- @@ -227,6 +290,8 @@
*/
@NonNull
protected ResourceItem getResourceItem(@NonNull ResourceType type, @NonNull String name) {
        ensureInitialized();

// looking for an existing ResourceItem with this type and name
ResourceItem item = findDeclaredResourceItem(type, name);

//Synthetic comment -- @@ -308,6 +373,8 @@
*/
@Nullable
public ResourceFolder processFolder(@NonNull IAbstractFolder folder) {
        ensureInitialized();

// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(SdkConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -332,11 +399,15 @@
*/
@Nullable
public List<ResourceFolder> getFolders(@NonNull ResourceFolderType type) {
        ensureInitialized();

return mFolderMap.get(type);
}

@NonNull
public List<ResourceType> getAvailableResourceTypes() {
        ensureInitialized();

List<ResourceType> list = new ArrayList<ResourceType>();

// For each key, we check if there's a single ResourceType match.
//Synthetic comment -- @@ -381,6 +452,8 @@
*/
@NonNull
public Collection<ResourceItem> getResourceItemsOfType(@NonNull ResourceType type) {
        ensureInitialized();

Map<String, ResourceItem> map = mResourceMap.get(type);

if (map == null) {
//Synthetic comment -- @@ -402,6 +475,8 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(@NonNull ResourceType type) {
        ensureInitialized();

Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}
//Synthetic comment -- @@ -413,15 +488,9 @@
*/
@Nullable
public ResourceFolder getResourceFolder(@NonNull IAbstractFolder folder) {
        ensureInitialized();

        Collection<List<ResourceFolder>> values = mFolderMap.values();

for (List<ResourceFolder> list : values) {
for (ResourceFolder resFolder : list) {
//Synthetic comment -- @@ -445,6 +514,8 @@
@Nullable
public ResourceFile getMatchingFile(@NonNull String name, @NonNull ResourceFolderType type,
@NonNull FolderConfiguration config) {
        ensureInitialized();

// get the folders for the given type
List<ResourceFolder> folders = mFolderMap.get(type);

//Synthetic comment -- @@ -486,6 +557,7 @@
@Nullable
public List<ResourceFile> getSourceFiles(@NonNull ResourceType type, @NonNull String name,
@Nullable FolderConfiguration referenceConfig) {
        ensureInitialized();

Collection<ResourceItem> items = getResourceItemsOfType(type);

//Synthetic comment -- @@ -517,6 +589,8 @@
@NonNull
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {
        ensureInitialized();

return doGetConfiguredResources(referenceConfig);
}

//Synthetic comment -- @@ -530,6 +604,7 @@
@NonNull
protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
@NonNull FolderConfiguration referenceConfig) {
        ensureInitialized();

Map<ResourceType, Map<String, ResourceValue>> map =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -547,6 +622,8 @@
*/
@NonNull
public SortedSet<String> getLanguages() {
        ensureInitialized();

SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -569,6 +646,8 @@
*/
@NonNull
public SortedSet<String> getRegions(@NonNull String currentLanguage) {
        ensureInitialized();

SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -591,41 +670,17 @@
}

/**
     * Loads the resources.
*/
    public void loadResources() {
        clear();
        ensureInitialized();
}

protected void removeFile(@NonNull Collection<ResourceType> types,
@NonNull ResourceFile file) {
        ensureInitialized();

for (ResourceType type : types) {
removeFile(type, file);
}







