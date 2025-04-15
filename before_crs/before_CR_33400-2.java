/*CTS Holo: Add +/- tolerance to bitmap comparison

This will allow CTS Holo to pass as long as the bitmap color value is within +/-tolerance.

Change-Id:I75c7c73b60abac0cbac3e15039e5dbed740ceb8eSigned-off-by: Jack Yen <jyen@ti.com>
Signed-off-by: Jack Yen <ckrston@gmail.com>*/
//Synthetic comment -- diff --git a/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java b/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java
//Synthetic comment -- index 88b9e5a..f30fbb8 100644

//Synthetic comment -- @@ -33,6 +33,7 @@

import java.io.FileNotFoundException;
import java.io.IOException;

/**
* {@link Activity} that applies a theme, inflates a layout, and then either
//Synthetic comment -- @@ -160,7 +161,51 @@
protected void onPreExecute() {
mBitmap = getBitmap();
mReferenceBitmap = BitmapAssets.getBitmap(getApplicationContext(), mBitmapName);
            mSame = mBitmap.sameAs(mReferenceBitmap);
}

@Override







