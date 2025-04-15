/*Synchronize window docking state between windows

The new layout editor window docking framework, used for the palette
and structure views to allow views to be reorganized and minimized,
allows the user to customize (persistently) the window configuration
in the layout editor.  However, even though the state is persisted,
this is a single global state, not a per-window state.

This changeset makes the editors automatically synchronize to the
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
//Synthetic comment -- @@ -144,7 +145,6 @@
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
//Synthetic comment -- @@ -212,6 +212,12 @@
*   which all listen to each others indirectly.
*/

    /** Property key for the window preferences for the structure flyout */
    private static final String PREF_STRUCTURE = "design.structure";     //$NON-NLS-1$

    /** Property key for the window preferences for the palette flyout */
    private static final String PREF_PALETTE = "design.palette";         //$NON-NLS-1$

/**
* Session-property on files which specifies the initial config state to be used on
* this file
//Synthetic comment -- @@ -354,9 +360,9 @@
}
}

        IPreferenceStore preferenceStore = AdtPlugin.getDefault().getPreferenceStore();
        PluginFlyoutPreferences preferences;
        preferences = new PluginFlyoutPreferences(preferenceStore, PREF_PALETTE);
preferences.initializeDefaults(DOCK_WEST, STATE_OPEN, 200);
mPaletteComposite = new FlyoutControlComposite(parent, SWT.NONE, preferences);
mPaletteComposite.setTitleText("Palette");
//Synthetic comment -- @@ -376,8 +382,7 @@
decor.createToolbarItems(paletteComposite.getToolBar());

// Create the shared structure+editor area
        preferences = new PluginFlyoutPreferences(preferenceStore, PREF_STRUCTURE);
preferences.initializeDefaults(DOCK_EAST, STATE_OPEN, 300);
mStructureFlyout = new FlyoutControlComposite(editorParent, SWT.NONE, preferences);
mStructureFlyout.setTitleText("Structure");
//Synthetic comment -- @@ -1146,6 +1151,7 @@
if (!mActive) {
mActive = true;

            syncDockingState();
mActionBar.updateErrorIndicator();

boolean changed = mConfigComposite.syncRenderState();
//Synthetic comment -- @@ -1161,6 +1167,53 @@
}

/**
     * The global docking state version. This number is incremented each time
     * the user customizes the window layout in any layout.
     */
    private static int sDockingStateVersion;

    /**
     * The window docking state version that this window is currently showing;
     * when a different window is reconfigured, the global version number is
     * incremented, and when this window is shown, and the current version is
     * less than the global version, the window layout will be synced.
     */
    private int mDockingStateVersion;

    /**
     * Syncs the window docking state.
     * <p>
     * The layout editor lets you change the docking state -- e.g. you can minimize the
     * palette, and drag the structure view to the bottom, and so on. When you restart
     * the IDE, the window comes back up with your customized state.
     * <p>
     * <b>However</b>, when you have multiple editor files open, if you minimize the palette
     * in one editor and then switch to another, the other editor will have the old window
     * state. That's because each editor has its own set of windows.
     * <p>
     * This method fixes this. Whenever a window is shown, this method is called, and the
     * docking state is synced such that the editor will match the current persistent docking
     * state.
     */
    private void syncDockingState() {
        if (mDockingStateVersion == sDockingStateVersion) {
            // No changes to apply
            return;
        }
        mDockingStateVersion = sDockingStateVersion;

        IPreferenceStore preferenceStore = AdtPlugin.getDefault().getPreferenceStore();
        PluginFlyoutPreferences preferences;
        preferences = new PluginFlyoutPreferences(preferenceStore, PREF_PALETTE);
        mPaletteComposite.apply(preferences);
        preferences = new PluginFlyoutPreferences(preferenceStore, PREF_STRUCTURE);
        mStructureFlyout.apply(preferences);
        mPaletteComposite.layout();
        mStructureFlyout.layout();
        mPaletteComposite.redraw(); // the structure view is nested within the palette
    }

    /**
* Responds to a page change that made the Graphical editor page the deactivated page
*/
public void deactivated() {
//Synthetic comment -- @@ -2754,6 +2807,8 @@
mPaletteComposite.dismissHover();
}

    // ---- Implements IFlyoutListener ----

@Override
public void stateChanged(int oldState, int newState) {
// Auto zoom the surface if you open or close flyout windows such as the palette
//Synthetic comment -- @@ -2761,5 +2816,8 @@
if (newState == STATE_OPEN || newState == STATE_COLLAPSED && oldState == STATE_OPEN) {
getCanvasControl().setFitScale(true /*onlyZoomOut*/);
}

        sDockingStateVersion++;
        mDockingStateVersion = sDockingStateVersion;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleXmlTransfer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleXmlTransfer.java
//Synthetic comment -- index 11f3ea2..20ac203 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import java.io.UnsupportedEncodingException;







