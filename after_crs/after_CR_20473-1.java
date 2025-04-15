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

    public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
public static final String ATTR_LAYOUT_WIDTH = "layout_width";      //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 7137e0a..93b7d51 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BOTTOM;
//Synthetic comment -- @@ -92,7 +93,7 @@
}

private void addAttr(String propertyName, INode childNode, List<String> infos) {
        String a = childNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_PREFIX + propertyName);
if (a != null && a.length() > 0) {
String s = propertyName + ": " + a;
infos.add(s);
//Synthetic comment -- @@ -640,7 +641,7 @@

for (String it : data.getCurr().getAttr()) {
newChild.setAttribute(ANDROID_URI,
                             ATTR_LAYOUT_PREFIX + it, id != null ? id : "true"); //$NON-NLS-1$
}

addInnerElements(newChild, element, idMap);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExtractIncludeAction.java
//Synthetic comment -- index 83a6267..da86943 100644

//Synthetic comment -- @@ -20,12 +20,12 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS_COLON;
//Synthetic comment -- @@ -43,9 +43,12 @@
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.ResourceNameValidator;
import com.android.sdklib.util.Pair;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -59,6 +62,10 @@
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
//Synthetic comment -- @@ -67,6 +74,8 @@
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -218,7 +227,9 @@

String newFileName = newName + DOT_XML;
IProject project = mCanvas.getLayoutEditor().getProject();
        IContainer parent = mCanvas.getLayoutEditor().getInputFile().getParent();
        IPath parentPath = parent.getProjectRelativePath();
        IFile file = project.getFile(new Path(parentPath + WS_SEP + newFileName));

writeFile(file, sb.toString());

//Synthetic comment -- @@ -226,13 +237,114 @@
LayoutEditor editor = mCanvas.getLayoutEditor();
editor.getGraphicalEditor().refreshProjectResources();

        String referenceId = getReferenceId();
        List<Edit> edits = new ArrayList<Edit>();

        // Replace existing elements in the source file and insert <include>
        String include = computeIncludeString(newName, androidNsPrefix, referenceId);
        edits.add(new Edit(start, end, include));

        // Update any layout references to the old id with the new id
        if (referenceId != null) {
            String rootId = getRootId();
            IStructuredModel model = editor.getModelForRead();
            try {
                IStructuredDocument doc = model.getStructuredDocument();
                if (doc != null) {
                    List<Edit> replaceIds = replaceIds(doc, start, end, rootId, referenceId);
                    edits.addAll(replaceIds);
                }
            } finally {
                model.releaseFromRead();
            }
        }

// Open extracted file. This seems to trigger refreshing of ProjectResources
// such that the @layout/<newName> reference from the new <include> we're adding
// will work; without this we get file reference errors
openFile(file);

        // Perform edits
        applyEdits("Extract As Include", edits);
    }

    /** Produce a list of edits to replace references to the given id with the given new id */
    private List<Edit> replaceIds(IStructuredDocument doc, int skipStart, int skipEnd,
            String rootId, String referenceId) {

        // We need to search for either @+id/ or @id/
        String match1 = rootId;
        String match2;
        if (match1.startsWith(ID_PREFIX)) {
            match2 = '"' + NEW_ID_PREFIX + match1.substring(ID_PREFIX.length()) + '"';
            match1 = '"' + match1 + '"';
        } else if (match1.startsWith(NEW_ID_PREFIX)) {
            match2 = '"' + ID_PREFIX + match1.substring(NEW_ID_PREFIX.length()) + '"';
            match1 = '"' + match1 + '"';
        } else {
            return Collections.emptyList();
        }

        String namePrefix = ANDROID_NS_PREFIX + ':' + ATTR_LAYOUT_PREFIX;
        List<Edit> edits = new ArrayList<Edit>();

        IStructuredDocumentRegion region = doc.getFirstStructuredDocumentRegion();
        for (; region != null; region = region.getNext()) {
            ITextRegionList list = region.getRegions();
            int regionStart = region.getStart();

            // Look at all attribute values and look for an id reference match
            String attributeName = ""; //$NON-NLS-1$
            for (int j = 0; j < region.getNumberOfRegions(); j++) {
                ITextRegion subRegion = list.get(j);
                String type = subRegion.getType();
                if (DOMRegionContext.XML_TAG_ATTRIBUTE_NAME.equals(type)) {
                    attributeName = region.getText(subRegion);
                } else if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(type)) {
                    // Only replace references in layout attributes
                    if (!attributeName.startsWith(namePrefix)) {
                        continue;
                    }
                    // Skip occurrences in the given skip range
                    int subRegionStart = regionStart + subRegion.getStart();
                    if (subRegionStart >= skipStart && subRegionStart <= skipEnd) {
                        continue;
                    }

                    String attributeValue = region.getText(subRegion);
                    if (attributeValue.equals(match1) || attributeValue.equals(match2)) {
                        int start = subRegionStart + 1; // skip quote
                        int end = start + rootId.length();
                        edits.add(new Edit(start, end, referenceId));
                    }
                }
            }
        }

        return edits;
    }

    /** Returns the id to be used for the include tag itself (may be null) */
    private String getReferenceId() {
        String rootId = getRootId();
        if (rootId != null) {
            return rootId + "_ref";
        }

        return null;
    }

    /** Get the id of the root selected element, if any */
    private String getRootId() {
        Element primaryNode = getPrimaryNode();
        if (primaryNode != null) {
            String oldId = primaryNode.getAttributeNS(ANDROID_URI, ATTR_ID);
            if (oldId.length() > 0) {
                return oldId;
            }
        }

        return null;
}

