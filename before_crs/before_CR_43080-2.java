/*Manifest Editor: class selector for <application backupAgent>.

<application android:backupAgent> needs a class selector
that accepts classes deriving from BackupAgent.

Change-Id:Id2e72e85967bc31a67d1eff2c342bcf7acc2cb3c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index ff555cc..8407073 100644

//Synthetic comment -- @@ -306,6 +306,12 @@
if (name.contains("ime") || name.startsWith("Ime")) {
name = name.replaceAll("(?<=^| )[iI]me(?=$| )", "IME");
}

return name;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index ba7894b..3429e43 100644

//Synthetic comment -- @@ -197,14 +197,18 @@
overrides.put("action,category,uses-permission/" + ANDROID_NAME_ATTR,       //$NON-NLS-1$
ListAttributeDescriptor.CREATOR);

        overrides.put("application/" + ANDROID_NAME_ATTR,                           //$NON-NLS-1$
                                            ApplicationAttributeDescriptor.CREATOR);

        overrideClassName(overrides, "activity", SdkConstants.CLASS_ACTIVITY);           //$NON-NLS-1$
        overrideClassName(overrides, "receiver", SdkConstants.CLASS_BROADCASTRECEIVER);  //$NON-NLS-1$
        overrideClassName(overrides, "service",  SdkConstants.CLASS_SERVICE);            //$NON-NLS-1$
        overrideClassName(overrides, "provider", SdkConstants.CLASS_CONTENTPROVIDER);    //$NON-NLS-1$
        overrideClassName(overrides, "instrumentation", SdkConstants.CLASS_INSTRUMENTATION);    //$NON-NLS-1$

// -- list element nodes already created --
// These elements are referenced by already opened editors, so we want to update them
//Synthetic comment -- @@ -235,18 +239,52 @@
SdkConstants.ANDROID_NS_NAME, SdkConstants.ANDROID_URI);
insertAttribute(MANIFEST_ELEMENT, xmlns);

assert sanityCheck(manifestMap, MANIFEST_ELEMENT);
}

/**
     * Sets up an attribute override for ANDROID_NAME_ATTR using a ClassAttributeDescriptor
* with the specified class name.
*/
private static void overrideClassName(
Map<String, ITextAttributeCreator> overrides,
String elementName,
final String className) {
        overrides.put(elementName + "/" + ANDROID_NAME_ATTR,
new ITextAttributeCreator() {
@Override
public TextAttributeDescriptor create(String xmlName, String nsUri,
//Synthetic comment -- @@ -262,7 +300,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_BROADCASTRECEIVER.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -271,7 +309,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_INSTRUMENTATION.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -280,7 +318,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
false /*defaultToProjectOnly*/);
} else {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -288,7 +326,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */);
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java
deleted file mode 100644
//Synthetic comment -- index 4f41ac2..0000000

//Synthetic comment -- @@ -1,62 +0,0 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.ide.eclipse.adt.internal.editors.manifest.descriptors;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ITextAttributeCreator;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.model.UiClassAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

/**
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
        @Override
        public TextAttributeDescriptor create(String xmlLocalName,
                String nsUri, IAttributeInfo attrInfo) {
            return new ApplicationAttributeDescriptor(
                    xmlLocalName, nsUri, attrInfo);
        }
    };

    private ApplicationAttributeDescriptor(String xmlLocalName, String nsUri,
            IAttributeInfo attrInfo) {
        super(xmlLocalName, nsUri, attrInfo);
    }

    /**
     * @return A new {@link UiClassAttributeNode} linked to this descriptor.
     */
    @Override
    public UiAttributeNode createUiNode(UiElementNode uiParent) {
        return new UiClassAttributeNode("android.app.Application", //$NON-NLS-1$
                null /* postCreationAction */, false /* mandatory */, this, uiParent,
                true /*defaultToProjectOnly*/);
    }
}







