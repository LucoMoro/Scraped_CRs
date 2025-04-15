/*Fix GestureOverlayView handling

This CL fixes a number of problems related to the GestureOverlayView.

The first big problem was that GestureOverlayView lives in the
android.gesture package, which is not one of the special builtin
packages you can omit when specifying the element in layout XML
files. This CL both stores a full path in the descriptor XML name,
which fixes tag-to-descriptor lookup (without this we were showing the
wrong icon and missing vital attributes in completion), and handles
scenarios where the full path has to be used.

The second problem was that the descriptor metadata listed
GestureOverlayView as a "view" rather than a "layout", which meant
that it was missing information about children (which meant you
couldn't drop into it), and it was missing layout params (which meant
children couldn't be assigned attributes like layout_width and
layout_height).

Also tweak selection handling for gesture views.

There are a number of places where we need to go from a class name to
a view descriptor; these are now centralized in a utility method and
sped up with a map lookup.

(Issues fixed: #17541, #17543)

Change-Id:I67c450f28eab07b3575e510420b7faf4410085e3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 3f48847..9061702 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
public static final String ATTR_TAG = "tag";                        //$NON-NLS-1$
public static final String ATTR_NUM_COLUMNS = "numColumns";         //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -133,16 +134,19 @@
public static final String ANDROID_URI = SdkConstants.NS_RESOURCES;

/**
     * The package name where the widgets live (the ones that require no prefix in layout
     * files)
     */
    public static final String ANDROID_WIDGET_PREFIX = "android.widget."; //$NON-NLS-1$

    /**
* The top level android package as a prefix, "android.".
*/
public static final String ANDROID_PKG_PREFIX = ANDROID_PKG + '.';

