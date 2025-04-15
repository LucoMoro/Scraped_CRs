/*For title bar refresh when changing layout file in same editor.

Bug:http://code.google.com/p/android/issues/detail?id=13230Change-Id:Ib9211a2512a47ba87f1ceceb97c86a47579a8872*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index a520fdf..17d6ddf 100644

//Synthetic comment -- @@ -274,6 +274,10 @@
// re-create or reload the pages with the default page shown as the previous active page.
createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

        // When changing an input file of an the editor, the titlebar is not refreshed to
        // show the new path/to/file being edited. So we force a refresh
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
}

/**







