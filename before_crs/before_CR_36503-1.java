/*Fix issue 30529: Ensure that XML editor uses platform line separators

Change-Id:I5c4fa6273ce14f4682e9e7fa9a8fbe9f8cdc8d46*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 4ec6593..114e45a 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -262,6 +263,29 @@
return sb.toString();
}

/**
* Returns the current editor (the currently visible and active editor), or null if
* not found








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/DexDumpAction.java
//Synthetic comment -- index f8a080a..7345a04 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -164,15 +165,7 @@

final BufferedWriter writer = new BufferedWriter(new FileWriter(dstFile));

                String sep = System.getProperty("line.separator");                  //$NON-NLS-1$
                if (sep == null || sep.length() < 1) {
                    if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
                        sep = "\r\n";                                               //$NON-NLS-1$
                    } else {
                        sep = "\n";                                                 //$NON-NLS-1$
                    }
                }
                final String lineSep = sep;

int err = GrabProcessOutput.grabProcessOutput(
process,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 754da29..9cdbf81 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
mPrefs = prefs;
mStyle = style;
if (lineSeparator == null) {
            lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
}
mLineSeparator = lineSeparator;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java
//Synthetic comment -- index d4bb476..16519fe 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.pages;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.UiElementPart;
//Synthetic comment -- @@ -219,7 +220,7 @@
}
mUndoXmlParent.insertBefore(mUndoXmlNode, next);
if (next == null) {
                    Text sep = mUndoXmlDocument.createTextNode("\n");  //$NON-NLS-1$
mUndoXmlParent.insertBefore(sep, null);  // insert separator before end tag
}
success = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 23ce92c..42a8679 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -50,9 +51,11 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
//Synthetic comment -- @@ -1034,7 +1037,14 @@
}

// Insert indent text node before the new element
        Text indentNode = doc.createTextNode("\n" + indent); //$NON-NLS-1$
parentXmlNode.insertBefore(indentNode, xmlNextSibling);

// Insert the element itself
//Synthetic comment -- @@ -1044,7 +1054,7 @@
// a tag into an area where there was no whitespace before
// (e.g. a new child of <LinearLayout></LinearLayout>).
if (insertAfter != null) {
            Text sep = doc.createTextNode("\n" + insertAfter); //$NON-NLS-1$
parentXmlNode.insertBefore(sep, xmlNextSibling);
}








