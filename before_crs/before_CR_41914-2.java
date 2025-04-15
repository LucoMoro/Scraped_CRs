/*RS: Water Livewallpaper crashed after setting it to wallpaper several times.

Fix water livewallpaper null pointer bug

Change-Id:I5dd2ee48c78d884a5a6b4f607e25300631299cc4Author: b340 <b340@borqs.com>
Signed-off-by: edingX <erjunx.ding@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 29172*/
//Synthetic comment -- diff --git a/src/com/android/wallpaper/RenderScriptWallpaper.java b/src/com/android/wallpaper/RenderScriptWallpaper.java
//Synthetic comment -- index 0016d33..fe1f21d 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
@Override
public void onOffsetsChanged(float xOffset, float yOffset,
float xStep, float yStep, int xPixels, int yPixels) {
            mRenderer.setOffset(xOffset, yOffset, xPixels, yPixels);
}

@Override
//Synthetic comment -- @@ -110,7 +110,11 @@
@Override
public Bundle onCommand(String action, int x, int y, int z,
Bundle extras, boolean resultRequested) {
            return mRenderer.onCommand(action, x, y, z, extras, resultRequested);
}

}







