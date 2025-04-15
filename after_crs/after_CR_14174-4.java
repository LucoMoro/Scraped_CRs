/*Gallery: Added routine to process Enter KeyEvent.

Change-Id:I32cbed1a3ac7349b59e65309e81d7dcad5b7eaa9*/




//Synthetic comment -- diff --git a/src/com/android/camera/ImageGallery.java b/src/com/android/camera/ImageGallery.java
//Synthetic comment -- index e62c05b..1b33cc8 100644

//Synthetic comment -- @@ -268,6 +268,16 @@
return super.onKeyDown(keyCode, event);
}

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            onImageClicked(mGvs.getCurrentSelection());
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

private boolean isPickIntent() {
String action = getIntent().getAction();
return (Intent.ACTION_PICK.equals(action)







