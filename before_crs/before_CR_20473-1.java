/*Fix Extract as Include refactoring for layout attributes

This changeset fixes several issues with the Extract As Include
operation:

1) Transfer all the layout_ attributes to the include
   itself. Generally the layout attributes are particular to the
   inclusion context, they are not shared among the different uses of
   the include. For example, in layout1, the include may be in a
   linear layout and have a layout weight, and in another layout the
   included may need relative layout attachments.

2) Generate a new id for the included tag itself

3) For any layout references to the old extracted id, use the include
   tag id instead

4) Generate the new layout in the same folder as the source of the
   extract operation, since it may depend on properties only defined
   there

Change-Id:I515a56fe07cc0ffc1b4fcb6eec8d3a10d383915e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index dd0af54..e8f9261 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_ID = "id";                          //$NON-NLS-1$

public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
public static final String ATTR_LAYOUT_WIDTH = "layout_width";      //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 7137e0a..93b7d51 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BOTTOM;
//Synthetic comment -- @@ -92,7 +93,7 @@
}

private void addAttr(String propertyName, INode childNode, List<String> infos) {
        String a = childNode.getStringAttr(ANDROID_URI, "layout_" + propertyName); //$NON-NLS-1$
if (a != null && a.length() > 0) {
String s = propertyName + ": " + a;
infos.add(s);
//Synthetic comment -- @@ -640,7 +641,7 @@

for (String it : data.getCurr().getAttr()) {
newChild.setAttribute(ANDROID_URI,
                             "layout_" + it, id != null ? id : "true"); //$NON-NLS-1$ //$NON-NLS-2$
}

addInnerElements(newChild, element, idMap);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java
//Synthetic comment -- index 83a6267..da86943 100644

//Synthetic comment -- @@ -20,12 +20,12 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS_COLON;
//Synthetic comment -- @@ -43,9 +43,12 @@
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.ResourceNameValidator;
import com.android.sdklib.util.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -59,6 +62,10 @@
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
//Synthetic comment -- @@ -67,6 +74,8 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -218,7 +227,9 @@

String newFileName = newName + DOT_XML;
IProject project = mCanvas.getLayoutEditor().getProject();
        IFile file = project.getFile(WS_LAYOUTS + WS_SEP + newFileName);

writeFile(file, sb.toString());

//Synthetic comment -- @@ -226,13 +237,114 @@
LayoutEditor editor = mCanvas.getLayoutEditor();
editor.getGraphicalEditor().refreshProjectResources();

// Open extracted file. This seems to trigger refreshing of ProjectResources
// such that the @layout/<newName> reference from the new <include> we're adding
// will work; without this we get file reference errors
openFile(file);

        // Replace existing elements in the source file and insert <include>
        replaceWithInclude(newName, start, end, androidNsPrefix);
}

private boolean writeFile(IFile file, String content) {
//Synthetic comment -- @@ -354,36 +466,35 @@
}
}

    /** Replace existing elements in the source file and insert {@code <include>} */
    private void replaceWithInclude(final String newName, final int start, final int end,
            final String androidNsPrefix) {
        final LayoutEditor editor = mCanvas.getLayoutEditor();
        editor.wrapUndoEditXmlModel("Extract As Include", new Runnable() {
            public void run() {
                IStructuredDocument document = editor.getStructuredDocument();
                if (document != null) {
                    String include = computeIncludeString(newName, androidNsPrefix);
                    try {
                        document.replace(start, end - start, include);
                    } catch (BadLocationException e) {
                        AdtPlugin.log(e, "Cannot insert <include> tag");
                        return;
                    }
                }
            }
        });
    }

/**
* Compute the actual {@code <include>} string to be inserted in place of the old
* selection
*/
    private String computeIncludeString(String newName, String androidNsPrefix) {
StringBuilder sb = new StringBuilder();
sb.append("<include layout=\"@layout/"); //$NON-NLS-1$
sb.append(newName);
sb.append('"');

// HACK: see issue 13494: We must duplicate the width/height attributes on the
// <include> statement for designtime rendering only
Element primaryNode = getPrimaryNode();
//Synthetic comment -- @@ -411,7 +522,7 @@
sb.append(':');
sb.append(ATTR_LAYOUT_WIDTH);
sb.append('=').append('"');
            sb.append(width);
sb.append('"');
}
if (height != null) {
//Synthetic comment -- @@ -420,10 +531,34 @@
sb.append(':');
sb.append(ATTR_LAYOUT_HEIGHT);
sb.append('=').append('"');
            sb.append(height);
sb.append('"');
}

sb.append("/>");
return sb.toString();
}
//Synthetic comment -- @@ -436,10 +571,11 @@
try {
IStructuredDocument document = editor.getStructuredDocument();
String xml = document.get(start, end - start);
xml = dedent(xml);

// Wrap siblings in <merge>?
            Element primaryNode = getPrimaryNode();
if (primaryNode == null) {
StringBuilder sb = new StringBuilder();
sb.append("<merge>\n"); //$NON-NLS-1$
//Synthetic comment -- @@ -461,6 +597,65 @@
}
}

private static String getIndent(String line, int max) {
int i = 0;
int n = Math.min(max, line.length());
//Synthetic comment -- @@ -571,4 +766,63 @@

return Pair.of(start, end);
}
}







