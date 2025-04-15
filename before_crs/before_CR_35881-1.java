/*Make XML code completion work for custom views & attributes

This changeset improves the custom view handling such that XML code
completion offers any custom attributes (along with documentation
tooltips and type information). This is done by finding any
declare-styleable attributes defined in the project defining the
custom view. In particular, this will also work for the GridLayout
library project shipped as part of the android.support package.

The fix is not tied to completion; it's improving the metadata
descriptors computed for custom views, so this fix for example also
makes these custom attributes show up in the property sheet in the
layout editor.

Finally, this changeset fixes a couple of bugs in this area:
- One initialization path was not looking up custom views for unknown
  descriptors, this might be the fix forhttp://code.google.com/p/android/issues/detail?id=23020- There was a bug in the code which looks up the namespace prefix to
  use for a given namespace URI: it would return the default Android
  prefix for some non-Android URIs.
- Small performance tweak to avoid regexp construction in a loop where
  it's not needed

Change-Id:I55dfcea6e6ea9d7c38e18a47b757678176facbd2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 3129f4d..bfe9bdb 100644

//Synthetic comment -- @@ -172,6 +172,7 @@
* The top level android package as a prefix, "android.".
*/
public static final String ANDROID_PKG_PREFIX = ANDROID_PKG + '.';

/** The android.view. package prefix */
public static final String ANDROID_VIEW_PKG = ANDROID_PKG_PREFIX + "view."; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index ec3d8a4..7b3e8a3 100755

//Synthetic comment -- @@ -75,6 +75,15 @@
mDeprecatedDoc = info.mDeprecatedDoc;
}

/** Returns the XML Name of the attribute */
@Override
public String getName() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 52b0e15..ff203c4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.log.ILogger;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -197,7 +198,7 @@
String xmlName = String.format("%1$s_%2$s", //$NON-NLS-1$
viewLayoutClass.getShortClassName(),
info.getShortClassName());
            xmlName = xmlName.replaceFirst("Params$", ""); //$NON-NLS-1$ //$NON-NLS-2$

DeclareStyleableInfo style = mStyleMap.get(xmlName);
if (style != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 7e2a44d..5e4f5ad 100644

//Synthetic comment -- @@ -80,6 +80,29 @@
}

/**
* Returns true if the given sequence ends at the given offset with the given suffix (case
* sensitive)
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index 8d38c7c..7da3cc0 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -35,7 +36,9 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -245,12 +248,16 @@
final IProject newProject;
try {
IProgressMonitor monitor = new NullProgressMonitor();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

String name = AdtUtils.getUniqueProjectName(
"gridlayout_v7", "_"); //$NON-NLS-1$ //$NON-NLS-2$
newProject = root.getProject(name);
            newProject.create(monitor);

// Copy in the files recursively
IFileSystem fileSystem = EFS.getLocalFileSystem();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index b7c95b5..7a2658b 100644

//Synthetic comment -- @@ -305,7 +305,7 @@
Node attr = attrs.item(n);
if (XmlnsAttributeDescriptor.XMLNS.equals(attr.getPrefix())) {
String uri = attr.getNodeValue();
                    if (SdkConstants.NS_RESOURCES.equals(uri)) {
return attr.getLocalName();
}
visited.add(uri);
//Synthetic comment -- @@ -316,7 +316,7 @@
// Use a sensible default prefix if we can't find one.
// We need to make sure the prefix is not one that was declared in the scope
// visited above.
        prefix = SdkConstants.NS_RESOURCES.equals(nsUri) ? "android" : "ns"; //$NON-NLS-1$ //$NON-NLS-2$
String base = prefix;
for (int i = 1; visited.contains(prefix); i++) {
prefix = base + Integer.toString(i);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index b00656e..19ff37a 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
//Synthetic comment -- @@ -100,7 +101,7 @@
*        entries in the form "elem-name/attr-name". Elem-name can be "*".
* @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
    public static void appendAttributes(ArrayList<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri, AttributeInfo[] infos,
Set<String> requiredAttributes,
//Synthetic comment -- @@ -131,7 +132,7 @@
*        a "*" to its UI name as a hint for the user.)
* @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
    public static void appendAttribute(ArrayList<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri,
AttributeInfo info, boolean required,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 9e821ad..325e284 100644

//Synthetic comment -- @@ -750,7 +750,10 @@

// Check if we can find a custom view specific to this project.
// This only works if there's an actual matching custom class in the project.
            desc = CustomViewDescriptorService.getInstance().getDescriptor(project, xmlLocalName);

if (desc == null) {
// If we didn't find a custom view, create a synthetic one using the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 5abbb0c..9aa7fac 100644

//Synthetic comment -- @@ -16,19 +16,43 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.sdklib.SdkConstants.CLASS_VIEWGROUP;

import com.android.ide.common.resources.platform.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
//Synthetic comment -- @@ -36,8 +60,13 @@
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.graphics.Image;

import java.util.HashMap;
import java.util.List;

/**
* Service responsible for creating/managing {@link ViewElementDescriptor} objects for custom
//Synthetic comment -- @@ -162,11 +191,24 @@

if (parentDescriptor != null) {
// we have a valid parent, lets create a new ViewElementDescriptor.

String name = DescriptorsUtils.getBasename(fqcn);
ViewElementDescriptor descriptor = new CustomViewDescriptor(name, fqcn,
                                getAttributeDescriptor(type, parentDescriptor),
                                getLayoutAttributeDescriptors(type, parentDescriptor),
parentDescriptor.getChildren());
descriptor.setSuperClass(parentDescriptor);

//Synthetic comment -- @@ -193,6 +235,168 @@
return null;
}

/**
* Computes (if needed) and returns the {@link ViewElementDescriptor} for the specified type.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index 5bd35a1..435529a 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
AttributeDescriptor[] direct_attrs = super.getAttributeDescriptors();
mCachedAttributeDescriptors = direct_attrs;

AttributeDescriptor[] layout_attrs = null;
boolean need_xmlns = false;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index f749e2b..a49a0aa 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
//Synthetic comment -- @@ -441,7 +442,11 @@
IField field = type.getField(layoutName);
if (field.exists()) {
SearchPattern pattern = SearchPattern.createPattern(field, REFERENCES);
                    search(requestor, javaProject, pattern);
}
}
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 219754b..23ce92c 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
//Synthetic comment -- @@ -48,7 +50,6 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
//Synthetic comment -- @@ -1165,10 +1166,19 @@
if (xmlChild.getNodeType() == Node.ELEMENT_NODE) {
String elementName = xmlChild.getNodeName();
UiElementNode uiNode = null;
if (mUiChildren.size() <= uiIndex) {
// A new node is being added at the end of the list
ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
if (desc == null) {
// Unknown node. Create a temporary descriptor for it.
// We'll add unknown attributes to it later.
//Synthetic comment -- @@ -1227,11 +1237,12 @@
// Inserting new node
ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
                        if (desc == null && elementName.indexOf('.') != -1) {
                            IProject project = getEditor().getProject();
                            if (project != null) {
                                desc = CustomViewDescriptorService.getInstance().getDescriptor(
                                        project, elementName);
}
}
if (desc == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 177662d..923a6ed 100644

//Synthetic comment -- @@ -114,4 +114,13 @@
assertSame("Foo", AdtUtils.capitalize("Foo"));
assertNull(null, AdtUtils.capitalize(null));
}
}







