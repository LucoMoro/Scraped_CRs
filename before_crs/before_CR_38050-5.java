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
//Synthetic comment -- index e773691..8684bf8 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.gallery3d.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -705,9 +707,28 @@

// If requested index is out of active window, suggest a valid index.
// If there is no valid index available, return -1.
public int findIndex(int indexHint) {
if (mAlbumDataAdapter.isActive(indexHint)) {
mIndex = indexHint;
} else {
mIndex = mAlbumDataAdapter.getActiveStart();
if (!mAlbumDataAdapter.isActive(mIndex)) {







