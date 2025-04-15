/*Defect Fix : 7926 : Not able to view notes data fully in contact details screen.

Change-Id:Ica21482558893992eefb6747013c9b6950eaaca5*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index ca3c08a..6b9ff79 100644

//Synthetic comment -- @@ -1237,7 +1237,7 @@
textView.setEllipsize(TextUtils.TruncateAt.END);
} else {
textView.setSingleLine(false);
                //textView.setMaxLines(maxLines);
textView.setEllipsize(null);
}
}







