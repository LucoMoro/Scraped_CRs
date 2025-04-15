/*Add framework resources to the Resource Chooser

Reenable system resources in the Resource Chooser.

Adds code to AndroidTargetData which on demand loads data from public.xml
such that it can provide a list of public resource names.

Also uses this data to filter out non-public resources from code
completion.

Change-Id:I98611668473543aaec56ce3bc2e28e6606c867fd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 944f7d4..ce299e4 100755

//Synthetic comment -- @@ -876,7 +876,7 @@
public String displayResourceInput(String resourceTypeName, String currentValue) {
AndroidXmlEditor editor = mEditor.getLayoutEditor();
IProject project = editor.getProject();
            ResourceType type = ResourceType.getEnum(resourceTypeName);
if (project != null) {
// get the resource repository for this project and the system resources.
IResourceRepository projectRepository = ResourceManager.getInstance()








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 32e6167..43c8fcd 100644

//Synthetic comment -- @@ -208,8 +208,7 @@
}
} else {
// If there's a prefix with "android:" in it, use the system resources
            // Non-public framework resources are filtered out later.
AndroidTargetData data = editor.getTargetData();
repository = data.getSystemResources();
isSystem = true;
//Synthetic comment -- @@ -264,8 +263,18 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ae81ed6..ad18ceb 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.sdklib.SdkConstants.FD_DATA;
import static com.android.sdklib.SdkConstants.FD_RES;
import static com.android.sdklib.SdkConstants.FD_VALUES;
import static java.io.File.separator;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -28,16 +33,33 @@
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
//Synthetic comment -- @@ -74,6 +96,7 @@
private XmlDescriptors mXmlDescriptors;

private Map<String, Map<String, Integer>> mEnumValueMap;
    private Map<ResourceType, Collection<String>> mPublicAttributeNames;

private ProjectResources mFrameworkResources;
private LayoutLibrary mLayoutLibrary;
//Synthetic comment -- @@ -336,4 +359,124 @@
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
        String relative = FD_DATA + separator + FD_RES + separator + FD_VALUES + separator +
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 1a6a9de..7b0a46b 100644

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -74,6 +76,10 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -84,19 +90,13 @@
public class ResourceChooser extends AbstractElementListSelectionDialog {

private Pattern mProjectResourcePattern;
private ResourceType mResourceType;
private IResourceRepository mProjectResources;
private Pattern mSystemResourcePattern;
private Button mProjectButton;
private Button mSystemButton;
    private Button mNewButton;
private String mCurrentResource;
private final IProject mProject;

/**
//Synthetic comment -- @@ -120,11 +120,8 @@
mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$

        mSystemResourcePattern = Pattern.compile(
                "@android:" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$

setTitle("Resource Chooser");
setMessage(String.format("Choose a %1$s resource",
//Synthetic comment -- @@ -146,7 +143,7 @@
ResourceItem item = (ResourceItem)elements[0];

mCurrentResource = ResourceHelper.getXmlString(mResourceType, item,
                    mSystemButton.getSelection());
}
}

//Synthetic comment -- @@ -174,9 +171,6 @@
* @param top the parent composite
*/
private void createButtons(Composite top) {
mProjectButton = new Button(top, SWT.RADIO);
mProjectButton.setText("Project Resources");
mProjectButton.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -184,7 +178,8 @@
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
if (mProjectButton.getSelection()) {
                    setupResourceList();
                    mNewButton.setEnabled(true);
}
}
});
//Synthetic comment -- @@ -194,8 +189,9 @@
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
                if (mSystemButton.getSelection()) {
                    setupResourceList();
                    mNewButton.setEnabled(false);
}
}
});
//Synthetic comment -- @@ -206,16 +202,15 @@
* @param top the parent composite
*/
private void createNewResButtons(Composite top) {
        mNewButton = new Button(top, SWT.NONE);

String title = String.format("New %1$s...", mResourceType.getDisplayName());
        mNewButton.setText(title);

// We only support adding new strings right now
        mNewButton.setEnabled(Hyperlinks.isValueResource(mResourceType));

        mNewButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
//Synthetic comment -- @@ -227,8 +222,8 @@
String newName = createNewValue(mResourceType);
if (newName != null) {
// Recompute the "current resource" to select the new id
                        ResourceItem[] items = setupResourceList();
                        selectItemName(newName, items);
}
}
}
//Synthetic comment -- @@ -361,10 +356,10 @@
IDialogConstants.OK_ID) {

// Recompute the "current resource" to select the new id
                ResourceItem[] items = setupResourceList();

// select it if possible
                selectItemName(ref.getXmlStringId(), items);
}
} catch (InterruptedException ex) {
// Interrupted. Pass.
//Synthetic comment -- @@ -372,37 +367,38 @@
}

/**
* Setups the current list.
*/
    private ResourceItem[] setupResourceList() {
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

/**
* Select an item by its name, if possible.
*/
    private void selectItemName(String itemName, ResourceItem[] items) {
        if (itemName == null || items == null) {
return;
}

for (ResourceItem item : items) {
if (itemName.equals(item.getName())) {
setSelection(new Object[] { item });
//Synthetic comment -- @@ -421,34 +417,31 @@

// Is this a system resource?
// If not a system resource or if they are not available, this will be a project res.
        Matcher m = mSystemResourcePattern.matcher(resourceString);
        if (m.matches()) {
            itemName = m.group(1);
            isSystem = true;
}

if (!isSystem && itemName == null) {
// Try to match project resource name
            m = mProjectResourcePattern.matcher(resourceString);
if (m.matches()) {
itemName = m.group(1);
}
}

// Update the repository selection
        mProjectButton.setSelection(!isSystem);
        mSystemButton.setSelection(isSystem);
        mNewButton.setEnabled(!isSystem);

// Update the list
        ResourceItem[] items = setupResourceList();

// If we have a selection name, select it
if (itemName != null) {
            selectItemName(itemName, items);
}
}








