/*Fix property sheet value completion

This changeset fixes the value completion such that you can add custom
values into properties that also have enum fields.

Rather than have separate completion routines for properties based on
whether they contain an enum, a flag, a reference, etc., have a single
completer which considers all the various formats and combines the
results.

In addition to combining results, this now also offers completion on
dimensions, and offers theme attribute values for references as well.

Change-Id:Idbc1799a34b3a3f14ea567654953925bf12afb8f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index bcb552f..af8de68 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor.ATTRIBUTE_ICON_FILENAME;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_PX;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_SP;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
//Synthetic comment -- @@ -1138,11 +1141,11 @@
* {@link #completeSuffix}.
*/
private static final String[] sDimensionUnits = new String[] {
        UNIT_DP,
"<b>Density-independent Pixels</b> - an abstract unit that is based on the physical "
+ "density of the screen.",

        UNIT_SP,
"<b>Scale-independent Pixels</b> - this is like the dp unit, but it is also scaled by "
+ "the user's font size preference.",

//Synthetic comment -- @@ -1155,7 +1158,7 @@
"in", //$NON-NLS-1$
"<b>Inches</b> - based on the physical size of the screen.",

        UNIT_PX,
"<b>Pixels</b> - corresponds to actual pixels on the screen. Not recommended.",
};









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/EnumValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/EnumValueCompleter.java
deleted file mode 100644
//Synthetic comment -- index 4746a72..0000000

//Synthetic comment -- @@ -1,56 +0,0 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/FlagValueCompleter.java
deleted file mode 100644
//Synthetic comment -- index 6958e0f..0000000

//Synthetic comment -- @@ -1,80 +0,0 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyValueCompleter.java
new file mode 100644
//Synthetic comment -- index 0000000..f2bf073

//Synthetic comment -- @@ -0,0 +1,41 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;

class PropertyValueCompleter extends ValueCompleter {
    private final XmlProperty mProperty;

    PropertyValueCompleter(XmlProperty property) {
        mProperty = property;
    }

    @Override
    @Nullable
    protected CommonXmlEditor getEditor() {
        return mProperty.getXmlEditor();
    }

    @Override
    @NonNull
    protected AttributeDescriptor getDescriptor() {
        return mProperty.getDescriptor();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java
//Synthetic comment -- index 19dc0bc..a5e3f64 100644

//Synthetic comment -- @@ -17,7 +17,9 @@

import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.resources.ResourceItem;
//Synthetic comment -- @@ -98,12 +100,21 @@
ResourceRepository repository = data.getFrameworkResources();
addMatches(repository, prefix, true /* isSystem */, results);
}
        } else if (prefix.startsWith("?") && //$NON-NLS-1$
                prefix.regionMatches(true /* ignoreCase */, 0, PREFIX_ANDROID_THEME_REF, 0,
                        Math.min(prefix.length() - 1, PREFIX_ANDROID_THEME_REF.length()))) {
            AndroidTargetData data = editor.getTargetData();
            if (data != null) {
                ResourceRepository repository = data.getFrameworkResources();
                addMatches(repository, prefix, true /* isSystem */, results);
            }
}