private boolean writeFile(IFile file, String content) {
//Synthetic comment -- @@ -354,36 +466,35 @@
}
}

/**
* Compute the actual {@code <include>} string to be inserted in place of the old
* selection
*/
    private String computeIncludeString(String newName, String androidNsPrefix,
            String referenceId) {
StringBuilder sb = new StringBuilder();
sb.append("<include layout=\"@layout/"); //$NON-NLS-1$
sb.append(newName);
sb.append('"');

        // Create new id for the include itself
        if (referenceId != null) {
            sb.append(androidNsPrefix);
            sb.append(':');
            sb.append(ATTR_ID);
            sb.append('=').append('"');
            sb.append(referenceId);
            sb.append('"').append(' ');
        }

        // Add id string, unless it's a <merge>, since we may need to adjust any layout
        // references to apply to the <include> tag instead
        // TODO: Use refactoring infrastructure to handle this part

        // I should move all the layout_ attributes as well
        // I also need to duplicate and modify the id and then replace
        // everything else in the file with this new id...

// HACK: see issue 13494: We must duplicate the width/height attributes on the
// <include> statement for designtime rendering only
Element primaryNode = getPrimaryNode();
//Synthetic comment -- @@ -411,7 +522,7 @@
sb.append(':');
sb.append(ATTR_LAYOUT_WIDTH);
sb.append('=').append('"');
            sb.append(DescriptorsUtils.toXmlAttributeValue(width));
sb.append('"');
}
if (height != null) {
//Synthetic comment -- @@ -420,10 +531,34 @@
sb.append(':');
sb.append(ATTR_LAYOUT_HEIGHT);
sb.append('=').append('"');
            sb.append(DescriptorsUtils.toXmlAttributeValue(height));
sb.append('"');
}

        // Duplicate all the other layout attributes as well
        if (primaryNode != null) {
            NamedNodeMap attributes = primaryNode.getAttributes();
            for (int i = 0, n = attributes.getLength(); i < n; i++) {
                Node attr = attributes.item(i);
                String name = attr.getLocalName();
                if (name.startsWith(ATTR_LAYOUT_PREFIX)
                        && ANDROID_URI.equals(attr.getNamespaceURI())) {
                    if (name.equals(ATTR_LAYOUT_WIDTH) || name.equals(ATTR_LAYOUT_HEIGHT)) {
                        // Already handled
                        continue;
                    }

                    sb.append(' ');
                    sb.append(androidNsPrefix);
                    sb.append(':');
                    sb.append(name);
                    sb.append('=').append('"');
                    sb.append(DescriptorsUtils.toXmlAttributeValue(attr.getNodeValue()));
                    sb.append('"');
                }
            }
        }

