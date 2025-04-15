/*ADT: Make GLE2 the default.

Export USE_GLE1 to something != 0 to force GLE1.

Change-Id:I525bc276f3ad096d6ae64508f4c9808f52684b02*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index ab3b1f5..0195105 100644

//Synthetic comment -- @@ -182,12 +182,14 @@
// the user, or through a configuration change in the configuration selector.)
if (mGraphicalEditor == null) {

                    String useGle1 = System.getenv("USE_GLE1");     //$NON-NLS-1$

                    if (useGle1 != null && !useGle1.equals("0")) {  //$NON-NLS-1$
                        // If USE_GLE1 exists and is non-zero, use the old GLE v1
mGraphicalEditor = new GraphicalLayoutEditor(this);
                    } else {
                        // Otherwise we now default to the new GLE v2
                        mGraphicalEditor = new GraphicalEditorPart(this);
}

mGraphicalEditorIndex = addPage(mGraphicalEditor, getEditorInput());







