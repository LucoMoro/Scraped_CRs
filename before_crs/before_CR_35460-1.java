/*Fix 29094: Some XML file types open multiple editors

Change-Id:I4981790afecbed04cce5bf434a859bec93e2b61b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java
//Synthetic comment -- index 72b5100..4a1d200 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.common;

import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorMatchingStrategy;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -25,6 +26,7 @@
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.part.FileEditorInput;

/**
//Synthetic comment -- @@ -46,6 +48,12 @@

LayoutEditorMatchingStrategy m = new LayoutEditorMatchingStrategy();
return m.matches(editorRef, fileInput);
}
}
return false;







