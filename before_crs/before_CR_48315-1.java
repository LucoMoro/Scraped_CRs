/*Music: [Graphics]The music of monkey crashed while running it.

check cursor status when use cursor, strong codes.

Change-Id:I0281dcd0961363ad6d379e8ee82a629ed3c719eeAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: b359 <b359@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51786*/
//Synthetic comment -- diff --git a/src/com/android/music/TrackBrowserActivity.java b/src/com/android/music/TrackBrowserActivity.java
//Synthetic comment -- index fc9b0f9..e208a40 100644

//Synthetic comment -- @@ -1395,7 +1395,7 @@
protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//Log.i("@@@", "query complete: " + cursor.getCount() + "   " + mActivity);
mActivity.init(cursor, cookie != null);
                if (token == 0 && cookie != null && cursor != null && cursor.getCount() >= 100) {
QueryArgs args = (QueryArgs) cookie;
startQuery(1, null, args.uri, args.projection, args.selection,
args.selectionArgs, args.orderBy);







