/*32745: Lack of warning of using duplicated IDs for element.

This changeset adds a validator to the assign/edit id dialog used in
the layout editor such that the user gets a warning if picking an id
which is already defined within the same layout.

Also cleans up the Rules API for adding a validator and makes the
generic resource validator handle both the case of requiring a unique
name and requiring an existing name.

Change-Id:Id9642c3bcd326f9734cf98c98f6799b67e11a4ae*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 4cc5f5e..a1a1bd0 100644

//Synthetic comment -- @@ -194,7 +194,11 @@
// Strip off the @id prefix stuff
String oldId = node.getStringAttr(ANDROID_URI, ATTR_ID);
oldId = stripIdPrefix(ensureValidString(oldId));
                        IValidator validator = mRulesEngine.getResourceValidator("id",
                                false /*uniqueInProject*/,
                                true /*uniqueInLayout*/,
                                false /*exists*/,
                                oldId);
String newId = mRulesEngine.displayInput("New Id:", oldId, validator);
if (newId != null && newId.trim().length() > 0) {
if (!newId.startsWith(NEW_ID_PREFIX)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index 9830f7e..a22b29a 100644

//Synthetic comment -- @@ -15,10 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.util.XmlUtils.ANDROID_URI;
import static org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML.ContentTypeID_XML;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index e793983..78183f6 100644

//Synthetic comment -- @@ -16,10 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.sdklib.SdkConstants.CLASS_FRAGMENT;
import static com.android.sdklib.SdkConstants.CLASS_V4_FRAGMENT;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -30,6 +33,7 @@
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseViewRule;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.actions.AddCompatibilityJarAction;
//Synthetic comment -- @@ -45,6 +49,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.CyclicDependencyValidator;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
//Synthetic comment -- @@ -91,11 +96,17 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
//Synthetic comment -- @@ -208,10 +219,61 @@
}

@Override
    public IValidator getResourceValidator(
            @NonNull final String resourceTypeName, final boolean uniqueInProject,
            final boolean uniqueInLayout, final boolean exists, final String... allowed) {
        return new IValidator() {
            private ResourceNameValidator mValidator;

            @Override
            public String validate(String text) {
                if (mValidator == null) {
                    ResourceType type = ResourceType.getEnum(resourceTypeName);
                    if (uniqueInLayout) {
                        assert !uniqueInProject;
                        assert !exists;
                        Set<String> existing = new HashSet<String>();
                        Document doc = mRulesEngine.getEditor().getModel().getXmlDocument();
                        if (doc != null) {
                            addIds(doc, existing);
                        }
                        for (String s : allowed) {
                            existing.remove(s);
                        }
                        mValidator = ResourceNameValidator.create(false, existing, type);
                    } else {
                        assert allowed.length == 0;
                        IProject project = mRulesEngine.getEditor().getProject();
                        mValidator = ResourceNameValidator.create(false, project, type);
                        if (uniqueInProject) {
                            mValidator.unique();
                        }
                    }
                    if (exists) {
                        mValidator.exist();
                    }
                }

                return mValidator.isValid(text);
            }
        };
    }

    /** Find declared ids under the given DOM node */
    private static void addIds(Node node, Set<String> ids) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String id = element.getAttributeNS(ANDROID_URI, ATTR_ID);
            if (id.startsWith(NEW_ID_PREFIX)) {
                ids.add(BaseViewRule.stripIdPrefix(id));
            }
        }

        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            Node child = children.item(i);
            addIds(child, ids);
        }
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java
//Synthetic comment -- index 657c9ec..f753408 100644

//Synthetic comment -- @@ -16,7 +16,6 @@
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
//Synthetic comment -- @@ -30,6 +29,7 @@
import static com.android.resources.ResourceType.LAYOUT;
import static com.android.sdklib.SdkConstants.FD_RES;
import static com.android.util.XmlUtils.ANDROID_NS_NAME;
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.util.XmlUtils.XMLNS;
import static com.android.util.XmlUtils.XMLNS_COLON;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintListDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintListDialog.java
//Synthetic comment -- index 147327d..57a85e8 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
super(parentShell);
mFile = file;
mEditor = editor;
        setHelpAvailable(false);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 188bbc7..5208ed8 100644

//Synthetic comment -- @@ -42,6 +42,12 @@
/** Set of existing names to check for conflicts with */
private Set<String> mExisting;

    /** If true, the validated name must be unique */
    private boolean mUnique = true;

    /** If true, the validated name must exist */
    private boolean mExist;

/**
* True if the resource name being considered is a "file" based resource (where the
* resource name is the actual file name, rather than just a value attribute inside an
//Synthetic comment -- @@ -65,6 +71,30 @@
mIsImageType = isImageType;
}

    /**
     * Makes the resource name validator require that names are unique.
     *
     * @return this, for construction chaining
     */
    public ResourceNameValidator unique() {
        mUnique = true;
        mExist = false;

        return this;
    }

    /**
     * Makes the resource name validator require that names already exist
     *
     * @return this, for construction chaining
     */
    public ResourceNameValidator exist() {
        mExist = true;
        mUnique = false;

        return this;
    }

