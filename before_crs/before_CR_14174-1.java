/*Gallery: Added routine to process KeyEvent.KEYCODE_ENTER

Change-Id:I6cd209002710e20ad151a8ce8305c132ad194e38*/
//Synthetic comment -- diff --git a/src/com/android/camera/ImageGallery.java b/src/com/android/camera/ImageGallery.java
//Synthetic comment -- index e62c05b..a18f79e 100644

//Synthetic comment -- @@ -264,6 +264,9 @@
this, mDeletePhotoRunnable, getCurrentImage());
}
return true;
}
return super.onKeyDown(keyCode, event);
}







