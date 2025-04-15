/*Gallery: Added routine to process Enter KeyEvent.

Change-Id:I23ec3041652da8c29120ec3b06a1ff687f1c85fe*/




//Synthetic comment -- diff --git a/src/com/android/camera/ImageGallery.java b/src/com/android/camera/ImageGallery.java
//Synthetic comment -- index e62c05b..b1b4cea 100644

//Synthetic comment -- @@ -265,9 +265,26 @@
}
return true;
}
        if ((keyCode == KeyEvent.KEYCODE_ENTER
             || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
            && event.getRepeatCount() == 0) {
            event.startTracking();
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