@Override
public String isValid(String newText) {
// IValidator has the same interface as SWT's IInputValidator
//Synthetic comment -- @@ -130,8 +160,14 @@
return String.format("%1$s is not a valid name (reserved Java keyword)", newText);
}


            if (mExisting != null && (mUnique || mExist)) {
                boolean exists = mExisting.contains(newText);
                if (mUnique && exists) {
                    return String.format("%1$s already exists", newText);
                } else if (mExist && !exists) {
                    return String.format("%1$s does not exist", newText);
                }
}

return null;
//Synthetic comment -- @@ -170,11 +206,12 @@
ResourceType type) {
boolean isFileType = ResourceHelper.isFileBasedResourceType(type);
return new ResourceNameValidator(allowXmlExtension, existing, isFileType,
                type == ResourceType.DRAWABLE).unique();
}

/**
     * Creates a new {@link ResourceNameValidator}. By default, the name will need to be
     * unique in the project.
*
* @param allowXmlExtension if true, allow .xml to be entered as a suffix for the
*            resource name








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 7b90633..d2747a9 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
//Synthetic comment -- @@ -367,15 +368,19 @@
} else {
if (ResourceHelper.isValueBasedResourceType(mResourceType)) {
String newName = createNewValue(mResourceType);
                        if (newName != null) {
                            selectAddedItem(newName);
                        }
} else {
String newName = createNewFile(mResourceType);
                        if (newName != null) {
                            selectAddedItem(newName);
                        }
}
}
}

            private void selectAddedItem(@NonNull String newName) {
// Recompute the "current resource" to select the new id
ResourceItem[] items = setupResourceList();

//Synthetic comment -- @@ -506,6 +511,7 @@
}
}

    @Nullable
private String createNewFile(ResourceType type) {
// Show a name/value dialog entering the key name and the value
Shell shell = AdtPlugin.getDisplay().getActiveShell();
//Synthetic comment -- @@ -538,6 +544,7 @@
}


    @Nullable
private String createNewValue(ResourceType type) {
// Show a name/value dialog entering the key name and the value
Shell shell = AdtPlugin.getDisplay().getActiveShell();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index 312df7d..826f36c 100644

//Synthetic comment -- @@ -16,8 +16,8 @@


package com.android.ide.common.layout;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -265,7 +265,8 @@
}

@Override
        public IValidator getResourceValidator(String resourceTypeName, boolean uniqueInProject,
                boolean uniqueInLayout, boolean exists, String... allowed) {
fail("Not supported in tests yet");
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index 97408c3..af0ba2b 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.resources.ResourceType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

//Synthetic comment -- @@ -61,4 +63,28 @@
assertTrue(ResourceNameValidator.create(true, ResourceFolderType.DRAWABLE)
.isValid("_foo") != null);
}

    public void testUniqueOrExists() throws Exception {
        Set<String> existing = new HashSet<String>();
        existing.add("foo1");
        existing.add("foo2");
        existing.add("foo3");

        ResourceNameValidator validator = ResourceNameValidator.create(true, existing,
                ResourceType.ID);
        validator.unique();

        assertNull(validator.isValid("foo")); // null: ok (no error message)
        assertNull(validator.isValid("foo4"));
        assertNotNull(validator.isValid("foo1"));
        assertNotNull(validator.isValid("foo2"));
        assertNotNull(validator.isValid("foo3"));

        validator.exist();
        assertNotNull(validator.isValid("foo"));
        assertNotNull(validator.isValid("foo4"));
        assertNull(validator.isValid("foo1"));
        assertNull(validator.isValid("foo2"));
        assertNull(validator.isValid("foo3"));
    }
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java b/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index b9ea6cb..329f38c 100644

//Synthetic comment -- @@ -106,11 +106,25 @@
/**
* Returns a resource name validator for the current project
*
     * @param resourceTypeName resource type, such as "id", "string", and so on
     * @param uniqueInProject if true, the resource name must be unique in the
     *            project (not already be defined anywhere else)
     * @param uniqueInLayout if true, the resource name must be unique at least
     *            within the current layout. This only applies to {@code @id}
     *            resources since only those resources can be defined in-place
     *            within a layout
     * @param exists if true, the resource name must already exist
     * @param allowed allowed names (optional). This can for example be used to
     *            request a unique-in-layout validator, but to remove the
     *            current value of the node being edited from consideration such
     *            that it allows you to leave the value the same
     * @return an {@link IValidator} for validating a new resource name in the
     *         current project
*/
@Nullable
    IValidator getResourceValidator(@NonNull String resourceTypeName,
            boolean uniqueInProject, boolean uniqueInLayout, boolean exists,
            String... allowed);

/**
* Displays an input dialog where the user can enter an Android reference value
//Synthetic comment -- @@ -119,7 +133,7 @@
* @return the reference selected by the user, or null
*/
@Nullable
    String displayReferenceInput(@Nullable String currentValue);

/**
* Displays an input dialog where the user can enter an Android resource name of the







