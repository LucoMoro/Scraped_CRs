/*fix typo in docs

description of 'android/hardware/Camera.Parameters.html#
    setPreviewFpsRange(int, int)' not right

Change-Id:Ic83c0f404710a0125138df1e2b7663994301aee7*/
//Synthetic comment -- diff --git a/core/java/android/hardware/Camera.java b/core/java/android/hardware/Camera.java
//Synthetic comment -- index a300776..483e9de 100644

//Synthetic comment -- @@ -2338,7 +2338,7 @@
}

/**
         * Sets the maximum and maximum preview fps. This controls the rate of
* preview frames received in {@link PreviewCallback}. The minimum and
* maximum preview fps must be one of the elements from {@link
* #getSupportedPreviewFpsRange}.







