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
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URL;
//Synthetic comment -- @@ -870,6 +872,38 @@
return null;
}


/**
* Listen to changes in the underlying XML model in the structured editor.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 2781d1a..f4dce0f 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;

import java.util.ArrayList;

//Synthetic comment -- @@ -242,4 +243,19 @@
((IPropertySource) uiView).setPropertyValue(id, value);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 44a2e74..29094b7 100755

//Synthetic comment -- @@ -1328,7 +1328,22 @@
}

private void onDoubleClick(MouseEvent e) {
        // pass, not used yet.
}

/**







