/*Synchronize window docking state between windows

The new layout editor window docking framework, used for the palette
and structure views to allow windows to be reorganized and minimized,
allows the user to customize (persistently) the window configuration
in the layout editor.  However, even though the state is persisted,
this is a single global state, not a per-window state.

This changeset makes the editors automatically synchonize to the
global state when they are shown. This means that if you have two
layout editors open, and you minimize the palette in one of them, then
as soon as you switch to the second layout, the palette is minimized
in that editor tab as well.

Change-Id:I863043f654c9f48ecce25f0f2686966c68669272*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 493f996..287a192 100644

//Synthetic comment -- @@ -115,6 +115,7 @@
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
//Synthetic comment -- @@ -144,7 +145,6 @@
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
//Synthetic comment -- @@ -212,6 +212,12 @@
*   which all listen to each others indirectly.
*/

/**
* Session-property on files which specifies the initial config state to be used on
* this file
//Synthetic comment -- @@ -354,9 +360,9 @@
}
}

        PluginFlyoutPreferences preferences =
                new PluginFlyoutPreferences(AdtPlugin.getDefault().getPreferenceStore(),
                        "design.palette"); //$NON-NLS-1$
preferences.initializeDefaults(DOCK_WEST, STATE_OPEN, 200);
mPaletteComposite = new FlyoutControlComposite(parent, SWT.NONE, preferences);
mPaletteComposite.setTitleText("Palette");
//Synthetic comment -- @@ -376,8 +382,7 @@
decor.createToolbarItems(paletteComposite.getToolBar());

// Create the shared structure+editor area
        preferences = new PluginFlyoutPreferences(AdtPlugin.getDefault().getPreferenceStore(),
                "design.structure"); //$NON-NLS-1$
preferences.initializeDefaults(DOCK_EAST, STATE_OPEN, 300);
mStructureFlyout = new FlyoutControlComposite(editorParent, SWT.NONE, preferences);
mStructureFlyout.setTitleText("Structure");
//Synthetic comment -- @@ -1146,6 +1151,7 @@
if (!mActive) {
mActive = true;

mActionBar.updateErrorIndicator();

boolean changed = mConfigComposite.syncRenderState();
//Synthetic comment -- @@ -1161,6 +1167,53 @@
}

/**
* Responds to a page change that made the Graphical editor page the deactivated page
*/
public void deactivated() {
//Synthetic comment -- @@ -2754,6 +2807,8 @@
mPaletteComposite.dismissHover();
}

@Override
public void stateChanged(int oldState, int newState) {
// Auto zoom the surface if you open or close flyout windows such as the palette
//Synthetic comment -- @@ -2761,5 +2816,8 @@
if (newState == STATE_OPEN || newState == STATE_COLLAPSED && oldState == STATE_OPEN) {
getCanvasControl().setFitScale(true /*onlyZoomOut*/);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleXmlTransfer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleXmlTransfer.java
//Synthetic comment -- index 11f3ea2..20ac203 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

import java.io.UnsupportedEncodingException;







