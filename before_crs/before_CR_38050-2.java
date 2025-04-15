/*Ensure that proper video details are shown in Gallery

This change fixes the following bug:

1. Go to Gallery App and select a folder with more than 3 videos.
2. Tap and hold one of the videos until it is highlighted.
3. Continue selecting the first 3 videos.
4. Tap and hold the videos in the opposite order now, only one video should remain highlighted.
5. Tap the menu button at the top right corner and select details.
6. The details for the last unselected video are shown instead of the highlighted video.

Change-Id:I3722750a3cde3a6868d517dbc446387897213e03Signed-off-by: Yuriy Zabroda <yuriy.zabroda@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/app/AlbumPage.java b/src/com/android/gallery3d/app/AlbumPage.java
//Synthetic comment -- index 6fb4143..1d24d59 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.GalleryUtils;

import java.util.Random;

public class AlbumPage extends ActivityState implements GalleryActionBar.ClusterRunner,
//Synthetic comment -- @@ -190,6 +191,11 @@
}
} else {
mSelectionManager.toggle(item.getPath());
mDetailsSource.findIndex(slotIndex);
mAlbumView.invalidate();
}
//Synthetic comment -- @@ -225,6 +231,11 @@
if (item == null) return;
mSelectionManager.setAutoLeaveSelectionMode(true);
mSelectionManager.toggle(item.getPath());
mDetailsSource.findIndex(slotIndex);
mAlbumView.invalidate();
}
//Synthetic comment -- @@ -602,6 +613,18 @@
}
}

private class MyLoadingListener implements LoadingListener {
@Override
public void onLoadingStarted() {







