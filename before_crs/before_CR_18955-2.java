/*Check the parent for null in deleteEditor

There is a reproducible error in Contacts when the delete
action is triggered more than once and thereby crashing the
app that is fixed by addeding a null check of parent.

Change-Id:I6de29ed0139a5fb377a845da3194475b02a86ee3*/
//Synthetic comment -- diff --git a/src/com/android/contacts/editor/LabeledEditorView.java b/src/com/android/contacts/editor/LabeledEditorView.java
//Synthetic comment -- index 2a1ec5e..2a97f4c 100644

//Synthetic comment -- @@ -177,7 +177,10 @@
mEntry.markDeleted();

// Remove the view
        ((ViewGroup) getParent()).removeView(LabeledEditorView.this);
}

public boolean isReadOnly() {







