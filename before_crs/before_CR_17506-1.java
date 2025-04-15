/*Apps/Music: Fix memory leak due to excessive JNI global references

When the Music app is destroyed the adapter cursor is not closed,
due to which cursor leaks are observed everytime music application
is started. Close the adapter cursor onDestory when the adapter is
not sent to another activity to avoid cursor leaks and Jni global
references

Change-Id:Ie8e7a31ff872486f93bfe7cff595ac81f0bdb489*/
//Synthetic comment -- diff --git a/src/com/android/music/AlbumBrowserActivity.java b/src/com/android/music/AlbumBrowserActivity.java
//Synthetic comment -- index 96dfd31..3d61352 100644

//Synthetic comment -- @@ -169,7 +169,8 @@
// instead of closing the cursor directly keeps the framework from accessing
// the closed cursor later.
if (!mAdapterSent && mAdapter != null) {
            mAdapter.changeCursor(null);
}
// Because we pass the adapter to the next activity, we need to make
// sure it doesn't keep a reference to this activity. We can do this








//Synthetic comment -- diff --git a/src/com/android/music/ArtistAlbumBrowserActivity.java b/src/com/android/music/ArtistAlbumBrowserActivity.java
//Synthetic comment -- index 3bb55cb..c41f6b5 100644

//Synthetic comment -- @@ -171,7 +171,8 @@
// instead of closing the cursor directly keeps the framework from accessing
// the closed cursor later.
if (!mAdapterSent && mAdapter != null) {
            mAdapter.changeCursor(null);
}
// Because we pass the adapter to the next activity, we need to make
// sure it doesn't keep a reference to this activity. We can do this








//Synthetic comment -- diff --git a/src/com/android/music/PlaylistBrowserActivity.java b/src/com/android/music/PlaylistBrowserActivity.java
//Synthetic comment -- index 8c79e64..89aeb4c 100644

//Synthetic comment -- @@ -189,7 +189,8 @@
// instead of closing the cursor directly keeps the framework from accessing
// the closed cursor later.
if (!mAdapterSent && mAdapter != null) {
            mAdapter.changeCursor(null);
}
// Because we pass the adapter to the next activity, we need to make
// sure it doesn't keep a reference to this activity. We can do this








//Synthetic comment -- diff --git a/src/com/android/music/TrackBrowserActivity.java b/src/com/android/music/TrackBrowserActivity.java
//Synthetic comment -- index b4d8516..47d4b90 100644

//Synthetic comment -- @@ -275,7 +275,8 @@
// instead of closing the cursor directly keeps the framework from accessing
// the closed cursor later.
if (!mAdapterSent && mAdapter != null) {
            mAdapter.changeCursor(null);
}
// Because we pass the adapter to the next activity, we need to make
// sure it doesn't keep a reference to this activity. We can do this







