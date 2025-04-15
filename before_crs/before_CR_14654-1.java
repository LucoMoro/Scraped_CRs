/*Defect Fix : 7926 : Not able to view notes data fully in Contact Details View.

Change-Id:Iee40d31e9ab01de4ad8a42381ec1c32d14d91916*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index ca3c08a..6b9ff79 100644

//Synthetic comment -- @@ -1237,7 +1237,7 @@
textView.setEllipsize(TextUtils.TruncateAt.END);
} else {
textView.setSingleLine(false);
                textView.setMaxLines(maxLines);
textView.setEllipsize(null);
}
}







