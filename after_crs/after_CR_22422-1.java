/*Fix menu editor such that it works for empty files

Some users have reported the IDE hanging when they create menu
resource files.  This happens if they create a "plain" file and name
it XML, rather than using the "New Android XML File" or "New XML File"
templates.  The reason this happens is that the initialization code in
the MenuEditor ends up with unbounded recursion where the
initialization code triggers itself.

Change-Id:Icfbc295bbf5cecac216fdff5144cbdb62211da6a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index e72cb41..39a132a 100644

//Synthetic comment -- @@ -1153,7 +1153,6 @@
*         not be computed.
*/
public static String getIndent(IStructuredDocument document, Node xmlNode) {
if (xmlNode instanceof IndexedRegion) {
IndexedRegion region = (IndexedRegion)xmlNode;
int startOffset = region.getStartOffset();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java
//Synthetic comment -- index a06d7be..911a833 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.menu;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor.Mandatory;
//Synthetic comment -- @@ -104,6 +104,8 @@
}
}

    private boolean mUpdatingModel;

/**
* Processes the new XML Model, which XML root node is given.
*
//Synthetic comment -- @@ -111,33 +113,43 @@
*/
@Override
protected void xmlModelChanged(Document xml_doc) {
        if (mUpdatingModel) {
            return;
}

        try {
            mUpdatingModel = true;

            // init the ui root on demand
            initUiRootNode(false /*force*/);

            mUiRootNode.setXmlDocument(xml_doc);
            if (xml_doc != null) {
                ElementDescriptor root_desc = mUiRootNode.getDescriptor();
                try {
                    XPath xpath = AndroidXPathFactory.newXPath();
                    Node node = (Node) xpath.evaluate("/" + root_desc.getXmlName(),  //$NON-NLS-1$
                            xml_doc,
                            XPathConstants.NODE);
                    if (node == null && root_desc.getMandatory() != Mandatory.NOT_MANDATORY) {
                        // Create the root element if it doesn't exist yet (for empty new documents)
                        node = mUiRootNode.createXmlNode();
                    }

                    // Refresh the manifest UI node and all its descendants
                    mUiRootNode.loadFromXmlNode(node);

                    // TODO ? startMonitoringMarkers();
                } catch (XPathExpressionException e) {
                    AdtPlugin.log(e, "XPath error when trying to find '%s' element in XML.", //$NON-NLS-1$
                            root_desc.getXmlName());
                }
            }

            super.xmlModelChanged(xml_doc);
        } finally {
            mUpdatingModel = false;
        }
}

/**







