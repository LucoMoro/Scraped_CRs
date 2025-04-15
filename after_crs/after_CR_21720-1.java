/*A bit more refactoring around the resource repository.

- Move Configurable in the same package as FolderConfiguration.
- Move ResourceManager.getConfig in FolderConfiguration.
- Move ResourceMaanger.processFolder in ResourceRepository.
- Move ResourceManager.processFile in ResourceFolder.
- Rename some misc methods.

Change-Id:Ifa557c7db22c3fb82327a68f4365c08d152d9cf4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index d432d14..d4ba57f 100644

//Synthetic comment -- @@ -208,7 +208,7 @@

public Pair<ResourceType, String> resolveResourceId(int id) {
if (mProjectRes != null) {
            return mProjectRes.resolveResourceId(id);
}

return null;
//Synthetic comment -- @@ -216,7 +216,7 @@

public String resolveResourceId(int[] id) {
if (mProjectRes != null) {
            return mProjectRes.resolveStyleable(id);
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Configurable.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/Configurable.java
similarity index 84%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Configurable.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/Configurable.java
//Synthetic comment -- index 64dce73..a8d8898 100644

//Synthetic comment -- @@ -14,9 +14,8 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources.configurations;


/**
* An object that is associated with a {@link FolderConfiguration}.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index 15d1ba3..6c69d49 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.AndroidConstants;
import com.android.resources.ResourceFolderType;

import java.util.ArrayList;
//Synthetic comment -- @@ -30,6 +29,16 @@
*/
public final class FolderConfiguration implements Comparable<FolderConfiguration> {

    private final static ResourceQualifier[] DEFAULT_QUALIFIERS;

    static {
        // get the default qualifiers.
        FolderConfiguration defaultConfig = new FolderConfiguration();
        defaultConfig.createDefault();
        DEFAULT_QUALIFIERS = defaultConfig.getQualifiers();
    }


private final ResourceQualifier[] mQualifiers = new ResourceQualifier[INDEX_COUNT];

private final static int INDEX_COUNTRY_CODE       = 0;
//Synthetic comment -- @@ -52,6 +61,45 @@
private final static int INDEX_COUNT              = 17;

/**
     * Creates a {@link FolderConfiguration} matching the folder segments.
     * @param folderSegments The segments of the folder name. The first segments should contain
     * the name of the folder
     * @return a FolderConfiguration object, or null if the folder name isn't valid..
     */
    public static FolderConfiguration getConfig(String[] folderSegments) {
        FolderConfiguration config = new FolderConfiguration();

        // we are going to loop through the segments, and match them with the first
        // available qualifier. If the segment doesn't match we try with the next qualifier.
        // Because the order of the qualifier is fixed, we do not reset the first qualifier
        // after each successful segment.
        // If we run out of qualifier before processing all the segments, we fail.

        int qualifierIndex = 0;
        int qualifierCount = DEFAULT_QUALIFIERS.length;

        for (int i = 1 ; i < folderSegments.length; i++) {
            String seg = folderSegments[i];
            if (seg.length() > 0) {
                while (qualifierIndex < qualifierCount &&
                        DEFAULT_QUALIFIERS[qualifierIndex].checkAndSet(seg, config) == false) {
                    qualifierIndex++;
                }

                // if we reached the end of the qualifier we didn't find a matching qualifier.
                if (qualifierIndex == qualifierCount) {
                    return null;
                }

            } else {
                return null;
            }
        }

        return config;
    }

