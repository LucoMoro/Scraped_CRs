/*Fix empty gallery not checking for new images on app restart.

    Steps To Reproduce:
    1. Start with empty SD card.
    2. Launch gallery. Should be empty.
    3. Use home button to stop the Gallery and go to home screen.
    4. Launch camera. Take a picture.
    5. Go back to gallery. No new images are found.

    Currently onCreate() calls sendInitialMessage() leading to logic which
    checks for new images. Consequently, a restart does not check for new images.

    This patch moves sendInitialMessage() to onStart() to always check for new images.

Change-Id:I1358a401098d3c57df79533bc9541c78337218b0*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/Gallery.java b/src/com/cooliris/media/Gallery.java
//Synthetic comment -- index bfa2575..1e2fe88 100644

//Synthetic comment -- @@ -152,7 +152,6 @@
}
};

        sendInitialMessage();

Log.i(TAG, "onCreate");
}
//Synthetic comment -- @@ -180,6 +179,7 @@

@Override
public void onStart() {
super.onStart();
}








