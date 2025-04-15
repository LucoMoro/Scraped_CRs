/*Defect 7926 : Fixed to view notes data fully

Change-Id:Ic7caa592d0a9e20ac1e08afbbd891118b3722b4b*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index ca3c08a..2e54d61 100644

//Synthetic comment -- @@ -1237,7 +1237,6 @@
textView.setEllipsize(TextUtils.TruncateAt.END);
} else {
textView.setSingleLine(false);
                textView.setMaxLines(maxLines);
textView.setEllipsize(null);
}
}







