/*Reset the ProjectCallback when the rendering target changes.

This is only done if the layout has custom views. This is
because ProjectCallback caches classes, but they are loaded
using the layoutlib classloader for type provided by the
layoutlib.

Since the rendering target changed, the layoutlib changed,
and we need a version of the custom view classes that uses
the base types of the new layoutlib jar.

Change-Id:Ia3e2a8f65d73dab2769d8c7686f970e647432ed3http://code.google.com/p/android/issues/detail?id=14061*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 73ad04a..6cadd05 100644

//Synthetic comment -- @@ -1683,6 +1683,12 @@
}
}

// FIXME: get rid of the current LayoutScene if any.
}








