/*Catch OOM when generated thumbnail bitmap

Change-Id:I569639f4c9a5c7cba097cffce51064fbb1dfcd21Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index caea83e..3cd60b7 100644

//Synthetic comment -- @@ -2064,9 +2064,15 @@
sThumbnailBitmap.recycle();
sThumbnailBitmap = null;
}
            sThumbnailBitmap =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.RGB_565);
}
Canvas canvas = new Canvas(sThumbnailBitmap);
int contentWidth = view.getContentWidth();
float overviewScale = scaledWidth / (view.getScale() * contentWidth);







