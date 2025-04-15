/*Refactor to make model rendering reusable

This changeset refactors the code in GraphicalEditorPart a bit to make
the part which renders a model into an image reusable. This will be
used by an upcoming changeset to provide previews of palette items as
you drag them.

The refactoring is straightforward, even though the diffs
unfortunately don't show it. I basically split up the large
recomputeLayout() method into smaller separate chunks (validating the
current file, validating the model and validating the sdk and looking
up the bridge) and added a new render method which can take just a
model (separate from the editor's own model) and render it.

Change-Id:I96603c4beb2d56d36416cd2b6448c87dd306217c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
//Synthetic comment -- index 73d856d..04f11a0 100755

//Synthetic comment -- @@ -17,10 +17,14 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;

/**
* Interface defining what {@link LayoutEditor} expects from a GraphicalLayoutEditor part.
*
//Synthetic comment -- @@ -79,4 +83,20 @@
abstract UiDocumentNode getModel();

abstract LayoutEditor getLayoutEditor();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index b0d5ed9..fa024af 100755

//Synthetic comment -- @@ -342,7 +342,7 @@
mSashPalette = new SashForm(parent, SWT.HORIZONTAL);
mSashPalette.setLayoutData(new GridData(GridData.FILL_BOTH));

        mPalette = new PaletteComposite(mSashPalette);

mSashError = new SashForm(mSashPalette, SWT.VERTICAL | SWT.BORDER);
mSashError.setLayoutData(new GridData(GridData.FILL_BOTH));
//Synthetic comment -- @@ -998,119 +998,50 @@
public void recomputeLayout() {
doXmlReload(false /* force */);
try {
            // check that the resource exists. If the file is opened but the project is closed
            // or deleted for some reason (changed from outside of eclipse), then this will
            // return false;
            if (mEditedFile.exists() == false) {
                displayError("Resource '%1$s' does not exist.",
                             mEditedFile.getFullPath().toString());
return;
}

            IProject iProject = mEditedFile.getProject();

            if (mEditedFile.isSynchronized(IResource.DEPTH_ZERO) == false) {
                String message = String.format("%1$s is out of sync. Please refresh.",
                        mEditedFile.getName());

                displayError(message);

                // also print it in the error console.
                AdtPlugin.printErrorToConsole(iProject.getName(), message);
return;
}

            Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null) {
                IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
                if (target == null) {
                    displayError("The project target is not set.");
                    return;
}

                AndroidTargetData data = currentSdk.getTargetData(target);
                if (data == null) {
                    // It can happen that the workspace refreshes while the SDK is loading its
                    // data, which could trigger a redraw of the opened layout if some resources
                    // changed while Eclipse is closed.
                    // In this case data could be null, but this is not an error.
                    // We can just silently return, as all the opened editors are automatically
                    // refreshed once the SDK finishes loading.
                    LoadStatus targetLoadStatus = currentSdk.checkAndLoadTargetData(target, null);
                    switch (targetLoadStatus) {
                        case LOADING:
                            displayError("The project target (%1$s) is still loading.\n%2$s will refresh automatically once the process is finished.",
                                    target.getName(), mEditedFile.getName());

                            break;
                        case FAILED: // known failure
                        case LOADED: // success but data isn't loaded?!?!
                            displayError("The project target (%s) was not properly loaded.",
                                    target.getName());
                            break;
                    }

                    return;
                }

                // check there is actually a model (maybe the file is empty).
                UiDocumentNode model = getModel();

                if (model.getUiChildren().size() == 0) {
                    displayError(
                            "No XML content. Please add a root view or layout to your document.");

                    // Although we display an error, we still treat an empty document as a
                    // successful layout result so that we can drop new elements in it.
                    //
                    // For that purpose, create a special ILayoutResult that has no image,
                    // no root view yet indicates success and then update the canvas with it.

                    ILayoutResult result = new ILayoutResult() {
                        public String getErrorMessage() {
                            return null;
                        }

                        public BufferedImage getImage() {
                            return null;
                        }

                        public ILayoutViewInfo getRootView() {
                            return null;
                        }

                        public int getSuccess() {
                            return ILayoutResult.SUCCESS;
                        }
                    };

                    mCanvasViewer.getCanvas().setResult(result, null /*explodeNodes*/);
                    return;
                }

                LayoutBridge bridge = data.getLayoutBridge();

                if (bridge.bridge != null) { // bridge can never be null.
                    // if drawing in real size, (re)set the scaling factor.
                    if (mZoomRealSizeButton.getSelection()) {
                        computeAndSetRealScale(false /*redraw*/);
                    }

                    renderWithBridge(iProject, model, bridge);
                } else {
                    // SDK is loaded but not the layout library!

                    // check whether the bridge managed to load, or not
                    if (bridge.status == LoadStatus.LOADING) {
                        displayError("Eclipse is loading framework information and the layout library from the SDK folder.\n%1$s will refresh automatically once the process is finished.",
                                     mEditedFile.getName());
                    } else {
                        displayError("Eclipse failed to load the framework information and the layout library!");
                    }
                }
            } else {
                displayError("Eclipse is loading the SDK.\n%1$s will refresh automatically once the process is finished.",
                             mEditedFile.getName());
}
} finally {
// no matter the result, we are done doing the recompute based on the latest
//Synthetic comment -- @@ -1119,13 +1050,170 @@
}
}

