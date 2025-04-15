/*Fixing gallery app crashes when receiving invalid intent.

Change-Id:I75612575b50eda42d115b2672deca196fda2cc00Related-Issue:http://code.google.com/p/android/issues/detail?id=42985*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/app/Gallery.java b/src/com/android/gallery3d/app/Gallery.java
//Synthetic comment -- index e28404f..249a2f5 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.data.DataManager;
import com.android.gallery3d.data.MediaItem;
import com.android.gallery3d.data.MediaSet;
import com.android.gallery3d.data.Path;
import com.android.gallery3d.picasasource.PicasaSource;
//Synthetic comment -- @@ -186,7 +187,14 @@
Path setPath = dm.findPathByUri(uri, null);
MediaSet mediaSet = null;
if (setPath != null) {
                    mediaSet = (MediaSet) dm.getMediaObject(setPath);
}
if (mediaSet != null) {
if (mediaSet.isLeafAlbum()) {