/** The fully qualified class name of an EditText view */
public static final String FQCN_EDIT_TEXT = "android.widget.EditText"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 7da9587..07593cf 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
//Synthetic comment -- @@ -58,7 +59,6 @@
* This pull parser generates {@link ViewInfo}s which key is a {@link UiElementNode}.
*/
public class UiElementPullParser extends BasePullParser {
    private final static String ATTR_PADDING = "padding"; //$NON-NLS-1$
private final static Pattern FLOAT_PATTERN = Pattern.compile("(-?[0-9]+(?:\\.[0-9]+)?)(.*)"); //$NON-NLS-1$

private final int[] sIntOut = new int[1];
//Synthetic comment -- @@ -68,7 +68,7 @@
private final boolean mExplodedRendering;
private boolean mZeroAttributeIsPadding = false;
private boolean mIncreaseExistingPadding = false;
    private List<ViewElementDescriptor> mLayoutDescriptors;
private final Density mDensity;
private final float mXdpi;

//Synthetic comment -- @@ -121,8 +121,7 @@
// get the layout descriptor
IAndroidTarget target = Sdk.getCurrent().getTarget(project);
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
            LayoutDescriptors descriptors = data.getLayoutDescriptors();
            mLayoutDescriptors = descriptors.getLayoutDescriptors();
}
push(mRoot);
}
//Synthetic comment -- @@ -162,18 +161,15 @@
if (mExplodedRendering) {
// first get the node name
String xml = node.getDescriptor().getXmlLocalName();
            for (ViewElementDescriptor descriptor : mLayoutDescriptors) {
                if (xml.equals(descriptor.getXmlLocalName())) {
                    NamedNodeMap attributes = node.getXmlNode().getAttributes();
                    Node padding = attributes.getNamedItemNS(SdkConstants.NS_RESOURCES, "padding");
                    if (padding == null) {
                        // we'll return an extra padding
                        mZeroAttributeIsPadding = true;
                    } else {
                        mIncreaseExistingPadding = true;
                    }

                    break;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 75fdf5b..3accaf8 100644

//Synthetic comment -- @@ -205,17 +205,6 @@
// check if the type is a built-in View class.
List<ViewElementDescriptor> builtInList = null;

        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk != null) {
            IAndroidTarget target = currentSdk.getTarget(project);
            if (target != null) {
                AndroidTargetData data = currentSdk.getTargetData(target);
                if (data != null) {
                    builtInList = data.getLayoutDescriptors().getViewDescriptors();
                }
            }
        }

// give up if there's no type
if (type == null) {
return null;
//Synthetic comment -- @@ -223,10 +212,18 @@

String fqcn = type.getFullyQualifiedName();

        if (builtInList != null) {
            for (ViewElementDescriptor viewDescriptor : builtInList) {
                if (fqcn.equals(viewDescriptor.getFullClassName())) {
                    return viewDescriptor;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 89cfe92..3bfcb5c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TAG;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.sdklib.SdkConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -116,6 +118,11 @@
/** The descriptor matching android.view.View. */
private ViewElementDescriptor mBaseViewDescriptor;

/** Returns the document descriptor. Contains all layouts and views linked together. */
public DocumentDescriptor getDescriptor() {
return mRootDescriptor;
//Synthetic comment -- @@ -141,16 +148,7 @@
*/
public ViewElementDescriptor getBaseViewDescriptor() {
if (mBaseViewDescriptor == null) {
            for (ElementDescriptor desc : mViewDescriptors) {
                if (desc instanceof ViewElementDescriptor) {
                    ViewElementDescriptor viewDesc = (ViewElementDescriptor) desc;
                    if (SdkConstants.CLASS_VIEW.equals(viewDesc.getFullClassName())) {
                        mBaseViewDescriptor = viewDesc;
                        break;
                    }
                }

            }
}
return mBaseViewDescriptor;
}
//Synthetic comment -- @@ -182,6 +180,7 @@
for (ViewClassInfo info : views) {
ViewElementDescriptor desc = convertView(info, infoDescMap);
newViews.add(desc);
}
}

//Synthetic comment -- @@ -194,15 +193,16 @@
for (ViewClassInfo info : layouts) {
ViewElementDescriptor desc = convertView(info, infoDescMap);
newLayouts.add(desc);
}
}

// Find View and inherit all its layout attributes
        AttributeDescriptor[] viewLayoutAttribs = findViewLayoutAttributes(
                SdkConstants.CLASS_FRAMELAYOUT, newLayouts);

if (target.getVersion().getApiLevel() >= 4) {
            ViewElementDescriptor fragmentTag = createFragment(viewLayoutAttribs, styleMap);
newViews.add(fragmentTag);
}

//Synthetic comment -- @@ -215,6 +215,15 @@
layoutDesc.setChildren(newDescriptors);
}

fixSuperClasses(infoDescMap);

ViewElementDescriptor requestFocus = createRequestFocus();
//Synthetic comment -- @@ -223,7 +232,7 @@

// The <merge> tag can only be a root tag, so it is added at the end.
// It gets everything else as children but it is not made a child itself.
        ViewElementDescriptor mergeTag = createMerge(viewLayoutAttribs);
mergeTag.setChildren(newDescriptors);  // mergeTag makes a copy of the list
newDescriptors.add(mergeTag);
newLayouts.add(mergeTag);
//Synthetic comment -- @@ -251,7 +260,12 @@
private ViewElementDescriptor convertView(
ViewClassInfo info,
HashMap<ViewClassInfo, ViewElementDescriptor> infoDescMap) {
        String xml_name = info.getShortClassName();
String tooltip = info.getJavaDoc();

ArrayList<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
//Synthetic comment -- @@ -297,13 +311,13 @@
LayoutParamsInfo layoutParams = info.getLayoutData();

for(; layoutParams != null; layoutParams = layoutParams.getSuperClass()) {
            boolean need_separator = true;
            for (AttributeInfo attr_info : layoutParams.getAttributes()) {
if (DescriptorsUtils.containsAttribute(layoutAttributes,
                        SdkConstants.NS_RESOURCES, attr_info)) {
continue;
}
                if (need_separator) {
String title;
if (layoutParams.getShortClassName().equals(
SdkConstants.CLASS_NAME_LAYOUTPARAMS)) {
//Synthetic comment -- @@ -315,20 +329,21 @@
layoutParams.getShortClassName());
}
layoutAttributes.add(new SeparatorAttributeDescriptor(title));
                    need_separator = false;
}
DescriptorsUtils.appendAttribute(layoutAttributes,
null, // elementName
SdkConstants.NS_RESOURCES,
                        attr_info,
false, // required
null /* overrides */);
}
}

        ViewElementDescriptor desc = new ViewElementDescriptor(xml_name,
                xml_name, // ui_name
                info.getFullClassName(),
tooltip,
null, // sdk_url
attributes.toArray(new AttributeDescriptor[attributes.size()]),
//Synthetic comment -- @@ -346,7 +361,7 @@
*   View descriptor and extract its layout attributes.
*/
private void insertInclude(List<ViewElementDescriptor> knownViews) {
        String xml_name = VIEW_INCLUDE;

// Create the include custom attributes
ArrayList<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
//Synthetic comment -- @@ -372,11 +387,11 @@

// Find View and inherit all its layout attributes
AttributeDescriptor[] viewLayoutAttribs = findViewLayoutAttributes(
                SdkConstants.CLASS_VIEW, knownViews);

// Create the include descriptor
        ViewElementDescriptor desc = new ViewElementDescriptor(xml_name,  // xml_name
                xml_name, // ui_name
VIEW_INCLUDE, // "class name"; the GLE only treats this as an element tag
"Lets you statically include XML layouts inside other XML layouts.",  // tooltip
null, // sdk_url
//Synthetic comment -- @@ -393,11 +408,11 @@
* @param viewLayoutAttribs The layout attributes to use for the new descriptor
*/
private ViewElementDescriptor createMerge(AttributeDescriptor[] viewLayoutAttribs) {
        String xml_name = VIEW_MERGE;

// Create the include descriptor
        ViewElementDescriptor desc = new ViewElementDescriptor(xml_name,  // xml_name
                xml_name, // ui_name
VIEW_MERGE, // "class name"; the GLE only treats this as an element tag
"A root tag useful for XML layouts inflated using a ViewStub.",  // tooltip
null,  // sdk_url
//Synthetic comment -- @@ -416,7 +431,7 @@
*/
private ViewElementDescriptor createFragment(AttributeDescriptor[] viewLayoutAttribs,
Map<String, DeclareStyleableInfo> styleMap) {
        String xml_name = VIEW_FRAGMENT;
final ViewElementDescriptor descriptor;

// First try to create the descriptor from metadata in attrs.xml:
//Synthetic comment -- @@ -457,9 +472,9 @@
// The above will only work on API 11 and up. However, fragments are *also* available
// on older platforms, via the fragment support library, so add in a manual
// entry if necessary.
            descriptor = new ViewElementDescriptor(xml_name,  // xml_name
                xml_name, // ui_name
                xml_name, // "class name"; the GLE only treats this as an element tag
fragmentTooltip,
sdkUrl,
new AttributeDescriptor[] {
//Synthetic comment -- @@ -489,13 +504,13 @@
* Creates and returns a new {@code <requestFocus>} descriptor.
*/
private ViewElementDescriptor createRequestFocus() {
        String xml_name = REQUEST_FOCUS;

// Create the include descriptor
return new ViewElementDescriptor(
                xml_name,  // xml_name
                xml_name, // ui_name
                xml_name, // "class name"; the GLE only treats this as an element tag
"Requests focus for the parent element or one of its descendants", // tooltip
null,  // sdk_url
null,  // attributes
//Synthetic comment -- @@ -508,13 +523,10 @@
* Finds the descriptor and retrieves all its layout attributes.
*/
private AttributeDescriptor[] findViewLayoutAttributes(
            String viewFqcn,
            List<ViewElementDescriptor> knownViews) {

        for (ViewElementDescriptor viewDesc : knownViews) {
            if (viewFqcn.equals(viewDesc.getFullClassName())) {
                return viewDesc.getLayoutAttributes();
            }
}

return null;
//Synthetic comment -- @@ -549,4 +561,45 @@
}
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java
//Synthetic comment -- index b45a197..a18b821 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -53,7 +57,7 @@
private final String mFullClassName;

/** The list of layout attributes. Can be empty but not null. */
    private final AttributeDescriptor[] mLayoutAttributes;

/** The super-class descriptor. Can be null. */
private ViewElementDescriptor mSuperClassDesc;
//Synthetic comment -- @@ -117,6 +121,16 @@
}

/**
* Returns a new {@link UiViewElementNode} linked to this descriptor.
*/
@Override
//Synthetic comment -- @@ -168,4 +182,17 @@
return icon;
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 6bd03b2..7471e54 100755

//Synthetic comment -- @@ -77,7 +77,7 @@
private final UiViewElementNode mUiViewNode;
private CanvasViewInfo mParent;
private ViewInfo mViewInfo;
    private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

/**
* Is this view info an individually exploded view? This is the case for views
//Synthetic comment -- @@ -390,7 +390,8 @@
// etc)
if (mParent != null
&& mParent.mName.endsWith(GESTURE_OVERLAY_VIEW)
                && mParent.isRoot()) {
return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 176b7bd..4923107 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.sdklib.SdkConstants.FD_GEN_SOURCES;

import com.android.ide.common.rendering.LayoutLibrary;
//Synthetic comment -- @@ -43,14 +44,13 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
//Synthetic comment -- @@ -1661,7 +1661,7 @@
// is the same
labelClass),
actual,
                                    suggested.startsWith(ANDROID_PKG) ? suggestedBase : suggested
);
addText(mErrorLabel, ", ");
}
//Synthetic comment -- @@ -1681,23 +1681,15 @@
}

private static Collection<String> getAndroidViewClassNames(IProject project) {
        List<String> classNames = new ArrayList<String>(100);

Sdk currentSdk = Sdk.getCurrent();
IAndroidTarget target = currentSdk.getTarget(project);
if (target != null) {
AndroidTargetData targetData = currentSdk.getTargetData(target);
LayoutDescriptors layoutDescriptors = targetData.getLayoutDescriptors();

            for (ViewElementDescriptor d : layoutDescriptors.getViewDescriptors()) {
                classNames.add(d.getFullClassName());
            }
            for (ViewElementDescriptor d : layoutDescriptors.getLayoutDescriptors()) {
                classNames.add(d.getFullClassName());
            }
}

        return classNames;
}

/** Add a normal line of text to the styled text widget. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java
//Synthetic comment -- index 87fdc14..4a92108 100644

//Synthetic comment -- @@ -1230,20 +1230,7 @@
protected ViewElementDescriptor getElementDescriptor(String fqcn) {
AndroidTargetData data = mEditor.getTargetData();
if (data != null) {
            List<ViewElementDescriptor> views =
                data.getLayoutDescriptors().getViewDescriptors();
            for (ViewElementDescriptor descriptor : views) {
                if (fqcn.equals(descriptor.getFullClassName())) {
                    return descriptor;
                }
            }
            List<ViewElementDescriptor> layouts =
                data.getLayoutDescriptors().getLayoutDescriptors();
            for (ViewElementDescriptor descriptor : layouts) {
                if (fqcn.equals(descriptor.getFullClassName())) {
                    return descriptor;
                }
            }
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index 60b5662..5bd35a1 100644

//Synthetic comment -- @@ -16,9 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.uimodel;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -29,8 +32,6 @@

import org.eclipse.core.resources.IProject;

import java.util.List;

/**
* Specialized version of {@link UiElementNode} for the {@link ViewElementDescriptor}s.
*/
//Synthetic comment -- @@ -67,7 +68,6 @@
// owned by a FrameLayout.
// TODO replace by something user-configurable.

            List<ViewElementDescriptor> layoutDescriptors = null;
IProject project = getEditor().getProject();
if (project != null) {
Sdk currentSdk = Sdk.getCurrent();
//Synthetic comment -- @@ -76,21 +76,17 @@
if (target != null) {
AndroidTargetData data = currentSdk.getTargetData(target);
if (data != null) {
                            layoutDescriptors = data.getLayoutDescriptors().getLayoutDescriptors();
}
}
}
}

            if (layoutDescriptors != null) {
                for (ViewElementDescriptor desc : layoutDescriptors) {
                    if (desc.getXmlName().equals(SdkConstants.CLASS_NAME_FRAMELAYOUT)) {
                        layout_attrs = desc.getLayoutAttributes();
                        need_xmlns = true;
                        break;
                    }
                }
            }
} else if (ui_parent instanceof UiViewElementNode) {
layout_attrs =
((ViewElementDescriptor) ui_parent.getDescriptor()).getLayoutAttributes();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 817fcbe..a9cf902 100644

//Synthetic comment -- @@ -1692,6 +1692,9 @@
if (region != null
&& DOMRegionContext.XML_TAG_NAME.equals(region.getType())) {
ITextRegion subRegion = region.getRegionAtCharacterOffset(offset);
int regionStart = region.getStartOffset();
int subregionStart = subRegion.getStart();
int relativeOffset = offset - (regionStart + subregionStart);







