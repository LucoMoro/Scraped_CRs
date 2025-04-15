/*Add logging to help track down issue #15003 in the wild

Change-Id:I3b7fb370e7e4b018d02b3986d81b14e89086c191*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index db5b3d8..4fe889d 100644

//Synthetic comment -- @@ -114,6 +114,12 @@
* The activator class controls the plug-in life cycle
*/
public class AdtPlugin extends AbstractUIPlugin implements ILogger {
/** The plug-in ID */
public static final String PLUGIN_ID = "com.android.ide.eclipse.adt"; //$NON-NLS-1$

//Synthetic comment -- @@ -1420,6 +1426,11 @@
// The resources files must have a file path similar to
//    project/res/.../*.xml
// There is no support for sub folders, so the segment count must be 4
if (file.getFullPath().segmentCount() == 4) {
// check if we are inside the res folder.
String segment = file.getFullPath().segment(1);
//Synthetic comment -- @@ -1449,24 +1460,45 @@
resourceChanged(file, resFolder.getType());
}
} else {
// if the res folder is null, this means the name is invalid,
// in this case we remove whatever android editors that was set
// as the default editor.
IEditorDescriptor desc = IDE.getDefaultEditor(file);
String editorId = desc.getId();
if (editorId.startsWith(AdtConstants.EDITORS_NAMESPACE)) {
// reset the default editor.
IDE.setDefaultEditor(file, null);
}
}
}
}
}
}

private void resourceAdded(IFile file, ResourceFolderType type) {
// set the default editor based on the type.
if (type == ResourceFolderType.LAYOUT) {
IDE.setDefaultEditor(file, LayoutEditor.ID);
} else if (type == ResourceFolderType.DRAWABLE
|| type == ResourceFolderType.VALUES) {
//Synthetic comment -- @@ -1475,8 +1507,14 @@
IDE.setDefaultEditor(file, MenuEditor.ID);
} else if (type == ResourceFolderType.XML) {
if (XmlEditor.canHandleFile(file)) {
IDE.setDefaultEditor(file, XmlEditor.ID);
} else {
// set a property to determine later if the XML can be handled
QualifiedName qname = new QualifiedName(
AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -1487,6 +1525,10 @@
}

private void resourceChanged(IFile file, ResourceFolderType type) {
if (type == ResourceFolderType.XML) {
IEditorDescriptor ed = IDE.getDefaultEditor(file);
if (ed == null || ed.getId() != XmlEditor.ID) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index c73cd6f..87a59d9 100644

//Synthetic comment -- @@ -612,6 +612,14 @@
IconFactory.getInstance().getIcon("editor_page_source")); //$NON-NLS-1$

if (!(mTextEditor.getTextViewer().getDocument() instanceof IStructuredDocument)) {
Status status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Error opening the Android XML editor. Is the document an XML file?");
throw new RuntimeException("Android XML Editor Error", new CoreException(status));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java
//Synthetic comment -- index 6ec52f1..4cd2ed5 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.xml;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.FirstElementParser;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
//Synthetic comment -- @@ -30,6 +30,7 @@

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
//Synthetic comment -- @@ -73,9 +74,15 @@
* @return True if the {@link XmlEditor} can handle that file.
*/
public static boolean canHandleFile(IFile file) {
// we need the target of the file's project to access the descriptors.
IProject project = file.getProject();
IAndroidTarget target = Sdk.getCurrent().getTarget(project);
if (target != null) {
// Note: the target data can be null when an SDK is not finished loading yet.
// We can potentially arrive here when Eclipse is started with a file previously
//Synthetic comment -- @@ -85,9 +92,16 @@
FirstElementParser.Result result = FirstElementParser.parse(
file.getLocation().toOSString(),
SdkConstants.NS_RESOURCES);

if (result != null && data != null) {
String name = result.getElement();
if (name != null && result.getXmlnsPrefix() != null) {
DocumentDescriptor desc = data.getXmlDescriptors().getDescriptor();
for (ElementDescriptor elem : desc.getChildren()) {
//Synthetic comment -- @@ -100,6 +114,10 @@
}
}

return false;
}








