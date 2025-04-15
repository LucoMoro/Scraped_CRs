/*Resource refactoring and clean-up.

Change-Id:Ie3ac1995213fed66153c7e7ecbdd170ec257be62*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index d82774d..180bff1 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
//Synthetic comment -- @@ -209,9 +210,9 @@
*/
void onRenderingTargetPostChange(IAndroidTarget target);

        ResourceRepository getProjectResources();
        ResourceRepository getFrameworkResources();
        ResourceRepository getFrameworkResources(IAndroidTarget target);
Map<ResourceType, Map<String, ResourceValue>> getConfiguredProjectResources();
Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources();
}
//Synthetic comment -- @@ -1119,12 +1120,12 @@
boolean hasLocale = false;

// get the languages from the project.
            ResourceRepository projectRes = mListener.getProjectResources();

// in cases where the opened file is not linked to a project, this could be null.
            if (projectRes != null) {
// now get the languages from the project.
                languages = projectRes.getLanguages();

for (String language : languages) {
hasLocale = true;
//Synthetic comment -- @@ -1132,7 +1133,7 @@
LanguageQualifier langQual = new LanguageQualifier(language);

// find the matching regions and add them
                    SortedSet<String> regions = projectRes.getRegions(language);
for (String region : regions) {
mLocaleCombo.add(
String.format("%1$s / %2$s", language, region)); //$NON-NLS-1$
//Synthetic comment -- @@ -1223,7 +1224,7 @@
return; // can't do anything w/o it.
}

        ResourceRepository frameworkRes = mListener.getFrameworkResources(getRenderingTarget());

mDisableUpdates++;

//Synthetic comment -- @@ -1235,10 +1236,10 @@
ArrayList<String> themes = new ArrayList<String>();

// get the themes, and languages from the Framework.
            if (frameworkRes != null) {
// get the configured resources for the framework
Map<ResourceType, Map<String, ResourceValue>> frameworResources =
                    frameworkRes.getConfiguredResources(getCurrentConfig());

if (frameworResources != null) {
// get the styles.
//Synthetic comment -- @@ -1267,9 +1268,9 @@
}

// now get the themes and languages from the project.
            ResourceRepository projectRes = mListener.getProjectResources();
// in cases where the opened file is not linked to a project, this could be null.
            if (projectRes != null) {
// get the configured resources for the project
Map<ResourceType, Map<String, ResourceValue>> configuredProjectRes =
mListener.getConfiguredProjectResources();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 88711e2..e199a4b 100644

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
//Synthetic comment -- @@ -575,7 +576,7 @@
* configuration selection.
* @return the framework resources or null if not found.
*/
        public ResourceRepository getFrameworkResources() {
return getFrameworkResources(getRenderingTarget());
}

//Synthetic comment -- @@ -585,7 +586,7 @@
* @param target the target for which to return the framework resources.
* @return the framework resources or null if not found.
*/
        public ResourceRepository getFrameworkResources(IAndroidTarget target) {
if (target != null) {
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index f9e103a..248af89 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -396,8 +396,8 @@
private void scanProject() {
ProjectResources resources = ResourceManager.getInstance().getProjectResources(mProject);
if (resources != null) {
            ResourceItem[] layouts = resources.getResources(ResourceType.LAYOUT);
            for (ResourceItem layout : layouts) {
List<ResourceFile> sources = layout.getSourceFileList();
for (ResourceFile source : sources) {
updateFileIncludes(source, false);








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
//Synthetic comment -- index 43c8fcd..7884163 100644

//Synthetic comment -- @@ -21,9 +21,9 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
//Synthetic comment -- @@ -126,19 +126,19 @@
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
//Synthetic comment -- @@ -194,7 +194,7 @@
*/
@Override
public String[] getPossibleValues(String prefix) {
        ResourceRepository repository = null;
boolean isSystem = false;

UiElementNode uiNode = getUiParent();
//Synthetic comment -- @@ -210,7 +210,7 @@
// If there's a prefix with "android:" in it, use the system resources
// Non-public framework resources are filtered out later.
AndroidTargetData data = editor.getTargetData();
            repository = data.getFrameworkResources();
isSystem = true;
}

//Synthetic comment -- @@ -263,18 +263,8 @@
sb.append(typeName).append('/');
String base = sb.toString();

                for (ResourceItem item : repository.getResources(resType)) {
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
//Synthetic comment -- index 327bd89..0bb0d26 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.resources.ResourceType;


//Synthetic comment -- @@ -29,9 +30,8 @@
*/
public static String getXmlString(ResourceType type, ResourceItem resourceItem,
boolean system) {
        if (type == ResourceType.ID && resourceItem.isDeclaredInline()) {
            if (resourceItem.isDeclaredInline()) {
return (system?"@android:":"@+") + type.getName() + "/" + resourceItem.getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceItem.java
deleted file mode 100644
//Synthetic comment -- index c340ffe..0000000

//Synthetic comment -- @@ -1,48 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 543719b..2d62c3a 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.FolderTypeRelationship;
//Synthetic comment -- @@ -160,8 +160,8 @@
Set<String> existing = new HashSet<String>();
ResourceManager manager = ResourceManager.getInstance();
ProjectResources projectResources = manager.getProjectResources(project);
        ResourceItem[] resources = projectResources.getResources(type);
        for (ResourceItem resource : resources) {
existing.add(resource.getName());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ConfigurableResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 2a998f8..0000000

//Synthetic comment -- @@ -1,82 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..6a10786

//Synthetic comment -- @@ -0,0 +1,29 @@
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

class FrameworkResourceItem extends ResourceItem {

    FrameworkResourceItem(String name) {
        super(name);
    }

    @Override
    public boolean isEditableDirectly() {
        return false;
    };
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FrameworkResources.java
new file mode 100644
//Synthetic comment -- index 0000000..88c3b8d

//Synthetic comment -- @@ -0,0 +1,179 @@
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
import static com.android.sdklib.SdkConstants.FD_DATA;
import static com.android.sdklib.SdkConstants.FD_RES;

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
import java.util.HashMap;
import java.util.HashSet;
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
public class FrameworkResources extends ResourceRepository {

    protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    public FrameworkResources() {
        super(true /*isFrameworkRepository*/);
    }

    /**
     * Returns an array (always non null, but can be empty) of {@link ResourceItem} matching
     * a given {@link ResourceType}, <b>and that are public</b>.
     * @param type the type of the resources to return
     * @return a non null array of resources
     */
    @Override
    public ResourceItem[] getResources(ResourceType type) {
        List<ResourceItem> items = mPublicResourceMap.get(type);

        return items.toArray(new ResourceItem[items.size()]);
    }

    /**
     * Returns whether the repository has <b>public</b> resources of a given {@link ResourceType}.
     * @param type the type of resource to check.
     * @return true if the repository contains resources of the given type, false otherwise.
     */
    @Override
    public boolean hasResources(ResourceType type) {
        List<ResourceItem> items = mPublicResourceMap.get(type);
        return (items != null && items.size() > 0);
    }

    @Override
    protected ResourceItem doCreateResourceItem(String name) {
        return new FrameworkResourceItem(name);
    }

    /**
     * Reads the public.xml file in data/res/values/ for this SDK and
     * returns the result as a map from resource type to a list of names
     */
    Map<ResourceType, Collection<String>> readPublicAttributeLists(String location) {
        String relative = FD_DATA + separator + FD_RES + separator + FD_RES_VALUES + separator +
            "public.xml"; //$NON-NLS-1$
        File file = new File(location, relative);
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/IdResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 8b142fb..0000000

//Synthetic comment -- @@ -1,54 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..802dec5

//Synthetic comment -- @@ -0,0 +1,45 @@
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
 * This covers the typically ID declaration of "@+id/foo", but does not cover normal value
 * resources declared in strings.xml or other similar value files.
 */
public class InlineResourceItem extends ResourceItem {

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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index 8f8e0d3..bf4b40c 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.io.IAbstractFile;
import com.android.io.StreamException;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.xml.sax.SAXException;

//Synthetic comment -- @@ -64,8 +65,7 @@

if (mResourceTypeList == null) {
Set<ResourceType> keys = mResourceItems.keySet();
            mResourceTypeList = new ArrayList<ResourceType>(keys);
mResourceTypeList = Collections.unmodifiableList(mResourceTypeList);
}

//Synthetic comment -- @@ -81,28 +81,29 @@
}

@Override
    public Collection<ResourceItem> getResources(ResourceType type,
            ResourceRepository resources) {
update();

HashMap<String, ResourceValue> list = mResourceItems.get(type);

        ArrayList<ResourceItem> items = new ArrayList<ResourceItem>();

if (list != null) {
Collection<ResourceValue> values = list.values();
for (ResourceValue res : values) {
                Pair<ResourceItem, Boolean> pair =
                    resources.createResourceItem(type, res.getName());

                ResourceItem item = pair.getFirst();

                // add it the list if it was just created (it could be exist from another
                // file that generates the same resource.)
                if (pair.getSecond()) {
items.add(item);
}

                // add this file to the list of files generating this resource item.
item.add(this);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResourceItem.java
deleted file mode 100644
//Synthetic comment -- index 845a974..0000000

//Synthetic comment -- @@ -1,91 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index b7f406d..020a530 100644

//Synthetic comment -- @@ -17,51 +17,31 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* Represents the resources of a project. This is a file view of the resources, with handling
* for the alternate resource types. For a compiled view use CompiledResources.
*/
public class ProjectResources extends ResourceRepository {
private final static int DYNAMIC_ID_SEED_START = 0; // this should not conflict with any
// project IDs that start at a much higher
// value

/** Map of (name, id) for resources of type {@link ResourceType#ID} coming from R.java */
private Map<ResourceType, Map<String, Integer>> mResourceValueMap;
/** Map of (id, [name, resType]) for all resources coming from R.java */
//Synthetic comment -- @@ -69,352 +49,36 @@
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
* @param referenceConfig the configuration that each value must match.
*/
    @Override
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
FolderConfiguration referenceConfig) {

        Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

// if the project contains libraries, we need to add the libraries resources here
//Synthetic comment -- @@ -438,22 +102,25 @@
// make sure they are loaded
libRes.loadAll();

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
//Synthetic comment -- @@ -462,62 +129,21 @@
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
//Synthetic comment -- @@ -572,55 +198,9 @@
}

/**
* Resets the list of dynamic Ids. This list is used by
* {@link #getResourceValue(String, String)} when the resource query is an ID that doesn't
     * exist (for example for ID automatically generated in layout files that are not saved yet.)
* <p/>This method resets those dynamic ID and must be called whenever the actual list of IDs
* change.
*/
//Synthetic comment -- @@ -631,262 +211,20 @@
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
//Synthetic comment -- @@ -900,24 +238,6 @@
}

/**
* Sets compiled resource information.
* @param resIdValueToNameMap a map of compiled resource id to resource name.
*  The map is acquired by the {@link ProjectResources} object.
//Synthetic comment -- @@ -934,77 +254,49 @@
mergeIdResources();
}

    @Override
    protected void postUpdate(ResourceType type) {
        if (type == ResourceType.ID) {
            mergeIdResources();
        }

        super.postUpdate(type);
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

        // remove from the main list all inline values that are not in the list coming from R.java
        Set<String> visited = new HashSet<String>();

        // remove all current inline values from it, unless they are
        for (int i = 0 ; i < resources.size() ;) {
            ResourceItem item = resources.get(i);
            if (item.isDeclaredInline()) {
                if (rResources.containsKey(item.getName()) == false) {
                    resources.remove(i);
                    visited.add(item.getName());
                } else {
                    i++;
}
} else {
                i++;
}
        }

        // loop through all ID in R.java and add the one that weren't found in the previous step.
        for (String resName : rResources.keySet()) {
            if (visited.contains(resName) == false) {
                resources.add(new InlineResourceItem(resName));
            }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index a6e10ae..7c861fc 100644

//Synthetic comment -- @@ -79,16 +79,16 @@
public abstract boolean hasResources(ResourceType type);

/**
     * Get the list of {@link ResourceItem} of a specific type generated by the file.
* This method must make sure not to create duplicate.
     * @param type The type of {@link ResourceItem} to return.
     * @param resources A global Resource object, allowing the implementation to query for already
     * existing {@link ResourceItem} to not reallocate them and keep the same instance.
     * @return The list of <b>new</b> {@link ResourceItem}
     * @see ResourceRepository#findResourceItem(ResourceType, String)
*/
    public abstract Collection<ResourceItem> getResources(ResourceType type,
            ResourceRepository resources);

/**
* Returns the value of a resource generated by this file by {@link ResourceType} and name.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 40f6fbb..634ae45 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
//Synthetic comment -- @@ -248,14 +247,14 @@
* @param projectResources The global Project Resource object, allowing the implementation to
* query for already existing {@link ResourceItem}
* @return The list of <b>new</b> {@link ResourceItem}
     * @see ResourceRepository#findResourceItem(ResourceType, String)
*/
    public Collection<ResourceItem> getResources(ResourceType type,
            ResourceRepository repository) {
        Collection<ResourceItem> list = new ArrayList<ResourceItem>();
if (mFiles != null) {
for (ResourceFile f : mFiles) {
                list.addAll(f.getResources(type, repository));
}
}
return list;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..b6e1579

//Synthetic comment -- @@ -0,0 +1,173 @@
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
 * Base class for Resource Item coming from an Android Project.
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
    private final ArrayList<ResourceFile> mFiles = new ArrayList<ResourceFile>();

    /**
     * Constructs a new ResourceItem.
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


    /**
     * Returns whether the resource item is editable directly.
     * <p/>
     * This is typically the case for resources that don't have alternate versions, or resources
     * of type {@link ResourceType#ID} that aren't declared inline.
     */
    public boolean isEditableDirectly() {
        return hasAlternates() == false;
    }

    /**
     * Returns whether the ID resource has been declared inline inside another resource XML file.
     */
    public boolean isDeclaredInline() {
        return false;
    }

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

    /**
     * Replaces the content of the receiver with the ResourceItem received as parameter.
     * @param item
     */
    protected void replaceWith(ResourceItem item) {
        mFiles.clear();
        mFiles.addAll(item.mFiles);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index c039f6b..4e969ba 100644

//Synthetic comment -- @@ -413,12 +413,12 @@
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
//Synthetic comment -- @@ -447,13 +447,13 @@
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
//Synthetic comment -- @@ -578,10 +578,10 @@
/**
* Processes a folder and adds it to the list of the project resources.
* @param folder the folder to process
     * @param resources the resource repository.
* @return the ConfiguredFolder created from this folder, or null if the process failed.
*/
    private ResourceFolder processFolder(IAbstractFolder folder, ResourceRepository resources) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -593,7 +593,7 @@
FolderConfiguration config = getConfig(folderSegments);

if (config != null) {
                ResourceFolder configuredFolder = resources.add(type, config, folder);

return configuredFolder;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
new file mode 100644
//Synthetic comment -- index 0000000..2e0811f

//Synthetic comment -- @@ -0,0 +1,741 @@
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
import com.android.util.Pair;

import org.eclipse.core.resources.IFolder;

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

/**
 * Represents the resources of a project. This is a file view of the resources, with handling
 * for the alternate resource types. For a compiled view use CompiledResources.
 */
public abstract class ResourceRepository {

    protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
        new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, List<ResourceItem>> mResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    private final boolean mIsFrameworkRepository;

    protected final IntArrayWrapper mWrapper = new IntArrayWrapper(null);

    /**
     * Makes a ProjectResources for a given <var>project</var>.
     * @param project the project.
     */
    protected ResourceRepository(boolean isFrameworkRepository) {
        mIsFrameworkRepository = isFrameworkRepository;
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
     * Returns a {@link ResourceItem} matching the given {@link ResourceType} and name.
     * If an item already exists, it is returned and the boolean in the {@link Pair} is set to
     * {@code false}. If it doesn't exist, it is created and the boolean is set to {@code true}.
     * @param type the resource type
     * @param name the name of the resource.
     * @return a pair containing the resource item and the creation state
     */
    protected Pair<ResourceItem, Boolean> createResourceItem(ResourceType type, String name) {
        // looking for an existing ResourceItem with this type and name
        ResourceItem item = findResourceItem(type, name);

        // create one if there isn't one already, or if the existing one is inlined, since
        // clearly we need a non inlined one (the inline one will be removed after)
        if (item == null || item.isDeclaredInline()) {
            return Pair.of(doCreateResourceItem(name), true);
        }

        return Pair.of(item, false);
    }

    protected abstract ResourceItem doCreateResourceItem(String name);


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
                            if (list.indexOf(folderResType) == -1) {
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
     * Returns an array (always non null, but can be empty) of {@link ResourceItem} matching
     * a given {@link ResourceType}.
     * @param type the type of the resources to return
     * @return a non null array of resources
     */
    public ResourceItem[] getResources(ResourceType type) {
        checkAndUpdate(type);

        List<ResourceItem> items = mResourceMap.get(type);

        return items.toArray(new ResourceItem[items.size()]);
    }

    /**
     * Returns whether the repository has resources of a given {@link ResourceType}.
     * @param type the type of resource to check.
     * @return true if the repository contains resources of the given type, false otherwise.
     */
    public boolean hasResources(ResourceType type) {
        checkAndUpdate(type);

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

        ResourceItem[] resources = getResources(type);

        for (ResourceItem item : resources) {
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
        return doGetConfiguredResources(referenceConfig);
    }

    /**
     * Returns the resources values matching a given {@link FolderConfiguration} for the current
     * project.
     * @param referenceConfig the configuration that each value must match.
     */
    protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
            FolderConfiguration referenceConfig) {

        Map<ResourceType, Map<String, ResourceValue>> map =
            new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);

        Set<ResourceType> keys = mResourceMap.keySet();
        for (ResourceType key : keys) {
            // get the local results and put them in the map
            map.put(key, getConfiguredResource(key, referenceConfig));
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

        // create the map
        HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>();

        for (ResourceItem item : items) {
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
        List<ResourceItem> newItems = mResourceMap.get(type);
        List<ResourceItem> oldItems = new ArrayList<ResourceItem>();

        if (newItems == null) {
            newItems = new ArrayList<ResourceItem>();
            mResourceMap.put(type, newItems);
        } else {
            // backup the list
            oldItems.addAll(newItems);

            // we reset the list itself.
            newItems.clear();
        }

        // get the list of folder that can output this type
        List<ResourceFolderType> folderTypes = FolderTypeRelationship.getRelatedFolders(type);

        for (ResourceFolderType folderType : folderTypes) {
            List<ResourceFolder> folders = mFolderMap.get(folderType);

            if (folders != null) {
                for (ResourceFolder folder : folders) {
                    newItems.addAll(folder.getResources(type, this));
                    folder.resetTouch();
                }
            }
        }

        // now items contains the new list. We "merge" it with the backup list.
        // Basically, we need to keep the old instances of ResourceItem (where applicable),
        // but replace them by the content of the new items.
        // This will let the resource explorer keep the expanded state of the nodes whose data
        // is a ResourceItem object.
        if (oldItems.size() > 0) {
            // this is not going to change as we're only replacing instances.
            final int newCount = newItems.size();

            for (int i = 0 ; i < newCount; i++) {
                // get the "new" item
                ResourceItem newItem = newItems.get(i);

                // look for a similar item in the old list.
                ResourceItem foundOldItem = null;
                for (ResourceItem oldItem : oldItems) {
                    if (oldItem.getName().equals(newItem.getName())) {
                        foundOldItem = oldItem;
                        break;
                    }
                }

                // if there was a previous item and there's no change in the inline value then
                // we replace the new item with the old item, keeping the new data.
                // (the inline can happen if an inlined ID resource becomes non-inlined or
                // vice-versa)
                if (foundOldItem != null &&
                        newItem.isDeclaredInline() != foundOldItem.isDeclaredInline()) {
                    // erase the data of the old item with the data from the new one.
                    foundOldItem.replaceWith(newItem);

                    // remove the old and new item from their respective lists
                    newItems.remove(i);
                    oldItems.remove(foundOldItem);

                    // add the old item to the new list in place of the current item
                    newItems.add(i, foundOldItem);
                }
            }
        }

        postUpdate(type);
    }

    protected void postUpdate(ResourceType type) {
        List<ResourceItem> items = mResourceMap.get(type);

        // this is the list that will actually be displayed, so we sort it.
        Collections.sort(items);
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

        for (ResourceItem item : list) {
            // ignore inline
            if (name.equals(item.getName()) && item.isDeclaredInline() == false) {
                return item;
            }
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 8677e5d..6c1ba2d 100644

//Synthetic comment -- @@ -22,9 +22,10 @@
import com.android.io.IAbstractFile;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
//Synthetic comment -- @@ -84,23 +85,25 @@
}

@Override
    public Collection<ResourceItem> getResources(ResourceType type,
            ResourceRepository resources) {

        // get a resource item matching the given type and name
        Pair<ResourceItem, Boolean> pair = resources.createResourceItem(type, mResourceName);

        ResourceItem item = pair.getFirst();

        // add this file to the list of files generating this resource item.
item.add(this);

        // if it was just created (it could be there from another
        // file that generates the same resource), return a list containing it
        if (pair.getSecond()) {
            return Collections.singletonList(item);
        }

        // it already existed, return an empty list.
        return Collections.emptyList();
}

/*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ccf4301..3ed971f 100644

//Synthetic comment -- @@ -16,12 +16,6 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -32,35 +26,18 @@
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.manager.FrameworkResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
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

    private FrameworkResources mFrameworkResources;
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
            FrameworkResources frameworkResources,
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
    public FrameworkResources getFrameworkResources() {
return mFrameworkResources;
}

//Synthetic comment -- @@ -360,124 +329,4 @@
mAttributeValues.remove(name);
mAttributeValues.put(name, values);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 6426fdb..8b535ea 100644

//Synthetic comment -- @@ -25,11 +25,8 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.manager.FrameworkResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
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
            FrameworkResources frameworkResources =
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
//Synthetic comment -- index 1a9a78f..62f30f9 100644

//Synthetic comment -- @@ -15,7 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.manager.FrameworkResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -196,9 +197,9 @@
Button button = (Button) event.widget;

// Open a resource chooser dialog for specified resource type.
                ProjectResources projectRepository = ResourceManager.getInstance()
.getProjectResources(mProject);
                FrameworkResources systemRepository = mTargetData.getFrameworkResources();
ResourceChooser dlg = new ResourceChooser(mProject, ResourceType.DIMEN,
projectRepository, systemRepository, getShell());
Text text = (Text) button.getData(PROP_TEXTFIELD);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index 2a170a4..d9056e8 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringWizard;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceRepository;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -66,7 +66,7 @@

private static IDialogSettings sDialogSettings = new DialogSettings("");

    private ResourceRepository mProjectResources;
private String mCurrentResource;
private FilteredTree mFilteredTree;
private Button mNewResButton;
//Synthetic comment -- @@ -77,10 +77,11 @@
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
//Synthetic comment -- @@ -177,7 +178,7 @@

mTreeViewer.setLabelProvider(new ResourceLabelProvider());
mTreeViewer.setContentProvider(new ResourceContentProvider(false /* fullLevels */));
        mTreeViewer.setInput(mProjectResources);
}

protected void handleSelection() {
//Synthetic comment -- @@ -339,7 +340,7 @@
*/
private void setupInitialSelection(ResourceType resourceType, String resourceName) {
// get all the resources of this type
        ResourceItem[] resourceItems = mProjectResources.getResources(resourceType);

for (ResourceItem resourceItem : resourceItems) {
if (resourceName.equals(resourceItem.getName())) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 87d1b75..2dec4c9 100644

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
//Synthetic comment -- @@ -77,10 +75,6 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -92,7 +86,8 @@

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
    private final ResourceRepository mProjectResources;
    private final ResourceRepository mSystemResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
private Button mSystemButton;
//Synthetic comment -- @@ -109,14 +104,15 @@
* @param parent the parent shell
*/
public ResourceChooser(IProject project, ResourceType type,
            ResourceRepository projectResources,
            ResourceRepository systemResources,
Shell parent) {
super(parent, new ResourceLabelProvider());
mProject = project;

mResourceType = type;
mProjectResources = projectResources;
        mSystemResources = systemResources;

mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -374,21 +370,12 @@
ResourceItem[] items = null;
if (mProjectButton.getSelection()) {
items = mProjectResources.getResources(mResourceType);
} else if (mSystemButton.getSelection()) {
            items = mSystemResources.getResources(mResourceType);
}

        setListElements(items);

return items;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceContentProvider.java
//Synthetic comment -- index f57b74e..673700f 100644

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
//Synthetic comment -- @@ -67,8 +66,8 @@
public Object[] getChildren(Object parentElement) {
if (parentElement instanceof ResourceType) {
return mResources.getResources((ResourceType)parentElement);
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







