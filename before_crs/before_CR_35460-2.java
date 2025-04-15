/*Fix 29094: Some XML file types open multiple editors

Change-Id:I4981790afecbed04cce5bf434a859bec93e2b61b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonMatchingStrategy.java
//Synthetic comment -- index 72b5100..421ffb0 100755

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
//Synthetic comment -- @@ -46,6 +48,18 @@

LayoutEditorMatchingStrategy m = new LayoutEditorMatchingStrategy();
return m.matches(editorRef, fileInput);
}
}
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorMatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorMatchingStrategy.java
//Synthetic comment -- index 604279f..6a6b99c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceFolderType;

//Synthetic comment -- @@ -43,6 +44,14 @@
IFile iFile = fileInput.getFile();
ResourceFolder resFolder = ResourceManager.getInstance().getResourceFolder(iFile);

// if it's a layout, we now check the name of the fileInput against the name of the
// file being currently edited by the editor since those are independent of the config.
if (resFolder != null && resFolder.getType() == ResourceFolderType.LAYOUT) {







