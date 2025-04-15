/*Warp to source editor on widget double click

Update mouse handler such that a double click will look up the
corresponding XML element, front the XML source editor and select the
text range (scrolling if necessary) to reveal the corresponding tag.

Change-Id:Iaa3d6f845c3fea190c304a07fab07314baa3f638*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 18f1e28..a1b9d13 100644

//Synthetic comment -- @@ -62,10 +62,12 @@
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.net.MalformedURLException;
import java.net.URL;
//Synthetic comment -- @@ -870,6 +872,38 @@
return null;
}

    /**
     * Shows the editor range corresponding to the given XML node. This will
     * front the editor and select the text range.
     *
     * @param xmlNode The DOM node to be shown. The DOM node should be an XML
     *            node from the existing XML model used by the structured XML
     *            editor; it will not do attribute matching to find a
     *            "corresponding" element in the document from some foreign DOM
     *            tree.
     * @return True if the node was shown.
     */
    @SuppressWarnings("restriction") // Yes, this method relies a lot on restricted APIs
    public boolean show(Node xmlNode) {
        if (xmlNode instanceof IndexedRegion) {
            IndexedRegion region = (IndexedRegion)xmlNode;

            IEditorPart textPage = getEditor(mTextPageIndex);
            if (textPage instanceof StructuredTextEditor) {
                StructuredTextEditor editor = (StructuredTextEditor) textPage;

                setActivePage(AndroidXmlEditor.TEXT_EDITOR_ID);

                // Note - we cannot use region.getLength() because that seems to
                // always return 0.
                int regionLength = region.getEndOffset() - region.getStartOffset();
                editor.selectAndReveal(region.getStartOffset(), regionLength);
                return true;
            }
        }

        return false;
    }

/**
* Listen to changes in the underlying XML model in the structured editor.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 2781d1a..f4dce0f 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.w3c.dom.Node;

import java.util.ArrayList;

//Synthetic comment -- @@ -242,4 +243,19 @@
((IPropertySource) uiView).setPropertyValue(id, value);
}
}

    /**
     * Returns the XML node corresponding to this info, or null if there is no
     * such XML node.
     *
     * @return The XML node corresponding to this info object, or null
     */
    public Node getXmlNode() {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            return uiView.getXmlNode();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 44a2e74..29094b7 100755

//Synthetic comment -- @@ -1328,7 +1328,22 @@
}

private void onDoubleClick(MouseEvent e) {
        // Warp to the text editor and show the corresponding XML for the
        // double-clicked widget
        int x = mHScale.inverseTranslate(e.x);
        int y = mVScale.inverseTranslate(e.y);
        CanvasViewInfo vi = findViewInfoAt(x, y);
        if (vi == null) {
            return;
        }

        Node xmlNode = vi.getXmlNode();
        if (xmlNode != null) {
            boolean found = mLayoutEditor.show(xmlNode);
            if (!found) {
                getDisplay().beep();
            }
        }
}

/**







