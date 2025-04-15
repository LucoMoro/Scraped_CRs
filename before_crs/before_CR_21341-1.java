/*Disallow uppercase chars in file-based resource names

Modifies the resource name validator (used from the New XML File
wizard) to consider the resource type. While value-based resources
(like ids) can contain uppercase characters, they are not allowed in
file-based resource names, like layouts, animations, etc.

Also hooks up the resource name validator to the Resource Chooser's
"New Resource" dialog.

Change-Id:Ic5b948d73e9a8b17c6fdaa11cc38189202b04dc2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 7b0a46b..78853f5 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -446,12 +447,13 @@
}

/** Dialog asking for a Name/Value pair */
    private static class NameValueDialog extends SelectionStatusDialog implements Listener {
private org.eclipse.swt.widgets.Text mNameText;
private org.eclipse.swt.widgets.Text mValueText;
private String mInitialName;
private String mName;
private String mValue;

public NameValueDialog(Shell parent, String initialName) {
super(parent);
//Synthetic comment -- @@ -464,7 +466,7 @@
container.setLayout(new GridLayout(2, false));
GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
// Wide enough to accommodate the error label
            gridData.widthHint = 400;
container.setLayoutData(gridData);


//Synthetic comment -- @@ -520,7 +522,15 @@
} else if (mValue.length() == 0) {
status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, "Enter a value");
} else {
                status = new Status(IStatus.OK, AdtPlugin.PLUGIN_ID, null);
}
updateStatus(status);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 03aed88..9021951 100644

//Synthetic comment -- @@ -1255,12 +1255,6 @@
error = "Please select an Android project.";
}

        // -- validate filename
        if (error == null) {
            String fileName = getFileName();
            error = ResourceNameValidator.create(true).isValid(fileName);
        }

// -- validate type
if (error == null) {
TypeInfo type = getSelectedType();
//Synthetic comment -- @@ -1270,6 +1264,13 @@
}
}

// -- validate type API level
if (error == null) {
IAndroidTarget target = Sdk.getCurrent().getTarget(mProject);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java
//Synthetic comment -- index cc1aa25..a6c59e6 100644

//Synthetic comment -- @@ -20,8 +20,10 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceType;

//Synthetic comment -- @@ -40,12 +42,21 @@
/** Set of existing names to check for conflicts with */
private Set<String> mExisting;

/** If true, allow .xml as a name suffix */
private boolean mAllowXmlExtension;

    private ResourceNameValidator(boolean allowXmlExtension, Set<String> existing) {
mAllowXmlExtension = allowXmlExtension;
mExisting = existing;
}

public String isValid(String newText) {
//Synthetic comment -- @@ -71,7 +82,19 @@
for (int i = 1, n = newText.length(); i < n; i++) {
char c = newText.charAt(i);
if (!Character.isJavaIdentifierPart(c)) {
                    return String.format("%1$c is not a valid resource name character", c);
}
}

//Synthetic comment -- @@ -97,10 +120,13 @@
*
* @param allowXmlExtension if true, allow .xml to be entered as a suffix for the
*            resource name
* @return a new {@link ResourceNameValidator}
*/
    public static ResourceNameValidator create(boolean allowXmlExtension) {
        return new ResourceNameValidator(allowXmlExtension, null);
}

/**
//Synthetic comment -- @@ -110,10 +136,13 @@
*            resource name
* @param existing An optional set of names that already exist (and therefore will not
*            be considered valid if entered as the new name)
* @return a new {@link ResourceNameValidator}
*/
    public static ResourceNameValidator create(boolean allowXmlExtension, Set<String> existing) {
        return new ResourceNameValidator(allowXmlExtension, existing);
}

/**
//Synthetic comment -- @@ -135,6 +164,7 @@
existing.add(resource.getName());
}

        return new ResourceNameValidator(allowXmlExtension, existing);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidatorTest.java
//Synthetic comment -- index 5ee6793..4c4b292 100644

//Synthetic comment -- @@ -13,8 +13,12 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import java.util.Collections;

import junit.framework.TestCase;
//Synthetic comment -- @@ -22,18 +26,25 @@
public class ResourceNameValidatorTest extends TestCase {
public void testValidator() throws Exception {
// Valid
        assertTrue(ResourceNameValidator.create(true).isValid("foo") == null);
        assertTrue(ResourceNameValidator.create(true).isValid("foo.xml") == null);
        assertTrue(ResourceNameValidator.create(true).isValid("Foo123_$") == null);

// Invalid
        assertTrue(ResourceNameValidator.create(true).isValid("") != null);
        assertTrue(ResourceNameValidator.create(true).isValid(" ") != null);
        assertTrue(ResourceNameValidator.create(true).isValid("foo.xm") != null);
        assertTrue(ResourceNameValidator.create(true).isValid("foo bar") != null);
        assertTrue(ResourceNameValidator.create(true).isValid("1foo") != null);
        assertTrue(ResourceNameValidator.create(true).isValid("foo%bar") != null);
        assertTrue(ResourceNameValidator.create(true, Collections.singleton("foo"))
                .isValid("foo") != null);
}
}







