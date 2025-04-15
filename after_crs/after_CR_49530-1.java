/*Fix "Previous slideshow preview cann't be cleaned"

It maybe a ImageView bug that the resource in ImageView
cannot set to NULL by invoking setImageUri(null).

So the preview in SlideshowAttachmentView will be displayed
as the previous one until Slideshow model has changed.

Change-Id:Ib64318a58c8304ce9f84043a9429f15a5b3f5710Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowAttachmentView.java b/src/com/android/mms/ui/SlideshowAttachmentView.java
//Synthetic comment -- index 22dccef..03d50e5 100644

//Synthetic comment -- @@ -125,7 +125,7 @@
}

public void reset() {
        mImageView.setImageBitmap(null);
mTextView.setText("");
}








