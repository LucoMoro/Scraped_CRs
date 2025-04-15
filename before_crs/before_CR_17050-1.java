/*Open the debug perspective when showing hprof files.

This prevents the creation of an editor area in the
ddms perspective (the only way to get rid of it later
seems to be to reset the perspective).

Change-Id:I1eb4a3f6a77f27cc462b18b9db43d27cfef09337*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index c2088df..afe04ae 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.ide.eclipse.adt;

import com.android.ddmuilib.StackTracePanel;
import com.android.ddmuilib.StackTracePanel.ISourceRevealer;
import com.android.ddmuilib.console.DdmConsole;
import com.android.ddmuilib.console.IDdmConsole;
import com.android.ide.eclipse.adt.internal.VersionCheck;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index f2033de..bbaded7 100644

//Synthetic comment -- @@ -42,6 +42,8 @@

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -56,8 +58,12 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

//Synthetic comment -- @@ -228,6 +234,24 @@

IFileStore fileStore =  EFS.getLocalFileSystem().getStore(new Path(tempPath));
if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
IDE.openEditorOnFileStore(
getSite().getWorkbenchWindow().getActivePage(),
fileStore);







