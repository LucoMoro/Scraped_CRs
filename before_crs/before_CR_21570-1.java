/*Resource refactoring and clean-up.

Change-Id:Ie3ac1995213fed66153c7e7ecbdd170ec257be62*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index d82774d..180bff1 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
//Synthetic comment -- @@ -209,9 +210,9 @@
*/
void onRenderingTargetPostChange(IAndroidTarget target);

        ProjectResources getProjectResources();
        ProjectResources getFrameworkResources();
        ProjectResources getFrameworkResources(IAndroidTarget target);
Map<ResourceType, Map<String, ResourceValue>> getConfiguredProjectResources();
Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources();
}
//Synthetic comment -- @@ -1119,12 +1120,12 @@
boolean hasLocale = false;

// get the languages from the project.
            ProjectResources project = mListener.getProjectResources();

// in cases where the opened file is not linked to a project, this could be null.
            if (project != null) {
// now get the languages from the project.
                languages = project.getLanguages();

for (String language : languages) {
hasLocale = true;
//Synthetic comment -- @@ -1132,7 +1133,7 @@
LanguageQualifier langQual = new LanguageQualifier(language);

// find the matching regions and add them
                    SortedSet<String> regions = project.getRegions(language);
for (String region : regions) {
mLocaleCombo.add(
String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
//Synthetic comment -- @@ -1223,7 +1224,7 @@
return; // can't do anything w/o it.
}

        ProjectResources frameworkProject = mListener.getFrameworkResources(getRenderingTarget());

mDisableUpdates++;

//Synthetic comment -- @@ -1235,10 +1236,10 @@
ArrayList<String> themes = new ArrayList<String>();

// get the themes, and languages from the Framework.
            if (frameworkProject != null) {
// get the configured resources for the framework
Map<ResourceType, Map<String, ResourceValue>> frameworResources =
                    frameworkProject.getConfiguredResources(getCurrentConfig());

if (frameworResources != null) {
// get the styles.
//Synthetic comment -- @@ -1267,9 +1268,9 @@
}

// now get the themes and languages from the project.
            ProjectResources project = mListener.getProjectResources();
// in cases where the opened file is not linked to a project, this could be null.
            if (project != null) {
// get the configured resources for the project
Map<ResourceType, Map<String, ResourceValue>> configuredProjectRes =
mListener.getConfiguredProjectResources();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 88711e2..e199a4b 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -541,7 +542,7 @@

public Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
                ProjectResources frameworkRes = getFrameworkResources();

if (frameworkRes == null) {
AdtPlugin.log(IStatus.ERROR, "Failed to get ProjectResource for the framework");
//Synthetic comment -- @@ -575,7 +576,7 @@
* configuration selection.
* @return the framework resources or null if not found.
*/
        public ProjectResources getFrameworkResources() {
return getFrameworkResources(getRenderingTarget());
}

//Synthetic comment -- @@ -585,7 +586,7 @@
* @param target the target for which to return the framework resources.
* @return the framework resources or null if not found.
*/
        public ProjectResources getFrameworkResources(IAndroidTarget target) {
if (target != null) {
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index f9e103a..248af89 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -396,8 +396,8 @@
private void scanProject() {
ProjectResources resources = ResourceManager.getInstance().getProjectResources(mProject);
if (resources != null) {
            ProjectResourceItem[] layouts = resources.getResources(ResourceType.LAYOUT);
            for (ProjectResourceItem layout : layouts) {
List<ResourceFile> sources = layout.getSourceFileList();
for (ResourceFile source : sources) {
updateFileIncludes(source, false);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ce299e4..7a1e06e 100755

//Synthetic comment -- @@ -38,8 +38,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -852,7 +852,7 @@
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
                IResourceRepository projectRepository =
ResourceManager.getInstance().getProjectResources(project);
Shell shell = AdtPlugin.getDisplay().getActiveShell();
if (shell == null) {
//Synthetic comment -- @@ -879,7 +879,7 @@
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (project != null) {
// get the resource repository for this project and the system resources.
                IResourceRepository projectRepository = ResourceManager.getInstance()
.getProjectResources(project);
Shell shell = AdtPlugin.getDisplay().getActiveShell();
if (shell == null) {
//Synthetic comment -- @@ -887,7 +887,7 @@
}

AndroidTargetData data = editor.getTargetData();
                IResourceRepository systemRepository = data.getSystemResources();

// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 43c8fcd..7884163 100644

//Synthetic comment -- @@ -21,9 +21,9 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
//Synthetic comment -- @@ -126,19 +126,19 @@
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
            IResourceRepository projectRepository =
ResourceManager.getInstance().getProjectResources(project);

if (mType != null) {
// get the Target Data to get the system resources
AndroidTargetData data = editor.getTargetData();
                IResourceRepository systemRepository = data.getSystemResources();

// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project,
mType,
projectRepository,
                        systemRepository,
shell);

dlg.setCurrentResource(currentValue);
//Synthetic comment -- @@ -194,7 +194,7 @@
*/
@Override
public String[] getPossibleValues(String prefix) {
        IResourceRepository repository = null;
boolean isSystem = false;

UiElementNode uiNode = getUiParent();
//Synthetic comment -- @@ -210,7 +210,7 @@
// If there's a prefix with "android:" in it, use the system resources
// Non-public framework resources are filtered out later.
AndroidTargetData data = editor.getTargetData();
            repository = data.getSystemResources();
isSystem = true;
}

//Synthetic comment -- @@ -263,18 +263,8 @@
sb.append(typeName).append('/');
String base = sb.toString();

                if (isSystem) {
                    AndroidTargetData targetData = editor.getTargetData();
                    for (ResourceItem item : repository.getResources(resType)) {
                        String name = item.getName();
                        if (targetData == null || targetData.isPublicResource(resType, name)) {
                            results.add(base + name);
                        }
                    }
                } else {
                    for (ResourceItem item : repository.getResources(resType)) {
                        results.add(base + item.getName());
                    }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index efd95e2..5a2f39f 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -790,7 +791,7 @@
}

/** Return either the project resources or the framework resources (or null) */
    private static ProjectResources getResources(IProject project, boolean framework) {
if (framework) {
IAndroidTarget target = getTarget(project);
if (target == null) {
//Synthetic comment -- @@ -822,7 +823,7 @@
}

// Look in the configuration folder: Search compatible configurations
        ProjectResources resources = getResources(project, false /* isFramework */);
FolderConfiguration configuration = getConfiguration();
if (configuration != null) { // Not the case when searching from Java files for example
List<ResourceFolder> folders = resources.getFolders(ResourceFolderType.LAYOUT);
//Synthetic comment -- @@ -1128,7 +1129,7 @@

boolean isFramework = url.startsWith("@android"); //$NON-NLS-1$

        ProjectResources resources = getResources(project, isFramework);
if (resources == null) {
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IIdResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IIdResourceItem.java
deleted file mode 100644
//Synthetic comment -- index acc4cf2..0000000

//Synthetic comment -- @@ -1,30 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources;

import com.android.resources.ResourceType;

/**
 * Classes which implements this interface provides a method indicating the state of a resource of
 * type {@link ResourceType#ID}.
 */
public interface IIdResourceItem {
    /**
     * Returns whether the ID resource has been declared inline inside another resource XML file. 
     */
    public boolean isDeclaredInline();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IResourceRepository.java
deleted file mode 100644
//Synthetic comment -- index 1abd9eb..0000000

//Synthetic comment -- @@ -1,49 +0,0 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources;

import com.android.resources.ResourceType;

/**
 * A repository of resources. This allows access to the resource by {@link ResourceType}.
 */
public interface IResourceRepository {

    /**
     * Returns the present {@link ResourceType}s in the project.
     * @return an array containing all the type of resources existing in the project.
     */
    public abstract ResourceType[] getAvailableResourceTypes();

    /**
     * Returns an array of the existing resource for the specified type.
     * @param type the type of the resources to return
     */
    public abstract ResourceItem[] getResources(ResourceType type);

    /**
     * Returns whether resources of the specified type are present.
     * @param type the type of the resources to check.
     */
    public abstract boolean hasResources(ResourceType type);
    
    /**
     * Returns whether the repository is a system repository.
     */
    public abstract boolean isSystemRepository();

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 327bd89..0bb0d26 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.resources.ResourceType;


//Synthetic comment -- @@ -29,9 +30,8 @@
*/
public static String getXmlString(ResourceType type, ResourceItem resourceItem,
boolean system) {
        if (type == ResourceType.ID && resourceItem instanceof IIdResourceItem) {
            IIdResourceItem idResource = (IIdResourceItem)resourceItem;
            if (idResource.isDeclaredInline()) {
return (system?"@android:":"@+") + type.getName() + "/" + resourceItem.getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java
deleted file mode 100644
//Synthetic comment -- index c340ffe..0000000

//Synthetic comment -- @@ -1,48 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources;

/**
 * Base class representing a Resource Item, as returned by a {@link IResourceRepository}.
 */
public class ResourceItem implements Comparable<ResourceItem> {
    
    private final String mName;
    
    /**
     * Constructs a new ResourceItem
     * @param name the name of the resource as it appears in the XML and R.java files.
     */
    public ResourceItem(String name) {
        mName = name;
    }

    /**
     * Returns the name of the resource item.
     */
    public final String getName() {
        return mName;
    }

    /**
     * Compares the {@link ResourceItem} to another.
     * @param other the ResourceItem to be compared to.
     */
    public int compareTo(ResourceItem other) {
        return mName.compareTo(other.mName);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 543719b..2d62c3a 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.FolderTypeRelationship;
//Synthetic comment -- @@ -160,8 +160,8 @@
Set<String> existing = new HashSet<String>();
ResourceManager manager = ResourceManager.getInstance();
ProjectResources projectResources = manager.getProjectResources(project);
        ProjectResourceItem[] resources = projectResources.getResources(type);
        for (ProjectResourceItem resource : resources) {
existing.add(resource.getName());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 2a998f8..0000000

//Synthetic comment -- @@ -1,82 +0,0 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources.manager;

/**
 * Represents a resource item that can exist in multiple "alternate" versions. 
 */
public class ConfigurableResourceItem extends ProjectResourceItem {
    
    /**
     * Constructs a new Resource Item.
     * @param name the name of the resource as it appears in the XML and R.java files.
     */
    public ConfigurableResourceItem(String name) {
        super(name);
    }

    /**
     * Returns if the resource item has at least one non-default configuration.
     */
    public boolean hasAlternates() {
        for (ResourceFile file : mFiles) {
            if (file.getFolder().getConfiguration().isDefault() == false) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Returns whether the resource has a default version, with no qualifier.
     */
    public boolean hasDefault() {
        for (ResourceFile file : mFiles) {
            if (file.getFolder().getConfiguration().isDefault()) {
                return true;
            }
        }
        
        // We only want to return false if there's no default and more than 0 items.
        return (mFiles.size() == 0);
    }

    /**
     * Returns the number of alternate versions of this resource.
     */
    public int getAlternateCount() {
        int count = 0;
        for (ResourceFile file : mFiles) {
            if (file.getFolder().getConfiguration().isDefault() == false) {
                count++;
            }
        }

        return count;
    }

    /*
     * (non-Javadoc)
     * Returns whether the item can be edited directly (ie it does not have alternate versions).
     */
    @Override
    public boolean isEditableDirectly() {
        return hasAlternates() == false;
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..6a10786

//Synthetic comment -- @@ -0,0 +1,29 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java
new file mode 100644
//Synthetic comment -- index 0000000..88c3b8d

//Synthetic comment -- @@ -0,0 +1,179 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 8b142fb..0000000

//Synthetic comment -- @@ -1,54 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.IIdResourceItem;
import com.android.resources.ResourceType;

/**
 * Represents a resource item of type {@link ResourceType#ID}
 */
public class IdResourceItem extends ProjectResourceItem implements IIdResourceItem {

    private final boolean mIsDeclaredInline;

    /**
     * Constructs a new ResourceItem.
     * @param name the name of the resource as it appears in the XML and R.java files.
     * @param isDeclaredInline Whether this id was declared inline.
     */
    IdResourceItem(String name, boolean isDeclaredInline) {
        super(name);
        mIsDeclaredInline = isDeclaredInline;
    }

    /*
     * (non-Javadoc)
     * Returns whether the ID resource has been declared inline inside another resource XML file. 
     */
    public boolean isDeclaredInline() {
        return mIsDeclaredInline;
    }

    /* (non-Javadoc)
     * Returns whether the item can be edited (ie, the id was not declared inline).
     */
    @Override
    public boolean isEditableDirectly() {
        return !mIsDeclaredInline;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..802dec5

//Synthetic comment -- @@ -0,0 +1,45 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index 8f8e0d3..bf4b40c 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.io.IAbstractFile;
import com.android.io.StreamException;
import com.android.resources.ResourceType;

import org.xml.sax.SAXException;

//Synthetic comment -- @@ -64,8 +65,7 @@

if (mResourceTypeList == null) {
Set<ResourceType> keys = mResourceItems.keySet();
            mResourceTypeList = new ArrayList<ResourceType>();
            mResourceTypeList.addAll(keys);
mResourceTypeList = Collections.unmodifiableList(mResourceTypeList);
}

//Synthetic comment -- @@ -81,28 +81,29 @@
}

@Override
    public Collection<ProjectResourceItem> getResources(ResourceType type,
            ProjectResources projectResources) {
update();

HashMap<String, ResourceValue> list = mResourceItems.get(type);

        ArrayList<ProjectResourceItem> items = new ArrayList<ProjectResourceItem>();

if (list != null) {
Collection<ResourceValue> values = list.values();
for (ResourceValue res : values) {
                ProjectResourceItem item = projectResources.findResourceItem(type, res.getName());

                if (item == null) {
                    if (type == ResourceType.ID) {
                        item = new IdResourceItem(res.getName(), false /* isDeclaredInline */);
                    } else {
                        item = new ConfigurableResourceItem(res.getName());
                    }
items.add(item);
}

item.add(this);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 845a974..0000000

//Synthetic comment -- @@ -1,91 +0,0 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Base class for Resource Item coming from an Android Project.
 */
public abstract class ProjectResourceItem extends ResourceItem {

    private final static Comparator<ResourceFile> sComparator = new Comparator<ResourceFile>() {
        public int compare(ResourceFile file1, ResourceFile file2) {
            // get both FolderConfiguration and compare them
            FolderConfiguration fc1 = file1.getFolder().getConfiguration();
            FolderConfiguration fc2 = file2.getFolder().getConfiguration();
            
            return fc1.compareTo(fc2);
        }
    };

    /**
     * List of files generating this ResourceItem.
     */
    protected final ArrayList<ResourceFile> mFiles = new ArrayList<ResourceFile>();

    /**
     * Constructs a new ResourceItem.
     * @param name the name of the resource as it appears in the XML and R.java files.
     */
    public ProjectResourceItem(String name) {
        super(name);
    }
    
    /**
     * Returns whether the resource item is editable directly.
     * <p/>
     * This is typically the case for resources that don't have alternate versions, or resources
     * of type {@link ResourceType#ID} that aren't declared inline.
     */
    public abstract boolean isEditableDirectly();

    /**
     * Adds a new version of this resource item, by adding its {@link ResourceFile}.
     * @param file the {@link ResourceFile} object.
     */
    protected void add(ResourceFile file) {
        mFiles.add(file);
    }
    
    /**
     * Reset the item by emptying its version list.
     */
    protected void reset() {
        mFiles.clear();
    }
    
    /**
     * Returns the sorted list of {@link ResourceItem} objects for this resource item.
     */
    public ResourceFile[] getSourceFileArray() {
        ArrayList<ResourceFile> list = new ArrayList<ResourceFile>();
        list.addAll(mFiles);
        
        Collections.sort(list, sComparator);
        
        return list.toArray(new ResourceFile[list.size()]);
    }
    
    /**
     * Returns the list of {@link ResourceItem} objects for this resource item.
     */
    public List<ResourceFile> getSourceFileList() {
        return Collections.unmodifiableList(mFiles);
    }
    

    /**
     * Replaces the content of the receiver with the ResourceItem received as parameter.
     * @param item
     */
    protected void replaceWith(ProjectResourceItem item) {
        mFiles.clear();
        mFiles.addAll(item.mFiles);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index b7f406d..020a530 100644

//Synthetic comment -- @@ -17,51 +17,31 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFolder;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
* Represents the resources of a project. This is a file view of the resources, with handling
* for the alternate resource types. For a compiled view use CompiledResources.
*/
public class ProjectResources implements IResourceRepository {
private final static int DYNAMIC_ID_SEED_START = 0; // this should not conflict with any
// project IDs that start at a much higher
// value

    private final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
        new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    private final Map<ResourceType, List<ProjectResourceItem>> mResourceMap =
        new EnumMap<ResourceType, List<ProjectResourceItem>>(ResourceType.class);

/** Map of (name, id) for resources of type {@link ResourceType#ID} coming from R.java */
private Map<ResourceType, Map<String, Integer>> mResourceValueMap;
/** Map of (id, [name, resType]) for all resources coming from R.java */
//Synthetic comment -- @@ -69,352 +49,36 @@
/** Map of (int[], name) for styleable resources coming from R.java */
private Map<IntArrayWrapper, String> mStyleableValueToNameMap;

private final Map<String, Integer> mDynamicIds = new HashMap<String, Integer>();
private int mDynamicSeed = DYNAMIC_ID_SEED_START;

    /** Cached list of {@link IdResourceItem}. This is mix of IdResourceItem created by
     * {@link MultiResourceFile} for ids coming from XML files under res/values and
     * {@link IdResourceItem} created manually, from the list coming from R.java */
    private final List<IdResourceItem> mIdResourceList = new ArrayList<IdResourceItem>();

    private final boolean mIsFrameworkRepository;
private final IProject mProject;

    private final IntArrayWrapper mWrapper = new IntArrayWrapper(null);


/**
* Makes a ProjectResources for a given <var>project</var>.
* @param project the project.
*/
public ProjectResources(IProject project) {
        mIsFrameworkRepository = false;
mProject = project;
}

/**
     * Makes a ProjectResource for a framework repository.
     *
     * @see #isSystemRepository()
     */
    public ProjectResources() {
        mIsFrameworkRepository = true;
        mProject = null;
    }

    /**
     * Returns whether this ProjectResources is for a project or for a framework.
     */
    public boolean isSystemRepository() {
        return mIsFrameworkRepository;
    }

    /**
     * Adds a Folder Configuration to the project.
     * @param type The resource type.
     * @param config The resource configuration.
     * @param folder The workspace folder object.
     * @return the {@link ResourceFolder} object associated to this folder.
     */
    protected ResourceFolder add(ResourceFolderType type, FolderConfiguration config,
            IAbstractFolder folder) {
        // get the list for the resource type
        List<ResourceFolder> list = mFolderMap.get(type);

        if (list == null) {
            list = new ArrayList<ResourceFolder>();

            ResourceFolder cf = new ResourceFolder(type, config, folder, mIsFrameworkRepository);
            list.add(cf);

            mFolderMap.put(type, list);

            return cf;
        }

        // look for an already existing folder configuration.
        for (ResourceFolder cFolder : list) {
            if (cFolder.mConfiguration.equals(config)) {
                // config already exist. Nothing to be done really, besides making sure
                // the IFolder object is up to date.
                cFolder.mFolder = folder;
                return cFolder;
            }
        }

        // If we arrive here, this means we didn't find a matching configuration.
        // So we add one.
        ResourceFolder cf = new ResourceFolder(type, config, folder, mIsFrameworkRepository);
        list.add(cf);

        return cf;
    }

    /**
     * Removes a {@link ResourceFolder} associated with the specified {@link IAbstractFolder}.
     * @param type The type of the folder
     * @param folder the IFolder object.
     * @return the {@link ResourceFolder} that was removed, or null if no matches were found.
     */
    protected ResourceFolder removeFolder(ResourceFolderType type, IFolder folder) {
        // get the list of folders for the resource type.
        List<ResourceFolder> list = mFolderMap.get(type);

        if (list != null) {
            int count = list.size();
            for (int i = 0 ; i < count ; i++) {
                ResourceFolder resFolder = list.get(i);
                // this is only used for Eclipse stuff so we know it's an IFolderWrapper
                IFolderWrapper wrapper = (IFolderWrapper) resFolder.getFolder();
                if (wrapper.getIFolder().equals(folder)) {
                    // we found the matching ResourceFolder. we need to remove it.
                    list.remove(i);

                    // we now need to invalidate this resource type.
                    // The easiest way is to touch one of the other folders of the same type.
                    if (list.size() > 0) {
                        list.get(0).touch();
                    } else {
                        // if the list is now empty, and we have a single ResouceType out of this
                        // ResourceFolderType, then we are done.
                        // However, if another ResourceFolderType can generate similar ResourceType
                        // than this, we need to update those ResourceTypes as well.
                        // For instance, if the last "drawable-*" folder is deleted, we need to
                        // refresh the ResourceItem associated with ResourceType.DRAWABLE.
                        // Those can be found in ResourceFolderType.DRAWABLE but also in
                        // ResourceFolderType.VALUES.
                        // If we don't find a single folder to touch, then it's fine, as the top
                        // level items (the list of generated resource types) is not cached
                        // (for now)

                        // get the lists of ResourceTypes generated by this ResourceFolderType
                        List<ResourceType> resTypes =
                            FolderTypeRelationship.getRelatedResourceTypes(type);

                        // for each of those, make sure to find one folder to touch so that the
                        // list of ResourceItem associated with the type is rebuilt.
                        for (ResourceType resType : resTypes) {
                            // get the list of folder that can generate this type
                            List<ResourceFolderType> folderTypes =
                                FolderTypeRelationship.getRelatedFolders(resType);

                            // we only need to touch one folder in any of those (since it's one
                            // folder per type, not per folder type).
                            for (ResourceFolderType folderType : folderTypes) {
                                List<ResourceFolder> resFolders = mFolderMap.get(folderType);

                                if (resFolders != null && resFolders.size() > 0) {
                                    resFolders.get(0).touch();
                                    break;
                                }
                            }
                        }
                    }

                    // we're done updating/touching, we can stop
                    return resFolder;
                }
            }
        }

        return null;
    }


    /**
     * Returns a list of {@link ResourceFolder} for a specific {@link ResourceFolderType}.
     * @param type The {@link ResourceFolderType}
     */
    public List<ResourceFolder> getFolders(ResourceFolderType type) {
        return mFolderMap.get(type);
    }

    /* (non-Javadoc)
     * @see com.android.ide.eclipse.editors.resources.IResourceRepository#getAvailableResourceTypes()
     */
    public ResourceType[] getAvailableResourceTypes() {
        ArrayList<ResourceType> list = new ArrayList<ResourceType>();

        // For each key, we check if there's a single ResourceType match.
        // If not, we look for the actual content to give us the resource type.

        for (ResourceFolderType folderType : mFolderMap.keySet()) {
            List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(folderType);
            if (types.size() == 1) {
                // before we add it we check if it's not already present, since a ResourceType
                // could be created from multiple folders, even for the folders that only create
                // one type of resource (drawable for instance, can be created from drawable/ and
                // values/)
                if (list.indexOf(types.get(0)) == -1) {
                    list.add(types.get(0));
                }
            } else {
                // there isn't a single resource type out of this folder, so we look for all
                // content.
                List<ResourceFolder> folders = mFolderMap.get(folderType);
                if (folders != null) {
                    for (ResourceFolder folder : folders) {
                        Collection<ResourceType> folderContent = folder.getResourceTypes();

                        // then we add them, but only if they aren't already in the list.
                        for (ResourceType folderResType : folderContent) {
                            if (list.indexOf(folderResType) == -1) {
                                list.add(folderResType);
                            }
                        }
                    }
                }
            }
        }

        // in case ResourceType.ID haven't been added yet because there's no id defined
        // in XML, we check on the list of compiled id resources.
        if (list.indexOf(ResourceType.ID) == -1 && mResourceValueMap != null) {
            Map<String, Integer> map = mResourceValueMap.get(ResourceType.ID);
            if (map != null && map.size() > 0) {
                list.add(ResourceType.ID);
            }
        }

        // at this point the list is full of ResourceType defined in the files.
        // We need to sort it.
        Collections.sort(list);

        return list.toArray(new ResourceType[list.size()]);
    }

    /* (non-Javadoc)
     * @see com.android.ide.eclipse.editors.resources.IResourceRepository#getResources(com.android.ide.eclipse.common.resources.ResourceType)
     */
    public ProjectResourceItem[] getResources(ResourceType type) {
        checkAndUpdate(type);

        if (type == ResourceType.ID) {
            synchronized (mIdResourceList) {
                return mIdResourceList.toArray(new ProjectResourceItem[mIdResourceList.size()]);
            }
        }

        List<ProjectResourceItem> items = mResourceMap.get(type);

        return items.toArray(new ProjectResourceItem[items.size()]);
    }

    /* (non-Javadoc)
     * @see com.android.ide.eclipse.editors.resources.IResourceRepository#hasResources(com.android.ide.eclipse.common.resources.ResourceType)
     */
    public boolean hasResources(ResourceType type) {
        checkAndUpdate(type);

        if (type == ResourceType.ID) {
            synchronized (mIdResourceList) {
                return mIdResourceList.size() > 0;
            }
        }

        List<ProjectResourceItem> items = mResourceMap.get(type);
        return (items != null && items.size() > 0);
    }

    /**
     * Returns the {@link ResourceFolder} associated with a {@link IFolder}.
     * @param folder The {@link IFolder} object.
     * @return the {@link ResourceFolder} or null if it was not found.
     */
    public ResourceFolder getResourceFolder(IFolder folder) {
        for (List<ResourceFolder> list : mFolderMap.values()) {
            for (ResourceFolder resFolder : list) {
                // this is only used for Eclipse stuff so we know it's an IFolderWrapper
                IFolderWrapper wrapper = (IFolderWrapper) resFolder.getFolder();
                if (wrapper.getIFolder().equals(folder)) {
                    return resFolder;
                }
            }
        }

        return null;
    }

    /**
     * Returns the {@link ResourceFile} matching the given name, {@link ResourceFolderType} and
     * configuration.
     * <p/>This only works with files generating one resource named after the file (for instance,
     * layouts, bitmap based drawable, xml, anims).
     * @return the matching file or <code>null</code> if no match was found.
     */
    public ResourceFile getMatchingFile(String name, ResourceFolderType type,
            FolderConfiguration config) {
        // get the folders for the given type
        List<ResourceFolder> folders = mFolderMap.get(type);

        // look for folders containing a file with the given name.
        ArrayList<ResourceFolder> matchingFolders = new ArrayList<ResourceFolder>();

        // remove the folders that do not have a file with the given name.
        for (int i = 0 ; i < folders.size(); i++) {
            ResourceFolder folder = folders.get(i);

            if (folder.hasFile(name) == true) {
                matchingFolders.add(folder);
            }
        }

        // from those, get the folder with a config matching the given reference configuration.
        Resource match = findMatchingConfiguredResource(matchingFolders, config);

        // do we have a matching folder?
        if (match instanceof ResourceFolder) {
            // get the ResourceFile from the filename
            return ((ResourceFolder)match).getFile(name);
        }

        return null;
    }

    /**
     * Returns the list of source files for a given resource.
     * Optionally, if a {@link FolderConfiguration} is given, then only the best
     * match for this config is returned.
     *
     * @param type the type of the resource.
     * @param name the name of the resource.
     * @param referenceConfig an optional config for which only the best match will be returned.
     *
     * @return a list of files generating this resource or null if it was not found.
     */
    public List<ResourceFile> getSourceFiles(ResourceType type, String name,
            FolderConfiguration referenceConfig) {

        ProjectResourceItem[] resources = getResources(type);

        for (ProjectResourceItem item : resources) {
            if (name.equals(item.getName())) {
                if (referenceConfig != null) {
                    Resource match = findMatchingConfiguredResource(item.getSourceFileList(),
                            referenceConfig);
                    if (match instanceof ResourceFile) {
                        ArrayList<ResourceFile> list = new ArrayList<ResourceFile>();
                        list.add((ResourceFile) match);
                        return list;
                    }

                    return null;
                }
                return item.getSourceFileList();
            }
        }

        return null;
    }

    /**
     * Returns the resources values matching a given {@link FolderConfiguration}.
* @param referenceConfig the configuration that each value must match.
*/
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
FolderConfiguration referenceConfig) {

        Map<ResourceType, Map<String, ResourceValue>> map =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

// if the project contains libraries, we need to add the libraries resources here
//Synthetic comment -- @@ -438,22 +102,25 @@
// make sure they are loaded
libRes.loadAll();

// we don't want to simply replace the whole map, but instead merge the
// content of any sub-map
                        Map<ResourceType, Map<String, ResourceValue>> libMap =
                                libRes.getConfiguredResources(referenceConfig);

                        for (Entry<ResourceType, Map<String, ResourceValue>> entry : libMap.entrySet()) {
// get the map currently in the result map for this resource type
                            Map<String, ResourceValue> tempMap = map.get(entry.getKey());
if (tempMap == null) {
// since there's no current map for this type, just add the map
// directly coming from the library resources
                                map.put(entry.getKey(), entry.getValue());
} else {
// already a map for this type. add the resources from the
                                // library.
                                tempMap.putAll(entry.getValue());
}
}
}
//Synthetic comment -- @@ -462,62 +129,21 @@
}

// now the project resources themselves.
        // Don't blindly fill the map, instead check if there are sub-map already present
        // due to library resources.

        // special case for Id since there's a mix of compiled id (declared inline) and id declared
        // in the XML files.
        if (mIdResourceList.size() > 0) {
            Map<String, ResourceValue> idMap = map.get(ResourceType.ID);

            if (idMap == null) {
                idMap = new HashMap<String, ResourceValue>();
                map.put(ResourceType.ID, idMap);
            }
            for (IdResourceItem id : mIdResourceList) {
                // FIXME: cache the ResourceValue!
                idMap.put(id.getName(), new ResourceValue(ResourceType.ID, id.getName(),
                        mIsFrameworkRepository));
            }

        }

        Set<ResourceType> keys = mResourceMap.keySet();
        for (ResourceType key : keys) {
            // we don't process ID resources since we already did it above.
            if (key != ResourceType.ID) {
                // get the local results
                Map<String, ResourceValue> localResMap = getConfiguredResource(key,
                        referenceConfig);

                // check if a map for this type already exists
                Map<String, ResourceValue> resMap = map.get(key);
                if (resMap == null) {
                    // just use the local results.
                    map.put(key, localResMap);
                } else {
                    // add to the library results.
                    resMap.putAll(localResMap);
                }
}
}

        return map;
    }

    /**
     * Loads all the resources. Essentially this forces to load the values from the
     * {@link ResourceFile} objects to make sure they are up to date and loaded
     * in {@link #mResourceMap}.
     */
    public void loadAll() {
        // gets all the resource types available.
        ResourceType[] types = getAvailableResourceTypes();

        // loop on them and load them
        for (ResourceType type: types) {
            checkAndUpdate(type);
        }
}

/**
//Synthetic comment -- @@ -572,55 +198,9 @@
}

/**
     * Returns the sorted list of languages used in the resources.
     */
    public SortedSet<String> getLanguages() {
        SortedSet<String> set = new TreeSet<String>();

        Collection<List<ResourceFolder>> folderList = mFolderMap.values();
        for (List<ResourceFolder> folderSubList : folderList) {
            for (ResourceFolder folder : folderSubList) {
                FolderConfiguration config = folder.getConfiguration();
                LanguageQualifier lang = config.getLanguageQualifier();
                if (lang != null) {
                    set.add(lang.getShortDisplayValue());
                }
            }
        }

        return set;
    }

    /**
     * Returns the sorted list of regions used in the resources with the given language.
     * @param currentLanguage the current language the region must be associated with.
     */
    public SortedSet<String> getRegions(String currentLanguage) {
        SortedSet<String> set = new TreeSet<String>();

        Collection<List<ResourceFolder>> folderList = mFolderMap.values();
        for (List<ResourceFolder> folderSubList : folderList) {
            for (ResourceFolder folder : folderSubList) {
                FolderConfiguration config = folder.getConfiguration();

                // get the language
                LanguageQualifier lang = config.getLanguageQualifier();
                if (lang != null && lang.getShortDisplayValue().equals(currentLanguage)) {
                    RegionQualifier region = config.getRegionQualifier();
                    if (region != null) {
                        set.add(region.getShortDisplayValue());
                    }
                }
            }
        }

        return set;
    }

    /**
* Resets the list of dynamic Ids. This list is used by
* {@link #getResourceValue(String, String)} when the resource query is an ID that doesn't
     * exist (for example for ID automatically generated in layout files that are not saved.
* <p/>This method resets those dynamic ID and must be called whenever the actual list of IDs
* change.
*/
//Synthetic comment -- @@ -631,262 +211,20 @@
}
}

    /**
     * Returns a map of (resource name, resource value) for the given {@link ResourceType}.
     * <p/>The values returned are taken from the resource files best matching a given
     * {@link FolderConfiguration}.
     * @param type the type of the resources.
     * @param referenceConfig the configuration to best match.
     */
    private Map<String, ResourceValue> getConfiguredResource(ResourceType type,
            FolderConfiguration referenceConfig) {
        // get the resource item for the given type
        List<ProjectResourceItem> items = mResourceMap.get(type);

        // create the map
        HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>();

        for (ProjectResourceItem item : items) {
            // get the source files generating this resource
            List<ResourceFile> list = item.getSourceFileList();

            // look for the best match for the given configuration
            Resource match = findMatchingConfiguredResource(list, referenceConfig);

            if (match instanceof ResourceFile) {
                ResourceFile matchResFile = (ResourceFile)match;

                // get the value of this configured resource.
                ResourceValue value = matchResFile.getValue(type, item.getName());

                if (value != null) {
                    map.put(item.getName(), value);
                }
            }
        }

        return map;
}

/**
     * Returns the best matching {@link Resource}.
     * @param resources the list of {@link Resource} to choose from.
     * @param referenceConfig the {@link FolderConfiguration} to match.
     * @see http://d.android.com/guide/topics/resources/resources-i18n.html#best-match
*/
    private Resource findMatchingConfiguredResource(List<? extends Resource> resources,
            FolderConfiguration referenceConfig) {
        //
        // 1: eliminate resources that contradict the reference configuration
        // 2: pick next qualifier type
        // 3: check if any resources use this qualifier, if no, back to 2, else move on to 4.
        // 4: eliminate resources that don't use this qualifier.
        // 5: if more than one resource left, go back to 2.
        //
        // The precedence of the qualifiers is more important than the number of qualifiers that
        // exactly match the device.

        // 1: eliminate resources that contradict
        ArrayList<Resource> matchingResources = new ArrayList<Resource>();
        for (int i = 0 ; i < resources.size(); i++) {
            Resource res = resources.get(i);

            if (res.getConfiguration().isMatchFor(referenceConfig)) {
                matchingResources.add(res);
            }
        }

        // if there is only one match, just take it
        if (matchingResources.size() == 1) {
            return matchingResources.get(0);
        } else if (matchingResources.size() == 0) {
            return null;
        }

        // 2. Loop on the qualifiers, and eliminate matches
        final int count = FolderConfiguration.getQualifierCount();
        for (int q = 0 ; q < count ; q++) {
            // look to see if one resource has this qualifier.
            // At the same time also record the best match value for the qualifier (if applicable).

            // The reference value, to find the best match.
            // Note that this qualifier could be null. In which case any qualifier found in the
            // possible match, will all be considered best match.
            ResourceQualifier referenceQualifier = referenceConfig.getQualifier(q);

            boolean found = false;
            ResourceQualifier bestMatch = null; // this is to store the best match.
            for (Resource res : matchingResources) {
                ResourceQualifier qualifier = res.getConfiguration().getQualifier(q);
                if (qualifier != null) {
                    // set the flag.
                    found = true;

                    // Now check for a best match. If the reference qualifier is null ,
                    // any qualifier is a "best" match (we don't need to record all of them.
                    // Instead the non compatible ones are removed below)
                    if (referenceQualifier != null) {
                        if (qualifier.isBetterMatchThan(bestMatch, referenceQualifier)) {
                            bestMatch = qualifier;
                        }
                    }
                }
            }

            // 4. If a resources has a qualifier at the current index, remove all the resources that
            // do not have one, or whose qualifier value does not equal the best match found above
            // unless there's no reference qualifier, in which case they are all considered
            // "best" match.
            if (found) {
                for (int i = 0 ; i < matchingResources.size(); ) {
                    Resource res = matchingResources.get(i);
                    ResourceQualifier qualifier = res.getConfiguration().getQualifier(q);

                    if (qualifier == null) {
                        // this resources has no qualifier of this type: rejected.
                        matchingResources.remove(res);
                    } else if (referenceQualifier != null && bestMatch != null &&
                            bestMatch.equals(qualifier) == false) {
                        // there's a reference qualifier and there is a better match for it than
                        // this resource, so we reject it.
                        matchingResources.remove(res);
                    } else {
                        // looks like we keep this resource, move on to the next one.
                        i++;
                    }
                }

                // at this point we may have run out of matching resources before going
                // through all the qualifiers.
                if (matchingResources.size() < 2) {
                    break;
                }
            }
        }

        // Because we accept resources whose configuration have qualifiers where the reference
        // configuration doesn't, we can end up with more than one match. In this case, we just
        // take the first one.
        if (matchingResources.size() == 0) {
            return null;
        }
        return matchingResources.get(0);
    }

    /**
     * Checks if the list of {@link ResourceItem}s for the specified {@link ResourceType} needs
     * to be updated.
     * @param type the Resource Type.
     */
    private void checkAndUpdate(ResourceType type) {
        // get the list of folder that can output this type
        List<ResourceFolderType> folderTypes = FolderTypeRelationship.getRelatedFolders(type);

        for (ResourceFolderType folderType : folderTypes) {
            List<ResourceFolder> folders = mFolderMap.get(folderType);

            if (folders != null) {
                for (ResourceFolder folder : folders) {
                    if (folder.isTouched()) {
                        // if this folder is touched we need to update all the types that can
                        // be generated from a file in this folder.
                        // This will include 'type' obviously.
                        List<ResourceType> resTypes =
                            FolderTypeRelationship.getRelatedResourceTypes(folderType);
                        for (ResourceType resType : resTypes) {
                            update(resType);
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Updates the list of {@link ResourceItem} objects associated with a {@link ResourceType}.
     * This will reset the touch status of all the folders that can generate this resource type.
     * @param type the Resource Type.
     */
    private void update(ResourceType type) {
        // get the cache list, and lets make a backup
        List<ProjectResourceItem> items = mResourceMap.get(type);
        List<ProjectResourceItem> backup = new ArrayList<ProjectResourceItem>();

        if (items == null) {
            items = new ArrayList<ProjectResourceItem>();
            mResourceMap.put(type, items);
        } else {
            // backup the list
            backup.addAll(items);

            // we reset the list itself.
            items.clear();
        }

        // get the list of folder that can output this type
        List<ResourceFolderType> folderTypes = FolderTypeRelationship.getRelatedFolders(type);

        for (ResourceFolderType folderType : folderTypes) {
            List<ResourceFolder> folders = mFolderMap.get(folderType);

            if (folders != null) {
                for (ResourceFolder folder : folders) {
                    items.addAll(folder.getResources(type, this));
                    folder.resetTouch();
                }
            }
        }

        // now items contains the new list. We "merge" it with the backup list.
        // Basically, we need to keep the old instances of ResourceItem (where applicable),
        // but replace them by the content of the new items.
        // This will let the resource explorer keep the expanded state of the nodes whose data
        // is a ResourceItem object.
        if (backup.size() > 0) {
            // this is not going to change as we're only replacing instances.
            int count = items.size();

            for (int i = 0 ; i < count;) {
                // get the "new" item
                ProjectResourceItem item = items.get(i);

                // look for a similar item in the old list.
                ProjectResourceItem foundOldItem = null;
                for (ProjectResourceItem oldItem : backup) {
                    if (oldItem.getName().equals(item.getName())) {
                        foundOldItem = oldItem;
                        break;
                    }
                }

                if (foundOldItem != null) {
                    // erase the data of the old item with the data from the new one.
                    foundOldItem.replaceWith(item);

                    // remove the old and new item from their respective lists
                    items.remove(i);
                    backup.remove(foundOldItem);

                    // add the old item to the new list
                    items.add(foundOldItem);
                } else {
                    // this is a new item, we skip to the next object
                    i++;
                }
            }
        }

        // if this is the ResourceType.ID, we create the actual list, from this list and
        // the compiled resource list.
        if (type == ResourceType.ID) {
            mergeIdResources();
        } else {
            // else this is the list that will actually be displayed, so we sort it.
            Collections.sort(items);
        }
    }

private Integer getDynamicId(String name) {
synchronized (mDynamicIds) {
Integer value = mDynamicIds.get(name);
//Synthetic comment -- @@ -900,24 +238,6 @@
}

/**
     * Looks up an existing {@link ProjectResourceItem} by {@link ResourceType} and name.
     * @param type the Resource Type.
     * @param name the Resource name.
     * @return the existing ResourceItem or null if no match was found.
     */
    protected ProjectResourceItem findResourceItem(ResourceType type, String name) {
        List<ProjectResourceItem> list = mResourceMap.get(type);

        for (ProjectResourceItem item : list) {
            if (name.equals(item.getName())) {
                return item;
            }
        }

        return null;
    }

    /**
* Sets compiled resource information.
* @param resIdValueToNameMap a map of compiled resource id to resource name.
*  The map is acquired by the {@link ProjectResources} object.
//Synthetic comment -- @@ -934,77 +254,49 @@
mergeIdResources();
}

/**
* Merges the list of ID resource coming from R.java and the list of ID resources
* coming from XML declaration into the cached list {@link #mIdResourceList}.
*/
void mergeIdResources() {
        // get the list of IDs coming from XML declaration. Those ids are present in
        // mCompiledIdResources already, so we'll need to use those instead of creating
        // new IdResourceItem
        List<ProjectResourceItem> xmlIdResources = mResourceMap.get(ResourceType.ID);

        synchronized (mIdResourceList) {
            // copy the currently cached items.
            ArrayList<IdResourceItem> oldItems = new ArrayList<IdResourceItem>();
            oldItems.addAll(mIdResourceList);

            // empty the current list
            mIdResourceList.clear();

            // get the list of compile id resources.
            Map<String, Integer> idMap = null;
            if (mResourceValueMap != null) {
                idMap = mResourceValueMap.get(ResourceType.ID);
            }

            if (idMap == null) {
                if (xmlIdResources != null) {
                    for (ProjectResourceItem resourceItem : xmlIdResources) {
                        // check the actual class just for safety.
                        if (resourceItem instanceof IdResourceItem) {
                            mIdResourceList.add((IdResourceItem)resourceItem);
                        }
                    }
}
} else {
                // loop on the full list of id, and look for a match in the old list,
                // in the list coming from XML (in case a new XML item was created.)

                Set<String> idSet = idMap.keySet();

                idLoop: for (String idResource : idSet) {
                    // first look in the XML list in case an id went from inline to XML declared.
                    if (xmlIdResources != null) {
                        for (ProjectResourceItem resourceItem : xmlIdResources) {
                            if (resourceItem instanceof IdResourceItem &&
                                    resourceItem.getName().equals(idResource)) {
                                mIdResourceList.add((IdResourceItem)resourceItem);
                                continue idLoop;
                            }
                        }
                    }

                    // if we haven't found it, look in the old items.
                    int count = oldItems.size();
                    for (int i = 0 ; i < count ; i++) {
                        IdResourceItem resourceItem = oldItems.get(i);
                        if (resourceItem.getName().equals(idResource)) {
                            oldItems.remove(i);
                            mIdResourceList.add(resourceItem);
                            continue idLoop;
                        }
                    }

                    // if we haven't found it, it looks like it's a new id that was
                    // declared inline.
                    mIdResourceList.add(new IdResourceItem(idResource,
                            true /* isDeclaredInline */));
                }
}

            // now we sort the list
            Collections.sort(mIdResourceList);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index a6e10ae..7c861fc 100644

//Synthetic comment -- @@ -79,16 +79,16 @@
public abstract boolean hasResources(ResourceType type);

/**
     * Get the list of {@link ProjectResourceItem} of a specific type generated by the file.
* This method must make sure not to create duplicate.
     * @param type The type of {@link ProjectResourceItem} to return.
     * @param projectResources The global Project Resource object, allowing the implementation to
     * query for already existing {@link ProjectResourceItem}
     * @return The list of <b>new</b> {@link ProjectResourceItem}
     * @see ProjectResources#findResourceItem(ResourceType, String)
*/
    public abstract Collection<ProjectResourceItem> getResources(ResourceType type,
            ProjectResources projectResources);

/**
* Returns the value of a resource generated by this file by {@link ResourceType} and name.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 40f6fbb..634ae45 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
//Synthetic comment -- @@ -248,14 +247,14 @@
* @param projectResources The global Project Resource object, allowing the implementation to
* query for already existing {@link ResourceItem}
* @return The list of <b>new</b> {@link ResourceItem}
     * @see ProjectResources#findResourceItem(ResourceType, String)
*/
    public Collection<ProjectResourceItem> getResources(ResourceType type,
            ProjectResources projectResources) {
        Collection<ProjectResourceItem> list = new ArrayList<ProjectResourceItem>();
if (mFiles != null) {
for (ResourceFile f : mFiles) {
                list.addAll(f.getResources(type, projectResources));
}
}
return list;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..b6e1579

//Synthetic comment -- @@ -0,0 +1,173 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index c039f6b..4e969ba 100644

//Synthetic comment -- @@ -413,12 +413,12 @@
* Loads and returns the resources for a given {@link IAndroidTarget}
* @param androidTarget the target from which to load the framework resources
*/
    public ProjectResources loadFrameworkResources(IAndroidTarget androidTarget) {
String osResourcesPath = androidTarget.getPath(IAndroidTarget.RESOURCES);

FolderWrapper frameworkRes = new FolderWrapper(osResourcesPath);
if (frameworkRes.exists()) {
            ProjectResources resources = new ProjectResources();

try {
loadResources(resources, frameworkRes);
//Synthetic comment -- @@ -447,13 +447,13 @@
* setting rendering tests.
*
*
     * @param resources The {@link ProjectResources} files to load. It is expected that the
     * framework flag has been properly setup. This is filled up with the content of the folder.
* @param rootFolder The folder to read the resources from. This is the top level
* resource folder (res/)
* @throws IOException
*/
    public void loadResources(ProjectResources resources, IAbstractFolder rootFolder)
throws IOException {
IAbstractResource[] files = rootFolder.listMembers();
for (IAbstractResource file : files) {
//Synthetic comment -- @@ -578,10 +578,10 @@
/**
* Processes a folder and adds it to the list of the project resources.
* @param folder the folder to process
     * @param project the folder's project.
* @return the ConfiguredFolder created from this folder, or null if the process failed.
*/
    private ResourceFolder processFolder(IAbstractFolder folder, ProjectResources project) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -593,7 +593,7 @@
FolderConfiguration config = getConfig(folderSegments);

if (config != null) {
                ResourceFolder configuredFolder = project.add(type, config, folder);

return configuredFolder;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
new file mode 100644
//Synthetic comment -- index 0000000..2e0811f

//Synthetic comment -- @@ -0,0 +1,741 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 8677e5d..6c1ba2d 100644

//Synthetic comment -- @@ -22,9 +22,10 @@
import com.android.io.IAbstractFile;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
//Synthetic comment -- @@ -84,23 +85,25 @@
}

@Override
    public Collection<ProjectResourceItem> getResources(ResourceType type,
            ProjectResources projectResources) {

        // looking for an existing ResourceItem with this name and type
        ProjectResourceItem item = projectResources.findResourceItem(type, mResourceName);

        ArrayList<ProjectResourceItem> items = new ArrayList<ProjectResourceItem>();

        if (item == null) {
            item = new ConfigurableResourceItem(mResourceName);
            items.add(item);
        }

        // add this ResourceFile to the ResourceItem
item.add(this);

        return items;
}

/*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ccf4301..3ed971f 100644

//Synthetic comment -- @@ -16,12 +16,6 @@

package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.sdklib.SdkConstants.FD_DATA;
import static com.android.sdklib.SdkConstants.FD_RES;

import static java.io.File.separator;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -32,35 +26,18 @@
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;

import org.eclipse.core.runtime.IStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
* This class contains the data of an Android Target as loaded from the SDK.
*/
//Synthetic comment -- @@ -89,17 +66,14 @@
*/
private Hashtable<String, String[]> mAttributeValues = new Hashtable<String, String[]>();

    private IResourceRepository mSystemResourceRepository;

private AndroidManifestDescriptors mManifestDescriptors;
private LayoutDescriptors mLayoutDescriptors;
private MenuDescriptors mMenuDescriptors;
private XmlDescriptors mXmlDescriptors;

private Map<String, Map<String, Integer>> mEnumValueMap;
    private Map<ResourceType, Collection<String>> mPublicAttributeNames;

    private ProjectResources mFrameworkResources;
private LayoutLibrary mLayoutLibrary;

private boolean mLayoutBridgeInit = false;
//Synthetic comment -- @@ -113,7 +87,7 @@
* @param platformLibraries
* @param optionalLibraries
*/
    void setExtraData(IResourceRepository systemResourceRepository,
AndroidManifestDescriptors manifestDescriptors,
LayoutDescriptors layoutDescriptors,
MenuDescriptors menuDescriptors,
//Synthetic comment -- @@ -126,16 +100,15 @@
String[] intentCategoryValues,
String[] platformLibraries,
IOptionalLibrary[] optionalLibraries,
            ProjectResources resources,
LayoutLibrary layoutLibrary) {

        mSystemResourceRepository = systemResourceRepository;
mManifestDescriptors = manifestDescriptors;
mLayoutDescriptors = layoutDescriptors;
mMenuDescriptors = menuDescriptors;
mXmlDescriptors = xmlDescriptors;
mEnumValueMap = enumValueMap;
        mFrameworkResources = resources;
mLayoutLibrary = layoutLibrary;

setPermissions(permissionValues);
//Synthetic comment -- @@ -144,10 +117,6 @@
setOptionalLibraries(platformLibraries, optionalLibraries);
}

    public IResourceRepository getSystemResources() {
        return mSystemResourceRepository;
    }

/**
* Returns an {@link IDescriptorProvider} from a given Id.
* The Id can be one of {@link #DESCRIPTOR_MANIFEST}, {@link #DESCRIPTOR_LAYOUT},
//Synthetic comment -- @@ -263,7 +232,7 @@
/**
* Returns the {@link ProjectResources} containing the Framework Resources.
*/
    public ProjectResources getFrameworkResources() {
return mFrameworkResources;
}

//Synthetic comment -- @@ -360,124 +329,4 @@
mAttributeValues.remove(name);
mAttributeValues.put(name, values);
}

    /**
     * Returns true if the given name represents a public attribute of the given type.
     *
     * @param type the type of resource
     * @param name the name of the resource
     * @return true if the given property is public
     */
    public boolean isPublicResource(ResourceType type, String name) {
        Collection<String> names = getNameMap(type);
        if (names != null) {
            return names.contains(name);
        }

        return false;
    }

    /**
     * Returns all public properties (in no particular order) of a given resource type.
     *
     * @param type the type of resource
     * @return an unmodifiable collection of public resource names
     */
    public Collection<String> getPublicResourceNames(ResourceType type) {
        Collection<String> names = getNameMap(type);
        if (names != null) {
            return Collections.<String>unmodifiableCollection(names);
        }

        return Collections.emptyList();
    }

    /** Returns a (possibly cached) list of names for the given resource type, or null */
    private Collection<String> getNameMap(ResourceType type) {
        if (mPublicAttributeNames == null) {
            mPublicAttributeNames = readPublicAttributeLists();
        }

        return mPublicAttributeNames.get(type);
    }

    /**
     * Reads the public.xml file in data/res/values/ for this SDK and
     * returns the result as a map from resource type to a list of names
     */
    private Map<ResourceType, Collection<String>> readPublicAttributeLists() {
        String relative = FD_DATA + separator + FD_RES + separator + FD_RES_VALUES + separator +
            "public.xml"; //$NON-NLS-1$
        File file = new File(mTarget.getLocation(), relative);
        if (file.isFile()) {
            Map<ResourceType, Collection<String>> map =
                new HashMap<ResourceType, Collection<String>>();
            Document document = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Reader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                InputSource is = new InputSource(reader);
                factory.setNamespaceAware(true);
                factory.setValidating(false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(is);

                ResourceType lastType = null;
                String lastTypeName = "";

                NodeList children = document.getDocumentElement().getChildNodes();
                for (int i = 0, n = children.getLength(); i < n; i++) {
                    Node node = children.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String name = element.getAttribute("name"); //$NON-NLS-1$
                        if (name.length() > 0) {
                            String typeName = element.getAttribute("type"); //$NON-NLS-1$
                            ResourceType type = null;
                            if (typeName.equals(lastTypeName)) {
                                type = lastType;
                            } else {
                                type = ResourceType.getEnum(typeName);
                                lastType = type;
                                lastTypeName = typeName;
                            }
                            if (type != null) {
                                Collection<String> list = map.get(type);
                                if (list == null) {
                                    // Use sets for some of the larger maps to make
                                    // searching for isPublicResource faster.
                                    if (type == ResourceType.ATTR) {
                                        list = new HashSet<String>(900);
                                    } else if (type == ResourceType.STYLE) {
                                        list = new HashSet<String>(300);
                                    } else if (type == ResourceType.DRAWABLE) {
                                        list = new HashSet<String>(200);
                                    } else {
                                        list = new ArrayList<String>(30);
                                    }
                                    map.put(type, list);
                                }
                                list.add(name);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AdtPlugin.log(e, "Can't read and parse public attribute list");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // Nothing to be done here - we don't care if it closed or not.
                    }
                }
            }

            return map;

        }
        return Collections.emptyMap();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 6426fdb..8b535ea 100644

//Synthetic comment -- @@ -25,11 +25,8 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -86,7 +83,7 @@
try {
SubMonitor progress = SubMonitor.convert(monitor,
String.format("Parsing SDK %1$s", mAndroidTarget.getName()),
                    13);

AndroidTargetData targetData = new AndroidTargetData(mAndroidTarget);

//Synthetic comment -- @@ -101,15 +98,6 @@
return Status.CANCEL_STATUS;
}

            // get the resource Ids.
            progress.subTask("Resource IDs");
            IResourceRepository frameworkRepository = collectResourceIds(classLoader);
            progress.worked(1);

            if (progress.isCanceled()) {
                return Status.CANCEL_STATUS;
            }

// get the permissions
progress.subTask("Permissions");
String[] permissionValues = collectPermissions(classLoader);
//Synthetic comment -- @@ -231,8 +219,8 @@
progress.worked(1);

// load the framework resources.
            ProjectResources resources = ResourceManager.getInstance().loadFrameworkResources(
                    mAndroidTarget);
progress.worked(1);

// now load the layout lib bridge
//Synthetic comment -- @@ -243,7 +231,7 @@
progress.worked(1);

// and finally create the PlatformData with all that we loaded.
            targetData.setExtraData(frameworkRepository,
manifestDescriptors,
layoutDescriptors,
menuDescriptors,
//Synthetic comment -- @@ -256,7 +244,7 @@
categories.toArray(new String[categories.size()]),
mAndroidTarget.getPlatformLibraries(),
mAndroidTarget.getOptionalLibraries(),
                    resources,
layoutBridge);

Sdk.getCurrent().setTargetData(mAndroidTarget, targetData);
//Synthetic comment -- @@ -290,72 +278,6 @@
}

/**
     * Creates an IResourceRepository for the framework resources.
     *
     * @param classLoader The framework SDK jar classloader
     * @return a map of the resources, or null if it failed.
     */
    private IResourceRepository collectResourceIds(
            AndroidJarLoader classLoader) {
        try {
            Class<?> r = classLoader.loadClass(SdkConstants.CLASS_R);

            if (r != null) {
                Map<ResourceType, List<ResourceItem>> map = parseRClass(r);
                if (map != null) {
                    return new FrameworkResourceRepository(map);
                }
            }
        } catch (ClassNotFoundException e) {
            AdtPlugin.logAndPrintError(e, TAG,
                    "Collect resource IDs failed, class %1$s not found in %2$s", //$NON-NLS-1$
                    SdkConstants.CLASS_R,
                    mAndroidTarget.getPath(IAndroidTarget.ANDROID_JAR));
        }

        return null;
    }

    /**
     * Parse the R class and build the resource map.
     *
     * @param rClass the Class object representing the Resources.
     * @return a map of the resource or null
     */
    private Map<ResourceType, List<ResourceItem>> parseRClass(Class<?> rClass) {
        // get the sub classes.
        Class<?>[] classes = rClass.getClasses();

        if (classes.length > 0) {
            HashMap<ResourceType, List<ResourceItem>> map =
                new HashMap<ResourceType, List<ResourceItem>>();

            // get the fields of each class.
            for (int c = 0 ; c < classes.length ; c++) {
                Class<?> subClass = classes[c];
                String name = subClass.getSimpleName();

                // get the matching ResourceType
                ResourceType type = ResourceType.getEnum(name);
                if (type != null) {
                    List<ResourceItem> list = new ArrayList<ResourceItem>();
                    map.put(type, list);

                    Field[] fields = subClass.getFields();

                    for (Field f : fields) {
                        list.add(new ResourceItem(f.getName()));
                    }
                }
            }

            return map;
        }

        return null;
    }

    /**
* Loads, collects and returns the list of default permissions from the framework.
*
* @param classLoader The framework SDK jar classloader








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/FrameworkResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/FrameworkResourceRepository.java
deleted file mode 100644
//Synthetic comment -- index 247a888..0000000

//Synthetic comment -- @@ -1,76 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.resources.ResourceType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link IResourceRepository} interface to hold the system resource Ids
 * parsed by {@link AndroidTargetParser}. 
 */
final class FrameworkResourceRepository implements IResourceRepository {
    
    private Map<ResourceType, List<ResourceItem>> mResourcesMap; 
    
    public FrameworkResourceRepository(Map<ResourceType, List<ResourceItem>> systemResourcesMap) {
        mResourcesMap = systemResourcesMap;
    }

    public ResourceType[] getAvailableResourceTypes() {
        if (mResourcesMap != null) {
            Set<ResourceType> types = mResourcesMap.keySet();

            if (types != null) {
                return types.toArray(new ResourceType[types.size()]);
            }
        }

        return null;
    }

    public ResourceItem[] getResources(ResourceType type) {
        if (mResourcesMap != null) {
            List<ResourceItem> items = mResourcesMap.get(type);

            if (items != null) {
                return items.toArray(new ResourceItem[items.size()]);
            }
        }

        return null;
    }

    public boolean hasResources(ResourceType type) {
        if (mResourcesMap != null) {
            List<ResourceItem> items = mResourcesMap.get(type);

            return (items != null && items.size() > 0);
        }

        return false;
    }

    public boolean isSystemRepository() {
        return true;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java
//Synthetic comment -- index 1a9a78f..62f30f9 100644

//Synthetic comment -- @@ -15,7 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -196,9 +197,9 @@
Button button = (Button) event.widget;

// Open a resource chooser dialog for specified resource type.
                IResourceRepository projectRepository = ResourceManager.getInstance()
.getProjectResources(mProject);
                IResourceRepository systemRepository = mTargetData.getSystemResources();
ResourceChooser dlg = new ResourceChooser(mProject, ResourceType.DIMEN,
projectRepository, systemRepository, getShell());
Text text = (Text) button.getData(PROP_TEXTFIELD);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index 2a170a4..d9056e8 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -66,7 +66,7 @@

private static IDialogSettings sDialogSettings = new DialogSettings("");

    private IResourceRepository mResources;
private String mCurrentResource;
private FilteredTree mFilteredTree;
private Button mNewResButton;
//Synthetic comment -- @@ -77,10 +77,11 @@
* @param project
* @param parent
*/
    public ReferenceChooserDialog(IProject project, IResourceRepository resources, Shell parent) {
super(parent);
mProject = project;
        mResources = resources;

int shellStyle = getShellStyle();
setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);
//Synthetic comment -- @@ -177,7 +178,7 @@

mTreeViewer.setLabelProvider(new ResourceLabelProvider());
mTreeViewer.setContentProvider(new ResourceContentProvider(false /* fullLevels */));
        mTreeViewer.setInput(mResources);
}

protected void handleSelection() {
//Synthetic comment -- @@ -339,7 +340,7 @@
*/
private void setupInitialSelection(ResourceType resourceType, String resourceName) {
// get all the resources of this type
        ResourceItem[] resourceItems = mResources.getResources(resourceType);

for (ResourceItem resourceItem : resourceItems) {
if (resourceName.equals(resourceItem.getName())) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 87d1b75..2dec4c9 100644

//Synthetic comment -- @@ -28,12 +28,10 @@
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -77,10 +75,6 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -92,7 +86,8 @@

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
    private IResourceRepository mProjectResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
private Button mSystemButton;
//Synthetic comment -- @@ -109,14 +104,15 @@
* @param parent the parent shell
*/
public ResourceChooser(IProject project, ResourceType type,
            IResourceRepository projectResources,
            IResourceRepository systemResources,
Shell parent) {
super(parent, new ResourceLabelProvider());
mProject = project;

mResourceType = type;
mProjectResources = projectResources;

mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -374,21 +370,12 @@
ResourceItem[] items = null;
if (mProjectButton.getSelection()) {
items = mProjectResources.getResources(mResourceType);
            setListElements(items);
} else if (mSystemButton.getSelection()) {
            AndroidTargetData targetData = Sdk.getCurrent().getTargetData(mProject);
            if (targetData != null) {
                Collection<String> names = targetData.getPublicResourceNames(mResourceType);
                List<ResourceItem> list = new ArrayList<ResourceItem>();
                for (String name : names) {
                    list.add(new ResourceItem(name));
                }
                Collections.sort(list);
                items = list.toArray(new ResourceItem[list.size()]);
                setListElements(items);
            }
}

return items;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java
//Synthetic comment -- index f57b74e..673700f 100644

//Synthetic comment -- @@ -16,10 +16,9 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ConfigurableResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.resources.ResourceType;

import org.eclipse.jface.viewers.ITreeContentProvider;
//Synthetic comment -- @@ -40,10 +39,10 @@
* <li>{@link ResourceFile}. (optional) This represents a particular version of the
* {@link ResourceItem}. It is displayed as a list of resource qualifier.
* </li>
 * </ul> 
 * </ul> 
 * </ul> 
 * 
* @see ResourceLabelProvider
*/
public class ResourceContentProvider implements ITreeContentProvider {
//Synthetic comment -- @@ -51,10 +50,10 @@
/**
* The current ProjectResources being displayed.
*/
    private IResourceRepository mResources;
    
private boolean mFullLevels;
    
/**
* Constructs a new content providers for resource display.
* @param fullLevels if <code>true</code> the content provider will suppport all 3 levels. If
//Synthetic comment -- @@ -67,8 +66,8 @@
public Object[] getChildren(Object parentElement) {
if (parentElement instanceof ResourceType) {
return mResources.getResources((ResourceType)parentElement);
        } else if (mFullLevels && parentElement instanceof ConfigurableResourceItem) {
            return ((ConfigurableResourceItem)parentElement).getSourceFileArray();
}
return null;
}
//Synthetic comment -- @@ -81,15 +80,15 @@
public boolean hasChildren(Object element) {
if (element instanceof ResourceType) {
return mResources.hasResources((ResourceType)element);
        } else if (mFullLevels && element instanceof ConfigurableResourceItem) {
            return ((ConfigurableResourceItem)element).hasAlternates();
}
return false;
}

public Object[] getElements(Object inputElement) {
        if (inputElement instanceof IResourceRepository) {
            if ((IResourceRepository)inputElement == mResources) {
// get the top level resources.
return mResources.getAvailableResourceTypes();
}
//Synthetic comment -- @@ -103,8 +102,8 @@
}

public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof IResourceRepository) {
             mResources = (IResourceRepository)newInput;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java
//Synthetic comment -- index 081b6b6..6115215 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -138,10 +138,10 @@
}
} catch (PartInitException e) {
}
                        } else if (element instanceof ProjectResourceItem) {
// if it's a ResourceItem, we open the first file, but only if
// there's no alternate files.
                            ProjectResourceItem item = (ProjectResourceItem)element;

if (item.isEditableDirectly()) {
ResourceFile[] files = item.getSourceFileArray();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceLabelProvider.java
//Synthetic comment -- index 50e1d07..f08389f 100644

//Synthetic comment -- @@ -16,10 +16,7 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.IIdResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ConfigurableResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.IdResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.resources.ResourceType;

//Synthetic comment -- @@ -47,15 +44,15 @@
* <li>{@link ResourceFile}. This represents a particular version of the {@link ResourceItem}.
* It is displayed as a list of resource qualifier.
* </li>
 * </ul> 
 * </ul> 
 * </ul> 
 * 
* @see ResourceContentProvider
*/
public class ResourceLabelProvider implements ILabelProvider, ITableLabelProvider {
private Image mWarningImage;
    
public ResourceLabelProvider() {
mWarningImage = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
ISharedImages.IMG_OBJS_WARN_TSK).createImage();
//Synthetic comment -- @@ -94,8 +91,8 @@

public Image getColumnImage(Object element, int columnIndex) {
if (columnIndex == 1) {
            if (element instanceof ConfigurableResourceItem) {
                ConfigurableResourceItem item = (ConfigurableResourceItem)element;
if (item.hasDefault() == false) {
return mWarningImage;
}
//Synthetic comment -- @@ -116,19 +113,18 @@
}
break;
case 1:
                if (element instanceof ConfigurableResourceItem) {
                    ConfigurableResourceItem item = (ConfigurableResourceItem)element;
                    int count = item.getAlternateCount();
                    if (count > 0) {
                        if (item.hasDefault()) {
                            count++;
                        }
                        return String.format("%1$d version(s)", count);
                    }
                } else if (element instanceof IIdResourceItem) {
                    IIdResourceItem idResource = (IIdResourceItem)element;
                    if (idResource.isDeclaredInline()) {
return "Declared inline";
}
}
return null;







