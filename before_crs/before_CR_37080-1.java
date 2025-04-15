/*NPE fix.

Change-Id:I5885993d43b8edbf965314fe6f3248262c87a251*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 8b017f2..3524970 100644

//Synthetic comment -- @@ -261,7 +261,13 @@

/** Refresh all the icon state */
public void refreshIcons() {
        getTreeViewer().refresh();
}

/**