private void renderWithBridge(IProject iProject, UiDocumentNode model, LayoutBridge bridge) {
ResourceManager resManager = ResourceManager.getInstance();

ProjectResources projectRes = resManager.getProjectResources(iProject);
if (projectRes == null) {
displayError("Missing project resources.");
            return;
}

// Get the resources of the file's project.
//Synthetic comment -- @@ -1183,14 +1271,9 @@
displayError("Missing theme.");
}

        // Compute the layout
        Rectangle rect = getBounds();

        int width = rect.width;
        int height = rect.height;
if (mUseExplodeMode) {
// compute how many padding in x and y will bump the screen size
            List<UiElementNode> children = getModel().getUiChildren();
if (children.size() == 1) {
ExplodedRenderingHelper helper = new ExplodedRenderingHelper(
children.get(0).getXmlNode(), iProject);
//Synthetic comment -- @@ -1209,10 +1292,7 @@
float ydpi = mConfigComposite.getYDpi();
boolean isProjectTheme = mConfigComposite.isProjectTheme();

        LayoutCanvas canvas = getCanvasControl();
        Set<UiElementNode> explodeNodes = canvas.getNodesToExplode();

        UiElementPullParser parser = new UiElementPullParser(getModel(),
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

ILayoutResult result = computeLayout(bridge, parser,
//Synthetic comment -- @@ -1226,27 +1306,7 @@
// post rendering clean up
bridge.cleanUp();

        canvas.setResult(result, explodeNodes);

        // update the UiElementNode with the layout info.
        if (result.getSuccess() != ILayoutResult.SUCCESS) {
            // An error was generated. Print it.
            displayError(result.getErrorMessage());

        } else {
            // Success means there was no exception. But we might have detected
            // some missing classes and swapped them by a mock view.
            Set<String> missingClasses = mProjectCallback.getMissingClasses();
            if (missingClasses.size() > 0) {
                displayMissingClasses(missingClasses);
            } else {
                // Nope, no missing classes. Clear success, congrats!
                hideError();
            }

        }

        model.refreshUi();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 71b1605..0087d97 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

//Synthetic comment -- @@ -74,14 +75,16 @@
private ScrollBar mVBar;
private ControlListener mControlListener;
private Listener mScrollbarListener;

/**
* Create the composite.
* @param parent The parent composite.
*/
    public PaletteComposite(Composite parent) {
super(parent, SWT.BORDER | SWT.V_SCROLL);

mVBar = getVerticalBar();

mScrollbarListener = new Listener() {