// When completing project resources skip framework resources unless
// the prefix possibly completes both, such as "@an" which can match
// both the project resource @animator as well as @android:string
        if (!prefix.startsWith("@and") && !prefix.startsWith("?and")) { //$NON-NLS-1$ //$NON-NLS-2$
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
//Synthetic comment -- @@ -136,7 +147,14 @@
if (prefix.regionMatches(typeStart, type.getName(), 0,
Math.min(type.getName().length(), prefix.length() - typeStart))) {
StringBuilder sb = new StringBuilder();
                if (prefix.length() == 0 || prefix.startsWith(PREFIX_RESOURCE_REF)) {
                    sb.append(PREFIX_RESOURCE_REF);
                } else {
                    if (type != ResourceType.ATTR) {
                        continue;
                    }
                    sb.append(PREFIX_THEME_REF);
                }

if (type == ResourceType.ID && prefix.startsWith(NEW_ID_PREFIX)) {
sb.append('+');








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java
new file mode 100644
//Synthetic comment -- index 0000000..944b889

//Synthetic comment -- @@ -0,0 +1,186 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.api.IAttributeInfo.Format.BOOLEAN;
import static com.android.ide.common.api.IAttributeInfo.Format.DIMENSION;
import static com.android.ide.common.api.IAttributeInfo.Format.ENUM;
import static com.android.ide.common.api.IAttributeInfo.Format.FLAG;
import static com.android.ide.common.api.IAttributeInfo.Format.FLOAT;
import static com.android.ide.common.api.IAttributeInfo.Format.INTEGER;
import static com.android.ide.common.api.IAttributeInfo.Format.REFERENCE;
import static com.android.ide.common.api.IAttributeInfo.Format.STRING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT_SIZE;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_SP;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * An {@link IContentProposalProvider} which completes possible property values
 * for Android properties, completing resource strings, flag values, enum
 * values, as well as dimension units.
 */
abstract class ValueCompleter implements IContentProposalProvider {
    @Nullable
    protected abstract CommonXmlEditor getEditor();

    @NonNull
    protected abstract AttributeDescriptor getDescriptor();

    @Override
    public IContentProposal[] getProposals(String contents, int position) {
        AttributeDescriptor descriptor = getDescriptor();
        IAttributeInfo info = descriptor.getAttributeInfo();
        EnumSet<Format> formats = info.getFormats();

        List<IContentProposal> proposals = new ArrayList<IContentProposal>();

        String prefix = contents; // TODO: Go back to position inside the array?

        // TODO: If the user is typing in a number, or a number plus a prefix of a dimension unit,
        // then propose that number plus the completed dimension unit (using sp for text, dp
        // for other properties and maybe both if I'm not sure)
        if (formats.contains(STRING)
                && !contents.isEmpty()
                && (formats.size() > 1 && formats.contains(REFERENCE) ||
                        formats.size() > 2)
                && !contents.startsWith(PREFIX_RESOURCE_REF)
                && !contents.startsWith(PREFIX_THEME_REF)) {
            proposals.add(new ContentProposal(contents));
        }

        if (!contents.isEmpty() && Character.isDigit(contents.charAt(0))
                && (formats.contains(DIMENSION)
                        || formats.contains(INTEGER)
                        || formats.contains(FLOAT))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, n = contents.length(); i < n; i++) {
                char c = contents.charAt(i);
                if (Character.isDigit(c)) {
                    sb.append(c);
                } else {
                    break;
                }
            }

            String number = sb.toString();
            if (formats.contains(Format.DIMENSION)) {
                if (descriptor.getXmlLocalName().equals(ATTR_TEXT_SIZE)) {
                    proposals.add(new ContentProposal(number + UNIT_SP));
                }
                proposals.add(new ContentProposal(number + UNIT_DP));
            } else if (formats.contains(Format.INTEGER)) {
                proposals.add(new ContentProposal(number));
            }
            // Perhaps offer other units too -- see AndroidContentAssist.sDimensionUnits
        }

        if (formats.contains(REFERENCE) || contents.startsWith(PREFIX_RESOURCE_REF)
                || contents.startsWith(PREFIX_THEME_REF)) {
            CommonXmlEditor editor = getEditor();
            if (editor != null) {
                String[] matches = ResourceValueCompleter.computeResourceStringMatches(
                        editor,
                        descriptor, contents.substring(0, position));
                for (String match : matches) {
                    proposals.add(new ContentProposal(match));
                }
            }
        }

        if (formats.contains(FLAG)) {
            String[] values = info.getFlagValues();
            if (values != null) {
                // Flag completion
                int flagStart = prefix.lastIndexOf('|');
                String prepend = null;
                if (flagStart != -1) {
                    prepend = prefix.substring(0, flagStart + 1);
                    prefix = prefix.substring(flagStart + 1).trim();
                }

                boolean exactMatch = false;
                for (String value : values) {
                    if (prefix.equals(value)) {
                        exactMatch = true;
                        proposals.add(new ContentProposal(contents));

                        break;
                    }
                }

                if (exactMatch) {
                    prepend = contents + '|';
                    prefix = "";
                }

                for (String value : values) {
                    if (AdtUtils.startsWithIgnoreCase(value, prefix)) {
                        if (prepend != null && prepend.contains(value)) {
                            continue;
                        }
                        String match;
                        if (prepend != null) {
                            match = prepend + value;
                        } else {
                            match = value;
                        }
                        proposals.add(new ContentProposal(match));
                    }
                }
            }
        } else if (formats.contains(ENUM)) {
            String[] values = info.getEnumValues();
            if (values != null) {
                for (String value : values) {
                    if (AdtUtils.startsWithIgnoreCase(value, prefix)) {
                        proposals.add(new ContentProposal(value));
                    }
                }

                for (String value : values) {
                    if (!AdtUtils.startsWithIgnoreCase(value, prefix)) {
                        proposals.add(new ContentProposal(value));
                    }
                }
            }
        } else if (formats.contains(BOOLEAN)) {
            proposals.add(new ContentProposal(VALUE_TRUE));
            proposals.add(new ContentProposal(VALUE_FALSE));
        }

        return proposals.toArray(new IContentProposal[proposals.size()]);
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java
//Synthetic comment -- index d3985a9..2a756c9 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -43,7 +42,6 @@
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Map;

/**
//Synthetic comment -- @@ -66,24 +64,29 @@
mDescriptor = descriptor;
}

    @NonNull
public PropertyFactory getFactory() {
return mFactory;
}

    @NonNull
public UiViewElementNode getNode() {
return mNode;
}

    @NonNull
public AttributeDescriptor getDescriptor() {
return mDescriptor;
}

@Override
    @NonNull
public String getName() {
return mDescriptor.getXmlLocalName();
}

@Override
    @NonNull
public String getTitle() {
String name = mDescriptor.getXmlLocalName();
int nameLength = name.length();
//Synthetic comment -- @@ -137,12 +140,7 @@
} else if (adapter == IContentProposalProvider.class) {
IAttributeInfo info = mDescriptor.getAttributeInfo();
if (info != null) {
                return adapter.cast(new PropertyValueCompleter(this));
}
// Fallback: complete values on resource values
return adapter.cast(new ResourceValueCompleter(this));
//Synthetic comment -- @@ -183,6 +181,7 @@
return s != null && s.toString().length() > 0;
}

    @Nullable
public String getStringValue() {
Element element = (Element) mNode.getXmlNode();
if (element == null) {
//Synthetic comment -- @@ -217,6 +216,7 @@
}

@Override
    @Nullable
public Object getValue() throws Exception {
return getStringValue();
}
//Synthetic comment -- @@ -242,6 +242,7 @@
}

@Override
    @NonNull
public Property getComposite(Property[] properties) {
return XmlPropertyComposite.create(properties);
}
//Synthetic comment -- @@ -251,7 +252,8 @@
return mFactory.getGraphicalEditor();
}

    @Nullable
    CommonXmlEditor getXmlEditor() {
GraphicalEditorPart graphicalEditor = getGraphicalEditor();
if (graphicalEditor != null) {
return graphicalEditor.getEditorDelegate().getEditor();
//Synthetic comment -- @@ -260,11 +262,12 @@
return null;
}

    @Nullable
public Property getParent() {
return mParent;
}

    public void setParent(@Nullable Property parent) {
mParent = parent;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyComposite.java
//Synthetic comment -- index 7abc91c..af9e13b 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import com.android.annotations.NonNull;
import com.google.common.base.Objects;

import org.eclipse.wb.internal.core.model.property.Property;
//Synthetic comment -- @@ -46,6 +47,7 @@
}

@Override
    @NonNull
public String getTitle() {
return mProperties[0].getTitle();
}
//Synthetic comment -- @@ -103,6 +105,7 @@
}
}

    @NonNull
public static XmlPropertyComposite create(Property... properties) {
// Cast from Property into XmlProperty
XmlProperty[] xmlProperties = new XmlProperty[properties.length];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index a7a863c..7cb3f66 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

//Synthetic comment -- @@ -120,22 +124,29 @@
if (text.startsWith("@") || text.startsWith("?")) { //$NON-NLS-1$ //$NON-NLS-2$
// Yes, try to resolve it in order to show better info
XmlProperty xmlProperty = (XmlProperty) property;
                GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
                if (graphicalEditor != null) {
                    ResourceResolver resolver = graphicalEditor.getResourceResolver();
                    boolean isFramework = text.startsWith(PREFIX_ANDROID_RESOURCE_REF)
                            || text.startsWith(PREFIX_ANDROID_THEME_REF);
                    resValue = resolver.findResValue(text, isFramework);
                    while (resValue != null && resValue.getValue() != null) {
                        String value = resValue.getValue();
                        if (value.startsWith(PREFIX_RESOURCE_REF)
                                || value.startsWith(PREFIX_THEME_REF)) {
                            // TODO: do I have to strip off the @ too?
                            isFramework = isFramework
                                    || value.startsWith(PREFIX_ANDROID_RESOURCE_REF)
                                    || value.startsWith(PREFIX_ANDROID_THEME_REF);;
                            ResourceValue v = resolver.findResValue(text, isFramework);
                            if (v != null && !value.equals(v.getValue())) {
                                resValue = v;
                            } else {
                                break;
                            }
} else {
break;
}
}
}
} else if (text.startsWith("#") && text.matches("#\\p{XDigit}+")) { //$NON-NLS-1$
//Synthetic comment -- @@ -149,34 +160,36 @@
if (value.startsWith("#") || value.endsWith(DOT_XML) //$NON-NLS-1$
&& value.contains("res/color")) { //$NON-NLS-1$ // TBD: File.separator?
XmlProperty xmlProperty = (XmlProperty) property;
                    GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
                    if (graphicalEditor != null) {
                        ResourceResolver resolver = graphicalEditor.getResourceResolver();
                        RGB rgb = ResourceHelper.resolveColor(resolver, resValue);
                        if (rgb != null) {
                            Color color = new Color(gc.getDevice(), rgb);
                            // draw color sample
                            Color oldBackground = gc.getBackground();
                            Color oldForeground = gc.getForeground();
                            try {
                                int width_c = SAMPLE_SIZE;
                                int height_c = SAMPLE_SIZE;
                                int x_c = x;
                                int y_c = y + (height - height_c) / 2;
                                // update rest bounds
                                int delta = SAMPLE_SIZE + SAMPLE_MARGIN;
                                x += delta;
                                width -= delta;
                                // fill
                                gc.setBackground(color);
                                gc.fillRectangle(x_c, y_c, width_c, height_c);
                                // draw line
                                gc.setForeground(IColorConstants.gray);
                                gc.drawRectangle(x_c, y_c, width_c, height_c);
                            } finally {
                                gc.setBackground(oldBackground);
                                gc.setForeground(oldForeground);
                            }
                            color.dispose();
}
}
} else {
Image swtImage = null;
//Synthetic comment -- @@ -350,37 +363,38 @@
// Multiple resource types (such as string *and* boolean):
// just use a reference chooser
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
                if (graphicalEditor != null) {
                    LayoutEditorDelegate delegate = graphicalEditor.getEditorDelegate();
                    IProject project = delegate.getEditor().getProject();
                    if (project != null) {
                        // get the resource repository for this project and the system resources.
                        ResourceRepository projectRepository =
                            ResourceManager.getInstance().getProjectResources(project);
                        Shell shell = AdtPlugin.getDisplay().getActiveShell();
                        ReferenceChooserDialog dlg = new ReferenceChooserDialog(
                                project,
                                projectRepository,
                                shell);
                        dlg.setPreviewHelper(new ResourcePreviewHelper(dlg, graphicalEditor));

                        String currentValue = (String) property.getValue();
                        dlg.setCurrentResource(currentValue);

                        if (dlg.open() == Window.OK) {
                            String resource = dlg.getCurrentResource();
                            if (resource != null) {
                                // Returns null for cancel, "" for clear and otherwise a new value
                                if (resource.length() > 0) {
                                    property.setValue(resource);
                                } else {
                                    property.setValue(null);
                                }
}
}

                        return;
}
}
} else if (type != null) {
// Single resource type: use a resource chooser
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
//Synthetic comment -- @@ -418,7 +432,8 @@
@NonNull
private static Map<String, Image> getImageCache(@NonNull Property property) {
XmlProperty xmlProperty = (XmlProperty) property;
        GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
        IProject project = graphicalEditor.getProject();
try {
Map<String, Image> cache = (Map<String, Image>) project.getSessionProperty(CACHE_NAME);
if (cache == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleterTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleterTest.java
new file mode 100644
//Synthetic comment -- index 0000000..bbe82e3

//Synthetic comment -- @@ -0,0 +1,151 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.layout.TestAttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;

import org.eclipse.jface.fieldassist.IContentProposal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class ValueCompleterTest extends TestCase {
    private void checkCompletion(String text, int offset,
            String property, EnumSet<Format> formats, String[] values,
            List<String> expected) {
        assertTrue(text.length() >= offset);

        TestAttributeInfo info =
                new TestAttributeInfo(property, formats, "unittest", values, values, null);
        TestValueCompleter completer = new TestValueCompleter(
                new TestTextAttributeDescriptor(property, info));
        IContentProposal[] proposals = completer.getProposals(text, offset);
        List<String> actual = new ArrayList<String>();
        for (IContentProposal proposal : proposals) {
            String content = proposal.getContent();
            actual.add(content);
        }
        assertEquals(expected.toString(), actual.toString());
    }

    public void test() throws Exception {
        checkCompletion("@android:string", 3, "text",
                EnumSet.of(Format.REFERENCE), null,
                Arrays.asList(new String[] { }));
    }

    public void test1a() throws Exception {
        checkCompletion("matc", 4, "layout_width",
                EnumSet.of(Format.DIMENSION, Format.ENUM),
                new String[] { "fill_parent", "match_parent", "wrap_content" },

                Arrays.asList(new String[] { "match_parent", "fill_parent", "wrap_content" }));
    }

    public void test1b() throws Exception {
        checkCompletion("fi", 2, "layout_width",
                EnumSet.of(Format.DIMENSION, Format.ENUM),
                new String[] { "fill_parent", "match_parent", "wrap_content" },

                Arrays.asList(new String[] { "fill_parent", "match_parent", "wrap_content" }));
    }

    public void test2() throws Exception {
        checkCompletion("50", 2, "layout_width",
                EnumSet.of(Format.DIMENSION, Format.ENUM),
                new String[] { "fill_parent", "match_parent", "wrap_content" },

                Arrays.asList(new String[] { "50dp", "fill_parent", "match_parent",
                        "wrap_content" }));
    }

    public void test3() throws Exception {
        checkCompletion("42", 2, "textSize",
                EnumSet.of(Format.DIMENSION),
                null,

                Arrays.asList(new String[] { "42sp", "42dp" }));
    }

    public void test4() throws Exception {
        checkCompletion("", 0, "gravity",
                EnumSet.of(Format.FLAG),
                new String[] { "top", "bottom", "left", "right", "center" },

                Arrays.asList(new String[] { "top", "bottom", "left", "right", "center" }));
    }

    public void test5() throws Exception {
        checkCompletion("left", 4, "gravity",
                EnumSet.of(Format.FLAG),
                new String[] { "top", "bottom", "left", "right", "center" },

                Arrays.asList(new String[] {
                        "left", "left|top", "left|bottom", "left|right", "left|center" }));
    }

    public void test6() throws Exception {
        checkCompletion("left|top", 8, "gravity",
                EnumSet.of(Format.FLAG),
                new String[] { "top", "bottom", "left", "right", "center" },

                Arrays.asList(new String[] {
                        "left|top", "left|top|bottom", "left|top|right", "left|top|center" }));
    }

    // TODO ?android

    private class TestTextAttributeDescriptor extends TextAttributeDescriptor {
        public TestTextAttributeDescriptor(String xmlLocalName, IAttributeInfo attrInfo) {
            super(xmlLocalName, ANDROID_URI, attrInfo);
        }
    }

    private class TestValueCompleter extends ValueCompleter {
        private final AttributeDescriptor mDescriptor;

        TestValueCompleter(AttributeDescriptor descriptor) {
            mDescriptor = descriptor;
            assert descriptor.getAttributeInfo() != null;
        }

        @Override
        @Nullable
        protected CommonXmlEditor getEditor() {
            return null;
        }

        @Override
        @NonNull
        protected AttributeDescriptor getDescriptor() {
            return mDescriptor;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java
//Synthetic comment -- index 40b468d..c43a8ed 100644

//Synthetic comment -- @@ -149,6 +149,7 @@

public static final String ATTR_ID = "id";                         //$NON-NLS-1$
public static final String ATTR_TEXT = "text";                     //$NON-NLS-1$
    public static final String ATTR_TEXT_SIZE = "textSize";            //$NON-NLS-1$
public static final String ATTR_LABEL = "label";                   //$NON-NLS-1$
public static final String ATTR_HINT = "hint";                     //$NON-NLS-1$
public static final String ATTR_PROMPT = "prompt";                 //$NON-NLS-1$
//Synthetic comment -- @@ -257,6 +258,12 @@
public static final String VALUE_IF_ROOM = "ifRoom";               //$NON-NLS-1$
public static final String VALUE_ALWAYS = "always";                //$NON-NLS-1$

    // Units
    public static final String UNIT_DP = "dp";                         //$NON-NLS-1$
    public static final String UNIT_DIP = "dip";                       //$NON-NLS-1$
    public static final String UNIT_SP = "sp";                         //$NON-NLS-1$
    public static final String UNIT_PX = "px";                         //$NON-NLS-1$

// Filenames and folder names
public static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml"; //$NON-NLS-1$
public static final String OLD_PROGUARD_FILE = "proguard.cfg";     //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index 6734592..c7d688f 100644

//Synthetic comment -- @@ -17,8 +17,12 @@
package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT_SIZE;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ITEM;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DIP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_PX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -42,8 +46,6 @@
/**
* Check for px dimensions instead of dp dimensions.
* Also look for non-"sp" text sizes.
*/
public class PxUsageDetector extends LayoutDetector {
/** The main issue discovered by this detector */
//Synthetic comment -- @@ -123,7 +125,7 @@
}

String value = attribute.getValue();
        if (value.endsWith(UNIT_PX) && value.matches("\\d+px")) { //$NON-NLS-1$
if (value.charAt(0) == '0') {
// 0px is fine. 0px is 0dp regardless of density...
return;
//Synthetic comment -- @@ -132,8 +134,8 @@
context.report(PX_ISSUE, attribute, context.getLocation(attribute),
"Avoid using \"px\" as units; use \"dp\" instead", null);
}
        } else if (ATTR_TEXT_SIZE.equals(attribute.getLocalName())
                && (value.endsWith(UNIT_DP) || value.endsWith(UNIT_DIP))
&& (value.matches("\\d+di?p"))) {
if (context.isEnabled(DP_ISSUE)) {
context.report(DP_ISSUE, attribute, context.getLocation(attribute),
//Synthetic comment -- @@ -185,7 +187,7 @@
|| text.charAt(j - 1) == 'i')) { // ends with dp or di
text = text.trim();
String name = item.getAttribute(ATTR_NAME);
                    if ((name.equals(ATTR_TEXT_SIZE)
|| name.equals("android:textSize"))  //$NON-NLS-1$
&& text.matches("\\d+di?p")) {  //$NON-NLS-1$
if (context.isEnabled(DP_ISSUE)) {







