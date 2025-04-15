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
//Synthetic comment -- index 3129f4d..768f93f 100644

//Synthetic comment -- @@ -172,6 +172,7 @@
* The top level android package as a prefix, "android.".
*/
public static final String ANDROID_PKG_PREFIX = ANDROID_PKG + '.';
    public static final String ANDROID_SUPPORT_PKG_PREFIX = ANDROID_PKG_PREFIX + "support."; //$NON-NLS-1$

/** The android.view. package prefix */
public static final String ANDROID_VIEW_PKG = ANDROID_PKG_PREFIX + "view."; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index ec3d8a4..7b3e8a3 100755

//Synthetic comment -- @@ -75,6 +75,15 @@
mDeprecatedDoc = info.mDeprecatedDoc;
}

    /**
     * Sets the XML Name of the attribute
     *
     * @param name the new name to assign
     */
    public void setName(String name) {
        mName = name;
    }

/** Returns the XML Name of the attribute */
@Override
public String getName() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 52b0e15..a0a5ad8 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.log.ILogger;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -197,7 +198,7 @@
String xmlName = String.format("%1$s_%2$s", //$NON-NLS-1$
viewLayoutClass.getShortClassName(),
info.getShortClassName());
            xmlName = AdtUtils.stripSuffix(xmlName, "Params"); //$NON-NLS-1$

DeclareStyleableInfo style = mStyleMap.get(xmlName);
if (style != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 7e2a44d..50ea943 100644

//Synthetic comment -- @@ -219,6 +219,22 @@
}

