/*Fix misc minor issues

Fixes some NPEs and avoid logging a common issue
(which is already listed inline in the layout editor
error display).

Change-Id:I6a62f37ec845ca081e1dc7fc8ad857a7ec4e9a29*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index 8f50001..89dd263 100644

//Synthetic comment -- @@ -472,7 +472,10 @@
}

if (job != null) {
            GraphicalEditorPart graphicalEditor = getGraphicalEditor();
            if (graphicalEditor != null) {
                job.addJobChangeListener(new LintJobListener(graphicalEditor));
            }
}
return job;
}
//Synthetic comment -- @@ -483,7 +486,7 @@

LintJobListener(GraphicalEditorPart editor) {
mEditor = editor;
            mCanvas = editor.getCanvasControl();
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 1fa3f06..5330752 100644

//Synthetic comment -- @@ -1020,7 +1020,11 @@
}

private IAndroidTarget getSelectedTarget() {
        if (!mTargetCombo.isDisposed()) {
            return (IAndroidTarget) mTargetCombo.getData();
        }

        return null;
}

void selectTheme(String theme) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 954305e..2154179 100644

//Synthetic comment -- @@ -663,7 +663,11 @@
*            rendered image, but it will never zoom in.
*/
void setFitScale(boolean onlyZoomOut) {
        ImageOverlay imageOverlay = getImageOverlay();
        if (imageOverlay == null) {
            return;
        }
        Image image = imageOverlay.getImage();
if (image != null) {
Rectangle canvasSize = getClientArea();
int canvasWidth = canvasSize.width;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 74bc4f5..9b0d7a6 100644

//Synthetic comment -- @@ -144,15 +144,20 @@
@Override
public void warning(String tag, String message, Object data) {
String description = describe(message);

        boolean log = true;
if (TAG_RESOURCES_FORMAT.equals(tag)) {
if (description.equals("You must supply a layout_width attribute.")       //$NON-NLS-1$
|| description.equals("You must supply a layout_height attribute.")) {//$NON-NLS-1$
tag = TAG_MISSING_DIMENSION;
                log = false;
}
}

        if (log) {
            AdtPlugin.log(IStatus.WARNING, "%1$s: %2$s", mName, description);
        }

addWarning(tag, description);
}








