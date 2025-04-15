/*Guard against a possible NPE when the rendering fails.

Change-Id:I8b0a0c65f2b0d7b7faf10224ac2945081605c5c4*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 0d6e5e3..5500279 100644

//Synthetic comment -- @@ -328,8 +328,10 @@
// Extended view info was requested but the layoutlib does not support it.
// Add it manually.
List<ViewInfo> infoList = session.getRootViews();
                for (ViewInfo info : infoList) {
                    addExtendedViewInfo(info);
}
}








