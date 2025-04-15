/*Fix incorrect assertion

The assertion was missing the case that with nested layouts,
we display a label between the sets of tool buttons, and these
do not correspond to a rule action, so there is no associated
widget data.

Change-Id:Ied75a08f5bb9d76109b0b4b67722f82485f52000*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 9898a89..02aea9b 100644

//Synthetic comment -- @@ -252,7 +252,8 @@
item.setSelection(selected);
}
} else {
                // Must be a separator, or a label (which we insert for nested widgets)
                assert (style & SWT.SEPARATOR) != 0 || !item.getText().isEmpty() : item;
}
}