/**
     * Strips the given suffix from the given string, provided that the string ends with
     * the suffix.
     *
     * @param string the full string to strip from
     * @param suffix the suffix to strip out
     * @return the string without the suffix at the end
     */
    public static String stripSuffix(@NonNull String string, @NonNull String suffix) {
        if (string.endsWith(suffix)) {
            return string.substring(0, string.length() - suffix.length());
        }

        return string;
    }

    /**
* Capitalizes the string, i.e. transforms the initial [a-z] into [A-Z].
* Returns the string unmodified if the first character is not [a-z].
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index 8d38c7c..7da3cc0 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
//Synthetic comment -- @@ -35,7 +36,9 @@
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -245,12 +248,16 @@
final IProject newProject;
try {
IProgressMonitor monitor = new NullProgressMonitor();
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();

String name = AdtUtils.getUniqueProjectName(
"gridlayout_v7", "_"); //$NON-NLS-1$ //$NON-NLS-2$
newProject = root.getProject(name);
            IProjectDescription description = workspace.newProjectDescription(name);
            String[] natures = new String[] { AdtConstants.NATURE_DEFAULT, JavaCore.NATURE_ID };
            description.setNatureIds(natures);
            newProject.create(description, monitor);

// Copy in the files recursively
IFileSystem fileSystem = EFS.getLocalFileSystem();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index b7c95b5..7a2658b 100644

//Synthetic comment -- @@ -305,7 +305,7 @@
Node attr = attrs.item(n);
if (XmlnsAttributeDescriptor.XMLNS.equals(attr.getPrefix())) {
String uri = attr.getNodeValue();
                    if (nsUri.equals(uri)) {
return attr.getLocalName();
}
visited.add(uri);
//Synthetic comment -- @@ -316,7 +316,7 @@
// Use a sensible default prefix if we can't find one.
// We need to make sure the prefix is not one that was declared in the scope
// visited above.
        prefix = SdkConstants.NS_RESOURCES.equals(nsUri) ? "android" : "app"; //$NON-NLS-1$ //$NON-NLS-2$
String base = prefix;
for (int i = 1; visited.contains(prefix); i++) {
prefix = base + Integer.toString(i);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index b00656e..19ff37a 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
//Synthetic comment -- @@ -100,7 +101,7 @@
*        entries in the form "elem-name/attr-name". Elem-name can be "*".
* @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
    public static void appendAttributes(List<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri, AttributeInfo[] infos,
Set<String> requiredAttributes,
//Synthetic comment -- @@ -131,7 +132,7 @@
*        a "*" to its UI name as a hint for the user.)
* @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
    public static void appendAttribute(List<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri,
AttributeInfo info, boolean required,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 9e821ad..325e284 100644

//Synthetic comment -- @@ -750,7 +750,10 @@

// Check if we can find a custom view specific to this project.
// This only works if there's an actual matching custom class in the project.
            if (xmlLocalName.indexOf('.') != -1) {
                desc = CustomViewDescriptorService.getInstance().getDescriptor(project,
                        xmlLocalName);
            }

if (desc == null) {
// If we didn't find a custom view, create a synthetic one using the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 5abbb0c..23aa2a0 100644

//Synthetic comment -- @@ -16,19 +16,43 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.sdklib.SdkConstants.CLASS_VIEWGROUP;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.common.resources.platform.AttrsXmlParser;
import com.android.ide.common.resources.platform.ViewClassInfo;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.google.common.collect.ObjectArrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
//Synthetic comment -- @@ -36,8 +60,13 @@
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Service responsible for creating/managing {@link ViewElementDescriptor} objects for custom
//Synthetic comment -- @@ -162,11 +191,24 @@

if (parentDescriptor != null) {
// we have a valid parent, lets create a new ViewElementDescriptor.
                        List<AttributeDescriptor> attrList = new ArrayList<AttributeDescriptor>();
                        List<AttributeDescriptor> paramList = new ArrayList<AttributeDescriptor>();
                        findCustomDescriptors(project, type, attrList, paramList);

                        AttributeDescriptor[] attributes =
                                getAttributeDescriptor(type, parentDescriptor);
                        if (!attrList.isEmpty()) {
                            attributes = join(attrList, attributes);
                        }
                        AttributeDescriptor[] layoutAttributes =
                                getLayoutAttributeDescriptors(type, parentDescriptor);
                        if (!paramList.isEmpty()) {
                            layoutAttributes = join(paramList, layoutAttributes);
                        }
String name = DescriptorsUtils.getBasename(fqcn);
ViewElementDescriptor descriptor = new CustomViewDescriptor(name, fqcn,
                                attributes,
                                layoutAttributes,
parentDescriptor.getChildren());
descriptor.setSuperClass(parentDescriptor);

//Synthetic comment -- @@ -193,6 +235,168 @@
return null;
}

    private static AttributeDescriptor[] join(
            @NonNull List<AttributeDescriptor> attributeList,
            @NonNull AttributeDescriptor[] attributes) {
        if (!attributeList.isEmpty()) {
            return ObjectArrays.concat(
                    attributeList.toArray(new AttributeDescriptor[attributeList.size()]),
                    attributes,
                    AttributeDescriptor.class);
        } else {
            return attributes;
        }

    }

    /** Cache used by {@link #getParser(ResourceFile)} */
    private Map<ResourceFile, AttrsXmlParser> mParserCache;

    private AttrsXmlParser getParser(ResourceFile file) {
        if (mParserCache == null) {
            mParserCache = new HashMap<ResourceFile, AttrsXmlParser>();
        }

        AttrsXmlParser parser = mParserCache.get(file);
        if (parser == null) {
            parser = new AttrsXmlParser(
                    file.getFile().getOsLocation(),
                    AdtPlugin.getDefault());
            parser.preload();
            mParserCache.put(file, parser);
        }

        return parser;
    }

    /** Compute/find the styleable resources for the given type, if possible */
    private void findCustomDescriptors(
            IProject project,
            IType type,
            List<AttributeDescriptor> customAttributes,
            List<AttributeDescriptor> customLayoutAttributes) {
        // Look up the project where the type is declared (could be a library project;
        // we cannot use type.getJavaProject().getProject())
        IProject library = getProjectDeclaringType(type);
        if (library == null) {
            library = project;
        }

        String className = type.getElementName();
        Set<ResourceFile> resourceFiles = findAttrsFiles(library, className);
        if (resourceFiles != null && resourceFiles.size() > 0) {
            String appUri = getAppResUri(project);
            for (ResourceFile file : resourceFiles) {
                AttrsXmlParser attrsXmlParser = getParser(file);
                String fqcn = type.getFullyQualifiedName();

                // Attributes
                ViewClassInfo classInfo = new ViewClassInfo(true, fqcn, className);
                attrsXmlParser.loadViewAttributes(classInfo);
                appendAttributes(customAttributes, classInfo.getAttributes(), appUri);

                // Layout params
                LayoutParamsInfo layoutInfo = new ViewClassInfo.LayoutParamsInfo(
                        classInfo, "Layout", null /*superClassInfo*/); //$NON-NLS-1$
                attrsXmlParser.loadLayoutParamsAttributes(layoutInfo);
                appendAttributes(customLayoutAttributes, layoutInfo.getAttributes(), appUri);
            }
        }
    }

    /**
     * Finds the set of XML files (if any) in the given library declaring
     * attributes for the given class name
     */
    @Nullable
    private Set<ResourceFile> findAttrsFiles(IProject library, String className) {
        Set<ResourceFile> resourceFiles = null;
        ResourceManager manager = ResourceManager.getInstance();
        ProjectResources resources = manager.getProjectResources(library);
        if (resources != null) {
            Collection<ResourceItem> items =
                resources.getResourceItemsOfType(ResourceType.DECLARE_STYLEABLE);
            for (ResourceItem item : items) {
                String viewName = item.getName();
                if (viewName.equals(className)
                        || (viewName.startsWith(className)
                            && viewName.equals(className + "_Layout"))) { //$NON-NLS-1$
                    if (resourceFiles == null) {
                        resourceFiles = new HashSet<ResourceFile>();
                    }
                    resourceFiles.addAll(item.getSourceFileList());
                }
            }
        }
        return resourceFiles;
    }

    /**
     * Find the project containing this type declaration. We cannot use
     * {@link IType#getJavaProject()} since that will return the including
     * project and we're after the library project such that we can find the
     * attrs.xml file in the same project.
     */
    @Nullable
    private IProject getProjectDeclaringType(IType type) {
        IClassFile classFile = type.getClassFile();
        if (classFile != null) {
            IPath path = classFile.getPath();
            IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
            IResource resource;
            if (path.isAbsolute()) {
                resource = AdtUtils.fileToResource(path.toFile());
            } else {
                resource = workspace.findMember(path);
            }
            if (resource != null && resource.getProject() != null) {
                return resource.getProject();
            }
        }

        return null;
    }

    /** Returns the name space to use for application attributes */
    private String getAppResUri(IProject project) {
        String appResource;
        ProjectState projectState = Sdk.getProjectState(project);
        if (projectState != null && projectState.isLibrary()) {
            appResource = AUTO_URI;
        } else {
            ManifestInfo manifestInfo = ManifestInfo.get(project);
            appResource = URI_PREFIX + manifestInfo.getPackage();
        }
        return appResource;
    }


    /** Append the {@link AttributeInfo} objects converted {@link AttributeDescriptor}
     * objects into the given attribute list.
     * <p>
     * This is nearly identical to
     *  {@link DescriptorsUtils#appendAttribute(List, String, String, AttributeInfo, boolean, Map)}
     * but it handles namespace declarations in the attrs.xml file where the android:
     * namespace is included in the names.
     */
    private static void appendAttributes(List<AttributeDescriptor> attributes,
            AttributeInfo[] attributeInfos, String appResource) {
        // Custom attributes
        for (AttributeInfo info : attributeInfos) {
            String nsUri;
            if (info.getName().startsWith(ANDROID_NS_NAME_PREFIX)) {
                info.setName(info.getName().substring(ANDROID_NS_NAME_PREFIX.length()));
                nsUri = ANDROID_URI;
            } else {
                nsUri = appResource;
            }

            DescriptorsUtils.appendAttribute(attributes,
                    null /*elementXmlName*/, nsUri, info, false /*required*/,
                    null /*overrides*/);
        }
    }

