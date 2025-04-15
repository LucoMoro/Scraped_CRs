/*Add framework resources to the Resource Chooser

Reenable system resources in the Resource Chooser.

Adds code to AndroidTargetData which on demand loads data from public.xml
such that it can provide a list of public resource names.

Also uses this data to filter out non-public resources from code
completion.

Change-Id:I98611668473543aaec56ce3bc2e28e6606c867fd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index 32e6167..43c8fcd 100644

//Synthetic comment -- @@ -208,8 +208,7 @@
}
} else {
// If there's a prefix with "android:" in it, use the system resources
            //
            // TODO find a way to only list *public* framework resources here.
AndroidTargetData data = editor.getTargetData();
repository = data.getSystemResources();
isSystem = true;
//Synthetic comment -- @@ -264,8 +263,18 @@
sb.append(typeName).append('/');
String base = sb.toString();

                for (ResourceItem item : repository.getResources(resType)) {
                    results.add(base + item.getName());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ae81ed6..7fbdd8a 100644

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -28,16 +33,33 @@
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
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
//Synthetic comment -- @@ -74,6 +96,7 @@
private XmlDescriptors mXmlDescriptors;

private Map<String, Map<String, Integer>> mEnumValueMap;

private ProjectResources mFrameworkResources;
private LayoutLibrary mLayoutLibrary;
//Synthetic comment -- @@ -336,4 +359,127 @@
mAttributeValues.remove(name);
mAttributeValues.put(name, values);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 1a6a9de..8662f26 100644

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -74,6 +76,10 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -84,19 +90,13 @@
public class ResourceChooser extends AbstractElementListSelectionDialog {

private Pattern mProjectResourcePattern;

private ResourceType mResourceType;

private IResourceRepository mProjectResources;

    private final static boolean SHOW_SYSTEM_RESOURCE = false;  // TODO re-enable at some point
private Pattern mSystemResourcePattern;
    private IResourceRepository mSystemResources;
private Button mProjectButton;
private Button mSystemButton;

private String mCurrentResource;

private final IProject mProject;

/**
//Synthetic comment -- @@ -120,11 +120,8 @@
mProjectResourcePattern = Pattern.compile(
"@" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$

        if (SHOW_SYSTEM_RESOURCE) {
            mSystemResources = systemResources;
            mSystemResourcePattern = Pattern.compile(
                    "@android:" + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$ //$NON-NLS-2$
        }

setTitle("Resource Chooser");
setMessage(String.format("Choose a %1$s resource",
//Synthetic comment -- @@ -146,7 +143,7 @@
ResourceItem item = (ResourceItem)elements[0];

mCurrentResource = ResourceHelper.getXmlString(mResourceType, item,
                    SHOW_SYSTEM_RESOURCE && mSystemButton.getSelection());
}
}

//Synthetic comment -- @@ -174,9 +171,6 @@
* @param top the parent composite
*/
private void createButtons(Composite top) {
        if (!SHOW_SYSTEM_RESOURCE) {
            return;
        }
mProjectButton = new Button(top, SWT.RADIO);
mProjectButton.setText("Project Resources");
mProjectButton.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -184,7 +178,8 @@
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
if (mProjectButton.getSelection()) {
                    setListElements(mProjectResources.getResources(mResourceType));
}
}
});
//Synthetic comment -- @@ -194,8 +189,9 @@
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
                if (mProjectButton.getSelection()) {
                    setListElements(mSystemResources.getResources(mResourceType));
}
}
});
//Synthetic comment -- @@ -206,16 +202,15 @@
* @param top the parent composite
*/
private void createNewResButtons(Composite top) {

        Button newResButton = new Button(top, SWT.NONE);

String title = String.format("New %1$s...", mResourceType.getDisplayName());
        newResButton.setText(title);

// We only support adding new strings right now
        newResButton.setEnabled(Hyperlinks.isValueResource(mResourceType));

        newResButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
//Synthetic comment -- @@ -227,8 +222,8 @@
String newName = createNewValue(mResourceType);
if (newName != null) {
// Recompute the "current resource" to select the new id
                        setupResourceList();
                        selectItemName(newName);
}
}
}
//Synthetic comment -- @@ -361,10 +356,10 @@
IDialogConstants.OK_ID) {

// Recompute the "current resource" to select the new id
                setupResourceList();

// select it if possible
                selectItemName(ref.getXmlStringId());
}
} catch (InterruptedException ex) {
// Interrupted. Pass.
//Synthetic comment -- @@ -372,37 +367,38 @@
}

/**
     * @return The repository currently selected.
     */
    private IResourceRepository getCurrentRepository() {
        IResourceRepository repo = mProjectResources;

        if (SHOW_SYSTEM_RESOURCE && mSystemButton.getSelection()) {
            repo = mSystemResources;
        }
        return repo;
    }

    /**
* Setups the current list.
*/
    private void setupResourceList() {
        IResourceRepository repo = getCurrentRepository();
        setListElements(repo.getResources(mResourceType));
}

/**
* Select an item by its name, if possible.
*/
    private void selectItemName(String itemName) {
        if (itemName == null) {
return;
}

        IResourceRepository repo = getCurrentRepository();

        ResourceItem[] items = repo.getResources(mResourceType);

for (ResourceItem item : items) {
if (itemName.equals(item.getName())) {
setSelection(new Object[] { item });
//Synthetic comment -- @@ -421,34 +417,30 @@

// Is this a system resource?
// If not a system resource or if they are not available, this will be a project res.
        if (SHOW_SYSTEM_RESOURCE) {
            Matcher m = mSystemResourcePattern.matcher(resourceString);
            if (m.matches()) {
                itemName = m.group(1);
                isSystem = true;
            }
}

if (!isSystem && itemName == null) {
// Try to match project resource name
            Matcher m = mProjectResourcePattern.matcher(resourceString);
if (m.matches()) {
itemName = m.group(1);
}
}

// Update the repository selection
        if (SHOW_SYSTEM_RESOURCE) {
            mProjectButton.setSelection(!isSystem);
            mSystemButton.setSelection(isSystem);
        }

// Update the list
        setupResourceList();

// If we have a selection name, select it
if (itemName != null) {
            selectItemName(itemName);
}
}








