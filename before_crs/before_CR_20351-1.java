/*Use res/animator rather than res/anim for the Play Animation code

We should be using ResourceType.ANIMATOR rather than ResourceType.ANIM
for the designtime playable animations. Simple tweak to make the
"Create Animation" dialog able to create files into res/animator/,
though this needs to be improved (with proper animator/interpolator
root elements etc).

Change-Id:I70a504fbd946da3ff161f9cf953b878a41886fee*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index b1bd79f..878c50e 100644

//Synthetic comment -- @@ -16,13 +16,14 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AndroidConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_ANIM;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.IAnimationListener;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
//Synthetic comment -- @@ -116,7 +117,7 @@

// List of animations
Collection<String> animationNames = graphicalEditor.getResourceNames(mFramework,
                    ResourceType.ANIM);
if (animationNames.size() > 0) {
if (!mFramework) {
new Separator().fill(menu, -1);
//Synthetic comment -- @@ -158,7 +159,7 @@
if (viewObject != null) {
ViewHierarchy viewHierarchy = mCanvas.getViewHierarchy();
RenderSession session = viewHierarchy.getSession();
                session.animate(viewObject, mAnimationName, mIsFrameworkAnim,
new IAnimationListener() {
private boolean mPendingDrawing = false;

//Synthetic comment -- @@ -211,6 +212,12 @@
});
}
});
}
}
}
//Synthetic comment -- @@ -231,7 +238,7 @@
LayoutEditor editor = mCanvas.getLayoutEditor();
IWorkbenchWindow workbenchWindow = editor.getEditorSite().getWorkbenchWindow();
IWorkbench workbench = workbenchWindow.getWorkbench();
            String animationDir = FD_RESOURCES + WS_SEP + FD_ANIM;
Pair<IProject, String> pair = Pair.of(editor.getProject(), animationDir);
IStructuredSelection selection = new StructuredSelection(pair);
wizard.init(workbench, selection);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index fe62e26..b3f34aa 100644

//Synthetic comment -- @@ -1040,6 +1040,19 @@
}
}

if (matches.size() == 1) {
// If there's only one match, select it if it's not already selected
if (!selected) {







