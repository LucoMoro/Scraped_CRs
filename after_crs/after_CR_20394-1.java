/*Cleanup warnings in the form-based UI editor.

Change-Id:Ide9952dfefeb86e59d4ed5b1db25150625a54143*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/CopyCutAction.java
//Synthetic comment -- index 2261fe0..3fe98bb 100644

//Synthetic comment -- @@ -46,6 +46,7 @@
/**
* Provides Cut and Copy actions for the tree nodes.
*/
@SuppressWarnings({"restriction", "deprecation"})
public class CopyCutAction extends Action {
private List<UiElementNode> mUiNodes;
private boolean mPerformCut;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/PasteAction.java
//Synthetic comment -- index 9cd7412..ea41e3e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
/**
* Provides Paste operation for the tree nodes
*/
@SuppressWarnings("restriction")
public class PasteAction extends Action {
private UiElementNode mUiNode;
private final AndroidXmlEditor mEditor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java
//Synthetic comment -- index c1e2d0b..c5fa670 100644

//Synthetic comment -- @@ -246,6 +246,8 @@
});
}

        assert last_node != null; // tell Eclipse this can't be null below

if (select_xml_node[0] == null) {
// The XML node has not been moved, we can just select the same UI node
selectUiNode(last_node);
//Synthetic comment -- @@ -328,6 +330,8 @@
});
}

        assert last_node != null; // tell Eclipse this can't be null below

if (select_xml_node[0] == null) {
// The XML node has not been moved, we can just select the same UI node
selectUiNode(last_node);
//Synthetic comment -- @@ -400,7 +404,7 @@
rootNode.getEditor().wrapEditXmlModel(new Runnable() {
public void run() {
DescriptorsUtils.setDefaultLayoutAttributes(uiNew, updateLayout);
                uiNew.createXmlNode();
}
});
return uiNew;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiElementDetail.java
//Synthetic comment -- index b0cd84a..593d657 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
//Synthetic comment -- @@ -50,7 +49,6 @@
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import java.util.Collection;
import java.util.HashSet;
//Synthetic comment -- @@ -284,8 +282,7 @@

// Fallback to a pure text tooltip, no fancy HTML
tooltip = DescriptorsUtils.formatTooltip(elem_desc.getTooltip());
                    SectionHelper.createLabel(masterTable, toolkit, tooltip, tooltip);
}
}








