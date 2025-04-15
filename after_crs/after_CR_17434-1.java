/*ADT: Fix attributes UI in Android Manifest Editor.

External bug:http://b.android.com/11392Issue: all customized fields were showing as regular "string"
references (with the default string browser) instead of having
customized pre-selected choices, etc.

The core issue is that the UI widgets were created using reflection
but I changed the base class constructor of the default
TextAttributeDescriptor recently and forgot to update DescriptorUtils
accordingly. Since it was failing to create a custom widget it was
reverting to the default UI for a string reference.

The long-term fix is to stop using reflection.
Creator pattern to the rescue!
Instead there's a new ITextAttributeCreator interface and the
override map uses a static "creator" instance that will instantiate
the correct UI widget when needed.

Change-Id:Ie7afbf682a2aa8ae18565d08445e73a8c051a6c7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 52cde55..277c125 100644

//Synthetic comment -- @@ -27,8 +27,6 @@

import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
//Synthetic comment -- @@ -64,27 +62,6 @@
private static final String BREAK = "$break"; //$NON-NLS-1$

/**
* Add all {@link AttributeInfo} to the the array of {@link AttributeDescriptor}.
*
* @param attributes The list of {@link AttributeDescriptor} to append to
//Synthetic comment -- @@ -96,15 +73,13 @@
* @param requiredAttributes An optional set of attributes to mark as "required" (i.e. append
*        a "*" to their UI name as a hint for the user.) If not null, must contains
*        entries in the form "elem-name/attr-name". Elem-name can be "*".
     * @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
public static void appendAttributes(ArrayList<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri, AttributeInfo[] infos,
Set<String> requiredAttributes,
            Map<String, ITextAttributeCreator> overrides) {
for (AttributeInfo info : infos) {
boolean required = false;
if (requiredAttributes != null) {
//Synthetic comment -- @@ -129,15 +104,13 @@
*              See {@link SdkConstants#NS_RESOURCES} for a common value.
* @param required True if the attribute is to be marked as "required" (i.e. append
*        a "*" to its UI name as a hint for the user.)
     * @param overrides A map [attribute name => ITextAttributeCreator creator].
*/
public static void appendAttribute(ArrayList<AttributeDescriptor> attributes,
String elementXmlName,
String nsUri,
AttributeInfo info, boolean required,
            Map<String, ITextAttributeCreator> overrides) {
AttributeDescriptor attr = null;

String xmlLocalName = info.getName();
//Synthetic comment -- @@ -195,8 +168,9 @@
sb.append("]"); //$NON-NLS-1$

if (required) {
                // Note: this string is split in 2 to make it translatable.
                sb.append(".@@");          //$NON-NLS-1$ @@ inserts a break and is not translatable
                sb.append("* Required.");
}

// The extra space at the end makes the tooltip more readable on Windows.
//Synthetic comment -- @@ -207,7 +181,11 @@

// Create a specialized attribute if we can
if (overrides != null) {
                for (Entry<String, ITextAttributeCreator> entry: overrides.entrySet()) {
                    // The override key can have the following formats:
                    //   */xmlLocalName
                    //   element/xmlLocalName
                    //   element1,element2,...,elementN/xmlLocalName
String key = entry.getKey();
String elements[] = key.split("/");          //$NON-NLS-1$
String overrideAttrLocalName = null;
//Synthetic comment -- @@ -241,35 +219,9 @@
continue;
}

                    ITextAttributeCreator override = entry.getValue();
                    if (override != null) {
                        attr = override.create(xmlLocalName, uiName, nsUri, tooltip, info);
}
}
} // if overrides








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ITextAttributeCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ITextAttributeCreator.java
new file mode 100755
//Synthetic comment -- index 0000000..f01909f

//Synthetic comment -- @@ -0,0 +1,52 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.sdklib.SdkConstants;


/**
 * The {@link ITextAttributeCreator} interface is used by the appendAttribute(...) in
 * {@link DescriptorsUtils} to allows callers to override the kind of
 * {@link TextAttributeDescriptor} created for a given XML attribute name.
 * <p/>
 * The <code>create()</code> method must take arguments that are similar to the
 * single constructor for {@link TextAttributeDescriptor}.
 */
public interface ITextAttributeCreator {

