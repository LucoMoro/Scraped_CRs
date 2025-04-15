/*Use transformend size to get bitmap in slideshow

Change-Id:I0ff77c87147eef0908275649b88f5a6a82c554abSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowPresenter.java b/src/com/android/mms/ui/SlideshowPresenter.java
//Synthetic comment -- index 64af07b..acb7a01 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
}

if (dataChanged) {
            view.setImage(image.getSrc(), image.getBitmap(transformedWidth, transformedHeight));
}

if (view instanceof AdaptableSlideViewInterface) {







