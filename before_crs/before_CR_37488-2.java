/*32745: Lack of warning of using duplicated IDs for element.

This changeset adds a validator to the assign/edit id dialog used in
the layout editor such that the user gets a warning if picking an id
which is already defined within the same layout.

Also cleans up the Rules API for adding a validator and makes the
generic resource validator handle both the case of requiring a unique
name and requiring an existing name.

Change-Id:Id9642c3bcd326f9734cf98c98f6799b67e11a4ae*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 4cc5f5e..2c9dcbe 100644

//Synthetic comment -- @@ -194,7 +194,11 @@
// Strip off the @id prefix stuff
String oldId = node.getStringAttr(ANDROID_URI, ATTR_ID);
oldId = stripIdPrefix(ensureValidString(oldId));
                        IValidator validator = mRulesEngine.getResourceValidator();
String newId = mRulesEngine.displayInput("New Id:", oldId, validator);
if (newId != null && newId.trim().length() > 0) {
if (!newId.startsWith(NEW_ID_PREFIX)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index 9830f7e..a22b29a 100644

//Synthetic comment -- @@ -15,10 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML.ContentTypeID_XML;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index e793983..83a4e42 100644

//Synthetic comment -- @@ -16,10 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.sdklib.SdkConstants.CLASS_FRAGMENT;
import static com.android.sdklib.SdkConstants.CLASS_V4_FRAGMENT;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -30,6 +33,7 @@
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.actions.AddCompatibilityJarAction;
//Synthetic comment -- @@ -45,6 +49,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
//Synthetic comment -- @@ -91,11 +96,17 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
//Synthetic comment -- @@ -208,10 +219,61 @@
}

@Override
    public @Nullable IValidator getResourceValidator() {
        //return ResourceNameValidator.create(false, mRulesEngine.getEditor().getProject(),
        //        ResourceType.ID);
        return null;
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java
//Synthetic comment -- index 657c9ec..f753408 100644

//Synthetic comment -- @@ -16,7 +16,6 @@
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
//Synthetic comment -- @@ -30,6 +29,7 @@
import static com.android.resources.ResourceType.LAYOUT;
import static com.android.sdklib.SdkConstants.FD_RES;
import static com.android.util.XmlUtils.ANDROID_NS_NAME;
import static com.android.util.XmlUtils.XMLNS;
import static com.android.util.XmlUtils.XMLNS_COLON;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintListDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintListDialog.java
//Synthetic comment -- index 147327d..57a85e8 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
super(parentShell);
mFile = file;
mEditor = editor;
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 188bbc7..5208ed8 100644

//Synthetic comment -- @@ -42,6 +42,12 @@
/** Set of existing names to check for conflicts with */
private Set<String> mExisting;

/**
* True if the resource name being considered is a "file" based resource (where the
* resource name is the actual file name, rather than just a value attribute inside an
//Synthetic comment -- @@ -65,6 +71,30 @@
mIsImageType = isImageType;
}

@Override
public String isValid(String newText) {
// IValidator has the same interface as SWT's IInputValidator
//Synthetic comment -- @@ -130,8 +160,14 @@
return String.format("%1$s is not a valid name (reserved Java keyword)", newText);
}

            if (mExisting != null && mExisting.contains(newText)) {
                return String.format("%1$s already exists", newText);
}

return null;
//Synthetic comment -- @@ -170,11 +206,12 @@
ResourceType type) {
boolean isFileType = ResourceHelper.isFileBasedResourceType(type);
return new ResourceNameValidator(allowXmlExtension, existing, isFileType,
                type == ResourceType.DRAWABLE);
}

/**
     * Creates a new {@link ResourceNameValidator}
*
* @param allowXmlExtension if true, allow .xml to be entered as a suffix for the
*            resource name








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 7b90633..d2747a9 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;

import com.android.annotations.NonNull;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
//Synthetic comment -- @@ -367,15 +368,19 @@
} else {
if (ResourceHelper.isValueBasedResourceType(mResourceType)) {
String newName = createNewValue(mResourceType);
                        selectAddedItem(newName);
} else {
String newName = createNewFile(mResourceType);
                        selectAddedItem(newName);
}
}
}

            private void selectAddedItem(String newName) {
// Recompute the "current resource" to select the new id
ResourceItem[] items = setupResourceList();

//Synthetic comment -- @@ -506,6 +511,7 @@
}
}

private String createNewFile(ResourceType type) {
// Show a name/value dialog entering the key name and the value
Shell shell = AdtPlugin.getDisplay().getActiveShell();
//Synthetic comment -- @@ -538,6 +544,7 @@
}


private String createNewValue(ResourceType type) {
// Show a name/value dialog entering the key name and the value
Shell shell = AdtPlugin.getDisplay().getActiveShell();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index 312df7d..826f36c 100644

//Synthetic comment -- @@ -16,8 +16,8 @@


package com.android.ide.common.layout;
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -265,7 +265,8 @@
}

@Override
        public @NonNull IValidator getResourceValidator() {
fail("Not supported in tests yet");
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index 97408c3..af0ba2b 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.resources.ResourceType;

import java.util.Collections;

import junit.framework.TestCase;

//Synthetic comment -- @@ -61,4 +63,28 @@
assertTrue(ResourceNameValidator.create(true, ResourceFolderType.DRAWABLE)
.isValid("_foo") != null);
}
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java b/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index b9ea6cb..329f38c 100644

//Synthetic comment -- @@ -106,11 +106,25 @@
/**
* Returns a resource name validator for the current project
*
     * @return an {@link IValidator} for validating a new resource name in the current
     *         project
*/
@Nullable
    IValidator getResourceValidator();

/**
* Displays an input dialog where the user can enter an Android reference value
//Synthetic comment -- @@ -119,7 +133,7 @@
* @return the reference selected by the user, or null
*/
@Nullable
    String displayReferenceInput(String currentValue);

/**
* Displays an input dialog where the user can enter an Android resource name of the







