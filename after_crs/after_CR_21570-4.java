/*Resource management refactoring and clean-up.

- (I)ResourceRepository is now a common class instead of an
  interface. This contains most of the code to control
  a repository (which was extracted from ProjectResources)
  ProjectResources extends it adding minor features such as
      library support, and inline ID definition.
  FrameworkResources extends it adding support for public
      resources (which used to be duplicated and dispersed
      in weird places).
  Changed the way resources are reloaded on resource change event.
  Instead of marking the resources as modified (using
  Resource.touch()), the resources are now parsed as the files are
  processed during the resource delta visitor. This makes more sense
  as there are now other listeners to the resource changes (hyperlinks)
  that access the resource list in their listeners, which wouldn't work
  previously.
  This also makes the code cleaner as the previous method had to query
  the repo for items and return a list of new ones, which was kinda
  crappy. The new code is much simpler, as is the post update process.

- ResourceItem is now the base class for resource items. It includes
  all the small methods that were added by all the child classes or
  interfaces.
  Project/ConfigurableResourceItem are merged into the based class.
  IIdResourceItem and IdResourceItem are gone and replaced by a
  simpler InlineResourceItem.
  FrameworkResourceItem is a simple override for framework resources.

- Also improved the API of a bit for the resource repository, making
  more use of unmodifiable lists and emptyList/Map()

Change-Id:Ie3ac1995213fed66153c7e7ecbdd170ec257be62*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index 10ccd77..c28a561 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import org.eclipse.core.runtime.CoreException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//Synthetic comment -- @@ -352,7 +353,7 @@

// now check that the file is *NOT* a layout file (those automatically trigger a layout
// reload and we don't want to do it twice.)
            Collection<ResourceType> resTypes = file.getResourceTypes();

// it's unclear why but there has been cases of resTypes being empty!
if (resTypes.size() > 0) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 3c5cea4..5e03b54 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
//Synthetic comment -- @@ -211,9 +212,9 @@
*/
void onRenderingTargetPostChange(IAndroidTarget target);

        ResourceRepository getProjectResources();
        ResourceRepository getFrameworkResources();
        ResourceRepository getFrameworkResources(IAndroidTarget target);
Map<ResourceType, Map<String, ResourceValue>> getConfiguredProjectResources();
Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources();
}
//Synthetic comment -- @@ -1121,12 +1122,12 @@
boolean hasLocale = false;

// get the languages from the project.
            ResourceRepository projectRes = mListener.getProjectResources();

