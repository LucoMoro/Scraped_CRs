/*When use ldpi,the sw must recalculated by density

Change-Id:I1ae3939bd2496c56422647e73cb3a6714be6d8ca*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index c833919..5f4b8bf 100755

//Synthetic comment -- @@ -5973,7 +5973,8 @@
unrotDw = dw;
unrotDh = dh;
}
        int sw = reduceConfigWidthSize(unrotDw, Surface.ROTATION_0, density, unrotDw, unrotDh);
sw = reduceConfigWidthSize(sw, Surface.ROTATION_90, density, unrotDh, unrotDw);
sw = reduceConfigWidthSize(sw, Surface.ROTATION_180, density, unrotDw, unrotDh);
sw = reduceConfigWidthSize(sw, Surface.ROTATION_270, density, unrotDh, unrotDw);







