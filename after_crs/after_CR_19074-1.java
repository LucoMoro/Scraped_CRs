/*Remove a magic number and make the tap position to be scalable

Change-Id:I83ff769ea07d0e6473f549afe33824722bcc9660*/




//Synthetic comment -- diff --git a/src/com/android/wallpaper/nexus/NexusRS.java b/src/com/android/wallpaper/nexus/NexusRS.java
old mode 100644
new mode 100755
//Synthetic comment -- index 88f3a0e..2806d13

//Synthetic comment -- @@ -282,16 +282,14 @@
public Bundle onCommand(String action, int x, int y, int z, Bundle extras,
boolean resultRequested) {

if (mWorldState.rotate == 0) {
// nexus.rs ignores the xOffset when rotated; we shall endeavor to do so as well
            x = (int) (x + mWorldState.xOffset * mWorldState.width);
}

// android.util.Log.d("NexusRS", String.format(
        //     "width=%d, xOffset=%g, x=%d",
        //     mWorldState.width, mWorldState.xOffset, x));

if ("android.wallpaper.tap".equals(action)) {
sendCommand(1, x, y);







