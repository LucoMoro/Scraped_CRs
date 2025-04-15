/*Check the parent for null in edit_delete

There is a reproducible error in Contacts when the delete
action is triggered more than once and thereby crashing the
app that is fixed by addeding a null check of parent.

Also preventing the mListener.onDeleted from getting mutiple
callbacks.

Change-Id:I6de29ed0139a5fb377a845da3194475b02a86ee3*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ui/widget/GenericEditorView.java b/src/com/android/contacts/ui/widget/GenericEditorView.java
//Synthetic comment -- index 24262bb..e4b985d 100644

//Synthetic comment -- @@ -385,11 +385,12 @@

// Remove editor from parent view
final ViewGroup parent = (ViewGroup)getParent();
                parent.removeView(this);

                if (mListener != null) {
                    // Notify listener when present
                    mListener.onDeleted(this);
}
break;
}







