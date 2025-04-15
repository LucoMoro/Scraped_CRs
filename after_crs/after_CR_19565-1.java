/*Reenable Show Included In, and handle target compatibility

Hook up the Show Included In menu item to the layout library's
Capability.EMBEDDED_LAYOUT which tracks whether nested parsers are
handled by the layout library. Also handle this properly in the some
related scenarios: If you switch rendering targets on a page that is
already in an included context, remove that included context (since
otherwise you'll be editing the including file rather than the
included file_), and when you jump from an including file to an
included file, only set inclusion context if the target file is either
not open or its rendering target supports inclusion.

Also fix a bug in the initialization of capabilities; it was passing
the project target rather than the rendering target back from the
onXmlLoaded() method, which meant the clipping support flag was not
set correctly.

Change-Id:I2c1ae55d44fc627c56ecbcb76a7f163e8094f975*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 02ef055..9a7f810 100644

//Synthetic comment -- @@ -180,6 +180,13 @@
*/
void onRenderingTargetPreChange(IAndroidTarget oldTarget);

        /**
         * Called after the rendering target changes.
         *
         * @param target the new rendering target
         */
        void onRenderingTargetPostChange(IAndroidTarget target);

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
ProjectResources getFrameworkResources(IAndroidTarget target);
//Synthetic comment -- @@ -840,6 +847,8 @@
mDockCombo.select(DockMode.getIndex(mState.dock));
mNightCombo.select(NightMode.getIndex(mState.night));
mTargetCombo.select(mTargetList.indexOf(mState.target));

                        targetData = Sdk.getCurrent().getTargetData(mState.target);
} else {
findAndSetCompatibleConfig(false /*favorCurrentConfig*/);

//Synthetic comment -- @@ -1808,6 +1817,10 @@
// change could have impacted it.
saveState();

        if (mListener != null && mRenderingTarget != null) {
            mListener.onRenderingTargetPostChange(mRenderingTarget);
        }

if (computeOk &&  mListener != null) {
mListener.onConfigurationChange();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 3f58df1..79060d1 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.layoutlib.api.Capability;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -207,9 +208,8 @@
}
}

        // Showing includes is not supported for all the targeted platforms
        if (mEditor.getGraphicalEditor().renderingSupports(Capability.EMBEDDED_LAYOUT)) {
insertShowIncludedMenu(endId);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 914b33e..cf72e16 100644

//Synthetic comment -- @@ -88,7 +88,6 @@
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -624,7 +623,7 @@
public void onCreate() {
LayoutCreatorDialog dialog = new LayoutCreatorDialog(mConfigComposite.getShell(),
mEditedFile.getName(), mConfigComposite.getCurrentConfig());
            if (dialog.open() == Window.OK) {
final FolderConfiguration config = new FolderConfiguration();
dialog.getConfiguration(config);

//Synthetic comment -- @@ -636,6 +635,11 @@
preRenderingTargetChangeCleanUp(oldTarget);
}

        public void onRenderingTargetPostChange(IAndroidTarget target) {
            AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
            updateCapabilities(targetData);
        }

public Map<String, Map<String, ResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
ProjectResources frameworkRes = getFrameworkResources();
//Synthetic comment -- @@ -1034,12 +1038,21 @@
*/
public void onTargetChange() {
AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
        updateCapabilities(targetData);

        mConfigListener.onConfigurationChange();
    }

    /** Updates the capabilities for the given target data (which may be null) */
    private void updateCapabilities(AndroidTargetData targetData) {
if (targetData != null) {
LayoutLibrary layoutLib = targetData.getLayoutLibrary();
setClippingSupport(layoutLib.supports(Capability.UNBOUND_RENDERING));

            if (mIncludedWithin != null &&  !layoutLib.supports(Capability.EMBEDDED_LAYOUT)) {
                showIn(null);
            }
        }
}

public LayoutEditor getLayoutEditor() {
//Synthetic comment -- @@ -1351,6 +1364,23 @@
return null;
}

    /**
     * Returns whether the current rendering target supports the given capability
     *
     * @param capability the capability to be looked up
     * @return true if the current rendering target supports the given capability
     */
    public boolean renderingSupports(Capability capability) {
        IAndroidTarget target = getRenderingTarget();
        if (target != null) {
            AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
            LayoutLibrary layoutLib = targetData.getLayoutLibrary();
            return layoutLib.supports(capability);
        }

        return false;
    }

private boolean ensureModelValid(UiDocumentNode model) {
// check there is actually a model (maybe the file is empty).
if (model.getUiChildren().size() == 0) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 74f358e..cd411d2 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.layoutlib.api.Capability;
import com.android.layoutlib.api.LayoutScene;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -744,31 +745,38 @@
IResource xmlFile = workspace.findMember(relativePath);
if (xmlFile != null) {
IFile leavingFile = graphicalEditor.getEditedFile();
Reference next = Reference.create(graphicalEditor.getEditedFile());

try {
IEditorPart openAlready = EditorUtility.isOpenInEditor(xmlFile);

                    // Show the included file as included within this click source?
if (openAlready != null) {
if (openAlready instanceof LayoutEditor) {
LayoutEditor editor = (LayoutEditor)openAlready;
GraphicalEditorPart gEditor = editor.getGraphicalEditor();
                            if (gEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                                gEditor.showIn(next);
                            }
}
} else {
try {
                            // Set initial state of a new file
                            // TODO: Only set rendering target portion of the state
                            QualifiedName qname = ConfigurationComposite.NAME_CONFIG_STATE;
                            String state = leavingFile.getPersistentProperty(qname);
                            xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INITIAL_STATE, state);
} catch (CoreException e) {
                            // pass
                        }

                        if (graphicalEditor.renderingSupports(Capability.EMBEDDED_LAYOUT)) {
                            try {
                                xmlFile.setSessionProperty(GraphicalEditorPart.NAME_INCLUDE, next);
                            } catch (CoreException e) {
                                // pass - worst that can happen is that we don't
                                //start with inclusion
                            }
}
}








