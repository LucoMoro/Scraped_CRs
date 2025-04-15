/*frameworks/base: remove redundant code in WindowManager

Change-Id:I8a356ca36129645977d33129e0d56c1b89f97fb0*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowManager.java b/core/java/android/view/WindowManager.java
//Synthetic comment -- index 091844e..d6dcd4c 100644

//Synthetic comment -- @@ -1050,14 +1050,6 @@
gravity = o.gravity;
changes |= LAYOUT_CHANGED;
}
if (format != o.format) {
format = o.format;
changes |= FORMAT_CHANGED;