    /**
     * Creates a new {@link TextAttributeDescriptor} instance for the given XML name,
     * UI name and tooltip.
     *
     * @param xmlLocalName The XML name of the attribute (case sensitive)
     * @param uiName The UI attribute name.
     * @param nsUri The URI of the attribute. Can be null if attribute has no namespace.
     *              See {@link SdkConstants#NS_RESOURCES} for a common value.
     * @param tooltip An optional tooltip.
     * @param attrInfo The {@link IAttributeInfo} of this attribute. Can't be null.
     *
     * @return A new {@link TextAttributeDescriptor} (or derived) instance.
     */
    public TextAttributeDescriptor create(
            String xmlLocalName,
            String uiName,
            String nsUri,
            String tooltip,
            IAttributeInfo attrInfo);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ListAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ListAttributeDescriptor.java
//Synthetic comment -- index f990553..ba5df50 100644

//Synthetic comment -- @@ -34,6 +34,17 @@
private String[] mValues = null;

/**
     * Used by {@link DescriptorsUtils} to create instances of this descriptor.
     */
    public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
        public TextAttributeDescriptor create(String xmlLocalName,
                String uiName, String nsUri, String tooltip,
                IAttributeInfo attrInfo) {
            return new ListAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip, attrInfo);
        }
    };

    /**
* Creates a new {@link ListAttributeDescriptor}.
* <p/>
* If <code>attrInfo</code> is not null and has non-null enum values, these will be








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ReferenceAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ReferenceAttributeDescriptor.java
//Synthetic comment -- index 63472c7..3e4c413 100644

//Synthetic comment -- @@ -17,10 +17,12 @@
package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.ui.ResourceValueCellEditor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiResourceAttributeNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -33,11 +35,27 @@
*/
public final class ReferenceAttributeDescriptor extends TextAttributeDescriptor {

