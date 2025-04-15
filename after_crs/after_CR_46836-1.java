/*40368: Support a root vertical LinearLayout in graphical editor

Change-Id:I7b156f1f8b8f939f9f940962e50c34595b9fa3b7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index fcfdfd1..906a97a 100644

//Synthetic comment -- @@ -1058,7 +1058,12 @@
end = begin + 1;
}

                                    if (mFormatChildren
                                         && node == node.getOwnerDocument().getDocumentElement()) {
                                        reformatDocument();
                                    } else {
                                        reformatRegion(begin, end);
                                    }
}
}
mFormatNode = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index ae2737b..2634569 100644

//Synthetic comment -- @@ -304,7 +304,7 @@
* In case of success, the new element will have some default attributes set (xmlns:android,
* layout_width and height). The edit is wrapped in a proper undo.
* <p/>
     * Implementation is similar to {@link #createDocumentRoot} except we also
* copy all the attributes and inner elements recursively.
*/
private void pasteInEmptyDocument(final IDragElement pastedElement) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 4b6b803..6f8aef3 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Point;
//Synthetic comment -- @@ -1579,16 +1580,16 @@
* This is invoked by
* {@link MoveGesture#drop(org.eclipse.swt.dnd.DropTargetEvent)}.
*
     * @param root A non-null descriptor of the root element to create.
*/
    void createDocumentRoot(final @NonNull SimpleElement root) {
        String rootFqcn = root.getFqcn();

// Need a valid empty document to create the new root
final UiDocumentNode uiDoc = mEditorDelegate.getUiRootNode();
if (uiDoc == null || uiDoc.getUiChildren().size() > 0) {
            debugPrintf("Failed to create document root for %1$s: document is not empty",
                    rootFqcn);
return;
}

//Synthetic comment -- @@ -1620,6 +1621,16 @@
SdkConstants.NS_RESOURCES,
true /*override*/);

                IDragAttribute[] attributes = root.getAttributes();
                if (attributes != null) {
                    for (IDragAttribute attribute : attributes) {
                        String uri = attribute.getUri();
                        String name = attribute.getName();
                        String value = attribute.getValue();
                        uiNew.setAttributeValue(name, uri, value, false /*override*/);
                    }
                }

// Adjust the attributes
DescriptorsUtils.setDefaultLayoutAttributes(uiNew, false /*updateLayout*/);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 3c3ed54..7cf3a64 100644

//Synthetic comment -- @@ -832,9 +832,7 @@
return;
}

        mCanvas.createDocumentRoot(elements[0]);
}

/**