sb.append("/>");
return sb.toString();
}
//Synthetic comment -- @@ -436,10 +571,11 @@
try {
IStructuredDocument document = editor.getStructuredDocument();
String xml = document.get(start, end - start);
            Element primaryNode = getPrimaryNode();
            xml = stripTopLayoutAttributes(primaryNode, start, xml);
xml = dedent(xml);

// Wrap siblings in <merge>?
if (primaryNode == null) {
StringBuilder sb = new StringBuilder();
sb.append("<merge>\n"); //$NON-NLS-1$
//Synthetic comment -- @@ -461,6 +597,65 @@
}
}

    /** Remove sections of the document that correspond to top level layout attributes;
     * these are placed on the include element instead */
    private String stripTopLayoutAttributes(Element primaryNode, int start, String xml) {
        if (primaryNode != null) {
            // List of attributes to remove
            //IndexedRegion attRegion = (IndexedRegion) attrs.item(i);
            List<IndexedRegion> skip = new ArrayList<IndexedRegion>();
            NamedNodeMap attributes = primaryNode.getAttributes();
            for (int i = 0, n = attributes.getLength(); i < n; i++) {
                Node attr = attributes.item(i);
                String name = attr.getLocalName();
                if (name.startsWith(ATTR_LAYOUT_PREFIX)
                        && ANDROID_URI.equals(attr.getNamespaceURI())) {
                    if (name.equals(ATTR_LAYOUT_WIDTH) || name.equals(ATTR_LAYOUT_HEIGHT)) {
                        // These are special and are left in
                        continue;
                    }

                    if (attr instanceof IndexedRegion) {
                        skip.add((IndexedRegion) attr);
                    }
                }
            }
            if (skip.size() > 0) {
                Collections.sort(skip, new Comparator<IndexedRegion>() {
                    // Sort in start order
                    public int compare(IndexedRegion r1, IndexedRegion r2) {
                        return r1.getStartOffset() - r2.getStartOffset();
                    }
                });

                // Successively cut out the various layout attributes
                // TODO remove adjacent whitespace too (but not newlines, unless they
                // are newly adjacent)
                StringBuilder sb = new StringBuilder(xml.length());
                int nextStart = 0;

                // Copy out all the sections except the skip sections
                for (IndexedRegion r : skip) {
                    int regionStart = r.getStartOffset();
                    // Adjust to string offsets since we've copied the string out of
                    // the document
                    regionStart -= start;

                    sb.append(xml.substring(nextStart, regionStart));

                    nextStart = regionStart + r.getLength();
                }
                if (nextStart < xml.length()) {
                    sb.append(xml.substring(nextStart));
                }

                return sb.toString();
            }
        }

        return xml;
    }

private static String getIndent(String line, int max) {
int i = 0;
int n = Math.min(max, line.length());
//Synthetic comment -- @@ -571,4 +766,63 @@

return Pair.of(start, end);
}

    /** Apply edits into document under an undo lock */
    private void applyEdits(String label, final List<Edit> edits) {
        // Process the edits in reverse document position order to ensure
        // that the offsets aren't affected by other edits
        Collections.sort(edits);
        final LayoutEditor editor = mCanvas.getLayoutEditor();
        editor.wrapUndoEditXmlModel(label, new Runnable() {
            public void run() {
                IStructuredDocument document = editor.getStructuredDocument();
                if (document != null) {
                    try {
                        for (Edit edit : edits) {
                            edit.apply(document);
                        }
                    } catch (BadLocationException e) {
                        AdtPlugin.log(e, "Cannot insert <include> tag");
                        return;
                    }
                }
            }
        });
    }

    /**
     * Edit operation (insert, delete, replace) at a given offset - a collection of these
     * can be sorted in reverse offset order such that they can be applied successively
     * without having to updated offsets
     * <p>
     * TODO: When rewriting this extract operation to the refactoring framework, use
     * refactoring framework's change list instead
     */
    private static class Edit implements Comparable<Edit> {
        private final int mStart;
        private final int mEnd;
        private final String mReplaceWith;

        public Edit(int start, int end, String replaceWith) {
            super();
            mStart = start;
            mEnd = end;
            mReplaceWith = replaceWith;
        }

        void apply(IStructuredDocument document) throws BadLocationException {
            document.replace(mStart, mEnd - mStart, mReplaceWith);
        }

        public int compareTo(Edit o) {
            // Sort in *reverse* offset order
            return o.mStart - mStart;
        }

        @Override
        public String toString() {
            return "Edit [start=" + mStart + ", end=" + mEnd + ", replaceWith=" + mReplaceWith
                    + "]";
        }
    }
}







