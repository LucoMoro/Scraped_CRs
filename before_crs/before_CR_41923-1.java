/*Fix music crash when search song.

BZ: 26647

Change-Id:Ib65d7f505e60889c23b459b98425d52daf169629Author: b359 <b359@borqs.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26647*/
//Synthetic comment -- diff --git a/src/com/android/music/QueryBrowserActivity.java b/src/com/android/music/QueryBrowserActivity.java
//Synthetic comment -- index 977650c..fb29a3d 100644

//Synthetic comment -- @@ -210,7 +210,9 @@
// Because we pass the adapter to the next activity, we need to make
// sure it doesn't keep a reference to this activity. We can do this
// by clearing its DatasetObservers, which setListAdapter(null) does.
        setListAdapter(null);
mAdapter = null;
super.onDestroy();
}