    /**
* Returns the number of {@link ResourceQualifier} that make up a Folder configuration.
*/
public static int getQualifierCount() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 20f869a..4dd01bb 100644

//Synthetic comment -- @@ -155,10 +155,10 @@

/**
* Resolves a compiled resource id into the resource name and type
     * @param id the resource integer id.
     * @return a {@link Pair} of 2 strings { name, type } or null if the id could not be resolved
*/
    public Pair<ResourceType, String> resolveResourceId(int id) {
if (mResIdValueToNameMap != null) {
return mResIdValueToNameMap.get(id);
}
//Synthetic comment -- @@ -167,9 +167,9 @@
}

/**
     * Resolves a compiled styleable id of type int[] into the styleable name.
*/
    public String resolveStyleable(int[] id) {
if (mStyleableValueToNameMap != null) {
mWrapper.set(id);
return mStyleableValueToNameMap.get(mWrapper);
//Synthetic comment -- @@ -246,11 +246,13 @@

/**
* Sets compiled resource information.
     *
* @param resIdValueToNameMap a map of compiled resource id to resource name.
     *    The map is acquired by the {@link ProjectResources} object.
     * @param styleableValueMap a map of (int[], name) for the styleable information. The map is
     *    acquired by the {@link ProjectResources} object.
* @param resourceValueMap a map of (name, id) for resources of type {@link ResourceType#ID}.
     *    The list is acquired by the {@link ProjectResources} object.
*/
void setCompiledResources(Map<Integer, Pair<ResourceType, String>> resIdValueToNameMap,
Map<IntArrayWrapper, String> styleableValueMap,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index 151830e..048a9ba 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.Configurable;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.io.IAbstractFile;
import com.android.resources.ResourceType;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 3d3cf70..09c98df 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.internal.resources.configurations.Configurable;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
//Synthetic comment -- @@ -25,6 +26,8 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IResourceDelta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//Synthetic comment -- @@ -57,6 +60,52 @@
}

/**
     * Processes a file and adds it to its parent folder resource.
     * @param file the underlying resource file.
     * @param folder the parent of the resource file.
     * @param kind the file change kind.
     * @return the {@link ResourceFile} that was created.
     */
    public ResourceFile processFile(IAbstractFile file, int kind) {
        // look for this file if it's already been created
        ResourceFile resFile = getFile(file);

        if (resFile == null) {
            if (kind != IResourceDelta.REMOVED) {
                // create a ResourceFile for it.

                // check if that's a single or multi resource type folder. For now we define this by
                // the number of possible resource type output by files in the folder. This does
                // not make the difference between several resource types from a single file or
                // the ability to have 2 files in the same folder generating 2 different types of
                // resource. The former is handled by MultiResourceFile properly while we don't
                // handle the latter. If we were to add this behavior we'd have to change this call.
                List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(mType);

                if (types.size() == 1) {
                    resFile = new SingleResourceFile(file, this);
                } else {
                    resFile = new MultiResourceFile(file, this);
                }

                resFile.load();

                // add it to the folder
                addFile(resFile);
            }
        } else {
            if (kind == IResourceDelta.REMOVED) {
                removeFile(resFile);
            } else {
                resFile.update();
            }
        }

        return resFile;
    }


    /**
* Adds a {@link ResourceFile} to the folder.
* @param file The {@link ResourceFile}.
*/
//Synthetic comment -- @@ -140,7 +189,7 @@
* @param file The {@link IAbstractFile} object.
* @return the {@link ResourceFile} or null if no match was found.
*/
    private ResourceFile getFile(IAbstractFile file) {
if (mFiles != null) {
for (ResourceFile f : mFiles) {
if (f.getFile().equals(file)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index f3d872e..dc14e18 100644

//Synthetic comment -- @@ -16,13 +16,10 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFolderListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
//Synthetic comment -- @@ -33,9 +30,7 @@
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;
import com.android.resources.ResourceFolderType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -77,9 +72,6 @@

private final static ResourceManager sThis = new ResourceManager();

/**
* Map associating project resource with project objects.
* <p/><b>All accesses must be inside a synchronized(mMap) block</b>, and do as a little as
//Synthetic comment -- @@ -240,8 +232,8 @@
}
}

                            ResourceFolder newFolder = resources.processFolder(
                                    new IFolderWrapper(folder));
if (newFolder != null) {
notifyListenerOnFolderChange(project, newFolder, kind);
}
//Synthetic comment -- @@ -334,10 +326,8 @@
// REMOVED event for the folder comes first. In this case, the
// folder will have taken care of things.
if (folder != null) {
                            ResourceFile resFile = folder.processFile(
                                    new IFileWrapper(file), kind);
notifyListenerOnFileChange(project, resFile, kind);
}
}
//Synthetic comment -- @@ -461,7 +451,7 @@
for (IAbstractResource file : files) {
if (file instanceof IAbstractFolder) {
IAbstractFolder folder = (IAbstractFolder) file;
                ResourceFolder resFolder = resources.processFolder(folder);

if (resFolder != null) {
// now we process the content of the folder
//Synthetic comment -- @@ -469,7 +459,7 @@

for (IAbstractResource childRes : children) {
if (childRes instanceof IAbstractFile) {
                            resFolder.processFile((IAbstractFile) childRes, IResourceDelta.ADDED);
}
}
}
//Synthetic comment -- @@ -510,8 +500,8 @@
for (IResource res : resources) {
if (res.getType() == IResource.FOLDER) {
IFolder folder = (IFolder)res;
                            ResourceFolder resFolder = projectResources.processFolder(
                                    new IFolderWrapper(folder));

if (resFolder != null) {
// now we process the content of the folder
//Synthetic comment -- @@ -521,7 +511,7 @@
if (fileRes.getType() == IResource.FILE) {
IFile file = (IFile)fileRes;

                                        resFolder.processFile(new IFileWrapper(file),
IResourceDelta.ADDED);
}
}
//Synthetic comment -- @@ -538,119 +528,6 @@
}
}


/**
* Returns true if the path is under /project/res/
//Synthetic comment -- @@ -692,10 +569,6 @@
* Private constructor to enforce singleton design.
*/
private ResourceManager() {
}

// debug only








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
//Synthetic comment -- index 93946cd..b10bf03 100644

//Synthetic comment -- @@ -16,7 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.AndroidConstants;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.Configurable;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
//Synthetic comment -- @@ -66,6 +68,7 @@

protected final IntArrayWrapper mWrapper = new IntArrayWrapper(null);


/**
* Makes a resource repository
* @param isFrameworkRepository whether the repository is for framework resources.
//Synthetic comment -- @@ -85,7 +88,7 @@
* @param folder The workspace folder object.
* @return the {@link ResourceFolder} object associated to this folder.
*/
    private ResourceFolder add(ResourceFolderType type, FolderConfiguration config,
IAbstractFolder folder) {
// get the list for the resource type
List<ResourceFolder> list = mFolderMap.get(type);
//Synthetic comment -- @@ -192,6 +195,29 @@
*/
protected abstract ResourceItem createResourceItem(String name);

    /**
     * Processes a folder and adds it to the list of existing folders.
     * @param folder the folder to process
     * @return the ResourceFolder created from this folder, or null if the process failed.
     */
    public ResourceFolder processFolder(IAbstractFolder folder) {
        // split the name of the folder in segments.
        String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

        // get the enum for the resource type.
        ResourceFolderType type = ResourceFolderType.getTypeByName(folderSegments[0]);

        if (type != null) {
            // get the folder configuration.
            FolderConfiguration config = FolderConfiguration.getConfig(folderSegments);

            if (config != null) {
                return add(type, config, folder);
            }
        }

        return null;
    }

/**
* Returns a list of {@link ResourceFolder} for a specific {@link ResourceFolderType}.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index d98d674..f9748f6 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.TextInputMethodQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.TouchScreenQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.VersionQualifier;
import com.android.resources.Density;
import com.android.resources.DockMode;
import com.android.resources.Keyboard;
//Synthetic comment -- @@ -491,7 +490,7 @@
* @return true if success, or false if the folder name is not a valid name.
*/
public boolean setConfiguration(String[] folderSegments) {
        FolderConfiguration config = FolderConfiguration.getConfig(folderSegments);

if (config == null) {
return false;







