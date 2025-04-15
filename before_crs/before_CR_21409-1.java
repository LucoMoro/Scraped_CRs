/*Tweak resource name validator, move code around

Incorporate some feedback fromhttps://review.source.android.com//#change,21341Disallow uppercase chars in file-based resource names

Change-Id:I078b30be64e1aeb984f26d5c8b2976aa0083fcf4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 5e662bf..faa21ed 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -50,6 +49,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -488,24 +488,6 @@
}

/**
     * Is this a resource that can be defined in any file within the "values" folder?
     *
     * @param type the resource type to check
     * @return true if the given resource type can be represented as a value under the
     *         values/ folder
     */
    public static boolean isValueResource(ResourceType type) {
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
        for (ResourceFolderType folderType : folderTypes) {
            if (folderType == ResourceFolderType.VALUES) {
                return true;
            }
        }

        return false;
    }

    /**
* Returns the XML tag containing an element description for value items of the given
* resource type
*
//Synthetic comment -- @@ -1181,8 +1163,7 @@
});

// Is this something found in a values/ folder?
                boolean valueResource = isValueResource(type);
                //boolean fileResource = isFileResource(type);

for (ResourceFile file : matches) {
String folderName = file.getFolder().getFolder().getName();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 78853f5..4ae2b38 100644

//Synthetic comment -- @@ -208,8 +208,8 @@
String title = String.format("New %1$s...", mResourceType.getDisplayName());
mNewButton.setText(title);

        // We only support adding new strings right now
        mNewButton.setEnabled(Hyperlinks.isValueResource(mResourceType));

mNewButton.addSelectionListener(new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -219,7 +219,7 @@
if (mResourceType == ResourceType.STRING) {
createNewString();
} else {
                    assert Hyperlinks.isValueResource(mResourceType);
String newName = createNewValue(mResourceType);
if (newName != null) {
// Recompute the "current resource" to select the new id








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java
//Synthetic comment -- index a6c59e6..6a315b9 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
//Synthetic comment -- @@ -91,7 +91,7 @@
// "%s: Invalid file name: must contain only [a-z0-9_.]","
for (int i = 0, n = newText.length(); i < n; i++) {
char c = newText.charAt(i);
                    if (!(c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_') {
return String.format(
"File-based resource names must contain only lowercase a-z, 0-9, or _.");
}
//Synthetic comment -- @@ -141,7 +141,7 @@
*/
public static ResourceNameValidator create(boolean allowXmlExtension, Set<String> existing,
ResourceType type) {
        boolean isFileType = !Hyperlinks.isValueResource(type);
return new ResourceNameValidator(allowXmlExtension, existing, isFileType);
}

//Synthetic comment -- @@ -164,7 +164,54 @@
existing.add(resource.getName());
}

        boolean isFileType = !Hyperlinks.isValueResource(type);
return new ResourceNameValidator(allowXmlExtension, existing, isFileType);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidatorTest.java
//Synthetic comment -- index 4c4b292..303b2ef 100644

//Synthetic comment -- @@ -45,6 +45,32 @@
// Only lowercase chars allowed in file-based resource names
assertTrue(ResourceNameValidator.create(true, ResourceFolderType.LAYOUT)
.isValid("Foo123_$") != null);

}
}