// in cases where the opened file is not linked to a project, this could be null.
            if (projectRes != null) {
// now get the languages from the project.
                languages = projectRes.getLanguages();

for (String language : languages) {
hasLocale = true;
//Synthetic comment -- @@ -1134,7 +1135,7 @@
LanguageQualifier langQual = new LanguageQualifier(language);

// find the matching regions and add them
                    SortedSet<String> regions = projectRes.getRegions(language);
for (String region : regions) {
mLocaleCombo.add(
String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
//Synthetic comment -- @@ -1225,7 +1226,7 @@
return; // can't do anything w/o it.
}

        ResourceRepository frameworkRes = mListener.getFrameworkResources(getRenderingTarget());

mDisableUpdates++;

//Synthetic comment -- @@ -1237,10 +1238,10 @@
ArrayList<String> themes = new ArrayList<String>();

// get the themes, and languages from the Framework.
            if (frameworkRes != null) {
// get the configured resources for the framework
Map<ResourceType, Map<String, ResourceValue>> frameworResources =
                    frameworkRes.getConfiguredResources(getCurrentConfig());

if (frameworResources != null) {
// get the styles.
//Synthetic comment -- @@ -1269,9 +1270,9 @@
}

// now get the themes and languages from the project.
            ResourceRepository projectRes = mListener.getProjectResources();
// in cases where the opened file is not linked to a project, this could be null.
            if (projectRes != null) {
// get the configured resources for the project
Map<ResourceType, Map<String, ResourceValue>> configuredProjectRes =
mListener.getConfiguredProjectResources();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 9e0525e..858bfb8 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -541,7 +542,7 @@

public Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
                ResourceRepository frameworkRes = getFrameworkResources();

if (frameworkRes == null) {
AdtPlugin.log(IStatus.ERROR, "Failed to get ProjectResource for the framework");
//Synthetic comment -- @@ -559,9 +560,6 @@
if (mConfiguredProjectRes == null && mConfigComposite != null) {
ProjectResources project = getProjectResources();

// get the project resource values based on the current config
mConfiguredProjectRes = project.getConfiguredResources(
mConfigComposite.getCurrentConfig());
//Synthetic comment -- @@ -575,7 +573,7 @@
* configuration selection.
* @return the framework resources or null if not found.
*/
        public ResourceRepository getFrameworkResources() {
return getFrameworkResources(getRenderingTarget());
}

//Synthetic comment -- @@ -585,7 +583,7 @@
* @param target the target for which to return the framework resources.
* @return the framework resources or null if not found.
*/
        public ResourceRepository getFrameworkResources(IAndroidTarget target) {
if (target != null) {
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index f9e103a..046b4de 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -62,6 +62,7 @@
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -396,8 +397,8 @@
private void scanProject() {
ProjectResources resources = ResourceManager.getInstance().getProjectResources(mProject);
if (resources != null) {
            Collection<ResourceItem> layouts = resources.getResourceItems(ResourceType.LAYOUT);
            for (ResourceItem layout : layouts) {
List<ResourceFile> sources = layout.getSourceFileList();
for (ResourceFile source : sources) {
updateFileIncludes(source, false);
//Synthetic comment -- @@ -419,7 +420,7 @@
* @return true if we updated the includes for the resource file
*/
private boolean updateFileIncludes(ResourceFile resourceFile, boolean singleUpdate) {
        Collection<ResourceType> resourceTypes = resourceFile.getResourceTypes();
for (ResourceType type : resourceTypes) {
if (type == ResourceType.LAYOUT) {
ensureInitialized();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ce299e4..7a1e06e 100755

//Synthetic comment -- @@ -38,8 +38,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -852,7 +852,7 @@
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
                ResourceRepository projectRepository =
ResourceManager.getInstance().getProjectResources(project);
Shell shell = AdtPlugin.getDisplay().getActiveShell();
if (shell == null) {
//Synthetic comment -- @@ -879,7 +879,7 @@
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (project != null) {
// get the resource repository for this project and the system resources.
                ResourceRepository projectRepository = ResourceManager.getInstance()
.getProjectResources(project);
Shell shell = AdtPlugin.getDisplay().getActiveShell();
if (shell == null) {
//Synthetic comment -- @@ -887,7 +887,7 @@
}

AndroidTargetData data = editor.getTargetData();
                ResourceRepository systemRepository = data.getFrameworkResources();

// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index b10d69b..07e09bf 100644

//Synthetic comment -- @@ -23,9 +23,9 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
//Synthetic comment -- @@ -128,19 +128,19 @@
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
            ResourceRepository projectRepository =
ResourceManager.getInstance().getProjectResources(project);

if (mType != null) {
// get the Target Data to get the system resources
AndroidTargetData data = editor.getTargetData();
                ResourceRepository frameworkRepository = data.getFrameworkResources();

// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project,
mType,
projectRepository,
                        frameworkRepository,
shell);

dlg.setCurrentResource(currentValue);
//Synthetic comment -- @@ -196,7 +196,7 @@
*/
@Override
public String[] getPossibleValues(String prefix) {
        ResourceRepository repository = null;
boolean isSystem = false;

UiElementNode uiNode = getUiParent();
//Synthetic comment -- @@ -212,7 +212,7 @@
// If there's a prefix with "android:" in it, use the system resources
// Non-public framework resources are filtered out later.
AndroidTargetData data = editor.getTargetData();
            repository = data.getFrameworkResources();
isSystem = true;
}

//Synthetic comment -- @@ -265,18 +265,8 @@
sb.append(typeName).append('/');
String base = sb.toString();

                for (ResourceItem item : repository.getResourceItems(resType)) {
                    results.add(base + item.getName());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index efd95e2..5a2f39f 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -790,7 +791,7 @@
}

/** Return either the project resources or the framework resources (or null) */
    private static ResourceRepository getResources(IProject project, boolean framework) {
if (framework) {
IAndroidTarget target = getTarget(project);
if (target == null) {
//Synthetic comment -- @@ -822,7 +823,7 @@
}

// Look in the configuration folder: Search compatible configurations
        ResourceRepository resources = getResources(project, false /* isFramework */);
FolderConfiguration configuration = getConfiguration();
if (configuration != null) { // Not the case when searching from Java files for example
List<ResourceFolder> folders = resources.getFolders(ResourceFolderType.LAYOUT);
//Synthetic comment -- @@ -1128,7 +1129,7 @@

boolean isFramework = url.startsWith("@android"); //$NON-NLS-1$

        ResourceRepository resources = getResources(project, isFramework);
if (resources == null) {
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IIdResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IIdResourceItem.java
deleted file mode 100644
//Synthetic comment -- index acc4cf2..0000000

//Synthetic comment -- @@ -1,30 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/IResourceRepository.java
deleted file mode 100644
//Synthetic comment -- index 1abd9eb..0000000

//Synthetic comment -- @@ -1,49 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 327bd89..568174c 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.resources.ResourceType;


//Synthetic comment -- @@ -29,11 +30,8 @@
*/
public static String getXmlString(ResourceType type, ResourceItem resourceItem,
boolean system) {
        if (type == ResourceType.ID && resourceItem.isDeclaredInline()) {
            return (system?"@android:":"@+") + type.getName() + "/" + resourceItem.getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

return (system?"@android:":"@") + type.getName() + "/" + resourceItem.getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java
deleted file mode 100644
//Synthetic comment -- index c340ffe..0000000

//Synthetic comment -- @@ -1,48 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 543719b..8d9f9d0 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.FolderTypeRelationship;
//Synthetic comment -- @@ -32,6 +32,7 @@
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.dialogs.IInputValidator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -160,9 +161,9 @@
Set<String> existing = new HashSet<String>();
ResourceManager manager = ResourceManager.getInstance();
ProjectResources projectResources = manager.getProjectResources(project);
        Collection<ResourceItem> items = projectResources.getResourceItems(type);
        for (ResourceItem item : items) {
            existing.add(item.getName());
}

boolean isFileType = isFileBasedResourceType(type);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Resource.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Configurable.java
similarity index 65%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Resource.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Configurable.java
//Synthetic comment -- index 0ad5c39..0dfa102 100644

//Synthetic comment -- @@ -19,28 +19,11 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;

/**
 * An object that is associated to a {@link FolderConfiguration}.
*/
public interface Configurable {
/**
* Returns the {@link FolderConfiguration} for this object.
*/
    public FolderConfiguration getConfiguration();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 2a998f8..0000000

//Synthetic comment -- @@ -1,82 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..52ef572

//Synthetic comment -- @@ -0,0 +1,40 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
 * A custom {@link ResourceItem} for resources provided by the framework.
 *
 * The main change is that {@link #isEditableDirectly()} returns false.
 */
class FrameworkResourceItem extends ResourceItem {

    FrameworkResourceItem(String name) {
        super(name);
    }

    @Override
    public boolean isEditableDirectly() {
        return false;
    }

    @Override
    public String toString() {
        return "FrameworkResourceItem [mName=" + getName() + ", mFiles="
                + getSourceFileList() + "]";
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java
new file mode 100644
//Synthetic comment -- index 0000000..5af51cd

//Synthetic comment -- @@ -0,0 +1,200 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import static com.android.AndroidConstants.FD_RES_VALUES;

import static java.io.File.separator;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.resources.ResourceType;

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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Framework resources repository.
 *
 * This behaves the same as {@link ResourceRepository} except that it differentiate between
 * resources that are public and non public.
 * {@link #getResources(ResourceType)} and {@link #hasResources(ResourceType)} only return
 * public resources. This is typically used to display resource lists in the UI.
 *
 * {@link #getConfiguredResources(com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration)}
 * returns all resources, even the non public one so that this can be used for rendering.
 */
class FrameworkResources extends ResourceRepository {

    /**
     * Map of {@link ResourceType} to list of items. It is guaranteed to contain a list for all
     * possible values of ResourceType.
     */
    protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    FrameworkResources() {
        super(true /*isFrameworkRepository*/);
    }

    /**
     * Returns a {@link Collection} (always non null, but can be empty) of <b>public</b>
     * {@link ResourceItem} matching a given {@link ResourceType}.
     *
     * @param type the type of the resources to return
     * @return a collection of items, possible empty.
     */
    @Override
    public Collection<ResourceItem> getResourceItems(ResourceType type) {
        return mPublicResourceMap.get(type);
    }

    /**
     * Returns whether the repository has <b>public</b> resources of a given {@link ResourceType}.
     * @param type the type of resource to check.
     * @return true if the repository contains resources of the given type, false otherwise.
     */
    @Override
    public boolean hasResources(ResourceType type) {
        return mPublicResourceMap.get(type).size() > 0;
    }

    @Override
    protected ResourceItem doCreateResourceItem(String name) {
        return new FrameworkResourceItem(name);
    }

    /**
     * Reads the public.xml file in data/res/values/ for this SDK and fills up the public resource
     * map. This map is a subset of the full resource map that only contains framework resources
     * that are public.
     *
     * @param osFrameworkResourcePath the path to the framework resource folder.
     */
    void loadPublicResources(String osFrameworkResourcePath) {
        String relative = FD_RES_VALUES + separator + "public.xml"; //$NON-NLS-1$
        File file = new File(osFrameworkResourcePath, relative);
        if (file.isFile()) {
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
                                List<ResourceItem> typeList = mResourceMap.get(type);

                                ResourceItem match = null;
                                if (typeList != null) {
                                    for (ResourceItem item : typeList) {
                                        if (name.equals(item.getName())) {
                                            match = item;
                                            break;
                                        }
                                    }
                                }

                                if (match != null) {
                                    List<ResourceItem> publicList = mPublicResourceMap.get(type);
                                    if (publicList == null) {
                                        publicList = new ArrayList<ResourceItem>();
                                        mPublicResourceMap.put(type, publicList);
                                    }

                                    publicList.add(match);
                                } else {
                                    // log that there's a public resource that doesn't actually
                                    // exist?
                                    if (ResourceManager.DEBUG) {
                                        System.out.println(String.format(
                                                "No res matching public value %s/%s",
                                                typeName, name));
                                    }
                                }
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
        }

        // put unmodifiable list for all res type in the public resource map
        // this will simplify access
        for (ResourceType type : ResourceType.values()) {
            List<ResourceItem> list = mPublicResourceMap.get(type);
            if (list == null) {
                list = Collections.emptyList();
            } else {
                list = Collections.unmodifiableList(list);
            }

            // put the new list in the map
            mPublicResourceMap.put(type, list);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 8b142fb..0000000

//Synthetic comment -- @@ -1,54 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..5283f5e

//Synthetic comment -- @@ -0,0 +1,54 @@
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


/**
 * Represents a resource item that has been declared inline in another resource file.
 *
 * This covers the typical ID declaration of "@+id/foo", but does not cover normal value
 * resources declared in strings.xml or other similar value files.
 *
 * This resource will return {@code true} for {@link #isDeclaredInline()} and {@code false} for
 * {@link #isEditableDirectly()}.
 */
class InlineResourceItem extends ResourceItem {

    /**
     * Constructs a new inline ResourceItem.
     * @param name the name of the resource as it appears in the XML and R.java files.
     */
    InlineResourceItem(String name) {
        super(name);
    }

    @Override
    public boolean isDeclaredInline() {
        return true;
    }

    @Override
    public boolean isEditableDirectly() {
        return false;
    }

    @Override
    public String toString() {
        return "InlineResourceItem [mName=" + getName() + ", mFiles="
                + getSourceFileList() + "]";
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index 8f8e0d3..653f7f2 100644

//Synthetic comment -- @@ -26,14 +26,11 @@
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
//Synthetic comment -- @@ -49,82 +46,80 @@

private final static SAXParserFactory sParserFactory = SAXParserFactory.newInstance();

    private final Map<ResourceType, Map<String, ResourceValue>> mResourceItems =
        new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

    private Collection<ResourceType> mResourceTypeList = null;

public MultiResourceFile(IAbstractFile file, ResourceFolder folder) {
super(file, folder);
}

@Override
    protected void load() {
        // need to parse the file and find the content.
        parseFile();

        // create new ResourceItems for the new content.
        mResourceTypeList = Collections.unmodifiableCollection(mResourceItems.keySet());

        // create/update the resource items.
        updateResourceItems();
    }

    @Override
    protected void update() {
        // remove this file from all existing ResourceItem.
        getFolder().getRepository().removeFile(mResourceTypeList, this);

        // reset current content.
        mResourceItems.clear();

        // need to parse the file and find the content.
        parseFile();

        // create new ResourceItems for the new content.
        mResourceTypeList = Collections.unmodifiableCollection(mResourceItems.keySet());

        // create/update the resource items.
        updateResourceItems();
    }

    @Override
    protected void dispose() {
        // only remove this file from all existing ResourceItem.
        getFolder().getRepository().removeFile(mResourceTypeList, this);

        // don't need to touch the content, it'll get reclaimed as this objects disappear.
        // In the mean time other objects may need to access it.
    }

    @Override
    public Collection<ResourceType> getResourceTypes() {
return mResourceTypeList;
}

@Override
public boolean hasResources(ResourceType type) {
        Map<String, ResourceValue> list = mResourceItems.get(type);
return (list != null && list.size() > 0);
}

    private void updateResourceItems() {
        ResourceRepository repository = getRepository();
        for (ResourceType type : mResourceTypeList) {
            Map<String, ResourceValue> list = mResourceItems.get(type);

            if (list != null) {
                Collection<ResourceValue> values = list.values();
                for (ResourceValue res : values) {
                    ResourceItem item = repository.createResourceItem(type, res.getName());

                    // add this file to the list of files generating this resource item.
                    item.add(this);
}
}
}
}

/**
//Synthetic comment -- @@ -147,7 +142,7 @@
* @param value The value of the resource.
*/
public void addResourceValue(ResourceType resType, ResourceValue value) {
        Map<String, ResourceValue> list = mResourceItems.get(resType);

// if the list does not exist, create it.
if (list == null) {
//Synthetic comment -- @@ -169,10 +164,8 @@

@Override
public ResourceValue getValue(ResourceType type, String name) {
// get the list for the given type
        Map<String, ResourceValue> list = mResourceItems.get(type);

if (list != null) {
return list.get(name);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 845a974..0000000

//Synthetic comment -- @@ -1,91 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index b7f406d..6d14dc1 100644

//Synthetic comment -- @@ -17,51 +17,35 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents the resources of a project.
 * On top of the regular {@link ResourceRepository} features it provides:
 *
 * - configured resources contain the resources coming from the libraries.
 * - resolution to and from resource integer (compiled value in R.java)
 * - handles resource integer for non existing values of type ID. This is used when rendering
 *   layouts that have no been saved yet. This is handled by generating dynamic IDs on the fly.
*/
public class ProjectResources extends ResourceRepository {
private final static int DYNAMIC_ID_SEED_START = 0; // this should not conflict with any
// project IDs that start at a much higher
// value

/** Map of (name, id) for resources of type {@link ResourceType#ID} coming from R.java */
private Map<ResourceType, Map<String, Integer>> mResourceValueMap;
/** Map of (id, [name, resType]) for all resources coming from R.java */
//Synthetic comment -- @@ -69,352 +53,38 @@
/** Map of (int[], name) for styleable resources coming from R.java */
private Map<IntArrayWrapper, String> mStyleableValueToNameMap;

    /**
     * This list is used by {@link #getResourceValue(String, String)} when the resource
     * query is an ID that doesn't exist (for example for ID automatically generated in
     * layout files that are not saved yet).
     */
private final Map<String, Integer> mDynamicIds = new HashMap<String, Integer>();
private int mDynamicSeed = DYNAMIC_ID_SEED_START;

private final IProject mProject;


/**
* Makes a ProjectResources for a given <var>project</var>.
* @param project the project.
*/
public ProjectResources(IProject project) {
        super(false /*isFrameworkRepository*/);
mProject = project;
}

/**
     * Returns the resources values matching a given {@link FolderConfiguration}, this will
     * include library dependency.
*
* @param referenceConfig the configuration that each value must match.
     * @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
    @Override
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
FolderConfiguration referenceConfig) {

        Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

// if the project contains libraries, we need to add the libraries resources here
//Synthetic comment -- @@ -435,25 +105,25 @@

ProjectResources libRes = resMgr.getProjectResources(library);
if (libRes != null) {
                        // get the library resources, and only the library, not the dependencies
                        // so call doGetConfiguredResources() directly.
                        Map<ResourceType, Map<String, ResourceValue>> libMap =
                                libRes.doGetConfiguredResources(referenceConfig);

// we don't want to simply replace the whole map, but instead merge the
// content of any sub-map
                        for (Entry<ResourceType, Map<String, ResourceValue>> libEntry : libMap.entrySet()) {
// get the map currently in the result map for this resource type
                            Map<String, ResourceValue> tempMap = resultMap.get(libEntry.getKey());
if (tempMap == null) {
// since there's no current map for this type, just add the map
// directly coming from the library resources
                                resultMap.put(libEntry.getKey(), libEntry.getValue());
} else {
// already a map for this type. add the resources from the
                                // library, this will override existing value, which is why
                                // we loop in a specific library order.
                                tempMap.putAll(libEntry.getValue());
}
}
}
//Synthetic comment -- @@ -462,62 +132,21 @@
}

// now the project resources themselves.
        Map<ResourceType, Map<String, ResourceValue>> thisProjectMap =
                doGetConfiguredResources(referenceConfig);

        // now merge the maps.
        for (Entry<ResourceType, Map<String, ResourceValue>> entry : thisProjectMap.entrySet()) {
            ResourceType type = entry.getKey();
            Map<String, ResourceValue> typeMap = resultMap.get(type);
            if (typeMap == null) {
                resultMap.put(type, entry.getValue());
            } else {
                typeMap.putAll(entry.getValue());
}
}

        return resultMap;
}

/**
//Synthetic comment -- @@ -572,55 +201,9 @@
}

/**
* Resets the list of dynamic Ids. This list is used by
* {@link #getResourceValue(String, String)} when the resource query is an ID that doesn't
     * exist (for example for ID automatically generated in layout files that are not saved yet.)
* <p/>This method resets those dynamic ID and must be called whenever the actual list of IDs
* change.
*/
//Synthetic comment -- @@ -631,262 +214,20 @@
}
}

    @Override
    protected ResourceItem doCreateResourceItem(String name) {
        return new ResourceItem(name);
}

/**
     * Returns a dynamic integer for the given resource name, creating it if it doesn't
     * already exist.
     *
     * @param name the name of the resource
     * @return an integer.
     *
     * @see #resetDynamicIds()
*/
private Integer getDynamicId(String name) {
synchronized (mDynamicIds) {
Integer value = mDynamicIds.get(name);
//Synthetic comment -- @@ -900,24 +241,6 @@
}

/**
* Sets compiled resource information.
* @param resIdValueToNameMap a map of compiled resource id to resource name.
*  The map is acquired by the {@link ProjectResources} object.
//Synthetic comment -- @@ -934,77 +257,62 @@
mergeIdResources();
}

    @Override
    protected void postUpdate() {
        super.postUpdate();
        mergeIdResources();
    }

/**
* Merges the list of ID resource coming from R.java and the list of ID resources
* coming from XML declaration into the cached list {@link #mIdResourceList}.
*/
void mergeIdResources() {
        // get the current ID values
        List<ResourceItem> resources = mResourceMap.get(ResourceType.ID);

        // get the ID values coming from the R class.
        Map<String, Integer> rResources = mResourceValueMap.get(ResourceType.ID);

        if (rResources != null) {
            // make a copy of the compiled Resources.
            // As we loop on the full resources, we'll check with this copy map and remove from it
            // all the resources we find in the full list.
            // At the end, whatever is in the copy of the compile list is not in the full map, and
            // should be added as inlined resource items.
            Map<String, Integer> copy = new HashMap<String, Integer>(rResources);

            for (int i = 0 ; i < resources.size(); ) {
                ResourceItem item = resources.get(i);
                String name = item.getName();
                if (item.isDeclaredInline()) {
                    // This ID is declared inline in the full resource map.
                    // Check if it's also in the compiled version, in which case we can keep it.
                    // Otherwise, if it doesn't exist in the compiled map, remove it from the full
                    // map.
                    // Since we're going to remove it from the copy map either way, we can use
                    // remove to test if it's there
                    if (copy.remove(name) != null) {
                        // there is a match in the compiled list, do nothing, keep current one.
                        i++;
                    } else {
                        // the ID is now gone, remove it from the list
                        resources.remove(i);
}
                } else {
                    // not an inline item, remove it from the copy.
                    copy.remove(name);
                    i++;
}
}

            // now add what's left in copy to the list
            for (String name : copy.keySet()) {
                resources.add(new InlineResourceItem(name));
            }

            // sort the result
            Collections.sort(resources);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index a6e10ae..151830e 100644

//Synthetic comment -- @@ -22,12 +22,11 @@
import com.android.resources.ResourceType;

import java.util.Collection;

/**
* Represents a Resource file (a file under $Project/res/)
*/
public abstract class ResourceFile implements Configurable {

private final IAbstractFile mFile;
private final ResourceFolder mFolder;
//Synthetic comment -- @@ -37,11 +36,10 @@
mFolder = folder;
}

    protected abstract void load();
    protected abstract void update();
    protected abstract void dispose();

public FolderConfiguration getConfiguration() {
return mFolder.getConfiguration();
}
//Synthetic comment -- @@ -60,17 +58,21 @@
return mFolder;
}

    public final ResourceRepository getRepository() {
        return mFolder.getRepository();
    }

/**
* Returns whether the resource is a framework resource.
*/
public final boolean isFramework() {
        return mFolder.getRepository().isFrameworkRepository();
}

/**
* Returns the list of {@link ResourceType} generated by the file. This is never null.
*/
    public abstract Collection<ResourceType> getResourceTypes();

/**
* Returns whether the file generated a resource of a specific type.
//Synthetic comment -- @@ -79,18 +81,6 @@
public abstract boolean hasResources(ResourceType type);

/**
* Returns the value of a resource generated by this file by {@link ResourceType} and name.
* <p/>If no resource match, <code>null</code> is returned.
* @param type the type of the resource.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 40f6fbb..b5bb9d7 100644

//Synthetic comment -- @@ -16,32 +16,28 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* Resource Folder class. Contains list of {@link ResourceFile}s,
 * the {@link FolderConfiguration}, and a link to the {@link IAbstractFolder} object.
*/
public final class ResourceFolder implements Configurable {
    final ResourceFolderType mType;
    final FolderConfiguration mConfiguration;
IAbstractFolder mFolder;
ArrayList<ResourceFile> mFiles = null;
    private final ResourceRepository mRepository;


/**
* Creates a new {@link ResourceFolder}
//Synthetic comment -- @@ -50,19 +46,19 @@
* @param folder The associated {@link IAbstractFolder} object.
* @param isFrameworkRepository
*/
    protected ResourceFolder(ResourceFolderType type, FolderConfiguration config,
            IAbstractFolder folder, ResourceRepository repository) {
mType = type;
mConfiguration = config;
mFolder = folder;
        mRepository = repository;
}

/**
* Adds a {@link ResourceFile} to the folder.
* @param file The {@link ResourceFile}.
*/
    protected void addFile(ResourceFile file) {
if (mFiles == null) {
mFiles = new ArrayList<ResourceFile>();
}
//Synthetic comment -- @@ -70,35 +66,21 @@
mFiles.add(file);
}

    protected void removeFile(ResourceFile file) {
        file.dispose();
        mFiles.remove(file);
    }

    protected void dispose() {
        for (ResourceFile file : mFiles) {
            file.dispose();
}

        mFiles.clear();
}

/**
     * Returns the {@link IAbstractFolder} associated with this object.
*/
public IAbstractFolder getFolder() {
return mFolder;
//Synthetic comment -- @@ -111,11 +93,8 @@
return mType;
}

    public ResourceRepository getRepository() {
        return mRepository;
}

/**
//Synthetic comment -- @@ -126,7 +105,7 @@

if (mFiles != null) {
for (ResourceFile file : mFiles) {
                Collection<ResourceType> types = file.getResourceTypes();

// loop through those and add them to the main list,
// if they are not already present
//Synthetic comment -- @@ -141,11 +120,6 @@
return list;
}

public FolderConfiguration getConfiguration() {
return mConfiguration;
}
//Synthetic comment -- @@ -160,7 +134,7 @@

/**
* Returns the {@link ResourceFile} matching a {@link IAbstractFile} object.
     * @param file The {@link IAbstractFile} object.
* @return the {@link ResourceFile} or null if no match was found.
*/
public ResourceFile getFile(IAbstractFile file) {
//Synthetic comment -- @@ -175,27 +149,6 @@
}

/**
* Returns the {@link ResourceFile} matching a given name.
* @param filename The name of the file to return.
* @return the {@link ResourceFile} or <code>null</code> if no match was found.
//Synthetic comment -- @@ -240,27 +193,6 @@
return false;
}

@Override
public String toString() {
return mFolder.toString();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..983fcea

//Synthetic comment -- @@ -0,0 +1,209 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An android resource.
 *
 * This is a representation of the resource, not of its value(s). It gives access to all
 * the source files that generate this particular resource which then can be used to access
 * the actual value(s).
 *
 * @see ResourceFile#getResources(ResourceType, ResourceRepository)
 */
public class ResourceItem implements Comparable<ResourceItem> {

    private final static Comparator<ResourceFile> sComparator = new Comparator<ResourceFile>() {
        public int compare(ResourceFile file1, ResourceFile file2) {
            // get both FolderConfiguration and compare them
            FolderConfiguration fc1 = file1.getFolder().getConfiguration();
            FolderConfiguration fc2 = file2.getFolder().getConfiguration();

            return fc1.compareTo(fc2);
        }
    };

    private final String mName;

    /**
     * List of files generating this ResourceItem.
     */
    private final List<ResourceFile> mFiles = new ArrayList<ResourceFile>();

    /**
     * Constructs a new ResourceItem.
     * @param name the name of the resource as it appears in the XML and R.java files.
     */
    public ResourceItem(String name) {
        mName = name;
    }

    /**
     * Returns the name of the resource.
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

    /**
     * Returns whether the resource is editable directly.
     * <p/>
     * This is typically the case for resources that don't have alternate versions, or resources
     * of type {@link ResourceType#ID} that aren't declared inline.
     */
    public boolean isEditableDirectly() {
        return hasAlternates() == false;
    }

    /**
     * Returns whether the ID resource has been declared inline inside another resource XML file.
     * If the resource type is not {@link ResourceType#ID}, this will always return {@code false}.
     */
    public boolean isDeclaredInline() {
        return false;
    }

    /**
     * Adds a new source file.
     * @param file the source file.
     */
    protected void add(ResourceFile file) {
        mFiles.add(file);
    }

    /**
     * Removes a file from the list of source files.
     * @param file the file to remove
     */
    protected void removeFile(ResourceFile file) {
        mFiles.remove(file);
    }

    /**
     * Returns {@code true} if the item has no source file.
     * @return
     */
    protected boolean hasNoSourceFile() {
        return mFiles.size() == 0;
    }

    /**
     * Reset the item by emptying its source file list.
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
     * Returns the list of source file for this resource.
     */
    public List<ResourceFile> getSourceFileList() {
        return Collections.unmodifiableList(mFiles);
    }

    /**
     * Returns if the resource has at least one non-default version.
     *
     * @see ResourceFile#getConfiguration()
     * @see FolderConfiguration#isDefault()
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
     *
     * @see ResourceFile#getConfiguration()
     * @see FolderConfiguration#isDefault()
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
     * Returns the number of alternate versions for this resource.
     *
     * @see ResourceFile#getConfiguration()
     * @see FolderConfiguration#isDefault()
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

    /**
     * Replaces the content of the receiver with the ResourceItem received as parameter.
     * @param item the item to use for the replacement
     */
    protected void replaceWith(ResourceItem item) {
        mFiles.clear();
        mFiles.addAll(item.mFiles);
    }

    @Override
    public String toString() {
        return "ResourceItem [mName=" + mName + ", mFiles=" + mFiles + "]";
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index c039f6b..1414434 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFolderListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IResourceEventListener;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.FolderWrapper;
//Synthetic comment -- @@ -70,6 +71,7 @@
* @see ProjectResources
*/
public final class ResourceManager {
    public final static boolean DEBUG = true;

private final static ResourceManager sThis = new ResourceManager();

//Synthetic comment -- @@ -114,7 +116,9 @@
* @param monitor The global project monitor
*/
public static void setup(GlobalProjectMonitor monitor) {
        monitor.addResourceEventListener(sThis.mResourceEventListener);
monitor.addProjectListener(sThis.mProjectListener);

int mask = IResourceDelta.ADDED | IResourceDelta.REMOVED | IResourceDelta.CHANGED;
monitor.addFolderListener(sThis.mFolderListener, mask);
monitor.addFileListener(sThis.mFileListener, mask);
//Synthetic comment -- @@ -160,6 +164,40 @@
}
}

    private class ResourceEventListener implements IResourceEventListener {
        private final List<IProject> mChangedProjects = new ArrayList<IProject>();

        public void resourceChangeEventEnd() {
            for (IProject project : mChangedProjects) {
                ProjectResources resources;
                synchronized (mMap) {
                    resources = mMap.get(project);
                }

                resources.postUpdate();
            }

            mChangedProjects.clear();
        }

        public void resourceChangeEventStart() {
            // pass
        }

        void addProject(IProject project) {
            if (mChangedProjects.contains(project) == false) {
                mChangedProjects.add(project);
            }
        }
    }

    /**
     * Delegate listener for resource changes. This is called before and after any calls to the
     * project and file listeners (for a given resource change event).
     */
    private ResourceEventListener mResourceEventListener = new ResourceEventListener();


/**
* Implementation of the {@link IFolderListener} as an internal class so that the methods
* do not appear in the public API of {@link ResourceManager}.
//Synthetic comment -- @@ -179,6 +217,8 @@
return;
}

            mResourceEventListener.addProject(project);

switch (kind) {
case IResourceDelta.ADDED:
// checks if the folder is under res.
//Synthetic comment -- @@ -207,13 +247,13 @@
}
break;
case IResourceDelta.CHANGED:
                    // only call the listeners.
synchronized (mMap) {
resources = mMap.get(folder.getProject());
}
if (resources != null) {
ResourceFolder resFolder = resources.getResourceFolder(folder);
if (resFolder != null) {
notifyListenerOnFolderChange(project, resFolder, kind);
}
}
//Synthetic comment -- @@ -227,7 +267,8 @@
ResourceFolderType type = ResourceFolderType.getFolderType(
folder.getName());

                        ResourceFolder removedFolder = resources.removeFolder(type,
                                new IFolderWrapper(folder));
if (removedFolder != null) {
notifyListenerOnFolderChange(project, removedFolder, kind);
}
//Synthetic comment -- @@ -255,8 +296,6 @@
* @see IFileListener#fileChanged
*/
public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
final IProject project = file.getProject();

try {
//Synthetic comment -- @@ -268,80 +307,39 @@
return;
}

            // get the project resources
            ProjectResources resources;
            synchronized (mMap) {
                resources = mMap.get(project);
            }

            if (resources == null) {
                return;
            }

            // checks if the file is under res/something.
            IPath path = file.getFullPath();

            if (path.segmentCount() == 4) {
                if (isInResFolder(path)) {
                    IContainer container = file.getParent();
                    if (container instanceof IFolder) {

                        ResourceFolder folder = resources.getResourceFolder(
                                (IFolder)container);

                        // folder can be null as when the whole folder is deleted, the
                        // REMOVED event for the folder comes first. In this case, the
                        // folder will have taken care of things.
                        if (folder != null) {
                            ResourceFile resFile = processFile(
                                    new IFileWrapper(file),
                                    folder,
                                    kind);
                            notifyListenerOnFileChange(project, resFile, kind);
}
}
                }
}
}
};
//Synthetic comment -- @@ -413,15 +411,16 @@
* Loads and returns the resources for a given {@link IAndroidTarget}
* @param androidTarget the target from which to load the framework resources
*/
    public FrameworkResources loadFrameworkResources(IAndroidTarget androidTarget) {
String osResourcesPath = androidTarget.getPath(IAndroidTarget.RESOURCES);

FolderWrapper frameworkRes = new FolderWrapper(osResourcesPath);
if (frameworkRes.exists()) {
            FrameworkResources resources = new FrameworkResources();

try {
loadResources(resources, frameworkRes);
                resources.loadPublicResources(osResourcesPath);
return resources;
} catch (IOException e) {
// since we test that folders are folders, and files are files, this shouldn't
//Synthetic comment -- @@ -447,13 +446,13 @@
* setting rendering tests.
*
*
     * @param resources The {@link FrameworkResources} files to fill.
     *       This is filled up with the content of the folder.
* @param rootFolder The folder to read the resources from. This is the top level
* resource folder (res/)
* @throws IOException
*/
    private void loadResources(FrameworkResources resources, IAbstractFolder rootFolder)
throws IOException {
IAbstractResource[] files = rootFolder.listMembers();
for (IAbstractResource file : files) {
//Synthetic comment -- @@ -467,15 +466,12 @@

for (IAbstractResource childRes : children) {
if (childRes instanceof IAbstractFile) {
                            processFile((IAbstractFile) childRes, resFolder, IResourceDelta.ADDED);
}
}
}
}
}
}

/**
//Synthetic comment -- @@ -522,7 +518,8 @@
if (fileRes.getType() == IResource.FILE) {
IFile file = (IFile)fileRes;

                                        processFile(new IFileWrapper(file), resFolder,
                                                IResourceDelta.ADDED);
}
}
}
//Synthetic comment -- @@ -578,10 +575,10 @@
/**
* Processes a folder and adds it to the list of the project resources.
* @param folder the folder to process
     * @param resources the resource repository.
* @return the ConfiguredFolder created from this folder, or null if the process failed.
*/
    private ResourceFolder processFolder(IAbstractFolder folder, ResourceRepository resources) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -593,7 +590,7 @@
FolderConfiguration config = getConfig(folderSegments);

if (config != null) {
                ResourceFolder configuredFolder = resources.add(type, config, folder);

return configuredFolder;
}
//Synthetic comment -- @@ -606,37 +603,45 @@
* Processes a file and adds it to its parent folder resource.
* @param file the underlying resource file.
* @param folder the parent of the resource file.
     * @param kind the file change kind.
* @return the {@link ResourceFile} that was created.
*/
    private ResourceFile processFile(IAbstractFile file, ResourceFolder folder, int kind) {
// get the type of the folder
ResourceFolderType type = folder.getType();

// look for this file if it's already been created
ResourceFile resFile = folder.getFile(file);

        if (resFile == null) {
            if (kind != IResourceDelta.REMOVED) {
                // create a ResourceFile for it.

                // check if that's a single or multi resource type folder. For now we define this by
                // the number of possible resource type output by files in the folder. This does
                // not make the difference between several resource types from a single file or
                // the ability to have 2 files in the same folder generating 2 different types of
                // resource. The former is handled by MultiResourceFile properly while we don't
                // handle the latter. If we were to add this behavior we'd have to change this call.
                List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(type);

                if (types.size() == 1) {
                    resFile = new SingleResourceFile(file, folder);
                } else {
                    resFile = new MultiResourceFile(file, folder);
                }

                resFile.load();

                // add it to the folder
                folder.addFile(resFile);
}
        } else {
            if (kind == IResourceDelta.REMOVED) {
                folder.removeFile(resFile);
            } else {
                resFile.update();
            }
}

return resFile;
//Synthetic comment -- @@ -687,4 +692,16 @@
defaultConfig.createDefault();
mQualifiers = defaultConfig.getQualifiers();
}

    // debug only
    @SuppressWarnings("unused")
    private String getKindString(int kind) {
        switch (kind) {
            case IResourceDelta.ADDED: return "ADDED";
            case IResourceDelta.REMOVED: return "REMOVED";
            case IResourceDelta.CHANGED: return "CHANGED";
        }

        return Integer.toString(kind);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
new file mode 100644
//Synthetic comment -- index 0000000..9d5e588

//Synthetic comment -- @@ -0,0 +1,652 @@
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

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFolder;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Base class for resource repository.
 *
 * A repository is both a file representation of a resource folder and a representation
 * of the generated resources, organized by type.
 *
 * {@link #getResourceFolder(IFolder)} and {@link #getSourceFiles(ResourceType, String, FolderConfiguration)}
 * give access to the folders and files of the resource folder.
 *
 * {@link #getResources(ResourceType)} gives access to the resources directly.
 *
 */
public abstract class ResourceRepository {

    protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
        new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, List<ResourceItem>> mResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    private final Map<List<ResourceItem>, List<ResourceItem>> mReadOnlyListMap =
        new IdentityHashMap<List<ResourceItem>, List<ResourceItem>>();

    private final boolean mFrameworkRepository;

    protected final IntArrayWrapper mWrapper = new IntArrayWrapper(null);

    /**
     * Makes a ProjectResources for a given <var>project</var>.
     * @param project the project.
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

            ResourceFolder cf = new ResourceFolder(type, config, folder, this);
            list.add(cf);

            mFolderMap.put(type, list);

            return cf;
        }

        // look for an already existing folder configuration.
        for (ResourceFolder cFolder : list) {
            if (cFolder.mConfiguration.equals(config)) {
                // config already exist. Nothing to be done really, besides making sure
                // the IAbstractFolder object is up to date.
                cFolder.mFolder = folder;
                return cFolder;
            }
        }

        // If we arrive here, this means we didn't find a matching configuration.
        // So we add one.
        ResourceFolder cf = new ResourceFolder(type, config, folder, this);
        list.add(cf);

        return cf;
    }

    /**
     * Removes a {@link ResourceFolder} associated with the specified {@link IAbstractFolder}.
     * @param type The type of the folder
     * @param removedFolder the IAbstractFolder object.
     * @return the {@link ResourceFolder} that was removed, or null if no matches were found.
     */
    protected ResourceFolder removeFolder(ResourceFolderType type, IAbstractFolder removedFolder) {
        // get the list of folders for the resource type.
        List<ResourceFolder> list = mFolderMap.get(type);

        if (list != null) {
            int count = list.size();
            for (int i = 0 ; i < count ; i++) {
                ResourceFolder resFolder = list.get(i);
                // this is only used for Eclipse stuff so we know it's an IFolderWrapper
                IAbstractFolder folder = (IFolderWrapper) resFolder.getFolder();
                if (removedFolder.equals(folder)) {
                    // we found the matching ResourceFolder. we need to remove it.
                    list.remove(i);

                    // remove its content
                    resFolder.dispose();

                    return resFolder;
                }
            }
        }

        return null;
    }

    /**
     * Returns a {@link ResourceItem} matching the given {@link ResourceType} and name.
     *
     * @param type the resource type
     * @param name the name of the resource.
     * @return a pair containing the resource item and the creation state
     */
    protected ResourceItem createResourceItem(ResourceType type, String name) {
        // looking for an existing ResourceItem with this type and name
        ResourceItem item = findResourceItem(type, name);

        // create one if there isn't one already, or if the existing one is inlined, since
        // clearly we need a non inlined one (the inline one is removed too)
        if (item == null || item.isDeclaredInline()) {
            ResourceItem oldItem = item != null && item.isDeclaredInline() ? item : null;

            item = doCreateResourceItem(name);

            List<ResourceItem> list = mResourceMap.get(type);
            if (list == null) {
                list = new ArrayList<ResourceItem>();
                mResourceMap.put(type, list);
            }

            list.add(item);

            if (oldItem != null) {
                list.remove(oldItem);
            }
        }

        return item;
    }

    /**
     * Creates a resource item with the given name.
     * @param name the name of the resource
     * @return a new ResourceItem (or child class) instance.
     */
    protected abstract ResourceItem doCreateResourceItem(String name);


    /**
     * Returns a list of {@link ResourceFolder} for a specific {@link ResourceFolderType}.
     * @param type The {@link ResourceFolderType}
     */
    public List<ResourceFolder> getFolders(ResourceFolderType type) {
        return mFolderMap.get(type);
    }

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
                if (list.contains(types.get(0)) == false) {
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
                            if (list.contains(folderResType) == false) {
                                list.add(folderResType);
                            }
                        }
                    }
                }
            }
        }

        // at this point the list is full of ResourceType defined in the files.
        // We need to sort it.
        Collections.sort(list);

        return list.toArray(new ResourceType[list.size()]);
    }

    /**
     * Returns a collection of {@link ResourceItem} matching a given {@link ResourceType}.
     * @param type the type of the resource items to return
     * @return a non null collection of resource items
     */
    public Collection<ResourceItem> getResourceItems(ResourceType type) {
        List<ResourceItem> list = mResourceMap.get(type);

        if (list == null) {
            return Collections.emptyList();
        }

        List<ResourceItem> roList = mReadOnlyListMap.get(list);
        if (roList == null) {
            roList = Collections.unmodifiableList(list);
            mReadOnlyListMap.put(list, roList);
        }

        return roList;
    }

    /**
     * Returns whether the repository has resources of a given {@link ResourceType}.
     * @param type the type of resource to check.
     * @return true if the repository contains resources of the given type, false otherwise.
     */
    public boolean hasResources(ResourceType type) {
        List<ResourceItem> items = mResourceMap.get(type);
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
        Configurable match = findMatchingConfigurable(matchingFolders, config);

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

        Collection<ResourceItem> items = getResourceItems(type);

        for (ResourceItem item : items) {
            if (name.equals(item.getName())) {
                if (referenceConfig != null) {
                    Configurable match = findMatchingConfigurable(item.getSourceFileList(),
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
     *
     * @param referenceConfig the configuration that each value must match.
     * @return a map with guaranteed to contain an entry for each {@link ResourceType}
     */
    public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
            FolderConfiguration referenceConfig) {
        return doGetConfiguredResources(referenceConfig);
    }

    /**
     * Returns the resources values matching a given {@link FolderConfiguration} for the current
     * project.
     *
     * @param referenceConfig the configuration that each value must match.
     * @return a map with guaranteed to contain an entry for each {@link ResourceType}
     */
    protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
            FolderConfiguration referenceConfig) {

        Map<ResourceType, Map<String, ResourceValue>> map =
            new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

        for (ResourceType key : ResourceType.values()) {
            // get the local results and put them in the map
            map.put(key, getConfiguredResource(key, referenceConfig));
        }

        return map;
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

    protected void removeFile(Collection<ResourceType> types, ResourceFile file) {
        for (ResourceType type : types) {
            removeFile(type, file);
        }
    }

    protected void removeFile(ResourceType type, ResourceFile file) {
        List<ResourceItem> list = mResourceMap.get(type);
        for (int i = 0 ; i < list.size(); i++) {
            ResourceItem item = list.get(i);
            item.removeFile(file);
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
        List<ResourceItem> items = mResourceMap.get(type);
        if (items == null) {
            return Collections.emptyMap();
        }

        // create the map
        HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>();

        for (ResourceItem item : items) {
            // get the source files generating this resource
            List<ResourceFile> list = item.getSourceFileList();

            // look for the best match for the given configuration
            Configurable match = findMatchingConfigurable(list, referenceConfig);

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
     * Returns the best matching {@link Configurable}.
     *
     * @param configurables the list of {@link Configurable} to choose from.
     * @param referenceConfig the {@link FolderConfiguration} to match.
     *
     * @return an item from the given list of {@link Configurable} or null.
     *
     * @see http://d.android.com/guide/topics/resources/resources-i18n.html#best-match
     */
    private Configurable findMatchingConfigurable(List<? extends Configurable> configurables,
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
        ArrayList<Configurable> matchingConfigurables = new ArrayList<Configurable>();
        for (int i = 0 ; i < configurables.size(); i++) {
            Configurable res = configurables.get(i);

            if (res.getConfiguration().isMatchFor(referenceConfig)) {
                matchingConfigurables.add(res);
            }
        }

        // if there is only one match, just take it
        if (matchingConfigurables.size() == 1) {
            return matchingConfigurables.get(0);
        } else if (matchingConfigurables.size() == 0) {
            return null;
        }

        // 2. Loop on the qualifiers, and eliminate matches
        final int count = FolderConfiguration.getQualifierCount();
        for (int q = 0 ; q < count ; q++) {
            // look to see if one configurable has this qualifier.
            // At the same time also record the best match value for the qualifier (if applicable).

            // The reference value, to find the best match.
            // Note that this qualifier could be null. In which case any qualifier found in the
            // possible match, will all be considered best match.
            ResourceQualifier referenceQualifier = referenceConfig.getQualifier(q);

            boolean found = false;
            ResourceQualifier bestMatch = null; // this is to store the best match.
            for (Configurable configurable : matchingConfigurables) {
                ResourceQualifier qualifier = configurable.getConfiguration().getQualifier(q);
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

            // 4. If a configurable has a qualifier at the current index, remove all the ones that
            // do not have one, or whose qualifier value does not equal the best match found above
            // unless there's no reference qualifier, in which case they are all considered
            // "best" match.
            if (found) {
                for (int i = 0 ; i < matchingConfigurables.size(); ) {
                    Configurable configurable = matchingConfigurables.get(i);
                    ResourceQualifier qualifier = configurable.getConfiguration().getQualifier(q);

                    if (qualifier == null) {
                        // this resources has no qualifier of this type: rejected.
                        matchingConfigurables.remove(configurable);
                    } else if (referenceQualifier != null && bestMatch != null &&
                            bestMatch.equals(qualifier) == false) {
                        // there's a reference qualifier and there is a better match for it than
                        // this resource, so we reject it.
                        matchingConfigurables.remove(configurable);
                    } else {
                        // looks like we keep this resource, move on to the next one.
                        i++;
                    }
                }

                // at this point we may have run out of matching resources before going
                // through all the qualifiers.
                if (matchingConfigurables.size() < 2) {
                    break;
                }
            }
        }

        // Because we accept resources whose configuration have qualifiers where the reference
        // configuration doesn't, we can end up with more than one match. In this case, we just
        // take the first one.
        if (matchingConfigurables.size() == 0) {
            return null;
        }
        return matchingConfigurables.get(0);
    }

    /**
     * Called after a resource change event, when the resource delta has been processed.
     */
    protected void postUpdate() {
        // Since removed files/folders remove source files from existing ResourceItem, loop through
        // all resource items and remove the ones that have no source files.

        Collection<List<ResourceItem>> lists = mResourceMap.values();
        for (List<ResourceItem> list : lists) {
            for (int i = 0 ; i < list.size() ;) {
                if (list.get(i).hasNoSourceFile()) {
                    list.remove(i);
                } else {
                    i++;
                }
            }

            // this is the list that will actually be displayed, so we sort it.
            Collections.sort(list);
        }
    }

    /**
     * Looks up an existing {@link ResourceItem} by {@link ResourceType} and name. This
     * ignores inline resources.
     * @param type the Resource Type.
     * @param name the Resource name.
     * @return the existing ResourceItem or null if no match was found.
     */
    private ResourceItem findResourceItem(ResourceType type, String name) {
        List<ResourceItem> list = mResourceMap.get(type);

        if (list != null) {
            for (ResourceItem item : list) {
                // ignore inline
                if (name.equals(item.getName()) && item.isDeclaredInline() == false) {
                    return item;
                }
            }
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 8677e5d..c0d58bf 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;

import java.util.Collection;
import java.util.List;

//Synthetic comment -- @@ -74,7 +73,31 @@
}

@Override
    protected void load() {
        // get a resource item matching the given type and name
        ResourceItem item = getRepository().createResourceItem(mType, mResourceName);

        // add this file to the list of files generating this resource item.
        item.add(this);
    }

    @Override
    protected void update() {
        // when this happens, nothing needs to be done since the file only generates
        // a single resources that doesn't actually change (its content is the file path)
    }

    @Override
    protected void dispose() {
        // only remove this file from the existing ResourceItem.
        getFolder().getRepository().removeFile(mType, this);

        // don't need to touch the content, it'll get reclaimed as this objects disappear.
        // In the mean time other objects may need to access it.
    }

    @Override
    public Collection<ResourceType> getResourceTypes() {
return FolderTypeRelationship.getRelatedResourceTypes(getFolder().getType());
}

//Synthetic comment -- @@ -83,26 +106,6 @@
return FolderTypeRelationship.match(type, getFolder().getType());
}

/*
* (non-Javadoc)
* @see com.android.ide.eclipse.editors.resources.manager.ResourceFile#getValue(com.android.ide.eclipse.common.resources.ResourceType, java.lang.String)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ccf4301..62aa180 100644

//Synthetic comment -- @@ -16,12 +16,6 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -32,35 +26,18 @@
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;

import org.eclipse.core.runtime.IStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
* This class contains the data of an Android Target as loaded from the SDK.
*/
//Synthetic comment -- @@ -89,17 +66,14 @@
*/
private Hashtable<String, String[]> mAttributeValues = new Hashtable<String, String[]>();

private AndroidManifestDescriptors mManifestDescriptors;
private LayoutDescriptors mLayoutDescriptors;
private MenuDescriptors mMenuDescriptors;
private XmlDescriptors mXmlDescriptors;

private Map<String, Map<String, Integer>> mEnumValueMap;

    private ResourceRepository mFrameworkResources;
private LayoutLibrary mLayoutLibrary;

private boolean mLayoutBridgeInit = false;
//Synthetic comment -- @@ -113,7 +87,7 @@
* @param platformLibraries
* @param optionalLibraries
*/
    void setExtraData(
AndroidManifestDescriptors manifestDescriptors,
LayoutDescriptors layoutDescriptors,
MenuDescriptors menuDescriptors,
//Synthetic comment -- @@ -126,16 +100,15 @@
String[] intentCategoryValues,
String[] platformLibraries,
IOptionalLibrary[] optionalLibraries,
            ResourceRepository frameworkResources,
LayoutLibrary layoutLibrary) {

mManifestDescriptors = manifestDescriptors;
mLayoutDescriptors = layoutDescriptors;
mMenuDescriptors = menuDescriptors;
mXmlDescriptors = xmlDescriptors;
mEnumValueMap = enumValueMap;
        mFrameworkResources = frameworkResources;
mLayoutLibrary = layoutLibrary;

setPermissions(permissionValues);
//Synthetic comment -- @@ -144,10 +117,6 @@
setOptionalLibraries(platformLibraries, optionalLibraries);
}

/**
* Returns an {@link IDescriptorProvider} from a given Id.
* The Id can be one of {@link #DESCRIPTOR_MANIFEST}, {@link #DESCRIPTOR_LAYOUT},
//Synthetic comment -- @@ -263,7 +232,7 @@
/**
* Returns the {@link ProjectResources} containing the Framework Resources.
*/
    public ResourceRepository getFrameworkResources() {
return mFrameworkResources;
}

//Synthetic comment -- @@ -360,124 +329,4 @@
mAttributeValues.remove(name);
mAttributeValues.put(name, values);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 6426fdb..977c759 100644

//Synthetic comment -- @@ -25,11 +25,8 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -86,7 +83,7 @@
try {
SubMonitor progress = SubMonitor.convert(monitor,
String.format("Parsing SDK %1$s", mAndroidTarget.getName()),
                    12);

AndroidTargetData targetData = new AndroidTargetData(mAndroidTarget);

//Synthetic comment -- @@ -101,15 +98,6 @@
return Status.CANCEL_STATUS;
}

// get the permissions
progress.subTask("Permissions");
String[] permissionValues = collectPermissions(classLoader);
//Synthetic comment -- @@ -231,8 +219,8 @@
progress.worked(1);

// load the framework resources.
            ResourceRepository frameworkResources =
                    ResourceManager.getInstance().loadFrameworkResources(mAndroidTarget);
progress.worked(1);

// now load the layout lib bridge
//Synthetic comment -- @@ -243,7 +231,7 @@
progress.worked(1);

// and finally create the PlatformData with all that we loaded.
            targetData.setExtraData(
manifestDescriptors,
layoutDescriptors,
menuDescriptors,
//Synthetic comment -- @@ -256,7 +244,7 @@
categories.toArray(new String[categories.size()]),
mAndroidTarget.getPlatformLibraries(),
mAndroidTarget.getOptionalLibraries(),
                    frameworkResources,
layoutBridge);

Sdk.getCurrent().setTargetData(mAndroidTarget, targetData);
//Synthetic comment -- @@ -290,72 +278,6 @@
}

/**
* Loads, collects and returns the list of default permissions from the framework.
*
* @param classLoader The framework SDK jar classloader








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/FrameworkResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/FrameworkResourceRepository.java
deleted file mode 100644
//Synthetic comment -- index 247a888..0000000

//Synthetic comment -- @@ -1,76 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/MarginChooser.java
//Synthetic comment -- index 1a9a78f..3c291f3 100644

//Synthetic comment -- @@ -15,8 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;

//Synthetic comment -- @@ -196,11 +197,11 @@
Button button = (Button) event.widget;

// Open a resource chooser dialog for specified resource type.
                ProjectResources projectRepository = ResourceManager.getInstance()
.getProjectResources(mProject);
                ResourceRepository frameworkRepository = mTargetData.getFrameworkResources();
ResourceChooser dlg = new ResourceChooser(mProject, ResourceType.DIMEN,
                        projectRepository, frameworkRepository, getShell());
Text text = (Text) button.getData(PROP_TEXTFIELD);
dlg.setCurrentResource(text.getText().trim());
if (dlg.open() == Window.OK) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index 2a170a4..16ffa82 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -52,6 +52,7 @@
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -66,7 +67,7 @@

private static IDialogSettings sDialogSettings = new DialogSettings("");

    private ResourceRepository mProjectResources;
private String mCurrentResource;
private FilteredTree mFilteredTree;
private Button mNewResButton;
//Synthetic comment -- @@ -77,10 +78,11 @@
* @param project
* @param parent
*/
    public ReferenceChooserDialog(IProject project, ResourceRepository projectResources,
            Shell parent) {
super(parent);
mProject = project;
        mProjectResources = projectResources;

int shellStyle = getShellStyle();
setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);
//Synthetic comment -- @@ -177,7 +179,7 @@

mTreeViewer.setLabelProvider(new ResourceLabelProvider());
mTreeViewer.setContentProvider(new ResourceContentProvider(false /* fullLevels */));
        mTreeViewer.setInput(mProjectResources);
}

protected void handleSelection() {
//Synthetic comment -- @@ -339,7 +341,7 @@
*/
private void setupInitialSelection(ResourceType resourceType, String resourceName) {
// get all the resources of this type
        Collection<ResourceItem> resourceItems = mProjectResources.getResourceItems(resourceType);

for (ResourceItem resourceItem : resourceItems) {
if (resourceName.equals(resourceItem.getName())) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 87d1b75..8b02422 100644

//Synthetic comment -- @@ -28,12 +28,10 @@
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -77,10 +75,7 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -92,7 +87,8 @@

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
    private final ResourceRepository mProjectResources;
    private final ResourceRepository mFrameworkResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
private Button mSystemButton;
//Synthetic comment -- @@ -105,18 +101,19 @@
* @param project Project being worked on
* @param type The type of the resource to choose
* @param projectResources The repository for the project
     * @param frameworkResources The Framework resource repository
* @param parent the parent shell
*/
public ResourceChooser(IProject project, ResourceType type,
            ResourceRepository projectResources,
            ResourceRepository frameworkResources,
Shell parent) {
super(parent, new ResourceLabelProvider());
mProject = project;

mResourceType = type;
mProjectResources = projectResources;
        mFrameworkResources = frameworkResources;

mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -371,25 +368,18 @@
* Setups the current list.
*/
private ResourceItem[] setupResourceList() {
        Collection<ResourceItem> items = null;
if (mProjectButton.getSelection()) {
            items = mProjectResources.getResourceItems(mResourceType);
} else if (mSystemButton.getSelection()) {
            items = mFrameworkResources.getResourceItems(mResourceType);
}

        ResourceItem[] arrayItems = items.toArray(new ResourceItem[items.size()]);

        setListElements(arrayItems);

        return arrayItems;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java
//Synthetic comment -- index f57b74e..3632fbd 100644

//Synthetic comment -- @@ -16,10 +16,9 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
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
    private ResourceRepository mResources;

private boolean mFullLevels;

/**
* Constructs a new content providers for resource display.
* @param fullLevels if <code>true</code> the content provider will suppport all 3 levels. If
//Synthetic comment -- @@ -66,9 +65,9 @@

public Object[] getChildren(Object parentElement) {
if (parentElement instanceof ResourceType) {
            return mResources.getResourceItems((ResourceType)parentElement).toArray();
        } else if (mFullLevels && parentElement instanceof ResourceItem) {
            return ((ResourceItem)parentElement).getSourceFileArray();
}
return null;
}
//Synthetic comment -- @@ -81,15 +80,15 @@
public boolean hasChildren(Object element) {
if (element instanceof ResourceType) {
return mResources.hasResources((ResourceType)element);
        } else if (mFullLevels && element instanceof ResourceItem) {
            return ((ResourceItem)element).hasAlternates();
}
return false;
}

public Object[] getElements(Object inputElement) {
        if (inputElement instanceof ResourceRepository) {
            if ((ResourceRepository)inputElement == mResources) {
// get the top level resources.
return mResources.getAvailableResourceTypes();
}
//Synthetic comment -- @@ -103,8 +102,8 @@
}

public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof ResourceRepository) {
             mResources = (ResourceRepository)newInput;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java
//Synthetic comment -- index 081b6b6..6115215 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -138,10 +138,10 @@
}
} catch (PartInitException e) {
}
                        } else if (element instanceof ResourceItem) {
// if it's a ResourceItem, we open the first file, but only if
// there's no alternate files.
                            ResourceItem item = (ResourceItem)element;

if (item.isEditableDirectly()) {
ResourceFile[] files = item.getSourceFileArray();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceLabelProvider.java
//Synthetic comment -- index 50e1d07..f08389f 100644

//Synthetic comment -- @@ -16,10 +16,7 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
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
            if (element instanceof ResourceItem) {
                ResourceItem item = (ResourceItem)element;
if (item.hasDefault() == false) {
return mWarningImage;
}
//Synthetic comment -- @@ -116,19 +113,18 @@
}
break;
case 1:
                if (element instanceof ResourceItem) {
                    ResourceItem item = (ResourceItem)element;
                    if (item.isDeclaredInline()) {
return "Declared inline";
                    } else {
                        int count = item.getAlternateCount();
                        if (count > 0) {
                            if (item.hasDefault()) {
                                count++;
                            }
                            return String.format("%1$d version(s)", count);
                        }
}
}
return null;








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 2a06373..9493c35 100644

//Synthetic comment -- @@ -121,14 +121,10 @@
if (frameworkTheme) {
Map<String, ResourceValue> frameworkStyleMap = mFrameworkResources.get(
ResourceType.STYLE);
            theme = frameworkStyleMap.get(name);
} else {
Map<String, ResourceValue> projectStyleMap = mProjectResources.get(ResourceType.STYLE);
            theme = projectStyleMap.get(name);
}

if (theme instanceof StyleResourceValue) {
//Synthetic comment -- @@ -334,29 +330,25 @@
// if allowed, search in the project resources first.
if (frameworkOnly == false) {
typeMap = mProjectResources.get(resType);
            ResourceValue item = typeMap.get(resName);
            if (item != null) {
                return item;
}
}

// now search in the framework resources.
typeMap = mFrameworkResources.get(resType);
        ResourceValue item = typeMap.get(resName);
        if (item != null) {
            return item;
        }

        // if it was not found and the type is an id, it is possible that the ID was
        // generated dynamically when compiling the framework resources.
        // Look for it in the R map.
        if (mFrameworkProvider != null && resType == ResourceType.ID) {
            if (mFrameworkProvider.getId(resType, resName) != null) {
                return new ResourceValue(resType, resName, true);
}
}

//Synthetic comment -- @@ -397,32 +389,30 @@
Map<String, ResourceValue> projectStyleMap = mProjectResources.get(ResourceType.STYLE);
Map<String, ResourceValue> frameworkStyleMap = mFrameworkResources.get(ResourceType.STYLE);

        // first, get the theme
        ResourceValue theme = null;

        // project theme names have been prepended with a *
        if (isProjectTheme) {
            theme = projectStyleMap.get(themeName);
        } else {
            theme = frameworkStyleMap.get(themeName);
        }

        if (theme instanceof StyleResourceValue) {
            // compute the inheritance map for both the project and framework styles
            computeStyleInheritance(projectStyleMap.values(), projectStyleMap,
                    frameworkStyleMap);

            // Compute the style inheritance for the framework styles/themes.
            // Since, for those, the style parent values do not contain 'android:'
            // we want to force looking in the framework style only to avoid using
            // similarly named styles from the project.
            // To do this, we pass null in lieu of the project style map.
            computeStyleInheritance(frameworkStyleMap.values(), null /*inProjectStyleMap */,
                    frameworkStyleMap);

            mTheme = (StyleResourceValue) theme;
}
}








