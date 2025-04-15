/*Fix Linux first-time rendering

On Linux, the first time a screen size or theme is rendered, the wrong
image is shown (until the scene is re-rendered). The reason for this
is that the first time, the palette previews are generated. And this
has the side-effect of replacing the current render session for the
editor with the most recent render image from the palette preview.

The fix is simple: After rendering previews, trigger another render.

Change-Id:Ic00fa8b2703c4ce8ad4068412d5eea7b0b9f093c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index e80ad4a..be7ceb8 100644

//Synthetic comment -- @@ -363,6 +363,8 @@
}
}

return true;
}








