/*Corrected message size calculation when replacing resizable media

Replacing resizable media in Slideshow will yield in an incorrect
message size. An exception will be thrown when validating the size and
the application will crash. This change fixes this problem.*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/SlideModel.java b/src/com/android/mms/model/SlideModel.java
//Synthetic comment -- index 8eff7d6..5ea90e0 100644

//Synthetic comment -- @@ -151,7 +151,7 @@
increaseSlideSize(addSize);
increaseMessageSize(addSize);
} else {
            removeSize = old.getMediaSize();
if (addSize > removeSize) {
if (null != mParent) {
mParent.checkMessageSize(addSize - removeSize);







