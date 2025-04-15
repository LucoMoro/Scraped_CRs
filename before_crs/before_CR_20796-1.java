/*Fix compilation errors due to 2 independent changes.

Change-Id:Ib7aa41aa6c95ec17ffde2644320b851f8f02f85f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4e66bb0..8e94358 100644

//Synthetic comment -- @@ -1703,11 +1703,11 @@
ResourceResolver createResolver() {
String theme = mConfigComposite.getTheme();
boolean isProjectTheme = mConfigComposite.isProjectTheme();
        Map<String, Map<String, ResourceValue>> configuredProjectRes =
mConfigListener.getConfiguredProjectResources();

// Get the framework resources
        Map<String, Map<String, ResourceValue>> frameworkResources =
mConfigListener.getConfiguredFrameworkResources();

return ResourceResolver.create(







