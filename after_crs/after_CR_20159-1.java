/*Fix NPE in layout lib legacy conversion for empty layouts.

Change-Id:I23a87efb586d02d571118a6f9e49628fc850d8cb*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index c9d9f13..a961135 100644

//Synthetic comment -- @@ -428,14 +428,16 @@
private RenderSession convertToScene(ILayoutResult result) {

Result sceneResult;
        ViewInfo rootViewInfo = null;

if (result.getSuccess() == ILayoutResult.SUCCESS) {
sceneResult = Status.SUCCESS.createResult();
            ILayoutViewInfo oldRootView = result.getRootView();
            if (oldRootView != null) {
                rootViewInfo = convertToViewInfo(oldRootView);
            }
} else {
sceneResult = Status.ERROR_UNKNOWN.createResult(result.getErrorMessage());
}

// create a BasicLayoutScene. This will return the given values but return the default







