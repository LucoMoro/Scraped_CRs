BUG FIX -http://code.google.com/p/android/issues/detail?id=955Fixed AlertDialog.Builder setMultiChoiceItems losing checked state for invisible items in UI
when backed by a Cursor. Please refer to the issue tracker for more details.
diff --git a/core/java/com/android/internal/app/AlertController.java b/core/java/com/android/internal/app/AlertController.java
index 53b9654..2b54706 100644

@@ -784,6 +784,9 @@
Cursor cursor) {
CheckedTextView text = (CheckedTextView) view.findViewById(R.id.text1);
text.setText(cursor.getString(cursor.getColumnIndexOrThrow(mLabelColumn)));
}

@Override
@@ -792,9 +795,6 @@
View view = mInflater.inflate(
R.layout.select_dialog_multichoice, parent, false);
bindView(view, context, cursor);
                            if (cursor.getInt(cursor.getColumnIndexOrThrow(mIsCheckedColumn)) == 1) {
                                listView.setItemChecked(cursor.getPosition(), true);
                            }
return view;
}