    /**
     * The {@link ResourceType} that this reference attribute can accept. It can be null,
     * in which case any reference type can be used.
     */
private ResourceType mResourceType;

/**
     * Used by {@link DescriptorsUtils} to create instances of this descriptor.
     */
    public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
        public TextAttributeDescriptor create(String xmlLocalName,
                String uiName, String nsUri, String tooltip,
                IAttributeInfo attrInfo) {
            return new ReferenceAttributeDescriptor(
                    ResourceType.DRAWABLE,
                    xmlLocalName, uiName, nsUri, tooltip,
                    new AttributeInfo(xmlLocalName, new Format[] { Format.REFERENCE }));
        }
    };

    /**
* Creates a reference attributes that can contain any type of resources.
* @param xmlLocalName The XML name of the attribute (case sensitive)
* @param uiName The UI name of the attribute. Cannot be an empty string and cannot be null.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/TextAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/TextAttributeDescriptor.java
//Synthetic comment -- index 4f38cf5..c02b35e 100644

//Synthetic comment -- @@ -52,8 +52,12 @@
* @param tooltip A non-empty tooltip string or null
* @param attrInfo The {@link IAttributeInfo} of this attribute. Can't be null.
*/
    public TextAttributeDescriptor(
            String xmlLocalName,
            String uiName,
            String nsUri,
            String tooltip,
            IAttributeInfo attrInfo) {
super(xmlLocalName, nsUri, attrInfo);
mUiName = uiName;
mTooltip = (tooltip != null && tooltip.length() > 0) ? tooltip : null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index 796e018..55a635c 100644

//Synthetic comment -- @@ -23,12 +23,12 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ITextAttributeCreator;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.IStatus;
//Synthetic comment -- @@ -177,32 +177,24 @@
// The key for each override is "element1,element2,.../attr-xml-local-name" or
// "*/attr-xml-local-name" to match the attribute in any element.

        Map<String, ITextAttributeCreator> overrides = new HashMap<String, ITextAttributeCreator>();

        overrides.put("*/icon",             ReferenceAttributeDescriptor.CREATOR);  //$NON-NLS-1$

        overrides.put("*/theme",            ThemeAttributeDescriptor.CREATOR);      //$NON-NLS-1$
        overrides.put("*/permission",       ListAttributeDescriptor.CREATOR);       //$NON-NLS-1$
        overrides.put("*/targetPackage",    ManifestPkgAttrDescriptor.CREATOR);     //$NON-NLS-1$

        overrides.put("uses-library/name",  ListAttributeDescriptor.CREATOR);       //$NON-NLS-1$
        overrides.put("action,category,uses-permission/" + ANDROID_NAME_ATTR,       //$NON-NLS-1$
                                            ListAttributeDescriptor.CREATOR);

        overrides.put("application/" + ANDROID_NAME_ATTR,                           //$NON-NLS-1$
                                            ApplicationAttributeDescriptor.CREATOR);

overrideClassName(overrides, "activity", SdkConstants.CLASS_ACTIVITY);           //$NON-NLS-1$
overrideClassName(overrides, "receiver", SdkConstants.CLASS_BROADCASTRECEIVER);  //$NON-NLS-1$
        overrideClassName(overrides, "service",  SdkConstants.CLASS_SERVICE);            //$NON-NLS-1$
overrideClassName(overrides, "provider", SdkConstants.CLASS_CONTENTPROVIDER);    //$NON-NLS-1$
overrideClassName(overrides, "instrumentation", SdkConstants.CLASS_INSTRUMENTATION);    //$NON-NLS-1$

//Synthetic comment -- @@ -238,16 +230,19 @@
* Sets up an attribute override for ANDROID_NAME_ATTR using a ClassAttributeDescriptor
* with the specified class name.
*/
    private static void overrideClassName(
            Map<String, ITextAttributeCreator> overrides,
            String elementName,
            final String className) {
overrides.put(elementName + "/" + ANDROID_NAME_ATTR,
                new ITextAttributeCreator() {
public TextAttributeDescriptor create(String xmlName, String uiName, String nsUri,
                    String tooltip, IAttributeInfo attrInfo) {
uiName += "*";  //$NON-NLS-1$

                if (attrInfo == null) {
                    attrInfo = new AttributeInfo(xmlName, new Format[] { Format.STRING } );
                }

if (SdkConstants.CLASS_ACTIVITY.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -367,7 +362,7 @@
*/
private void inflateElement(
Map<String, DeclareStyleableInfo> styleMap,
            Map<String, ITextAttributeCreator> overrides,
Set<String> requiredAttributes,
HashMap<String, ElementDescriptor> existingElementDescs,
ElementDescriptor elemDesc,
//Synthetic comment -- @@ -376,8 +371,12 @@
assert styleName != null;
assert styleMap != null;

        if (styleMap == null) {
            return;
        }

// define attributes
        DeclareStyleableInfo style = styleMap.get(styleName);
if (style != null) {
ArrayList<AttributeDescriptor> attrDescs = new ArrayList<AttributeDescriptor>();
DescriptorsUtils.appendAttributes(attrDescs,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java
//Synthetic comment -- index de71f4e..a67f078 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.descriptors;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ITextAttributeCreator;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.model.UiClassAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -26,10 +28,23 @@
* Describes an 'Application' class XML attribute. It is displayed by a
* {@link UiClassAttributeNode}, that restricts creation and selection to classes
* inheriting from android.app.Application.
 * <p/>
 * Used by the override for application/name in {@link AndroidManifestDescriptors}.
*/
public class ApplicationAttributeDescriptor extends TextAttributeDescriptor {

    /**
     * Used by {@link DescriptorsUtils} to create instances of this descriptor.
     */
    public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
        public TextAttributeDescriptor create(String xmlLocalName,
                String uiName, String nsUri, String tooltip,
                IAttributeInfo attrInfo) {
            return new ApplicationAttributeDescriptor(
                    xmlLocalName, uiName, nsUri, tooltip, attrInfo);
        }
    };

public ApplicationAttributeDescriptor(String xmlLocalName, String uiName,
String nsUri, String tooltip, IAttributeInfo attrInfo) {
super(xmlLocalName, uiName, nsUri, tooltip, attrInfo);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ManifestPkgAttrDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ManifestPkgAttrDescriptor.java
//Synthetic comment -- index 2c36b32..d5a501a 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.descriptors;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ITextAttributeCreator;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.model.UiManifestPkgAttrNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
//Synthetic comment -- @@ -24,10 +26,22 @@

/**
* Describes a package XML attribute. It is displayed by a {@link UiManifestPkgAttrNode}.
 * <p/>
 * Used by the override for .../targetPackage in {@link AndroidManifestDescriptors}.
*/
public class ManifestPkgAttrDescriptor extends TextAttributeDescriptor {

    /**
     * Used by {@link DescriptorsUtils} to create instances of this descriptor.
     */
    public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
        public TextAttributeDescriptor create(String xmlLocalName,
                String uiName, String nsUri, String tooltip,
                IAttributeInfo attrInfo) {
            return new ManifestPkgAttrDescriptor(xmlLocalName, uiName, nsUri, tooltip, attrInfo);
        }
    };

public ManifestPkgAttrDescriptor(String xmlLocalName, String uiName, String nsUri,
String tooltip, IAttributeInfo attrInfo) {
super(xmlLocalName, uiName, nsUri, tooltip, attrInfo);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ThemeAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ThemeAttributeDescriptor.java
//Synthetic comment -- index dcb2488..b67ce43 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.descriptors;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ITextAttributeCreator;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -25,10 +27,22 @@

/**
* Describes a Theme/Style XML attribute displayed by a {@link UiResourceAttributeNode}
 * <p/>
 * Used by the override for .../theme in {@link AndroidManifestDescriptors}.
*/
public final class ThemeAttributeDescriptor extends TextAttributeDescriptor {

    /**
     * Used by {@link DescriptorsUtils} to create instances of this descriptor.
     */
    public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
        public TextAttributeDescriptor create(String xmlLocalName,
                String uiName, String nsUri, String tooltip,
                IAttributeInfo attrInfo) {
            return new ThemeAttributeDescriptor(xmlLocalName, uiName, nsUri, tooltip, attrInfo);
        }
    };

public ThemeAttributeDescriptor(String xmlLocalName, String uiName, String nsUri,
String tooltip, IAttributeInfo attrInfo) {
super(xmlLocalName, uiName, nsUri, tooltip, attrInfo);







