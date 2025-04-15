/*Remove one song from music playlist, move other songs position incorrect.

You can reproduce the issue with below actions:
Create one new playlist(example: mylist) ->
add some songs into mylist(more than 5 songs is good for reproduce) ->
Remove one song(not first and last one) from mylist ->
press song items left, move the item to other position, you may find that
the item not moved to what postion you want it to.
Reason: In media database table audio_playlists_map, there is one attribute
named play_order, this attribute indicatethe songs play order in playlist
and also indicate the sort order in TrackBrowserActivity view. when create
a new playlist, system will store songs information in audio_playlists_map
table. When you remove songs from playlist, this item information will be
removed. For example: there is five songs, play_order is 0,1,2,3,4,
you removed thrid song from playlist, the play_order become 0,1,3,4.
But in Music source code, when you move item to other positon,
source code is use playlist listview position, that means if you change
frist song to third postion, play_order 0 will be become 2, so the
play_order becomes 1,2,3,4, the song which you want to move to third position
becomes second position, so we need movement operation use database cursor
data, not listview postion.*/
//Synthetic comment -- diff --git a/src/com/android/music/TrackBrowserActivity.java b/src/com/android/music/TrackBrowserActivity.java
//Synthetic comment -- index 9f77e50..b8d81e2 100644

//Synthetic comment -- @@ -529,6 +529,10 @@
getListView().invalidateViews();
mDeletedOneRow = true;
} else {
// update a saved playlist
MediaStore.Audio.Playlists.Members.moveItem(getContentResolver(),
Long.valueOf(mPlaylist), from, to);







