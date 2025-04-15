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
    public static final String ATTR_PADDING = "padding";                //$NON-NLS-1$

// Some common layout element names
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -133,16 +134,19 @@
public static final String ANDROID_URI = SdkConstants.NS_RESOURCES;

/**
* The top level android package as a prefix, "android.".
*/
public static final String ANDROID_PKG_PREFIX = ANDROID_PKG + '.';

    /** The android.view. package prefix */
    public static final String ANDROID_VIEW_PKG = ANDROID_PKG_PREFIX + "view."; //$NON-NLS-1$

    /** The android.widget. package prefix */
    public static final String ANDROID_WIDGET_PREFIX = ANDROID_PKG_PREFIX + "widget."; //$NON-NLS-1$

    /** The android.webkit. package prefix */
    public static final String ANDROID_WEBKIT_PKG = ANDROID_PKG_PREFIX + "webkit."; //$NON-NLS-1$

/** The fully qualified class name of an EditText view */
public static final String FQCN_EDIT_TEXT = "android.widget.EditText"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 7da9587..07593cf 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_PADDING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
//Synthetic comment -- @@ -58,7 +59,6 @@
* This pull parser generates {@link ViewInfo}s which key is a {@link UiElementNode}.
*/
public class UiElementPullParser extends BasePullParser {
private final static Pattern FLOAT_PATTERN = Pattern.compile("(-?[0-9]+(?:\\.[0-9]+)?)(.*)"); //$NON-NLS-1$

private final int[] sIntOut = new int[1];
//Synthetic comment -- @@ -68,7 +68,7 @@
private final boolean mExplodedRendering;
private boolean mZeroAttributeIsPadding = false;
private boolean mIncreaseExistingPadding = false;
    private LayoutDescriptors mDescriptors;
private final Density mDensity;
private final float mXdpi;

//Synthetic comment -- @@ -121,8 +121,7 @@
// get the layout descriptor
IAndroidTarget target = Sdk.getCurrent().getTarget(project);
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
            mDescriptors = data.getLayoutDescriptors();
}
push(mRoot);
}
//Synthetic comment -- @@ -162,18 +161,15 @@
if (mExplodedRendering) {
// first get the node name
String xml = node.getDescriptor().getXmlLocalName();
            ViewElementDescriptor descriptor = mDescriptors.findDescriptorByTag(xml);
            if (descriptor != null) {
                NamedNodeMap attributes = node.getXmlNode().getAttributes();
                Node padding = attributes.getNamedItemNS(SdkConstants.NS_RESOURCES, ATTR_PADDING);
                if (padding == null) {
                    // we'll return an extra padding
                    mZeroAttributeIsPadding = true;
                } else {
                    mIncreaseExistingPadding = true;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 75fdf5b..3accaf8 100644

//Synthetic comment -- @@ -205,17 +205,6 @@
// check if the type is a built-in View class.
List<ViewElementDescriptor> builtInList = null;

// give up if there's no type
if (type == null) {
return null;
//Synthetic comment -- @@ -223,10 +212,18 @@

String fqcn = type.getFullyQualifiedName();

        Sdk currentSdk = Sdk.getCurrent();
        if (currentSdk != null) {
            IAndroidTarget target = currentSdk.getTarget(project);
            if (target != null) {
                AndroidTargetData data = currentSdk.getTargetData(target);
                if (data != null) {
                    LayoutDescriptors descriptors = data.getLayoutDescriptors();
                    ViewElementDescriptor d = descriptors.findDescriptorByClass(fqcn);
                    if (d != null) {
                        return d;
                    }
                    builtInList = descriptors.getViewDescriptors();
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 89cfe92..3bfcb5c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TAG;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.sdklib.SdkConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -116,6 +118,11 @@
/** The descriptor matching android.view.View. */
private ViewElementDescriptor mBaseViewDescriptor;

    /** Map from view full class name to view descriptor */
    private Map<String, ViewElementDescriptor> mFqcnToDescriptor =
        // As of 3.1 there are 58 items in this map
        new HashMap<String, ViewElementDescriptor>(80);

/** Returns the document descriptor. Contains all layouts and views linked together. */
public DocumentDescriptor getDescriptor() {
return mRootDescriptor;
//Synthetic comment -- @@ -141,16 +148,7 @@
*/
public ViewElementDescriptor getBaseViewDescriptor() {
if (mBaseViewDescriptor == null) {
            mBaseViewDescriptor = findDescriptorByClass(SdkConstants.CLASS_VIEW);
}
return mBaseViewDescriptor;
}
//Synthetic comment -- @@ -182,6 +180,7 @@
for (ViewClassInfo info : views) {
ViewElementDescriptor desc = convertView(info, infoDescMap);
newViews.add(desc);
                mFqcnToDescriptor.put(desc.getFullClassName(), desc);
}
}

//Synthetic comment -- @@ -194,15 +193,16 @@
for (ViewClassInfo info : layouts) {
ViewElementDescriptor desc = convertView(info, infoDescMap);
newLayouts.add(desc);
                mFqcnToDescriptor.put(desc.getFullClassName(), desc);
}
}

// Find View and inherit all its layout attributes
        AttributeDescriptor[] frameLayoutAttrs = findViewLayoutAttributes(
                SdkConstants.CLASS_FRAMELAYOUT);

if (target.getVersion().getApiLevel() >= 4) {
            ViewElementDescriptor fragmentTag = createFragment(frameLayoutAttrs, styleMap);
newViews.add(fragmentTag);
}

//Synthetic comment -- @@ -215,6 +215,15 @@
layoutDesc.setChildren(newDescriptors);
}

        // The gesture overlay descriptor is really a layout but not included in the layouts list
        // so handle it specially
        ViewElementDescriptor gestureView = findDescriptorByClass(FQCN_GESTURE_OVERLAY_VIEW);
        if (gestureView != null) {
            gestureView.setChildren(newDescriptors);
            // Inherit layout attributes from FrameLayout
            gestureView.setLayoutAttributes(frameLayoutAttrs);
        }

fixSuperClasses(infoDescMap);

ViewElementDescriptor requestFocus = createRequestFocus();
//Synthetic comment -- @@ -223,7 +232,7 @@

// The <merge> tag can only be a root tag, so it is added at the end.
// It gets everything else as children but it is not made a child itself.
        ViewElementDescriptor mergeTag = createMerge(frameLayoutAttrs);
mergeTag.setChildren(newDescriptors);  // mergeTag makes a copy of the list
newDescriptors.add(mergeTag);
newLayouts.add(mergeTag);
//Synthetic comment -- @@ -251,7 +260,12 @@
private ViewElementDescriptor convertView(
ViewClassInfo info,
HashMap<ViewClassInfo, ViewElementDescriptor> infoDescMap) {
        String xmlName = info.getShortClassName();
        String uiName = xmlName;
        String fqcn = info.getFullClassName();
        if (ViewElementDescriptor.viewNeedsPackage(fqcn)) {
            xmlName = fqcn;
        }
String tooltip = info.getJavaDoc();

ArrayList<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
//Synthetic comment -- @@ -297,13 +311,13 @@
LayoutParamsInfo layoutParams = info.getLayoutData();

for(; layoutParams != null; layoutParams = layoutParams.getSuperClass()) {
            boolean needSeparator = true;
            for (AttributeInfo attrInfo : layoutParams.getAttributes()) {
if (DescriptorsUtils.containsAttribute(layoutAttributes,
                        SdkConstants.NS_RESOURCES, attrInfo)) {
continue;
}
                if (needSeparator) {
String title;
if (layoutParams.getShortClassName().equals(
SdkConstants.CLASS_NAME_LAYOUTPARAMS)) {
//Synthetic comment -- @@ -315,20 +329,21 @@
layoutParams.getShortClassName());
}
layoutAttributes.add(new SeparatorAttributeDescriptor(title));
                    needSeparator = false;
}
DescriptorsUtils.appendAttribute(layoutAttributes,
null, // elementName
SdkConstants.NS_RESOURCES,
                        attrInfo,
false, // required
null /* overrides */);
}
}

        ViewElementDescriptor desc = new ViewElementDescriptor(
                xmlName,
                uiName,
                fqcn,
tooltip,
null, // sdk_url
attributes.toArray(new AttributeDescriptor[attributes.size()]),
//Synthetic comment -- @@ -346,7 +361,7 @@
*   View descriptor and extract its layout attributes.
*/
private void insertInclude(List<ViewElementDescriptor> knownViews) {
        String xmlName = VIEW_INCLUDE;

// Create the include custom attributes
ArrayList<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();
//Synthetic comment -- @@ -372,11 +387,11 @@

// Find View and inherit all its layout attributes
AttributeDescriptor[] viewLayoutAttribs = findViewLayoutAttributes(
                SdkConstants.CLASS_VIEW);

// Create the include descriptor
        ViewElementDescriptor desc = new ViewElementDescriptor(xmlName,
                xmlName, // ui_name
VIEW_INCLUDE, // "class name"; the GLE only treats this as an element tag
"Lets you statically include XML layouts inside other XML layouts.",  // tooltip
null, // sdk_url
//Synthetic comment -- @@ -393,11 +408,11 @@
* @param viewLayoutAttribs The layout attributes to use for the new descriptor
*/
private ViewElementDescriptor createMerge(AttributeDescriptor[] viewLayoutAttribs) {
        String xmlName = VIEW_MERGE;

// Create the include descriptor
        ViewElementDescriptor desc = new ViewElementDescriptor(xmlName,
                xmlName, // ui_name
VIEW_MERGE, // "class name"; the GLE only treats this as an element tag
"A root tag useful for XML layouts inflated using a ViewStub.",  // tooltip
null,  // sdk_url
//Synthetic comment -- @@ -416,7 +431,7 @@
*/
private ViewElementDescriptor createFragment(AttributeDescriptor[] viewLayoutAttribs,
Map<String, DeclareStyleableInfo> styleMap) {
        String xmlName = VIEW_FRAGMENT;
final ViewElementDescriptor descriptor;

// First try to create the descriptor from metadata in attrs.xml:
//Synthetic comment -- @@ -457,9 +472,9 @@
// The above will only work on API 11 and up. However, fragments are *also* available
// on older platforms, via the fragment support library, so add in a manual
// entry if necessary.
            descriptor = new ViewElementDescriptor(xmlName,
                xmlName, // ui_name
                xmlName, // "class name"; the GLE only treats this as an element tag
fragmentTooltip,
sdkUrl,
new AttributeDescriptor[] {
//Synthetic comment -- @@ -489,13 +504,13 @@
* Creates and returns a new {@code <requestFocus>} descriptor.
*/
private ViewElementDescriptor createRequestFocus() {
        String xmlName = REQUEST_FOCUS;

// Create the include descriptor
return new ViewElementDescriptor(
                xmlName,  // xml_name
                xmlName, // ui_name
                xmlName, // "class name"; the GLE only treats this as an element tag
"Requests focus for the parent element or one of its descendants", // tooltip
null,  // sdk_url
null,  // attributes
//Synthetic comment -- @@ -508,13 +523,10 @@
* Finds the descriptor and retrieves all its layout attributes.
*/
private AttributeDescriptor[] findViewLayoutAttributes(
            String viewFqcn) {
        ViewElementDescriptor viewDesc = findDescriptorByClass(viewFqcn);
        if (viewDesc != null) {
            return viewDesc.getLayoutAttributes();
}

return null;
//Synthetic comment -- @@ -549,4 +561,45 @@
}
}
}

    /**
     * Returns the {@link ViewElementDescriptor} with the given fully qualified class
     * name, or null if not found. This is a quick map lookup.
     *
     * @param fqcn the fully qualified class name
     * @return the corresponding {@link ViewElementDescriptor} or null
     */
    public ViewElementDescriptor findDescriptorByClass(String fqcn) {
        return mFqcnToDescriptor.get(fqcn);
    }

    /**
     * Returns the {@link ViewElementDescriptor} with the given XML tag name,
     * which usually does not include the package (depending on the
     * value of {@link ViewElementDescriptor#viewNeedsPackage(String)}).
     *
     * @param tag the XML tag name
     * @return the corresponding {@link ViewElementDescriptor} or null
     */
    public ViewElementDescriptor findDescriptorByTag(String tag) {
        // TODO: Consider whether we need to add a direct map lookup for this as well.
        // Currently not done since this is not frequently needed (only needed for
        // exploded rendering which was already performing list iteration.)
        for (ViewElementDescriptor descriptor : mLayoutDescriptors) {
            if (tag.equals(descriptor.getXmlLocalName())) {
                return descriptor;
            }
        }

        return null;
    }

    /**
     * Returns a collection of all the view class names, including layouts
     *
     * @return a collection of all the view class names, never null
     */
    public Collection<String> getAllViewClassNames() {
        return mFqcnToDescriptor.keySet();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java
//Synthetic comment -- index b45a197..a18b821 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_VIEW_PKG;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_WEBKIT_PKG;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -53,7 +57,7 @@
private final String mFullClassName;

/** The list of layout attributes. Can be empty but not null. */
    private AttributeDescriptor[] mLayoutAttributes;

/** The super-class descriptor. Can be null. */
private ViewElementDescriptor mSuperClassDesc;
//Synthetic comment -- @@ -117,6 +121,16 @@
}

/**
     * Sets the list of layout attribute attributes.
     *
     * @param attributes the new layout attributes, not null
     */
    public void setLayoutAttributes(AttributeDescriptor[] attributes) {
        assert attributes != null;
        mLayoutAttributes = attributes;
    }

    /**
* Returns a new {@link UiViewElementNode} linked to this descriptor.
*/
@Override
//Synthetic comment -- @@ -168,4 +182,17 @@
return icon;
}

    /**
     * Returns true if views with the given fully qualified class name need to include
     * their package in the layout XML tag
     *
     * @param fqcn the fully qualified class name, such as android.widget.Button
     * @return true if the full package path should be included in the layout XML element
     *         tag
     */
    public static boolean viewNeedsPackage(String fqcn) {
        return !(fqcn.startsWith(ANDROID_WIDGET_PREFIX)
              || fqcn.startsWith(ANDROID_VIEW_PKG)
              || fqcn.startsWith(ANDROID_WEBKIT_PKG));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 6bd03b2..7471e54 100755

//Synthetic comment -- @@ -77,7 +77,7 @@
private final UiViewElementNode mUiViewNode;
private CanvasViewInfo mParent;
private ViewInfo mViewInfo;
    private final List<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

/**
* Is this view info an individually exploded view? This is the case for views
//Synthetic comment -- @@ -390,7 +390,8 @@
// etc)
if (mParent != null
&& mParent.mName.endsWith(GESTURE_OVERLAY_VIEW)
                && mParent.isRoot()
                && mParent.mChildren.size() == 1) {
return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 176b7bd..4923107 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;
import static com.android.sdklib.SdkConstants.FD_GEN_SOURCES;

import com.android.ide.common.rendering.LayoutLibrary;
//Synthetic comment -- @@ -43,14 +44,13 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor;
import com.android.ide.eclipse.adt.internal.editors.layout.ProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ChangeFlags;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
//Synthetic comment -- @@ -1661,7 +1661,7 @@
// is the same
labelClass),
actual,
                                    viewNeedsPackage(suggested) ? suggested : suggestedBase
);
addText(mErrorLabel, ", ");
}
//Synthetic comment -- @@ -1681,23 +1681,15 @@
}

private static Collection<String> getAndroidViewClassNames(IProject project) {
Sdk currentSdk = Sdk.getCurrent();
IAndroidTarget target = currentSdk.getTarget(project);
if (target != null) {
AndroidTargetData targetData = currentSdk.getTargetData(target);
LayoutDescriptors layoutDescriptors = targetData.getLayoutDescriptors();
            return layoutDescriptors.getAllViewClassNames();
}

        return Collections.emptyList();
}

/** Add a normal line of text to the styled text widget. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java
//Synthetic comment -- index 87fdc14..4a92108 100644

//Synthetic comment -- @@ -1230,20 +1230,7 @@
protected ViewElementDescriptor getElementDescriptor(String fqcn) {
AndroidTargetData data = mEditor.getTargetData();
if (data != null) {
            return data.getLayoutDescriptors().findDescriptorByClass(fqcn);
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index 60b5662..5bd35a1 100644

//Synthetic comment -- @@ -16,9 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.uimodel;

import static com.android.ide.common.layout.LayoutConstants.FQCN_FRAME_LAYOUT;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -29,8 +32,6 @@

import org.eclipse.core.resources.IProject;

/**
* Specialized version of {@link UiElementNode} for the {@link ViewElementDescriptor}s.
*/
//Synthetic comment -- @@ -67,7 +68,6 @@
// owned by a FrameLayout.
// TODO replace by something user-configurable.

IProject project = getEditor().getProject();
if (project != null) {
Sdk currentSdk = Sdk.getCurrent();
//Synthetic comment -- @@ -76,21 +76,17 @@
if (target != null) {
AndroidTargetData data = currentSdk.getTargetData(target);
if (data != null) {
                            LayoutDescriptors descriptors = data.getLayoutDescriptors();
                            ViewElementDescriptor desc =
                                descriptors.findDescriptorByClass(FQCN_FRAME_LAYOUT);
                            if (desc != null) {
                                layout_attrs = desc.getLayoutAttributes();
                                need_xmlns = true;
                            }
}
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
                            if (subRegion == null) {
                                return null;
                            }
int regionStart = region.getStartOffset();
int subregionStart = subRegion.getStart();
int relativeOffset = offset - (regionStart + subregionStart);







