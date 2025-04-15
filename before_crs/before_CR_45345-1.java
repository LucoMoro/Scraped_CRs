/*Prevent potential NPE

Change-Id:Id2a5b2e73d70d11da9395b84488a75f4f793bf14*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 95722c5..5a926e8 100644

//Synthetic comment -- @@ -515,7 +515,8 @@
disposeThumbnail();

Configuration configuration =
                mAlternateInput != null ? mAlternateConfiguration : mConfiguration;
ResourceResolver resolver = getResourceResolver(configuration);
RenderService renderService = RenderService.create(editor, configuration, resolver);








