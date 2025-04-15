/*Prevent potential NPE

(cherry picked from commit db02b1f6e4960737494f45da43b58b22cf6167dc)

Change-Id:I7c1324d49d00ddc76762dbc03339e724d1f4e94d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 95722c5..5a926e8 100644

//Synthetic comment -- @@ -515,7 +515,8 @@
disposeThumbnail();

Configuration configuration =
                mAlternateInput != null ? mAlternateConfiguration : mConfiguration;
ResourceResolver resolver = getResourceResolver(configuration);
RenderService renderService = RenderService.create(editor, configuration, resolver);