/**
* Computes (if needed) and returns the {@link ViewElementDescriptor} for the specified type.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index 5bd35a1..435529a 100644

//Synthetic comment -- @@ -60,6 +60,7 @@
AttributeDescriptor[] direct_attrs = super.getAttributeDescriptors();
mCachedAttributeDescriptors = direct_attrs;

        // Compute layout attributes: These depend on the *parent* this widget is within
AttributeDescriptor[] layout_attrs = null;
boolean need_xmlns = false;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index f749e2b..a49a0aa 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
//Synthetic comment -- @@ -441,7 +442,11 @@
IField field = type.getField(layoutName);
if (field.exists()) {
SearchPattern pattern = SearchPattern.createPattern(field, REFERENCES);
                    try {
                        search(requestor, javaProject, pattern);
                    } catch (OperationCanceledException canceled) {
                        // pass
                    }
}
}
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 219754b..23ce92c 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_SUPPORT_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
//Synthetic comment -- @@ -48,7 +50,6 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
//Synthetic comment -- @@ -1165,10 +1166,19 @@
if (xmlChild.getNodeType() == Node.ELEMENT_NODE) {
String elementName = xmlChild.getNodeName();
UiElementNode uiNode = null;
                CustomViewDescriptorService service = CustomViewDescriptorService.getInstance();
if (mUiChildren.size() <= uiIndex) {
// A new node is being added at the end of the list
ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
                    if (desc == null && elementName.indexOf('.') != -1 &&
                            (!elementName.startsWith(ANDROID_PKG_PREFIX)
                                    || elementName.startsWith(ANDROID_SUPPORT_PKG_PREFIX))) {
                        AndroidXmlEditor editor = getEditor();
                        if (editor != null && editor.getProject() != null) {
                            desc = service.getDescriptor(editor.getProject(), elementName);
                        }
                    }
if (desc == null) {
// Unknown node. Create a temporary descriptor for it.
// We'll add unknown attributes to it later.
//Synthetic comment -- @@ -1227,11 +1237,12 @@
// Inserting new node
ElementDescriptor desc = mDescriptor.findChildrenDescriptor(elementName,
false /* recursive */);
                        if (desc == null && elementName.indexOf('.') != -1 &&
                                (!elementName.startsWith(ANDROID_PKG_PREFIX)
                                        || elementName.startsWith(ANDROID_SUPPORT_PKG_PREFIX))) {
                            AndroidXmlEditor editor = getEditor();
                            if (editor != null && editor.getProject() != null) {
                                desc = service.getDescriptor(editor.getProject(), elementName);
}
}
if (desc == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index 177662d..9eeb2ea 100644

//Synthetic comment -- @@ -114,4 +114,14 @@
assertSame("Foo", AdtUtils.capitalize("Foo"));
assertNull(null, AdtUtils.capitalize(null));
}

    public void testStripSuffix() {
        assertEquals("Foo", AdtUtils.stripSuffix("Foo", ""));
        assertEquals("Fo", AdtUtils.stripSuffix("Foo", "o"));
        assertEquals("F", AdtUtils.stripSuffix("Fo", "o"));
        assertEquals("", AdtUtils.stripSuffix("Foo", "Foo"));
        assertEquals("LinearLayout_Layout",
                AdtUtils.stripSuffix("LinearLayout_LayoutParams", "Params"));
        assertEquals("Foo", AdtUtils.stripSuffix("Foo", "Bar"));
    }
}







