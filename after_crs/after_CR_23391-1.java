/*Fix invalid caching of project callback

Change-Id:If7c0ee87e5263767e607cf226838e65667a5106c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 88624bc..41017a0 100644

//Synthetic comment -- @@ -1425,11 +1425,10 @@
}
}

        // Also remove the ProjectCallback as it caches custom views which must be reloaded
        // with the classloader of the new LayoutLib. We also have to clear it out
        // because it stores a reference to the layout library which could have changed.
        mProjectCallback = null;

// FIXME: get rid of the current LayoutScene if any.
}







