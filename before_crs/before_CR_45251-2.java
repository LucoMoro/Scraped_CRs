/*Ensure that the configuration bar is visible

This handles a scenario which can come up if you
switch the SDK installation area, or start the
IDE with a project that does not have a valid
project property setting pointing to a valid
platform.

Change-Id:I645033707a575d9da86b619c2f97787957b1200b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index ea66ac4..750c192 100644

//Synthetic comment -- @@ -537,6 +537,8 @@
selectConfiguration(mConfiguration.getEditedConfig());
updateActivity();
}
}
} finally {
mDisableUpdates--;
//Synthetic comment -- @@ -808,6 +810,8 @@

// compute the final current config
mConfiguration.syncFolderConfig();
}
} finally {
mDisableUpdates--;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 78e670e..95722c5 100644

//Synthetic comment -- @@ -505,11 +505,17 @@

/** Render immediately */
private void renderSync() {
disposeThumbnail();

Configuration configuration =
mAlternateInput != null ? mAlternateConfiguration : mConfiguration;
        GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver(configuration);
RenderService renderService = RenderService.create(editor, configuration, resolver);








