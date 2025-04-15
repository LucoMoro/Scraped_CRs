/*Catch OOM Ex when generated bitmap

Change-Id:Ia0821e79728a87e4698b2a5c64f1ba7919b008e2Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/browser/PhoneUi.java b/src/com/android/browser/PhoneUi.java
//Synthetic comment -- index 89eae70..1e408f9 100644

//Synthetic comment -- @@ -520,7 +520,13 @@
+ ", height: " + height);
return null;
}
            return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
}

public void set(Bitmap image) {







