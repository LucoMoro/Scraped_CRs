/*Gallery: Added routine to process Enter KeyEvent.

Change-Id:I4e694a35e85e87f24b35cc4856dd9ea16e663778*/




//Synthetic comment -- diff --git a/src/com/android/camera/ImageGallery.java b/src/com/android/camera/ImageGallery.java
//Synthetic comment -- index e62c05b..4c61d5e 100644

//Synthetic comment -- @@ -264,10 +264,27 @@
this, mDeletePhotoRunnable, getCurrentImage());
}
return true;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (event.getRepeatCount() == 0) {
                    event.startTracking();
                }
                return true;
}
return super.onKeyDown(keyCode, event);
}

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_ENTER
             || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
            && event.isTracking() && !event.isCanceled()) {
            onImageClicked(mGvs.getCurrentSelection());
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

private boolean isPickIntent() {
String action = getIntent().getAction();
return (Intent.ACTION_PICK.equals(action)







