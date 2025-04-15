/*Ensure that the configuration bar is visible

This handles a scenario which can come up if you
switch the SDK installation area, or start the
IDE with a project that does not have a valid
project property setting pointing to a valid
platform.

(cherry picked from commit cbfe6c5f0a012d5987b79ddb717842af61b636d0)

Change-Id:Ieac467305b5f211932a85c8243e8f87bb8b86f82*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index ea66ac4..750c192 100644

//Synthetic comment -- @@ -537,6 +537,8 @@
selectConfiguration(mConfiguration.getEditedConfig());
updateActivity();
}
            } else if (sdkStatus == LoadStatus.FAILED) {
                setVisible(true);
}
} finally {
mDisableUpdates--;
//Synthetic comment -- @@ -808,6 +810,8 @@

// compute the final current config
mConfiguration.syncFolderConfig();
                } else if (targetStatus == LoadStatus.FAILED) {
                    setVisible(true);
}
} finally {
mDisableUpdates--;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 78e670e..95722c5 100644

//Synthetic comment -- @@ -505,11 +505,17 @@

/** Render immediately */
private void renderSync() {
        GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
        if (editor.getReadyLayoutLib(false /*displayError*/) == null) {
            // Don't attempt to render when there is no ready layout library: most likely
            // the targets are loading/reloading.
            return;
        }

disposeThumbnail();

Configuration configuration =
mAlternateInput != null ? mAlternateConfiguration : mConfiguration;
ResourceResolver resolver = getResourceResolver(configuration);
RenderService renderService = RenderService.create(editor, configuration, resolver);








