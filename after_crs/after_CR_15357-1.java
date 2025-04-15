/*Revert "Fix for bug # 2337042  URL that starts with "/.""

This reverts commit 7065eb1651a43d2c3d80d74fe072b0c4841674a1.*/




//Synthetic comment -- diff --git a/core/java/android/webkit/URLUtil.java b/core/java/android/webkit/URLUtil.java
//Synthetic comment -- index 075a3a9..232ed36 100644

//Synthetic comment -- @@ -56,14 +56,6 @@
if (inUrl.endsWith(".") == true) {
inUrl = inUrl.substring(0, inUrl.length() - 1);
}

try {
webAddress = new WebAddress(inUrl);







